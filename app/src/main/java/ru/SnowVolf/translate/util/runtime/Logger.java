package ru.SnowVolf.translate.util.runtime;


import android.util.Log;

import ru.SnowVolf.translate.util.Constants;
import ru.SnowVolf.translate.util.Preferences;

/**
 * Created by Snow Volf on 14.06.2017, 6:14
 */

public class Logger {
    public static void log(Object o){
        if (Preferences.isLogAllowed()) {
            logi(Logger.class, o);
        }
    }

    public static void logi(Class c, Object o){
        if (Preferences.isLogAllowed()) {
            Log.i(Constants.Common.TAG, "[" + c.getSimpleName() + "] ->> " + o);
        }
    }

    public static void loge(Class c, Object o){
        if (Preferences.isLogAllowed()) {
            Log.e(Constants.Common.TAG, "[" + c.getSimpleName() + "] ->> " + o);
        }
    }
}
