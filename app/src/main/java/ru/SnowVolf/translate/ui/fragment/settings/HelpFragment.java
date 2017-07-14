package ru.SnowVolf.translate.ui.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Snow Volf on 12.06.2017, 9:10
 */

public class HelpFragment extends TextParserFragment{
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TEXT_URL = "help/help.txt";
    }
}