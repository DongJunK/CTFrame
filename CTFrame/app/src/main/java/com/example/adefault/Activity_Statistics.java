package com.example.adefault;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.adefault.Interfaces.SendDataToServer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity_Statistics extends AppCompatActivity {

    BarChart barChart;

    ArrayList<BarEntry> barPositive = new ArrayList<>();
    ArrayList<BarEntry> barNegative = new ArrayList<>();

    int positiveCount[] = new int[24];
    int negativeCount[] = new int[24];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        for(int i=0; i<24; i++)
        {
            positiveCount[i] = 0;
            negativeCount[i] = 0;
        }
        BarChartInit();
    }

    private void BarChartInit()
    {
        barChart = (BarChart) findViewById(R.id.barChart);
        Log.i("Statistics", "BarChartInit 실행" );
        RequestStatistics();

        // create BarEntry for Bar Group 1
        for(int i=0; i<24; i++)
        {
            barPositive.add(new BarEntry(positiveCount[i],i));
        }

        for(int i=0; i<24; i++)
        {
            barNegative.add(new BarEntry(negativeCount[i], i));
        }

        // creating dataset for Bar Group1
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSetP = new BarDataSet(barPositive, "Positive Bar Group");
        barDataSetP.setColor(Color.rgb(0,0,255));

        // creating dataset for Bar Group 2
        BarDataSet barDataSetN = new BarDataSet(barNegative, "Negative Bar Group");
        barDataSetN.setColor(Color.rgb(255,0,0));

        //0~24
        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0; i<24; i++)
        {
            labels.add(i+"");
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(barDataSetP);
        dataSets.add(barDataSetN);

// initialize the Bardata with argument labels and dataSet
        BarData data = new BarData(labels, dataSets);
        barChart.setData(data);

        barChart.animateY(1000);
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
                            positiveCount[Integer.parseInt(date.substring(11,13))]++;
                        }
                        else
                        {
                            negativeCount[Integer.parseInt(date.substring(11,13))]++;
                        }
                    }
                } catch (JSONException e) {
                    Log.i("CTFrame", "JSONError : " + e.toString());
                }
            } catch (Exception e) {
                Log.i("Exception", e.toString());
            }
        }
    }
}
