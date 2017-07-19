package ru.SnowVolf.translate.api.yandex.language;

import android.support.annotation.Nullable;

/**
 * Created by Snow Volf on 25.05.2017, 11:17
 */

public enum  Language {
    AFRICANS("af"),
    ALBANIAN("sq"),
    AMHARIC("am"),
    ARABIAN("ar"),
    ARMENIAN("hy"),
    AZERBAIJANI("az"),
    BASQUE("eu"),
    BASHKIR("ba"),
    BELARUSIAN("be"),
    BENGALI("bn"),
    BURMESE("my"),
    BULGARIAN("bg"),
    BOSNIAN("bs"),
    CATALAN("ca"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESTONIAN("et"),
    PERSIAN("fa"),
    FINNISH("fi"),
    FRENCH("fr"),
    KAZAKH("kk"),
    JAVANESE("jv"),
    JAPANESE("ja"),
    KANNADA("kn"),
    KHMER("km"),
    KOREAN("ko"),
    KYRGYZ("ky"),
    GALICIAN("gl"),
    GERMAN("de"),
    GEORGIAN("ka"),
    GREEK("el"),
    GUJARATI("gu"),
    HAITIAN("ht"),
    HEBREW("he"),
    HILL_MARI("mrj"),
    HUNGARIAN("hu"),
    ICELANDIC("is"),
    IRISH("ga"),
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
    UKRAINIAN("uk"),
    VIETNAMESE("vi"),
    WELSH("cy"),
    XHOSA("xh"),

    YIDDISH("yi");

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
