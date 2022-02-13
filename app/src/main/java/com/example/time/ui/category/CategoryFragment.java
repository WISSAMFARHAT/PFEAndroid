package com.example.time.ui.category;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.time.DatabaseHelper;
import com.example.time.R;
import com.example.time.Service_Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends Fragment {

    private CategoryViewModel slideshowViewModel;
    public static List<ApplicationInfo> Gategory_Game = new ArrayList<>();
    public static List<ApplicationInfo> Gategory_News = new ArrayList<>();
    public static List<ApplicationInfo> Gategory_Productive = new ArrayList<>();
    public static List<ApplicationInfo> Gategory_SocialMedia = new ArrayList<>();
    public static List<ApplicationInfo> Gategory_Undifiend = new ArrayList<>();
    public List<ApplicationInfo> asplist=new ArrayList<>();
    Map<String,Integer> checklist=new HashMap<>();
    public ApplicationInfo appinfo;
    private ArrayAdapter aAdapter;
    Dialog myDialog;
    EditText Search;
    public  static int check=0;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseHelper myDb;


    public class list extends ArrayAdapter<ApplicationInfo> {

        private List<ApplicationInfo> appList = null;
        private Context context;
        private PackageManager packageManager;


        public list(@NonNull Context context, int resource, List<ApplicationInfo> objects) {
            super(context, resource, objects);
            packageManager = context.getPackageManager();
            this.context = context;
            this.appList= objects;

        }


        @Override
        public int getCount() {
            return ((null != appList) ? appList.size() : 0);
        }

        @Override
        public ApplicationInfo getItem(int position) {
            return ((null != appList) ? appList.get(position) : null);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = convertView;
            if (null == view) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.info, null);
            }

                ApplicationInfo data = appList.get(position);

                if (null != data) {


                        TextView name = (TextView) view.findViewById(R.id.name);
                        ImageView icon = (ImageView) view.findViewById(R.id.icon);
                        name.setText(data.loadLabel(packageManager));
                        icon.setImageDrawable(data.loadIcon(packageManager));



            }



            return view;


        }

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        getActivity().startService(new Intent(getContext(), Service_Block.class));
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.pop_category);
        slideshowViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        final Spinner spinner = root.findViewById(R.id.spinner);
        final ListView listView=root.findViewById(R.id.list);
        Search=root.findViewById(R.id.search);

        final String txt_search=Search.getText().toString();
        asplist.clear();
        swipeRefreshLayout=(SwipeRefreshLayout)root.findViewById(R.id.swipe);
        asplist=getContext().getPackageManager().getInstalledApplications(0);

//            aAdapter = new list(getContext(), 0, asplist);
//            listView.setAdapter(aAdapter);

            Search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(Search.getText().toString().equals("")){
                        aAdapter = new list(getContext(), 0, asplist);
                        listView.setAdapter(aAdapter);
                    }else{
                    CharSequence last =s.subSequence(Search.getText().length()-1,Search.getText().length());

                        if(last.toString().equals("\n")){

                            String text = Search.getText().toString();
                            Search.setText(text.substring(0, text.length() - 1));
                            Search.setSelection(Search.getText().length());
                        }else {

                           List<ApplicationInfo> copy = new ArrayList<>();
                            for (int i = 0; i < asplist.size(); i++) {
                                if (asplist.get(i).loadLabel(getContext().getPackageManager()).toString().indexOf(s.toString())==0) {
                                    copy.add(asplist.get(i));
                                }
                            }
                            aAdapter = new list(getContext(), 0, copy);
                            listView.setAdapter(aAdapter);
                        }
                        }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        myDb=new DatabaseHelper(getContext());
        boolean isEmpty=myDb.isEmpty();
        int result=0;

 //*************************************************************************************************************
 //if data nom emty
        if(isEmpty==true){
            ArrayList<String> arrayList=myDb.select(1);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=1;
                    }
                }

            }
            arrayList=myDb.select(5);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=5;
                    }
                }
            }

            arrayList=myDb.select(7);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=7;
                    }
                }
            }

            arrayList=myDb.select(4);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=4;
                    }
                }
            }
            arrayList=myDb.select(-1);
            for(int l=0;l<arrayList.size();l++){
                for(int m=0;m<asplist.size();m++) {
                    if (arrayList.get(l).equals(asplist.get(m).packageName)){
                        asplist.get(m).category=-1;
                    }
                }
            }


        }

