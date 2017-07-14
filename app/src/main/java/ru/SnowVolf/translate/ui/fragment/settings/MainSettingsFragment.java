package ru.SnowVolf.translate.ui.fragment.settings;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.api.yandex.translate.Translate;
import ru.SnowVolf.translate.ui.interfacer.Interfacer;
import ru.SnowVolf.translate.util.Constants;
import ru.SnowVolf.translate.util.Preferences;
import ru.SnowVolf.translate.util.runtime.Logger;

/**
 * Created by Snow Volf on 29.05.2017, 12:32
 */

public class MainSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String FRAGMENT_TAG = "main_settings_fragment";
    Preference mStatusDark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //регистрируем слушателя
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        if (Build.VERSION.SDK_INT < 23){
            getPreferenceScreen().removePreference(mStatusDark);
            getPreferenceScreen().removePreference(findPreference(Constants.Prefs.SYS_PERMISSIONS));
        }
        //иначе будет падать на kit-kat
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        init();
        setCurrentValue((ListPreference) findPreference(Constants.Prefs.UI_ACCENT));
        setCurrentValue((ListPreference) findPreference(Constants.Prefs.UI_THEME));
        setCurrentValue((ListPreference) findPreference(Constants.Prefs.SYS_LANG));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case Constants.Prefs.UI_THEME:
                setCurrentValue((ListPreference) findPreference(key));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("org.openintents.action.REFRESH_THEME"));
                break;
            case Constants.Prefs.API_KEY:
                Translate.setKey(App.ctx().getPreferences().getString(key, ""));
                break;
            case Constants.Prefs.UI_ACCENT:
                setCurrentValue((ListPreference) findPreference(Constants.Prefs.UI_ACCENT));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("org.openintents.action.REFRESH_THEME"));
                break;
            case Constants.Prefs.UI_LIGHT_STATUS_BAR:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mStatusDark.isEnabled()) {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
                    }
                } else {
                    Logger.log("Not supported");
                }
                break;
            case Constants.Prefs.SYS_LANG:
                setCurrentValue((ListPreference) findPreference(key));
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            getActivity().setTitle(R.string.action_settings);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() {
        mStatusDark = findPreference(Constants.Prefs.UI_LIGHT_STATUS_BAR);
        Preference mFont = findPreference(Constants.Prefs.UI_FONT_SIZE);
        mFont.setOnPreferenceClickListener(__ -> {
            showFontSizeDialog();
            return true;
        });
        Preference mRuntime = findPreference(Constants.Prefs.SYS_PERMISSIONS);
        mRuntime.setOnPreferenceClickListener(__ -> {
            Fragment fragment = getFragmentManager().findFragmentByTag(PermissionSettingsFragment.FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new PermissionSettingsFragment();
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.settings_frame_container, fragment, PermissionSettingsFragment.FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
            return true;
        });
        Preference mReturnToTranslate = findPreference(Constants.Prefs.PERFORMANCE_RETURN);
        mReturnToTranslate.setOnPreferenceClickListener(__ -> {
            if (!Preferences.isReturnNotif()) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.notice)
                        .setMessage(R.string.dlg_return_notif_msg)
                        .setPositiveButton(R.string.ok, ((dialogInterface, i) -> Preferences.returnNotifDone()))
                        .show();
            }
            return true;
        });
        Preference mGuide = findPreference(Constants.Prefs.OTHER_GUIDE);
        mGuide.setOnPreferenceClickListener(__ -> {
            getFragmentManager().beginTransaction().replace(R.id.settings_frame_container, new HelpFragment()).addToBackStack(null).commit();
            return true;
        });

        Preference mAbout = findPreference(Constants.Prefs.OTHER_VERSION);
        mAbout.setTitle(R.string.app_name);
        mAbout.setSummary("v. " + BuildConfig.VERSION_NAME + " r" + BuildConfig.VERSION_CODE + ", " + BuildConfig.BUILD_TIME);
        mAbout.setOnPreferenceClickListener(__ -> {
            getFragmentManager().beginTransaction().replace(R.id.settings_frame_container, new AboutFragment()).addToBackStack(null).commit();
            return true;
        });

        Preference mPolicy = findPreference(Constants.Prefs.OTHER_POLICY);
        mPolicy.setOnPreferenceClickListener(__ -> {
            getFragmentManager().beginTransaction().replace(R.id.settings_frame_container, new PrivacyPolicyFragment()).addToBackStack(null).commit();
            return true;
        });

        Preference mDev = findPreference("dev.opt");
        mDev.setOnPreferenceClickListener(__ -> {
            Fragment fragment = getFragmentManager().findFragmentByTag(DevSettingsFragment.FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new DevSettingsFragment();
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.settings_frame_container, fragment, DevSettingsFragment.FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
            return true;
        });
    }

    private void setCurrentValue(ListPreference listPreference){
        listPreference.setSummary(listPreference.getEntry());
    }

    public static int getThemeIndex(Context ctx) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(ctx).getString(Constants.Prefs.UI_THEME, String.valueOf(Interfacer.Theme.LIGHT.ordinal())));
    }

    public static int getAccentIndex(Context ctx) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(ctx).getString(Constants.Prefs.UI_ACCENT, String.valueOf(Interfacer.Accent.BLUE.ordinal())));
    }

    @SuppressLint("SetTextI18n")
    protected void showFontSizeDialog() {
        @SuppressLint("InflateParams") View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_font_size, null);

        assert v != null;
        final SeekBar seekBar = (SeekBar) v.findViewById(R.id.value_seek_bar);
        seekBar.setProgress(Preferences.getFontSize());
        final EditText editText = (EditText) v.findViewById(R.id.value_text);
        final TextView sampleText = (TextView) v.findViewById(R.id.font_size_sample);
        sampleText.setTextSize(seekBar.getProgress() + 1);
        editText.setText((seekBar.getProgress() + 1) + "");

        v.findViewById(R.id.button_minus).setOnClickListener(v1 -> {
            if (seekBar.getProgress() > 0) {
                int i = seekBar.getProgress() - 1;
                seekBar.setProgress(i);
                sampleText.setTextSize(i + 1);
                editText.setText((i + 1) + "");
            }
        });
        v.findViewById(R.id.button_plus).setOnClickListener(v12 -> {
            if (seekBar.getProgress() < seekBar.getMax()) {
                int i = seekBar.getProgress() + 1;
                seekBar.setProgress(i);
                sampleText.setTextSize(i + 1);
                editText.setText((i + 1) + "");
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    seekBar.setProgress(Integer.valueOf(s.toString()) - 1);
                    editText.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sampleText.setTextSize(i + 1);
                editText.setText((i + 1) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dlg_font_size)
                .setView(v)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> Preferences.setFontSize(seekBar.getProgress()))
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> Preferences.getFontSize())
                .setNeutralButton(R.string.clear, (dialogInterface, i) -> {
                    seekBar.setProgress(15);
                    Preferences.setFontSize(15);
                    sampleText.setTextSize(Preferences.getFontSize());
                })
                .show();
    }
}
