package com.example.time.ui.home;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.autofill.Dataset;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.time.DatabaseHelper;
import com.example.time.Databaseadd;
import com.example.time.HomeActivity;
import com.example.time.Main_page;
import com.example.time.Myservice;
import com.example.time.R;
import com.example.time.Service_Block;
import com.example.time.Usage_Today;
import com.example.time.Usage_statics;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.NOTIFICATION_SERVICE;
import static java.text.DateFormat.getTimeInstance;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener , OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    private ListView listView;
    private static final String TAG = "UsageStatsActivity";
    private UsageStatsManager mUsageStatsManager;
    private GoogleApiClient mApiClient;
    static  String st="0";
    static String kilometre="";
    static int pers=0;
    float calories=0;
    TextView km,calo;
    static String temp="";
    private LayoutInflater mInflater;
    private HomeFragment.UsageStatsAdapter mAdapter;
    private PackageManager mPm;
    public  static PieChart pieChart;
    public static  String appTop="",appTop2="";
    public static  String Pkg="",Pkg1="";
    public  static float timeapp=0,timeapp2=0;
    public static  float Game=0;
    public static  float News=0;
    public static  float Social=0;
    public static  float undfiend=0;
    public  static float Productiv=0;
    public static  float total=0;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    DatabaseHelper myDb;
    Databaseadd db;
    public static int steps=0;
    public static ArrayList<String> arrayList_Games;
    public static ArrayList<String> arrayList_News;
    public static ArrayList<String> arrayList_Productive;
    public static ArrayList<String> arrayList_SocialMedia;
    public static  ArrayList<String> arrayList_Undifiend;

    ArrayList<Integer> colors =new ArrayList<>();
    ArrayList<String> name =new ArrayList<>();
    ArrayList<Statique> number=new ArrayList<>();
    BarChart barChart;

    public static class UsageTimeComparator implements Comparator<UsageStats> {
        @Override
        public final int compare(UsageStats a, UsageStats b) {
            return (int)(b.getTotalTimeInForeground() - a.getTotalTimeInForeground());

                                                        }
                                                            }

    // View Holder used when displaying views
    static class AppViewHolder {
        TextView pkgName;
        ImageView pkgicon;
        ProgressBar lastTimeUsed;
        TextView percentage;
        TextView usageTime;
    }


    public class Statique{
        public Float time;
        public String name;
        Statique(Float time,String name){
            this.name=name;
            this.time=time;
        }
    }

    class UsageStatsAdapter extends BaseAdapter {

        private HomeFragment.UsageTimeComparator mUsageTimeComparator = new HomeFragment.UsageTimeComparator();
        private final ArrayMap<String, String> mAppLabelMap = new ArrayMap<>();
        private final ArrayList<UsageStats> mPackageStats = new ArrayList<>();
        private final ArrayList<UsageStats> mPackageStats_copy = new ArrayList<>();

        UsageStatsAdapter() {

            Calendar cal = Calendar.getInstance();
            Long end=0l,start=0l;
            if(cal.get(Calendar.HOUR)>=1){
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MINUTE,1);
            cal.set(Calendar.HOUR_OF_DAY,1);
            start=cal.getTimeInMillis();

            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.SECOND,59);
            cal.set(Calendar.MINUTE,59);
            cal.set(Calendar.HOUR_OF_DAY,0);
            end=cal.getTimeInMillis();}
            else {
                cal.set(Calendar.SECOND,59);
                cal.set(Calendar.MINUTE,59);
                cal.set(Calendar.HOUR_OF_DAY,0);
                end=cal.getTimeInMillis();
                cal.add(Calendar.DATE, -1);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MINUTE,1);
                cal.set(Calendar.HOUR_OF_DAY,1);
                start=cal.getTimeInMillis();
            }

            final List<UsageStats> stats =
                    mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                            start,end);


            if (stats == null) {
                return ;
            }



            ArrayMap<String, UsageStats> map = new ArrayMap<>();
            final int statCount = stats.size();
            for (int i = 0; i < statCount; i++) {
                final UsageStats pkgStats = stats.get(i);


                try {

                    ApplicationInfo appInfo = mPm.getApplicationInfo(pkgStats.getPackageName(), 0);
                    String label = appInfo.loadLabel(mPm).toString();
                    int category = appInfo.category;
                        String label1=String.valueOf(category);
                    total=total+(pkgStats.getTotalTimeInForeground() / 1000);
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
            sortList();

            List<ApplicationInfo> aaplist=getContext().getPackageManager().getInstalledApplications(0);;
            ArrayList<String>  arrayList=Main_page.mydb.selectall();
            if(arrayList.size()!=0){
                Main_page.WhitelList_add.clear();
                for(int i=0;i<arrayList.size();i++) {
                    for (int j = 0; j < aaplist.size(); j++) {
                        if (arrayList.get(i).equals(aaplist.get(j).packageName)) {
                            Main_page.WhitelList_add.add(aaplist.get(j));

                        }
                    }
                }
            }

            if(Main_page.WhitelList_add.size()!=0){
                mPackageStats_copy.clear();
                for(int i=0;i<mPackageStats.size();i++){
                    for(int j=0;j<Main_page.WhitelList_add.size();j++){
                        if(Main_page.WhitelList_add.get(j).packageName.equals(mPackageStats.get(i).getPackageName())){
                            mPackageStats_copy.add(mPackageStats.get(i));
                        }
                    }
                }
//
            }else {
                ArrayList<String>  arrayLis=Main_page.mydb_remove.selectall();
                if(arrayLis.size()!=0){
                    Main_page.WhitelList_remove.clear();
                    for(int i=0;i<arrayLis.size();i++) {
                        for (int j = 0; j < aaplist.size(); j++) {
                            if (arrayLis.get(i).equals(aaplist.get(j).packageName)) {
                                Main_page.WhitelList_remove.add(aaplist.get(j));

                            }
                        }
                    }
                }

                if(Main_page.WhitelList_remove.size()!=0){

                    mPackageStats_copy.clear();
                    for (int j = 0; j < Main_page.WhitelList_remove.size(); j++) {
                     for (int i = 0; i < mPackageStats.size(); i++) {
                            if (!mPackageStats.get(i).getPackageName().equals(Main_page.WhitelList_remove.get(j).packageName))
                            {mPackageStats_copy.add(mPackageStats.get(i));}
                                    }
                                }
                    }
                else {
                    if(mPackageStats.size()!=0) {
                        if(mPackageStats.size()<3){
                            for (int i = 0; i < mPackageStats.size(); i++) {

                                mPackageStats_copy.add(mPackageStats.get(i));

                            }
                        }else {
                            for (int i = 0; i <= 2; i++) {

                                mPackageStats_copy.add(mPackageStats.get(i));

                            }
                        }
                    }
                }
            }
            if(mPackageStats.size()>=3) {
                if(mPackageStats.get(0).getPackageName().equals("com.example.time")){
                    appTop = mAppLabelMap.get(mPackageStats.get(1).getPackageName());
                    timeapp = mPackageStats.get(1).getTotalTimeInForeground();
                    Pkg = mPackageStats.get(1).getPackageName();

                    if(mPackageStats.get(2).getPackageName().equals("com.example.time")){
                        appTop2 = mAppLabelMap.get(mPackageStats.get(3).getPackageName());
                        timeapp2 = mPackageStats.get(3).getTotalTimeInForeground();
                        Pkg1 = mPackageStats.get(3).getPackageName();
                    }else{
                        appTop2 = mAppLabelMap.get(mPackageStats.get(2).getPackageName());
                        timeapp2 = mPackageStats.get(2).getTotalTimeInForeground();
                        Pkg1 = mPackageStats.get(2).getPackageName();
                    }
                }else {
                    appTop = mAppLabelMap.get(mPackageStats.get(0).getPackageName());
                    timeapp = mPackageStats.get(0).getTotalTimeInForeground();
                    Pkg = mPackageStats.get(0).getPackageName();
                    if(mPackageStats.get(1).getPackageName().equals("com.example.time")){
                        appTop2 = mAppLabelMap.get(mPackageStats.get(2).getPackageName());
                        timeapp2 = mPackageStats.get(2).getTotalTimeInForeground();
                        Pkg1 = mPackageStats.get(2).getPackageName();
                    }else{
                        appTop2 = mAppLabelMap.get(mPackageStats.get(1).getPackageName());
                        timeapp2 = mPackageStats.get(1).getTotalTimeInForeground();
                        Pkg1 = mPackageStats.get(1).getPackageName();
                    }
                }
            }


        }




        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            HomeFragment.AppViewHolder holder;

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.usage_stats_item, null);
                holder = new HomeFragment.AppViewHolder();
                holder.pkgName = (TextView) convertView.findViewById(R.id.package_name);
                holder.pkgicon = (ImageView) convertView.findViewById(R.id.package_icon);
                holder.lastTimeUsed = (ProgressBar) convertView.findViewById(R.id.last_time_used);
                holder.usageTime = (TextView) convertView.findViewById(R.id.usage_time);
                holder.percentage = (TextView) convertView.findViewById(R.id.perc);
                convertView.setTag(holder);


            } else {

                holder = (HomeFragment.AppViewHolder) convertView.getTag();

            }


            // Bind the data efficiently with the holder
            UsageStats pkgStats = mPackageStats_copy.get(position);
            if (pkgStats != null) {
                Drawable img = null;
                int category=-1;
                float time = 0;
                float perc = 0;
                float time_spend = 0;
                int minutes = 0;
                int seconds = 0;
                int hours = 0;
                try {
                    String label1 = mAppLabelMap.get(pkgStats.getPackageName());
                    category=mPm.getApplicationInfo(pkgStats.getPackageName(),0).category;
                    img = mPm.getApplicationIcon(pkgStats.getPackageName());
                    img.setBounds(0, 0, 50, 50);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.pkgicon.setImageDrawable(img);
//----------------------------------------------------------------------------------------------------------------
                for(int i=0;i<arrayList_SocialMedia.size();i++){
                    if(pkgStats.getPackageName().equals(arrayList_SocialMedia.get(i)) ){
                        // Social = Social + pkgStats.getTotalTimeInForeground();
                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                                Color.rgb(2, 119, 189), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(2, 119, 189));
                        holder.pkgName.setTextColor(Color.rgb(2, 119, 189));
                        holder.usageTime.setTextColor(Color.rgb(2, 119, 189));

                    }
                }

                for(int i=0;i<arrayList_Productive.size();i++){
                    if(pkgStats.getPackageName().equals(arrayList_Productive.get(i)) ){
                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                                Color.rgb(212, 80, 135), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(212, 80, 135));
                        holder.pkgName.setTextColor(Color.rgb(212, 80, 135));
                        holder.usageTime.setTextColor(Color.rgb(212, 80, 135));
                    }
                }

                for(int i=0;i<arrayList_Games.size();i++){
                    if(pkgStats.getPackageName().equals(arrayList_Games.get(i))){

                        //   Image = Image + pkgStats.getTotalTimeInForeground();
                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                        Color.rgb(134, 110, 74), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(134, 110, 74));
                        holder.pkgName.setTextColor(Color.rgb(134, 110, 74));
                        holder.usageTime.setTextColor(Color.rgb(134, 110, 74));
                    }
                }


                for(int i=0;i<arrayList_News.size();i++){
                    if(arrayList_News.get(i).equals(pkgStats.getPackageName())){
                        // News = News + pkgStats.getTotalTimeInForeground();
                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                        Color.rgb(249, 56, 34), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(249, 56, 34));
                        holder.pkgName.setTextColor(Color.rgb(249, 56, 34));
                        holder.usageTime.setTextColor(Color.rgb(249, 56, 34));

                    }
                }


                for(int i=0;i<arrayList_Undifiend.size();i++){
                    if(pkgStats.getPackageName().equals(arrayList_Undifiend.get(i))){

                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                                Color.rgb(253,210,14), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(253,210,14));
                        holder.pkgName.setTextColor(Color.rgb(253,210,14));
                        holder.usageTime.setTextColor(Color.rgb(253,210,14));


                    }
                }



