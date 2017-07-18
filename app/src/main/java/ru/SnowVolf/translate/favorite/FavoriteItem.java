package ru.SnowVolf.translate.favorite;

/**
 * Created by Snow Volf on 04.06.2017, 22:44
 */

public class FavoriteItem {
    private long id = -1;
    private int toPosition;
    private int fromPosition;
    private String title;
    private String source;

    public FavoriteItem(long id, int fromPosition, int toPosition, String title, String source) {
        this.id = id;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
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

    public int getToPosition(){
        return toPosition;
    }

    public int getFromPosition(){
        return fromPosition;
    }
}

