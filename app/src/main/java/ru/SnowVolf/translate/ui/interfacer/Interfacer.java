package ru.SnowVolf.translate.ui.interfacer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.fragment.settings.MainSettingsFragment;

/**
 * Created by Snow Volf on 05.06.2017, 9:30
 */

public abstract class Interfacer {
    public enum Theme{
        LIGHT,
        DARK,
        BLACK
    }

    public enum Accent{
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

    public static void applyTheme(Activity ctx){
        int theme;
        switch (Theme.values()[MainSettingsFragment.getThemeIndex(ctx)]){
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
        //ctx.getResources().newTheme().applyStyle();
        ctx.setTheme(theme);
    }

    public static void applyAccent(Activity ctx){
        int accent;
        switch (Accent.values()[MainSettingsFragment.getAccentIndex(ctx)]){
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
        //ctx.getResources().newTheme().applyStyle();
        ctx.getTheme().applyStyle(accent, true);
    }

    public static int getThemedResourceId(Context ctx, int attrId){
        return getThemedResourceId(ctx.getTheme(), attrId);
    }

    public static int getThemedResourceId(Resources.Theme theme, int attrId){
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        int result = a.getResourceId(0, -1);
        a.recycle();
        return result;
    }

    public static int getThemedColor(Context ctx, int attrId){
        return getThemedColor(ctx.getTheme(), attrId);
    }

    public static int getThemedColor(Resources.Theme theme, int attrId){
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        int result = a.getResourceId(0, -1);
        a.recycle();
        return result;
    }
}
