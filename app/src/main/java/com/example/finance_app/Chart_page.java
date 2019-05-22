package com.example.finance_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chart_page extends AppCompatActivity {

    DataBase dbHelper;

    final String ATTRIBUTE_NAME_CATEGORY = "category";
    final String ATTRIBUTE_NAME_SUM = "sum";
    final String ATTRIBUTE_NAME_TYPE = "type";
    final String ATTRIBUTE_NAME_DATE = "date";

    String category[] = {"Food", "Transport", "Health", "Home"};
    float cost[] = {5125, 512, 350.5f, 720};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_page);

        SetupChart();
    }

    private void SetupChart() {

        ArrayList<PieEntry> pieEntries = getPieEntries();

        PieDataSet dataSet = new PieDataSet(pieEntries, "Categories");

        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.chart);

        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(0);

        /*dataSet.setValueLinePart1OffsetPercentage(0.1f);
        dataSet.setValueLinePart1Length(0.1f);
        dataSet.setValueLinePart2Length(0.1f);
        dataSet.setValueLineColor(Color.WHITE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);*/


        chart.setUsePercentValues(true);
        chart.setHoleRadius(90);
        chart.setTransparentCircleRadius(0);
        chart.setEntryLabelColor(0);
        chart.getDescription().setEnabled(false);
        chart.setHoleColor(0);
        chart.animateY(500);

        //chart.setExtraTopOffset(10);
        chart.setExtraBottomOffset(10);

        Legend l= chart.getLegend();
        l.setTextColor(Color.WHITE);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(20);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYEntrySpace(10f);
        l.setYOffset(10f);

        chart.setData(data);
    }

    public ArrayList<PieEntry> getPieEntries() {
        dbHelper = new DataBase(this);
        String sum = "sum";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = null;
        String[] columns = new String[] { "category", "sum(sum) as sum" };//new String[]{"SUM(income) AS " + sum,"income_category"};
        c = db.query("mytable", columns, null, null, "category", null, null);
        ArrayList<PieEntry> rv = new ArrayList<>();
        while(c.moveToNext()) {
            if(!c.getString(c.getColumnIndex("category")).equals("Salary"))
            rv.add(new PieEntry(c.getFloat(c.getColumnIndex(sum)), c.getString(c.getColumnIndex("category"))));
        }
        c.close();
        return rv;
    }
}
