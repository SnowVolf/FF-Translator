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

package ru.SnowVolf.translate;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Locale;

import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.clipboard.AppUtils;
import ru.SnowVolf.translate.clipboard.ClipboardWatcherService;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.util.runtime.Logger;

import static org.acra.ReportField.ANDROID_VERSION;
import static org.acra.ReportField.APP_VERSION_CODE;
import static org.acra.ReportField.APP_VERSION_NAME;
import static org.acra.ReportField.LOGCAT;
import static org.acra.ReportField.PHONE_MODEL;
import static org.acra.ReportField.STACK_TRACE;

/**
 * Created by Snow Volf on 28.05.2017, 6:31
 *
 * Extend the App class so we can get a {@link Context} anywhere
 */
@ReportsCrashes(
        mailTo = "svolf15@yandex.ru",
        customReportContent = {APP_VERSION_CODE, APP_VERSION_NAME, ANDROID_VERSION, PHONE_MODEL, STACK_TRACE, LOGCAT},
        mode = ReportingInteractionMode.NOTIFICATION,
        resNotifTickerText = R.string.error,
        resNotifTitle = R.string.acra_app_crashed,
        resNotifText = R.string.acra_notif_text,
        resNotifIcon = android.R.drawable.stat_notify_error,
        resDialogText = R.string.acra_email_promt,
        resDialogTheme = R.style.CrashDialog,
        resDialogIcon = R.mipmap.ic_launcher,
        resDialogTitle = R.string.acra_app_crashed
)

public class App extends Application {
    private static App INSTANCE = new App();
    private SharedPreferences preferences;
    Locale locale;
    String lang;

    public App() {
        INSTANCE = this;
    }

    public static App ctx() {
        return INSTANCE;
    }

    public static Context getContext() {
        return ctx();
    }

    public void onCreate(){
        super.onCreate();
        // Задание настроек по умолчанию
        PreferenceManager.setDefaultValues(this, R.xml.preferences_dev, false);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_ui, false);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_api, false);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_system, false);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_interaction, false);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_notifications, false);
        ACRA.init(this);

        // Изменение языка
        // TODO: Переделать под новый, не deprecated api [02.08.2017]
        Configuration config = getResources().getConfiguration();
        lang = Preferences.getDefaultLanguage();
        if (lang.equals("default"))
            lang = config.locale.getLanguage();
        locale = new Locale(lang);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, null);

        if (Preferences.isClipboardServiceAllowed() && !AppUtils.isMyServiceRunning(ClipboardWatcherService.class)){
            startService(new Intent(this, ClipboardWatcherService.class));
        }
        final String SUKA = "Ты что тут забыл?! Пидор блядь!!! Пошёл нахуй!!! Добра тебе сука. :-D";
        Logger.log(SUKA);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
            Configuration config = getResources().getConfiguration();
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getResources().updateConfiguration(config, null);
    }

    public SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
        return preferences;
    }

    public static Language[] langs = {
            Language.AZERBAIJANIAN,
            Language.ALBANIAN,
            Language.ENGLISH,
            Language.ARABIAN,
            Language.ARMENIAN,
            Language.AFRICANS,
            Language.AMHARIC,
            Language.BASQUE,
            Language.BASHKIR,
            Language.BELARUSIAN,
            Language.BENGALI,
            Language.BURMESE,
            Language.BULGARIAN,
            Language.BOSNIAN,
            Language.WELSH,
            Language.HUNGARIAN,
            Language.VIETNAMESE,
            Language.HAITIAN,
            Language.GALICIAN,
            Language.GERMAN,
            Language.DUTCH,
            Language.HILL_MARI,
            Language.GREEK,
            Language.GEORGIAN,
            Language.GUJARATI,
            Language.DANISH,
            Language.HEBREW,
            Language.YIDDISH,
            Language.IRISH,
            Language.ICELANDIC,
            Language.SPANISH,
            Language.ITALIAN,
            Language.KAZAKH,
            Language.KANNADA,
            Language.CATALAN,
            Language.KYRGYZ,
            Language.KOREAN,
            Language.XHOSA,
            Language.KHMER,
            Language.LATVIAN,
            Language.LITHUANIAN,
            Language.MACEDONIAN,
            Language.NORWEGIAN,
            Language.PERSIAN,
            Language.POLISH,
            Language.PORTUGUESE,
            Language.ROMANIAN,
            Language.RUSSIAN,
            Language.SERBIAN,
            Language.SLOVAK,
            Language.SLOVENIAN,
            Language.TURKISH,
            Language.UKRAINIAN,
            Language.FINNISH,
            Language.FRENCH,
            Language.CROATIAN,
            Language.CZECH,
            Language.SWEDISH,
            Language.ESTONIAN,
            Language.JAVANESE,
            Language.JAPANESE
    };

    public static String injectString(@StringRes int resId){
        return ctx().getString(resId);
    }
}
