package com.example.finance_app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Plan_page extends AppCompatActivity
        implements  View.OnClickListener{

    final String LOG_TAG = "myLogs";
    View database_lay,equal_lay;
    Button btnAdd, btnTime;
    TextView numbers;
    String Sum = "", sign = "";
    double tempDouble, tempDouble2, course;
    int max_row = 5;
    boolean operator_pressed = false;

    String[] data = {"Cafes & restaurants", "Food", "Home", "Transport", "Shopping", "Gift",
            "Health", "Leisure", "Family",};
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
        setContentView(R.layout.activity_plan_page);

        String cat_name = getIntent().getStringExtra("Category");
        course = getIntent().getDoubleExtra("Course",1);
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_text, data2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        database_lay = (View) findViewById(R.id.For_database);
        equal_lay = (View) findViewById(R.id.For_equal);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnTime = (Button) findViewById(R.id.btn_Time);

        numbers = findViewById(R.id.number_text);

        String TimeNow = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        btnTime.setText(TimeNow);
    }

    public void onClickNumber(View v) {
        operator_pressed = false;
        if(max_row > 0) {
            Button bn = (Button) v;
            Sum += bn.getText();
            max_row--;
            numbers.setText(Sum);
        }
    }

    public void onClickOperator(View v) {
        if (!(TextUtils.isEmpty(numbers.getText().toString())) && operator_pressed == false) {
            max_row = 5;
            operator_pressed = true;
            Button bn = (Button) v;
            Sum = "";
            tempDouble = Double.parseDouble(numbers.getText().toString());
            numbers.setText(bn.getText().toString());
            sign = bn.getText().toString();
            database_lay.setVisibility(View.GONE);
            equal_lay.setVisibility(View.VISIBLE);
        }
    }


    public void onClickEqual(View v){
        operator_pressed = false;
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
                    Toast.makeText(this, "You can't divide by 0", Toast.LENGTH_SHORT).show();
                }
                else {
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
        operator_pressed = false;
        if(!Sum.contains(".")){
            max_row = 2;
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
        operator_pressed = false;
        if (Sum.length() >1 ) {
            max_row++;
            Sum = Sum.substring(0, Sum.length() - 1);
            numbers.setText(Sum);
        }
        else if (Sum.length() <=1 ) {
            max_row = 5;
            Sum = "";
            numbers.setText("0");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAdd:
                if ((!(TextUtils.isEmpty(numbers.getText().toString()))) && (!Sum.equals("0"))&&!(numbers.getText().toString()).equals("0")) {
                    Sum = String.valueOf(Double.parseDouble(Sum) * course);
                    Intent intent = new Intent();
                    intent.putExtra("hola", Double.parseDouble(Sum));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

            case R.id.btnClose:
                finish();
                break;
        }
    }
}
