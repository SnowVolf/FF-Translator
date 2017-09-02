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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 02.08.2017, 15:59
 */

public class NotificationSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @SuppressLint("BatteryLife")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_notifications);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getPreferenceScreen().removePreference(findPreference("notifications.doze"));
        } else {
            findPreference("notifications.doze").setOnPreferenceClickListener(__ -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    final Intent intent = new Intent();
                    final String packageName = getActivity().getPackageName();
                    PowerManager manager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
                    if (!manager.isIgnoringBatteryOptimizations(packageName)) {
                        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        intent.setData(Uri.parse("package:" + packageName));
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), R.string.doze_already_granted, Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            });
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //TODO: Сделать нотификации настраиваемыми
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.pref_ctg_notifications);
    }
}
