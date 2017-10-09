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

package ru.SnowVolf.translate.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.SnowVolf.translate.favorite.FavoriteItem;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.preferences.Preferences;

/**
 * Created by Snow Volf on 04.06.2017, 22:52
 */

public class FavoriteDbModel extends SQLiteOpenHelper {

    public FavoriteDbModel(Context context) {
        super(context, Constants.favDb.DB_NAME, null, Constants.favDb.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.favDb.DB_TABLE_FAVORITES + " (" +
                Constants.favDb.KEY_ID + " INTEGER PRIMARY KEY, " +
                Constants.favDb.KEY_FROM_INT + " INTEGER, " +
                Constants.favDb.KEY_TO_INT + " INTEGER, " +
                Constants.favDb.KEY_TITLE + " TEXT, " +
                Constants.favDb.KEY_SOURCE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Update this when db table will be changed
    }

    public void addItem(FavoriteItem item) {
        if (item.getId() == -1) {
            item.setId(System.currentTimeMillis());
        }

        ContentValues values = new ContentValues();
        values.put(Constants.favDb.KEY_ID, item.getId());
        values.put(Constants.favDb.KEY_FROM_INT, item.getFromPosition());
        values.put(Constants.favDb.KEY_TO_INT, item.getToPosition());
        values.put(Constants.favDb.KEY_TITLE, item.getTitle());
        values.put(Constants.favDb.KEY_SOURCE, item.getSource());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(Constants.favDb.DB_TABLE_FAVORITES, null, values);
        db.close();
    }

    public void updateItem(FavoriteItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.favDb.KEY_TITLE, item.getTitle());
        values.put(Constants.favDb.KEY_SOURCE, item.getSource());

        db.update(Constants.favDb.DB_TABLE_FAVORITES, values, Constants.favDb.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    public void deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.favDb.DB_TABLE_FAVORITES, Constants.favDb.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<FavoriteItem> getAllItems() {
        List<FavoriteItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.favDb.DB_TABLE_FAVORITES, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new FavoriteItem(Long.parseLong(cursor.getString(0)),
                        cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if (Preferences.isFavoriteReverse()){
            Collections.reverse(list);
        }
        return list;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.favDb.DB_TABLE_FAVORITES, Constants.favDb.KEY_ID + ">=?", new String[]{"0"});
        db.close();
    }
}

