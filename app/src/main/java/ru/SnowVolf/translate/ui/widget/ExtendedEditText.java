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
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.Scroller;

import com.onurciner.oxswipe.OXSwipe;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.util.KeyboardUtil;
import ru.SnowVolf.translate.util.TypefaceHelper;

/**
 * Класс расширяющий возможности стандартного EditText
 *
 * Created by Snow Volf on 08.06.2017, 11:11
 */

public class ExtendedEditText extends TextInputEditText {
    public ExtendedEditText(@NonNull Context context) {
        super(context);
        init();
    }

    public ExtendedEditText(@NonNull Context ctx, AttributeSet attrs){
        super(ctx, attrs);
        init();
    }

    public ExtendedEditText(@NonNull Context ctx, AttributeSet attrs, int defStyleArr){
        super(ctx, attrs, defStyleArr);
        init();
    }

    private void init(){
        if (isInEditMode()) return;
        setInputType(getInputType() | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        setImeOptions(getImeOptions() | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        // Отключение spell check
        if (!Preferences.isSuggestionsAllowed())
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        TypefaceHelper.applyTypeface(this);
        if (Preferences.isListenerAllowed()) {
            setTextIsSelectable(true);
            setScroller(new Scroller(App.getContext()));
            setVerticalScrollBarEnabled(true);
            setMovementMethod(new ScrollingMovementMethod());
            setOnLongClickListener(v -> {
                performLongClick();
                return true;
            });
            setOnTouchListener(new OXSwipe() {
                @Override
                public void leftSwipe() {
                    super.leftSwipe();
                    setText("");

                }

                @Override
                public void oneTouch() {
                    super.oneTouch();
                    requestFocusFromTouch();
                    KeyboardUtil.showKeyboard();
                    moveCursorToVisibleOffset();
                }
            });
        }
    }
}
