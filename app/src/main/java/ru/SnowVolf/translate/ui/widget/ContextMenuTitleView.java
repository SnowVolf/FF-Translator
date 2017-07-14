package ru.SnowVolf.translate.ui.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import ru.SnowVolf.translate.util.Utils;

/**
 * Created by Snow Volf on 27.06.2017, 14:08
 */

public class ContextMenuTitleView extends ScrollView {
    private static final int MAX_HEIGHT_DP = 70;
    private static final int PADDING_DP = 16;

    public ContextMenuTitleView(Context context, String title) {
        super(context);

        int padding = Utils.dpToPx(PADDING_DP);
        setPadding(padding, padding, padding, 0);

        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        titleView.setTextColor(context.getResources().getColor(android.R.color.white));
        addView(titleView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Utils.dpToPx(MAX_HEIGHT_DP), View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
