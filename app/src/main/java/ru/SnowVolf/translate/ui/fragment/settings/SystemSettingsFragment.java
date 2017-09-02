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

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.preferences.Constants;

/**
 * Created by Snow Volf on 02.08.2017, 16:05
 */

public class SystemSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_system);
        setCurrentValue((ListPreference) findPreference(Constants.prefs.SYS_LANG));
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            getPreferenceScreen().removePreference(findPreference(Constants.prefs.SYS_PERMISSIONS));
        }
        findPreference(Constants.prefs.SYS_PERMISSIONS).setOnPreferenceClickListener(__ -> {
            Fragment fragment = getFragmentManager().findFragmentByTag(PermissionSettingsFragment.FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new PermissionSettingsFragment();
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.settings_frame_container, fragment, PermissionSettingsFragment.FRAGMENT_TAG)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            return true;
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case Constants.prefs.SYS_LANG:
                setCurrentValue((ListPreference) findPreference(key));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.pref_ctg_sys);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setCurrentValue(ListPreference listPreference){
        listPreference.setSummary(listPreference.getEntry());
    }
}
