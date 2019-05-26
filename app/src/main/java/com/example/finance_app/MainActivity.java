package com.example.finance_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataBase dbHelper;
    TextView Cafe,Food,Home,Transport,Shopping,Gift,Health,Leisure,Family,Cash,Card, Total,Cost;

    DecimalFormat format = new DecimalFormat("#.#");

    String currency[] = { "$", "₴", "€", " RUB" };

    Object current_currency = "$";
    public void ValutUpdater(String curent_value){
        DataBase dbHelper;
        ContentValues cv = new ContentValues();
        String valut_type [] = { "$", "₴", "€", " RUB"};
        double course[] = { 1, 26.37, 0.89, 64.45};
        dbHelper = new DataBase(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = null;
        String k = curent_value;
        c = db.query("valut", null, null, null, null, null, null);
        if (c.getCount() == 0) {
            for (int i = 0; i < 4; i++) {
                cv.put("valut_type", valut_type[i]);
                cv.put("course", course[i]);
                Log.d("myLog", "id = " + db.insert("valut", null, cv));
            }
        }
        c = db.query("valut", null, null, null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        switch (cn){
                            case ("valut_type"):
                                if(!k.equals(c.getString(c.getColumnIndex(cn)))){
                                    cv.put("valut_type", c.getString(c.getColumnIndex(cn)));
                                    cv.put("course", c.getString(c.getColumnIndex(cn)+1));
                                    cv.put("state", "Not Active");
                                    db.update("valut", cv, "_id = ?",
                                            new String[] { c.getString(c.getColumnIndex(cn)-1) });;
                                }
                                else{
                                    cv.put("valut_type", c.getString(c.getColumnIndex(cn)));
                                    cv.put("course", c.getString(c.getColumnIndex(cn)+1));
                                    cv.put("state", "Active");
                                    db.update("valut", cv, "_id = ?",
                                            new String[] { c.getString(c.getColumnIndex(cn)-1) });}
                                break;
                        }}}
                while (c.moveToNext()) ;}
            c.close();}
        dbHelper.close();
    }
    public double ValutActivCourse(){
        dbHelper = new DataBase(this);
        Cursor c = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] columns = new String[] { "state","course" };
        double course=0;
        c=db.query("valut", columns, null, null, null, null,
                null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames())
                    {

                        if(c.getString(c.getColumnIndex(cn)).equals("Active")){
                            course+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1));
                            Log.d("myLog", String.valueOf(course));
                        }}}
                while (c.moveToNext()) ;}}
        return course;
    }
    public String ValutActivType(){
        dbHelper = new DataBase(this);
        Cursor c = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] columns = new String[] { "valut_type","state"};
        String courent="";
        c=db.query("valut", columns, null, null, null, null,
                null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames())
                    {

                        if(c.getString(c.getColumnIndex(cn)).equals("Active")){
                            courent+=c.getString(c.getColumnIndex(cn)-1);
                            Log.d("myLog", courent);
                        }}}
                while (c.moveToNext()) ;}}
        return courent;
    }

    public void DataBaseTakeInformation(){
        dbHelper = new DataBase(this);
        Cursor c = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        double costCard=0;
        double costCash=0;
        double balanceCard=0;
        double balanceCash=0;
        double costs=0;
        double course= ValutActivCourse();
        String [] columns = new String[] { "type", "sum(sum) as sum" };
        String groupBy = "type";
        c = db.query("mytable", columns, null, null, groupBy, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        switch (c.getString(c.getColumnIndex(cn)))
                        {
                            case "From Cash":
                                costCash+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))*course;
                                break;
                            case "From Card":
                                costCard+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))*course;
                                break;
                            case "To Card":
                                balanceCard+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))*course;
                                break;
                            case "To Cash":
                                balanceCash+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))*course;
                                break;
                        }}}
                while (c.moveToNext()) ;}}

        columns = new String[] { "category", "sum(sum) as sum" };
        groupBy = "category";
        c = db.query("mytable", columns, null, null, groupBy, null, null);
       String courents=ValutActivType();
        Cafe.setText("0" + courents);
        Food.setText("0" + courents);
        Home.setText("0" + courents);
        Transport.setText("0" + courents);
        Shopping.setText("0" + courents);
        Gift.setText("0" + courents);
        Health.setText("0" + courents);
        Leisure.setText("0" + courents);
        Family.setText("0" + courents);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        switch (c.getString(c.getColumnIndex(cn)))
                        {
                            case "Cafes & restaurants":
                                Cafe.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) * course) + current_currency);
                                break;
                            case "Food":
                                Food.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) * course) + courents);
                                break;
                            case "Home":
                                Home.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) * course) + courents);
                                break;
                            case "Transport":
                                Transport.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) * course) + courents);
                                break;
                            case "Shopping":
                                Shopping.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) * course) + courents);
                                break;
                            case "Gift":
                                Gift.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) * course) + courents);
                                break;
                            case "Health":
                                Health.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) * course) + courents);
                                break;
                            case "Leisure":
                                Leisure.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) * course) + courents);
                                break;
                            case "Family":
                                Family.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) * course) + courents);
                                break;
                        }}}
                while (c.moveToNext()) ;
            c.close();
        }
        dbHelper.close();
            double resCard=balanceCard-costCard;
            double resCash=balanceCash-costCash;
            double resTotal=resCard+resCash;
            double resCost=costCard+costCash;
            Cost.setText(format.format(resCost)+ courents);
            Card.setText(format.format(resCard)+ courents);
            Cash.setText(format.format(resCash)+ courents);
            Total.setText(format.format(resTotal)+ courents);
    }}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Cafe = findViewById(R.id.textCafe);
        Food = findViewById(R.id.textFood);
        Home = findViewById(R.id.textHome);
        Transport = findViewById(R.id.textTransport);
        Shopping = findViewById(R.id.textShopping);
        Gift = findViewById(R.id.textGift);
        Health = findViewById(R.id.textHealth);
        Leisure = findViewById(R.id.textLeisure);
        Family = findViewById(R.id.textFamily);
        Card = findViewById(R.id.textCard);
        Cash = findViewById(R.id.textCash);
        Total = findViewById(R.id.text_Balance_num);
        Cost = findViewById(R.id.text_Cost_num);


        DataBaseTakeInformation();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DataBaseTakeInformation();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            Intent intent_Operation = new Intent(this, Operation_page.class);
            startActivityForResult(intent_Operation, 1);
        } else if (id == R.id.nav_statistic) {
            Intent intent_Chart = new Intent(this, Chart_page.class);
            startActivityForResult(intent_Chart, 1);
        } else if (id == R.id.nav_currency) {
            showDialog(0);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View v){
        Intent intent_Add = new Intent(this, Add_page.class);
        Intent intent_Add_earn = new Intent(this, Add_earn_page.class);
        String category_name="None";
        double course=ValutActivCourse();
        switch (v.getId()) {
            case R.id.btnCafe:
                category_name = "Cafes & restaurants";
                intent_Add.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnFood:
                category_name = "Food";
                intent_Add.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnHome:
                category_name = "Home";
                intent_Add.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add, 1);
                break;
            case R.id.btnTransport:
                category_name = "Transport";
                intent_Add.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnShopping:
                category_name = "Shopping";
                intent_Add.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnGift:
                category_name = "Gift";
                intent_Add.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnHealth:
                category_name = "Health";
                intent_Add.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnLeisure:
                category_name = "Leisure";
                intent_Add.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnFamily:
                category_name = "Family";
                intent_Add.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnCash:
                category_name = "Cash";
                intent_Add_earn.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add_earn, 1);
                break;

            case R.id.btnCard:
                category_name = "Card";
                intent_Add_earn.putExtra("Category", category_name);
                intent_Add.putExtra("Course", course);
                startActivityForResult(intent_Add_earn, 1);
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Select currency");
        adb.setSingleChoiceItems(currency, -1, myClickListener);
        adb.setPositiveButton("OK", myClickListener);
        return adb.create();
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            ListView lv = ((AlertDialog) dialog).getListView();
            current_currency = lv.getAdapter().getItem(lv.getCheckedItemPosition());//це запамятвовування в змінну
            Log.d("myLog", (String) current_currency);
            ValutUpdater((String) current_currency);
            onResume();
        }
    };
}
