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

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.activity.SettingsActivity;
import ru.SnowVolf.translate.util.FragmentUtil;

/**
 * Created by Snow Volf on 02.08.2017, 21:39
 */

public class HeadersFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    public static final String FRAGMENT_TAG = "headers_fragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_headers);
        findPreference("header.api").setOnPreferenceClickListener(this);
        findPreference("header.sys").setOnPreferenceClickListener(this);
        findPreference("header.ui").setOnPreferenceClickListener(this);
        findPreference("header.performance").setOnPreferenceClickListener(this);
        findPreference("header.lists").setOnPreferenceClickListener(this);
        findPreference("header.notifications").setOnPreferenceClickListener(this);
        findPreference("header.import.export").setOnPreferenceClickListener(this);
        findPreference("header.other").setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.action_settings);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case "header.api":{
                FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new ApiSettingsFragment());
                return true;
            }
            case "header.sys":{
                FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new SystemSettingsFragment());
                return true;
            }
            case "header.ui":{
                FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new UiSettingsFragment());
                return true;
            }
            case "header.performance":{
                FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new InteractionSettingsFragment());
                return true;
            }
            case "header.lists":{
                FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new ListsSettingsFragment());
                return true;
            }
            case "header.notifications":{
                FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new NotificationSettingsFragment());
                return true;
            }
            case "header.import.export":{
                FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new ImportExportSettingsFragment());
                return true;
            }
            case "header.other":{
                FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new OtherSettingsFragment());
                return true;
            }
        }
        return false;
    }
}
