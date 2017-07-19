package ru.SnowVolf.translate.ui.fragment.settings;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.favorite.FavoriteItem;
import ru.SnowVolf.translate.history.HistoryItem;
import ru.SnowVolf.translate.model.FavoriteDbModel;
import ru.SnowVolf.translate.model.HistoryDbModel;
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
    }

    private class AsyncFavorite extends AsyncTask<Void, Void, Void> {
        private FavoriteDbModel mDataHandler;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDataHandler = new FavoriteDbModel(App.ctx());
            dialog = new ProgressDialog(getActivity());
            dialog.setIndeterminate(true);
            dialog.setMessage("Please wait...");
            dialog.show();
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
            dialog.dismiss();
            Toast.makeText(getActivity(), "Completed", Toast.LENGTH_LONG).show();
        }
    }

    private class AsyncHistory extends AsyncTask<Void, Void, Void> {
        private HistoryDbModel mDataHandler;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDataHandler = new HistoryDbModel(App.ctx());
            dialog = new ProgressDialog(getActivity());
            dialog.setIndeterminate(true);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 51; i++) {
                mDataHandler.add(new HistoryItem(System.currentTimeMillis(), 0, 0,  "DevTest :: " + i, "DevTest :: " + i, "DevTest :: " + i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Toast.makeText(getActivity(), "Completed", Toast.LENGTH_LONG).show();
        }
    }


}
