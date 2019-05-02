package com.example.finance_app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add_page extends AppCompatActivity
        implements  View.OnClickListener{

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear;
    TextView numbers;
    EditText etComment;
    DataBase dbHelper;
    Cursor cursor;
    String Sum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG,"Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        numbers = findViewById(R.id.number_text);
        etComment = (EditText) findViewById(R.id.comment);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DataBase(this);
    }

    public void onClickNumber(View v) { //TODO Написати функцію для зчитування операцій onClickOperator
        Button bn = (Button) v;
        Sum += bn.getText();
        numbers.setText(Sum);
        }

    public void onClickRemove(View v) {
        if (Sum.length() >1 ) {
            Sum = Sum.substring(0, Sum.length() - 1);
            numbers.setText(Sum);
        }
        else if (Sum.length() <=1 ) {
            Sum = "";
            numbers.setText("0");
        }
    }

    @Override
    public void onClick(View v) {
    //    Log.d(LOG_TAG,"Press button");
        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String Category = getIntent().getStringExtra("Category");
        String Comment = etComment.getText().toString(); //TODO EditText при розгортці звигає все вгору, а має видвинутись поверх кнопок
        SimpleDateFormat sdf = new SimpleDateFormat("'Date:' yyyy:MM:dd 'Time:' HH:mm:ss");
        String TimeNow = sdf.format(new Date());

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение
                cv.put("category", Category);
                cv.put("sum", Sum);
                cv.put("time", TimeNow);
                cv.put("comment", Comment);

                // вставляем запись и получаем ее ID
                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
            case R.id.btnRead:
                Log.d(LOG_TAG, "--- Rows in mytable: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = db.query("mytable", null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int categoryColIndex = c.getColumnIndex("category");
                    int sumColIndex = c.getColumnIndex("sum");
                    int commentColIndex = c.getColumnIndex("comment");
                    int timeColIndex = c.getColumnIndex("time");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", Category = " + c.getString(categoryColIndex) +
                                        ", Sum = " + c.getString(sumColIndex)+
                                        ", Comment = " + c.getString(commentColIndex)+
                                        ", " + c.getString(timeColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
           //     finish(); //TODO Залишив поки як є щоб можна було тестити, закриває вікно на цю кнопку
                break;
            case R.id.btnClear: //TODO Кнопка "/". Переназначити кнопку.
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                // удаляем все записи
                int clearCount = db.delete("mytable", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;

        }
        // закрываем подключение к БД
        dbHelper.close();
        }
}
