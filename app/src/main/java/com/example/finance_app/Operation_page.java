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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

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
    final String ATTRIBUTE_NAME_COMMENT = "comment";

    String valute = "$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_page);

        dbHelper = new DataBase(this);
        Cursor c = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] columns = new String[] { "category", "sum","type", "date","comment" };
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
                        m.put(ATTRIBUTE_NAME_TYPE, c.getString(c.getColumnIndex(cn)+2));
                        if (c.getString(c.getColumnIndex(cn)+2).equals("From Cash")||
                                c.getString(c.getColumnIndex(cn)+2).equals("From Card"))
                        m.put(ATTRIBUTE_NAME_SUM, "- " + c.getString(c.getColumnIndex(cn)+1) + valute);
                        else
                        m.put(ATTRIBUTE_NAME_SUM, "+ " + c.getString(c.getColumnIndex(cn)+1) + valute);

                        m.put(ATTRIBUTE_NAME_DATE, c.getString(c.getColumnIndex(cn)+3));
                        m.put(ATTRIBUTE_NAME_COMMENT, c.getString(c.getColumnIndex(cn)+4));
                        switch (c.getString(c.getColumnIndex(cn))){
                            case "Salary":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.salary);
                                break;

                            case "Card":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.card);
                                break;

                            case "Cash":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.cash);
                                break;

                            case "Cafes & restaurants":
                              m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.coffee);
                                break;

                            case "Transport":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.car);
                                break;

                            case "Home":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.home);
                                break;

                            case "Food":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.food);
                                break;

                            case "Gift":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.gift);
                                break;

                            case "Shopping":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.shop);
                                break;

                            case "Leisure":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.leisure);
                                break;

                            case "Health":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.health);
                                break;

                            case "Family":
                                m.put(ATTRIBUTE_NAME_IMAGE,img = R.drawable.family);
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
        //TODO ATTRIBUTE_NAME_COMMENT записати верх i відповідно нижче додати для нього мсіце
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
        public void setViewText(TextView v, String text) {
            // метод супер-класса, который вставляет текст
            super.setViewText(v, text);
            if (v.getId() == R.id.tvSum) {
                String i = String.valueOf(text.charAt(0));
                if (i.equals("-")) v.setTextColor(Color.RED);
                else v.setTextColor(getResources().getColor(R.color.green_color));
            }
        }
    }
}


