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

package ru.SnowVolf.translate.util.runtime;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Snow Volf on 21.06.2017, 11:20
 */

public class RuntimeUtil {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_AUDIO = 2;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] PERMISSIONS_AUDIO = {
            Manifest.permission.RECORD_AUDIO
    };
    
    public static void storage(Activity activity) {
        Log.i("VfTr", "RuntimePermission");
        // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            Log.i("VfTr", "Check : RuntimePermission");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                Log.i("VfTr", "RequestPermissions : RuntimePermission");
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
    }

    public static void audio(Activity activity) {
        Log.i("VfTr", "RuntimePermission");
        // Check if we have write permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            Log.i("VfTr", "Check : RuntimePermission");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                Log.i("VfTr", "RequestPermissions : RuntimePermission");
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_AUDIO,
                        REQUEST_AUDIO
                );
            }
        }
    }
}
