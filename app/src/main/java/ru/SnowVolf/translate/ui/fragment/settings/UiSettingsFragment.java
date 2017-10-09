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

package ru.SnowVolf.translate.ui.fragment.settings;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.util.runtime.Logger;

//import com.thebluealliance.spectrum.SpectrumPreference;

/**
 * Created by Snow Volf on 02.08.2017, 15:53
 */

public class UiSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    Preference mStatusDark;
    //SpectrumPreference mAccentPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_ui);
        mStatusDark = findPreference(Constants.prefs.UI_LIGHT_STATUS_BAR);
        //mAccentPreference = (SpectrumPreference) findPreference("ui.accent.circle");
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        if (Build.VERSION.SDK_INT < 23){
            getPreferenceScreen().removePreference(mStatusDark);
            getPreferenceScreen().removePreference(findPreference(Constants.prefs.SYS_PERMISSIONS));
        }
        setCurrentValue((ListPreference) findPreference(Constants.prefs.UI_ACCENT));
        setCurrentValue((ListPreference) findPreference(Constants.prefs.UI_THEME));
        setCurrentValue((ListPreference) findPreference(Constants.prefs.UI_TOOLBAR));

        findPreference(Constants.prefs.UI_FONT_SIZE).setOnPreferenceClickListener(__ -> {
            showFontSizeDialog();
            return true;
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case Constants.prefs.UI_THEME:
                setCurrentValue((ListPreference) findPreference(key));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("org.openintents.action.REFRESH_THEME"));
                break;
            case Constants.prefs.UI_TOOLBAR:
                setCurrentValue((ListPreference) findPreference(Constants.prefs.UI_TOOLBAR));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("org.openintents.action.REFRESH_THEME"));
                break;
            case Constants.prefs.UI_ACCENT:
                setCurrentValue((ListPreference) findPreference(Constants.prefs.UI_ACCENT));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("org.openintents.action.REFRESH_THEME"));
                break;
            case Constants.prefs.UI_LIGHT_STATUS_BAR:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mStatusDark.isEnabled()) {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
                    }
                } else {
                    Logger.log("Light status bar is not supported");
                }
                break;
//            case "ui.accent.circle":{
//                switch (mAccentPreference.getColor()){
//                    case -1:
//                        //white
//                        break;
//                    case -1695465:
//                        //red
//                        break;
//                    case -720809:
//                        //pink
//                        break;
//                    case -6543440:
//                        //purple
//                        break;
//                }
//            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.pref_ctg_ui);
    }

    private void setCurrentValue(ListPreference listPreference){
        if (listPreference != null){
            listPreference.setSummary(listPreference.getEntry());
        }
    }

    @SuppressLint("SetTextI18n")
    protected void showFontSizeDialog() {
        @SuppressLint("InflateParams") View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_font_size, null);

        assert v != null;
        final SeekBar seekBar = v.findViewById(R.id.value_seek_bar);
        seekBar.setProgress(Preferences.getFontSize());
        final EditText editText = v.findViewById(R.id.value_text);
        final TextView sampleText = v.findViewById(R.id.font_size_sample);
        sampleText.setTextSize(seekBar.getProgress() + 1);
        editText.setText(Integer.toString(seekBar.getProgress() + 1));

        v.findViewById(R.id.button_minus).setOnClickListener(v1 -> {
            if (seekBar.getProgress() > 0) {
                int i = seekBar.getProgress() - 1;
                seekBar.setProgress(i);
                sampleText.setTextSize(i + 1);
                editText.setText(Integer.toString(i + 1));
            }
        });
        v.findViewById(R.id.button_plus).setOnClickListener(v12 -> {
            if (seekBar.getProgress() < seekBar.getMax()) {
                int i = seekBar.getProgress() + 1;
                seekBar.setProgress(i);
                sampleText.setTextSize(i + 1);
                editText.setText(Integer.toString(i + 1));
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
                editText.setText(Integer.toString(i + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dlg_font_size)
                .setView(v)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> Preferences.setFontSize(seekBar.getProgress()))
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> Preferences.getFontSize())
                .setNeutralButton(R.string.clear, null)
                .show();
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(view -> {
            seekBar.setProgress(15);
            Preferences.setFontSize(15);
            sampleText.setTextSize(Preferences.getFontSize());
        });
    }
}
