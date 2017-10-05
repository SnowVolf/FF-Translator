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


package ru.SnowVolf.translate.ui.fragment.main;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.api.yandex.YandexAPI;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.util.FragmentUtil;
import ru.SnowVolf.translate.util.runtime.Logger;

public class NoNetworkConnectionFragment extends NativeContainerFragment {
    private Unbinder unbinder;
    private int response, tempInt1, tempInt2;
    private String mTempData;
    @BindView(R.id.response_explanation) TextView mExplanation;
    @BindView(R.id.response_title) TextView mTitle;
    @BindView(R.id.no_net_image) ImageView mImage;

    public static NoNetworkConnectionFragment newInstance(int response, int position1, int position2, String param1) {
        NoNetworkConnectionFragment fragment = new NoNetworkConnectionFragment();
        Bundle args = new Bundle();
        args.putInt("response", response);
        args.putInt(Constants.intents.INTENT_FROM, position1);
        args.putInt(Constants.intents.INTENT_TO, position2);
        args.putString(Constants.intents.INTENT_SOURCE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            response = getArguments().getInt("response");
            tempInt1 = getArguments().getInt(Constants.intents.INTENT_FROM);
            tempInt2 = getArguments().getInt(Constants.intents.INTENT_TO);
            mTempData = getArguments().getString(Constants.intents.INTENT_SOURCE);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_net_connection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Preferences.setSpinnerPosition(Constants.prefs.SPINNER_1, tempInt1);
        Preferences.setSpinnerPosition(Constants.prefs.SPINNER_2, tempInt2);
            mTitle.setText(R.string.no_connection_translate_title);
            mImage.setImageResource(R.drawable.ic_warning);
            switch (response){
                case YandexAPI.RESPONSE_NULL:
                    mExplanation.setText(R.string.no_connection_translate_sum);
                    break;
                case YandexAPI.RESPONSE_KEY_BLOCKED:
                    mExplanation.setText(R.string.err_resp_402);
                    break;
                case YandexAPI.RESPONSE_WRONG_KEY:
                    mExplanation.setText(R.string.err_resp_401);
                    break;
                case YandexAPI.RESPONSE_LIMIT_EXPIRED:
                    mExplanation.setText(R.string.err_resp_404);
                    break;
                case YandexAPI.RESPONSE_BIG_TEXT_SIZE:
                    mExplanation.setText(R.string.err_resp_413);
                    break;
                case YandexAPI.RESPONSE_CANNOT_BE_TRANSLATED:
                    mExplanation.setText(R.string.err_resp_422);
                    break;
                case YandexAPI.RESPONSE_DIRECTION_ISNT_SUPPORTED:
                    mExplanation.setText(R.string.err_resp_501);
                    break;
                default:
                    Logger.i(this.getClass(), "Unknown response from server :: " + YandexAPI.getResponseCode());
                    mExplanation.setText(getString(R.string.err_resp_unknown) + "\n" + getString(R.string.err_resp_code_explanation) + " " + YandexAPI.getResponseCode());
                    break;
            }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_retry_network) void retryNetwork(){
        FragmentUtil.ctx().iterate(getActivity(), R.id.main_container,
                TranslatorFragment.newInstance(tempInt1, tempInt2, mTempData, true));
    }

    @OnClick(R.id.button_response_network) void netSettings(){
        // Посылаем в настройки Wi-Fi
        Intent netSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(netSettings);
    }
}

