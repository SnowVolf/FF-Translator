package ru.SnowVolf.translate;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Locale;

import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.clipboard.ClipboardService;
import ru.SnowVolf.translate.util.Preferences;
import ru.SnowVolf.translate.util.runtime.Logger;

import static org.acra.ReportField.ANDROID_VERSION;
import static org.acra.ReportField.APP_VERSION_CODE;
import static org.acra.ReportField.APP_VERSION_NAME;
import static org.acra.ReportField.LOGCAT;
import static org.acra.ReportField.PHONE_MODEL;
import static org.acra.ReportField.STACK_TRACE;

/**
 * Created by Snow Volf on 28.05.2017, 6:31
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
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_dev, false);
        ACRA.init(this);

        //Lang
        Configuration config = getResources().getConfiguration();
        lang = Preferences.getDefaultLanguage();
        if (lang.equals("default"))
            lang = config.locale.getLanguage();
        locale = new Locale(lang);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, null);

        if (Preferences.isClipboardServiceAllowed()){
            startService(new Intent(this, ClipboardService.class));
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
            // 3 columns
            Language.AFRICANS, Language.ALBANIAN, Language.ARABIAN,
            Language.ARMENIAN, Language.AZERBAIJANI, Language.BASQUE,
            Language.BASHKIR, Language.BELARUSIAN, Language.BENGALI,
            Language.BURMESE, Language.BULGARIAN, Language.BOSNIAN,
            Language.WELSH, Language.VIETNAMESE, Language.CATALAN,
            Language.CROATIAN, Language.CZECH, Language.DANISH,
            Language.DUTCH, Language.ENGLISH, Language.ESTONIAN,
            Language.FINNISH, Language.FRENCH, Language.KAZAKH,
            Language.XHOSA, Language.JAVANESE, Language.JAPANESE,
            Language.KANNADA, Language.KHMER, Language.KOREAN,
            Language.KYRGYZ, Language.GALICIAN, Language.GERMAN,
            Language.GEORGIAN, Language.GREEK, Language.GUJARATI,
            Language.HAITIAN, Language.HEBREW, Language.HUNGARIAN,
            Language.YIDDISH, Language.HILL_MARI, Language.ICELANDIC,
            Language.IRISH, Language.ITALIAN, Language.LATVIAN,
            Language.LITHUANIAN, Language.MACEDONIAN, Language.NORWEGIAN,
            Language.PERSIAN, Language.POLISH, Language.PORTUGUESE,
            Language.ROMANIAN, Language.RUSSIAN, Language.SERBIAN,
            Language.SLOVAK, Language.SLOVENIAN, Language.SPANISH,
            Language.SWEDISH, Language.TURKISH, Language.UKRAINIAN
    };
}
