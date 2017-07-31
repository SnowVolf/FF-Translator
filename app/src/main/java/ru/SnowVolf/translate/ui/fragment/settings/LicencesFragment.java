package ru.SnowVolf.translate.ui.fragment.settings;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 30.07.2017, 16:36
 */

public class LicencesFragment extends PreferenceFragment {

    private OnPreferenceSelectedListener onPreferenceSelectedListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onPreferenceSelectedListener = (OnPreferenceSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPreferenceSelectedListener");
        }
    }

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
