package com.example.finance_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class Operation_page extends AppCompatActivity {

    DataBase dbHelper;
    ListView lvData;

    private static final int CM_DELETE_ID = 1;

    double course;

    final String ATTRIBUTE_NAME_CATEGORY = "category";
    final String ATTRIBUTE_NAME_SUM = "sum";
    final String ATTRIBUTE_NAME_TYPE = "type";
    final String ATTRIBUTE_NAME_DATE = "date";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    final String ATTRIBUTE_NAME_COMMENT = "comment";

    String valute = "₴";
    Cursor c = null;

    public void ListUpdate(){
        DecimalFormat format = new DecimalFormat("#.##");
        dbHelper = new DataBase(this);
        Cursor c = null;
        course = getIntent().getDoubleExtra("Course",1);
        String valute = getIntent().getStringExtra("Course_type");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] columns = new String[] { "category", "sum","type", "date","comment" };
        String orderBy = "_id";
        c = db.query("Finance_app_add_table", columns, null, null, null, null, orderBy + " DESC");
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                c.getCount());
        Map<String, Object> m;
        if (c != null) {
            if (c.moveToFirst()) {
                int img=0;
                do {
                    for (String cn : c.getColumnNames()) {
                        if (cn.equals("category")){
                            m = new HashMap<String, Object>();
                            m.put(ATTRIBUTE_NAME_CATEGORY, c.getString(c.getColumnIndex(cn)));
                            m.put(ATTRIBUTE_NAME_TYPE, c.getString(c.getColumnIndex(cn)+2));
                            if (c.getString(c.getColumnIndex(cn)+2).equals("From Cash")||
                                    c.getString(c.getColumnIndex(cn)+2).equals("From Card"))
                                m.put(ATTRIBUTE_NAME_SUM, "- " + format.format(Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))/course) + valute);
                            else
                                m.put(ATTRIBUTE_NAME_SUM, "+ " + format.format(Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))/course) + valute);

                            m.put(ATTRIBUTE_NAME_DATE, c.getString(c.getColumnIndex(cn)+3));
                            if (c.getString(c.getColumnIndex(cn)+4).equals(""))
                                m.put(ATTRIBUTE_NAME_COMMENT, "No comment");
                            else
                                m.put(ATTRIBUTE_NAME_COMMENT,  c.getString(c.getColumnIndex(cn)+4));
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
            //  c.close();
        }
        startManagingCursor(c);
        String[] from = { ATTRIBUTE_NAME_CATEGORY, ATTRIBUTE_NAME_SUM,ATTRIBUTE_NAME_TYPE,
                ATTRIBUTE_NAME_DATE, ATTRIBUTE_NAME_IMAGE, ATTRIBUTE_NAME_COMMENT };
        int[] to = new int[] {R.id.tvCategory, R.id.tvSum, R.id.tvType, R.id.tvTime, R.id.tvImage, R.id.comment };

        // создааем адаптер и настраиваем список
        MySimpleAdapter sAdapter = new MySimpleAdapter(this, data,
                R.layout.item, from, to);
        // определяем список и присваиваем ему адаптер
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(sAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_page);
        ListUpdate();
        registerForContextMenu(lvData);
    }
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0,"Delete Operation");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] colums = new String[]{"_id"};
            String orderBy = "_id";
            c = db.query("Finance_app_add_table", colums, null, null, null, null, orderBy + " DESC");
            int[] indexs = new int[10000];
            int i = 0;
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        for (String cn : c.getColumnNames()) {
                            indexs[i] = Integer.parseInt(c.getString(c.getColumnIndex(cn)));
                            i++;
                        }
                    } while (c.moveToNext());
                }}
            String id = "_id";
            db.delete("Finance_app_add_table", id + " = " + indexs[acmi.position], null);
            ListUpdate();
            return true;
        }
        return super.onContextItemSelected(item);
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


