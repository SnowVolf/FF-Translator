package ru.SnowVolf.translate.ui.fragment.historyfav;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
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
import ru.SnowVolf.translate.ui.widget.recyclerview.RecyclerViewLinearManager;

/**
 * Created by Snow Volf on 10.06.2017, 11:01
 */

public class HistoryFragment extends NativeContainerFragment {
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mRefresh;
    @BindView(R.id.history_empty_layout) View mEmptyView;
    @BindView(R.id.history_list) RecyclerView list;

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
        list.setLayoutManager(new RecyclerViewLinearManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(this::refresh);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
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
