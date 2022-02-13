package com.example.time;

import android.app.Activity;
import android.app.Dialog;
import android.app.TaskStackBuilder;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Observable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.*;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.SensorsApi;
import com.google.android.gms.fitness.data.Bucket;
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
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getTimeInstance;

public class Main_page extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    Dialog myDialog;
    Dialog myDialog1;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private ArrayAdapter aAdapter,Adapter;
    private PackageManager packageManager=null;
    public List<ApplicationInfo> aaplist=null;
    public List<ApplicationInfo> asplist=new ArrayList<>();
    public  static  List<ApplicationInfo> WhitelList_add=new ArrayList<>();
    public  static  List<ApplicationInfo> WhitelList_remove=new ArrayList<>();
    public static Databaseadd mydb;
    public static Databaseremove mydb_remove;
    SharedPreferences sharedPreferences;
    EditText Search;
    List<ApplicationInfo> copy = new ArrayList<>();
    SwitchCompat simpleSwitch;
    NavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        startService(new Intent(this,Service_Block.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//-------------------------------------- USAGE STATS PERMISSION ---------------------------------------------
        UsageStatsManager mUsageStatsManager;
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = mUsageStatsManager
                .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis());
        boolean isEmpty = stats.isEmpty();
        if (isEmpty) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

// ----------------------------------------------------------------------------------------------------------

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        account = GoogleSignIn.getLastSignedInAccount(this);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }




        myDialog = new Dialog(this);
        myDialog1 = new Dialog(this);
        packageManager=getPackageManager();
        mydb=new Databaseadd(this);
        mydb_remove=new Databaseremove(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_analyse, R.id.nav_data,
                R.id.nav_help, R.id.nav_calander, R.id.nav_solution,R.id.nav_category)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


              }


    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);

        return true;
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout){
            signout();
        }
        if(id==R.id.add){
            //ShowPopup();
            Show_list();
        }
        if(id==R.id.remove){
            show_list_remove();
        }

        return super.onOptionsItemSelected(item);
    }


