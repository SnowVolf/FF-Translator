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


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.zip.CRC32;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.util.FragmentUtil;
import ru.SnowVolf.translate.util.Utils;

/**
 * Created by Snow Volf on 14.06.2017, 7:17
 *
 * Экран 'О программе'
 */

public class AboutFragment extends NativeContainerFragment {
    @BindView(R.id.about_version_item_sub) TextView version;
    @BindView(R.id.about_code_item_sub) TextView id;
    @BindView(R.id.about_time_item_sub) TextView time;
    @BindView(R.id.list_libs) Button buttonLib;
    @BindView(R.id.artem_header_img) ImageView zoeMorrell;
    @BindView(R.id.about_author_artem_mail_item) Button volfMailContact;
    @BindView(R.id.about_author_artem_pda_item) Button volfPdaContact;
    @BindView(R.id.about_author_artem_github_item) Button volfGitContact;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TITLE = R.string.about;
        initCards();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null){
            menu.clear();
        } else menu = new MenuBuilder(getActivity());
        menu.add(R.string.action_source)
                .setShowAsActionFlags(0)
                .setOnMenuItemClickListener(menuItem -> {
                    getActivity().startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SnowVolf/FF-Translator")), getString(R.string.action_source)));
                    return true;
                });
        menu.add(R.string.changelog)
                .setShowAsActionFlags(0)
                .setOnMenuItemClickListener(menuItem -> {
                    FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new ChangelogFragment());
                    return true;
                });
        menu.add(R.string.create_bug_report)
                .setShowAsActionFlags(0)
                .setOnMenuItemClickListener(menuItem -> {
                    getActivity().startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SnowVolf/FF-Translator/issues/new")), getString(R.string.create_bug_report)));
                    return true;
                });
        menu.add(R.string.donate)
                .setShowAsActionFlags(0)
                .setOnMenuItemClickListener(menuItem -> {
                    showDonateDialog();
                    return true;
                });
    }

    /**
     * Init OnClickListener-ов
     */
    private void initCards(){
        if (Preferences.isGirlEnabled()){
            zoeMorrell.setImageResource(R.drawable.zoe3s);
        }
        byte[] bytes = BuildConfig.BUILD_TIME.getBytes();
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);

        version.setText(String.format(Locale.ENGLISH, getString(R.string.version_sub), BuildConfig.VERSION_NAME));
        id.setText(String.format(Locale.ENGLISH, getString(R.string.id_sub), crc32.getValue()));
        time.setText(String.format(Locale.ENGLISH, getString(R.string.date_sub), BuildConfig.BUILD_TIME));
        volfMailContact.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            // Только программы для отправки Email смогут это перехватить
            intent.setData(Uri.parse("mailto:svolf15@yandex.ru"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            // Базовая информация об устройстве и приложении
            intent.putExtra(Intent.EXTRA_TEXT,
                    "App version: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")\n"+
                            "Android/SDK: " + Build.VERSION.RELEASE + "/" + Build.VERSION.SDK_INT +
                            "\nModel: " + Build.MANUFACTURER + ", " + Build.MODEL +
                            "\n\n --- Write your message here --- \n"
            );
            startActivity(Intent.createChooser(intent, "EMAIL"));
        });
        volfPdaContact.setOnClickListener(__ -> getActivity().startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://4pda.ru/index.php?showuser=4324432")), "ForPDA")));
        volfGitContact.setOnClickListener(__ -> getActivity().startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SnowVolf/")), "GitHub")));
        buttonLib.setOnClickListener(__ -> FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new LicencesFragment()));
    }

    /**
     * Диалог о донейте
     */
    private void showDonateDialog(){
        CharSequence[] items = new CharSequence[]{"Yandex.Money"};

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.donate)
                .setItems(items, (dialogInterface, i) -> {
                    switch (i){
                        case 0:{
                            Utils.copyToClipboard("410014101896353");
                            Toast.makeText(getActivity(), R.string.donate_copied, Toast.LENGTH_LONG).show();
                        }
                    }
                }).setPositiveButton(R.string.ok, (dialogInterface, i) -> Snackbar.make(getView(), R.string.thanks, Snackbar.LENGTH_LONG).show())
                .show();
    }
}