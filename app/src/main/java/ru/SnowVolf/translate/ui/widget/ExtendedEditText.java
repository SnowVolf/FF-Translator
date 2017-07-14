package ru.SnowVolf.translate.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import ru.SnowVolf.translate.util.TypefaceHelper;

/**
 * Created by Snow Volf on 08.06.2017, 11:11
 */

public class ExtendedEditText extends TextInputEditText {
    Bitmap drawableRight, button2, button3;
    Paint paint;

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
        TypefaceHelper.applyTypeface(this);
    }


    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return super.onKeyShortcut(keyCode, event);
    }
}
