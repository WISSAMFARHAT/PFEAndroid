package com.example.time.ui.solution;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.time.Datagooltime;
import com.example.time.Dataweekly;
import com.example.time.R;
import com.example.time.Service_Block;
import com.example.time.ui.home.HomeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.time.ui.home.HomeFragment.timeapp2;

public class SolutionFragment extends Fragment {
    ArrayList<Integer> rows;
    private PackageManager mPm;
    public List<ApplicationInfo> asplist=new ArrayList<>();
    Service_Block mSensorService = new Service_Block(getContext());
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> temp=new ArrayList<>();
    Boolean service=true;
    private LayoutInflater mInflater;
    private SolutionFragment.list mAdapter;
    TextView about, solution, day, date ,close,Tdate;
    Button left,right;
    Float Game, News, Productives, Media, Total, time_app,Timeapp2,Undifiend;
    Dialog myDialog;
    String Time = "";
    int Top = -1;
    int l=0;
    String des_about = "";
    String app = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApplicationInfo info;
    ListView listView;
    TextView social, productive, New, games,Undifiends;
    Dataweekly db;
    Datagooltime goal;
    String category = "";
    String Description = "2nsa";
    String tempapp = "";
    String des_solution = "";

    int next = 0;
    String next_day = "";
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");

    private UsageStatsManager mUsageStatsManager;
    public class Time {
        public String app;
        public Float time;

        public Time() {
        }

    }



    public String Time(Float time) {
        int minutes = (int) ((time / (60)) % 60);
        int seconds = (int) ((time) % 60);
        int hours = (int) ((time / (60 * 60)));
        if (time < 0) {
            return ("1s");
        } else {
            if (hours == 0) {
                if (minutes == 0) {
                    if (seconds == 0) {
                        return ("1 s");
                    } else {
                        return ("" + seconds + " s");
                    }
                } else {
                    if (seconds == 0) {
                        return ("" + minutes + " m ");
                    } else {
                        return ("" + minutes + " m " + seconds + " s");
                    }
                }
            } else {
                if (minutes == 0) {
                    if (seconds == 0) {
                        return ("" + hours + " h ");
                    } else {
                        return ("" + hours + " H " + seconds + " s");
                    }
                } else {
                    if (seconds == 0) {
                        return ("" + hours + " h " + minutes + " m ");
                    } else {
                        return ("" + hours + " h " + minutes + " m ");
                    }
                }
            }
        }
    }


    public int top(Float Media, Float Productive, Float News, Float Games) {
        int type = 0;
        Float a = Media;
        if (Productive > a) {
            a = Productive;
            type = 1;
        }
        if (News > a) {
            a = News;
            type = 2;
        }
        if (Games > a) {
            a = Games;
            type = 3;
        }

        return type;
    }


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

