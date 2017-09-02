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

import android.graphics.Typeface;
import android.widget.TextView;

import ru.SnowVolf.translate.App;

/**
 * Created by Snow Volf on 08.06.2017, 11:54
 */

public class TypefaceHelper {
    private static Typeface typeface;

    public static void generateTypeface(){
        typeface = Typeface.createFromAsset(App.ctx().getAssets(), "fonts/app_font.ttf");
    }

    public static void applyTypeface(TextView textView){
        textView.setTypeface(typeface);
    }

    public static Typeface getTypeface(){
        return typeface;
    }
}
