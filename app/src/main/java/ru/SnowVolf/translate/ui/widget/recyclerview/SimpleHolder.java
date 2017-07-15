package ru.SnowVolf.translate.ui.widget.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.View;
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

    public SimpleHolder(View view) {
        super(view);
        mContainer = (RelativeLayout) view.findViewById(R.id.row_simple_layout);
        mTitle = (TextView) view.findViewById(R.id.row_simple_title);
        mSubTitle = (TextView) view.findViewById(R.id.row_simple_subtitle);
    }

    public void setData(SimpleItem item) {
        String title = item.getTitle();
        String subtitle = item.getOwner();
        if (subtitle == null || subtitle.isEmpty()) {
            mSubTitle.setVisibility(View.INVISIBLE);
        }
        mTitle.setText(title);
        mSubTitle.setText(subtitle);

        mContainer.setOnLongClickListener(v -> false);
    }
}
