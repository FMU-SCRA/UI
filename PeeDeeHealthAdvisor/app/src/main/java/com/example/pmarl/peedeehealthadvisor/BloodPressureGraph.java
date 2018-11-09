/*
    Author: Patrick Marlowe
    Email Address: pmarlowe782@gmail.com
    Written: June 22, 2018
    Last Updated: June 25, 2018

    Compilation:
    Execution:

    Description of Execution:
 */
package com.example.pmarl.peedeehealthadvisor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultXAxisValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class BloodPressureGraph extends AppCompatActivity
{
    private ImageButton home;
    LineChart lineChart;


    @Override
    protected void  onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.blood_pressure_graph);

        home = (ImageButton) findViewById(R.id.Home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainActivity();
            }
        });

        // finding the graph by element id.
        lineChart = (LineChart) findViewById(R.id.lineGraph);

        /*Creating a cursor, which is a table that stores the data from
        * the sql query*/
        Cursor  cursor = MainActivity.myDB.readBloodPressure();

        /*Creating an array list that will hold the systolic values and the dates that
        * the values were taken on*/
        ArrayList<Entry> systolic = new ArrayList<>();

        /*Creating an array list that will hold the diastolic values and the dates that
        * the values were taken on*/
        ArrayList<Entry> diastolic = new ArrayList<>();

        /*Just a test array for the x values for the graph*/
        final ArrayList<String> xLabels = new ArrayList<>();

        /*Test values for the xLabels array*/
        Integer i = 0;

            /*Loop through the rows of the cursor and set the (y,x) values
            * cursor.moveToNext() returns a boolean value and performs an action to move to the
            * next cursor*/
            while(cursor.moveToNext())
            {
                /*Adding the systolic value from the DB and the date that corresponds
                * with it*/
                systolic.add(new Entry(Integer.parseInt(cursor.getString(1)),i));

                /*Adding the diastolic value from the DB and the date that corresponds
                * with it*/
                diastolic.add(new Entry(Integer.parseInt(cursor.getString(2)),i));

                /*Adding test values for the systolic and diastolic values*/
                xLabels.add(i.toString());

                /*Incrementing i*/
                i++;


            }


        /*Creating the systolic data set and setting different attributes for it*/
        LineDataSet systolicSet = new LineDataSet(systolic,"Systolic");
        systolicSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        systolicSet.setColor(getResources().getColor(R.color.RedHuesDark));
        systolicSet.setValueTextSize(13f);
        systolicSet.setDrawCubic(true);//Allows for curved lines instead of linear lines
        systolicSet.setCircleRadius(4f);
        systolicSet.setCircleColor(getResources().getColor(R.color.RedHuesLight));
        systolicSet.setLineWidth(2f);


        /*Creatin the diastolic data set and setting different attributes for it*/
        LineDataSet diastolicSet = new LineDataSet(diastolic,"Diastolic");
        diastolicSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        diastolicSet.setColor(getResources().getColor(R.color.RedHuesLight));
        diastolicSet.setValueTextSize(13f);
        diastolicSet.setDrawCubic(true);//Allows for curved lines instead of linear lines
        diastolicSet.setCircleRadius(4f);
        diastolicSet.setCircleColor(getResources().getColor(R.color.RedHuesDark));
        diastolicSet.setLineWidth(2f);
        diastolicSet.enableDashedLine(10f,5f,0f);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);


        /*Creating an array list for your data sets
        * "A set of sets"*/
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(systolicSet);
        dataSets.add(diastolicSet);

        /*creating the data to be plotted in the line chart*/
        LineData data = new LineData(xLabels, dataSets);

        /*Setting the data and attributes for the line chart*/
        lineChart.setDescription("Blood Pressure");
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        lineChart.setData(data);
        lineChart.setVisibleXRangeMaximum(30);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDoubleTapToZoomEnabled(true);
        lineChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        launchPrevActivity();
    }

    private void launchMainActivity()
    {
        Intent intent = new Intent (this, MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void launchPrevActivity()
    {
        Intent intent = new Intent (this, SelectBloodPressureActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}