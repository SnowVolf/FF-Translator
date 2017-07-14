package ru.SnowVolf.translate.favorite;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.activity.TranslatorActivity;
import ru.SnowVolf.translate.ui.fragment.historyfav.FavoriteFragment;
import ru.SnowVolf.translate.util.Constants;


/**
 * Created by Snow Volf on 04.06.2017, 22:55
 */

public class FavoriteHolder extends RecyclerView.ViewHolder {
    private final RelativeLayout mCard;
    private final TextView mTitle;
    private final TextView mSubTitle;

    public FavoriteHolder(View view) {
        super(view);
        mCard = (RelativeLayout) view.findViewById(R.id.row_favorite_card);
        mTitle = (TextView) view.findViewById(R.id.row_favorite_title);
        mSubTitle = (TextView) view.findViewById(R.id.row_favorite_subtitle);
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
            mIntent.putExtra(Constants.Intents.INTENT_SOURCE, item.getSource());
            mIntent.putExtra(Constants.Intents.INTENT_TRANSLATED, item.getTitle());
            context.startActivity(mIntent);
        });

        mCard.setOnLongClickListener(v -> FavoriteFragment.editItem(context, item));}
    }

