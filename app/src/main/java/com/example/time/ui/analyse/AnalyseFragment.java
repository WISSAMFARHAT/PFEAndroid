package com.example.time.ui.analyse;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.time.AnalyseApps;
import com.example.time.R;
import com.example.time.Usage_Today;
import com.example.time.ui.home.HomeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AnalyseFragment extends Fragment {
    public  final ArrayList<UsageStats> mPackageStats = new ArrayList<>();

    ListView listView;
    List<UsageStats> stats;
    TextView text;
    int temp1=-1,temp2=-1;
    String date1,date2;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM");
    private LayoutInflater mInflater;
    private PackageManager mPm;
    public  ArrayList<UsageStats> mPackageStats3 = new ArrayList<>();
    private UsageStatsManager mUsageStatsManager;
    private AnalyseFragment.UsageStatsAdapter mAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_analyse, container, false);
        listView=root.findViewById(R.id.pkg_list);
        mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mPm = getActivity().getPackageManager();
        Calendar calander = Calendar.getInstance();
        calander.add(Calendar.DAY_OF_WEEK, -1);
        date1=format.format(calander.getTime());
        calander.add(Calendar.DAY_OF_WEEK, -1);
        date2=format.format(calander.getTime());
        try {
            mAdapter = new UsageStatsAdapter();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent =new Intent(getContext(), AnalyseApps.class);
                intent.putExtra("Name",mPackageStats3.get(position).getPackageName());
                startActivity(intent);
            }
        });
        return root;
    }

    public static class UsageTimeComparator implements Comparator<UsageStats> {
        @Override
        public final int compare(UsageStats a, UsageStats b) {
            return (int)(b.getTotalTimeInForeground() - a.getTotalTimeInForeground());

        }
    }

    static class AppViewHolder {
        TextView pkgName;
        ImageView pkgicon;
        TextView T1;
        TextView T2;
        TextView R;

    }

    public class UsageStatsAdapter extends BaseAdapter {
        public AnalyseFragment.UsageTimeComparator mUsageTimeComparator = new AnalyseFragment.UsageTimeComparator();
        public  final ArrayList<UsageStats> mPackageStats1 = new ArrayList<>();
        public  final ArrayList<UsageStats> mPackageStats2 = new ArrayList<>();

        Long Time1=0l;
        Long Time2=0l;
        Long Result=0l;


        public UsageStatsAdapter() throws ParseException {
            Calendar ca=Calendar.getInstance();
            ca.add(Calendar.DAY_OF_WEEK,-1);
            ca.set(Calendar.HOUR_OF_DAY,3);
            ca.set(Calendar.MINUTE,0);
            Long date_start1= ca.getTimeInMillis();
            ca.set(Calendar.HOUR_OF_DAY,23);
            Long Date_end1=ca.getTimeInMillis();


            ca.add(Calendar.DAY_OF_WEEK,-1);
            ca.set(Calendar.HOUR_OF_DAY,3);
            ca.set(Calendar.MINUTE,0);
            Long date_start2= ca.getTimeInMillis();
            ca.set(Calendar.HOUR_OF_DAY,23);
            Long Date_end2=ca.getTimeInMillis();


            stats=mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,date_start2,Date_end2);
            if (stats == null) {
                return ;
            }
            for(int i=0;i<stats.size();i++){
                if(stats.get(i).getTotalTimeInForeground()!=0){
                    mPackageStats1.add(stats.get(i));
                }
            }
            stats.clear();
            stats=mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,date_start1,Date_end1);
            if (stats == null) {
                return ;
            }

            for(int i=0;i<stats.size();i++){
                if(stats.get(i).getTotalTimeInForeground()!=0){
                    mPackageStats2.add(stats.get(i));

                }
            }


            for(int i=0;i<mPackageStats2.size();i++){
                for(int j=0;j<mPackageStats1.size();j++){
                    if(mPackageStats2.get(i).getPackageName().equals(mPackageStats1.get(j).getPackageName())){
                        mPackageStats3.add(mPackageStats2.get(i));
                    }
                }
            }
            sortList();

        }

        private void sortList() {

            Collections.sort(mPackageStats3, mUsageTimeComparator);
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return mPackageStats3.size();
        }

        @Override
        public Object getItem(int position) {

            return mPackageStats3.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
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

            AnalyseFragment.AppViewHolder holder;
            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.analyse_item, null);
                holder = new AnalyseFragment.AppViewHolder();
                holder.pkgName = (TextView) convertView.findViewById(R.id.package_name);
                holder.pkgicon = (ImageView) convertView.findViewById(R.id.package_icon);
                holder.T1 = (TextView) convertView.findViewById(R.id.Time1);
                holder.T2 = (TextView) convertView.findViewById(R.id.Time2);
                holder.R=(TextView)convertView.findViewById(R.id.Time3);
                convertView.setTag(holder);


            } else {

                holder = (AnalyseFragment.AppViewHolder) convertView.getTag();

            }

            UsageStats pkgStats = mPackageStats3.get(position);

            if(pkgStats!=null){
                Drawable img = null;
                String name="";
                final PackageManager pm = getApplicationContext().getPackageManager();
                ApplicationInfo ai;
                try {
                    ai=pm.getApplicationInfo(pkgStats.getPackageName(),0);
                    img = mPm.getApplicationIcon(pkgStats.getPackageName());
                    img.setBounds(0, 0, 50, 50);
                    name= (String) pm.getApplicationLabel(ai);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(img!=null) {
                    holder.pkgName.setText(name);
                    holder.pkgicon.setImageDrawable(img);


                    for (int i = 0; i < mPackageStats1.size(); i++) {

                        if (mPackageStats1.get(i).getPackageName().equals(pkgStats.getPackageName())) {

                                Time1 = pkgStats.getTotalTimeInForeground() / 1000;//yesterday
                                Time2 = mPackageStats1.get(i).getTotalTimeInForeground() / 1000;
                                Result = Time1 - Time2;
                                if (Result > 0) {
                                    holder.T2.setText("" + date1 + " : " + Time((float) Time1));
                                    holder.T1.setText("" + date2 + " : " + Time((float) (Time2)));
                                    holder.R.setText("X\n+" + Time((float) Result));
                                    holder.R.setTextColor(Color.rgb(216, 27, 96));
                                    temp2++;
                                } else {

                                    holder.T2.setText("" + date1 + " : " + Time((float) Time1));
                                    holder.T1.setText("" + date2 + " : " + Time((float) (Time2)));
                                    holder.R.setText("✓\n-" + Time((float) Result));
                                    temp1++;
                                    holder.R.setTextColor(Color.rgb(0, 133, 119));
                                    Time1 = 0l;
                                    Time2 = 0l;
                                    Result = 0l;


                                }
                            }
                        }
                    }


            }



            return convertView;
        }
    }



}
