package com.feicui.zxynews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "newss.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table newss(id integer primary key autoincrement,summary text,icon text,stamp text,title text,link text,nid integer,type integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
