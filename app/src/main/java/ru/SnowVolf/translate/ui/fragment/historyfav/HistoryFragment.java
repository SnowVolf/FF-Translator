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

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.history.HistoryItem;
import ru.SnowVolf.translate.model.HistoryDbModel;
import ru.SnowVolf.translate.ui.adapter.HistoryAdapter;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.ui.interfacer.ThemeWrapper;
import ru.SnowVolf.translate.ui.widget.recyclerview.FastScroller;
import ru.SnowVolf.translate.ui.widget.recyclerview.RecyclerViewLinearManager;

/**
 * Created by Snow Volf on 10.06.2017, 11:01
 */

public class HistoryFragment extends NativeContainerFragment {
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mRefresh;
    @BindView(R.id.history_empty_layout) View mEmptyView;
    @BindView(R.id.history_list) RecyclerView list;
    @BindView(R.id.fast_scroller) FastScroller fastScroller;

    private HistoryDbModel mDbHandler;
    public HistoryAdapter mAdapter;

    public HistoryFragment(){
    }

    private final RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateHistoryView(mAdapter.getItemCount() == 0);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateHistoryView(mAdapter.getItemCount() == 0);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TITLE = R.string.action_history_fav;
        mDbHandler = new HistoryDbModel(getActivity());
        mAdapter = new HistoryAdapter(getActivity(), new ArrayList<>());
        mAdapter.registerAdapterDataObserver(mAdapterDataObserver);
        list.setLayoutManager(new RecyclerViewLinearManager(getActivity(), LinearLayoutManager.VERTICAL, false){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                final int firstVisiblePosition = findFirstVisibleItemPosition();
                if (firstVisiblePosition != 0){
                    if (firstVisiblePosition == -1){
                        fastScroller.setVisibility(View.GONE);
                        return;
                    }
                    final int lastVisiblePosition = findLastVisibleItemPosition();
                    int itemsShown = lastVisiblePosition - firstVisiblePosition + 1;
                    fastScroller.setVisibility(mAdapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
                }
            }
        });
        fastScroller.setRecyclerView(list);
        fastScroller.setViewsToUse(R.layout.fast_scroller, R.id.fast_scroller_handle);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(mAdapter);
        mRefresh.setDistanceToTriggerSync(300);
        mRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(),R.color.accent_red),
                ContextCompat.getColor(getActivity(), R.color.md_light_blue_500),
                ContextCompat.getColor(getActivity(),R.color.accent_green));
        mRefresh.setBackgroundResource(!ThemeWrapper.isLightTheme() ?
                R.color.dark_colorPrimary : R.color.light_colorPrimary);
        mRefresh.setOnRefreshListener(this::refresh);
        refresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                refresh();
            } catch (NullPointerException ignored){}
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_delete_history) {
            return super.onOptionsItemSelected(item);
        }

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dlg_delete_history)
                .setMessage(R.string.dlg_delete_history_msg)
                .setPositiveButton(R.string.ok,
                        (d, w) -> deleteAll())
                .setNegativeButton(android.R.string.cancel, (d, w) -> d.dismiss())
                .show();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
    }

    public void refresh() {
        List<HistoryItem> items = mDbHandler.getAllItems();
        mAdapter.updateList(items);
        // History view
        updateHistoryView(items.isEmpty());
        new Handler().postDelayed(() ->  mRefresh.setRefreshing(false), 2500);
    }

    private void deleteAll() {
        mRefresh.setRefreshing(true);

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                mDbHandler.deleteAll();
                return true;
            }
            @Override
            protected void onPostExecute(Boolean param) {
                new Handler().postDelayed(() -> refresh(), 500);
            }
        }.execute();
    }

    public  HistoryAdapter getAdapter() {
        return mAdapter;
    }

    private void updateHistoryView(boolean isEmpty){
        mEmptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}
