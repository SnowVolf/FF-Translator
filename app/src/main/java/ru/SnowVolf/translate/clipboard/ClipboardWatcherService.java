/*
 * Copyright (c) 2017 Snow Volf (Artem Zhiganov).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.SnowVolf.translate.clipboard;


import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.api.yandex.translate.Translate;
import ru.SnowVolf.translate.history.HistoryItem;
import ru.SnowVolf.translate.model.HistoryDbModel;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.util.runtime.Logger;

/**
 * An app private {@link Service} to listen for changes to the clipboard.
 */
public class ClipboardWatcherService extends Service implements
        ClipboardManager.OnPrimaryClipChangedListener {

    private static final String TAG = "ClipboardWatcherService";
    private static final String EXTRA_ON_BOOT = "on_boot";

    // ye olde ClipboardManager
    private ClipboardManager mClipboard = null;
    private String translated = "";

    /**
     * Start ourselves
     *
     * @param onBoot true if called on device boot
     *
     */
    public static void startService(Boolean onBoot) {
        if (!AppUtils.isMyServiceRunning(ClipboardWatcherService.class)) {
            // only start if the user has allowed it and we are not running
            final Context context = App.getContext();
            final Intent intent = new Intent(context, ClipboardWatcherService.class);
            intent.putExtra(EXTRA_ON_BOOT, onBoot);
            context.startService(intent);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Superclass overrides
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate() {
        mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboard.addPrimaryClipChangedListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final boolean onBoot = intent.getBooleanExtra(EXTRA_ON_BOOT, false);
            if (!onBoot) {
                // don't process on boot
                processClipboard(true);
            }
        } else {
            processClipboard(true);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClipboard.removePrimaryClipChangedListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Unimplemented onBind method in: " + TAG);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Implement ClipboardManager.OnPrimaryClipChangedListener
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onPrimaryClipChanged() {
        processClipboard(false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Read the clipboard, then write to database asynchronously.
     * DO NOT read clipboard in AsyncTask, you will regret it my boy.
     *
     * @param onNewOnly if true only update database if the current contents don't exit
     *
     */
    private void processClipboard(boolean onNewOnly) {
        final ClipItem clipItem = ClipItem.getFromClipboard(mClipboard);
        if ((clipItem == null) || TextUtils.isEmpty(clipItem.getText())) {
            return;
        }
        new StoreClipAsyncTask(clipItem).executeMe();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner classes
    ///////////////////////////////////////////////////////////////////////////

    /**
     * AsyncTask to write to the Clip database
     *
     */
    private class StoreClipAsyncTask extends ThreadedAsyncTask<String, String, String> {
        final ClipItem mClipItem;
        boolean mResult;

        StoreClipAsyncTask(ClipItem clipItem) {
            mClipItem = clipItem;
        }

        @Override
        protected String doInBackground(String... params) {
            mResult = ClipContentProvider.insert(mClipItem);
            if (mResult){
                try {
                    Preferences.setKey();
                    translated = Translate.execute(mClipItem.getText(), Language.fromString(Preferences.getFromLang()), Language.fromString(Preferences.getToLang()));
                    HistoryItem item = new HistoryItem(System.currentTimeMillis());
                    item.setFromPosition(Preferences.getSpinnerPosition(Constants.prefs.SPINNER_1));
                    item.setToPosition(Preferences.getSpinnerPosition(Constants.prefs.SPINNER_2));
                    item.setTitle(mClipItem.getText());
                    item.setSource(mClipItem.getText());
                    item.setTranslation(translated);
                    item.setFromCode(Preferences.getFromLang());
                    item.setToCode(Preferences.getFromLang());
                    if (translated != null) {
                        Logger.log("doInBackground :: \n" + translated);
                    }
                    HistoryDbModel model = new HistoryDbModel(App.getContext());
                    model.add(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return translated;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mResult) {
                // display notification if requested by user
                NotificationHelper.show(mClipItem, result);
            }
        }
    }
}
