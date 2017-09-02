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

package ru.SnowVolf.translate.ui.fragment.settings;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.favorite.FavoriteItem;
import ru.SnowVolf.translate.history.HistoryItem;
import ru.SnowVolf.translate.model.FavoriteDbModel;
import ru.SnowVolf.translate.model.HistoryDbModel;
import ru.SnowVolf.translate.net.NetworkStateHelper;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.util.Utils;

/**
 * Created by Snow Volf on 25.06.2017, 5:52
 */

public class DevSettingsFragment extends PreferenceFragment {
    public static final String FRAGMENT_TAG = "dev_settings_fragment";
    private int zoeGirlCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_dev);
        init();
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle("JVM Girl");

    }

    public void init(){
        findPreference("dev.dbg.info").setOnPreferenceClickListener(__ -> {
                String JackInfo =
                        "=== BEGIN GIRL LOG ===\n"
                                + "GIRL_VERSION = " + Constants.common.GIRL_ALIAS + "\n"
                                + "CURRENT_TIME = " + Utils.getNormalDate(System.currentTimeMillis()) + "\n"
                                + "APP_VERSION = " + BuildConfig.VERSION_NAME + "\n"
                                + "VERSION_CODE = " + BuildConfig.VERSION_CODE + "\n"
                                + "CONNECTED_NET = " + NetworkStateHelper.isAnyNetworkAvailable() + "\n"
                                + "CONNECTED_WIFI = " + NetworkStateHelper.isWifiAvailable() + "\n"
                                + "IS_BETA = " + BuildConfig.DEBUG + "\n"
                                + "SDK = " + Build.VERSION.SDK_INT + "\n"
                                + "PHONE_MODEL = " + Build.MANUFACTURER + ", " + Build.MODEL + "\n"
                                + "HISTORY_DB_VER = " + Constants.historyDb.DB_VERSION + "\n"
                                + "FAVORITE_DB_VER = " + Constants.favDb.DB_VERSION + "\n"
                                + "=== END GIRL LOG ===\n";
                new AlertDialog.Builder(getActivity())
                        .setTitle("GIRL_LOG")
                        .setMessage(JackInfo)
                        .setPositiveButton(R.string.ok, (d, w) -> d.dismiss())
                        .setNeutralButton(R.string.copy, (d, w) -> {
                            Utils.copyToClipboard(JackInfo);
                            Toast.makeText(getActivity(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
                        }).show();
                return true;
            });

        findPreference("dev.logging").setDefaultValue(BuildConfig.DEBUG);

        findPreference("dev.exception").setOnPreferenceClickListener(__ -> {
                throw new RuntimeException("Dev Exception");
            });
        findPreference("dev.sql.favorite").setOnPreferenceClickListener(__ -> {
            new AsyncFavorite().execute();
            return true;
        });
        findPreference("dev.sql.history").setOnPreferenceClickListener(__ -> {
            new AsyncHistory().execute();
            return true;
        });

        findPreference("dev.enablegirl").setOnPreferenceClickListener(__ -> {
            if (!Preferences.isGirlEnabled()){
                ++zoeGirlCount;
                if (zoeGirlCount >= 7){
                    Preferences.enableGirl();
                    zoeGirlCount = 0;
                }
            } else {
                Snackbar.make(getView(), "Girl enabled!", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private class AsyncFavorite extends AsyncTask<Void, Void, Void> {
        private FavoriteDbModel mDataHandler;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDataHandler = new FavoriteDbModel(App.ctx());
            Toast.makeText(getActivity(), "Please wait...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 51; i++) {
                mDataHandler.addItem(new FavoriteItem(System.currentTimeMillis(), 0, 0, "DevTest :: " + i, "DevTest :: " + i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "Completed", Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncHistory extends AsyncTask<Void, Void, Void> {
        private HistoryDbModel mDataHandler;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDataHandler = new HistoryDbModel(App.ctx());
            Toast.makeText(getActivity(), "Please wait...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 51; i++) {
                HistoryItem item = new HistoryItem(System.currentTimeMillis());
                item.setFromPosition(i + 1);
                item.setToPosition(i);
                item.setTitle(App.injectString(R.string.ipsum));
                item.setSource(App.injectString(R.string.ipsum));
                item.setTranslation(App.injectString(R.string.ipsum_translate));
                item.setFromCode(Preferences.getFromLang());
                item.setToCode(Preferences.getToLang());
                mDataHandler.add(item);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "Completed", Toast.LENGTH_SHORT).show();
        }
    }


}
