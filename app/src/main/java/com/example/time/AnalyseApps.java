package com.example.time;

import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AnalyseApps extends AppCompatActivity {
    TextView Top,low,average,Name;
    BarChart barChart;
    Calendar c =Calendar.getInstance();
    Long TimeTop=0l,TimeLow=0l,Total=0l;
    Long L1=0l,L2=0l,L3=0l,L4=0l,L5=0l,L6=0l,L7=0l;
    Date D1,D2,D3,D4,D5,D6,D7;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM");
    private UsageStatsManager mUsageStatsManager;
    List<UsageStats> stats;
    PackageManager pm;
    ApplicationInfo ai;


    public String Time(Float time){
        int  minutes = (int) ((time / (60)) % 60);
        int seconds = (int) ((time) % 60);
        int hours = (int) ((time / (60 * 60) ) );
        if (hours == 0) {
            if (minutes == 0) {
                if (seconds == 0) {
                    return("1 s");
                } else {
                    return(""+seconds + " s");
                }
            } else {
                if (seconds == 0) {
                    return(""+minutes + " m ");
                } else {
                    return(""+minutes + " m " + seconds + " s");
                }
            }
        } else {
            if (minutes == 0) {
                if (seconds == 0) {
                    return("" + hours + " h ");
                } else {
                    return("" + hours + " H " + seconds + " s");
                }
            } else {
                if (seconds == 0) {
                    return("" + hours + " h " + minutes + " m ");
                } else {
                    return("" + hours + " h " + minutes + " m ");
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_apps);
        Bundle extras = getIntent().getExtras();
        Top = findViewById(R.id.Top);
        low = findViewById(R.id.Low);
        average = findViewById(R.id.Avverage);
        Name=findViewById(R.id.MyApp);
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        pm = getApplication().getPackageManager();
        try {
            ai=pm.getApplicationInfo(extras.getString("Name"),0);
            String name= (String) pm.getApplicationLabel(ai);
            if(name!=""){
                Name.setText("My App : "+name);
            }else{
                Name.setText("My App ");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        D1 = new Date();
        D2 = new Date();
        D3 = new Date();
        D4 = new Date();
        D5 = new Date();
        D6 = new Date();
        D7 = new Date();
        Date start, end;
        //-------------------------------- first day ----------------------------------------------------------------
        c.add(Calendar.DAY_OF_WEEK, -1);
        start = c.getTime();
        end = c.getTime();
        start.setHours(4);
        end.setHours(24);

        stats =
                mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start.getTime(), end.getTime());
        if (stats != null) {
            for (int i = 0; i < stats.size(); i++) {
                if (stats.get(i).getPackageName().equals(extras.getString("Name"))) {
                    L1 = stats.get(i).getTotalTimeInForeground();
                    D1.setTime(start.getTime());
                }
            }
        }

        stats.clear();
        //-------------------------------- second  day ----------------------------------------------------------------
        c.add(Calendar.DAY_OF_WEEK, -1);
        start = c.getTime();
        end = c.getTime();
        start.setHours(4);
        end.setHours(24);

        stats =
                mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start.getTime(), end.getTime());
        if (stats != null) {
            for (int i = 0; i < stats.size(); i++) {
                if (stats.get(i).getPackageName().equals(extras.getString("Name"))) {
                    L2 = stats.get(i).getTotalTimeInForeground();
                    D2.setTime(start.getTime());
                }
            }
        }

        stats.clear();
//-------------------------------- third day ----------------------------------------------------------------
        c.add(Calendar.DAY_OF_WEEK, -1);
        start = c.getTime();
        end = c.getTime();
        start.setHours(4);
        end.setHours(24);

        stats =
                mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start.getTime(), end.getTime());
        if (stats != null) {
            for (int i = 0; i < stats.size(); i++) {
                if (stats.get(i).getPackageName().equals(extras.getString("Name"))) {
                    L3 = stats.get(i).getTotalTimeInForeground();
                    D3.setTime(start.getTime());
                }
            }
        }

        stats.clear();
//-------------------------------- four day ----------------------------------------------------------------
        c.add(Calendar.DAY_OF_WEEK, -1);
        start = c.getTime();
        end = c.getTime();
        start.setHours(4);
        end.setHours(24);

        stats =
                mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start.getTime(), end.getTime());
        if (stats != null) {
            for (int i = 0; i < stats.size(); i++) {
                if (stats.get(i).getPackageName().equals(extras.getString("Name"))) {
                    L4 = stats.get(i).getTotalTimeInForeground();
                    D4.setTime(start.getTime());

                }
            }
        }

        stats.clear();
//-------------------------------- five day ----------------------------------------------------------------
        c.add(Calendar.DAY_OF_WEEK, -1);
        start = c.getTime();
        end = c.getTime();
        start.setHours(4);
        end.setHours(24);

        stats =
                mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start.getTime(), end.getTime());
        if (stats != null) {
            for (int i = 0; i < stats.size(); i++) {
                if (stats.get(i).getPackageName().equals(extras.getString("Name"))) {
                    L5 = stats.get(i).getTotalTimeInForeground();
                    D5.setTime(start.getTime());
                }
            }
        }
        stats.clear();
//-------------------------------- six day ----------------------------------------------------------------
        c.add(Calendar.DAY_OF_WEEK, -1);
        start = c.getTime();
        end = c.getTime();
        start.setHours(4);
        end.setHours(24);

        stats =
                mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start.getTime(), end.getTime());
        if (stats != null) {
            for (int i = 0; i < stats.size(); i++) {
                if (stats.get(i).getPackageName().equals(extras.getString("Name"))) {
                    L6 = stats.get(i).getTotalTimeInForeground();
                    D6.setTime(start.getTime());
                }
            }
        }
        stats.clear();