//----------------------------------------------------------------------------------------------------------------


                holder.pkgName.setText(mAppLabelMap.get(pkgStats.getPackageName()));
                time = (pkgStats.getTotalTimeInForeground() / 1000);
                perc = (time * 100) / total;

                if ((int) perc == 0) {
                    holder.percentage.setText("1%");
                    holder.lastTimeUsed.setProgress(1);
                } else {
                    holder.percentage.setText((int) perc + "%");
                    holder.lastTimeUsed.setProgress((int) perc);
                }

                System.out.println("sdsdsdsd ");
            } else {
                Log.w(TAG, "No usage stats info for package:" + position);
            }
            return convertView;
        }


        public void GetTotalTime(){
            for(int i=0;i<mPackageStats.size();i++){
                UsageStats pkgStats = mPackageStats.get(i);
                for (int k=0;k<arrayList_Games.size();k++){
                    if(pkgStats.getPackageName().equals(arrayList_Games.get(k))){
                        Game=Game+pkgStats.getTotalTimeInForeground();
                    }
                }

                for (int k=0;k<arrayList_News.size();k++){
                    if(pkgStats.getPackageName().equals(arrayList_News.get(k))){
                        News=News+pkgStats.getTotalTimeInForeground();
                    }
                }

                for (int k=0;k<arrayList_Productive.size();k++){
                    if(pkgStats.getPackageName().equals(arrayList_Productive.get(k))){
                        Productiv=Productiv+pkgStats.getTotalTimeInForeground();
                    }
                }


                for (int k=0;k<arrayList_SocialMedia.size();k++){
                    if(pkgStats.getPackageName().equals(arrayList_SocialMedia.get(k))){
                        Social=Social+pkgStats.getTotalTimeInForeground();
                    }
                }


                for (int k=0;k<arrayList_Undifiend.size();k++){
                    if(pkgStats.getPackageName().equals(arrayList_Undifiend.get(k))){
                        undfiend=undfiend+pkgStats.getTotalTimeInForeground();
                    }
                }

            }

        }

        @Override
        public int getCount() {
            return mPackageStats_copy.size();
        }

        @Override
        public Object getItem(int position) {
            return mPackageStats_copy.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        private void sortList() {

            Collections.sort(mPackageStats, mUsageTimeComparator);
            notifyDataSetChanged();
        }
    }

    private int Compare(Float a,Float b ){
        return a.compareTo(b);
    }


    @Override
    public void onStart() {
        super.onStart();
        mApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_OAUTH) {
            authInProgress = false;
            if (resultCode == RESULT_OK) {
                if (!mApiClient.isConnecting() && !mApiClient.isConnected()) {
                    mApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.e("GoogleFit", "RESULT_CANCELED");
            }
        } else {
            Log.e("GoogleFit", "requestCode NOT request_oauth");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public  void  count(DataSet dataset){
        for(DataPoint dp : dataset.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                Value value = dp.getValue(field);
                steps=value.asInt();
                String ch =String.valueOf(steps);
                st="";
                int end=0;
                if(ch.length()>=4) {
                    for (int i = 0; i < ch.length() - 3; i++) {
                        st = st + ch.charAt(i);
                        end++;
                    }
                    st = st + ",";
                    st = st + ch.charAt(end) + "" + ch.charAt(end + 1) + "" + ch.charAt(end + 2);

                }else{
                    st=ch;
                }
                int totalt=Integer.valueOf(ch);
                pers=(int)totalt*100/10000;
                kilometre=new DecimalFormat("0.00").format((totalt*0.762)/1000);

            }

        }
    }
    public void dumpDataset(DataSet dataset){

        for(DataPoint dp : dataset.getDataPoints()){
            for(Field field : dp.getDataType().getFields()){
                Value value=dp.getValue(field);
                String fieldname=field.getName();
                calories=value.asFloat();
                int values=(int)calories;
                temp="";
                String test=String.valueOf(values);
                int end=0;
                if(test.length()>=4) {
                    for(int i=0;i<test.length()-3;i++){
                        temp = temp + "" + test.charAt(i);
                        end++;
                    }
                    temp=temp+","+test.charAt(end)+test.charAt(end+1)+test.charAt(end+2)+" ";
                }else{
                    temp=temp+""+test;
                }
                temp=temp+"";
            }
        }

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }







    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStop() {
        super.onStop();

        Fitness.SensorsApi.remove( mApiClient, this )
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            mApiClient.disconnect();
                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if( !authInProgress ) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult( getActivity(), REQUEST_OAUTH );
            } catch(IntentSender.SendIntentException e ) {

            }
        } else {
            Log.e( "GoogleFit", "authInProgress" );
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        getActivity().startService(new Intent(getContext(), Service_Block.class));
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        listView=root.findViewById(R.id.pkg_list);
        CardView view=root.findViewById(R.id.View1);
        CardView statics=root.findViewById(R.id.statics);
        Social=0;
        Game=0;
        News=0;
        undfiend=0;
        Productiv=0;
        total=0;
        km=root.findViewById(R.id.km);
        calo=root.findViewById(R.id.kcal);
        pieChart=root.findViewById(R.id.stepscount);

        mApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();




////////// notification daily----------------------------------------------------------------
        //        Notification();
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,49);
        cal.set(Calendar.SECOND,30);
        Intent intent=new Intent(getActivity(),Myservice.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getActivity(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
//-------------------------------------------------------------------------------------


        mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPm = getActivity().getPackageManager();


        myDb=new DatabaseHelper(getContext());
        db=new Databaseadd(getContext());
        arrayList_News=myDb.select(5);
        arrayList_SocialMedia=myDb.select(4);
        arrayList_Productive=myDb.select(7);
        arrayList_Games=myDb.select(1);
        arrayList_Undifiend=myDb.select(-1);

        boolean isEmpty=myDb.isEmpty();
        int result=0;
        List<ApplicationInfo> asplist=new ArrayList<>();
        asplist=getContext().getPackageManager().getInstalledApplications(0);
        //*************************************************************************************************************
        //if data nom emty
        if(isEmpty==true){
            ArrayList<String> arrayList=myDb.select(1);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=1;
                    }
                }

            }
            arrayList=myDb.select(5);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=5;
                    }
                }
            }

            arrayList=myDb.select(7);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=7;
                    }
                }
            }

            arrayList=myDb.select(4);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=4;
                    }
                }
            }
            arrayList=myDb.select(-1);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=-1;
                    }
                }
            }


        }

