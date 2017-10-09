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

import android.app.ActivityManager;
import android.content.Context;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.BuildConfig;

/**
 * General static constants utility methods
 */
public class AppUtils {
    private static String PACKAGE_NAME = BuildConfig.APPLICATION_ID;
    private static final String PACKAGE_PATH = PACKAGE_NAME += '.';

    static final String SHARE_ACTION =
            PACKAGE_PATH + "SHARE_ACTION";
    static final String DELETE_NOTIFICATION_ACTION =
            PACKAGE_PATH + "DELETE_NOTIFICATION_ACTION";
    static final String INTENT_EXTRA_CLIP_ITEM =
            PACKAGE_PATH + "CLIP_ITEM";
    static final String INTENT_EXTRA_NOTIFICATION_ID =
            PACKAGE_PATH + "NOTIFICATION_ID";
    static final String INTENT_EXTRA_CLIP_COUNT =
            PACKAGE_PATH + "CLIP_COUNT";

    private AppUtils() {
    }

    /**
     * Check if a service is running
     * @param serviceClass Class name of Service
     * @return boolean
     * @see <a href="https://goo.gl/55RFa6">Stack Overflow</a>
     */
    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        final Context context = App.getContext();
        final ActivityManager manager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);

        boolean ret = false;
        for (final ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                ret = true;
                break;
            }
        }
        return ret;
    }
}

