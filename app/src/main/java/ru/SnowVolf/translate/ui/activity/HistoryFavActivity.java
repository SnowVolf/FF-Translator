package ru.SnowVolf.translate.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.adapter.HistoryPagerAdapter;
import ru.SnowVolf.translate.ui.fragment.historyfav.FavoriteFragment;
import ru.SnowVolf.translate.ui.fragment.historyfav.HistoryFragment;
import ru.SnowVolf.translate.ui.widget.ViewPagerView;

public class HistoryFavActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fav);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        ViewPagerView viewPager = (ViewPagerView) findViewById(R.id.historyFavPager);
        setViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.historyFavTab);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setViewPager(ViewPager viewPager){
        HistoryPagerAdapter adapter = new HistoryPagerAdapter(getFragmentManager());
        adapter.addFragment(new HistoryFragment(), getString(R.string.action_history));
        adapter.addFragment(new FavoriteFragment(), getString(R.string.action_favorite));
        viewPager.setAdapter(adapter);
    }
}
