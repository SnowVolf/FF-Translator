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

package ru.SnowVolf.translate.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.SnowVolf.translate.App;

/**
 * Created by Snow Volf on 27.06.2017, 9:01
 *
 * Класс для определения состояния сети
 */
public class NetworkStateHelper {

    private static boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager) App.ctx().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting() && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public static boolean isWifiAvailable(){
        ConnectivityManager manager = (ConnectivityManager) App.ctx().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isAnyNetworkAvailable(){
        return isNetworkAvailable() || isWifiAvailable();
    }
}
