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

import android.graphics.Typeface;
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

    public String TEXT_URL = "";


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
        Typeface mono = Typeface.createFromAsset(getActivity().getAssets(), "fonts/monospace.ttf");
        content.setTypeface(mono);
        TEXT_URL = "";
        TITLE = 0;
        progress.setVisibility(View.VISIBLE);
        Handler wait = new Handler();
        wait.postDelayed(() -> {
            progress.setIndeterminate(true);
            wait.postDelayed(() -> {
                content.setText(StrF.parseText(TEXT_URL));
                content.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }, 100);
        }, 300);

    }
}
