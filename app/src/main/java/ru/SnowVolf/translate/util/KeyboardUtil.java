package ru.SnowVolf.translate.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import ru.SnowVolf.translate.App;

/**
 * Created by Snow Volf on 25.06.2017, 20:19
 */

public class KeyboardUtil {
    public static void hideKeyboard(Activity act){
        if (act != null && act.getCurrentFocus() != null){
            InputMethodManager manager = (InputMethodManager) App.ctx().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showKeyboard(){
        InputMethodManager manager = (InputMethodManager) App.ctx().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
