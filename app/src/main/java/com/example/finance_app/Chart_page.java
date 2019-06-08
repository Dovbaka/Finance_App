package com.example.finance_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class Chart_page extends AppCompatActivity {

    public static final int[] CHART_COLORS = {
            Color.rgb(255, 245, 0)/*Yellow*/,
            Color.rgb(0, 8, 202)/*dark blue*/,
            Color.rgb(5, 226, 0)/*Green*/,
            Color.rgb(255, 168, 0)/*Orange*/,
            Color.rgb(0, 129, 223)/*Azure*/,
            Color.rgb(192, 0, 0)/*Red*/,
            Color.rgb(44, 215, 192)/*Blue*/,
            Color.rgb(255, 0, 148)/*Pink*/ ,
            Color.rgb(135, 135, 135)/*Grey*/
    };

    DataBase dbHelper;

    String[] category = { "Cafe", "Food", "Home", "Transport", "Shopping", "gift",
            "Health", "Leisure", "Family" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_page);

        SetupChart();
    }

    private void SetupChart() {

        ArrayList<PieEntry> pieEntries = getPieEntries();

        PieDataSet dataSet = new PieDataSet(pieEntries, "");

        dataSet.setColors(CHART_COLORS);

        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.chart);

        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(10);

        dataSet.setValueLinePart1Length(0.4f);
        dataSet.setValueLinePart2Length(0.1f);
        dataSet.setSliceSpace(3);
        //dataSet.setValueLineColor(Color.WHITE);
        dataSet.setUsingSliceColorAsValueLineColor(true);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        chart.setUsePercentValues(true);
        chart.setHoleRadius(60);
        chart.setTransparentCircleRadius(0);
        chart.setEntryLabelColor(0);
        chart.getDescription().setEnabled(false);
        chart.setHoleColor(0);
        chart.animateY(500);
        chart.setExtraOffsets(15,10,15,5);
        chart.setDragDecelerationFrictionCoef(10f);

        chart.setExtraBottomOffset(10);

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(20);
        chart.getLegend().setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setYEntrySpace(10f);
        legend.setYOffset(20f);
        legend.setXOffset(20f);
        legend.setTextColor(Color.WHITE);
        //chart.getLegend().setEnabled(false);

        chart.setData(data);
    }

    public ArrayList<PieEntry> getPieEntries() {
        dbHelper = new DataBase(this);
        String sum = "sum";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = null;
        String[] columns = new String[] { "category", "sum(sum) as sum" };//new String[]{"SUM(income) AS " + sum,"income_category"};
        c = db.query("Finance_app_add_table", columns, null, null, "category", null, null);
        ArrayList<PieEntry> rv = new ArrayList<>();
        while(c.moveToNext()) {
            if(!c.getString(c.getColumnIndex("category")).equals("Salary"))
            rv.add(new PieEntry(c.getFloat(c.getColumnIndex(sum)), c.getString(c.getColumnIndex("category"))));
        }
        c.close();

        return rv;
    }
}
