package com.example.finance_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataBase dbHelper;
    TextView Cafe,Food,Home,Transport,Shopping,Gift,Cash,Card, Total; //TODO Вивести в TetxView "Total" поточний баланс
    public void DataBaseTakeInformation(){
        dbHelper = new DataBase(this);
        Cursor c = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = null;
        String groupBy = null;
        int costs=0;
        int cash=0;
        int card=0;

        columns = new String[] { "category", "sum(sum) as sum" };
        groupBy = "category";
        c = db.query("mytable", columns, null, null, groupBy, null, null);
        Cafe.setText("0$");
        Food.setText("0$");
        Home.setText("0$");
        Transport.setText("0$");
        Shopping.setText("0$");
        Gift.setText("0$");
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        switch (c.getString(c.getColumnIndex(cn)))
                        {
                            case "Cafes & restaurants":
                                Cafe.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                costs+=parseInt(c.getString(c.getColumnIndex(cn)+1));
                                break;
                            case "Food":
                                Food.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                costs+=parseInt(c.getString(c.getColumnIndex(cn)+1));
                                break;
                            case "Home":
                                Home.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                costs+=parseInt(c.getString(c.getColumnIndex(cn)+1));
                                break;
                            case "Transport":
                                Transport.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                costs+=parseInt(c.getString(c.getColumnIndex(cn)+1));
                                break;
                            case "Shopping":
                                costs+=parseInt(c.getString(c.getColumnIndex(cn)+1));
                                Shopping.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                break;
                            case "Gift":
                                Gift.setText(c.getString(c.getColumnIndex(cn)+1)+"$");
                                costs+=parseInt(c.getString(c.getColumnIndex(cn)+1));
                                break;
                            case "Card":
                                card+=parseInt(c.getString(c.getColumnIndex(cn)+1));
                                break;
                            case "Cash":
                                cash+=parseInt(c.getString(c.getColumnIndex(cn)+1));
                                break;

                        }}}
                while (c.moveToNext()) ;
            c.close();
        }
        dbHelper.close();
            int Res=cash-costs;
           Cash.setText(""+Res+"$");
            int total=Res+card;
            Card.setText(""+card+'$');
            Total.setText(""+total+"$");
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

    @Override
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
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent_Operation = new Intent(this, Operation_page.class);
            startActivityForResult(intent_Operation, 1);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

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
        String category_name="None";
        switch (v.getId()) { //TODO Додати Balance в БД, при натисненні "btnCash" і "btnCard" записати в Balance суму.
            case R.id.btnCafe:
                category_name = "Cafes & restaurants";
                break;

            case R.id.btnFood:
                category_name = "Food";
                break;

            case R.id.btnHome:
                category_name = "Home";
                break;
            case R.id.btnTransport:
                category_name = "Transport";
                break;

            case R.id.btnShopping:
                category_name = "Shopping";
                break;

            case R.id.btnGift:
                category_name = "Gift";
                break;

            case R.id.btnCash:
                category_name = "Cash";
                break;

            case R.id.btnCard:
                category_name = "Card";
                break;
        }

        intent_Add.putExtra("Category", category_name);
        startActivityForResult(intent_Add, 1);
    }


}
