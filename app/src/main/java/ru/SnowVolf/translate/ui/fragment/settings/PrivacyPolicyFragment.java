package ru.SnowVolf.translate.ui.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 13.06.2017, 9:18
 */

public class PrivacyPolicyFragment extends TextParserFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TITLE = R.string.settings_policy;
        TEXT_URL = "help/policy.txt";
    }
}
