package com.example.finance_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Operation_page extends AppCompatActivity {

    DataBase dbHelper;
    ListView lvData;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;

    final int plus = android.R.drawable.ic_lock_power_off;

    //int IMAGES[] = {R.drawable.coffee};

    final String ATTRIBUTE_NAME_CATEGORY = "category";
    final String ATTRIBUTE_NAME_SUM = "sum";
    final String ATTRIBUTE_NAME_TYPE = "type";
    final String ATTRIBUTE_NAME_DATE = "date";
    final String ATTRIBUTE_NAME_IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_page);

        dbHelper = new DataBase(this);
        Cursor c = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] columns = new String[] { "category", "sum", "type", "date" };
        c = db.query("mytable", columns, null, null, null, null, null);
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
        c.getCount());
        Map<String, Object> m;
        if (c != null) {
            if (c.moveToFirst()) {
                int img=0;
                do {
                    for (String cn : c.getColumnNames()) {
                        Log.d("myLog",cn);
                        if (cn.equals("category")){
                        m = new HashMap<String, Object>();
                        m.put(ATTRIBUTE_NAME_CATEGORY, c.getString(c.getColumnIndex(cn)));
                        m.put(ATTRIBUTE_NAME_SUM, c.getString(c.getColumnIndex(cn)+1));
                        m.put(ATTRIBUTE_NAME_TYPE, c.getString(c.getColumnIndex(cn)+2));
                        m.put(ATTRIBUTE_NAME_DATE, c.getString(c.getColumnIndex(cn)+3));
                        switch (c.getString(c.getColumnIndex(cn))){
                            //Тута задаш іконку для категорій
                            case "Cafes & restaurants":
                              m.put(ATTRIBUTE_NAME_IMAGE,img = plus);
                                break;
                        }
                            data.add(m);
                    }
                    }
                } while (c.moveToNext());
            }
            c.close();
        }
        startManagingCursor(c);
        String[] from = { ATTRIBUTE_NAME_CATEGORY, ATTRIBUTE_NAME_SUM,ATTRIBUTE_NAME_TYPE,
                ATTRIBUTE_NAME_DATE, ATTRIBUTE_NAME_IMAGE };
        int[] to = new int[] {R.id.tvCategory, R.id.tvSum, R.id.tvType, R.id.tvTime, R.id.tvImage };

        // создааем адаптер и настраиваем список
        MySimpleAdapter sAdapter = new MySimpleAdapter(this, data,
                R.layout.item, from, to);
        // определяем список и присваиваем ему адаптер
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(sAdapter);
    }

    class MySimpleAdapter extends SimpleAdapter {

        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public void setViewImage(ImageView v, int value) {
            // метод супер-класса
            super.setViewImage(v, value);
            // разрисовываем ImageView
            if (value == plus) v.setBackgroundColor(0); else
            if (value == 0) v.setBackgroundColor(Color.GREEN);
            //TODO готово тільки іконки замутиш сам, записуєш в змінні їх, нащод беграунда я хз, і для кожної категорії сам мути цю фігню
        }
    }

}
