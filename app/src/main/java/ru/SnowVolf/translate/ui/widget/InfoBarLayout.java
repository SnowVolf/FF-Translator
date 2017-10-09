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

package ru.SnowVolf.translate.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 27.06.2017, 13:44
 */

public class InfoBarLayout extends FrameLayout {
    public interface OnLayoutChangeListener {
        void onLayoutChange(View view, int left, int top, int right, int bottom);
    }

    private OnLayoutChangeListener mOnLayoutChangeListener;

    private Drawable mLineDrawable;

    public InfoBarLayout(Context context) {
        super(context);
    }

    public InfoBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mLineDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.md_red_500));
    }

    public void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
        mOnLayoutChangeListener = onLayoutChangeListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && mOnLayoutChangeListener != null) {
            mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mLineDrawable.setBounds(0, 0, canvas.getWidth(), 2);
            mLineDrawable.draw(canvas);
        }
    }
}
