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

import android.app.Fragment;
import android.app.FragmentTransaction;

import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.R;

import ru.SnowVolf.translate.ui.fragment.other.LicencesFragment;
import ru.SnowVolf.translate.ui.fragment.settings.HeadersFragment;
import ru.SnowVolf.translate.util.runtime.Logger;

public class SettingsActivity extends BaseActivity implements LicencesFragment.OnPreferenceSelectedListener {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        // Задание overflow иконки
        Drawable overflow = AppCompatResources.getDrawable(this, R.drawable.ic_menu);
        toolbar.setOverflowIcon(overflow);
        assert getSupportActionBar() != null;

        if (savedInstanceState != null) return;
        // Create the fragment only when the activity is created for the first time.
        // ie. not after orientation changes
        Fragment fragment = getFragmentManager().findFragmentByTag(HeadersFragment.FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new HeadersFragment();
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_frame_container, fragment, HeadersFragment.FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
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
