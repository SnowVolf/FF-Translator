package ru.SnowVolf.translate.api.yandex.language;

import android.support.annotation.Nullable;

/**
 * Created by Snow Volf on 25.05.2017, 11:17
 */

public enum  Language {
    AFRICANS("af"),
    ALBANIAN("sq"),
    ARABIAN("ar"),
    ARMENIAN("hy"),
    AZERBAIJANI("az"),
    BELORUSSIAN("be"),
    BULGARIAN("bg"),
    CATALAN("ca"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESTONIAN("et"),
    FINNISH("fi"),
    FRENCH("fr"),
    GERMAN("de"),
    GEORGIAN("ka"),
    GREEK("el"),
    HUNGARIAN("hu"),
    ITALIAN("it"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    MACEDONIAN("mk"),
    NORWEGIAN("no"),
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
    UKRAINIAN("uk");

    private final String language;

    Language (final String pLanguage){
        language = pLanguage;
    }

    @Nullable
    public static Language fromString(final String pLanguage){
        for (Language l : values()){
            if (l.toString().equals(pLanguage)){
                return l;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return language;
    }
}
