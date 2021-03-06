package com.fmu.pmarl.peedeehealthadvisor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

public class BodyWeightGraph extends AppCompatActivity
{
    LineChart lineChart;
    LineChart lineChartLand;
    TimeZone tz = Calendar.getInstance().getTimeZone();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList results = new ArrayList<BodyWeightDataObject>(); // make sure to change this to Blood Sugar Data Object


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void  onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.body_weight_graph);

        ImageButton home = findViewById(R.id.Home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainActivity();
            }
        });

        // finding the graph by element id.
        lineChart =  findViewById(R.id.lineGraph);
        lineChartLand =  findViewById(R.id.lineGraphWeightLand);

        /*Creating a cursor, which is a table that stores the data from
         * the sql query*/
        Cursor cursor = MainActivity.myDB.readBodyWeight();

        /*Creating an array list that will hold the systolic values and the dates that
         * the values were taken on*/
        ArrayList<Entry> weight = new ArrayList<>();



        /*Just a test array for the x values for the graph*/
        final ArrayList<String> xLabels = new ArrayList<>();

        /*Test values for the xLabels array*/
        Integer i = 0;
        String date;
        String dateCard;
        Date date1;
        long epoch;
        TreeMap<Long,ArrayList<Float>> treeMap = new TreeMap<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_body_weight);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyBodyWeightRecyclerViewAdapter(results); // make sure to check this
        mRecyclerView.setAdapter(mAdapter);


        /*Loop through the rows of the cursor and set the (y,x) values
         * cursor.moveToNext() returns a boolean value and performs an action to move to the
         * next cursor*/
        if (cursor == null) {

        }

        cursor.moveToLast();
        do {


            try
            {
                if(cursor.getCount() > 0) {
                    date = cursor.getString(0) + cursor.getString(1);
                    date1 = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz").parse(date);

                    if (tz.inDaylightTime(date1)) {
                        epoch = date1.getTime() - 3600000;
                    } else {
                        epoch = date1.getTime();
                    }

                    treeMap.put(epoch, new ArrayList<Float>());
                    /*weight Added*/
                    treeMap.get(epoch).add(Float.parseFloat(cursor.getString(2)));

                } else {
                    launchPrevActivity();
                }

            } catch (ParseException e)
            {
                e.printStackTrace();
            }

        } while (cursor.moveToPrevious());



        Set<Map.Entry<Long, ArrayList<Float>>> set = treeMap.entrySet();
        Iterator<Map.Entry<Long, ArrayList<Float>>> iterator = set.iterator();

        while(iterator.hasNext())
        {
            Map.Entry<Long, ArrayList<Float>> mEntry = iterator.next();
            ArrayList<Float> arrayList = mEntry.getValue();

            /*Adding the weight value from the DB and the date that corresponds
             * with it*/
            weight.add(new Entry(arrayList.get(0), i));



            /*Adding test values for the systolic and diastolic values*/
            Long epoch1 = mEntry.getKey();

            Date date2 = new Date(epoch1);

            dateCard = new SimpleDateFormat("MMM dd hh:mm a").format(date2);

            xLabels.add(new SimpleDateFormat("MMM dd").format(date2));

            BodyWeightDataObject bodyWeightEntry = new BodyWeightDataObject(arrayList.get(0), dateCard, epoch1);

            results.add(bodyWeightEntry);

            i++;
        }



        /*Creating the systolic data set and setting different attributes for it*/
        LineDataSet weightSet = new LineDataSet(weight,"Weight");
        weightSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        weightSet.setColor(getResources().getColor(R.color.BodyWeightHuesDark));
        weightSet.setValueTextSize(0f);
        weightSet.setDrawCubic(true);//Allows for curved lines instead of linear lines
        weightSet.setCircleRadius(8f);
        weightSet.setCircleColor(getResources().getColor(R.color.BodyWeightHuesDark));
        weightSet.setLineWidth(5f);





        // vertical graph
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(false); // turns off bottom and lines
        xAxis.setLabelRotationAngle(-90);

        // landscape graph
        XAxis xAxisLand = lineChartLand.getXAxis();
        xAxisLand.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisLand.setEnabled(true);
        xAxisLand.setLabelRotationAngle(-90);

        // vertical graph
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setEnabled(false);
        yAxisRight.setDrawGridLines(false);

        // landscape graph
        YAxis yAxisRightLand = lineChartLand.getAxisRight();
        yAxisRightLand.setDrawAxisLine(false);
        yAxisRightLand.setEnabled(false);
        yAxisRightLand.setDrawGridLines(false);

        // vertical graph
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawZeroLine(true);
        yAxisLeft.setEnabled(false);

        // landscape graph
        YAxis yAxisLeftLand = lineChartLand.getAxisLeft();
        yAxisLeftLand.setDrawZeroLine(true);
        yAxisLeftLand.setEnabled(true);

        /*Creating an array list for your data sets
         * "A set of sets"*/
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(weightSet);


        /*creating the data to be plotted in the line chart*/
        LineData data = new LineData(xLabels,dataSets);


        /*Setting the data and attributes for the line chart*/
        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        lineChart.setData(data);
        lineChart.setVisibleXRangeMaximum(30);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.animateXY(1700,0000);
        lineChart.setPinchZoom(true);
        lineChart.setDoubleTapToZoomEnabled(true);
        lineChart.invalidate();

        lineChartLand.setDescription("");
        lineChartLand.setNoDataTextDescription("You need to provide data for the chart.");
        lineChartLand.setData(data);
        lineChartLand.setVisibleXRangeMaximum(29);
        lineChartLand.setDrawGridBackground(false);
        lineChartLand.setDrawBorders(false);
        lineChartLand.setTouchEnabled(true);
        lineChartLand.setDragEnabled(true);
        lineChartLand.setScaleEnabled(false);
        lineChartLand.setPinchZoom(false);
        lineChartLand.animateXY(1700,0000);
        lineChartLand.setDoubleTapToZoomEnabled(false);
        lineChartLand.invalidate();
    }

    @Override
    public void onBackPressed() {
        launchPrevActivity();
    }

    private void launchMainActivity()
    {
        Intent intent = new Intent (this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void launchPrevActivity()
    {
        Intent intent = new Intent (this, SelectBodyWeightActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
