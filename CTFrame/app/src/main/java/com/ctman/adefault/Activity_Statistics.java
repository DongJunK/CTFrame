package com.ctman.adefault;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ctman.adefault.Interfaces.SendDataToServer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Activity_Statistics extends AppCompatActivity {

    BarChart barChart;
    PieChart pieChart;

    ArrayList<BarEntry> barPositive = new ArrayList<>();
    ArrayList<BarEntry> barNegative = new ArrayList<>();

    ArrayList<Entry> pieValues = new ArrayList<Entry>();

    int positivePieCount = 0, negativePieCount = 0;

    int positiveBarCount[] = new int[24];
    int negativeBarCount[] = new int[24];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        for(int i=0; i<24; i++)
        {
            positiveBarCount[i] = 0;
            negativeBarCount[i] = 0;
        }
        BarChartInit();
        PieChartInit();
    }

    private void BarChartInit()
    {
        barChart = (BarChart) findViewById(R.id.barChart);

        barChart.setDescription("24시간 표정 통계(횟수)");
        RequestStatistics();

        // create BarEntry for Bar Group 1
        for(int i=0; i<24; i++)
        {
            barPositive.add(new BarEntry(positiveBarCount[i],i));
        }

        for(int i=0; i<24; i++)
        {
            barNegative.add(new BarEntry(negativeBarCount[i], i));
        }

        // creating dataset for Bar Group1
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSetP = new BarDataSet(barPositive, "긍정적인 표정");
        barDataSetP.setColor(Color.rgb(100,100,255));

        // creating dataset for Bar Group 2
        BarDataSet barDataSetN = new BarDataSet(barNegative, "부정적인 표정");
        barDataSetN.setColor(Color.rgb(255,100,100));

        //0~24
        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0; i<24; i++)
        {
            labels.add(i+"시");
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(barDataSetP);
        dataSets.add(barDataSetN);

// initialize the Bardata with argument labels and dataSet
        BarData data = new BarData(labels, dataSets);
        data.setValueFormatter(new DefaultValueFormatter(0));
        barChart.setData(data);

        barChart.animateY(1000);
    }

    private void PieChartInit()
    {
        pieChart = (PieChart)findViewById(R.id.pieChart);

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        pieValues.add(new Entry(positivePieCount,0));
        pieValues.add(new Entry(negativePieCount,1));

        PieDataSet dataSet = new PieDataSet(pieValues,"");

        ArrayList<String> emotion = new ArrayList<String>();

        emotion.add("긍정적인 표정");
        emotion.add("부정적인 표정");


        PieData data = new PieData(emotion, dataSet);
        // In Percentage
        pieChart.setUsePercentValues(true);
        data.setValueFormatter(new PercentFormatter());

        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(data);
        pieChart.setDescription("하루 표정 통계(%)");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(58f);

        int[] colorSet = {Color.rgb(100,100,255), Color.rgb(255,100,100)};
        pieChart.setHoleRadius(58f);
        dataSet.setColors(colorSet);

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.animateY(1000);

        //클릭 리스너
        //pieChart.setOnChartValueSelectedListener(this);
    }

    private void RequestStatistics() {
        //section 0 여기 건들지마
        JSONObject obj = new JSONObject();
        SendDataToServer sendDataToServer = new SendDataToServer();
        //section 0 여기 건들지마

        //section 2 여기는 고치치마라//
        JSONObject post_dict = new JSONObject();
        //section 2 여기 까지//

        //section 3 보내야 하는 값 만큼 매치시켜줘서 보내면됨//
        try {
            post_dict.put("email", MainActivity.loginId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //section 3 여기까지//

        if (post_dict.length() > 0) {
            try {
                //section 4   "signUpCheck 라고 되어있는 부분을 승배가 준 파일로 고쳐서 보낼것 //
                obj = new JSONObject(sendDataToServer.execute(String.valueOf(post_dict), "request_statistics").get());
                //section 4//

                try {
                    JSONArray staArray = obj.getJSONArray("statistics");
                    for (int i = 0; i < staArray.length(); i++) {
                        JSONObject tempobj = staArray.getJSONObject(i);
                        String emotion = tempobj.getString("emotion");
                        String date = tempobj.getString("date");

                        if(emotion.equals("positive"))
                        {
                            positiveBarCount[Integer.parseInt(date.substring(11,13))]++;
                            positivePieCount++;
                        }
                        else
                        {
                            negativeBarCount[Integer.parseInt(date.substring(11,13))]++;
                            negativePieCount++;
                        }
                    }
                } catch (JSONException e) {
                    //Log.i("CTFrame", "JSONError : " + e.toString());
                }
            } catch (Exception e) {
                //Log.i("Exception", e.toString());
            }
        }
    }
}
