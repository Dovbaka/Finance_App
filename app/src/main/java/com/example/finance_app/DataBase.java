package com.example.finance_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context) {
        // конструктор суперкласса
        super(context, "Finance_App_DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("myLog", "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table Finance_app_add_table ("
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

    public boolean checkForTables(String table_name){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table_name, null);

        if(cursor != null){

            cursor.moveToFirst();

            int count = cursor.getInt(0);

            if(count > 0){
                return true;
            }

            cursor.close();
        }

        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}