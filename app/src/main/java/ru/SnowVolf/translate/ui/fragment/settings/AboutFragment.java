package ru.SnowVolf.translate.ui.fragment.settings;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.util.FragmentUtil;
import ru.SnowVolf.translate.util.Utils;

/**
 * Created by Snow Volf on 14.06.2017, 7:17
 */

public class AboutFragment extends NativeContainerFragment {
    @BindView(R.id.about_version_item_sub) TextView version;
    @BindView(R.id.profile_block_information) CardView cardLicenses;
    @BindView(R.id.list_libs) Button buttonLib;
    @BindView(R.id.artem_header_img) ImageView widowMaker;
    @BindView(R.id.about_author_artem_mail_item) Button volfMailContact;
    @BindView(R.id.about_author_artem_pda_item) Button volfPdaContact;
    @BindView(R.id.about_author_artem_github_item) Button volfGitContact;

    int widowChar = 0;


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
                    getActivity().startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SnowVolf/FF-Translator/issues")), getString(R.string.create_bug_report)));
                    return true;
                });
        menu.add(R.string.donate)
                .setShowAsActionFlags(0)
                .setOnMenuItemClickListener(menuItem -> {
                    showDonateDialog();
                    return true;
                });
    }


    private void initCards(){
        version.setText("v. " + BuildConfig.VERSION_NAME + " r" + BuildConfig.VERSION_CODE + ", " + BuildConfig.BUILD_TIME);
        widowMaker.setOnClickListener(v -> {
            ++widowChar;
            switch(widowChar){
                case 0:
                    widowMaker.setImageResource(R.drawable.artem_about_background);
                    break;
                case 1:
                    widowMaker.setImageResource(R.drawable.artem_about_background2);
                    break;
                case 2:
                    widowMaker.setImageResource(R.drawable.artem_about_background3);
                    break;
                case 3:
                    widowMaker.setImageResource(R.drawable.artem_about_background);
                    widowChar = 0;
                    break;
            }
        });

        volfMailContact.setOnClickListener(__ -> getActivity().startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).setType("message/rfc822").putExtra(Intent.EXTRA_EMAIL, "svolf15@yandex.ru").putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name)), "EMAIL")));
        volfPdaContact.setOnClickListener(__ -> getActivity().startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://4pda.ru/index.php?showuser=4324432")), "ForPDA")));
        volfGitContact.setOnClickListener(__ -> getActivity().startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SnowVolf/")), "GitHub")));
        buttonLib.setOnClickListener(__ -> FragmentUtil.ctx().iterateStack(getActivity(), R.id.settings_frame_container, new LicencesFragment()));
    }
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