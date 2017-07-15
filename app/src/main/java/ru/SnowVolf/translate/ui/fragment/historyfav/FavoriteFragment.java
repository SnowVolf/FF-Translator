package ru.SnowVolf.translate.ui.fragment.historyfav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
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
import java.util.List;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.favorite.FavoriteItem;
import ru.SnowVolf.translate.model.FavoriteDbModel;
import ru.SnowVolf.translate.ui.adapter.FavoriteAdapter;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.ui.widget.recyclerview.RecyclerViewLinearManager;
import ru.SnowVolf.translate.util.Preferences;

/**
 * Created by Snow Volf on 10.06.2017, 10:45
 */
public class FavoriteFragment extends NativeContainerFragment {
    private static SwipeRefreshLayout mRefresh;
    private static RecyclerView mList;
    private static View mEmptyView;

    private static FavoriteDbModel mDbHandler;
    private static FavoriteAdapter mAdapter;

    private final RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateFavView(mAdapter.getItemCount() == 0);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateFavView(mAdapter.getItemCount() == 0);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        mList = (RecyclerView) rootView.findViewById(R.id.favorite_list);
        mEmptyView = rootView.findViewById(R.id.favorite_empty_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TITLE = R.string.action_history_fav;
        mDbHandler = new FavoriteDbModel(getActivity());
        mAdapter = new FavoriteAdapter(getActivity(), new ArrayList<>());
        mAdapter.registerAdapterDataObserver(mAdapterDataObserver);
        mList.setLayoutManager(new RecyclerViewLinearManager(getActivity()));
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(FavoriteFragment::refresh);
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
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
    }

    public static void refresh() {
        List<FavoriteItem> items = mDbHandler.getAllItems();
        mAdapter.updateList(items);
        updateFavView(items.isEmpty());
        new Handler().postDelayed(() ->  mRefresh.setRefreshing(false), 2500);
    }

    @SuppressWarnings("SameReturnValue")
    public static boolean editItem(Context context, FavoriteItem item) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_favorite_edit, null);
        EditText titleEdit = (EditText) view.findViewById(R.id.favorite_edit_title);
        EditText urlEdit = (EditText) view.findViewById(R.id.favorite_edit);

        titleEdit.setText(item.getTitle());
        urlEdit.setText(item.getSource());
        titleEdit.setTextSize(Preferences.getFontSize());
        urlEdit.setTextSize(Preferences.getFontSize());

        String error = App.ctx().getString(R.string.error);
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
                            mDbHandler.updateItem(item);
                            refresh();
                            dialog.dismiss();
                        }))
                .setNegativeButton(android.R.string.cancel,
                        (dialog, w) -> dialog.dismiss())
                .show();
        return true;
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
                new Handler().postDelayed(FavoriteFragment::refresh, 500);
            }
        }.execute();
    }

    private static void updateFavView(boolean isEmpty){
        mEmptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}
