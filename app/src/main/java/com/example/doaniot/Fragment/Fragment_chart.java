package com.example.doaniot.Fragment;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doaniot.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment_chart extends Fragment{
    private LineChart lineChart;
    private BarChart barChart;
    private TextView tv_average_humidity,tv_average_temperature;
    private Handler handler;
    private ArrayList<Float> temperatureDatasheet;
    private ArrayList<Float> humidityDatasheet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        //Xu li barchart
        temperatureDatasheet = new ArrayList<>();
        humidityDatasheet = new ArrayList<>();
        handler = new Handler(Looper.getMainLooper());
        startScheduledUpdate();
    }
    //Lay du lieu tu Database va tao thoi gian check
    private void startScheduledUpdate() {
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               getDataFromDatabase();
               tv_average_temperature.setText(average(temperatureDatasheet)+"°C");
               tv_average_humidity.setText(average(humidityDatasheet)+"%");
               handler.postDelayed(this,10*1000);
           }
       },0);
    }

    private void upDateBarChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.clear();
        for(int i=0;i<temperatureDatasheet.size();i++){
            barEntries.add(new BarEntry(i,temperatureDatasheet.get(i)));
        }
        BarDataSet dataSet = new BarDataSet(barEntries,"Temperature");
        BarData data = new BarData(dataSet);
        barChart.setData(data);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(16f);
        dataSet.setColor(Color.WHITE);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.WHITE);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(60f);
        yAxis.setTextColor(Color.WHITE);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setText("");
        barChart.getDescription().setTextSize(16f);
        barChart.getDescription().setTextColor(Color.WHITE);
        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.WHITE);
        barChart.notifyDataSetChanged();
        barChart.invalidate();

    }

    private void getDataFromDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference humidity_Ref = database.getReference("Do am");
        humidity_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    float humidity_Data = snapshot.getValue(Float.class);
                    if (humidityDatasheet.size() > 5) humidityDatasheet.remove(0);
                    humidityDatasheet.add(humidity_Data);
                }
                upDateLineChart();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference temperature_Ref = database.getReference("Nhiet do");
        temperature_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    float temperature_Data = snapshot.getValue(Float.class);
                    if (temperatureDatasheet.size() > 5) temperatureDatasheet.remove(0);
                    temperatureDatasheet.add(temperature_Data);
                }
                upDateBarChart();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void upDateLineChart() {
        ArrayList<Entry> lineEntries = new ArrayList<>();
        lineEntries.clear();
        for(int i=0;i<humidityDatasheet.size();i++){
            lineEntries.add(new Entry(i,humidityDatasheet.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(lineEntries,"Humidity");
        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(16f);
        dataSet.setColor(Color.WHITE);
        dataSet.setCircleColor(Color.rgb(255,255,255));
        dataSet.setCircleHoleColor(Color.rgb(122,245,249));
        dataSet.setCircleRadius(6f);
        dataSet.setCircleHoleRadius(4.5f);
        dataSet.setLineWidth(2f);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(false);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisMinimum(0f);
        lineChart.getDescription().setText("");
        lineChart.getDescription().setTextColor(Color.WHITE);
        lineChart.getDescription().setTextSize(16f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
    private float average(List<Float> list){
        float result = 0;
        for (float i:list
             ) {
            result+=i;
        }
        return Math.round(result/list.size()*10)/10;
    }
    private void initView(View view) {
        lineChart = view.findViewById(R.id.linechart);
        barChart = view.findViewById(R.id.barchart);
        tv_average_humidity = view.findViewById(R.id.tv_Average_humidity);
        tv_average_temperature = view.findViewById(R.id.tv_Average_temperature);
        LinearLayout linearLayout = view.findViewById(R.id.mainLayout_chart);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(10000);
        animationDrawable.start();
    }


}


