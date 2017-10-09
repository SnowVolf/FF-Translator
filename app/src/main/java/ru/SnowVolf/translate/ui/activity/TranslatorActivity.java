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

package ru.SnowVolf.translate.ui.activity;


import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AlertDialog;

import android.widget.Toast;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.ui.fragment.main.TranslatorFragment;
import ru.SnowVolf.translate.util.compat.LocaleCompat;
import ru.SnowVolf.translate.util.runtime.Logger;

public class TranslatorActivity extends BaseActivity {
    // Активити создана
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        // Create the fragment only when the activity is created for the first time.
        // ie. not after orientation changes
        Fragment fragment = getFragmentManager().findFragmentByTag(TranslatorFragment.FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new TranslatorFragment();
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment, TranslatorFragment.FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private String lang = null;
    //Активити возвращается в активное состояние
    @Override
    public void onResume() {
        super.onResume();

        // Языковые настройки
        if (lang == null){
            lang = LocaleCompat.getLanguage(this);
        }
        // Проверка не изменися ли язык
        if (!LocaleCompat.getLanguage(this).equals(lang)){
            new AlertDialog.Builder(this)
                    .setMessage(R.string.app_lang_changed)
                    .setPositiveButton(R.string.ok, (d, i) -> {
                        Intent mStartActivity = new Intent(TranslatorActivity.this, TranslatorActivity.class);
                        int mIntentPendingId = 6;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(TranslatorActivity.this, mIntentPendingId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager manager = (AlarmManager) TranslatorActivity.this.getSystemService(Context.ALARM_SERVICE);
                        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        }
    }

    private static long press_time = System.currentTimeMillis();
    //Нажатие кнопки назад
    @Override
    public void onBackPressed() {
        Logger.i(this.getClass(), "onBackPressed()");
        // Если включено двойное нажатие для выхода
        if (Preferences.isBackNotif()) {
            if (press_time + 2000 > System.currentTimeMillis()) {
                if (Preferences.isKillAllowed()) {
                    Process.killProcess(Process.myPid());
                } else finish();
            } else {
                Toast.makeText(this, R.string.press_once_more, Toast.LENGTH_SHORT).show();
                press_time = System.currentTimeMillis();
            }
        } else {
            // Если нет галки на завершении процесса
            if (!Preferences.isKillAllowed()) {
                finish();
            } else Process.killProcess(Process.myPid());
        }
    }
}
