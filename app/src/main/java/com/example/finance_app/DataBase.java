package com.example.finance_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("myLog", "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table mytable ("
                + "_id integer primary key autoincrement,"
                + "category text,"
                + "type text,"
                + "sum text,"
                + "time text,"
                + "date text,"
                + "comment text"
                + ");");

        db.execSQL("create table valut ("
                + "_id integer primary key autoincrement,"
                + "valut_type text,"
                + "course text,"
                + "state text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}