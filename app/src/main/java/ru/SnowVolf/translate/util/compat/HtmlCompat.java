package ru.SnowVolf.translate.util.compat;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by Snow Volf on 16.06.2017, 1:06
 */

public class HtmlCompat {
    public static Spanned fromHtmlSupport(StringBuilder source){
        if (Build.VERSION.SDK_INT < 24)
            //noinspection deprecation
            return Html.fromHtml(source.toString());
         else
            return Html.fromHtml(source.toString(), Html.FROM_HTML_MODE_LEGACY);
    }
}
