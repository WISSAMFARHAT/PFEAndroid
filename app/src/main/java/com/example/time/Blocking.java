package com.example.time;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Blocking extends AppCompatActivity {
    TextView t,name;
    ImageView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocking);
        Intent a =getIntent();
        t=findViewById(R.id.back);
        name=findViewById(R.id.name);
        v=findViewById(R.id.imageView6);
        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai=pm.getApplicationInfo(a.getStringExtra("Name"),0);
            v.setImageDrawable(pm.getApplicationIcon(a.getStringExtra("Name")));
        } catch (PackageManager.NameNotFoundException e) {
            ai=null;
        }
        name.setText(pm.getApplicationLabel(ai));

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finish();
            }
        });
    }
}
