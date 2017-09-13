package com.java.group8;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 亦铭 on 9/10/2017.
 */

public class NewsDatabase extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Newss.db";
    public static final String ALL_TABLE_NAME = "AllNews";
    public static final String FAV_TABLE_NAME = "FavNews";
    public static final String HIS_TABLE_NAME = "SearchHistory";
    private static final String ALL_DETAILS = "(ID text primary key, ClassTag text, " +
            "Source text, Title text, Time text, " +
            "URL text, Author text, Type text, " +
            "Pictures text, Video text, Read int, "+
            "Details blob)";


    private static final String HISTORY_DETAILS = "(History text primary key)";

    public NewsDatabase(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("databse", "create");
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + ALL_TABLE_NAME + ALL_DETAILS;
        sqLiteDatabase.execSQL(sql);
        String str = "create table if not exists " + FAV_TABLE_NAME + ALL_DETAILS;
        sqLiteDatabase.execSQL(str);
        String sqr = "create table if not exists " + HIS_TABLE_NAME + HISTORY_DETAILS;
        Log.d("srq", sqr);
        sqLiteDatabase.execSQL(sqr);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("databse", "update");
        String sql = "DROP TABLE IF EXISTS " + ALL_TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        String str = "DROP TABLE IF EXISTS " + FAV_TABLE_NAME;
        sqLiteDatabase.execSQL(str);
        String sqr = "DROP TABLE IF EXISTS " + HIS_TABLE_NAME;
        sqLiteDatabase.execSQL(sqr);
        onCreate(sqLiteDatabase);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        // 每次成功打开数据库后首先被执行
        super.onOpen(db);
    }
    //表名，插入数据
    public void insert(ContentValues values, String table_name){
        SQLiteDatabase db = getWritableDatabase();
        db.insert(table_name, null, values);
        Log.d("insert", "sucess");
    }
    //查询，选择语句，占位符
    public Cursor query(String sql, String[] str){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, str);
        //Log.d("query", "query");
        return c;
    }
    //根据id删除
    public void delete(String id, String table_name){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table_name, "ID=?", new String[]{String.valueOf(id)});
    }
    public  void delete_all(String table_name){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table_name, null, null);
    }
    //表名，要更改的字段和内容，修改条件，修改条件的参数
    public void update(String table_name, ContentValues values, String whereClause, String[] whereArgs){
        SQLiteDatabase db = getWritableDatabase();
        db.update(table_name, values, whereClause, whereArgs);
    }

}
