package ru.SnowVolf.translate.ui.widget.recyclerview;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Snow Volf on 15.06.2017, 5:08
 */

public class SimpleItem {
    private long id = -1;
    private String title;
    private String owner;
    private int logo;
    View.OnClickListener listener;

    public SimpleItem(@DrawableRes int logo, String title, @Nullable String owner, @Nullable View.OnClickListener listener) {
        this.logo = logo;
        this.title = title;
        this.owner = owner;
        this.listener = listener;
    }

    public SimpleItem(long id, String title, @Nullable String owner) {
        this.id = id;
        this.title = title;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner(){
        return owner;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }

    public int getLogo(){
        return logo;
    }

    public void setLogo(int logo){
        this.logo = logo;
    }

    public View.OnClickListener getListener(){
        return listener;
    }
}
