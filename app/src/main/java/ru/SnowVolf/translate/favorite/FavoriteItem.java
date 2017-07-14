package ru.SnowVolf.translate.favorite;

/**
 * Created by Snow Volf on 04.06.2017, 22:44
 */

public class FavoriteItem {
    private long id = -1;
    private String title;
    private String source;

    public FavoriteItem(String title, String source) {
        this.title = title;
        this.source = source;
    }

    public FavoriteItem(long id, String title, String source) {
        this.id = id;
        this.title = title;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

