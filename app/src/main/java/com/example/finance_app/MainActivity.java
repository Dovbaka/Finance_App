package com.example.finance_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataBase dbHelper;
    TextView Cafe,Food,Home,Transport,Shopping,Gift;

    public void DataBaseTakeInformation(){
        dbHelper = new DataBase(this);
        Cursor c = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = null;
        String groupBy = null;

        columns = new String[] { "category", "sum(sum) as sum" };
        groupBy = "category";
        c = db.query("mytable", columns, null, null, groupBy, null, null);
        Cafe.setText("0$");   //TODO Костиль в тому що при видаленні БД, значення обнуляються лише при перезаході
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
                        }}}
                while (c.moveToNext()) ;
            }
            c.close();
        }
        dbHelper.close();
    }


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
            // Handle the camera action
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
        switch (v.getId()) {

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
        }

        intent_Add.putExtra("Category", category_name);
        startActivityForResult(intent_Add, 1);
    }


}