        asplist=getContext().getPackageManager().getInstalledApplications(0);
        Calendar ca = Calendar.getInstance();
        Calendar CalanderTime = Calendar.getInstance();
        setHasOptionsMenu(true);
        myDialog = new Dialog(getContext());
        db = new Dataweekly(getContext());
        //db.delete();
        mPm = getActivity().getPackageManager();
        mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        goal = new Datagooltime(getContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences = getActivity().getSharedPreferences("Tracking", 0);
        editor = sharedPreferences.edit();
        service=sharedPreferences.getBoolean("Boolean_service",true);
        if(service){
            if (!isMyServiceRunning(mSensorService.getClass())) {
                getActivity().startService(new Intent(getContext(), Service_Block.class));
            }
        }

        if ((CalanderTime.getTime().getHours() >= 23 && CalanderTime.getTime().getHours() <= 24) ||
                (CalanderTime.getTime().getHours() >= 0 && CalanderTime.getTime().getHours() < 1)) {
            View root = inflater.inflate(R.layout.fragment_solution, container, false);
            about = root.findViewById(R.id.des_about);
            solution = root.findViewById(R.id.des_solution);
            day = root.findViewById(R.id.des_day);

            date = root.findViewById(R.id.date);
            if(CalanderTime.getTime().getHours() >=0) {
                CalanderTime.add(Calendar.DAY_OF_WEEK,-1);
                date.setText(format.format(CalanderTime.getTime()));
            }else {
                date.setText(format.format(CalanderTime.getTime()));
            }
            social = root.findViewById(R.id.SocialMedia);
            productive = root.findViewById(R.id.Productive);
            New = root.findViewById(R.id.News);
            games = root.findViewById(R.id.Games);
            Undifiends=root.findViewById(R.id.Undifiend);
            Game = HomeFragment.Game;
            Undifiend=HomeFragment.undfiend;
            News = HomeFragment.News;
            Productives = HomeFragment.Productiv;
            Media = HomeFragment.Social;
            Total = HomeFragment.total;
            mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);


            Top = top(Media, Productives, News, Game);
            time_app = HomeFragment.timeapp;
            app = HomeFragment.appTop;
            tempapp=HomeFragment.appTop2;
            Timeapp2= timeapp2;
            des_about = "Today your top category is : ";

            if (Top == 0) {
                Time = Time(Media / 1000);
                des_about = des_about + " Social Media \n";
                category = "Social Media";
            } else if (Top == 1) {
                Time = Time(Productives / 1000);
                des_about = des_about + " Productive \n";
                category = "Productive";
            } else if (Top == 2) {
                Time = Time(News / 1000);
                des_about = des_about + " News \n";
                category = "News";
            } else if (Top == 3) {
                Time = Time(Game / 1000);
                des_about = des_about + " Games and Gallery \n";
                category = "Games";
            }

            des_about = des_about + "you used : " + Time + " in this category \n";
            int heurs = (int) ((Total) / (60 * 60) % 24);

            if (heurs > 4) {
                editor.putFloat("Block",-1);
                editor.commit();
                editor.putFloat("Block_app_2",-1);
                editor.commit();
                editor.putString("block_app_1","");
                editor.commit();
                editor.putString("Name_blocker","");
                editor.commit();
                editor.putBoolean("Boolean_app1",false);
                editor.commit();
                editor.putBoolean("Boolean_app2",false);
                editor.commit();
                des_about = des_about + "you have passed the Goal time (4 hours) \n";


                int dayhours = heurs;
  //---------------------- solution --------------------------------------------------------------------
                if (dayhours <= 5) {
                    if (isMyServiceRunning(mSensorService.getClass())) {
                        getActivity().stopService(new Intent(getContext(), Service_Block.class));
                    }
                    editor.putFloat("Block",-1);
                    editor.commit();
                    editor.putFloat("Block_app_2",-1);
                    editor.commit();
                    editor.putString("block_app_1","");
                    editor.commit();
                    editor.putString("Name_blocker","");
                    editor.commit();
                    editor.putBoolean("Boolean_app1",false);
                    editor.commit();
                    editor.putBoolean("Boolean_app2",false);
                    editor.commit();
                    //solution for the day only
                    des_solution = des_solution + "You are using about " + dayhours + " hours \n" +
                            "Here are some tips : \n" +
                            "-Hide your Notification \n";
                    next = 20;
                    if (ca.getTime().getDay() == 6 || ca.getTime().getDay() == 7) {
                        des_solution = des_solution + "-During vaction days do some activity \n";
                        next = 30;
                    }

                } else if (dayhours > 5 && dayhours < 7) {
                    if (isMyServiceRunning(mSensorService.getClass())) {
                        getActivity().stopService(new Intent(getContext(), Service_Block.class));
                    }
                    editor.putFloat("Block",-1);
                    editor.commit();
                    editor.putFloat("Block_app_2",-1);
                    editor.commit();
                    editor.putString("block_app_1","");
                    editor.commit();
                    editor.putString("Name_blocker","");
                    editor.commit();
                    editor.putBoolean("Boolean_app1",false);
                    editor.commit();
                    editor.putBoolean("Boolean_app2",false);
                    editor.commit();
                    des_solution = des_solution + "You are using about " + dayhours + " hours \n" +
                            "Here are some tips : \n" +
                            "-Hide your Notification \n" +
                            "-Open one or two Time your top gategory : " + category + "\n";
                    next = 30;
                    if (ca.getTime().getDay() == 6 || ca.getTime().getDay() == 7) {
                        des_solution = des_solution + "-During vaction days do some activity \n";
                        next = 40;
                    }

//---------------------------------- controller ---------------------------------------------------------------
                } else if (dayhours >= 7 && dayhours < 9) {
                    if (!isMyServiceRunning(mSensorService.getClass())) {
                        getActivity().startService(new Intent(getContext(), Service_Block.class));
                    }
                    editor.putBoolean("Boolean_service",true);
                    editor.commit();

                    //solution
                    des_solution = des_solution + "You are using about " + dayhours + " hours \n" +
                            "Here are some tips : \n" +
                            "-Hide your Notification \n" +
                            "-Open one or two Time your top gategory : " + category + "\n" +
                            "-don't be alone for long time \n" +
                            "-Keep yourself busy with the things you love\n";
                    editor.putBoolean("Boolean_app1",true);
                    editor.commit();

                    if (ca.getTime().getDay() == 6 || ca.getTime().getDay() == 7) {
                        des_solution = des_solution + "-During vaction days do some activity \n";
                        next = 65;
                    }
                    if(time_app>=7200000) {
                        if(time_app>=1080000){
                            des_solution = des_solution + "You want to controller time for your application (" + app + ")\n" +
                                    "you want to limite your application to maximum : 2 hours " ;
                            editor.putFloat("Block",7200000);
                            editor.commit();
                            editor.putString("block_app_1",app);
                            editor.commit();
                            editor.putBoolean("Boolean_app1",true);
                            editor.commit();

                        }
                        else {
                            des_solution = des_solution + "You want to controller time for your application (" + app + ")\n" +
                                    "you want to limite your application to maximum : " + Time((time_app - 3600000) / 1000);
                            editor.putFloat("Block",time_app - 3600000);
                            editor.commit();
                            editor.putString("block_app_1",app);
                            editor.commit();
                            editor.putBoolean("Boolean_app1",true);
                            editor.commit();
                        }
                        }else{
                        des_solution = des_solution + "You want to controller time for your application (" + app + ")\n" +
                                "you want to limite your application to maximum : " + Time((time_app/2) / 1000);
                        editor.putFloat("Block",time_app/2);
                        editor.commit();
                        editor.putString("block_app_1",app);
                        editor.commit();
                        editor.putBoolean("Boolean_app1",true);
                        editor.commit();
                    }

                    next = 60;
                } else if (dayhours >= 9 && dayhours < 24) {
                    if (!isMyServiceRunning(mSensorService.getClass())) {
                        getActivity().startService(new Intent(getContext(), Service_Block.class));
                    }
                    editor.putBoolean("Boolean_service",true);
                    editor.commit();
                    //controole
                    des_solution = des_solution + "You are using about " + dayhours + " hours \n" +
                            "Here are some tips : \n" +
                            "-Hide your Notification \n" +
                            "-Open one or two Time your top gategory : " + category + "\n" +
                            "-don't be alone for long time \n" +
                            "-Stay away from the phone during break\n" +
                            "-Give your time to sleep\n";
                    if (ca.getTime().getDay() == 6 || ca.getTime().getDay() == 7) {
                        des_solution = des_solution + "-During vaction days do some activity \n";
                        next = 75;
                    }
                        editor.putBoolean("Boolean_app1",true);
                        editor.commit();
                        editor.putBoolean("Boolean_app2",true);
                        editor.commit();
                        if(time_app>=7200000){
                            if(time_app>=1080000){

                                des_solution = des_solution + "You want to controller time for your application (" + app + ")\n" +
                                        "you want to limite your application to maximum : " + Time((time_app/2) / 1000) + "\n" +
                                        "and another application ( " + tempapp + " )  \n To "+ Time((timeapp2 - 1800000l) / 1000) ;
                                editor.putFloat("Block",time_app/2);
                                editor.commit();
                                editor.putFloat("Block_app_2",timeapp2 - 1800000l);
                                editor.commit();
                                editor.putString("block_app_1",app);
                                editor.commit();
                                editor.putString("Name_blocker",tempapp);
                                editor.commit();

                            }else{
                                des_solution = des_solution + "You want to controller time for your application (" + app + ")\n" +
                                        "you want to limite your application to maximum : 2 hours \n" +
                                        "and another application ( " + tempapp + " ) \n To "+ Time((timeapp2 - 1800000l) / 1000) ;
                                editor.putFloat("Block",time_app - 7200000 );
                                editor.commit();
                                editor.putFloat("Block_app_2",timeapp2 - 1800000l);
                                editor.commit();
                                editor.putString("block_app_1",app);
                                editor.commit();
                                editor.putString("Name_blocker",tempapp);
                                editor.commit();
                            }
                        }else{
                            des_solution = des_solution + "You want to controller time for your application (" + app + ")\n" +
                                    "you want to limite your application to maximum : " + Time((time_app/2) / 1000) + "\n" +
                                    "and another application ( " + tempapp + " ) \n To "+ Time((timeapp2 - 1800000l) / 1000) ;
                            editor.putFloat("Block",time_app/2 );
                            editor.commit();
                            editor.putFloat("Block_app_2",timeapp2 - 1800000l);
                            editor.commit();
                            editor.putString("block_app_1",app);
                            editor.commit();
                            editor.putString("Name_blocker",tempapp);
                            editor.commit();

                        }



                    next = 65;
                }


                solution.setText(des_solution);
                editor.putString("Description",des_solution);
                editor.commit();
                editor.putInt("Nextday",next);
                editor.commit();
                des_solution = "";

                int reserve = (next * dayhours) / 100;
                Float divise = (float) reserve / 10;
                reserve = dayhours - reserve;
                editor.putFloat("divise", divise);
                editor.commit();
                editor.putInt("reserve", reserve);
                editor.commit();
                editor.putString("Type", category);
                editor.commit();

                divise = divise * 100;
                Float TMedia = (Media * divise) / 100;
                Float TGame = (Game * divise) / 100;
                Float TProductives = (Productives * divise) / 100;
                Float TNews = (News * divise) / 100;
                Float TUndifiend=(Undifiend * divise)*100;
                if (category.equals("Social Media")) {
                    Media = Media - 1800000l;

                } else if (category.equals("Productive")) {
                    Productives = Productives - 1800000l;

                } else if (category.equals("News")) {
                    News = News - 1800000l;

                } else if (category.equals("Games")) {
                    Game = Game - 1800000l;

                }
                Undifiend=Undifiend-TUndifiend;
                Media = Media - TMedia;
                Game = Game - TGame;
                Productives = Productives - TProductives;
                News = TNews;

                if(Game < 60000){
                    Game= Float.valueOf(60000);
                }
                if(Undifiend < 60000){
                    Undifiend= Float.valueOf(60000);
                }

                games.setText(Time(Game / 1000));
                social.setText((Time((Media) / 1000)));
                productive.setText((Time(Productives / 1000)));
                New.setText(Time(News / 1000));
                Undifiends.setText(Time(Undifiend / 1000));

                Float Total_next_day=(Game)+(Media)+(Productives)+(News)+(Undifiend);

                next_day = "If you get all the solution \n" +
                            "We determine the time used for tomorrow  "+(Time(Total_next_day / 1000));
                day.setText(next_day);

            } else {
                if (heurs == 0) {
                    des_about = des_about + "you passed < one hours (GOOD) \n";
                } else {
                    des_about = des_about + "you passed = " + heurs + " hours (GOOD) \n";
                }
                Description = sharedPreferences.getString("Description", "null");

//                if (goal.trouver(format.format(ca.getTime())) == false) {
//
//                    goal.insertData(format.format(ca.getTime()), Total, app, Description);//insert if is the goal to select the description
//                }
                String[] splits = Description.split("\n");
                String des = "You are used your Goal Time \n ";
                des=des+"We'll give you old solutions, keep to reduce  your time \n ";
                for (int i = 2; i < splits.length; i++) {
                    des = des + splits[i] + "\n";
                }
                solution.setText(des);
                int reserve = sharedPreferences.getInt("reserve", 0);
                int Nextday= sharedPreferences.getInt("Nextday",20);
                reserve = (reserve * Nextday) / 100;



                Float divise = sharedPreferences.getFloat("divise", 0);
                divise = divise * 100;
                Float TMedia = (Media * divise) / 100;
                Float TGame = (Game * divise) / 100;
                Float TProductives = (Productives * divise) / 100;
                Float TUndifiend=(Undifiend*divise)/100;
                Float TNews = (News * divise) / 100;
                Media = Media - TMedia;
                Game = Game - TGame;
                Productives = Productives - TProductives;
                News = TNews;
                Undifiend=Undifiend-TUndifiend;
                if(Game < 60000){
                    Game= Float.valueOf(60000);
                }
                if(Undifiend < 60000){
                    Undifiend= Float.valueOf(60000);
                }

                games.setText(Time(Game / 1000));
                social.setText((Time(Media / 1000)));
                productive.setText((Time(Productives / 1000)));
                New.setText(Time(News / 1000));
                Float Total_next_day=Game+Media+Productives+News+Undifiend;

                next_day = "If you get all the solution \n" +
                        "We determine the time used for tomorrow  "+(Time(Total_next_day / 1000));
                day.setText(next_day);
            }
            des_about = des_about + "Top app used : " + app + " ( " + Time(time_app / 1000) + ") \n";
            des_about = des_about + "Tracking Time put a many solutions ";
            about.setText(des_about);
            Calendar days = Calendar.getInstance();
            String  d=format.format(days.getTime());
            if (ca.getTime().getHours() >= 23 && ca.getTime().getMinutes() >= 30 && ca.getTime().getMinutes() <= 59) {
                if(db.date(d)==false) {
                    db.insertData(ca.getTime().getDay(), Total, category, app, d);
                }
            }else{
                if(ca.getTime().getHours()>=0 && ca.getTime().getHours() < 23){

                    Calendar s =Calendar.getInstance();
                    s.add(Calendar.DAY_OF_WEEK, -1);
                    String  d1=format.format(s.getTime());
                    if(db.date(d1)==false) {
                        db.insertData(s.getTime().getDay(), Total, category, app, d1);
                    }
                }
            }
            editor.putString("Des_about", des_about);
            editor.commit();
            editor.putString("des_day", next_day);
            editor.commit();
            editor.putFloat("PMedia", Media);
            editor.commit();
            editor.putFloat("PGame", Game);
            editor.commit();
            editor.putFloat("PNews", News);
            editor.commit();
            editor.putFloat("Productives", Productives);
            editor.commit();
            editor.putFloat("PUndifiend", Undifiend);
            editor.commit();
//            editor.putInt("service_days",CalanderTime.getTime().getDate()+1);
//-------------------------- save ---------------------------------------------------------------

// ------------------------------------------------------------------------------------------------
//            getActivity().startService(new Intent(getContext(), Service_Block.class));
            getpackagename();
            return root;
        } else {
            editor.putInt("service_days",CalanderTime.getTime().getDate());
            editor.commit();
            View root = inflater.inflate(R.layout.fragment_solution, container, false);
            about = root.findViewById(R.id.des_about);
            solution = root.findViewById(R.id.des_solution);
            social = root.findViewById(R.id.SocialMedia);
            productive = root.findViewById(R.id.Productive);
            date = root.findViewById(R.id.date);
            New = root.findViewById(R.id.News);
            games = root.findViewById(R.id.Games);
            Undifiends=root.findViewById(R.id.Undifiend);
            day = root.findViewById(R.id.des_day);
            Calendar pre = Calendar.getInstance();
            pre.add(Calendar.DAY_OF_WEEK, -1);
            date.setText(format.format(pre.getTime()));
            about.setText(sharedPreferences.getString("Des_about", "about"));
            solution.setText(sharedPreferences.getString("Description", "Solution"));
            day.setText(sharedPreferences.getString("des_day", "Next Day"));
            social.setText(Time(sharedPreferences.getFloat("PMedia", 0) / 1000));
            productive.setText(Time(sharedPreferences.getFloat("Productives", 0) / 1000));
            Undifiends.setText(Time(sharedPreferences.getFloat("PUndifiend",0)/1000));
            games.setText(Time(sharedPreferences.getFloat("PGame", 0) / 1000));
            New.setText(Time(sharedPreferences.getFloat("PNews", 0) / 1000));

//            getActivity().startService(new Intent(getContext(),Service_Block.class));
            getpackagename();
            return root;

        }


    }

    public void getpackagename(){
        if(!app.equals("")){
                editor.putString("pkg_app1",HomeFragment.Pkg);
                editor.commit();
        }
        if(!tempapp.equals("")){
            editor.putString("pkg_app2",HomeFragment.Pkg1);
            editor.commit();
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.daily, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.daily) {
                Data();
        }
        return super.onOptionsItemSelected(item);
    }

    // View Holder used when displaying views
    static class AppViewHolder {
        ImageView pkgicon;
        TextView pkgName;
        TextView Date;
        TextView Grade;
    }
