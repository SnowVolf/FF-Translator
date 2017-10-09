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

package ru.SnowVolf.translate.ui.interfacer;

import android.app.Activity;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.preferences.Constants;

/**
 * Created by Snow Volf on 05.06.2017, 9:30
 */

public abstract class ThemeWrapper {
    private enum Theme{
        LIGHT,
        DARK,
        BLACK
    }

    private enum Accent{
        WHITE,
        RED,
        PINK,
        PURPLE,
        INDIGO,
        BLUE,
        LBLUE,
        CYAN,
        TEAL,
        GREEN,
        LGREEN,
        LIME,
        YELLOW,
        AMBER,
        ORANGE,
        DORANGE,
        BROWN,
        GREY,
        BGREY
    }

    private enum ToolbarTheme{
        RED,
        PINK,
        PURPLE,
        DPURPLE,
        INDIGO,
        BLUE,
        LBLUE,
        CYAN,
        TEAL,
        GREEN,
        LGREEN,
        LIME,
        YELLOW,
        AMBER,
        ORANGE,
        DORANGE,
        BROWN,
        GREY,
        BGREY
    }
    
    public static void applyTheme(Activity ctx){
        int theme;
        switch (Theme.values()[getThemeIndex()]){
            case LIGHT:
                theme = R.style.AppTheme;
                break;
            case DARK:
                theme = R.style.AppTheme_Dark;
                break;
            case BLACK:
                theme = R.style.AppTheme_Black;
                break;
            default:
                theme = R.style.AppTheme;
                break;
        }
        ctx.setTheme(theme);
    }

    public static void applyToolbarTheme(Activity ctx){
        int toolbarTheme;
        switch (ToolbarTheme.values()[getToolbarThemeIndex()]){
            case RED:
                toolbarTheme = R.style.RedPrimary;
                break;
            case PINK:
                toolbarTheme = R.style.PinkPrimary;
                break;
            case PURPLE:
                toolbarTheme = R.style.PurplePrimary;
                break;
            case DPURPLE:
                toolbarTheme = R.style.DeepPurplePrimary;
                break;
            case INDIGO:
                toolbarTheme = R.style.IndigoPrimary;
                break;
            case BLUE:
                toolbarTheme = R.style.BluePrimary;
                break;
            case LBLUE:
                toolbarTheme = R.style.LightBluePrimary;
                break;
            case CYAN:
                toolbarTheme = R.style.CyanPrimary;
                break;
            case TEAL:
                toolbarTheme = R.style.TealPrimary;
                break;
            case GREEN:
                toolbarTheme = R.style.GreenPrimary;
                break;
            case LGREEN:
                toolbarTheme = R.style.LightGreenPrimary;
                break;
            case LIME:
                toolbarTheme = R.style.LimePrimary;
                break;
            case YELLOW:
                toolbarTheme = R.style.YellowPrimary;
                break;
            case AMBER:
                toolbarTheme = R.style.AmberPrimary;
                break;
            case ORANGE:
                toolbarTheme = R.style.OrangePrimary;
                break;
            case DORANGE:
                toolbarTheme = R.style.DeepOrangePrimary;
                break;
            case BROWN:
                toolbarTheme = R.style.BrownPrimary;
                break;
            case GREY:
                toolbarTheme = R.style.GreyPrimary;
                break;
            case BGREY:
                toolbarTheme = R.style.BlueGreyPrimary;
                break;
            default:
                toolbarTheme = R.style.BluePrimary;
                break;
        }
        ctx.getTheme().applyStyle(toolbarTheme, true);
    }
    
    public static void applyAccent(Activity ctx){
        int accent;
        switch (Accent.values()[getAccentIndex()]){
            case WHITE:
                accent = R.style.AccentWhite;
                break;
            case RED:
                accent = R.style.AccentRed;
                break;
            case PINK:
                accent = R.style.AccentPink;
                break;
            case PURPLE:
                accent = R.style.AccentPurple;
                break;
            case INDIGO:
                accent = R.style.AccentIndigo;
                break;
            case BLUE:
                accent = R.style.AccentBlue;
                break;
            case LBLUE:
                accent = R.style.AccentLBlue;
                break;
            case CYAN:
                accent = R.style.AccentCyan;
                break;
            case TEAL:
                accent = R.style.AccentTeal;
                break;
            case GREEN:
                accent = R.style.AccentGreen;
                break;
            case LGREEN:
                accent = R.style.AccentLGreen;
                break;
            case LIME:
                accent = R.style.AccentLime;
                break;
            case YELLOW:
                accent = R.style.AccentYellow;
                break;
            case AMBER:
                accent = R.style.AccentAmber;
                break;
            case ORANGE:
                accent = R.style.AccentOrange;
                break;
            case DORANGE:
                accent = R.style.AccentDOrange;
                break;
            case BROWN:
                accent = R.style.AccentBrown;
                break;
            case GREY:
                accent = R.style.AccentGrey;
                break;
            case BGREY:
                accent = R.style.AccentBGrey;
                break;
            default:
                accent = R.style.AccentBlue;
                break;
        }
        ctx.getTheme().applyStyle(accent, true);
    }

    private static int getThemeIndex() {
        return Integer.parseInt(App.ctx().getPreferences().getString(Constants.prefs.UI_THEME, String.valueOf(ThemeWrapper.Theme.LIGHT.ordinal())));
    }

    private static int getToolbarThemeIndex() {
        return Integer.parseInt(App.ctx().getPreferences().getString(Constants.prefs.UI_TOOLBAR, String.valueOf(ThemeWrapper.ToolbarTheme.BLUE.ordinal())));
    }

    private static int getAccentIndex() {
        return Integer.parseInt(App.ctx().getPreferences().getString(Constants.prefs.UI_ACCENT, String.valueOf(ThemeWrapper.Accent.BLUE.ordinal())));
    }

    public static boolean isLightTheme(){
        return getThemeIndex() == Theme.LIGHT.ordinal();
    }
}
