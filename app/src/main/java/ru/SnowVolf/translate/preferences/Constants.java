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

/**
 * Created by Snow Volf on 04.06.2017, 6:22
 *
 * Константы приложения
 */
public class Constants {
    public static final class common {
        public static final String UPDATE_URL = "https://raw.githubusercontent.com/SnowVolf/FF-Translator/master/update_girl.json";
        public static final String TAG = "ff-girl";
        public static final String GIRL_ALIAS = "V_8 IRINA";
    }

    public static final class time {
        public static final long SIX_HOUR = 21600/*000*/;
    }

    public static final class prefs {
        public static final String API_KEY = "api.key";
        //public static final String API_SYNC = "api.sync.translate";
        static final String API_DETECT = "api.detect";
        public static final String SYS_LANG = "sys.lang";
        public static final String SYS_PERMISSIONS = "sys.permissions";
        public static final String UI_THEME = "ui.theme";
        public static final String UI_FONTSIZE = "ui.fontsize";
        public static final String UI_FONT_SIZE = "ui.font.size";
        public static final String UI_LIGHT_STATUS_BAR ="ui.blackicons";
        static final String UI_LIST_ANIM = "ui.list.anim";
        public static final String UI_ACCENT = "ui.accent";
        public static final String UI_TOOLBAR = "ui.toolbar";
        static final String UI_TOOLBAR_COLORING = "ui.toolbar.coloring";
        public static final String OTHER_VERSION = "other.version";
        public static final String SPINNER_1 = "saved1";
        public static final String SPINNER_2 = "saved2";
        static final String PERFORMANCE_DUP = "performance.duplicates";
        public static final String PERFORMANCE_RETURN = "performance.return";
        static final String PERFORMANCE_TRANSLATE_FROM_CLIPBOARD = "performance.translate.clipboard";
        static final String PERFORMANCE_RETURN_NOTIFY = "performance.return.notify";
        static final String PERFORMANCE_BACK = "performance.backpressed";
        static final String PERFORMANCE_KILL = "performance.kill";
        static final String PERFORMANCE_KBD = "performance.keyboard";
        static final String PERFORMANCE_SCAN_CLIP = "performance.scan.clipboard";
        static final String PERFORMANCE_SUGGEST = "performance.suggestions";
        static final String UI_ANIM = "ui.anim";
    }

    public static final class intents {
        public static final String INTENT_TRANSLATED = "history.translated";
        public static final String INTENT_SOURCE = "history.source";
        public static final String INTENT_FROM = "language.from_pos";
        public static final String INTENT_TO = "language.to_pos";
        public static final String INTENT_FORCE_TRANSLATE = "intent.force.translate";
    }
    
    public static final class historyDb {
        // ++ V_1
        public static final int DB_VERSION = 6;
        public static final String DB_NAME = "GirlHistory.db";
        public static final String DB_TABLE_HISTORY = "history";
        public static final String KEY_ID = "id";
        // ++ V_2
        public static final String KEY_TITLE = "title";
        @Deprecated
        public static final String KEY_SOURCE = "source";
        // ++ V_3
        public static final String KEY_TRANSLATE = "translate";
        // ++ V_4
        public static final String KEY_FROM_INT = "from_pos";
        public static final String KEY_TO_INT = "to_pos";
        // ++ V_6
        public static final String KEY_IN_FAVORITE = "in_favorites";
        public static final String KEY_FROM_CODE = "code_from";
        public static final String KEY_TO_CODE = "code_to";
    }
    
    public static final class favDb {
        public static final int DB_VERSION = 5;
        public static final String DB_NAME = "GirlFavorite.db";
        public static final String DB_TABLE_FAVORITES = "favorites";
        public static final String KEY_ID = "id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_SOURCE = "source";
        public static final String KEY_FROM_INT = "from_pos";
        public static final String KEY_TO_INT = "to_pos";
    }
}
