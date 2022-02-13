package com.example.time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Servie_Restart extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        context.startForegroundService(new Intent(context,Service_Block.class));
    }
}
