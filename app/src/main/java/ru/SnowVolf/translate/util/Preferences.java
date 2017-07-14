package ru.SnowVolf.translate.util;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.api.yandex.translate.Translate;


/**
 * Created by Snow Volf on 04.06.2017, 11:06
 */

public class Preferences {
    public static void setSpinnerPosition(String str, int i) {
        App.ctx().getPreferences().edit().putInt(str, i).apply();
    }

    public static int getSpinnerPosition(String str){
        return App.ctx().getPreferences().getInt(str, 0);
    }

    public static boolean isKillAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.PERFORMANCE_KILL, true);
    }

    public static boolean isBackNotif(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.PERFORMANCE_BACK, true);
    }

    public static void setKey(){
        Translate.setKey(App.ctx().getPreferences().getString(Constants.Prefs.API_KEY, ""));
    }

    public static int getFontSize() {
        return App.ctx().getPreferences().getInt(Constants.Prefs.UI_FONTSIZE, 16);
    }

    public static void setFontSize(int value) {
        App.ctx().getPreferences().edit().putInt(Constants.Prefs.UI_FONTSIZE, value).apply();
    }

    public static boolean isLightStatusBar() {
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.UI_LIGHT_STATUS_BAR, true);
    }

    public static boolean isReturnAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.PERFORMANCE_RETURN, true);
    }

    public static boolean isReturnNotif() {
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.PERFORMANCE_RETURN_NOTIFY, false);
    }

    public static void returnNotifDone(){
        App.ctx().getPreferences().edit().putBoolean(Constants.Prefs.PERFORMANCE_RETURN_NOTIFY, true).apply();
    }

    public static boolean isDetectAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.API_DETECT, true);
    }

    public static boolean isDuplicatesNotAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.PERFORMANCE_DUP, false);
    }

    public static boolean isClipboardTranslatable(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.PERFORMANCE_TRANSLATE_FROM_CLIPBOARD, true);
    }

    public static boolean isShowKeyboardAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.PERFORMANCE_KBD, true);
    }

    public static boolean isSyncTranslateAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.API_SYNC, true);
    }

    public static boolean isLogAllowed(){
        return App.ctx().getPreferences().getBoolean("dev.opt", true);
    }

    public static boolean isListAnimAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.UI_LIST_ANIM, true);
    }

    public static String getDefaultLanguage(){
        return App.ctx().getPreferences().getString(Constants.Prefs.SYS_LANG, "default");
    }

    public static boolean isClipboardServiceAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.Prefs.PERFORMANCE_SCAN_CLIP, true);
    }

    public static void setToLang(Language language){
        App.ctx().getPreferences().edit().putString("last.to.lang", language.toString()).apply();
    }

    public static void setFromLang(Language language){
        App.ctx().getPreferences().edit().putString("last.from.lang", language.toString()).apply();
    }

    public static String getToLang(){
        return App.ctx().getPreferences().getString("last.to.lang", Language.AFRICANS.toString());
    }

    public static String getFromLang(){
        return App.ctx().getPreferences().getString("last.from.lang", Language.AFRICANS.toString());
    }
}
