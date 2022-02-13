package com.example.time;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.time.ui.home.HomeFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class Service_Block extends Service {
    private UsageStatsManager mUsageStatsManager;
    Timer repeatTask = new Timer();
    int repeatInterval = 1000;
    long last=0;
    Boolean service=true,apps1=true,apps2=true;
    List<UsageStats> stats;
    ApplicationInfo applicationInfo = null;
    PackageManager packageManager;
    SharedPreferences sharedPreferences;
    String app1="",app2="";
    String pkgapp1="",pkgapp2="";
    Float Tapp1,Tapp2;
    MediaPlayer player;
    int days=-1;
    long end,begin;

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            repeatTask.scheduleAtFixedRate(new TimerTask() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void run() {

                    app1=sharedPreferences.getString("pkg_app1", "");
                    app2=sharedPreferences.getString("pkg_app2", "");
                    Tapp1 = sharedPreferences.getFloat("Block", -1);
                    Tapp2 = sharedPreferences.getFloat("Block_app_2", -1);
                    days = sharedPreferences.getInt("service_days", -1);
                    service=sharedPreferences.getBoolean("Boolean_service",true);
                    apps1=sharedPreferences.getBoolean("Boolean_app1",true);
                    apps2=sharedPreferences.getBoolean("Boolean_app2",true);

          if(!service){
              return;
          }

                        Calendar today = Calendar.getInstance();
          if(today.getTime().getHours()>=0 && today.getTime().getHours()<1){
              today.add(Calendar.DAY_OF_WEEK,-1);
              today.set(Calendar.HOUR_OF_DAY, 1);
              today.set(Calendar.MINUTE, 1);
              today.set(Calendar.SECOND, 0);

              begin = today.getTimeInMillis();
              today.set(Calendar.HOUR_OF_DAY, 23);
              today.set(Calendar.MINUTE, 59);
              today.set(Calendar.SECOND, 0);
              end = today.getTimeInMillis();
          }else {
              today.set(Calendar.HOUR_OF_DAY, 1);
              today.set(Calendar.MINUTE, 1);
              today.set(Calendar.SECOND, 0);

               begin = today.getTimeInMillis();
              today.set(Calendar.HOUR_OF_DAY, 23);
              today.set(Calendar.MINUTE, 59);
              today.set(Calendar.SECOND, 0);
               end = today.getTimeInMillis();
          }
                        stats =
                                mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                        begin, end);


                        if(apps1) {
                            if (!app1.equals("")) {
                                if (stats == null) {

                                } else {
                                    for (int i = 0; i < stats.size(); i++) {


                                        if (app1.equals(stats.get(i).getPackageName())) {
                                            Long time = stats.get(i).getTotalTimeInForeground();
                                            if (time > Tapp1) {
                                                ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                                                String mPackageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();

                                                if (mPackageName.equals("com.example.time")) {
                                                    last = stats.get(i).getLastTimeUsed();
                                                } else {

                                                    if (stats.get(i).getPackageName().equals(getForegroundApp())) {
                                                        showHomeScreen(app1);
                                                        last = stats.get(i).getLastTimeUsed();
                                                    } else {
                                                        last = stats.get(i).getLastTimeUsed();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                                    if(apps2) {
                                        if (!app2.equals("")) {
                                            for (int i = 0; i < stats.size(); i++) {

                                            if (app2.equals(stats.get(i).getPackageName())) {
                                                Long time = stats.get(i).getTotalTimeInForeground();
                                                if (time > Tapp2) {
                                                    ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                                                    String mPackageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
                                                    if (mPackageName.equals("com.example.time")) {
                                                        last = stats.get(i).getLastTimeUsed();
                                                    } else {
                                                        if (stats.get(i).getPackageName().equals(getForegroundApp())) {
                                                            showHomeScreen(app2);
                                                            last = stats.get(i).getLastTimeUsed();
                                                        } else {
                                                            last = stats.get(i).getLastTimeUsed();
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            stats.clear();

                    }


            }, 0, repeatInterval);
//        }
        return START_STICKY_COMPATIBILITY;
    }

    public String getForegroundApp() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        return currentApp;
    }


    @Override
    public boolean stopService(Intent name) {
        stopSelf();

        return super.stopService(name);

    }

    public Service_Block(){}
    public Service_Block(Context applicationContext) {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent broadcast=new Intent(this,Servie_Restart.class);
        sendBroadcast(broadcast);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        service=sharedPreferences.getBoolean("Boolean_service",true);
        if(service) {
            Intent broadcast = new Intent(this, Servie_Restart.class);
            sendBroadcast(broadcast);
        }else{

        }
    }
    @Override
    public void onCreate() {
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences("Tracking", 0);
         packageManager = getPackageManager();
        service=sharedPreferences.getBoolean("Boolean_service",true);



        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Blocking apps service ")
                .setContentText("")
                .setSmallIcon(R.mipmap.icon)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(101, notification);

        super.onCreate();
    }

    public boolean showHomeScreen(String name){
        Intent startMain = new Intent(this,Blocking.class);
        startMain.putExtra("Name",name);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

        return true;
    }




}
