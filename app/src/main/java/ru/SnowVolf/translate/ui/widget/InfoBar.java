package ru.SnowVolf.translate.ui.widget;

import android.animation.Animator;
import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 27.06.2017, 14:26
 */

public class InfoBar {


        static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();

        private static final int ANIMATION_DURATION = 250;

        private ViewGroup mTargetParent;
        private Context mContext;
        private InfoBarLayout mView;

        private ImageButton mCloseButton;
        private Button mPositiveButton;
        private Button mNegativeButton;
        private TextView mTextView;

        public InfoBar(ViewGroup parent) {
            mTargetParent = parent;
            mContext = parent.getContext();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            mView = (InfoBarLayout) inflater.inflate(R.layout.infobar, mTargetParent, false);
            mView.setVisibility(View.GONE);

            mCloseButton = (ImageButton) mView.findViewById(R.id.infobar_close);
            mCloseButton.setOnClickListener(v -> hide());

            mTextView = (TextView) mView.findViewById(R.id.infobar_text);

            mPositiveButton = (Button) mView.findViewById(R.id.infobar_positive);
            mPositiveButton.setVisibility(View.GONE);
            mNegativeButton = (Button) mView.findViewById(R.id.infobar_negative);
            mNegativeButton.setVisibility(View.GONE);

            mTargetParent.addView(mView);
        }

        public void setMessage(int textId) {
            mTextView.setText(textId);
        }

        public void setMessage(CharSequence text) {
            mTextView.setText(text);
        }

        public void setPositiveButton(int textId, View.OnClickListener listener) {
            mPositiveButton.setText(mContext.getText(textId));
            mPositiveButton.setOnClickListener(listener);
            mPositiveButton.setVisibility(View.VISIBLE);
        }

        public void setPositiveButton(CharSequence text, View.OnClickListener listener) {
            mPositiveButton.setText(text);
            mPositiveButton.setOnClickListener(listener);
            mPositiveButton.setVisibility(View.VISIBLE);
        }

        public void setNegativeButton(int textId, View.OnClickListener listener) {
            mNegativeButton.setText(mContext.getText(textId));
            mNegativeButton.setOnClickListener(listener);
            mNegativeButton.setVisibility(View.VISIBLE);
        }

        public void setNegativeButton(CharSequence text, View.OnClickListener listener) {
            mNegativeButton.setText(text);
            mNegativeButton.setOnClickListener(listener);
            mNegativeButton.setVisibility(View.VISIBLE);
        }

        public void animateView(int state) {
            if (mView.getTranslationY() > mView.getHeight() / 2 && state == 0) {
                mView.animate()
                        .translationY(mView.getHeight())
                        .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                        .setDuration(ANIMATION_DURATION)
                        .start();
            } else {
                mView.animate()
                        .translationY(0f)
                        .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                        .setDuration(ANIMATION_DURATION)
                        .start();
            }
        }

        private void animateViewIn() {
            mView.setTranslationY(mView.getHeight());
            mView.animate()
                    .translationY(0f)
                    .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .start();
        }

        private void animateViewOut() {
            mView.animate()
                    .translationY(mView.getHeight())
                    .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mTargetParent.removeView(mView);
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                    })
                    .start();
        }

        public void show() {
            mView.setVisibility(View.VISIBLE);
            mView.setOnLayoutChangeListener((view, left, top, right, bottom) -> {
                animateViewIn();
                mView.setOnLayoutChangeListener(null);
            });
        }

        public void hide() {
            animateViewOut();
        }

        public View getView() {
            return mView;
        }
    }
