package ru.SnowVolf.translate.favorite;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.model.FavoriteDbModel;
import ru.SnowVolf.translate.ui.activity.TranslatorActivity;
import ru.SnowVolf.translate.ui.adapter.FavoriteAdapter;
import ru.SnowVolf.translate.ui.fragment.historyfav.FavoriteFragment;
import ru.SnowVolf.translate.util.Constants;
import ru.SnowVolf.translate.util.Utils;


/**
 * Created by Snow Volf on 04.06.2017, 22:55
 */

public class FavoriteHolder extends RecyclerView.ViewHolder {
    private final RelativeLayout mCard;
    private final TextView mTitle;
    private final TextView mSubTitle;
    private final ImageView mMenu;

    public FavoriteHolder(View view) {
        super(view);
        mCard = (RelativeLayout) view.findViewById(R.id.row_favorite_card);
        mTitle = (TextView) view.findViewById(R.id.row_favorite_title);
        mSubTitle = (TextView) view.findViewById(R.id.row_favorite_subtitle);
        mMenu = (ImageView) view.findViewById(R.id.row_favorite_menu);
    }

    public void setData(Context context, FavoriteItem item) {
        String title = item.getTitle();
        String summary = item.getSource();
        if (title == null || title.isEmpty()) {
            title = item.getSource().split("/")[2];
        }
        mTitle.setText(title);
        mSubTitle.setText(summary);

        mCard.setOnClickListener(v -> {
            Intent mIntent = new Intent(context, TranslatorActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mIntent.putExtra(Constants.Intents.INTENT_FROM, item.getFromPosition());
            mIntent.putExtra(Constants.Intents.INTENT_TO, item.getToPosition());
            mIntent.putExtra(Constants.Intents.INTENT_SOURCE, item.getSource());
            mIntent.putExtra(Constants.Intents.INTENT_TRANSLATED, item.getTitle());
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
                case R.id.action_share:{
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
        menu.show();
    }

    private void share(Context ctx, FavoriteItem favoriteItem){
        ctx.startActivity(Intent.createChooser(
                new Intent(Intent.ACTION_SEND).setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, favoriteItem.getTitle()),
                ctx.getString(R.string.send)
        ));
    }
}

