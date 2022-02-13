package com.example.time;

import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.ConfigurationStats;
import android.app.usage.EventStats;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.VolumeShaper;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time.ui.home.HomeFragment;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Usage_Today extends AppCompatActivity implements AdapterView.OnItemSelectedListener{



    public List<ApplicationInfo> aaplist=null;
    private ListView listView;
    private static final String TAG = "UsageStatsActivity";
    private UsageStatsManager mUsageStatsManager;
    private LayoutInflater mInflater;
    private Usage_Today.UsageStatsAdapter mAdapter;
    private PackageManager mPm;
    float total=0;
    float RemoveTime=0;
    float TimeAdd=0;
    int month_instance=0;
    SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
    Button left,riht;
    TextView title_date;
    int day;
    int week;
    int month;
    int type=0;
    DateTimeFormatter dtf;
    LocalDateTime now;
    CheckBox daily,weekly,monthly;
    List<UsageStats> stats,tempstats;
    Calendar cal ;

    Calendar cal_month;
    TextView Toltal;

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
        TextView date;

    }




    public class UsageStatsAdapter extends BaseAdapter {

        public   float  Toltal_days=0;


        private Usage_Today.UsageTimeComparator mUsageTimeComparator = new Usage_Today.UsageTimeComparator();
        private final ArrayMap<String, String> mAppLabelMap = new ArrayMap<>();
        private final ArrayList<UsageStats> mPackageStats = new ArrayList<>();
        private final ArrayList<UsageStats> mPackageStatss = new ArrayList<>();


        public UsageStatsAdapter(Date start ,Date end,String type) throws ParseException {

            if(type.equals("daily")) {

            stats =
                    mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                            start.getTime(), end.getTime());


            Toast.makeText(Usage_Today.this,"Begin Time "+df.format(start.getTime()),Toast.LENGTH_LONG).show();

        }
            if(type.equals("week")) {

                stats =
                        mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                                start.getTime(), end.getTime());
                Toast.makeText(Usage_Today.this,"Begin Time "+df.format(stats.get(0).getFirstTimeStamp()),Toast.LENGTH_LONG).show();


            }

        else if(type.equals("monthly")){

            stats =
                    mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY,//because monthly get fault number
                            start.getTime(),end.getTime());
                Toast.makeText(Usage_Today.this,"Begin Time "+df.format(stats.get(1).getFirstTimeStamp()),Toast.LENGTH_LONG).show();



            }


            Toltal_days=0;
            ArrayMap<String, UsageStats> map = new ArrayMap<>();
            final int statCount = stats.size();
            if(statCount!=0) {
                for (int i = 0; i < statCount; i++) {
                    final UsageStats pkgStats = stats.get(i);
//                if(type.equals("monthly")){
//                    Toltal_days = Toltal_days + pkgStats.getTotalTimeInForeground() / 10000;
//                }else {
                    Toltal_days = Toltal_days + pkgStats.getTotalTimeInForeground() / 1000;
//                }
                    try {

                        ApplicationInfo appInfo = mPm.getApplicationInfo(pkgStats.getPackageName(), 0);
                        String label = appInfo.loadLabel(mPm).toString();
                        total = total + (pkgStats.getTotalTimeInForeground() / 1000);
                        if (mAppLabelMap.equals(pkgStats.getPackageName())) {


                        } else {
                            mAppLabelMap.put(pkgStats.getPackageName(), label);
                        }

                        UsageStats existingStats =
                                map.get(pkgStats.getPackageName());
                        if (existingStats == null) {

                            if (pkgStats.getTotalTimeInForeground() != 0) {

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
                if (Main_page.WhitelList_remove.size() != 0) {
                    for (int i = 0; i < mPackageStats.size(); i++) {
                        for (int j = 0; j < Main_page.WhitelList_remove.size(); j++) {
                            if (Main_page.WhitelList_remove.get(j).packageName.equals(mPackageStats.get(i).getPackageName())) {
//                            if(type.equals("monthly")){
//                                RemoveTime = RemoveTime + (mPackageStats.get(i).getTotalTimeInForeground() / 10000);
//
//                            }else {
                                RemoveTime = RemoveTime + (mPackageStats.get(i).getTotalTimeInForeground() / 1000);
//                            }
                                mPackageStats.remove(i);
                            }
                        }
                    }
                } else if (Main_page.WhitelList_add.size() != 0) {
                    if (Main_page.WhitelList_add.size() != 0) {
                        for (int i = 0; i < mPackageStats.size(); i++) {
                            for (int j = 0; j < Main_page.WhitelList_add.size(); j++) {
                                if (Main_page.WhitelList_add.get(j).packageName.equals(mPackageStats.get(i).getPackageName())) {
                                    mPackageStatss.add(mPackageStats.get(i));
//                                if(type.equals("monthly")){
//                                    TimeAdd = TimeAdd + (mPackageStats.get(i).getTotalTimeInForeground() / 10000);
//                                }else {
                                    TimeAdd = TimeAdd + (mPackageStats.get(i).getTotalTimeInForeground() / 1000);
//                                }
                                }
                            }
                        }
                        mPackageStats.clear();
                        mPackageStats.addAll(mPackageStatss);
                    }
                }
                sortList();


            }

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
        public View getView(int position, View convertView, ViewGroup parent) {

            Usage_Today.AppViewHolder holder;


            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.usage_stats_item, null);
                holder = new Usage_Today.AppViewHolder();
                holder.pkgName = (TextView) convertView.findViewById(R.id.package_name);
                holder.pkgicon = (ImageView) convertView.findViewById(R.id.package_icon);
                holder.lastTimeUsed = (ProgressBar) convertView.findViewById(R.id.last_time_used);
                holder.usageTime = (TextView) convertView.findViewById(R.id.usage_time);
                holder.percentage = (TextView) convertView.findViewById(R.id.perc);
                holder.date=(TextView)convertView.findViewById(R.id.date);
                convertView.setTag(holder);


            } else {

                holder = (Usage_Today.AppViewHolder) convertView.getTag();

            }


            // Bind the data efficiently with the holder
            UsageStats pkgStats = mPackageStats.get(position);
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

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime now = LocalDateTime.now();
                if(dtf.format(now).compareTo(df.format(pkgStats.getLastTimeUsed()))==0){
                    SimpleDateFormat f=new SimpleDateFormat("hh:mm a ");
                    holder.date.setText("Today : "+ f.format(pkgStats.getLastTimeUsed()));
                }else if(dtf.format(now.minusDays(1)).compareTo(df.format(pkgStats.getLastTimeUsed()))==0){
                    SimpleDateFormat f=new SimpleDateFormat("hh:mm a");
                    holder.date.setText("Yesturday : "+ f.format(pkgStats.getLastTimeUsed()));
                }
                else{
                    SimpleDateFormat f=new SimpleDateFormat(" E dd/MM/yyyy hh:mm a");
                    holder.date.setText("Date : "+ f.format(pkgStats.getLastTimeUsed()));
                }
                holder.pkgicon.setImageDrawable(img);
                for(int i=0;i< HomeFragment.arrayList_SocialMedia.size();i++){
                    if(pkgStats.getPackageName().equals(HomeFragment.arrayList_SocialMedia.get(i)) ){
                        // Social = Social + pkgStats.getTotalTimeInForeground();
                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                                Color.rgb(2, 119, 189), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(2, 119, 189));
                        holder.pkgName.setTextColor(Color.rgb(2, 119, 189));
                        holder.usageTime.setTextColor(Color.rgb(2, 119, 189));

                    }
                }

                for(int i=0;i<HomeFragment.arrayList_Productive.size();i++){
                    if(pkgStats.getPackageName().equals(HomeFragment.arrayList_Productive.get(i)) ){
                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                                Color.rgb(212, 80, 135), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(212, 80, 135));
                        holder.pkgName.setTextColor(Color.rgb(212, 80, 135));
                        holder.usageTime.setTextColor(Color.rgb(212, 80, 135));
                    }
                }

                for(int i=0;i<HomeFragment.arrayList_Games.size();i++){
                    if(pkgStats.getPackageName().equals(HomeFragment.arrayList_Games.get(i))){
                        //   Image = Image + pkgStats.getTotalTimeInForeground();
                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                        Color.rgb(134,110,74), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(134,110,74));
                        holder.pkgName.setTextColor(Color.rgb(134,110,74));
                        holder.usageTime.setTextColor(Color.rgb(134, 110, 74));
                    }
                }


                for(int i=0;i<HomeFragment.arrayList_News.size();i++){
                    if(HomeFragment.arrayList_News.get(i).equals(pkgStats.getPackageName())){
                        // News = News + pkgStats.getTotalTimeInForeground();
                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                                Color.rgb(249, 46, 34), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(249, 46, 34));
                        holder.pkgName.setTextColor(Color.rgb(249, 46, 34));
                        holder.usageTime.setTextColor(Color.rgb(249, 46, 34));

                    }
                }


                for(int i=0;i<HomeFragment.arrayList_Undifiend.size();i++){
                    if(pkgStats.getPackageName().equals(HomeFragment.arrayList_Undifiend.get(i))){

                        holder.lastTimeUsed.getProgressDrawable().setColorFilter(
                                Color.rgb(253,210,14), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.percentage.setTextColor(Color.rgb(253,210,14));
                        holder.pkgName.setTextColor(Color.rgb(253,210,14));
                        holder.usageTime.setTextColor(Color.rgb(253,210,14));


                    }
                }



                holder.pkgName.setText(mAppLabelMap.get(pkgStats.getPackageName()));
                time = (pkgStats.getTotalTimeInForeground() / 1000);
//`````````````````````````````````````````````````````````````````````````````````````````````````````````````
//`````````````````````````````````````````````````````````````````````````````````````````````````````````````
                perc = (time * 100) / total;
                if ((int) perc == 0) {
                    holder.percentage.setText("1%");
                    holder.lastTimeUsed.setProgress(1);
                } else {
                    holder.percentage.setText((int) perc + "%");
                    holder.lastTimeUsed.setProgress((int) perc);
                }
                time_spend = (pkgStats.getTotalTimeInForeground())/1000;
                minutes = (int) ((time_spend / (60)) % 60);
                seconds = (int) (time_spend ) % 60;
                hours = (int) ((time_spend / (60 * 60)) );
                if (hours == 0) {
                    if (minutes == 0) {
                        if (seconds == 0) {
                            holder.usageTime.setText(" 1 s");
                        } else {
                            holder.usageTime.setText(seconds + " s");
                        }
                    } else {
                        if (seconds == 0) {
                            holder.usageTime.setText(minutes + " m ");
                        } else {
                            holder.usageTime.setText(minutes + " m " + seconds + " s");
                        }
                    }
                } else {
                    if (minutes == 0) {
                        if (seconds == 0) {
                            holder.usageTime.setText(hours + " h ");
                        } else {
                            holder.usageTime.setText(hours + " H " + seconds + " s");
                        }
                    } else {
                        if (seconds == 0) {
                            holder.usageTime.setText(hours + " h " + minutes + " m ");
                        } else {
                            holder.usageTime.setText(hours + " h " + minutes + " m ");
                        }
                    }
                }
            } else {
                Log.w(TAG, "No usage stats info for package:" + position);
            }

            return convertView;
        }



        @Override
        public int getCount() {
            return mPackageStats.size();
        }

        @Override
        public Object getItem(int position) {
            return mPackageStats.get(position);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        setContentView(R.layout.activity_usage__today);
        listView=findViewById(R.id.pkg_list);
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Toltal=findViewById(R.id.views);
        mPm = getPackageManager();
         day=0;
         week=0;
        left=findViewById(R.id.left);
        riht=findViewById(R.id.right);
        title_date=findViewById(R.id.date);
         dtf = DateTimeFormatter.ofPattern("dd MMMM  yyyy");
         now = LocalDateTime.now();
        title_date.setText(dtf.format(now));

        cal = Calendar.getInstance();
        daily=findViewById(R.id.daily);
        weekly=findViewById(R.id.weekly);
        monthly=findViewById(R.id.monthly);
        type=0;
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily.setChecked(true);
                weekly.setChecked(false);
                monthly.setChecked(false);
                title_date.setText(dtf.format(now));
                day = 0;
                type=0;

                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stats.clear();
                        day--;
                        title_date.setText(dtf.format(now.minusDays(-day)));
                        try {
                            Date date1=new SimpleDateFormat("dd MMMM yyyy").parse(title_date.getText().toString());
                            String date=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                            Date st=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                            Date ed=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                            st.setHours(4);
                            st.setMinutes(10);
                            st.setSeconds(0);
                            ed.setHours(23);
                            ed.setMinutes(59);
                            ed.setSeconds(59);


                            if(day>-8 && day<=0) {
                                if(day==0){
                                    st.setHours(1);
                                    st.setMinutes(1);
                                    ed.setHours(0);
                                    ed.setMinutes(59);
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(ed);
                                    c.add(Calendar.DATE, 1);
                                    ed = c.getTime();
                                    total=0;
                                    mAdapter = new UsageStatsAdapter(st, ed, "daily");
                                    if(mAdapter!=null) {
                                        listView.setAdapter(mAdapter);
                                    }
                                }else {

                                    mAdapter = new UsageStatsAdapter(st, ed, "daily");
                                    if(mAdapter!=null) {
                                        listView.setAdapter(mAdapter);
                                    }
                                }

                                if(TimeAdd!=0){
                                    Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                                    TimeAdd=0;
                                }else if(RemoveTime!=0){
                                    mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                                    Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                                    RemoveTime=0;
                                }else {
                                    Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                                }
                                }else {
                                listView.setAdapter(null);
                                Toltal.setText("No data for the day ");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });

                riht.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stats.clear();
                        day++;
                        title_date.setText(dtf.format(now.plusDays(day)));
                        try {
                            Date date1=new SimpleDateFormat("dd MMMM yyyy").parse(title_date.getText().toString());
                            String date=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                            Date st=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                            Date ed=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                            st.setHours(4);
                            st.setMinutes(10);
                            st.setSeconds(0);
                            ed.setHours(23);
                            ed.setMinutes(59);
                            ed.setSeconds(59);

                            if(day>-8 && day<=0) {
                                if(day==0){
                                    st.setHours(1);
                                    st.setMinutes(1);
                                    ed.setHours(0);
                                    ed.setMinutes(59);
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(ed);
                                    c.add(Calendar.DATE, 1);
                                    ed = c.getTime();
                                    total=0;
                                    mAdapter = new UsageStatsAdapter(st, ed, "daily");
                                    if(mAdapter!=null) {
                                        listView.setAdapter(mAdapter);
                                    }
                                }else {
                                    mAdapter = new UsageStatsAdapter(st, ed, "daily");
                                    if(mAdapter!=null) {
                                        listView.setAdapter(mAdapter);
                                    }
                                     }

                                if(TimeAdd!=0){
                                    Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                                    TimeAdd=0;
                                }else if(RemoveTime!=0){
                                    mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                                    Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                                    RemoveTime=0;
                                }else {
                                    Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                                }
                            }else {
                                listView.setAdapter(null);
                                Toltal.setText("No data for the day ");
                            }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    }
                });
                try {
                    day=0;
                    Date date1=new SimpleDateFormat("dd MMMM yyyy").parse(title_date.getText().toString());
                    String date=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                    Date st=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                    Date ed=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                    stats.clear();
                    st.setHours(1);
                    st.setMinutes(1);
                    ed.setHours(0);
                    ed.setMinutes(59);
                    Calendar c = Calendar.getInstance();
                    c.setTime(ed);
                    c.add(Calendar.DATE, 1);
                    ed = c.getTime();
                    total=0;
                    mAdapter = new UsageStatsAdapter(st, ed,"daily");
                    if(TimeAdd!=0){
                        Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                        TimeAdd=0;
                    }else if(RemoveTime!=0){
                        mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                        Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                        RemoveTime=0;
                    }else {
                        Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(mAdapter!=null) {
                    listView.setAdapter(mAdapter);
                }

            }
        });


        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stats.clear();
                daily.setChecked(false);
                weekly.setChecked(true);
                monthly.setChecked(false);
                //type=1;
                week=0;
                // get today and clear time of day

                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);

                // get start of this week in milliseconds
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                long st=cal.getTimeInMillis();
                SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM ");
                String date1 = format1.format(cal.getTime());
                Date d1=cal.getTime();
                //get end of this week in milliseconds
                cal.add(Calendar.DATE,6);
                String date2 = format1.format(cal.getTime());
                title_date.setTextSize(15);
                title_date.setPadding(8,25,0,0);
                title_date.setText(date1+" to "+date2);
                Date d2=cal.getTime();
                try {
                    stats.clear();
                    d1.setHours(1);
                    d1.setMinutes(2);
                    d1.setSeconds(0);
                    d2.setHours(24);
                    d2.setMinutes(0);
                    d2.setSeconds(0);
                    total=0;
                    mAdapter = new UsageStatsAdapter(d1,d2,"week");
                    if(TimeAdd!=0){
                        Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                        TimeAdd=0;
                    }else if(RemoveTime!=0){
                        mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                        Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                        RemoveTime=0;
                    }else {
                        Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(mAdapter!=null) {
                    listView.setAdapter(mAdapter);
                }

                    left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            week=week++;
                            // get today and clear time of day
                            cal.add(Calendar.DATE,-7);
                            // get start of this week in milliseconds
                            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                            long st=cal.getTimeInMillis();
                            SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM ");
                            String date1 = format1.format(cal.getTime());
                            Date d1=cal.getTime();

                            //get end of this week in milliseconds
                            cal.add(Calendar.DATE,6);
                            String date2 = format1.format(cal.getTime());
                            title_date.setTextSize(15);
                            title_date.setPadding(8,25,0,0);
                            title_date.setText(date1+" to "+date2);
                            long ed=cal.getTimeInMillis();
                            Date d2=cal.getTime();
                            d1.setHours(1);
                            d1.setMinutes(2);
                            d1.setSeconds(0);
                            d2.setHours(24);
                            d2.setMinutes(0);
                            d2.setSeconds(0);

                            try {
                    week++;

                            if(week==0) {
                                stats.clear();
                                total=0;
                                mAdapter = new UsageStatsAdapter(d1,d2,"week");
                                if(mAdapter!=null) {
                                    listView.setAdapter(mAdapter);
                                }
                                if(TimeAdd!=0){
                                    Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                                    TimeAdd=0;
                                }else if(RemoveTime!=0){
                                    mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                                    Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                                    RemoveTime=0;
                                }else {
                                    Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                                }


                            }else {
                                listView.setAdapter(null);
                                Toltal.setText("No data for the day ");
                            }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    riht.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            week=week-1;
                            // get today and clear time of day

                            cal.add(Calendar.DATE,1);
                            // get start of this week in milliseconds
                            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                            long st=cal.getTimeInMillis();
                            SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM ");
                            String date1 = format1.format(cal.getTime());
                            Date d1=cal.getTime();

                            //get end of this week in milliseconds
                            cal.add(Calendar.DATE,6);
                            String date2 = format1.format(cal.getTime());
                            title_date.setTextSize(15);
                            title_date.setPadding(8,25,0,0);
                            title_date.setText(date1+" to "+date2);
                            long ed=cal.getTimeInMillis();
                            Date d2=cal.getTime();
                            d1.setHours(1);
                            d1.setMinutes(2);
                            d1.setSeconds(0);
                            d2.setHours(24);
                            d2.setMinutes(0);
                            d2.setSeconds(0);

                            try {
                                if(week==0) {
                                    stats.clear();
                                    total=0;
                                    mAdapter = new UsageStatsAdapter(d1,d2,"week");
                                    if(mAdapter!=null) {
                                        listView.setAdapter(mAdapter);
                                    }
                                    if(TimeAdd!=0){
                                        Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                                        TimeAdd=0;
                                    }else if(RemoveTime!=0){
                                        mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                                        Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                                        RemoveTime=0;
                                    }else {
                                        Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                                    }

                                }else {
                                    listView.setAdapter(null);
                                    Toltal.setText("No data for the day ");
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    });

            }
        });


        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily.setChecked(false);
                weekly.setChecked(false);
                monthly.setChecked(true);
                //type=2;
                 month=0;
                 month_instance=0;
                cal_month=Calendar.getInstance();
                cal_month.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal_month.clear(Calendar.MINUTE);
                cal_month.clear(Calendar.SECOND);
                cal_month.clear(Calendar.MILLISECOND);
                int  startmonth=cal.getActualMinimum(Calendar.DATE);
                final int endmonth=cal.getActualMaximum(Calendar.DATE);
                final SimpleDateFormat months  = new SimpleDateFormat("MMMM");
                SimpleDateFormat date_months  = new SimpleDateFormat("MM");
                SimpleDateFormat years  = new SimpleDateFormat("yyyy");
                title_date.setText(""+startmonth+" to "+endmonth+" -"+months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime()));
                title_date.setTextSize(15);
                title_date.setPadding(8,25,0,0);
