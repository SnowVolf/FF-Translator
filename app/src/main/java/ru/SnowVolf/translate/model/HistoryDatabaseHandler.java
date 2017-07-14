package ru.SnowVolf.translate.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ru.SnowVolf.translate.history.HistoryItem;
import ru.SnowVolf.translate.util.Constants;
import ru.SnowVolf.translate.util.Preferences;

/**
 * Created by Snow Volf on 30.05.2017, 21:33
 */

public class HistoryDatabaseHandler extends SQLiteOpenHelper {

    public HistoryDatabaseHandler(Context context) {
        super(context, Constants.DatabaseHistory.DB_NAME, null, Constants.DatabaseHistory.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.DatabaseHistory.DB_TABLE_HISTORY + " (" +
                Constants.DatabaseHistory.KEY_ID + " INTEGER PRIMARY KEY, " +
                Constants.DatabaseHistory.KEY_TITLE + " TEXT, " +
                Constants.DatabaseHistory.KEY_SOURCE + " TEXT, " +
                Constants.DatabaseHistory.KEY_TRANSLATE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Update this when db table will be changed
    }

    public void add(HistoryItem item) {
        if (item.getId() == -1) {
            item.setId(System.currentTimeMillis());
        }

        SQLiteDatabase db = getWritableDatabase();
        String[] projekt = {Constants.DatabaseHistory.KEY_ID};
        String selection = Constants.DatabaseHistory.KEY_TRANSLATE + " = ?";
        String[] selectionArgs = {item.getTranslation()};
        String sortOrder = Constants.DatabaseHistory.KEY_ID + " DESC";
        Cursor cursor = db.query(Constants.DatabaseHistory.DB_TABLE_HISTORY, projekt, selection, selectionArgs, null, null, sortOrder);
        if (Preferences.isDuplicatesNotAllowed()) {
            if (cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(Constants.DatabaseHistory.KEY_ID, item.getId());
                String updateSelection = Constants.DatabaseHistory.KEY_ID + " = ?";
                String[] updateSelectionArgs = {cursor.getString(0)};
                db.update(Constants.DatabaseHistory.DB_TABLE_HISTORY, values, updateSelection, updateSelectionArgs);
            } else {
                ContentValues values = new ContentValues();
                values.put(Constants.DatabaseHistory.KEY_ID, item.getId());
                values.put(Constants.DatabaseHistory.KEY_TITLE, item.getTitle());
                values.put(Constants.DatabaseHistory.KEY_SOURCE, item.getSource());
                values.put(Constants.DatabaseHistory.KEY_TRANSLATE, item.getTranslation());
                db.insert(Constants.DatabaseHistory.DB_TABLE_HISTORY, null, values);
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(Constants.DatabaseHistory.KEY_ID, item.getId());
            values.put(Constants.DatabaseHistory.KEY_TITLE, item.getTitle());
            values.put(Constants.DatabaseHistory.KEY_SOURCE, item.getSource());
            values.put(Constants.DatabaseHistory.KEY_TRANSLATE, item.getTranslation());
            db.insert(Constants.DatabaseHistory.DB_TABLE_HISTORY, null, values);
        }
        cursor.close();
        db.close();
    }

    public void delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.DatabaseHistory.DB_TABLE_HISTORY, Constants.DatabaseHistory.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<HistoryItem> getAllItems() {
        List<HistoryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.DatabaseHistory.DB_TABLE_HISTORY, null);
        String[] columns = {Constants.DatabaseHistory.KEY_ID, Constants.DatabaseHistory.KEY_TITLE, Constants.DatabaseHistory.KEY_SOURCE, Constants.DatabaseHistory.KEY_TRANSLATE};
        String sortOrder = Constants.DatabaseHistory.KEY_ID + " DESC";
        Cursor cursor = db.query(Constants.DatabaseHistory.DB_TABLE_HISTORY, columns, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                list.add(new HistoryItem(Long.parseLong(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.DatabaseHistory.DB_TABLE_HISTORY, Constants.DatabaseHistory.KEY_ID + ">=?", new String[]{"0"});
        db.close();
    }
}