//-------------------------------- seven day ----------------------------------------------------------------
        c.add(Calendar.DAY_OF_WEEK, -1);
        start = c.getTime();
        end = c.getTime();
        start.setHours(4);
        end.setHours(24);

        stats =
                mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start.getTime(), end.getTime());
        if (stats != null) {
            for (int i = 0; i < stats.size(); i++) {
                if (stats.get(i).getPackageName().equals(extras.getString("Name"))) {
                    L7 = stats.get(i).getTotalTimeInForeground();
                    D7.setTime(start.getTime());
                }
            }
        }
        stats.clear();

        ArrayList<Long> arrayTime = new ArrayList<>();
        arrayTime.add(L1);
        arrayTime.add(L2);
        arrayTime.add(L3);
        arrayTime.add(L4);
        arrayTime.add(L5);
        arrayTime.add(L6);
        arrayTime.add(L7);

        ArrayList<Date> arrayDate = new ArrayList<>();
        arrayDate.add(D1);
        arrayDate.add(D2);
        arrayDate.add(D3);
        arrayDate.add(D4);
        arrayDate.add(D5);
        arrayDate.add(D6);
        arrayDate.add(D7);

        TimeTop = arrayTime.get(0);
        int tempTop = 0;
        for (int i = 0; i < arrayTime.size(); i++) {
            if (arrayTime.get(i) > TimeTop) {
                TimeTop = arrayTime.get(i);
                tempTop = i;
            }
        }
        TimeLow = TimeTop;
        int tempLow = 0;
        for (int i = 0; i < arrayTime.size(); i++) {
            if (arrayTime.get(i) != 0) {
                if (arrayTime.get(i) < TimeLow) {
                    TimeLow = arrayTime.get(i);
                    tempLow = i;
                }
            }
        }
        Total = L1 + L2 + L3 + L4 + L5 + L6 + L7;
        Total = Total / 7;

        Top.setText("" + Time((float) (TimeTop / 1000)) + "     " +
                "" + format.format(arrayDate.get(tempTop)));
        low.setText("" + Time((float) (TimeLow / 1000)) + "      " + format.format(arrayDate.get(tempLow)));
        average.setText(Time((float) (Total / 1000)));

//---------------------------------- PIE CHART-----------------------------------------------------------------
        barChart = (BarChart) findViewById(R.id.barchart);
        barChart.setFitBars(true);
        barChart.setDescription(null);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        barChart.setScaleEnabled(false);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, L1));
        entries.add(new BarEntry(0f, L1));
        entries.add(new BarEntry(0f, L1));

        entries.add(new BarEntry(1f, L2));
        entries.add(new BarEntry(1f, L2));
        entries.add(new BarEntry(1f, L2));


        entries.add(new BarEntry(2f, L3));
        entries.add(new BarEntry(2f, L3));
        entries.add(new BarEntry(2f, L3));

        entries.add(new BarEntry(3f, L4));
        entries.add(new BarEntry(3f, L4));
        entries.add(new BarEntry(3f, L4));

        entries.add(new BarEntry(4f, L5));
        entries.add(new BarEntry(4f, L5));
        entries.add(new BarEntry(4f, L5));


        entries.add(new BarEntry(5f, L6));
        entries.add(new BarEntry(5f, L6));
        entries.add(new BarEntry(5f, L6));

        entries.add(new BarEntry(6f, L7));
        entries.add(new BarEntry(6f, L7));
        entries.add(new BarEntry(6f, L7));


        BarDataSet bardataset = new BarDataSet(entries, "");

        BarData data = new BarData(bardataset);
        data.setDrawValues(false);
        barChart.setData(data); // set the data and list of labels into chart


        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add(format.format(D1));
        xAxisLabel.add(format.format(D2));
        xAxisLabel.add(format.format(D3));
        xAxisLabel.add(format.format(D4));
        xAxisLabel.add(format.format(D5));
        xAxisLabel.add(format.format(D6));
        xAxisLabel.add(format.format(D7));


        barChart.getXAxis().setEnabled(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

        barChart.getLegend().setEnabled(false);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String time =Time(e.getY()/1000);
                Toast.makeText(getApplication(),"Time : "+time,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });

    }
}

