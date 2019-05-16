package com.example.finance_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class Operation_page extends AppCompatActivity {

    DataBase dbHelper;
    ListView lvData;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;

    int IMAGES[] = {R.drawable.coffee};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_page);

        dbHelper = new DataBase(this);
        Cursor c = null;



        SQLiteDatabase db = dbHelper.getWritableDatabase();
        c = db.query("mytable", null, null, null, null, null, null);

        startManagingCursor(c);

        String[] from = new String[] { "category","sum","type","date" };
        int[] to = new int[] {R.id.tvCategory, R.id.tvSum, R.id.tvType, R.id.tvTime };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, c, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

       /* CustomAdapter customAdapter = new CustomAdapter();
        lvData.setAdapter(customAdapter);*/
    }

    class CustomAdapter extends BaseAdapter{//TODO як я поняв SimpleCursorAdapter устарів. В неті всюди юзають Customadapter. Треба розібратись

        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item,null);
            ImageView image = (ImageView)view.findViewById(R.id.tvImage);

            image.setImageResource(IMAGES[0]);
            return view;
        }
    }
}
