package ru.SnowVolf.translate.ui.fragment.settings;


import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.net.NetworkStateHelper;
import ru.SnowVolf.translate.util.Constants;
import ru.SnowVolf.translate.util.Utils;

/**
 * Created by Snow Volf on 25.06.2017, 5:52
 */

public class DevSettingsFragment extends PreferenceFragment {
    public static final String FRAGMENT_TAG = "dev_settings_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_dev);
        init();
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            getActivity().setTitle("JVM Girl");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init(){
        Preference mDbg = findPreference("dev.dbg.info");
        mDbg.setOnPreferenceClickListener(__ -> {
            String JackInfo =
                    "=== BEGIN GIRL LOG ===\n"
                            + "GIRL_VERSION = " + Constants.Common.GIRL_ALIAS + "\n"
                            + "CURRENT_TIME = " + Utils.getNormalDate(System.currentTimeMillis()) + "\n"
                            + "APP_VERSION = " + BuildConfig.VERSION_NAME + "\n"
                            + "VERSION_CODE = " + BuildConfig.VERSION_CODE + "\n"
                            + "CONNECTED_NET = " + NetworkStateHelper.isAnyNetworkAvailable() + "\n"
                            + "CONNECTED_WIFI = " + NetworkStateHelper.isWifiAvailable() + "\n"
                            + "IS_BETA = " + BuildConfig.DEBUG + "\n"
                            + "SDK = " + Build.VERSION.SDK_INT + "\n"
                            + "PHONE_MODEL = " + Build.MANUFACTURER + ", " + Build.MODEL + "\n"
                            + "HISTORY_DB_VER = " + Constants.DatabaseHistory.DB_VERSION + "\n"
                            + "FAVORITE_DB_VER = " + Constants.DatabaseFavorites.DB_VERSION + "\n"
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

        Preference mLog = findPreference("dev.logging");
        mLog.setDefaultValue(BuildConfig.DEBUG);

        Preference mEx = findPreference("dev.exception");
        mEx.setOnPreferenceClickListener(__ -> {
            throw new RuntimeException("Dev Exception");
        });
    }


}
