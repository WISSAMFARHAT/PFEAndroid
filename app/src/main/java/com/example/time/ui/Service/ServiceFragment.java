package com.example.time.ui.Service;


import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.example.time.R;
import com.example.time.Service_Block;

import java.util.List;


public class ServiceFragment extends Fragment {

    ImageView icon_app1,icon_app2;
    TextView close,Notif;
    TimePicker timePicker;
    TextView Name_app1,Name_app2,Time_app1,Time_app2;
    SwitchCompat Switch_app1,Switch_app2,Switch_servie;
    PackageManager packageManager;
    Dialog myDialog;
    Button select;
    SharedPreferences.Editor editor;
    Boolean service=true,apps1=true,apps2=true;
    SharedPreferences sharedPreferences;
    String app1,app2;
    String Pkgapp1="",Pkgapp2="";
    String pkgapp1="",pkgapp2="";
    Float Tapp1,Tapp2;
    Drawable img1 = null,img2=null;
    Service_Block mSensorService = new Service_Block(getContext());

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;
            }
        }
        return false;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service, container, false);
        myDialog = new Dialog(getContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences = getActivity().getSharedPreferences("Tracking", 0);
        editor = sharedPreferences.edit();
        packageManager = getActivity().getPackageManager();
        icon_app1=root.findViewById(R.id.icon_app1);
        icon_app2=root.findViewById(R.id.icon_app2);
        Name_app1=root.findViewById(R.id.name_app1);
        Name_app2=root.findViewById(R.id.name_app2);
        Time_app1=root.findViewById(R.id.time_app1);
        Time_app2=root.findViewById(R.id.time_app2);
        Switch_app1=root.findViewById(R.id.on_off_app1);
        Switch_app2=root.findViewById(R.id.on_off_app2);
        Switch_servie=root.findViewById(R.id.service_on_off);
        service=sharedPreferences.getBoolean("Boolean_service",true);
        Switch_servie.setChecked(service);
        apps1=sharedPreferences.getBoolean("Boolean_app1",true);
        Switch_app1.setChecked(apps1);

        apps2=sharedPreferences.getBoolean("Boolean_app2",true);
        Switch_app2.setChecked(apps2);


        app1 = sharedPreferences.getString("block_app_1", "");
        app2 = sharedPreferences.getString("Name_blocker", "");
        Tapp1 = sharedPreferences.getFloat("Block", -1);
        Tapp2 = sharedPreferences.getFloat("Block_app_2", -1);
        pkgapp1=sharedPreferences.getString("pkg_app1", "");
        pkgapp2=sharedPreferences.getString("pkg_app2", "");



        if(service){

            if (!isMyServiceRunning(mSensorService.getClass())) {
                getActivity().startService(new Intent(getContext(), Service_Block.class));
            }


            try {
                if (!pkgapp1.equals("")) {
                    img1 = packageManager.getApplicationIcon(pkgapp1);
                    img1.setBounds(0, 0, 50, 50);
                    icon_app1.setImageDrawable(img1);
                    Name_app1.setText(app1);
                    Time_app1.setText(Time(Tapp1 / 1000));
                    if (!pkgapp2.equals("")) {

                        img2 = packageManager.getApplicationIcon(pkgapp2);
                        img2.setBounds(0, 0, 50, 50);
                        icon_app2.setImageDrawable(img2);
                        Name_app2.setText(app2);
                        Time_app2.setText(Time(Tapp2 / 1000));
                    } else {

                        icon_app2.setVisibility(View.INVISIBLE);
                        Name_app2.setVisibility(View.INVISIBLE);
                        Time_app2.setVisibility(View.INVISIBLE);
                        Switch_app2.setVisibility(View.INVISIBLE);
                    }
                } else {
                    icon_app1.setVisibility(View.INVISIBLE);
                    icon_app2.setVisibility(View.INVISIBLE);
                    Name_app1.setVisibility(View.INVISIBLE);
                    Name_app2.setVisibility(View.INVISIBLE);
                    Time_app1.setVisibility(View.INVISIBLE);
                    Time_app2.setVisibility(View.INVISIBLE);
                    Switch_app1.setVisibility(View.INVISIBLE);
                    Switch_app2.setVisibility(View.INVISIBLE);
                }



            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if(apps1){
                Switch_app1.setChecked(true);
            }else{
                Switch_app1.setChecked(false);
            }
            if(apps2){
                Switch_app2.setChecked(true);
            }else{
                Switch_app2.setChecked(false);
            }
            Switch_servie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        editor.putBoolean("Boolean_service",true);
                        editor.commit();
                        if (!isMyServiceRunning(mSensorService.getClass())) {
                            getActivity().startService(new Intent(getContext(), Service_Block.class));
                        }
                        if (!pkgapp1.equals("")) {
                            if (!pkgapp2.equals("")) {
                                icon_app1.setVisibility(View.VISIBLE);
                                icon_app2.setVisibility(View.VISIBLE);
                                Name_app1.setVisibility(View.VISIBLE);
                                Name_app2.setVisibility(View.VISIBLE);
                                Time_app1.setVisibility(View.VISIBLE);
                                Time_app2.setVisibility(View.VISIBLE);
                                Switch_app1.setVisibility(View.VISIBLE);
                                Switch_app2.setVisibility(View.VISIBLE);
                            } else {
                                icon_app1.setVisibility(View.VISIBLE);
                                Name_app1.setVisibility(View.VISIBLE);
                                Time_app1.setVisibility(View.VISIBLE);
                                Switch_app1.setVisibility(View.VISIBLE);

                            }

                        } else {
                            icon_app1.setVisibility(View.INVISIBLE);
                            icon_app2.setVisibility(View.INVISIBLE);
                            Name_app1.setVisibility(View.INVISIBLE);
                            Name_app2.setVisibility(View.INVISIBLE);
                            Time_app1.setVisibility(View.INVISIBLE);
                            Time_app2.setVisibility(View.INVISIBLE);
                            Switch_app1.setVisibility(View.INVISIBLE);
                            Switch_app2.setVisibility(View.INVISIBLE);
                        }



                    } else {
                        editor.putBoolean("Boolean_service",false);
                        editor.commit();
                        if (isMyServiceRunning(mSensorService.getClass())) {
                            getActivity().stopService(new Intent(getContext(), Service_Block.class));

                        }
                        icon_app1.setVisibility(View.INVISIBLE);
                        icon_app2.setVisibility(View.INVISIBLE);
                        Name_app1.setVisibility(View.INVISIBLE);
                        Name_app2.setVisibility(View.INVISIBLE);
                        Time_app1.setVisibility(View.INVISIBLE);
                        Time_app2.setVisibility(View.INVISIBLE);
                        Switch_app1.setVisibility(View.INVISIBLE);
                        Switch_app2.setVisibility(View.INVISIBLE);
                    }


                }
            });
            Switch_app1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){

                        editor.putBoolean("Boolean_app1",true);
                        editor.commit();
                    }else{
                        editor.putBoolean("Boolean_app1",false);
                        editor.commit();
                    }
                }
            });

            Switch_app2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        editor.putBoolean("Boolean_app2",true);
                        editor.commit();
                    }else{
                        editor.putBoolean("Boolean_app2",false);
                        editor.commit();
                    }
                }
            });


        }else{
            editor.putBoolean("Boolean_service",false);
            editor.commit();
            if (isMyServiceRunning(mSensorService.getClass())) {
                getActivity().stopService(new Intent(getContext(), Service_Block.class));
            }
            icon_app1.setVisibility(View.INVISIBLE);
            icon_app2.setVisibility(View.INVISIBLE);
            Name_app1.setVisibility(View.INVISIBLE);
            Name_app2.setVisibility(View.INVISIBLE);
            Time_app1.setVisibility(View.INVISIBLE);
            Time_app2.setVisibility(View.INVISIBLE);
            Switch_app1.setVisibility(View.INVISIBLE);
            Switch_app2.setVisibility(View.INVISIBLE);
            apps1=sharedPreferences.getBoolean("Boolean_app1",true);
            Switch_app1.setChecked(apps1);

            apps2=sharedPreferences.getBoolean("Boolean_app2",true);
            Switch_app2.setChecked(apps2);
            Switch_servie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        editor.putBoolean("Boolean_service",true);
                        editor.commit();
                        if (!isMyServiceRunning(mSensorService.getClass())) {
                            getActivity().startService(new Intent(getContext(), Service_Block.class));
                        }
                        icon_app1.setVisibility(View.VISIBLE);
                        icon_app2.setVisibility(View.VISIBLE);
                        Name_app1.setVisibility(View.VISIBLE);
                        Name_app2.setVisibility(View.VISIBLE);
                        Time_app1.setVisibility(View.VISIBLE);
                        Time_app2.setVisibility(View.VISIBLE);
                        Switch_app1.setVisibility(View.VISIBLE);
                        Switch_app2.setVisibility(View.VISIBLE);
                        app1 = sharedPreferences.getString("block_app_1", "");
                        app2 = sharedPreferences.getString("Name_blocker", "");
                        Tapp1 = sharedPreferences.getFloat("Block", -1);
                        Tapp2 = sharedPreferences.getFloat("Block_app_2", -1);
//
//                        app1 = "WhatsApp";
//                        app2 = "YouTube";
//                        Tapp1 = Float.valueOf(1800000l);
//                        Tapp2 = Float.valueOf(1800000l);
//                        pkgapp1="com.google.android.youtube";
//                        pkgapp2="com.google.android.youtube";
                        try {
                            if (!pkgapp1.equals("")) {
                                img1 = packageManager.getApplicationIcon(pkgapp1);
                                img1.setBounds(0, 0, 50, 50);
                                icon_app1.setImageDrawable(img1);
                                Name_app1.setText(app1);
                                Time_app1.setText(Time(Tapp1 / 1000));
                                if (!pkgapp2.equals("")) {
                                    img2 = packageManager.getApplicationIcon(pkgapp2);
                                    img2.setBounds(0, 0, 50, 50);
                                    icon_app2.setImageDrawable(img2);
                                    Name_app2.setText(app2);
                                    Time_app2.setText(Time(Tapp2 / 1000));
                                } else {
                                    icon_app2.setVisibility(View.INVISIBLE);
                                    Name_app2.setVisibility(View.INVISIBLE);
                                    Time_app2.setVisibility(View.INVISIBLE);
                                    Switch_app2.setVisibility(View.INVISIBLE);
                                }
                            } else {

                                icon_app1.setVisibility(View.INVISIBLE);
                                icon_app2.setVisibility(View.INVISIBLE);
                                Name_app1.setVisibility(View.INVISIBLE);
                                Name_app2.setVisibility(View.INVISIBLE);
                                Time_app1.setVisibility(View.INVISIBLE);
                                Time_app2.setVisibility(View.INVISIBLE);
                                Switch_app1.setVisibility(View.INVISIBLE);
                                Switch_app2.setVisibility(View.INVISIBLE);
                            }


                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        if(apps1){
                            Switch_app1.setChecked(true);
                        }else{
                            Switch_app1.setChecked(false);
                        }
                        if(apps2){
                            Switch_app2.setChecked(true);
                        }else{
                            Switch_app2.setChecked(false);
                        }
                        Switch_app1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    editor.putBoolean("Boolean_app1",true);
                                    editor.commit();
                                }else{
                                    editor.putBoolean("Boolean_app1",false);
                                    editor.commit();
                                }
                            }
                        });

                        Switch_app2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    editor.putBoolean("Boolean_app2",true);
                                    editor.commit();
                                }else{
                                    editor.putBoolean("Boolean_app2",false);
                                    editor.commit();
                                }
                            }
                        });

                    }else{
                        editor.putBoolean("Boolean_service",false);
                        editor.commit();
                        if (isMyServiceRunning(mSensorService.getClass())) {
                            getActivity().stopService(new Intent(getContext(), Service_Block.class));
                        }
                        icon_app1.setVisibility(View.INVISIBLE);
                        icon_app2.setVisibility(View.INVISIBLE);
                        Name_app1.setVisibility(View.INVISIBLE);
                        Name_app2.setVisibility(View.INVISIBLE);
                        Time_app1.setVisibility(View.INVISIBLE);
                        Time_app2.setVisibility(View.INVISIBLE);
                        Switch_app1.setVisibility(View.INVISIBLE);
                        Switch_app2.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        Time_app1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show("time_app1");
            }
        });

        Time_app2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show("time_app2");
            }
        });

        return root;
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
public void show(final String na){
    myDialog.setContentView(R.layout.pop_time);
    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    myDialog.show();
    close=myDialog.findViewById(R.id.exit);
    timePicker=myDialog.findViewById(R.id.timer);
    select=myDialog.findViewById(R.id.select);
    close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myDialog.dismiss();
        }
    });
    timePicker.setHour(0);
    timePicker.setMinute(0);
    timePicker.setEnabled(true);
    timePicker.setIs24HourView(true);
    select.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(na.equals("time_app1")){
                Time_app1.setText(""+Time((float) ((((timePicker.getCurrentHour())+(0.0166666667 *timePicker.getCurrentMinute()))*3600000)/1000)));
                editor.putFloat("Block", (float) (((timePicker.getCurrentHour())+(0.0166666667 *timePicker.getCurrentMinute()))*3600000));
                editor.commit();
                myDialog.dismiss();
            }else if(na.equals("time_app2")){
                Time_app2.setText(""+Time((float) ((((timePicker.getCurrentHour())+(0.0166666667 *timePicker.getCurrentMinute()))*3600000)/1000)));
                editor.putFloat("Block_app_2",(float) (((timePicker.getCurrentHour())+(0.0166666667 *timePicker.getCurrentMinute()))*3600000));
                editor.commit();
                myDialog.dismiss();
            }
        }
    });

}

}
