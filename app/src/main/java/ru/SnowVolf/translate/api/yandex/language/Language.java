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

package ru.SnowVolf.translate.api.yandex.language;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Snow Volf on 21.05.2017, 11:17
 *
 * Класс представляет список языков и интерфейс для их определения из исходной строки
 * В будущем планируется полностью отказаться от него, в пользу запроса списка языков с
 * сервера
 */
public enum  Language {
    AFRICANS("af"),
    ALBANIAN("sq"),
    AMHARIC("am"),
    ARABIAN("ar"),
    ARMENIAN("hy"),
    AZERBAIJANIAN("az"),
    BASHKIR("ba"),
    BASQUE("eu"),
    BELARUSIAN("be"),
    BENGALI("bn"),
    BOSNIAN("bs"),
    BULGARIAN("bg"),
    BURMESE("my"),
    CATALAN("ca"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESTONIAN("et"),
    FINNISH("fi"),
    FRENCH("fr"),
    GALICIAN("gl"),
    GEORGIAN("ka"),
    GERMAN("de"),
    GREEK("el"),
    GUJARATI("gu"),
    HAITIAN("ht"),
    HEBREW("he"),
    HILL_MARI("mrj"),
    HUNGARIAN("hu"),
    ICELANDIC("is"),
    IRISH("ga"),
    ITALIAN("it"),
    JAPANESE("ja"),
    JAVANESE("jv"),
    KANNADA("kn"),
    KAZAKH("kk"),
    KHMER("km"),
    KOREAN("ko"),
    KYRGYZ("ky"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    MACEDONIAN("mk"),
    NORWEGIAN("no"),
    PERSIAN("fa"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SERBIAN("sr"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SPANISH("es"),
    SWEDISH("sv"),
    TURKISH("tr"),
    UKRAINIAN("uk"),
    VIETNAMESE("vi"),
    WELSH("cy"),
    XHOSA("xh"),
    YIDDISH("yi");

    private final String language;

    Language (final String pLanguage){
        language = pLanguage;
    }

    /**
     * Получение объекта Language из заданной строки
     */
    @Nullable
    public static Language fromString(final String pLanguage){
        for (Language l : values()){
            if (l.toString().equals(pLanguage)){
                return l;
            }
        }
        return null;
    }
    @NonNull
    @Override
    public String toString(){
        return language;
    }
}
