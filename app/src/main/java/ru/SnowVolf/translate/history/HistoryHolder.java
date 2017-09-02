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

package ru.SnowVolf.translate.history;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.favorite.FavoriteItem;
import ru.SnowVolf.translate.model.FavoriteDbModel;
import ru.SnowVolf.translate.model.HistoryDbModel;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.ui.activity.FullscreenActivity;
import ru.SnowVolf.translate.ui.activity.HistoryFavActivity;
import ru.SnowVolf.translate.ui.activity.TranslatorActivity;
import ru.SnowVolf.translate.ui.adapter.HistoryAdapter;
import ru.SnowVolf.translate.util.CalendarUtils;
import ru.SnowVolf.translate.util.Utils;

/**
 * Created by Snow Volf on 30.05.2017, 11:37
 */

public class HistoryHolder extends RecyclerView.ViewHolder {

    private final LinearLayout mContainerLayout;
    private final TextView mTitle;
    private final TextView mSummary;
    private final TextView mDate;
    private final ImageView mMenu;
    private ImageView mFav;
    private final TextView mLangPair;
    private HistoryDbModel mDbModel;

    public HistoryHolder(View view) {
        super(view);
        mContainerLayout = view.findViewById(R.id.row_history_clickable_container);
        mTitle = view.findViewById(R.id.row_history_title);
        mSummary = view.findViewById(R.id.row_history_source);
        mDate = view.findViewById(R.id.row_history_summary);
        mMenu = view.findViewById(R.id.row_history_menu);
        mFav = view.findViewById(R.id.row_history_fav_indicator);
        mLangPair = view.findViewById(R.id.row_history_pair);
    }

    public void setData(Context context, HistoryItem item) {
        mDbModel = new HistoryDbModel(context);
        String title = item.getSource();

        mTitle.setText(title);
        mSummary.setText(item.getTranslation());
        mDate.setText(CalendarUtils.getRelativeDisplayTime(item.getDate()));
        if (item.isInFavorites()){
            mFav.setImageResource(R.drawable.ic_fav_stat_yes);
        }

        mLangPair.setText(item.getFromCode() + "-" + item.getToCode());
        Intent mIntent = new Intent(context, TranslatorActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.putExtra(Constants.intents.INTENT_FROM, item.getFromPosition());
        mIntent.putExtra(Constants.intents.INTENT_TO, item.getToPosition());
        mIntent.putExtra(Constants.intents.INTENT_SOURCE, item.getSource());
        mIntent.putExtra(Constants.intents.INTENT_TRANSLATED, item.getTranslation());
        mContainerLayout.setOnClickListener(v -> context.startActivity(mIntent));
        mMenu.setOnClickListener(view -> showCxtMenu(context, item));
    }

    private void showCxtMenu(Context ctx, HistoryItem historyItem){
        PopupMenu menu = new PopupMenu(ctx, mMenu);
        menu.inflate(R.menu.menu_popup_history);
        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.action_copy:{
                    Utils.copyToClipboard(historyItem.getTranslation());
                    Snackbar.make(mContainerLayout, R.string.translation_copied, Snackbar.LENGTH_SHORT).show();
                    return true;
                }
                case R.id.action_copy_close:{
                    Utils.copyToClipboard(historyItem.getTranslation());
                    ((HistoryFavActivity) ctx).finish();
                    return true;
                }
                case R.id.action_view_fullscreen:{
                    Intent fullIntent = new Intent(ctx, FullscreenActivity.class);
                    fullIntent.putExtra(Constants.intents.INTENT_TRANSLATED, historyItem.getTranslation());
                    ctx.startActivity(fullIntent);
                    return true;
                }
                case R.id.action_share_sys:{
                    share(ctx, historyItem);
                    return true;
                }
                case R.id.action_history_add_favorite:{
                    new FavoriteDbModel(ctx).addItem(new FavoriteItem(historyItem.getId(), historyItem.getFromPosition(), historyItem.getToPosition(),  historyItem.getTranslation(), historyItem.getSource()));
                    Snackbar.make(mContainerLayout, R.string.added_to_favorites, Snackbar.LENGTH_SHORT).show();
                    historyItem.setInFavorites(1);
                    mDbModel.updateItem(historyItem);
                    return true;
                }
                case R.id.action_history_delete:{
                    new HistoryDbModel(ctx).delete(historyItem.getId());
                    HistoryAdapter adapter = new HistoryAdapter(ctx, new ArrayList<>());
                    adapter.removeItem(historyItem.getId());
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }
            return true;
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(ctx, (MenuBuilder) menu.getMenu(), mMenu);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
    }

    private void share(Context ctx, HistoryItem historyItem) {
        ctx.startActivity(Intent.createChooser(
                new Intent(Intent.ACTION_SEND).setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, historyItem.getTranslation()),
                ctx.getString(R.string.send)
        ));
    }
}
