package ru.SnowVolf.translate.ui.widget.recyclerview;

import android.support.annotation.Nullable;

/**
 * Created by Snow Volf on 15.06.2017, 5:08
 */

public class SimpleItem {
    private long id = -1;
    private String title;
    private String owner;

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
}
