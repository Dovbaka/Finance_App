package com.example.finance_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DecimalFormat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataBase dbHelper;
    TextView Cafe,Food,Home,Transport,Shopping,Gift,Health,Leisure,Family,
             Cash,Card,Total,Cost,Plan,User_label,User2_label,User3_label,User,User2,User3;
    ImageButton User_btn,User2_btn,User3_btn;
    SharedPreferences sPref, user_cat_pref;
    double plan_sum, resCost = 0;
    boolean user_cat_exists = false, user2_cat_exists = false, user3_cat_exists = false;
    String lable, lable2, lable3;

    final Context context = this;

    DecimalFormat format = new DecimalFormat("#.##");

    double course_masiv[] = new double[10];

    public Elements title;

    String currency[] = { "₴ UAH", "$ USD", "€ EUR", "P RUB" };

    Object current_currency = "₴";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        new NewThread().execute();
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
        Plan = findViewById(R.id.text_Plan_num);

        User = findViewById(R.id.textUser);
        User2 = findViewById(R.id.textUser2);
        User3 = findViewById(R.id.textUser3);
        User_label = findViewById(R.id.text_name_User);
        User2_label = findViewById(R.id.text_name_User2);
        User3_label = findViewById(R.id.text_name_User3);
        User_btn = findViewById(R.id.btnUser);
        User2_btn = findViewById(R.id.btnUser2);
        User3_btn = findViewById(R.id.btnUser3);

        DataBaseTakeInformation();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(!dbHelper.checkForTables("Finance_app_add_table")) showDialog(0);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadUserCat();

        loadPlan();
        CheckPlan();

    }

    public void ValutUpdater(String curent_value){
        DataBase dbHelper;
        ContentValues cv = new ContentValues();
        String valut_type [] = { "₴", "$", "€", "P"};
        dbHelper = new DataBase(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = null;
        String k = curent_value;
        c = db.query("valut", null, null, null, null, null, null);
        if (c.getCount() == 0) {
            for (int i = 0; i < 4; i++) {
                cv.put("valut_type", valut_type[i]);
                cv.put("course", course_masiv[i]);
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

        String user1 = User_label.getText().toString(),
               user2 = User2_label.getText().toString(),
               user3 = User2_label.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        double costCard = 0;
        double costCash = 0;
        double balanceCard = 0;
        double balanceCash = 0;
        double costs = 0;
        double course = ValutActivCourse();
        String [] columns = new String[] { "type", "sum(sum) as sum" };
        String groupBy = "type";
        c = db.query("Finance_app_add_table", columns, null, null, groupBy, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    for (String cn : c.getColumnNames()) {
                        switch (c.getString(c.getColumnIndex(cn)))
                        {
                            case "From Cash":
                                costCash+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))/course;
                                break;
                            case "From Card":
                                costCard+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))/course;
                                break;
                            case "To Card":
                                balanceCard+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))/course;
                                break;
                            case "To Cash":
                                balanceCash+=Double.parseDouble(c.getString(c.getColumnIndex(cn)+1))/course;
                                break;
                        }}}
                while (c.moveToNext()) ;}}

        columns = new String[] { "category", "sum(sum) as sum" };
        groupBy = "category";
        c = db.query("Finance_app_add_table", columns, null, null, groupBy, null, null);
        String courents = ValutActivType();
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
                                        (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                                break;
                            case "Food":
                                Food.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                                break;
                            case "Home":
                                Home.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                                break;
                            case "Transport":
                                Transport.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                                break;
                            case "Shopping":
                                Shopping.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                                break;
                            case "Gift":
                                Gift.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                                break;
                            case "Health":
                                Health.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                                break;
                            case "Leisure":
                                Leisure.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                                break;
                            case "Family":
                                Family.setText(format.format(Double.parseDouble
                                        (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                                break;
                        }
                        if (c.getString(c.getColumnIndex(cn)).equals(User_label.getText().toString())){
                            User.setText(format.format(Double.parseDouble
                                    (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                            break;
                        }
                        if (c.getString(c.getColumnIndex(cn)).equals(User2_label.getText().toString())){
                            User2.setText(format.format(Double.parseDouble
                                    (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                            break;
                        }
                        if (c.getString(c.getColumnIndex(cn)).equals(User3_label.getText().toString())){
                            User3.setText(format.format(Double.parseDouble
                                    (c.getString(c.getColumnIndex(cn)+1)) / course) + courents);
                            break;
                        }

                    }
                }
                while (c.moveToNext()) ;
                c.close();
            }
            dbHelper.close();
            double resCard=balanceCard-costCard;
            double resCash=balanceCash-costCash;
            double resTotal=resCard+resCash;
            resCost=costCard+costCash;
            Cost.setText(format.format(resCost)+ courents);
            Card.setText(format.format(resCard)+ courents);
            Cash.setText(format.format(resCash)+ courents);
            Total.setText(format.format(resTotal)+ courents);
            if (resTotal<0)Total.setTextColor(getResources().getColor(R.color.pink_color));
            else Total.setTextColor(getResources().getColor(R.color.white));
        }
        CheckPlan();
    }

    @Override
    public void onResume() {
        super.onResume();
        DataBaseTakeInformation();
        loadPlan();
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
        int id = item.getItemId();
        double course=ValutActivCourse();
        String courents = ValutActivType();

        switch (id){
            case R.id.nav_history:
                Intent intent_Operation = new Intent(this, Operation_page.class);
                intent_Operation.putExtra("Course", course);
                intent_Operation.putExtra("Course_type", courents);
                startActivityForResult(intent_Operation, 1);
                break;

            case R.id.nav_statistic:
                Intent intent_Chart = new Intent(this, Chart_page.class);
                startActivityForResult(intent_Chart, 1);
                break;

            case R.id.nav_currency:
                showDialog(0);
                break;

            case R.id.nav_plan:
                Intent intent_Plan = new Intent(this, Plan_page.class);
                intent_Plan.putExtra("Course", course);
                intent_Plan.putExtra("Course_type", courents);
                startActivityForResult(intent_Plan, 4);
                break;
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
                intent_Add_earn.putExtra("Course", course);
                startActivityForResult(intent_Add_earn, 1);
                break;

            case R.id.btnCard:
                category_name = "Card";
                intent_Add_earn.putExtra("Category", category_name);
                intent_Add_earn.putExtra("Course", course);
                startActivityForResult(intent_Add_earn, 1);
                break;

            case R.id.btnUser:
                if(!user_cat_exists){
                    user_cat_exists = true;
                    getCatName(User_label,"User1","User1_name", user_cat_exists);
                    User.setVisibility(View.VISIBLE);
                    User_label.setVisibility(View.VISIBLE);
                    User2_btn.setVisibility(View.VISIBLE);
                    User_btn.setImageResource(R.drawable.ic_iconfinder_user);
                    break;
                }
                else {
                    category_name = User_label.getText().toString();
                    intent_Add.putExtra("Category", category_name);
                    intent_Add.putExtra("Course", course);
                    startActivityForResult(intent_Add, 1);
                    break;
                }

            case R.id.btnUser2:
                if(!user2_cat_exists){
                    user2_cat_exists = true;
                    getCatName(User2_label,"User2","User2_name", user2_cat_exists);
                    User2.setVisibility(View.VISIBLE);
                    User2_label.setVisibility(View.VISIBLE);
                    User3_btn.setVisibility(View.VISIBLE);
                    User2_btn.setImageResource(R.drawable.ic_iconfinder_user);
                    break;
                }
                else {
                    category_name = User2_label.getText().toString();
                    intent_Add.putExtra("Category", category_name);
                    intent_Add.putExtra("Course", course);
                    startActivityForResult(intent_Add, 1);
                    break;
                }

            case R.id.btnUser3:
                if(!user3_cat_exists){
                    user3_cat_exists = true;
                    getCatName(User3_label,"User3","User3_name", user3_cat_exists);
                    User3.setVisibility(View.VISIBLE);
                    User3_label.setVisibility(View.VISIBLE);
                    User3_btn.setImageResource(R.drawable.ic_iconfinder_user);
                    break;
                }
                else {
                    category_name = User3_label.getText().toString();
                    intent_Add.putExtra("Category", category_name);
                    intent_Add.putExtra("Course", course);
                    startActivityForResult(intent_Add, 1);
                    break;
                }
        }
    }

    public class NewThread extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... arg) {

            Document doc;
            try {
                doc = Jsoup.connect("https://finance.ua/ua/currency").get();
                title = doc.select(".c3");
                String[] masiv = new String[3];
                int i = 0;
                course_masiv[0]=1;
                for (Element titles : title) {
                    if (i < 3) {
                        masiv[i] = titles.text();
                        course_masiv[i+1] = Double.parseDouble(masiv[i]);
                        Log.d("myLog", masiv[i] + " next ");
                        i++;
                    }

                }
            } catch (Exception e) {
                course_masiv[0]=1;
                course_masiv[1]=26;
                course_masiv[2]=29;
                course_masiv[3]=0.4;
            }
            return null;
        }
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Select currency");
        adb.setSingleChoiceItems(currency, 0, myClickListener);
        adb.setPositiveButton("OK", myClickListener);
        return adb.create();
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            ListView lv = ((AlertDialog) dialog).getListView();
            current_currency = lv.getAdapter().getItem(lv.getCheckedItemPosition());
            switch ((String)current_currency) {
                case ("$ USD"):
                    current_currency = "$";
                    break;

                case ("₴ UAH"):
                    current_currency = "₴";
                    break;

                case ("€ EUR"):
                    current_currency = "€";
                    break;

                case ("P RUB"):
                    current_currency = "P";
                    break;
            }
            Log.d("myLog", (String) current_currency);
            ValutUpdater((String) current_currency);
            onResume();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String courents = ValutActivType();
        if (data == null) {return;}
        switch(requestCode) {
            case 4:
                plan_sum = data.getDoubleExtra("hola", 0);
                Plan.setText(format.format(plan_sum) + courents);
                sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("Plan", String.valueOf(plan_sum));
                ed.apply();
                break;
        }
    }

    public void CheckPlan() {
        if(dbHelper.checkForTables("Finance_app_add_table") || plan_sum!=0) {
            double course = ValutActivCourse();
            if (plan_sum / course < resCost && plan_sum != 0)
                Cost.setTextColor(getResources().getColor(R.color.pink_color));
            else Cost.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void loadPlan() {
        double course = ValutActivCourse();
        String courents = ValutActivType();
        try{
            sPref = getPreferences(MODE_PRIVATE);
            plan_sum = Double.parseDouble(sPref.getString("Plan", ""));
            Plan.setText(format.format(plan_sum / course) + courents);
        }
        catch (Exception e){
            Plan.setText("0" + courents);
        }
    }

    public void loadUserCat() {
        try{
            user_cat_pref = getPreferences(MODE_PRIVATE);
            SharedPreferences preferences = getApplicationContext().getSharedPreferences(" SHARED_PREFERENCES_NAME ", android.content.Context.MODE_PRIVATE);
            user_cat_exists = preferences.getBoolean("User1", false);
            lable = preferences.getString("User1_name", "None");
            if(user_cat_exists){
                User.setVisibility(View.VISIBLE);
                User_label.setVisibility(View.VISIBLE);
                User2_btn.setVisibility(View.VISIBLE);
                User_btn.setImageResource(R.drawable.ic_iconfinder_user);
                User_label.setText(lable);
            }
            user2_cat_exists = preferences.getBoolean("User2", false);
            lable2 = preferences.getString("User2_name", "None");
            if(user2_cat_exists){
                User2.setVisibility(View.VISIBLE);
                User2_label.setVisibility(View.VISIBLE);
                User3_btn.setVisibility(View.VISIBLE);
                User2_btn.setImageResource(R.drawable.ic_iconfinder_user);
                User2_label.setText(lable2);
            }
            user3_cat_exists = preferences.getBoolean("User3", false);
            lable3 = preferences.getString("User3_name", "None");
            if(user3_cat_exists){
                User3.setVisibility(View.VISIBLE);
                User3_label.setVisibility(View.VISIBLE);
                User3_btn.setImageResource(R.drawable.ic_iconfinder_user);
                User3_label.setText(lable3);
            }
        }
        catch (Exception e){
        }
    }

    public void getCatName (final TextView txt, final String user_cat, final String name,
                            final boolean cat_exist){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        mDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                txt.setText(userInput.getText().toString());
                                SharedPreferences preferences = getApplicationContext().getSharedPreferences(" SHARED_PREFERENCES_NAME ", android.content.Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean(user_cat, cat_exist);
                                editor.putString(name, (txt.getText().toString()));
                                editor.commit();
                            }
                        })
                .setNegativeButton("Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();

        alertDialog.show();

    }

}
