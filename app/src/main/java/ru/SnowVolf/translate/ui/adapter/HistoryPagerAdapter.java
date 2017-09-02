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

package ru.SnowVolf.translate.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;

import java.util.ArrayList;

/**
 * Created by Snow Volf on 10.06.2017, 1:06
 */

public class HistoryPagerAdapter extends NativeAdapter {

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> fragmentTitleList = new ArrayList<>();

    public HistoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     * current position
     */
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
}
