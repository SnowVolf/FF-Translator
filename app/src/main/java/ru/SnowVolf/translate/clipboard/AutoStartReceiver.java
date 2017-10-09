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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.util.runtime.Logger;

/**
 * Created by Snow Volf on 06.08.2017, 16:20
 */

public class AutoStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //noinspection CallToStringEquals
        if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            return;
        }
        setupClipboardWatcher();
    }

    /**
     * Start service to monitor Clipboard for changes
     */
    private static void setupClipboardWatcher() {
        if (Preferences.isClipboardServiceAllowed() && Preferences.isStartOnBootAllowed()) {
            ClipboardWatcherService.startService(true);
        } else if (Preferences.isClipboardServiceAllowed() && !Preferences.isStartOnBootAllowed()){
            ClipboardWatcherService.startService(false);
        } else {
            Logger.log("Clipboard monitoring disable");
        }
    }
}