public void show_list_remove(){
    WhitelList_add.clear();
    mydb.deleteall();
    myDialog.setContentView(R.layout.pop_add);
    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    myDialog.show();
    Button add=myDialog.findViewById(R.id.add_list);
    add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShowPopup_remove(v);
        }
    });
    ListView listView1=(ListView)myDialog.findViewById(R.id.Listname);
    aaplist=getPackageManager().getInstalledApplications(0);
    if(aaplist!=null) {
        ArrayList<String>arrayList=mydb_remove.selectall();
        if(arrayList.size()!=0){
            WhitelList_remove.clear();
            for(int i=0;i<arrayList.size();i++) {
                for (int j = 0; j < aaplist.size(); j++) {
                    if (arrayList.get(i).equals(aaplist.get(j).packageName)) {
                        WhitelList_remove.add(aaplist.get(j));

                    }
                }
            }
        }
        Adapter = new Adapter(this, 0, WhitelList_remove);
        listView1.setAdapter(Adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mydb_remove.deleteData(WhitelList_remove.get(position).packageName);
                WhitelList_remove.remove(WhitelList_remove.get(position));
                myDialog.dismiss();
                show_list_remove();

            }
        });

    }

}
    public void  Show_list(){
        WhitelList_remove.clear();
        mydb_remove.deleteall();
        myDialog.setContentView(R.layout.pop_add);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        Button add=myDialog.findViewById(R.id.add_list);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });
        ListView listView1=(ListView)myDialog.findViewById(R.id.Listname);
        aaplist=getPackageManager().getInstalledApplications(0);
        if(aaplist!=null) {
            ArrayList<String>arrayList=mydb.selectall();
            if(arrayList.size()!=0){
                WhitelList_add.clear();
            for(int i=0;i<arrayList.size();i++) {
                for (int j = 0; j < aaplist.size(); j++) {
                    if (arrayList.get(i).equals(aaplist.get(j).packageName)) {
                        WhitelList_add.add(aaplist.get(j));

                    }
                }
            }
            }
            Adapter = new Adapter(this, 0, WhitelList_add);
            listView1.setAdapter(Adapter);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    mydb.deleteData(WhitelList_add.get(position).packageName);
                    WhitelList_add.remove(WhitelList_add.get(position));
                    myDialog.dismiss();
                    Show_list();

                }
            });

        }
    }



    public class Adapter extends  ArrayAdapter<ApplicationInfo>{

        private List<ApplicationInfo> appList=null;
        private  Context context;
        private  PackageManager packageManager;
        public Adapter(@NonNull Context context, int resource,List<ApplicationInfo> objects) {
            super(context, resource,objects);
            this.context=context;
            this.appList=objects;
            packageManager=context.getPackageManager();
        }

        @Override
        public int getCount() {
            return ((null !=appList) ?appList.size() : 0);
        }
        @Override
        public ApplicationInfo getItem(int position){
            return ((null !=appList) ?appList.get(position) : null);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view=convertView;
            if(null==view){
                LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view=layoutInflater.inflate(R.layout.info,null);
            }
            ApplicationInfo data=appList.get(position);

            if(null!=data) {

                            TextView name = (TextView) view.findViewById(R.id.name);
                            ImageView icon = (ImageView) view.findViewById(R.id.icon);
                            name.setText(data.loadLabel(packageManager));
                            icon.setImageDrawable(data.loadIcon(packageManager));
                        }



            return view;
        }
    }

    public void ShowPopup(View v) {
        asplist.clear();
        myDialog1.setContentView(R.layout.pop_list);
        final ListView listViews = myDialog1.findViewById(R.id.List_info);
        myDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog1.show();
        Search=myDialog1.findViewById(R.id.search);

        for (int k = 0; k <  WhitelList_add.size(); k++) {
                for (int j = 0; j < aaplist.size(); j++) {
                    if (WhitelList_add.get(k).packageName.equals(aaplist.get(j).packageName)) {
                        aaplist.remove(j);
                    }
                }
            }

        aAdapter = new Adapter(this, 0, aaplist);
        listViews.setAdapter(aAdapter);


        listViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    WhitelList_add.add(aaplist.get(position));
                    mydb.insertData(aaplist.get(position).packageName);
                    myDialog1.dismiss();
                    Show_list();

                }
            });

        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                copy.clear();
                if(Search.getText().toString().equals("")){
                    aAdapter = new Adapter(myDialog1.getContext(), 0, aaplist);
                    listViews.setAdapter(aAdapter);
                }else{
                    CharSequence last =s.subSequence(Search.getText().length()-1,Search.getText().length());
                    if(last.toString().equals("\n")){

                        String text = Search.getText().toString();
                        Search.setText(text.substring(0, text.length() - 1));
                        Search.setSelection(Search.getText().length());
                    }else {

                        for (int i = 0; i < aaplist.size(); i++) {
                            if (aaplist.get(i).loadLabel(myDialog1.getContext().getPackageManager()).toString().indexOf(s.toString())==0) {
                                copy.add(aaplist.get(i));
                            }
                        }


                        aAdapter = new Adapter(myDialog1.getContext(), 0, copy);
                        listViews.setAdapter(aAdapter);

                        listViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                WhitelList_add.add(copy.get(position));
                                mydb.insertData(copy.get(position).packageName);
                                myDialog1.dismiss();
                                Show_list();

                            }
                        });




                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void ShowPopup_remove(View v) {
        asplist.clear();
        myDialog1.setContentView(R.layout.pop_list);
        final ListView listViews = myDialog1.findViewById(R.id.List_info);
        myDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog1.show();
        Search=myDialog1.findViewById(R.id.search);

        for (int k = 0; k <  WhitelList_remove.size(); k++) {
            for (int j = 0; j < aaplist.size(); j++) {
                if (WhitelList_remove.get(k).packageName.equals(aaplist.get(j).packageName)) {
                    aaplist.remove(j);
                }
            }
        }


        aAdapter = new Adapter(this, 0, aaplist);
        listViews.setAdapter(aAdapter);

        listViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                WhitelList_remove.add(aaplist.get(position));
                mydb_remove.insertData(aaplist.get(position).packageName);
                myDialog1.dismiss();
                show_list_remove();

            }
        });

        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                copy.clear();
                if(Search.getText().toString().equals("")){
                    aAdapter = new Adapter(myDialog1.getContext(), 0, aaplist);
                    listViews.setAdapter(aAdapter);
                }else{
                    CharSequence last =s.subSequence(Search.getText().length()-1,Search.getText().length());
                    if(last.toString().equals("\n")){

                        String text = Search.getText().toString();
                        Search.setText(text.substring(0, text.length() - 1));
                        Search.setSelection(Search.getText().length());
                    }else {


                        for (int i = 0; i < aaplist.size(); i++) {
                            if (aaplist.get(i).loadLabel(myDialog1.getContext().getPackageManager()).toString().indexOf(s.toString())==0) {
                               System.out.println("edeeefef "+aaplist.get(i));
                                copy.add(aaplist.get(i));
                            }
                        }



                        listViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                WhitelList_remove.add(copy.get(position));
                                mydb_remove.insertData(copy.get(position).packageName);
                                myDialog1.dismiss();
                                show_list_remove();

                            }
                        });

                        aAdapter = new Adapter(myDialog1.getContext(), 0, copy);
                        listViews.setAdapter(aAdapter);


                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void close(View v ){
        myDialog.dismiss();
        myDialog1.dismiss();
    }

    public void signout() {

        if(account!=null) {
            mGoogleSignInClient.revokeAccess()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                            LoginManager.getInstance().logOut();
                            Toast.makeText(getApplicationContext(), "log out ", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Main_page.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

        } else if(AccessToken.getCurrentAccessToken()!=null){
            LoginManager.getInstance().logOut();
            Intent i = new Intent(Main_page.this, MainActivity.class);
            Toast.makeText(getApplicationContext(), "log out ", Toast.LENGTH_SHORT).show();
            startActivity(i);
            finish();

        }else {
            sharedPreferences=getSharedPreferences("active", 0);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("active",0);
            editor.apply();
            Intent i = new Intent(Main_page.this, MainActivity.class);
            Toast.makeText(getApplicationContext(), "log out", Toast.LENGTH_SHORT).show();
            startActivity(i);
            finish();

        }
    }


    }
