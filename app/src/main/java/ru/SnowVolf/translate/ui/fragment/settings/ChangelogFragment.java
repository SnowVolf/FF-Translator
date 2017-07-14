package ru.SnowVolf.translate.ui.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 15.06.2017, 6:29
 */

public class ChangelogFragment extends TextParserFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TITLE = R.string.changelog;
        TEXT_URL = "changelog.txt";
    }
}