//***************************************************************************************************************
// add application in arraylist (gane,news,productive,media,undifiend)
//--------------------------------------------------------------------------------------------------------------

        for (int j = 0; j < asplist.size(); j++) {

            if (asplist.get(j).category == 0 || asplist.get(j).category == 1 || asplist.get(j).category == 2 || asplist.get(j).category == 3) {
                for (int l = 0; l < arrayList_Games.size(); l++) {
                    if (arrayList_Games.get(l).equals(asplist.get(j).packageName)) {
                        result = 1;
                    }
                }
                if (result == 0) {
                    arrayList_Games.add(asplist.get(j).packageName);
                } else {
                    result = 0;
                }
            }
            if (asplist.get(j).category == 5 || asplist.get(j).category == 6) {
                for (int l = 0; l < arrayList_News.size(); l++) {
                    if (arrayList_News.get(l).equals(asplist.get(j).packageName)) {
                        result = 1;
                    }
                }
                if (result == 0) {
                    arrayList_News.add(asplist.get(j).packageName);
                } else {
                    result = 0;
                }


            }
            if (asplist.get(j).category == 7) {

                for (int l = 0; l < arrayList_Productive.size(); l++) {
                    if (arrayList_Productive.get(l).equals(asplist.get(j).packageName)) {
                        result = 1;
                    }
                }
                if (result == 0) {
                    arrayList_Productive.add(asplist.get(j).packageName);
                } else {
                    result = 0;
                }


            }
            if (asplist.get(j).category == 4) {

                for (int l = 0; l < arrayList_SocialMedia.size(); l++) {
                    if (arrayList_SocialMedia.get(l).equals(asplist.get(j).packageName)) {
                        result = 1;
                    }
                }
                if (result == 0) {
                    arrayList_SocialMedia.add(asplist.get(j).packageName);
                } else {
                    result = 0;
                }


            }
            if (asplist.get(j).category == -1) {
                for (int l = 0; l < arrayList_Undifiend.size(); l++) {
                    if (arrayList_Undifiend.get(l).equals(asplist.get(j).packageName)) {
                        result = 1;
                    }
                }
                if (result == 0) {
                    arrayList_Undifiend.add(asplist.get(j).packageName);
                } else {
                    result = 0;
                }


            }

        }

