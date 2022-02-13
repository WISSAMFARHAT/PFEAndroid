package com.example.time;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class HomeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    private static int SPLASH_TIME_OUT=1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                sharedPreferences=getSharedPreferences("active", 0);
                int active=sharedPreferences.getInt("active",0);
                if(active==1){
                    Intent homeIntent = new Intent(HomeActivity.this, Main_page.class);
                    startActivity(homeIntent);
                    finish();
                }else {
                    Intent homeIntent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

    }
}