//***************************************************************************************************************
// add application in arraylist (gane,news,productive,media,undifiend)

            for (int j = 0; j < asplist.size(); j++) {

                if (asplist.get(j).category == 0 || asplist.get(j).category == 1 || asplist.get(j).category == 2 || asplist.get(j).category == 3) {
                    for (int l = 0; l < Gategory_Game.size(); l++) {
                        if (Gategory_Game.get(l).equals(asplist.get(j).packageName)) {
                            result = 1;
                        }
                    }
                    if (result == 0) {
                        Gategory_Game.add(asplist.get(j));
                    } else {
                        result = 0;
                    }
                }
                if (asplist.get(j).category == 5 || asplist.get(j).category == 6) {
                    for (int l = 0; l < Gategory_News.size(); l++) {
                        if (Gategory_News.get(l).packageName.equals(asplist.get(j).packageName)) {
                            result = 1;
                        }
                    }
                    if (result == 0) {
                        Gategory_News.add(asplist.get(j));
                    } else {
                        result = 0;
                    }


                }
                if (asplist.get(j).category == 7) {

                    for (int l = 0; l < Gategory_Productive.size(); l++) {
                        if (Gategory_Productive.get(l).packageName.equals(asplist.get(j).packageName)) {
                            result = 1;
                        }
                    }
                    if (result == 0) {
                        Gategory_Productive.add(asplist.get(j));
                    } else {
                        result = 0;
                    }


                }
                if (asplist.get(j).category == 4) {

                    for (int l = 0; l < Gategory_SocialMedia.size(); l++) {
                        if (Gategory_SocialMedia.get(l).packageName.equals(asplist.get(j).packageName)) {
                            result = 1;
                        }
                    }
                    if (result == 0) {
                        Gategory_SocialMedia.add(asplist.get(j));
                    } else {
                        result = 0;
                    }


                }
                if (asplist.get(j).category == -1) {
                    for (int l = 0; l < Gategory_Undifiend.size(); l++) {
                        if (Gategory_Undifiend.get(l).packageName.equals(asplist.get(j).packageName)) {
                            result = 1;
                        }
                    }
                    if (result == 0) {
                        Gategory_Undifiend.add(asplist.get(j));
                    } else {
                        result = 0;
                    }


                }

            }

//        ArrayList<String> arrayList1=myDb.select(5);
//            for (int k=0;k<arrayList1.size();k++){
//                System.out.println("kk : "+arrayList1.get(k));
//            }
//****************************************************************************************************************
//un nouveau appliocation intaller
            int insert=0;

            ArrayList<String> arrayList=myDb.selectall();
            for(int k=0;k<asplist.size();k++){
                for(int m=0;m<arrayList.size();m++){
                    if(asplist.get(k).packageName.equals(arrayList.get(m))){
                        insert=1;
                    }
                }
                if(insert!=1){
                    myDb.insertData(asplist.get(k).packageName,asplist.get(k).category);
                }else{insert=0;}
            }
//****************************************************************************************************************
// if data is emty

        if(isEmpty==false){
            for(int l=0;l<Gategory_Game.size();l++){
                myDb.insertData(Gategory_Game.get(l).packageName,Gategory_Game.get(l).category);
            }
            for(int l=0;l<Gategory_News.size();l++){
                myDb.insertData(Gategory_News.get(l).packageName,Gategory_News.get(l).category);
            }
            for(int l=0;l<Gategory_Productive.size();l++){
                myDb.insertData(Gategory_Productive.get(l).packageName,Gategory_Productive.get(l).category);
            }
            for(int l=0;l<Gategory_SocialMedia.size();l++){
                myDb.insertData(Gategory_SocialMedia.get(l).packageName,Gategory_SocialMedia.get(l).category);
            }
            for(int l=0;l<Gategory_Undifiend.size();l++){
                myDb.insertData(Gategory_Undifiend.get(l).packageName,Gategory_Undifiend.get(l).category);
            }
        }
