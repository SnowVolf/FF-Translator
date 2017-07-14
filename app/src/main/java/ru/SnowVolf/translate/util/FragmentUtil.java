package ru.SnowVolf.translate.util;

import android.app.Activity;
import android.app.Fragment;

import ru.SnowVolf.translate.App;

/**
 * Created by Snow Volf on 13.07.2017, 18:54
 */

public class FragmentUtil {
    public static FragmentUtil baseContext = null;

    public FragmentUtil(){
        baseContext = this;
    }

    public static FragmentUtil ctx(){
        if(baseContext == null){
            new FragmentUtil();
        }
        return baseContext;
    }

    public void interate(Activity activity, int containerId, Fragment fragment){
        activity.getFragmentManager().beginTransaction().replace(containerId, fragment).commit();
    }

    public void interateStack(Activity activity, int containerId, Fragment fragment){
        activity.getFragmentManager().beginTransaction().replace(containerId, fragment).addToBackStack(null).commit();
    }
}
