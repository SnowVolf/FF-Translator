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


import android.util.Log;

import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.preferences.Preferences;

/**
 * Created by Snow Volf on 14.06.2017, 6:14
 */

public class Logger {
    public static void log(Object o){
        if (Preferences.isLogAllowed()) {
            i(Logger.class, o);
        }
    }

    public static void i(Class c, Object o){
        if (Preferences.isLogAllowed()) {
            Log.i(Constants.common.TAG, "[" + c.getSimpleName() + "] -> " + o);
        }
    }

    public static void e(Class c, Object o){
        if (Preferences.isLogAllowed()) {
            Log.e(Constants.common.TAG, "[" + c.getSimpleName() + "] -> " + o);
        }
    }
}
