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

public class HistoryDbModel extends SQLiteOpenHelper {

    public HistoryDbModel(Context context) {
        super(context, Constants.historyDb.DB_NAME, null, Constants.historyDb.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.historyDb.DB_TABLE_HISTORY + " (" +
                Constants.historyDb.KEY_ID + " INTEGER PRIMARY KEY, " +
                Constants.historyDb.KEY_FROM_INT + " INTEGER, " +
                Constants.historyDb.KEY_TO_INT + " INTEGER, " +
                Constants.historyDb.KEY_TITLE + " TEXT, " +
                Constants.historyDb.KEY_SOURCE + " TEXT, " +
                Constants.historyDb.KEY_TRANSLATE + " TEXT)");
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
        String[] projekt = {Constants.historyDb.KEY_ID};
        String selection = Constants.historyDb.KEY_TRANSLATE + " = ?";
        String[] selectionArgs = {item.getTranslation()};
        String sortOrder = Constants.historyDb.KEY_ID + " DESC";
        Cursor cursor = db.query(Constants.historyDb.DB_TABLE_HISTORY, projekt, selection, selectionArgs, null, null, sortOrder);
        if (Preferences.isDuplicatesNotAllowed()) {
            if (cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(Constants.historyDb.KEY_ID, item.getId());
                String updateSelection = Constants.historyDb.KEY_ID + " = ?";
                String[] updateSelectionArgs = {cursor.getString(0)};
                db.update(Constants.historyDb.DB_TABLE_HISTORY, values, updateSelection, updateSelectionArgs);
            } else {
                ContentValues values = new ContentValues();
                values.put(Constants.historyDb.KEY_ID, item.getId());
                values.put(Constants.historyDb.KEY_FROM_INT, item.getFromPosition());
                values.put(Constants.historyDb.KEY_TO_INT, item.getToPosition());
                values.put(Constants.historyDb.KEY_TITLE, item.getTitle());
                values.put(Constants.historyDb.KEY_SOURCE, item.getSource());
                values.put(Constants.historyDb.KEY_TRANSLATE, item.getTranslation());
                db.insert(Constants.historyDb.DB_TABLE_HISTORY, null, values);
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(Constants.historyDb.KEY_ID, item.getId());
            values.put(Constants.historyDb.KEY_FROM_INT, item.getFromPosition());
            values.put(Constants.historyDb.KEY_TO_INT, item.getToPosition());
            values.put(Constants.historyDb.KEY_TITLE, item.getTitle());
            values.put(Constants.historyDb.KEY_SOURCE, item.getSource());
            values.put(Constants.historyDb.KEY_TRANSLATE, item.getTranslation());
            db.insert(Constants.historyDb.DB_TABLE_HISTORY, null, values);
        }
        cursor.close();
        db.close();
    }

    public void delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.historyDb.DB_TABLE_HISTORY, Constants.historyDb.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<HistoryItem> getAllItems() {
        List<HistoryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {Constants.historyDb.KEY_ID, Constants.historyDb.KEY_FROM_INT, Constants.historyDb.KEY_TO_INT, Constants.historyDb.KEY_TITLE, Constants.historyDb.KEY_SOURCE, Constants.historyDb.KEY_TRANSLATE};
        String sortOrder = Constants.historyDb.KEY_ID + " DESC";
        Cursor cursor = db.query(Constants.historyDb.DB_TABLE_HISTORY, columns, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                list.add(new HistoryItem(Long.parseLong(cursor.getString(0)),
                        cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.historyDb.DB_TABLE_HISTORY, Constants.historyDb.KEY_ID + ">=?", new String[]{"0"});
        db.close();
    }
}
