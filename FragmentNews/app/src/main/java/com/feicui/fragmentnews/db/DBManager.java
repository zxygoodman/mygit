package com.feicui.fragmentnews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.feicui.fragmentnews.bean.JsonData;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public class DBManager {
    private static DBManager dbManager;
    private DBHelper dbHelper;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public static DBManager getInstance(Context context) {

        if (dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    public void insertData(JsonData news) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("summary", news.getSummary());
        cv.put("icon", news.getIcon());
        cv.put("stamp", news.getStamp());
        cv.put("title", news.getTitle());
        cv.put("link", news.getLink());
        cv.put("nid", news.getNid());
        cv.put("type", news.getType());
        database.insert("newss", null, cv);
        database.close();
    }

    public LinkedList<JsonData> selectData() {

        LinkedList<JsonData> list = new LinkedList<JsonData>();
        SQLiteDatabase data = dbHelper.getReadableDatabase();
        String sql = "select * from newss";
        Cursor cursor = data.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String stamp = cursor.getString(cursor.getColumnIndex("stamp"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String link = cursor.getString(cursor.getColumnIndex("link"));
                int nid = cursor.getInt(cursor.getColumnIndex("nid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                JsonData jsonData = new JsonData(summary, icon, stamp, title, link, nid, type);
                list.add(jsonData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        data.close();
        return list;
    }

    public Boolean haveDataOrNot() {

        SQLiteDatabase data = dbHelper.getReadableDatabase();
        String sql = "select * from newss";
        Cursor cursor = data.rawQuery(sql, null);
        long num = cursor.getCount();
        cursor.close();
        data.close();
        if (num > 0) {
            return true;
        }
        return false;
    }
}
