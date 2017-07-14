package ru.SnowVolf.translate.ui.activity;

import android.os.Bundle;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.fragment.list.LanguageListFragment;

public class LanguageListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_list);
        getFragmentManager().beginTransaction().replace(R.id.lang_list_container, new LanguageListFragment()).commit();
    }
}
