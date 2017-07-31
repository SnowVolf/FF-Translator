package ru.SnowVolf.translate.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.fragment.settings.LicencesFragment;
import ru.SnowVolf.translate.ui.fragment.settings.MainSettingsFragment;

public class SettingsActivity extends BaseActivity implements LicencesFragment.OnPreferenceSelectedListener {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        if (savedInstanceState != null) return;
        // Create the fragment only when the activity is created for the first time.
        // ie. not after orientation changes
        Fragment fragment = getFragmentManager().findFragmentByTag(MainSettingsFragment.FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new MainSettingsFragment();
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_frame_container, fragment, MainSettingsFragment.FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onPreferenceWithUriSelected(Uri uri) {
        // No other stuff
    }
}
