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

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.adapter.HistoryPagerAdapter;
import ru.SnowVolf.translate.ui.fragment.historyfav.FavoriteFragment;
import ru.SnowVolf.translate.ui.fragment.historyfav.HistoryFragment;
import ru.SnowVolf.translate.ui.widget.ViewPagerView;

public class HistoryFavActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fav);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        ViewPagerView viewPager = (ViewPagerView) findViewById(R.id.historyFavPager);
        setViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.historyFavTab);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setViewPager(ViewPager viewPager){
        HistoryPagerAdapter adapter = new HistoryPagerAdapter(getFragmentManager());
        adapter.addFragment(new HistoryFragment(), getString(R.string.action_history));
        adapter.addFragment(new FavoriteFragment(), getString(R.string.action_favorite));
        viewPager.setAdapter(adapter);
    }
}
