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

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.ui.fragment.other.AboutFragment;

/**
 * Created by Snow Volf on 02.08.2017, 15:56
 */

public class OtherSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_other);
        Preference mAbout = findPreference(Constants.prefs.OTHER_VERSION);
        mAbout.setTitle(R.string.app_name);
        mAbout.setSummary("v. " + BuildConfig.VERSION_NAME + " r" + BuildConfig.VERSION_CODE + ", " + BuildConfig.BUILD_TIME);
        mAbout.setOnPreferenceClickListener(__ -> {
            getFragmentManager().beginTransaction().replace(R.id.settings_frame_container, new AboutFragment()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            return true;
        });

        findPreference("dev.opt").setOnPreferenceClickListener(__ -> {
//            Fragment fragment = getFragmentManager().findFragmentByTag(DevSettingsFragment.FRAGMENT_TAG);
//            if (fragment == null) {
//                fragment = new DevSettingsFragment();
//            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.settings_frame_container, new DevSettingsFragment())
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.pref_ctg_other);
    }
}
