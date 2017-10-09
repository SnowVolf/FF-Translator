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

package ru.SnowVolf.translate.preferences;

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
        return App.ctx().getPreferences().getBoolean(Constants.prefs.PERFORMANCE_KILL, false);
    }

    public static boolean isBackNotif(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.PERFORMANCE_BACK, true);
    }

    public static void setKey(){
        Translate.setKey(App.ctx().getPreferences().getString(Constants.prefs.API_KEY, ""));
    }

    public static int getFontSize() {
        return App.ctx().getPreferences().getInt(Constants.prefs.UI_FONTSIZE, 16);
    }

    public static void setFontSize(int value) {
        App.ctx().getPreferences().edit().putInt(Constants.prefs.UI_FONTSIZE, value).apply();
    }

    public static boolean isLightStatusBar() {
        return App.ctx().getPreferences().getBoolean(Constants.prefs.UI_LIGHT_STATUS_BAR, true);
    }

    public static boolean isClearIcon() {
        return App.ctx().getPreferences().getBoolean("notifications.clear", false);
    }

    public static boolean isReturnAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.PERFORMANCE_RETURN, false);
    }

    public static boolean isReturnNotif() {
        return App.ctx().getPreferences().getBoolean(Constants.prefs.PERFORMANCE_RETURN_NOTIFY, false);
    }

    public static void returnNotifDone(){
        App.ctx().getPreferences().edit().putBoolean(Constants.prefs.PERFORMANCE_RETURN_NOTIFY, true).apply();
    }

    public static boolean isDetectAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.API_DETECT, false);
    }

    public static boolean isDuplicatesNotAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.PERFORMANCE_DUP, false);
    }

    public static boolean isClipboardTranslatable(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.PERFORMANCE_TRANSLATE_FROM_CLIPBOARD, false);
    }

    public static boolean isShowKeyboardAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.PERFORMANCE_KBD, false);
    }

    public static boolean isLogAllowed(){
        return App.ctx().getPreferences().getBoolean("dev.opt", false);
    }

    public static boolean isListAnimAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.UI_LIST_ANIM, true);
    }

    public static String getDefaultLanguage(){
        return App.ctx().getPreferences().getString(Constants.prefs.SYS_LANG, "default");
    }

    public static boolean isClipboardServiceAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.PERFORMANCE_SCAN_CLIP, false);
    }

    public static void setToLang(Language language){
        App.ctx().getPreferences().edit().putString("last.to.lang", language.toString()).apply();
    }

    public static void setFromLang(Language language){
        App.ctx().getPreferences().edit().putString("last.from.lang", language.toString()).apply();
    }

    public static String getToLang(){
        return App.ctx().getPreferences().getString("last.to.lang", "");
    }

    public static String getFromLang(){
        return App.ctx().getPreferences().getString("last.from.lang", "");
    }

    public static boolean isSuggestionsAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.PERFORMANCE_SUGGEST, true);
    }

    public static boolean isListenerAllowed(){
        return App.ctx().getPreferences().getBoolean("dev.listener", false);
    }

    public static boolean isUpdateAllowed(){
        return App.ctx().getPreferences().getBoolean("dev.update", true);
    }

    public static boolean isTransitionsAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.UI_ANIM, true);
    }

    public static boolean isToolbarOverrideAllowed(){
        return App.ctx().getPreferences().getBoolean(Constants.prefs.UI_TOOLBAR_COLORING, false);
    }

    public static boolean isNotificationOngoing(){
        return App.ctx().getPreferences().getBoolean("notifications.ongoing", false);
    }

    public static boolean isNotificationVibrate(){
        return App.ctx().getPreferences().getBoolean("notifications.vibrate", false);
    }

    public static boolean isNotificationLights(){
        return App.ctx().getPreferences().getBoolean("notifications.lights", false);
    }

    public static boolean isHistoryReverse(){
        return App.ctx().getPreferences().getBoolean("history.reverse", false);
    }

    public static boolean isFavoriteReverse(){
        return App.ctx().getPreferences().getBoolean("favorite.reverse", true);
    }

    public static int getNotificationAccent(){
        return App.ctx().getPreferences().getInt("notifications.accent", 0x448aff);
    }

    public static boolean isOnlyAlertOnce(){
        return App.ctx().getPreferences().getBoolean("notifications.alert.once", true);
    }

    public static boolean isStartOnBootAllowed(){
        return App.ctx().getPreferences().getBoolean("notifications.boot", false);
    }

    public static boolean isGirlEnabled(){
        return App.ctx().getPreferences().getBoolean("dev.enablegirl", false);
    }

    public static void enableGirl(){
        App.ctx().getPreferences().edit().putBoolean("dev.enablegirl", true).apply();
    }

    public static boolean isRefreshAuto(){
        return App.ctx().getPreferences().getBoolean("lists.auto_refresh", false);
    }
}
