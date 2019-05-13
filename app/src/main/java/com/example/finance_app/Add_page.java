package com.example.finance_app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Add_page extends AppCompatActivity
        implements  View.OnClickListener{//TODO SUM не має бути відємним

    final String LOG_TAG = "myLogs";
    View database_lay,equal_lay;
    Button btnAdd, btnRead, btnClear, btnTime;
    TextView numbers;
    EditText etComment;
    DataBase dbHelper;
    Spinner spinner, spinner2;
    Cursor cursor;
    String Sum = "", sign = "";
    double tempDouble, tempDouble2;

    String[] data = {"Cafes & restaurants", "Food", "Home", "Transport", "Shopping", "Gift","Cash", "Card"};
   String[] data2 = {"Cash", "Card"};

    public boolean isInt(double a){
        if (a % 1 == 0)
            return true;
        else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG,"Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);

        String cat_name = getIntent().getStringExtra("Category");

        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter2);
        // выделяем элемент
        for (int i = 0; i < data.length; i++) {
            if(data[i].equals(cat_name)){ spinner.setSelection(i);break;}
            else {spinner.setSelection(0);}
        }
        spinner2.setSelection(0);

        database_lay = (View) findViewById(R.id.For_database);
        equal_lay = (View) findViewById(R.id.For_equal);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnClose);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnTime = (Button) findViewById(R.id.btn_Time);

        numbers = findViewById(R.id.number_text);
        etComment = (EditText) findViewById(R.id.comment);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DataBase(this);

        String TimeNow = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        btnTime.setText(TimeNow);
    }

    public void onClickNumber(View v) {
        Button bn = (Button) v;
        Sum += bn.getText();
        numbers.setText(Sum);
        }

    public void onClickOperator(View v) {
        Button bn = (Button) v;
        Sum = "";
        tempDouble = Double.parseDouble(numbers.getText().toString());
        numbers.setText(bn.getText().toString());
        sign = bn.getText().toString();
        database_lay.setVisibility( View.GONE );
        equal_lay.setVisibility( View.VISIBLE );
    }

    public void onClickEqual(View v){ //TODO Додати валідацію. Обмежити цифри після коми до 2
        tempDouble2 = Double.parseDouble(numbers.getText().toString());
        double tempsum = 0;

        switch (sign) {

            case "+":
                tempsum = tempDouble + tempDouble2;
                if(isInt(tempsum))
                numbers.setText(Integer.toString((int)tempsum));
                else numbers.setText(Double.toString(tempsum));
                break;

            case "-":
                tempsum = tempDouble - tempDouble2;
                if(isInt(tempsum))
                    numbers.setText(Integer.toString((int)tempsum));
                else numbers.setText(Double.toString(tempsum));
                break;

            case "*":
                tempsum = tempDouble * tempDouble2;
                if(isInt(tempsum))
                    numbers.setText(Integer.toString((int)tempsum));
                else numbers.setText(Double.toString(tempsum));
                break;

            case "/":
                tempsum = tempDouble / tempDouble2;
                if (tempDouble2 == 0) {
                    if(isInt(tempsum))
                        numbers.setText(Integer.toString((int)tempsum));
                    else numbers.setText(Double.toString(tempsum));
                } else {
                    if(isInt(tempsum))
                        numbers.setText(Integer.toString((int)tempsum));
                    else numbers.setText(Double.toString(tempsum));
                }
                break;
        }

        database_lay.setVisibility( View.VISIBLE );
        equal_lay.setVisibility( View.GONE );

        Sum = numbers.getText().toString();
    }

    public void onClickDot(View v) {
        if(!Sum.contains(".")){
            if (Sum.length() >= 1) {
                Sum += ".";
                numbers.setText(Sum);
            } else if (Sum.equals("")) {
                Sum = "0.";
                numbers.setText(Sum);
            }
        }
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
        String Category = spinner.getSelectedItem().toString(); // зчитування з спінера
        String Comment = etComment.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("'Date:' yyyy:MM:dd 'Time:' HH:mm:ss");
        String TimeNow = sdf.format(new Date());

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение
                cv.put("category", Category);
                cv.put("type","From Card");
                cv.put("sum", Sum);
                cv.put("time", TimeNow);
                cv.put("comment", Comment);

                // вставляем запись и получаем ее ID
                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
            case R.id.btnClose:
                Log.d(LOG_TAG, "--- Rows in mytable: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = db.query("mytable", null, null, null, null, null, null);
                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {
                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("_id");
                    int categoryColIndex = c.getColumnIndex("category");
                    int typeColIndex = c.getColumnIndex("type");
                    int sumColIndex = c.getColumnIndex("sum");
                    int commentColIndex = c.getColumnIndex("comment");
                    int timeColIndex = c.getColumnIndex("time");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", Category = " + c.getString(categoryColIndex) +
                                        ", Type = " + c.getString(typeColIndex)+
                                        ", Sum = " + c.getString(sumColIndex)+
                                        ", Comment = " + c.getString(commentColIndex)+
                                        ", " + c.getString(timeColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
           //     finish(); // Залишив поки як є щоб можна було тестити, закриває вікно на цю кнопку
                break;

            case R.id.btnClear: //TODO Кнопка "/". Переназначити кнопку Clear (під кінець роботи з БД)
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