//*****************************************************************************************************************

        final CheckBox Box_Game=myDialog.findViewById(R.id.Games);
        final CheckBox Box_News=myDialog.findViewById(R.id.News);
        final CheckBox Box_Productive=myDialog.findViewById(R.id.Productive);
        final CheckBox Box_SocialMedia=myDialog.findViewById(R.id.SocialMedia);
        final CheckBox Box_Undifiend=myDialog.findViewById(R.id.Undifiend);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myDialog.show();
                Box_Game.setChecked(false);
                Box_News.setChecked(false);
                Box_Productive.setChecked(false);
                Box_SocialMedia.setChecked(false);
                Box_Undifiend.setChecked(false);
                appinfo= (ApplicationInfo) listView.getItemAtPosition(position);
                for(int i=0;i<Gategory_Game.size();i++) {
                    if (appinfo.packageName.equals(Gategory_Game.get(i).packageName) == true) {
                        Box_Game.setChecked(true);
                        Box_News.setChecked(false);
                        Box_Productive.setChecked(false);
                        Box_SocialMedia.setChecked(false);
                        Box_Undifiend.setChecked(false);
                        check=1;
                    }
                }
                for(int i=0;i<Gategory_News.size();i++) {
                    if (appinfo.packageName.equals(Gategory_News.get(i).packageName) == true) {
                        Box_Game.setChecked(false);
                        Box_News.setChecked(true);
                        Box_Productive.setChecked(false);
                        Box_SocialMedia.setChecked(false);
                        Box_Undifiend.setChecked(false);
                        check=2;
                    }
                }
                for(int i=0;i<Gategory_Productive.size();i++) {
                    if (appinfo.packageName.equals(Gategory_Productive.get(i).packageName) == true) {
                        Box_Game.setChecked(false);
                        Box_News.setChecked(false);
                        Box_Productive.setChecked(true);
                        Box_SocialMedia.setChecked(false);
                        Box_Undifiend.setChecked(false);
                        check=3;
                    }
                }
                for(int i=0;i<Gategory_SocialMedia.size();i++) {
                    if (appinfo.packageName.equals(Gategory_SocialMedia.get(i).packageName) == true) {
                        Box_Game.setChecked(false);
                        Box_News.setChecked(false);
                        Box_Productive.setChecked(false);
                        Box_SocialMedia.setChecked(true);
                        Box_Undifiend.setChecked(false);
                        check=4;
                    }
                }
                for(int i=0;i<Gategory_Undifiend.size();i++) {
                    if (appinfo.packageName.equals(Gategory_Undifiend.get(i).packageName) == true) {
                        Box_Game.setChecked(false);
                        Box_News.setChecked(false);
                        Box_Productive.setChecked(false);
                        Box_SocialMedia.setChecked(false);
                        Box_Undifiend.setChecked(true);
                        check=5;
                    }
                }


            }
        });



        TextView close=(TextView)myDialog.findViewById(R.id.close);
        Button save=myDialog.findViewById(R.id.save);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_category(v);
            }
        });


        Box_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box_Game.setChecked(true);
                Box_News.setChecked(false);
                Box_Productive.setChecked(false);
                Box_SocialMedia.setChecked(false);
                Box_Undifiend.setChecked(false);
            }
        });

        Box_News.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box_Game.setChecked(false);
                Box_News.setChecked(true);
                Box_Productive.setChecked(false);
                Box_SocialMedia.setChecked(false);
                Box_Undifiend.setChecked(false);
            }
        });

        Box_Productive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box_Game.setChecked(false);
                Box_News.setChecked(false);
                Box_Productive.setChecked(true);
                Box_SocialMedia.setChecked(false);
                Box_Undifiend.setChecked(false);
            }
        });

        Box_SocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box_Game.setChecked(false);
                Box_News.setChecked(false);
                Box_Productive.setChecked(false);
                Box_SocialMedia.setChecked(true);
                Box_Undifiend.setChecked(false);
            }
        });
        Box_Undifiend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Box_Game.setChecked(false);
                Box_News.setChecked(false);
                Box_Productive.setChecked(false);
                Box_SocialMedia.setChecked(false);
                Box_Undifiend.setChecked(true);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Box_Game.isChecked()==true){

                    if(check==1){

                    }else if(check==2){
                        for(int i=0;i<Gategory_News.size();i++) {
                            if(Gategory_News.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_News.remove(i);
                            }
                        }
                        Gategory_Game.add(appinfo);
                    }else if(check==3){
                        for(int i=0;i<Gategory_Productive.size();i++) {
                            if(Gategory_Productive.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_Productive.remove(i);
                            }
                        }
                        Gategory_Game.add(appinfo);
                    }else if(check==4){
                        for(int i=0;i<Gategory_SocialMedia.size();i++) {
                            if(Gategory_SocialMedia.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_SocialMedia.remove(i);
                            }
                        }
                        Gategory_Game.add(appinfo);
                    }else if(check==5){
                        for(int i=0;i<Gategory_Undifiend.size();i++) {
                            if(Gategory_Undifiend.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_Undifiend.remove(i);
                            }
                        }
                        Gategory_Game.add(appinfo);
                    }else {
                        Gategory_Game.add(appinfo);
                    }

                    myDb.updateData(appinfo.packageName,1);
                }else if(Box_News.isChecked()==true){

                    if(check==1){
                        for(int i=0;i<Gategory_Game.size();i++) {
                            if(appinfo.packageName.contains(Gategory_Game.get(i).packageName)==true){
                                Gategory_Game.remove(i);
                            }
                        }
                        Gategory_News.add(appinfo);
                    }else if(check==2){

                    }else if(check==3){
                        for(int i=0;i<Gategory_Productive.size();i++) {
                            if(Gategory_Productive.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_Productive.remove(i);
                            }
                        }
                        Gategory_News.add(appinfo);
                    }else if(check==4){
                        for(int i=0;i<Gategory_SocialMedia.size();i++) {
                            if(Gategory_SocialMedia.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_SocialMedia.remove(i);
                            }
                        }
                        Gategory_News.add(appinfo);
                    }else if(check==5){
                        for(int i=0;i<Gategory_Undifiend.size();i++) {
                            if(Gategory_Undifiend.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_Undifiend.remove(i);
                            }
                        }
                        Gategory_News.add(appinfo);
                    }else {
                        Gategory_News.add(appinfo);
                    }

                    myDb.updateData(appinfo.packageName,5);
                }else if(Box_Productive.isChecked()==true){

                    if(check==1){

                        for(int i=0;i<Gategory_Game.size();i++) {
                            if(Gategory_Game.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_Game.remove(i);
                            }
                        }
                        Gategory_Productive.add(appinfo);
                    }else if(check==2){
                        for(int i=0;i<Gategory_News.size();i++) {
                            if(Gategory_News.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_News.remove(i);
                            }
                        }
                        Gategory_Productive.add(appinfo);

                    }else if(check==3){

                    }else if(check==4){
                        for(int i=0;i<Gategory_SocialMedia.size();i++) {
                            if(Gategory_SocialMedia.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_SocialMedia.remove(i);
                            }
                        }
                        Gategory_Productive.add(appinfo);
                    }else if(check==5){
                        for(int i=0;i<Gategory_Undifiend.size();i++) {
                            if(Gategory_Undifiend.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_Undifiend.remove(i);
                            }
                        }
                        Gategory_Productive.add(appinfo);
                    }else {
                        Gategory_Productive.add(appinfo);
                    }

                    myDb.updateData(appinfo.packageName,7);
                }else if (Box_SocialMedia.isChecked()==true){

                    if(check==1){

                        for(int i=0;i<Gategory_Game.size();i++) {
                            if(Gategory_Game.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_Game.remove(i);
                            }
                        }
                        Gategory_SocialMedia.add(appinfo);
                    }else if(check==2){
                        for(int i=0;i<Gategory_News.size();i++) {
                            if(Gategory_News.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_News.remove(i);
                            }
                        }
                        Gategory_SocialMedia.add(appinfo);

                    }else if(check==3){

                        for(int i=0;i<Gategory_Productive.size();i++) {
                            if(Gategory_Productive.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_Productive.remove(i);
                            }
                        }
                        Gategory_SocialMedia.add(appinfo);
                    }else if(check==4){

                    }else if(check==5){
                        for(int i=0;i<Gategory_Undifiend.size();i++) {
                            if(Gategory_Undifiend.get(i).packageName.contains(appinfo.packageName)==true){
                                Gategory_Undifiend.remove(i);
                            }
                        }
                        Gategory_SocialMedia.add(appinfo);
                    }else {
                        Gategory_SocialMedia.add(appinfo);
                    }


                    myDb.updateData(appinfo.packageName,4);
                }else if(Box_Undifiend.isChecked()==true) {

                    if (check == 1) {

                        for (int i = 0; i < Gategory_Game.size(); i++) {
                            if (Gategory_Game.get(i).packageName.contains(appinfo.packageName) == true) {
                                Gategory_Game.remove(i);
                            }
                        }
                        Gategory_Undifiend.add(appinfo);
                    } else if (check == 2) {
                        for (int i = 0; i < Gategory_News.size(); i++) {
                            if (Gategory_News.get(i).packageName.contains(appinfo.packageName) == true) {
                                Gategory_News.remove(i);
                            }
                        }
                        Gategory_Undifiend.add(appinfo);

                    } else if (check == 3) {

                        for (int i = 0; i < Gategory_Productive.size(); i++) {
                            if (Gategory_Productive.get(i).packageName.contains(appinfo.packageName) == true) {
                                Gategory_Productive.remove(i);
                            }
                        }
                        Gategory_Undifiend.add(appinfo);
                    } else if (check == 4) {

                        for (int i = 0; i < Gategory_SocialMedia.size(); i++) {
                            if (Gategory_SocialMedia.get(i).packageName.contains(appinfo.packageName) == true) {
                                Gategory_SocialMedia.remove(i);
                            }
                        }
                        Gategory_Undifiend.add(appinfo);
                    } else if (check == 5) {

                    } else {
                        Gategory_Undifiend.add(appinfo);
                    }
                   myDb.updateData(appinfo.packageName,-1);
                }


                myDialog.dismiss();
            }




        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) {
                    Search.setText(null);
                        asplist = getContext().getPackageManager().getInstalledApplications(0);
                        aAdapter = new list(getContext(), 0, asplist);
                        listView.setAdapter(aAdapter);

                }
                if(position==1) {
                    Search.setText(null);
                        asplist = Gategory_Game;
                        aAdapter = new list(getContext(), 0, asplist);
                        listView.setAdapter(aAdapter);

                }
                if(position==2) {
                    Search.setText(null);
                        asplist = Gategory_News;
                        aAdapter = new list(getContext(), 0, asplist);
                        listView.setAdapter(aAdapter);

                }
                if(position==3) {
                    Search.setText(null);
                        asplist = Gategory_Productive;
                        aAdapter = new list(getContext(), 0, asplist);
                        listView.setAdapter(aAdapter);

                }
                if(position==4) {
                    Search.setText(null);
                        asplist = Gategory_SocialMedia;
                        aAdapter = new list(getContext(), 0, asplist);
                        listView.setAdapter(aAdapter);

                }
                if(position==5) {
                    Search.setText(null);
                        asplist = Gategory_Undifiend;
                        aAdapter = new list(getContext(), 0, asplist);
                        listView.setAdapter(aAdapter);

                }

        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        Search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            aAdapter.getFilter().filter(s);