//----------------------------------------------------------------------------------------------------------------
        mAdapter = new HomeFragment.UsageStatsAdapter();
        listView.setAdapter(mAdapter);




        mAdapter.GetTotalTime();

        float time_social = (Social/ 1000);
        float perc_social = (time_social * 100) / total;

        float time_productive = (Productiv/ 1000);
        float perc_productive = (time_productive * 100) / total;

        float time_news = (News/ 1000);
        float perc_news = (time_news * 100) / total;


        float time_game = (Game/ 1000);
        float perc_game = (time_game * 100) / total;


        float time_undifend = (undfiend/ 1000);
        float perc_undifend = (time_undifend * 100) / total;
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
         colors =new ArrayList<>();
         name =new ArrayList<>();
         number=new ArrayList<>();

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
///-------------------------- click on cart view -------------------------------------------------------------
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), Usage_Today.class));
            }
        });





//-------------------------create barchart -----------------------------------------------------------------
        barChart=(BarChart)root.findViewById(R.id.barchart);
        barChart.setFitBars(true);
        barChart.setDescription(null);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        barChart.setScaleEnabled(false);
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
        Legend legend =barChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(9);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(10);
        LegendEntry [] legendEntries= new LegendEntry[5];
        for(int i =0;i<legendEntries.length;i++){
            LegendEntry entry=new LegendEntry();
            entry.formColor=colors.get(i);
            entry.label=name.get(i);
            legendEntries[i]=entry;
        }
        legend.setCustom(legendEntries);
