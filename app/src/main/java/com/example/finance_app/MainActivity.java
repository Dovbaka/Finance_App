package com.example.finance_app;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataBase dbHelper;
    TextView Cafe,Food,Home,Transport,Shopping,Gift,Health,Leisure,Family,Cash,Card, Total;

    DecimalFormat format = new DecimalFormat("#.#");

    String currency[] = { "USD", "UAH", "EUR", "RUB" };

    Object current_currency;

    public void DataBaseTakeInformation(){
        dbHelper = new DataBase(this);
        Cursor c = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        double costCard=0;
        double costCash=0;
        double balanceCard=0;
        double balanceCash=0;
        double costs=0;
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
                                costCash+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1));
                                break;
                            case "From Card":
                                costCash+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1));
                                break;
                            case "To Card":
                                balanceCard+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1));
                                break;
                            case "To Cash":
                                balanceCash+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1));
                                break;
                        }}}
                while (c.moveToNext()) ;}}

        columns = new String[] { "category", "sum(sum) as sum" };
        groupBy = "category";
        c = db.query("mytable", columns, null, null, groupBy, null, null);
        Cafe.setText("0$");
        Food.setText("0$");
        Home.setText("0$");
        Transport.setText("0$");
        Shopping.setText("0$");
        Gift.setText("0$");
        Health.setText("0$");
        Leisure.setText("0$");
        Family.setText("0$");
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        switch (c.getString(c.getColumnIndex(cn)))
                        {
                            case "Cafes & restaurants":
                                Cafe.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                            case "Food":
                                Food.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                            case "Home":
                                Home.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                            case "Transport":
                                Transport.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                            case "Shopping":
                                Shopping.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                            case "Gift":
                                Gift.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                            case "Health":
                                Health.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                            case "Leisure":
                                Leisure.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                            case "Family":
                                Family.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                        }}}
                while (c.moveToNext()) ;
            c.close();
        }
        dbHelper.close();
            double resCard=balanceCard-costCard;
            double resCash=balanceCash-costCash;
            double resTotal=resCard+resCash;
            Card.setText(format.format(resCard)+"$");
            Cash.setText(format.format(resCash)+"$");
            Total.setText(format.format(resTotal)+"$");
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
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
        switch (v.getId()) {
            case R.id.btnCafe:
                category_name = "Cafes & restaurants";
                intent_Add.putExtra("Category", category_name);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnFood:
                category_name = "Food";
                intent_Add.putExtra("Category", category_name);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnHome:
                category_name = "Home";
                intent_Add.putExtra("Category", category_name);
                startActivityForResult(intent_Add, 1);
                break;
            case R.id.btnTransport:
                category_name = "Transport";
                intent_Add.putExtra("Category", category_name);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnShopping:
                category_name = "Shopping";
                intent_Add.putExtra("Category", category_name);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnGift:
                category_name = "Gift";
                intent_Add.putExtra("Category", category_name);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnHealth:
                category_name = "Health";
                intent_Add.putExtra("Category", category_name);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnLeisure:
                category_name = "Leisure";
                intent_Add.putExtra("Category", category_name);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnFamily:
                category_name = "Family";
                intent_Add.putExtra("Category", category_name);
                startActivityForResult(intent_Add, 1);
                break;

            case R.id.btnCash:
                category_name = "Cash";
                intent_Add_earn.putExtra("Category", category_name);
                startActivityForResult(intent_Add_earn, 1);
                break;

            case R.id.btnCard:
                category_name = "Card";
                intent_Add_earn.putExtra("Category", category_name);
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

    // обработчик нажатия на пункт списка диалога или кнопку
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            ListView lv = ((AlertDialog) dialog).getListView();
            current_currency = lv.getAdapter().getItem(lv.getCheckedItemPosition());//це запамятвовування в змінну
        }
    };
}
