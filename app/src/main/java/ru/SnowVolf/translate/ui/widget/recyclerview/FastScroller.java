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

package ru.SnowVolf.translate.ui.widget.recyclerview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Snow Volf on 07.08.2017, 11:25
 *
 * FastScroller для {@link android.support.v7.widget.RecyclerView}
 */

public class FastScroller extends LinearLayout {
    private static final int TRACK_SNAP_RANGE = 5;

    private View handle;
    private RecyclerView recyclerView;
    private int height;
    private boolean isInitialized = false;
    private ObjectAnimator currentAnimator = null;

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            updateHandlerPosition();
        }
    };

    public FastScroller(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FastScroller(final Context context) {
        super(context);
        init();
    }

    public FastScroller(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        if (isInitialized)
            return;
        isInitialized = true;
        setOrientation(HORIZONTAL);
        setClipChildren(false);
    }

    public void setViewsToUse(@LayoutRes int layoutResId, @IdRes int handleResId) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(layoutResId, this, true);
        handle = findViewById(handleResId);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        updateHandlerPosition();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < handle.getX() - ViewCompat.getPaddingStart(handle))
                    return false;
                if (currentAnimator != null)
                    currentAnimator.cancel();
                handle.setSelected(true);
            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();
                setHandlePosition(y);
                setRecyclerViewPosition(y);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handle.setSelected(false);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setRecyclerView(final RecyclerView recyclerView) {
        if (this.recyclerView != recyclerView) {
            if (this.recyclerView != null)
                this.recyclerView.removeOnScrollListener(onScrollListener);
            this.recyclerView = recyclerView;
            if (this.recyclerView == null)
                return;
            recyclerView.addOnScrollListener(onScrollListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (recyclerView != null) {
            recyclerView.removeOnScrollListener(onScrollListener);
            recyclerView = null;
        }
    }

    private void setRecyclerViewPosition(float y) {
        if (recyclerView != null) {
            final int itemCount = recyclerView.getAdapter().getItemCount();
            float proportion;
            if (handle.getY() == 0)
                proportion = 0f;
            else if (handle.getY() + handle.getHeight() >= height - TRACK_SNAP_RANGE)
                proportion = 1f;
            else
                proportion = y / (float) height;
            final int targetPos = getValueInRange(0, itemCount - 1, (int) (proportion * (float) itemCount));
            ((RecyclerViewLinearManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(targetPos, 0);
        }
    }

    private int getValueInRange(int min, int max, int value) {
        int minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    private void updateHandlerPosition() {
        if (handle.isSelected())
            return;

        final int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
        final int verticalScrollRange = recyclerView.computeVerticalScrollRange();
        float proportion = (float) verticalScrollOffset / ((float) verticalScrollRange - height);
        setHandlePosition(height * proportion);
    }

    private void setHandlePosition(float y) {
        final int handleHeight = handle.getHeight();
        handle.setY(getValueInRange(0, height - handleHeight, (int) (y - handleHeight / 2)));
    }
}