//----------------------------------------------------------------------------------------------------------


//---------------------------------------------------------------------------------------------------------
        barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               v.getContext().startActivity(new Intent(v.getContext(), Usage_statics.class));


            }
        });
 //-------------------------------------------------------------------------------------------------------------
 //--------------- pie chart -------------------------------------------------------------------------------

        Fitness.getHistoryClient(getActivity(),GoogleSignIn.getLastSignedInAccount(getActivity()))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        count(dataSet);
                        chartpie();

                    }
                });
       Fitness.getHistoryClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        dumpDataset(dataSet);
                        chartpie();

                    }
                });

//---------------------------------------------------------------------------------------------------------------




        return root;

    }


    public void chartpie(){

    ArrayList values=new ArrayList<>();
    values.add(new PieEntry(pers,0));
    values.add(new PieEntry((100 - pers),1));
   PieDataSet pieDataSet = new PieDataSet(values, "");
    PieData data = new PieData(pieDataSet);
    pieChart.setData(data);
    pieDataSet.setColors(Color.rgb(247, 114, 39), Color.rgb(235, 235, 235));
    pieChart.setCenterText(""+st+ "\nSteps");
    calo.setText(" " + temp + " Cal");
    km.setText(" " + kilometre + " Km");
    pieChart.getData().getDataSet().setDrawValues(false);
    pieChart.setTouchEnabled(false);
    pieChart.setRotationEnabled(false);
    pieChart.getData().getDataSet().setDrawValues(false);
    pieChart.getDescription().setEnabled(false);
    pieChart.setDrawEntryLabels(false);
    pieChart.setDrawMarkers(false);
    pieChart.getLegend().setDrawInside(false);
    pieChart.getLegend().setWordWrapEnabled(false);
    pieChart.setClickable(false);
    pieChart.setHoleRadius(79);
    pieChart.setTransparentCircleAlpha(20);
    pieChart.setCenterTextSize(22f);
    pieChart.setCenterTextColor(Color.rgb(115, 115, 115));
    pieChart.setUsePercentValues(false);
    pieChart.setSelected(false);
    Legend l = pieChart.getLegend();
    l.setEnabled(false);
    pieChart.animateXY(6000, 6000);

}




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onDataPoint(DataPoint dataPoint) {
        for (final Field field : dataPoint.getDataType().getFields()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

        }
    }



}