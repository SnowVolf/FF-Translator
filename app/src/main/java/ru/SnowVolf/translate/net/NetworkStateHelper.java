package ru.SnowVolf.translate.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.SnowVolf.translate.App;

/**
 * Created by Snow Volf on 27.06.2017, 9:01
 */
@SuppressWarnings("ALL")
public class NetworkStateHelper {

    public static boolean isNetworkAvailable(){
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
