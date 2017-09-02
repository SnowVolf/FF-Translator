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
