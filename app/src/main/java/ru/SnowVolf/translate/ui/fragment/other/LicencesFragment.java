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

package ru.SnowVolf.translate.ui.fragment.other;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.widget.Toast;

import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 30.07.2017, 16:36
 */

public class LicencesFragment extends PreferenceFragment {

    private OnPreferenceSelectedListener onPreferenceSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onPreferenceSelectedListener = (OnPreferenceSelectedListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            //throw new ClassCastException(activity.toString() + " must implement OnPreferenceSelectedListener");
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            onPreferenceSelectedListener = (OnPreferenceSelectedListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement OnPreferenceSelectedListener");
//        }
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about_libs);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.pref_licences);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        onPreferenceSelectedListener.onPreferenceWithUriSelected(preference.getIntent().getData());
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public interface OnPreferenceSelectedListener {
        void onPreferenceWithUriSelected(Uri uri);
    }
}
