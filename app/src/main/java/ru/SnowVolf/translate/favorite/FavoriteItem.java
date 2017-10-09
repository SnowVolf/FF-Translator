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

