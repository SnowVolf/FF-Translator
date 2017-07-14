package ru.SnowVolf.translate.ui.fragment.settings;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.adapter.SimpleRVAdapter;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.ui.widget.recyclerview.RecyclerViewLinearManager;
import ru.SnowVolf.translate.ui.widget.recyclerview.SimpleItem;
import ru.SnowVolf.translate.util.FragmentUtil;

/**
 * Created by Snow Volf on 14.06.2017, 7:17
 */

public class AboutFragment extends NativeContainerFragment {
    @BindView(R.id.about_version_item_sub) TextView version;
    @BindView(R.id.profile_block_information) CardView cardLicenses;
    @BindView(R.id.open_close) ImageButton buttonOpenClose;
    @BindView(R.id.list_libs) RecyclerView recyclerView;
    @BindView(R.id.list_common) RecyclerView commonRecyclerView;
    @BindView(R.id.artem_header_img) ImageView widowMaker;
    @BindView(R.id.about_author_artem_mail_item) TextView volfMailContact;
    @BindView(R.id.about_author_artem_pda_item) TextView volfPdaContact;
    @BindView(R.id.about_author_artem_github_item) TextView volfGitContact;

    SimpleRVAdapter adapter;
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
        initActionList();
        initList();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initCards(){
        version.setText("v. " + BuildConfig.VERSION_NAME + " r" + BuildConfig.VERSION_CODE + ", " + BuildConfig.BUILD_TIME);
        //buttonChangelog.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.settings_frame_container, new ChangelogFragment()).addToBackStack(null).commit());
        buttonOpenClose.setOnClickListener(v -> {
            if (recyclerView.getVisibility() == View.GONE){
                buttonOpenClose.setImageResource(R.drawable.ic_about_drop_up);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                buttonOpenClose.setImageResource(R.drawable.ic_about_drop_down);
                recyclerView.setVisibility(View.GONE);
            }
        });
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
    }

    private void initActionList(){
        ArrayList<SimpleItem> list = new ArrayList<>();
        list.add(new SimpleItem(R.drawable.ic_changelog, getString(R.string.changelog), "", view ->{
            FragmentUtil.ctx().interateStack(getActivity(), R.id.settings_frame_container, new ChangelogFragment());
            Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
        }));
        list.add(new SimpleItem(R.drawable.ic_bug_report, getString(R.string.create_bug_report), null));
        adapter = new SimpleRVAdapter(getActivity(), list);

        commonRecyclerView.setLayoutManager(new RecyclerViewLinearManager(getActivity()));
        commonRecyclerView.setItemAnimator(new DefaultItemAnimator());
        commonRecyclerView.setAdapter(adapter);

    }

    private void initList(){
        final String[] libNames = {
                getString(R.string.lib1), getString(R.string.lib2), getString(R.string.lib3),
                getString(R.string.lib4), getString(R.string.lib5), getString(R.string.lib12), getString(R.string.lib6),
                getString(R.string.lib7), getString(R.string.lib8), getString(R.string.ilb9),
                getString(R.string.lib10), getString(R.string.lib11)};
        final String[] libOwners = {
                getString(R.string.owner_lib1), getString(R.string.owner_lib2), getString(R.string.owner_lib3),
                getString(R.string.owner_lib4), getString(R.string.owner_lib5), getString(R.string.owner_lib12), getString(R.string.owner_lib6),
                getString(R.string.owner_lib7), getString(R.string.owner_lib8), getString(R.string.owner_lib9),
                getString(R.string.owner_lib10), getString(R.string.owner_lib11)
        };
        ArrayList<SimpleItem> list = new ArrayList<>();
        for (int i = 0; i < libNames.length; i++) {
            list.add(i, new SimpleItem(libNames.length, libNames[i], libOwners[i]));
        }

        adapter = new SimpleRVAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new RecyclerViewLinearManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
}