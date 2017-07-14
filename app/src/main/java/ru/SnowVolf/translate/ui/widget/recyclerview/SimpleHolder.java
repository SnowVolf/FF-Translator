package ru.SnowVolf.translate.ui.widget.recyclerview;


import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 15.06.2017, 4:54
 */

public class SimpleHolder extends RecyclerView.ViewHolder {
    private final RelativeLayout mContainer;
    private final TextView mTitle;
    private final TextView mSubTitle;
    private final ImageView mLogo;

    public SimpleHolder(View view) {
        super(view);
        mContainer = (RelativeLayout) view.findViewById(R.id.row_simple_layout);
        mTitle = (TextView) view.findViewById(R.id.row_simple_title);
        mSubTitle = (TextView) view.findViewById(R.id.row_simple_subtitle);
        mLogo = (ImageView) view.findViewById(R.id.row_simple_icon);
    }

    public void setData(SimpleItem item) {
        String title = item.getTitle();
        String subtitle = item.getOwner();
        int logo = item.getLogo();
        if (logo == 0){
            mLogo.setVisibility(View.GONE);
        } else {
            mLogo.setVisibility(View.VISIBLE);
            mLogo.setImageResource(item.getLogo());
        }
        if (subtitle == null || subtitle.isEmpty()) {
            mSubTitle.setVisibility(View.INVISIBLE);
        }
        mTitle.setText(title);
        mSubTitle.setText(subtitle);

        mContainer.setOnClickListener(v -> item.getListener());

        mContainer.setOnLongClickListener(v -> false);
    }
}
