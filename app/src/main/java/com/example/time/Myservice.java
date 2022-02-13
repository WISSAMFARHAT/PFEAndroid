package com.example.time;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.example.time.ui.home.HomeFragment;

public class Myservice extends BroadcastReceiver {
    int ID = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = context.getSharedPreferences("Tracking", 0);
        editor = sharedPreferences.edit();
        editor.putBoolean("Boolean_service",false);
        editor.commit();


        // Notification();
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        Intent ii = new Intent(context, Main_page.class);
        ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, ii, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.icon);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("Check your Tracking ");
        mBuilder.setContentText("View your traking for today ");

//// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
            mNotificationManager.notify(ID, mBuilder.build());
            ID++;

            System.out.println("Wdwdwrerte ");
        }

    }