////                if(spinner.getSelectedItem().toString().equals("All")){
////                    asplist = getContext().getPackageManager().getInstalledApplications(0);
////                    aAdapter = new list(getContext(), 0, asplist);
////                    listView.setAdapter(aAdapter);
////                    if(Search.getText().toString().equals("")) {
////
////                    }else{
//////                        asplist = getContext().getPackageManager().getInstalledApplications(0);
//////                        aAdapter = new list(getContext(), 0, asplist);
//////                        listView.setAdapter(aAdapter);
////                        aAdapter.getFilter().filter(s);
////                    }
////                }else if(spinner.getSelectedItem().toString().equals("Games")){
////                    if(Search.getText().toString().equals("")) {
////                        asplist = Gategory_Game;
////                        aAdapter = new list(getContext(), 0, asplist);
////                        listView.setAdapter(aAdapter);
////                    }else{
////                        listView.setAdapter(null);
////                    }
////                }else if(spinner.getSelectedItem().toString().equals("News")){
////                    if(Search.getText().toString().equals("")) {
////                        asplist = Gategory_News;
////                        aAdapter = new list(getContext(), 0, asplist);
////                        listView.setAdapter(aAdapter);
////                    }else{
////                        listView.setAdapter(null);
////                    }
////
////                }else if(spinner.getSelectedItem().toString().equals("Productive")){
////                    if(Search.getText().toString().equals("")) {
////                        asplist = Gategory_Productive;
////                        aAdapter = new list(getContext(), 0, asplist);
////                        listView.setAdapter(aAdapter);
////                    }else{
////                        listView.setAdapter(null);
////                    }
////
////                }else if(spinner.getSelectedItem().toString().equals("Social Media")){
////                    if(Search.getText().toString().equals("")) {
////                        asplist = Gategory_SocialMedia;
////                        aAdapter = new list(getContext(), 0, asplist);
////                        listView.setAdapter(aAdapter);
////                    }else{
////                        listView.setAdapter(null);
////                    }
////
////                }else{
////                    if(Search.getText().toString().equals("")) {
////                        asplist = Gategory_Undifiend;
////                        aAdapter = new list(getContext(), 0, asplist);
////                        listView.setAdapter(aAdapter);
////
////                    }else{
////                        listView.setAdapter(null);
////                    }
////                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Search.setText(null);
                if(spinner.getSelectedItem().toString().equals("All")){
                    asplist = getContext().getPackageManager().getInstalledApplications(0);
                    aAdapter = new list(getContext(), 0, asplist);
                    listView.setAdapter(aAdapter);
                }else if(spinner.getSelectedItem().toString().equals("Games")){
                    asplist = Gategory_Game;
                    aAdapter = new list(getContext(), 0, asplist);
                    listView.setAdapter(aAdapter);
                }else if(spinner.getSelectedItem().toString().equals("News")){
                    asplist = Gategory_News;
                    aAdapter = new list(getContext(), 0, asplist);
                    listView.setAdapter(aAdapter);

                }else if(spinner.getSelectedItem().toString().equals("Productive")){

                    asplist = Gategory_Productive;
                    aAdapter = new list(getContext(), 0, asplist);
                    listView.setAdapter(aAdapter);


                }else if(spinner.getSelectedItem().toString().equals("Social Media")){

                    asplist = Gategory_SocialMedia;
                    aAdapter = new list(getContext(), 0, asplist);
                    listView.setAdapter(aAdapter);

                }else{

                    asplist = Gategory_Undifiend;
                    aAdapter = new list(getContext(), 0, asplist);
                    listView.setAdapter(aAdapter);

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

        return root;
    }

    public void close_category(View v ){
        myDialog.dismiss();
    }

}