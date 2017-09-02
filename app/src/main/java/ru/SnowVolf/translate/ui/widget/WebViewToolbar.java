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
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.util.Utils;

/**
 * Created by Snow Volf on 27.06.2017, 13:38
 */

public class WebViewToolbar extends Toolbar{
    private Context mContext;
    private Paint mPaint;

    private int progress;
    private boolean drawProgress;

    private String mTitle;
    private String mURL;

    public WebViewToolbar(Context context) {
        this(context, null);
    }

    public WebViewToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public WebViewToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mPaint = new Paint();

        setTitleTextAppearance(mContext, R.style.TextAppearance_AppCompat_Body2);
        setSubtitleTextAppearance(mContext, R.style.TextAppearance_AppCompat_Widget_ActionBar_Subtitle);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!drawProgress) {
            return;
        }
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.accent_grey));
        canvas.drawRect(0, canvas.getHeight() - Utils.dpToPx(2), canvas.getWidth(), canvas.getHeight(), mPaint);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.accent_red));
        canvas.drawRect(0, canvas.getHeight() - Utils.dpToPx(2), canvas.getWidth() * (float) progress / 100f, canvas.getHeight(), mPaint);
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;

        if (progress == 100) {
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    drawProgress = false;
                }
            }, 800);
        }/* else {
            drawProgress = true;
        }*/
    }

    public boolean getCanDrawProgress() {
        return drawProgress;
    }

    public void setCanDrawProgress(boolean drawProgress) {
        this.drawProgress = drawProgress;
    }
}
