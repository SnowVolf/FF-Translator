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

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.ui.activity.HistoryFavActivity;
import ru.SnowVolf.translate.ui.activity.TranslatorActivity;

/**
 * Static class to manage our {@link android.app.Notification} objects
 */
public class NotificationHelper {
    private static final int ID_COPY = 10;

    // keep track of number of clipboard messages received.
    private static int sClipItemCt;

    private NotificationHelper() {}

    ///////////////////////////////////////////////////////////////////////////
    // Public methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Display notification on a clipboard change
     * @param clipItem the {@link ClipItem} to display notification for
     */
    public static void show(ClipItem clipItem, String translated) {
        if ((clipItem == null) ||
                TextUtils.isEmpty(clipItem.getText())) {
            return;
        }

        final String clipText = clipItem.getText();
        final int id = ID_COPY;
        final Context context = App.getContext();
        PendingIntent pendingIntent;

        // keep track of number of new items
        sClipItemCt++;

        final Intent intent = new Intent(context, HistoryFavActivity.class);
        intent.putExtra(AppUtils.INTENT_EXTRA_CLIP_ITEM, clipItem);
        intent.putExtra(AppUtils.INTENT_EXTRA_CLIP_COUNT, sClipItemCt);

        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack
        stackBuilder.addParentStack(TranslatorActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(intent);
        // Gets a PendingIntent containing the entire back stack
        pendingIntent = stackBuilder
                .getPendingIntent(12345, PendingIntent.FLAG_UPDATE_CURRENT);

        // remote vs. local settings
        final int largeIcon;
        final String titleText;
            largeIcon = R.mipmap.ic_launcher;
            titleText = context.getString(R.string.translate_success);

        final NotificationCompat.Builder builder =
                getBuilder(pendingIntent, largeIcon, titleText);
        builder.setContentText(translated)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(clipText));

        // Fix Oreo notifications showing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_ID = Constants.notify.MAIN_CHANNEL;
            CharSequence name = App.injectString(R.string.app_name);
            final int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            builder.setChannelId(CHANNEL_ID);
            getManager().createNotificationChannel(channel);
        }
        // notification deleted (cleared, swiped, etc) action
        // does not get called on tap if autocancel is true
        pendingIntent = NotificationReceiver
                .getPendingIntent(AppUtils.DELETE_NOTIFICATION_ACTION, id, null);
        builder.setDeleteIntent(pendingIntent);

        // Share action
        pendingIntent = NotificationReceiver
                .getPendingIntent(AppUtils.SHARE_ACTION, id, clipItem);
        builder.addAction(R.drawable.ic_bar_share, context.getString(R.string.action_share) + "...", pendingIntent);

        final NotificationManager notificationManager = getManager();
        notificationManager.notify(id, builder.build());
    }

    /**
     * Remove our {@link ClipItem} notifications
     */
    private static void removeClips() {
        final NotificationManager notificationManager = getManager();
        notificationManager.cancel(ID_COPY);
        resetCount();
    }

    /**
     * Remove all our Notifications
     */
    public static void removeAll() {
        removeClips();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Reset count
     */
    private static void resetCount() {
        sClipItemCt = 0;
    }

    /**
     * Get large icon
     * @param id Resource id
     * @return new Bitmap
     */
    private static Bitmap getLargeIcon(int id) {
        return BitmapFactory.decodeResource(App.getContext().getResources(), id);
    }

    /**
     * Get a {@link NotificationCompat.Builder} with the shared settings
     * @param pInt      Content intent
     * @param largeIcon display icon
     * @param titleText display title
     * @return the Builder
     */


    private static NotificationCompat.Builder
    getBuilder(PendingIntent pInt, int largeIcon, String titleText) {
        final Context context = App.getContext();
        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);
        final long[] pattern = {200, 200};
        builder.setContentIntent(pInt);
        builder.setLargeIcon(getLargeIcon(largeIcon));
        builder.setSmallIcon(!Preferences.isClearIcon() ?
                R.drawable.ic_notification_translate : R.drawable.transparent);
        builder.setContentTitle(titleText);
        builder.setTicker(titleText);
        builder.setColor(Preferences.getNotificationAccent());
        builder.setShowWhen(Preferences.isNotificationOngoing());
        builder.setOnlyAlertOnce(Preferences.isOnlyAlertOnce());
        builder.setAutoCancel(true);
        if (Preferences.isNotificationVibrate()) {
            builder.setVibrate(pattern);
        }
        if (Preferences.isNotificationLights()) {
            builder.setLights(Preferences.getNotificationAccent(), 200, 10000);
        }

        return builder;
    }

    /**
     * Get the NotificationManager
     * @return NotificationManager
     */
    private static NotificationManager getManager() {
        final Context context = App.getContext();
        NotificationManager mngr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        return mngr;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner classes
    ///////////////////////////////////////////////////////////////////////////

    /**
     * {@link BroadcastReceiver} to handle notification actions
     */
    public static class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final ClipItem item;
            final int noteId =
                    intent.getIntExtra(AppUtils.INTENT_EXTRA_NOTIFICATION_ID, -1);

            if (AppUtils.DELETE_NOTIFICATION_ACTION.equals(action)) {
                resetCount();
            }  else if (AppUtils.SHARE_ACTION.equals(action)) {
                item = (ClipItem) intent.getSerializableExtra(
                        AppUtils.INTENT_EXTRA_CLIP_ITEM);
                // share the clip text with other apps
                context.startActivity(Intent.createChooser(
                        new Intent(Intent.ACTION_SEND).setType("text/plain")
                                .putExtra(Intent.EXTRA_TEXT, item.getText()),
                        context.getString(R.string.send)
                ));

                cancelNotification(noteId);
                // collapse notifications
                context.sendBroadcast(
                        new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
            }
        }

        /**
         * Get a pending intent for this receiver
         * @param action   An action we know about
         * @param noteId   The id of the source notification
         * @param clipItem The {@link ClipItem}
         * @return a {@link PendingIntent}
         */
        public static PendingIntent
        getPendingIntent(String action, int noteId, ClipItem clipItem) {
            final Context context = App.getContext();
            final Intent intent =
                    new Intent(context, NotificationReceiver.class);
            intent.setAction(action);
            intent.putExtra(AppUtils.INTENT_EXTRA_NOTIFICATION_ID, noteId);
            intent.putExtra(AppUtils.INTENT_EXTRA_CLIP_ITEM, clipItem);
            return PendingIntent
                    .getBroadcast(context, 12345, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
        }

        /**
         * Remove the given notification
         * @param notificationId notification id to remove
         */
        private static void cancelNotification(int notificationId) {
            if (notificationId != -1) {
                // cancel notification
                final NotificationManager notificationManager = getManager();
                notificationManager.cancel(notificationId);
                resetCount();
            }
        }
    }
}

