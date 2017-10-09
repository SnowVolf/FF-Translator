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
         else {
            return Html.fromHtml(source.toString(), Html.FROM_HTML_MODE_LEGACY);
        }
    }
}
