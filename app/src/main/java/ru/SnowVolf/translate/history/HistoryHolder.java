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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.favorite.FavoriteItem;
import ru.SnowVolf.translate.model.FavoriteDbModel;
import ru.SnowVolf.translate.model.HistoryDbModel;
import ru.SnowVolf.translate.ui.activity.FullscreenActivity;
import ru.SnowVolf.translate.ui.activity.HistoryFavActivity;
import ru.SnowVolf.translate.ui.activity.TranslatorActivity;
import ru.SnowVolf.translate.ui.adapter.HistoryAdapter;
import ru.SnowVolf.translate.util.Constants;
import ru.SnowVolf.translate.util.Utils;

/**
 * Created by Snow Volf on 30.05.2017, 11:37
 */

public class HistoryHolder extends RecyclerView.ViewHolder {

    private final RelativeLayout mContainerLayout;
    private final TextView mTitle;
    private final TextView mSummary;
    private final TextView mDate;
    private final ImageView mMenu;

    public HistoryHolder(View view) {
        super(view);
        mContainerLayout = (RelativeLayout) view.findViewById(R.id.row_history_clickable_container);
        mTitle = (TextView) view.findViewById(R.id.row_history_title);
        mSummary = (TextView) view.findViewById(R.id.row_history_source);
        mDate = (TextView) view.findViewById(R.id.row_history_summary);
        mMenu = (ImageView) view.findViewById(R.id.row_history_menu);

    }

    public void setData(Context context, HistoryItem item) {
        String title = item.getTitle();
        if (title == null || title.isEmpty()) {
            title = item.getTranslation();
        }
        mTitle.setText(title);
        mSummary.setText(item.getSource());
        mDate.setText(new SimpleDateFormat(context.getString(R.string.date), Locale.ENGLISH).format(new Date(item.getId())));

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
                case R.id.action_share:{
                    share(ctx, historyItem);
                    return true;
                }
                case R.id.action_history_add_favorite:{
                    new FavoriteDbModel(ctx).addItem(new FavoriteItem(historyItem.getId(), historyItem.getFromPosition(), historyItem.getToPosition(),  historyItem.getTitle(), historyItem.getSource()));
                    Snackbar.make(mContainerLayout, R.string.added_to_favorites, Snackbar.LENGTH_SHORT).show();
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
