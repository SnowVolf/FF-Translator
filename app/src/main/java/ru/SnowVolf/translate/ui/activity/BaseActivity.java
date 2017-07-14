package ru.SnowVolf.translate.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.SnowVolf.translate.ui.interfacer.Interfacer;
import ru.SnowVolf.translate.util.Preferences;

public class BaseActivity extends AppCompatActivity {
    private static BaseActivity INSTANCE = null;
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

    public static BaseActivity getInstance() {
        if (INSTANCE == null) {
            new BaseActivity();
        }
        return INSTANCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int kk = Integer.compare(1, 2);
        LocalBroadcastManager.getInstance(this).registerReceiver(mThemeReceiver, new IntentFilter("org.openintents.action.REFRESH_THEME"));
        Interfacer.applyTheme(this);
        Interfacer.applyAccent(this);
        super.onCreate(savedInstanceState);
        INSTANCE = this;
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