public class list extends BaseAdapter{
    ArrayList date,time,icon;
    int size;
        public list(ArrayList icon ,ArrayList date ,ArrayList time,int size){
            this.icon=icon;
            this.date=date;
            this.time=time;
            this.size=size;

        }
    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return rows.get(position);
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
            SolutionFragment.AppViewHolder holder;
            Drawable img = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.date_time, null);
            holder = new SolutionFragment.AppViewHolder();
            holder.pkgicon=(ImageView) convertView.findViewById(R.id.package_icon);
            holder.pkgName=(TextView) convertView.findViewById(R.id.package_date);
            holder.Date=(TextView) convertView.findViewById(R.id.time);
            holder.Grade=(TextView) convertView.findViewById(R.id.grade);
            convertView.setTag(holder);
        }else{
            holder = (SolutionFragment.AppViewHolder) convertView.getTag();
        }



        try {

            ApplicationInfo ai;
            final PackageManager pm = getContext().getApplicationContext().getPackageManager();

            for(int i=0;i<asplist.size();i++){
                ai=pm.getApplicationInfo(asplist.get(i).packageName,0);
               if(pm.getApplicationLabel(ai).equals(icon.get(position).toString())){
                   img=mPm.getApplicationIcon(asplist.get(i));
                   holder.pkgicon.setImageDrawable(img);
                   i=asplist.size();
               }
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



            holder.pkgName.setText(date.get(position).toString());
            holder.Date.setText(Time(Float.valueOf(time.get(position).toString())));

            Float grade=Float.valueOf(time.get(position).toString())*
                    1000;
            if(grade>=18000000){
                holder.Grade.setText("X");
                holder.Grade.setTextColor(Color.RED);
            }else{
                holder.Grade.setText("✓");
                holder.Grade.setTextColor(Color.GREEN);
            }

            return convertView;
    }
}

    public void Data(){
        myDialog.setContentView(R.layout.pop_daily);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        close=myDialog.findViewById(R.id.exit);
        left=myDialog.findViewById(R.id.left);
        right=myDialog.findViewById(R.id.right);
        Tdate=myDialog.findViewById(R.id.date);
        listView=myDialog.findViewById(R.id.list);
        arrayList.add("Sunday");
        arrayList.add("Monday");
        arrayList.add("Tuesday");
        arrayList.add("wensday");
        arrayList.add("Thersday");
        arrayList.add("Friday");
        arrayList.add("Saturday");
        temp=new ArrayList<>();
        Tdate.setText(arrayList.get(l));
        temp.addAll(db.all(l));

        rows=new ArrayList<>();
        for(int i =0;i<db.size(l);i++) {
            rows.add(i);
        }
            mAdapter = new SolutionFragment.list(db.icon(l), db.date(l), db.time(l), db.size(l));

        listView.setAdapter(mAdapter);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(l!=0){
                    l--;
                    Tdate.setText(arrayList.get(l));
                    if(temp.size()!=0){
                        temp.clear();
                    }
                    temp=db.all(l);
                    rows.clear();
                    for(int i =0;i<db.size(l);i++) {
                        rows.add(i);
                    }
                    mAdapter = new SolutionFragment.list(db.icon(l), db.date(l), db.time(l), db.size(l));
                    listView.setAdapter(mAdapter);
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(l!=6){
                    l++;
                    Tdate.setText(arrayList.get(l));
                    if(temp.size()!=0){
                        temp.clear();
                    }
                    temp=db.all(l);
                    rows.clear();
                    for(int i =0;i<db.size(l);i++) {
                        rows.add(i);
                    }
                    mAdapter = new SolutionFragment.list(db.icon(l), db.date(l), db.time(l), db.size(l));
                    listView.setAdapter(mAdapter);
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


    }
}