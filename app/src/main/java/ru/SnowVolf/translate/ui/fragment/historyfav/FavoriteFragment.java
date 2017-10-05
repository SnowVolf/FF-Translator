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

package ru.SnowVolf.translate.ui.fragment.historyfav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.favorite.FavoriteItem;
import ru.SnowVolf.translate.model.FavoriteDbModel;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.ui.adapter.FavoriteAdapter;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.ui.interfacer.ThemeWrapper;
import ru.SnowVolf.translate.ui.widget.recyclerview.FastScroller;
import ru.SnowVolf.translate.ui.widget.recyclerview.RecyclerViewLinearManager;

/**
 * Created by Snow Volf on 10.06.2017, 10:45
 */
public class FavoriteFragment extends NativeContainerFragment {
    private static SwipeRefreshLayout sRefresh;
    private static RecyclerView sList;
    private static View sEmptyView;
    private FastScroller sFastScroller;

    private static FavoriteDbModel sDbHandler;
    private static FavoriteAdapter sAdapter;

    private final RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateFavView(sAdapter.getItemCount() == 0);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateFavView(sAdapter.getItemCount() == 0);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sRefresh = rootView.findViewById(R.id.swipe_refresh);
        sList = rootView.findViewById(R.id.favorite_list);
        sEmptyView = rootView.findViewById(R.id.favorite_empty_layout);
        sFastScroller = rootView.findViewById(R.id.fast_scroller);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TITLE = R.string.action_history_fav;
        sDbHandler = new FavoriteDbModel(getActivity());
        sAdapter = new FavoriteAdapter(getActivity(), new ArrayList<>());
        sAdapter.registerAdapterDataObserver(mAdapterDataObserver);
        sList.setLayoutManager(new RecyclerViewLinearManager(getActivity(), LinearLayoutManager.VERTICAL, false){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                final int firstVisiblePosition = findFirstVisibleItemPosition();
                if (firstVisiblePosition != 0){
                    if (firstVisiblePosition == -1){
                        sFastScroller.setVisibility(View.GONE);
                        return;
                    }
                    final int lastVisiblePosition = findLastVisibleItemPosition();
                    int itemsShown = lastVisiblePosition - firstVisiblePosition + 1;
                    sFastScroller.setVisibility(sAdapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
                }
            }
        });
        sRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(),R.color.accent_red),
                ContextCompat.getColor(getActivity(), R.color.md_light_blue_500),
                ContextCompat.getColor(getActivity(),R.color.accent_green));
        sRefresh.setBackgroundResource(!ThemeWrapper.isLightTheme() ?
                R.color.dark_colorPrimary : R.color.light_colorPrimary);
        sFastScroller.setRecyclerView(sList);
        sFastScroller.setViewsToUse(R.layout.fast_scroller, R.id.fast_scroller_handle);
        sList.setItemAnimator(new DefaultItemAnimator());
        sList.setAdapter(sAdapter);
        sRefresh.setDistanceToTriggerSync(300);
        sRefresh.setOnRefreshListener(FavoriteFragment::refresh);
        refresh();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_favorite:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.dlg_delete_favorite)
                        .setMessage(R.string.dlg_delete_favorite_msg)
                        .setPositiveButton(R.string.ok,
                                (d, w) -> deleteAll())
                        .setNegativeButton(android.R.string.cancel, (d, w) -> d.dismiss())
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
    }

    public static void refresh() {
        List<FavoriteItem> items = sDbHandler.getAllItems();
        sAdapter.updateList(items);
        updateFavView(items.isEmpty());
        new Handler().postDelayed(() ->  sRefresh.setRefreshing(false), 2500);
    }

    @SuppressWarnings("SameReturnValue")
    public static boolean editItem(Context context, FavoriteItem item) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_favorite_edit, null);
        EditText titleEdit = view.findViewById(R.id.favorite_edit_title);
        EditText urlEdit = view.findViewById(R.id.favorite_edit);

        titleEdit.setText(item.getTitle());
        urlEdit.setText(item.getSource());
        titleEdit.setTextSize(Preferences.getFontSize());
        urlEdit.setTextSize(Preferences.getFontSize());

        String error = App.injectString(R.string.error);
        urlEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    urlEdit.setError(error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    urlEdit.setError(error);
                }
            }
        });

        new AlertDialog.Builder(context)
                .setTitle(R.string.dlg_favorite_edit)
                .setView(view)
                .setPositiveButton(R.string.ok,
                        ((dialog, w) -> {
                            String url = urlEdit.getText().toString();
                            String title = titleEdit.getText().toString();
                            if (url.isEmpty()) {
                                urlEdit.setError(error);
                                urlEdit.requestFocus();
                            }
                            item.setTitle(title);
                            item.setSource(url);
                            sDbHandler.updateItem(item);
                            refresh();
                            dialog.dismiss();
                        }))
                .setNegativeButton(android.R.string.cancel,
                        (dialog, w) -> dialog.dismiss())
                .show();
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && Preferences.isRefreshAuto()){
            try {
                refresh();
            } catch (NullPointerException ignored){}
        }
    }

    private void deleteAll() {
        sRefresh.setRefreshing(true);

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                sDbHandler.deleteAll();
                return true;
            }

            @Override
            protected void onPostExecute(Boolean param) {
                new Handler().postDelayed(FavoriteFragment::refresh, 500);
            }
        }.execute();
    }

    private static void updateFavView(boolean isEmpty){
        sEmptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}
