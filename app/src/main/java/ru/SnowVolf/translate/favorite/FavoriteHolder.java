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

package ru.SnowVolf.translate.favorite;

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
import android.widget.TextView;

import java.util.ArrayList;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.model.FavoriteDbModel;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.ui.activity.FullscreenActivity;
import ru.SnowVolf.translate.ui.activity.HistoryFavActivity;
import ru.SnowVolf.translate.ui.activity.TranslatorActivity;
import ru.SnowVolf.translate.ui.adapter.FavoriteAdapter;
import ru.SnowVolf.translate.ui.fragment.historyfav.FavoriteFragment;
import ru.SnowVolf.translate.util.Utils;


/**
 * Created by Snow Volf on 04.06.2017, 22:55
 */

public class FavoriteHolder extends RecyclerView.ViewHolder {
    private final LinearLayout mCard;
    private final TextView mTitle;
    private final TextView mSubTitle;
    private final ImageView mMenu;

    public FavoriteHolder(View view) {
        super(view);
        mCard = view.findViewById(R.id.row_favorite_card);
        mTitle = view.findViewById(R.id.row_favorite_title);
        mSubTitle = view.findViewById(R.id.row_favorite_subtitle);
        mMenu = view.findViewById(R.id.row_favorite_menu);
    }

    public void setData(Context context, FavoriteItem item) {
        String title = item.getSource();
        String summary = item.getTitle();

        mTitle.setText(title);
        mSubTitle.setText(summary);

        mCard.setOnClickListener(v -> {
            Intent mIntent = new Intent(context, TranslatorActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra(Constants.intents.INTENT_FROM, item.getFromPosition())
                    .putExtra(Constants.intents.INTENT_TO, item.getToPosition())
                    .putExtra(Constants.intents.INTENT_SOURCE, item.getSource())
                    .putExtra(Constants.intents.INTENT_TRANSLATED, item.getTitle());
            context.startActivity(mIntent);
        });
        mMenu.setOnClickListener(v -> showCxtMenu(context, item));
    }

    private void showCxtMenu(Context ctx, FavoriteItem favoriteItem){
        PopupMenu menu = new PopupMenu(ctx, mMenu);
        menu.inflate(R.menu.menu_popup_favorite);
        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.action_copy:{
                    Utils.copyToClipboard(favoriteItem.getTitle());
                    Snackbar.make(mCard, R.string.translation_copied, Snackbar.LENGTH_SHORT).show();
                    return true;
                }
                case R.id.action_copy_close:{
                    Utils.copyToClipboard(favoriteItem.getTitle());
                    ((HistoryFavActivity) ctx).finish();
                    return true;
                }
                case R.id.action_view_fullscreen:{
                    final Intent fullIntent = new Intent(ctx, FullscreenActivity.class);
                    fullIntent.putExtra(Constants.intents.INTENT_TRANSLATED, favoriteItem.getTitle());
                    ctx.startActivity(fullIntent);
                    return true;
                }
                case R.id.action_share_sys:{
                    share(ctx, favoriteItem);
                    return true;
                }
                case R.id.action_fav_edit:{
                    FavoriteFragment.editItem(ctx, favoriteItem);
                    return true;
                }
                case R.id.action_fav_delete:{
                    new FavoriteDbModel(ctx).deleteItem(favoriteItem.getId());
                    FavoriteAdapter adapter = new FavoriteAdapter(ctx, new ArrayList<>());
                    adapter.removeItem(favoriteItem.getId());
                    FavoriteFragment.refresh();
                    return true;
                }
            }
            return true;
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(ctx, (MenuBuilder) menu.getMenu(), mMenu);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
    }

    private void share(Context ctx, FavoriteItem favoriteItem){
        ctx.startActivity(Intent.createChooser(
                new Intent(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, favoriteItem.getTitle()),
                ctx.getString(R.string.send)
        ));
    }
}

