package ru.SnowVolf.translate.history;

/**
 * Created by Snow Volf on 30.05.2017, 11:27
 */

public class HistoryItem {

    private String title;
    private String source;
    private String translation;
    private int toPosition;
    private int fromPosition;
    private long id = -1;

    public HistoryItem(long id, int fromPosition, int toPosition, String title, String source, String translation){
        this.id = id;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
        this.title = title;
        this.source = source;
        this.translation = translation;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getSource(){
        return source;
    }

    public void setSource(String source){
        this.source = source;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTranslation(){
        return translation;
    }

    public int getToPosition(){
        return toPosition;
    }

    public int getFromPosition(){
        return fromPosition;
    }
}
