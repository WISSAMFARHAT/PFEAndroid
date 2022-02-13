package com.example.time;

import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Usage_statics extends AppCompatActivity {

    TextView date,Total,title;
    CheckBox daily,weekly,monthly;
    DateTimeFormatter dtf;
    LocalDateTime now;
    Button left,riht;
    int day=0,week=0,month=0;
    Calendar cal,cal_month;
    BarChart barChart;
    ArrayList<Integer> colors =new ArrayList<>();
    ArrayList<String> name =new ArrayList<>();
    ArrayList<Statique> number=new ArrayList<>();

    ArrayList<UsageStats> statsapp=new ArrayList<>();


    List<UsageStats> stats=new ArrayList<>();
    private UsageStatsManager mUsageStatsManager;
    private final ArrayMap<String, String> mAppLabelMap = new ArrayMap<>();
    public static ArrayList<String> arrayList_Games;
    public static ArrayList<String> arrayList_News;
    public static ArrayList<String> arrayList_Productive;
    public static ArrayList<String> arrayList_SocialMedia;
    public static  ArrayList<String> arrayList_Undifiend;
    SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
    DatabaseHelper myDb;
    public  float  Toltal=0;
    private PackageManager mPm;

    private float Game=0;
    private float News=0;
    public float Social=0;
    private float undfiend=0;
    private  float Productiv=0;


    TextView txtUnd,txtMedia,txtProductive,txtNews,txtGames;

    public class Statique{
        public Float time;
        public String name;
        Statique(Float time,String name){
            this.name=name;
            this.time=time;
        }
    }
int id=0;





    public ArrayList<UsageStats> Usagestats(Date start , Date end ,String type){
                ArrayList<UsageStats> mPackageStats = new ArrayList<>();

                if(type.equals("daily")) {

                    stats =
                            mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                    start.getTime(), end.getTime());



                }
                if(type.equals("week")) {


                    stats =
                            mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                                    start.getTime(), end.getTime());


                }

                else if(type.equals("monthly")){
                    stats =
                            mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY,//because monthly get fault number
                                    start.getTime(), end.getTime());
                }
                if (stats == null) {

                    return null;
                }
        if(stats.size()>0) {
            Toast.makeText(Usage_statics.this, "Begin Time " + df.format(stats.get(0).getFirstTimeStamp()), Toast.LENGTH_LONG).show();
        }
                Toltal=0;
                ArrayMap<String, UsageStats> map = new ArrayMap<>();
                final int statCount = stats.size();
                for (int i = 0; i < statCount; i++) {

                    final UsageStats pkgStats = stats.get(i);
                    Toltal=Toltal+pkgStats.getTotalTimeInForeground()/1000;

            try {

                ApplicationInfo appInfo = mPm.getApplicationInfo(pkgStats.getPackageName(), 0);
                String label = appInfo.loadLabel(mPm).toString();

                if(mAppLabelMap.equals(pkgStats.getPackageName())){


                }else {
                    mAppLabelMap.put(pkgStats.getPackageName(), label);
                }


                UsageStats existingStats =
                        map.get(pkgStats.getPackageName());
                if (existingStats == null) {

                    if(pkgStats.getTotalTimeInForeground()!=0) {

                        map.put(pkgStats.getPackageName(), pkgStats);
                    }
                } else {


                    existingStats.add(pkgStats);
                }


            } catch (PackageManager.NameNotFoundException e) {
                // This package may be gone.
            }
        }
        mPackageStats.addAll(map.values());

    return mPackageStats;


    }




    public void GetTotalTime(ArrayList<UsageStats> mPackageStats ){
        for(int i=0;i<mPackageStats.size();i++){
            UsageStats pkgStats = mPackageStats.get(i);
            for (int k=0;k<arrayList_Games.size();k++){
                if(pkgStats.getPackageName().equals(arrayList_Games.get(k))){
                    Game=Game+pkgStats.getTotalTimeInForeground()/1000;
                }
            }

            for (int k=0;k<arrayList_News.size();k++){
                if(pkgStats.getPackageName().equals(arrayList_News.get(k))){
                    News=News+pkgStats.getTotalTimeInForeground()/1000;
                }
            }

            for (int k=0;k<arrayList_Productive.size();k++){
                if(pkgStats.getPackageName().equals(arrayList_Productive.get(k))){
                    Productiv=Productiv+pkgStats.getTotalTimeInForeground()/1000;
                }
            }


            for (int k=0;k<arrayList_SocialMedia.size();k++){
                if(pkgStats.getPackageName().equals(arrayList_SocialMedia.get(k))){
                    Social=Social+pkgStats.getTotalTimeInForeground()/1000;
                }
            }


            for (int k=0;k<arrayList_Undifiend.size();k++){
                if(pkgStats.getPackageName().equals(arrayList_Undifiend.get(k))){
                    undfiend=undfiend+pkgStats.getTotalTimeInForeground()/1000;
                }
            }

        }

    }

    public void GetUsage(Date begin ,Date end,String type){



            Social=0;
            Toltal=0;
            News=0;
            Productiv=0;
            Game=0;
            undfiend=0;



            statsapp= Usagestats(begin,end,type);
            if(statsapp.size()!=0) {
                GetTotalTime(statsapp);
            }

        float time_social = (Social);
        float perc_social = (time_social * 100) / Toltal;

        float time_productive = (Productiv);
        float perc_productive = (time_productive * 100) / Toltal;

        float time_news = (News);
        float perc_news = (time_news * 100) / Toltal;


        float time_game = (Game);
        float perc_game = (time_game * 100) / Toltal;


        float time_undifend = (undfiend);
        float perc_undifend = (time_undifend * 100) / Toltal;
        if(perc_news==0){
            perc_news=1;
        }
        if(perc_game==0){
            perc_game=1;
        }
        if(perc_productive==0){
            perc_productive=1;
        }
        if(perc_social==0){
            perc_social=1;
        }
        if(perc_undifend==0){
            perc_undifend=1;
        }



        number.add(new Statique((perc_game),"Games"));
        number.add(new Statique((perc_news),"News"));
        number.add(new Statique((perc_productive),"Productive"));
        number.add(new Statique((perc_social),"Social"));
        number.add(new Statique((perc_undifend),"Undifend"));


        for(int i=0;i<number.size();i++){
            if(number.get(i).name=="Games"){
                colors.add(Color.rgb(134,110,74));
                name.add("Game");

            }else if(number.get(i).name=="News"){
                colors.add(Color.rgb(249,46,34));
                name.add("News");
            }else if(number.get(i).name=="Productive"){
                colors.add(Color.rgb(212,80,135));
                name.add("Productive");
            }else if(number.get(i).name=="Social"){
                colors.add(Color.rgb(2,119,189));
                name.add("Social Media");
            }else if(number.get(i).name=="Undifend"){
                colors.add(Color.rgb(253,210,14));
                name.add("Undifiend");
            }
        }


        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(1f, number.get(0).time));
        entries.add(new BarEntry(1f, number.get(0).time));
        entries.add(new BarEntry(1f, number.get(0).time));


        entries.add(new BarEntry(2f, number.get(1).time));
        entries.add(new BarEntry(2f, number.get(1).time));
        entries.add(new BarEntry(2f, number.get(1).time));

        entries.add(new BarEntry(3f, number.get(2).time));
        entries.add(new BarEntry(3f, number.get(2).time));
        entries.add(new BarEntry(3f, number.get(2).time));

        entries.add(new BarEntry(4f, number.get(3).time));
        entries.add(new BarEntry(4f, number.get(3).time));
        entries.add(new BarEntry(4f, number.get(3).time));

        entries.add(new BarEntry(5f, number.get(4).time));
        entries.add(new BarEntry(5f, number.get(4).time));
        entries.add(new BarEntry(5f, number.get(4).time));


        BarDataSet bardataset = new BarDataSet(entries, "Cells");
        barChart.getXAxis().setValueFormatter(null);
        BarData data = new BarData(bardataset);


        barChart.setData(data); // set the data and list of labels into chart
        barChart.getLegend().setEnabled(false);
        bardataset.setColors(colors.get(1),colors.get(3),colors.get(0),colors.get(2),colors.get(4));
        barChart.animateY(5000);
        Legend legends =barChart.getLegend();
        legends.setEnabled(true);
        legends.setTextSize(9);
        legends.setForm(Legend.LegendForm.LINE);
        legends.setFormSize(10);

        Legend legend =barChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(9);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(10);

        LegendEntry[] legendEntries= new LegendEntry[5];
        for(int i =0;i<legendEntries.length;i++){
            LegendEntry entry=new LegendEntry();
            entry.formColor=colors.get(i);
            entry.label=name.get(i);
            legendEntries[i]=entry;
        }
        legend.setCustom(legendEntries);


    }
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
//        startService(new Intent(this,Service_Block.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_statics);

        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        mPm = getPackageManager();
        title=findViewById(R.id.textView6);
        date=findViewById(R.id.date);
        Total=findViewById(R.id.views);
        daily=findViewById(R.id.daily);
        weekly=findViewById(R.id.weekly);
        monthly=findViewById(R.id.monthly);
        left=findViewById(R.id.left);
        riht=findViewById(R.id.right);
        dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        now = LocalDateTime.now();


        txtUnd=findViewById(R.id.Undifiend);
        txtMedia=findViewById(R.id.SocialMedia);
        txtProductive=findViewById(R.id.Productive);
        txtNews=findViewById(R.id.News);
        txtGames=findViewById(R.id.Games);

        myDb=new DatabaseHelper(this);
        arrayList_News=myDb.select(5);
        arrayList_SocialMedia=myDb.select(4);
        arrayList_Productive=myDb.select(7);
        arrayList_Games=myDb.select(1);
        arrayList_Undifiend=myDb.select(-1);

        barChart=(BarChart)findViewById(R.id.barchart);
        barChart.setFitBars(true);
        barChart.setDescription(null);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        barChart.setScaleEnabled(false);


        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                daily.setChecked(true);
                weekly.setChecked(false);
                monthly.setChecked(false);
                date.setText(dtf.format(now));
                title.setText("My Day ( "+dtf.format(now)+")");
                day=0;
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        day--;
                        date.setText(dtf.format(now.minusDays(-day)));
                        title.setText("My Day ( "+dtf.format(now.minusDays(-day))+")");
                        if(day>-8 && day<=0) {
                            try {
                                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                                Date st = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                                Date ed = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                                st.setHours(4);
                                st.setMinutes(1);
                                st.setSeconds(0);
                                ed.setHours(23);
                                ed.setMinutes(59);
                                ed.setSeconds(59);
                                number.clear();
                                if(day==0){
                                    st.setHours(1);
                                    GetUsage(st, ed, "daily");
                                }else {
                                    st.setTime(st.getTime()+4400000l);
                                    GetUsage(st, ed, "daily");
                                }

                                Total.setText("Total Phone Usage: "+Time(Toltal));
                                txtUnd.setText(Time(undfiend));
                                txtMedia.setText(Time(Social));
                                txtProductive.setText(Time(Productiv));
                                txtNews.setText(Time(News));
                                txtGames.setText(Time(Game));

                            } catch (ParseException e) {
                            }
                        }else{
                            Total.setText("No data for the day");
                            barChart.setData(null); // set the data and list of labels into chart
                            barChart.getLegend().setEnabled(false);
                            txtUnd.setText("0");
                            txtMedia.setText("0");
                            txtProductive.setText("0");
                            txtNews.setText("0");
                            txtGames.setText("0");
                        }
                    }
                });


                riht.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     day++;
                     date.setText(dtf.format(now.plusDays(day)));
                     title.setText("My Day ( "+dtf.format(now.plusDays(day))+")");

                        if(day>-8 && day<=0) {
                            try {
                                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                                Date st = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                                Date ed = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                                st.setHours(4);
                                st.setMinutes(1);
                                st.setSeconds(0);
                                ed.setHours(23);
                                ed.setMinutes(59);
                                ed.setSeconds(59);
                                number.clear();
                                if(day==0){
                                    st.setHours(1);
                                    GetUsage(st, ed, "daily");
                                }else {
                                    st.setTime(st.getTime());
                                    GetUsage(st, ed, "daily");
                                }
                                Total.setText("Total Phone Usage: "+Time(Toltal));
                                txtUnd.setText(Time(undfiend));
                                txtMedia.setText(Time(Social));
                                txtProductive.setText(Time(Productiv));
                                txtNews.setText(Time(News));
                                txtGames.setText(Time(Game));

                            } catch (ParseException e) {
                            }
                        }else{
                            Total.setText("No data for the day");
                            barChart.setData(null); // set the data and list of labels into chart
                            barChart.getLegend().setEnabled(false);
                            txtUnd.setText("0");
                            txtMedia.setText("0");
                            txtProductive.setText("0");
                            txtNews.setText("0");
                            txtGames.setText("0");
                        }
                    }
                });

                try {
                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                    String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                    Date st = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                    Date ed = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                    st.setHours(1);
                    st.setMinutes(1);
                    st.setSeconds(0);
                    ed.setHours(23);
                    ed.setMinutes(59);
                    ed.setSeconds(59);
                    number.clear();
                    st.setTime(st.getTime());
                    GetUsage(st, ed, "daily");
                    Total.setText("Total Phone Usage: "+Time(Toltal));
                    txtUnd.setText(Time(undfiend));
                    txtMedia.setText(Time(Social));
                    txtProductive.setText(Time(Productiv));
                    txtNews.setText(Time(News));
                    txtGames.setText(Time(Game));
                }catch (ParseException e ){}

            }

        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily.setChecked(false);
                weekly.setChecked(true);
                monthly.setChecked(false);
                title.setText("My Week");
                week=0;
                cal=Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                // get start of this week in milliseconds
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM ");
                String date1 = format1.format(cal.getTime());
                Date d1=cal.getTime();
                //get end of this week in milliseconds
                cal.add(Calendar.DATE,6);
                String date2 = format1.format(cal.getTime());
                date.setTextSize(15);
                date.setPadding(8,25,0,0);
                date.setText(date1+" to "+date2);
                Date d2=cal.getTime();

                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        week++;
                        // get today and clear time of day
                        cal.add(Calendar.DATE,-7);
                        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                        SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM ");
                        String date1 = format1.format(cal.getTime());
                        Date d1=cal.getTime();
                        cal.add(Calendar.DATE,6);
                        String date2 = format1.format(cal.getTime());
                        date.setTextSize(15);
                        date.setPadding(8,25,0,0);
                        date.setText(date1+" to "+date2);
                        Date d2=cal.getTime();
                        if(week==0){
                        number.clear();
                            d1.setHours(1);
                            d1.setMinutes(1);
                            d1.setSeconds(0);
                            d2.setHours(24);
                            d2.setMinutes(0);
                            d2.setSeconds(0);

                        GetUsage(d1,d2,"week");
                            Total.setText("Total Phone Usage: "+Time(Toltal));
                            txtUnd.setText(Time(undfiend));
                            txtMedia.setText(Time(Social));
                            txtProductive.setText(Time(Productiv));
                            txtNews.setText(Time(News));
                            txtGames.setText(Time(Game));
                        }else{
                            Total.setText("No data for the week");
                            barChart.setData(null); // set the data and list of labels into chart
                            barChart.getLegend().setEnabled(false);
                            txtUnd.setText("0");
                            txtMedia.setText("0");
                            txtProductive.setText("0");
                            txtNews.setText("0");
                            txtGames.setText("0");
                        }
                    }
                });

                riht.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        week--;
                        cal.add(Calendar.DATE,1);
                        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                        SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM ");
                        String date1 = format1.format(cal.getTime());
                        Date d1=cal.getTime();
                        cal.add(Calendar.DATE,6);
                        String date2 = format1.format(cal.getTime());
                        date.setTextSize(15);
                        date.setPadding(8,25,0,0);
                        date.setText(date1+" to "+date2);
                        Date d2=cal.getTime();
                        number.clear();
                        if(week==0) {
                            d1.setHours(1);
                            d1.setMinutes(1);
                            d2.setHours(24);
                            d2.setMinutes(0);
                            GetUsage(d1, d2, "week");
                            Total.setText("Total Phone Usage: "+Time(Toltal));
                            txtUnd.setText(Time(undfiend));
                            txtMedia.setText(Time(Social));
                            txtProductive.setText(Time(Productiv));
                            txtNews.setText(Time(News));
                            txtGames.setText(Time(Game));
                        }else{
                            Total.setText("No data for the week");
                            barChart.setData(null); // set the data and list of labels into chart
                            barChart.getLegend().setEnabled(false);
                            txtUnd.setText("0");
                            txtMedia.setText("0");
                            txtProductive.setText("0");
                            txtNews.setText("0");
                            txtGames.setText("0");
                        }
                    }
                });
                number.clear();
                d1.setHours(1);
                d1.setMinutes(1);
                d1.setSeconds(0);
                d2.setHours(24);
                d2.setMinutes(0);
                d2.setSeconds(0);
                GetUsage(d1,d2,"week");
                Total.setText("Total Phone Usage: "+Time(Toltal));
                txtUnd.setText(Time(undfiend));
                txtMedia.setText(Time(Social));
                txtProductive.setText(Time(Productiv));
                txtNews.setText(Time(News));
                txtGames.setText(Time(Game));

            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily.setChecked(false);
                weekly.setChecked(false);
                monthly.setChecked(true);
                title.setText("My Month");
                month=0;
                cal_month=Calendar.getInstance();
                cal_month.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal_month.clear(Calendar.MINUTE);
                cal_month.clear(Calendar.SECOND);
                cal_month.clear(Calendar.MILLISECOND);
                int  startmonth=cal_month.getActualMinimum(Calendar.DATE);
                final int endmonth=cal_month.getActualMaximum(Calendar.DATE);
                final SimpleDateFormat months  = new SimpleDateFormat("MMMM");
                SimpleDateFormat date_months  = new SimpleDateFormat("MM");
                SimpleDateFormat years  = new SimpleDateFormat("yyyy");
                date.setText(""+startmonth+" to "+endmonth+" -"+months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime()));
                date.setTextSize(15);
                date.setPadding(8,25,0,0);

                try {
                    String date_first = "0" + startmonth + "/" + date_months.format(cal_month.getTime()) + "/" + years.format(cal_month.getTime());
                    Date d1 = df.parse(date_first);

                    String date_last = endmonth + "/" + date_months.format(cal_month.getTime()) + "/" + years.format(cal_month.getTime());
                    Date d2 = df.parse(date_last);

                    number.clear();
                    d1.setHours(1);
                    d1.setMinutes(1);
                    d2.setHours(24);
                    d2.setMinutes(0);
                    GetUsage(d1, d2, "monthly");
                    Total.setText("Total Phone Usage: "+Time(Toltal));
                    txtUnd.setText(Time(undfiend));
                    txtMedia.setText(Time(Social));
                    txtProductive.setText(Time(Productiv));
                    txtNews.setText(Time(News));
                    txtGames.setText(Time(Game));
                }catch (ParseException e ){}

                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        month++;
                        SimpleDateFormat years  = new SimpleDateFormat("yyyy");
                        cal_month.add(Calendar.MONTH, -1);
                        int  startmonth=cal_month.getActualMinimum(Calendar.DATE);
                        int endmonth=cal_month.getActualMaximum(Calendar.DATE);
                        SimpleDateFormat months  = new SimpleDateFormat("MMMM");
                        SimpleDateFormat date_months  = new SimpleDateFormat("MM");
                        date.setText(""+startmonth+" to "+endmonth+" -"+months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime()));
                        date.setTextSize(15);
                        date.setPadding(8,25,0,0);
                        if(month==0) {
                            try {
                                String date_first = "0" + startmonth + "/" + date_months.format(cal_month.getTime()) + "/" + years.format(cal_month.getTime());
                                Date d1 = df.parse(date_first);

                                String date_last = endmonth + "/" + date_months.format(cal_month.getTime()) + "/" + years.format(cal_month.getTime());
                                Date d2 = df.parse(date_last);

                                number.clear();
                                d1.setHours(1);
                                d1.setMinutes(1);
                                d2.setHours(24);
                                d2.setMinutes(0);
                                GetUsage(d1, d2, "monthly");
                                Total.setText("Total Phone Usage: "+Time(Toltal));
                                txtUnd.setText(Time(undfiend));
                                txtMedia.setText(Time(Social));
                                txtProductive.setText(Time(Productiv));
                                txtNews.setText(Time(News));
                                txtGames.setText(Time(Game));
                            } catch (ParseException e) {
                            }
                        }else{
                            Total.setText("No data for the month");
                            barChart.setData(null); // set the data and list of labels into chart
                            barChart.getLegend().setEnabled(false);
                            txtUnd.setText("0");
                            txtMedia.setText("0");
                            txtProductive.setText("0");
                            txtNews.setText("0");
                            txtGames.setText("0");
                        }

                    }
                });

                riht.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        month--;
                        SimpleDateFormat years  = new SimpleDateFormat("yyyy");
                        cal_month.add(Calendar.MONTH,1);
                        int  startmonth=cal_month.getActualMinimum(Calendar.DATE);
                        int endmonth=cal_month.getActualMaximum(Calendar.DATE);

                        SimpleDateFormat months  = new SimpleDateFormat("MMMM");
                        SimpleDateFormat date_months  = new SimpleDateFormat("MM");
                        date.setText(""+startmonth+" to "+endmonth+" -"+months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime()));
                        date.setTextSize(15);
                        date.setPadding(8,25,0,0);
                if(month==0) {
                    try {
                        String date_first = "0" + startmonth + "/" + date_months.format(cal_month.getTime()) + "/" + years.format(cal_month.getTime());
                        Date d1 = df.parse(date_first);

                        String date_last = endmonth + "/" + date_months.format(cal_month.getTime()) + "/" + years.format(cal_month.getTime());
                        Date d2 = df.parse(date_last);

                        number.clear();
                        d1.setHours(1);
                        d1.setMinutes(1);
                        d2.setHours(24);
                        d2.setMinutes(0);
                        GetUsage(d1, d2, "monthly");
                        Total.setText("Total Phone Usage: "+Time(Toltal));
                        txtUnd.setText(Time(undfiend));
                        txtMedia.setText(Time(Social));
                        txtProductive.setText(Time(Productiv));
                        txtNews.setText(Time(News));
                        txtGames.setText(Time(Game));
                    } catch (ParseException e) {
                    }
                }else{
                    Total.setText("No data for the month");
                    barChart.setData(null); // set the data and list of labels into chart
                    barChart.getLegend().setEnabled(false);
                    txtUnd.setText("0");
                    txtMedia.setText("0");
                    txtProductive.setText("0");
                    txtNews.setText("0");
                    txtGames.setText("0");
                }
                    }
                });

            }
        });



        if(daily.isChecked()){
            daily.setChecked(true);
            weekly.setChecked(false);
            monthly.setChecked(false);
            date.setText(dtf.format(now));
            day=0;
            title.setText("My Day ( "+dtf.format(now)+")");
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    day--;
                    date.setText(dtf.format(now.minusDays(-day)));
                    title.setText("My Day ( "+dtf.format(now.minusDays(-day))+")");
                    if(day>-8 && day<=0) {
                        try {
                            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                            Date st = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                            Date ed = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                            st.setHours(4);
                            st.setMinutes(1);
                            st.setSeconds(0);
                            ed.setHours(23);
                            ed.setMinutes(59);
                            ed.setSeconds(59);
                            number.clear();
                            if(day==0){
                                Calendar c = Calendar.getInstance();
                                st.setHours(1);
                                st.setMinutes(1);
                                ed.setHours(0);
                                ed.setMinutes(59);
                                c.setTime(ed);
                                c.add(Calendar.DATE, 1);
                                ed = c.getTime();

                                GetUsage(st, ed, "daily");
                            }else {
                                st.setTime(st.getTime());
                                GetUsage(st, ed, "daily");
                            }
                            Total.setText("Total Phone Usage: "+Time(Toltal));
                            txtUnd.setText(Time(undfiend));
                            txtMedia.setText(Time(Social));
                            txtProductive.setText(Time(Productiv));
                            txtNews.setText(Time(News));
                            txtGames.setText(Time(Game));
                        } catch (ParseException e) {
                        }
                    }
                        else{
                            Total.setText("No data for the day");
                            barChart.setData(null); // set the data and list of labels into chart
                            barChart.getLegend().setEnabled(false);
                        txtUnd.setText("0");
                        txtMedia.setText("0");
                        txtProductive.setText("0");
                        txtNews.setText("0");
                        txtGames.setText("0");

                    }
                }
            });


            riht.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    day++;
                    date.setText(dtf.format(now.plusDays(day)));
                    title.setText("My Day ( "+dtf.format(now.plusDays(day))+")");
                    if(day>-8 && day<=0) {
                        try {
                            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                            Date st = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                            Date ed = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                            st.setHours(4);
                            st.setMinutes(1);
                            st.setSeconds(0);
                            ed.setHours(23);
                            ed.setMinutes(59);
                            ed.setSeconds(59);
                            number.clear();
                            if(day==0){
                                Calendar c = Calendar.getInstance();
                                st.setHours(1);
                                st.setMinutes(1);
                                ed.setHours(0);
                                ed.setMinutes(59);
                                c.setTime(ed);
                                c.add(Calendar.DATE, 1);
                                ed = c.getTime();

                                GetUsage(st, ed, "daily");
                            }else {
                                st.setTime(st.getTime());
                                GetUsage(st, ed, "daily");
                            }
                            Total.setText("Total Phone Usage: "+Time(Toltal));
                            txtUnd.setText(Time(undfiend));
                            txtMedia.setText(Time(Social));
                            txtProductive.setText(Time(Productiv));
                            txtNews.setText(Time(News));
                            txtGames.setText(Time(Game));
                        } catch (ParseException e) {
                        }
                    }else{
                        Total.setText("No data for the day");
                        barChart.setData(null); // set the data and list of labels into chart
                        barChart.getLegend().setEnabled(false);
                        txtUnd.setText("0");
                        txtMedia.setText("0");
                        txtProductive.setText("0");
                        txtNews.setText("0");
                        txtGames.setText("0");
                    }
                }
            });

            try {
                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                Date st = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                Date ed = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                Calendar c = Calendar.getInstance();
                st.setHours(1);
                st.setMinutes(1);
                ed.setHours(0);
                ed.setMinutes(59);
                c.setTime(ed);
                c.add(Calendar.DATE, 1);
                ed = c.getTime();

                GetUsage(st, ed, "daily");

                Total.setText("Total Phone Usage: "+Time(Toltal));
                txtUnd.setText(Time(undfiend));
                txtMedia.setText(Time(Social));
                txtProductive.setText(Time(Productiv));
                txtNews.setText(Time(News));
                txtGames.setText(Time(Game));
            }catch (ParseException e ){}
        }
        }


}
