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

package ru.SnowVolf.translate.history;

import org.joda.time.DateTime;

/**
 * Created by Snow Volf on 30.05.2017, 11:27
 */

public class HistoryItem {

    private String mTitle;
    private String mSource;
    private String mTranslation;
    private int mToPosition;
    private int mFromPosition;
    private long mId = -1;
    private int mInFavorite;
    private String mFromCode, mToCode;
    private DateTime mDate;

    public HistoryItem(long id){
        mId = id;
        mDate = new DateTime(getId());
    }

    public long getId(){
        return mId;
    }

    public HistoryItem setId(long id){
        mId = id;
        return this;
    }

    public String getSource(){
        return mSource;
    }

    public HistoryItem setSource(String source){
        mSource = source;
        return this;
    }

    public String getTitle(){
        return mTitle;
    }

    public HistoryItem setTitle(String title){
        mTitle = title;
        return this;
    }

    public String getTranslation(){
        return mTranslation;
    }

    public int getToPosition(){
        return mToPosition;
    }

    public HistoryItem setToPosition(int position){
        mToPosition = position;
        return this;
    }

    public int getFromPosition(){
        return mFromPosition;
    }

    public HistoryItem setFromPosition(int position){
        mFromPosition = position;
        return this;
    }

    public int getInFavorites(){
        return mInFavorite;
    }

    public HistoryItem setInFavorites(int favorites){
        mInFavorite = favorites;
        return this;
    }

    boolean isInFavorites(){
        return getInFavorites() == 1;
    }

    public String getFromCode(){
        return mFromCode;
    }

    public String getToCode(){
        return mToCode;
    }

    public HistoryItem setToCode(String toCode){
        mToCode = toCode;
        return this;
    }

    public HistoryItem setFromCode(String fromCode){
        mFromCode = fromCode;
        return this;
    }

    public HistoryItem setTranslation(String translation){
        mTranslation = translation;
        return this;
    }
    DateTime getDate() {
        return new DateTime(mDate.getMillis());
    }
}
