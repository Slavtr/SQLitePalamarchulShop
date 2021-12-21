package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactDb";
    public static final String TABLE_LIBRARY = "Mogozin";
    public static final String TABLE_USERS = "Usors";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";

    public static final String U_ID = "_id";
    public static final String U_LOGIN = "login";
    public static final String U_PASSWORD = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_LIBRARY + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_PRICE + " text" + ")");
        db.execSQL("create table " + TABLE_USERS + "(" + U_ID + " integer primary key," + U_LOGIN + " text," + U_PASSWORD + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_LIBRARY);
        db.execSQL("drop table if exists " + TABLE_USERS);

        onCreate(db);

    }
}