package com.example.finance_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Operation_page extends AppCompatActivity {

    DataBase dbHelper;
    ListView lvData;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_page);

        dbHelper = new DataBase(this);
        Cursor c = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        c = db.query("mytable", null, null, null, null, null, null);

        startManagingCursor(c);

        String[] from = new String[] { "category","sum","type" };
        int[] to = new int[] { R.id.tvCategory, R.id.tvSum, R.id.tvType };
        //TODO ця штука як я бачу дуже обмежена в можливостях але я в тебе вірю)

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, c, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);
    }
}
