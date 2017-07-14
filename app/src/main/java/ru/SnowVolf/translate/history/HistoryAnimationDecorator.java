package ru.SnowVolf.translate.history;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 30.05.2017, 21:18
 */

public class HistoryAnimationDecorator extends RecyclerView.ItemDecoration {

    private final Drawable mBackground;

    public HistoryAnimationDecorator(Context context) {
        mBackground = new ColorDrawable(ContextCompat.getColor(context, R.color.md_red_500));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!parent.getItemAnimator().isRunning()) {
            super.onDraw(c, parent, state);
            return;
        }

        View firstComingUp = null;
        View lastComingDown = null;
        int left = 0;
        int top = 0;
        int right = parent.getWidth();
        int bottom = 0;

        int size = parent.getLayoutManager().getChildCount();
        for (int i = 0; i < size; i++) {
            View child = parent.getLayoutManager().getChildAt(i);
            if (child.getTranslationY() < 0) {
                lastComingDown = child;
            } else if (child.getTranslationY() > 0 && firstComingUp == null) {
                firstComingUp = child;
            }
        }

        if (firstComingUp != null && lastComingDown != null) {
            top = lastComingDown.getBottom() + (int) lastComingDown.getTranslationY();
            bottom = firstComingUp.getTop() + (int) firstComingUp.getTranslationY();
        } else if (firstComingUp != null) {
            top = firstComingUp.getTop();
            bottom = firstComingUp.getTop() + (int) firstComingUp.getTranslationY();
        } else if (lastComingDown != null) {
            top = lastComingDown.getBottom() + (int) lastComingDown.getTranslationY();
            bottom = lastComingDown.getBottom();
        }

        mBackground.setBounds(left, top, right, bottom);
        mBackground.draw(c);

        super.onDraw(c, parent, state);
    }


}


