package ru.SnowVolf.translate.util;

/**
 * Created by Snow Volf on 04.06.2017, 6:22
 */

public class Constants {
    public static final class Common{
        public static final String TAG = "ff-girl";
        public static final String GIRL_ALIAS = "V_6 GOLD";
    }

    public static final class Prefs{
        public static final String API_KEY = "api.key";
        public static final String API_SYNC = "api.sync.translate";
        public static final String API_DETECT = "api.detect";
        public static final String SYS_LANG = "sys.lang";
        public static final String SYS_PERMISSIONS = "sys.permissions";
        public static final String UI_THEME = "ui.theme";
        public static final String UI_FONTSIZE = "ui.fontsize";
        public static final String UI_FONT_SIZE = "ui.font.size";
        public static final String UI_LIGHT_STATUS_BAR ="ui.blackicons";
        public static final String UI_LIST_ANIM = "ui.list.anim";
        public static final String UI_ACCENT = "ui.accent";
        public static final String OTHER_GUIDE = "other.guide";
        public static final String OTHER_VERSION = "other.version";
        public static final String OTHER_POLICY = "other.policy";
        public static final String SPINNER_1 = "saved1";
        public static final String SPINNER_2 = "saved2";
        public static final String PERFORMANCE_DUP = "performance.duplicates";
        public static final String PERFORMANCE_RETURN = "performance.return";
        public static final String PERFORMANCE_TRANSLATE_FROM_CLIPBOARD = "performance.translate.clipboard";
        public static final String PERFORMANCE_RETURN_NOTIFY = "performance.return.notif";
        public static final String PERFORMANCE_BACK = "performance.backpressed";
        public static final String PERFORMANCE_KILL = "performance.kill";
        public static final String PERFORMANCE_KBD = "performance.keyboard";
        public static final String PERFORMANCE_SCAN_CLIP = "performance.scan.clipboard";
    }

    public static final class Intents{
        public static final String INTENT_TRANSLATED = "history.translated";
        public static final String INTENT_SOURCE = "history.source";
    }
    
    public static final class DatabaseHistory {
        public static final int DB_VERSION = 3;
        public static final String DB_NAME = "GirlHistory";
        public static final String DB_TABLE_HISTORY = "history";
        public static final String KEY_ID = "id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_SOURCE = "source";
        public static final String KEY_TRANSLATE = "translate";
    }
    
    public static final class DatabaseFavorites{
        public static final int DB_VERSION = 3;
        public static final String DB_NAME = "GirlFavorite";
        public static final String DB_TABLE_FAVORITES = "favorites";
        public static final String KEY_ID = "id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_SOURCE = "source";
    }

    public static final class Licenses{
        public static final int LICENSE_APACHE = 0;
    }
}
