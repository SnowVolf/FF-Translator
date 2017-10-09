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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.preferences.BackupFactory;
import ru.SnowVolf.translate.ui.activity.TranslatorActivity;

/**
 * Created by Snow Volf on 05.08.2017, 12:07
 */

public class ImportExportSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_backup_restore);
        PreferenceCategory category = (PreferenceCategory) findPreference("import.export.category");
        category.setTitle(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/ff-girl/backup/");
        findPreference("backup.db").setOnPreferenceClickListener(__ -> {
            BackupFactory.exportDb(getActivity());
            return true;
        });
        findPreference("restore.db").setOnPreferenceClickListener(__ -> {
            BackupFactory.importDb(getActivity());
            return true;
        });
        findPreference("backup.prefs").setOnPreferenceClickListener(__ -> {
            BackupFactory.exportPrefs(getActivity());
            return true;
        });
        findPreference("restore.prefs").setOnPreferenceClickListener(__ -> {
            BackupFactory.importPrefs(getActivity());
            return true;
        });
        findPreference("reboot").setOnPreferenceClickListener(__ -> {
            Intent mStartActivity = new Intent(getActivity(), TranslatorActivity.class);
            int mIntentPendingId = 6;
            PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mIntentPendingId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Устанавливаем Title у Activity
        getActivity().setTitle(R.string.pref_ctg_backup_restore);
    }
}
