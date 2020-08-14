package com.example.movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.movie.contact.ItemCategory;
import com.example.movie.contact.Video;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLHelper";
    static final String DB_NAME_TABLE = "SQLVideo";
    static final String DB_NAME = "SQLVideo.db";
    static final int VERSION = 1;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;

    public SQLHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "CREATE TABLE SQLItemClick ( " +
                "image TEXT," +
                "title TEXT," +
                "time TEXT," +
                "url TEXT )";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("drop table if exists " + DB_NAME_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    public void insertItem(ItemCategory item) {
        sqLiteDatabase = this.getWritableDatabase();
        contentValues= new ContentValues();
        contentValues.put("image", item.getAvatar());
        contentValues.put("title", item.getName());
        contentValues.put("time", item.getDate());
        contentValues.put("url", item.getUrl());
        sqLiteDatabase.insert(DB_NAME_TABLE, null, contentValues);
        closeDB();
    }

    public ArrayList<ItemCategory> getAll() {
        ArrayList<ItemCategory> videoHistory = new ArrayList<>();
        ItemCategory item;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        cursor = sqLiteDatabase.query(false, DB_NAME_TABLE, null, null, null
                , null, null, null, null);
        while (cursor.moveToNext()) {
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            item=new ItemCategory(image,title,time,url);
            videoHistory.add(item);
        }
        closeDB();
        return videoHistory;
    }

    public int deleteItem(String title) {
        sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(DB_NAME_TABLE, "title=?", new String[]{title});
    }

    public boolean deleteAll() {
        int check;
        sqLiteDatabase = getWritableDatabase();
        check = sqLiteDatabase.delete(DB_NAME_TABLE, null, null);
        if (check == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void closeDB() {
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();
    }
}
