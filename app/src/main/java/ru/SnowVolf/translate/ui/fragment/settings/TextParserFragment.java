package ru.SnowVolf.translate.ui.fragment.settings;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.util.StrF;

/**
 * Created by Snow Volf on 15.06.2017, 1:52
 */

public class TextParserFragment extends NativeContainerFragment {
    @BindView(R.id.help_content) TextView content;
    @BindView(R.id.help_progress) ProgressBar progress;
    public TextParserFragment(){}

    public String TEXT_URL = "help/help.txt";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_simple_content, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TEXT_URL = "help/help.txt";
        TITLE = R.string.settings_help;
        progress.setVisibility(View.VISIBLE);
        Handler wait = new Handler();
        wait.postDelayed(() -> {
            progress.setIndeterminate(true);
            wait.postDelayed(() -> {
                content.setText(StrF.parseText(TEXT_URL));
                content.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }, 100);
        }, 400);

    }
}
