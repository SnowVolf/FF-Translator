package ru.SnowVolf.translate.ui.fragment.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.util.runtime.RuntimeUtil;

/**
 * Created by Snow Volf on 22.06.2017, 13:28
 */

public class PermissionSettingsFragment extends PreferenceFragment {
    public static final String FRAGMENT_TAG = "permissions_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_runtime);
        init();
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            getActivity().setTitle(R.string.permissions);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init(){
        Preference mAudio = findPreference("perm.audio");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            mAudio.setOnPreferenceClickListener(__ -> {
                RuntimeUtil.audio(getActivity());
                return true;
            });
        } else mAudio.setSummary("GRANTED");
        Preference mStorage = findPreference("perm.storage");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mStorage.setOnPreferenceClickListener(__ -> {
                RuntimeUtil.storage(getActivity());
                return true;
            });
        } else mStorage.setSummary("GRANTED");

        Preference mSettings = findPreference("perm.settings");
        mSettings.setOnPreferenceClickListener(__ -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            getActivity().startActivityForResult(intent, 5);
            Toast.makeText(getActivity(), R.string.goto_settings_toast, Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}
