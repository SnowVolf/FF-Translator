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
