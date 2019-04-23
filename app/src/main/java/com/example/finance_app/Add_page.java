package com.example.finance_app;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class add_page extends AppCompatActivity
        implements  View.OnClickListener{

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear;
    EditText etName, etEmail;
    DataBase data_base;
    Cursor cursor;

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

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);

        data_base = new DataBase(this);
        data_base.open();
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG,"Press button");
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        switch (v.getId()) {
            case R.id.btnAdd:
                data_base.addRec(name,email);

            case R.id.btnRead: /*
                Log.d(LOG_TAG,"dd");
                cursor = db.getAllData();
                if (cursor.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = cursor.getColumnIndex("id");
                    int nameColIndex = cursor.getColumnIndex("name");
                    int emailColIndex = cursor.getColumnIndex("text");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + cursor.getInt(idColIndex) +
                                        ", name = " + cursor.getString(nameColIndex) +
                                        ", email = " + cursor.getString(emailColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (cursor.moveToNext());
                } else {
                    Log.d(LOG_TAG, "0 rows");
                cursor.close();}*/
                break;
        } }
    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        data_base.close();
    }
}
