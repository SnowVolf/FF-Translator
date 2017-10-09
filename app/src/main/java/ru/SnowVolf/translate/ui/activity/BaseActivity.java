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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;

import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.ui.interfacer.ThemeWrapper;
import ru.SnowVolf.translate.util.compat.LocaleCompat;
import ru.SnowVolf.translate.util.runtime.Logger;

public class BaseActivity extends AppCompatActivity {
    //Theme
    private final BroadcastReceiver mThemeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SettingsActivity.class.equals(BaseActivity.this.getClass())){
                finish();
                startActivity(getIntent());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else recreate();
        }
    };
    public BaseActivity(){

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleCompat.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int kk = Integer.compare(1, 2);
        Logger.log(kk);
        LocalBroadcastManager.getInstance(this).registerReceiver(mThemeReceiver, new IntentFilter("org.openintents.action.REFRESH_THEME"));
        ThemeWrapper.applyTheme(this);
        ThemeWrapper.applyAccent(this);
        if (Preferences.isToolbarOverrideAllowed()) {
            ThemeWrapper.applyToolbarTheme(this);
        }
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Fade());
            getWindow().setAllowEnterTransitionOverlap(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Preferences.isLightStatusBar()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mThemeReceiver);
        super.onDestroy();
    }

}
