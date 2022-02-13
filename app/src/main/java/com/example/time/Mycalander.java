package com.example.time;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.time.ui.home.HomeFragment;

public class Mycalander extends BroadcastReceiver {
    int ID=0;
    private NotificationManager mNotificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Notification();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "notify_001");

        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent ii = new Intent(context, Main_page.class);
        ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, ii, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.icon);
        mBuilder.setContentTitle("Check your Calander ");
        mBuilder.setContentText("Your Event : "+intent.getStringExtra("event")+" ");
        mBuilder.setAutoCancel(true);




//// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
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
    }


}