//                left.setEnabled(false);
//                riht.setEnabled(false);
                try {
                    String date_first="0"+startmonth+"/"+date_months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime());
                    Date d1=df.parse(date_first);

                    String date_last=endmonth+"/"+date_months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime());
                    Date d2=df.parse(date_last);

                    stats.clear();
                    d1.setHours(1);
                    d1.setMinutes(1);
                    d2.setHours(23);
                    d2.setMinutes(59);
                    total=0;
                    mAdapter=new UsageStatsAdapter(d1,d2,"monthly");
                    listView.setAdapter(null);
                    if(mAdapter!=null) {
                        listView.setAdapter(mAdapter);
                    }
                    if(TimeAdd!=0){
                        Toltal.setText("Total Phone Usagess : "+Time(TimeAdd));
                        TimeAdd=0;
                    }else if(RemoveTime!=0){
                        mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                        Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                        RemoveTime=0;
                    }else {
                        Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                    }

                }catch (ParseException e){}


            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    month--;
                    month_instance--;
                    SimpleDateFormat years  = new SimpleDateFormat("yyyy");
                    cal_month.add(Calendar.MONTH, -1);

                    int  startmonth=cal_month.getActualMinimum(Calendar.DATE);
                    int endmonth=cal_month.getActualMaximum(Calendar.DATE);

                    SimpleDateFormat months  = new SimpleDateFormat("MMMM");
                    SimpleDateFormat date_months  = new SimpleDateFormat("MM");

                    title_date.setText(""+startmonth+" to "+endmonth+" -"+months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime()));
                    title_date.setTextSize(15);
                    title_date.setPadding(8,25,0,0);

                    try {
                        String date_first="0"+startmonth+"/"+date_months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime());
                        Date d1=df.parse(date_first);

                        String date_last=endmonth+"/"+date_months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime());
                        Date d2=df.parse(date_last);

                if(month_instance==0) {
                    stats.clear();
                    d1.setHours(1);
                    d1.setMinutes(1);
                    d2.setHours(24);
                    d2.setMinutes(0);
                    total=0;
                    mAdapter=new UsageStatsAdapter(d1,d2,"monthly");
                    listView.setAdapter(null);
                    if(mAdapter!=null) {
                        listView.setAdapter(mAdapter);
                    }
                    if(TimeAdd!=0){
                        Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                        TimeAdd=0;
                    }else if(RemoveTime!=0){
                        mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                        Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                        RemoveTime=0;
                    }else {
                        Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                    }
                }else {
                    listView.setAdapter(null);
                    Toltal.setText("No data for the day");
                                  }
                     //   listView.setAdapter(mAdapter);
                    }catch (ParseException e){}

                }
            });

            riht.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    month++;
                    month_instance++;
                    SimpleDateFormat years  = new SimpleDateFormat("yyyy");
                    cal_month.add(Calendar.MONTH,1);
                    int  startmonth=cal_month.getActualMinimum(Calendar.DATE);
                    int endmonth=cal_month.getActualMaximum(Calendar.DATE);

                    SimpleDateFormat months  = new SimpleDateFormat("MMMM");
                    SimpleDateFormat date_months  = new SimpleDateFormat("MM");
                    title_date.setText(""+startmonth+" to "+endmonth+" -"+months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime()));
                    title_date.setTextSize(15);
                    title_date.setPadding(8,25,0,0);


                    try {
                        String date_first="0"+startmonth+"/"+date_months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime());
                        Date d1=df.parse(date_first);

                        String date_last=endmonth+"/"+date_months.format(cal_month.getTime())+"/"+years.format(cal_month.getTime());
                        Date d2=df.parse(date_last);


                        if(month_instance==0) {
                            stats.clear();
                            d1.setHours(1);
                            d1.setMinutes(1);
                            d2.setHours(24);
                            d2.setMinutes(0);
                            total=0;
                            mAdapter=new UsageStatsAdapter(d1,d2,"monthly");
                            listView.setAdapter(null);
                            if(mAdapter!=null) {
                                listView.setAdapter(mAdapter);
                            }
                            if(TimeAdd!=0){
                                Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                                TimeAdd=0;
                            }else if(RemoveTime!=0){
                                mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                                Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                                RemoveTime=0;
                            }else {
                                Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                            }
                        }else {
                            listView.setAdapter(null);
                            Toltal.setText("No data for the day");
                        }
                    }catch (ParseException e){}


                }
            });


            }
        });

        if(daily.isChecked()){

            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    day--;
                    title_date.setText(dtf.format(now.minusDays(-day)));
                    try {
                        Date date1=new SimpleDateFormat("dd MMMM yyyy").parse(title_date.getText().toString());
                        String date=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                        Date st=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                        Date ed=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                        st.setHours(4);
                        st.setMinutes(0);
                        st.setSeconds(0);
                        ed.setHours(23);
                        ed.setMinutes(59);
                        ed.setSeconds(59);

                        stats.clear();


                        if(day>-8 && day<=0) {
                            if(day==0){
                                Calendar c = Calendar.getInstance();
                                st.setHours(1);
                                st.setMinutes(1);
                                ed.setHours(0);
                                ed.setMinutes(59);
                                c.setTime(ed);
                                c.add(Calendar.DATE, 1);
                                ed = c.getTime();
                                total=0;
                                mAdapter = new UsageStatsAdapter(st, ed, "daily");
                                if(mAdapter!=null) {
                                    listView.setAdapter(mAdapter);
                                }
                            }else {
                                mAdapter = new UsageStatsAdapter(st, ed, "daily");
                                if(mAdapter!=null) {
                                    listView.setAdapter(mAdapter);
                                }

                            }
                            if(TimeAdd!=0){
                                Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                                TimeAdd=0;
                            }else if(RemoveTime!=0){
                                mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                                Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                                RemoveTime=0;
                            }else {
                                Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                            }

                        }else {
                            listView.setAdapter(null);
                            Toltal.setText("No data for the day ");
                        }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                }
            });

            riht.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    day++;
                    title_date.setText(dtf.format(now.plusDays(day)));
                    try {
                        Date date1=new SimpleDateFormat("dd MMMM yyyy").parse(title_date.getText().toString());
                        String date=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                        Date st=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                        Date ed=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
//                        ed.setDate(ed.getDate()+1);
                        st.setHours(4);
                        st.setMinutes(0);
                        st.setSeconds(0);
                        ed.setHours(23);
                        ed.setMinutes(59);
                        ed.setSeconds(59);
                        stats.clear();


                    if(day>-8 && day<=0) {
                        if(day==0){
                            st.setHours(1);
                            st.setMinutes(1);
                            ed.setHours(0);
                            ed.setMinutes(59);
                            Calendar c = Calendar.getInstance();
                            c.setTime(ed);
                            c.add(Calendar.DATE, 1);
                            ed = c.getTime();
                            total=0;
                            mAdapter = new UsageStatsAdapter(st, ed, "daily");
                            if(mAdapter!=null) {
                                listView.setAdapter(mAdapter);
                            }
                        }else {
                            mAdapter = new UsageStatsAdapter(st, ed, "daily");
                            if(mAdapter!=null) {
                                listView.setAdapter(mAdapter);
                            }
                        }

                        if(TimeAdd!=0){
                            Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                            TimeAdd=0;
                        }else if(RemoveTime!=0){
                            mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                            Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                            RemoveTime=0;
                        }else {
                            Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                        }
                    }else {
                        listView.setAdapter(null);
                        Toltal.setText("No data for the day ");
                    }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            try {
                day=0;

                Date date1 = new SimpleDateFormat("dd MMMM yyyy").parse(title_date.getText().toString());
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date1);
                Date st = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                Date ed = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                st.setHours(1);
                st.setMinutes(1);
                ed.setHours(0);
                ed.setMinutes(59);
                Calendar c = Calendar.getInstance();
                c.setTime(ed);
                c.add(Calendar.DATE, 1);
                ed = c.getTime();
                total=0;
                mAdapter = new UsageStatsAdapter(st, ed,"daily");
                if(TimeAdd!=0){
                    Toltal.setText("Total Phone Usage : "+Time(TimeAdd));
                    TimeAdd=0;
                }else if(RemoveTime!=0){
                    mAdapter.Toltal_days=mAdapter.Toltal_days-RemoveTime;
                    Toltal.setText("Total Phone Usage : "+Time(mAdapter.Toltal_days));
                    RemoveTime=0;
                }else {
                    Toltal.setText("Total Phone Usage : " + Time(mAdapter.Toltal_days));
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            if(mAdapter!=null) {
                listView.setAdapter(mAdapter);
            }

        }





    }

}
