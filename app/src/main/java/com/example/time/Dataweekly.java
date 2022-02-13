package com.example.time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Dataweekly extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weekl.db";
    public static final String TABLE_NAME = "weekly";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Time_spend";
    public static final String COL_3 = "Type";
    public static final String COL_4 = "Best_app";
    public static final String COL_5 = "Date";


    public Dataweekly(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER ,Time_spend Float , Type INTEGER ,Best_app TEXT,Date TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }


    public Boolean insertData(int id ,Float time,String type,String name,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2, time);
        contentValues.put(COL_3, type);
        contentValues.put(COL_4, name);
        contentValues.put(COL_5, date);
        Long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    public Integer deleteData (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "Best_app = ?",new String[] {name});
    }

    public void delete(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

    public ArrayList selectall(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Float time_spend=0.0f;
        int m=0;
        String type="";
        String app="";
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery( "select * from "+TABLE_NAME+" where ID = "+id, null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            time_spend=time_spend+(res.getFloat(1));
            m++;
            res.moveToNext();
        }
        if(m!=0) {
            time_spend = time_spend / m;
        }
        res = db.rawQuery( "select COUNT(Best_app),Best_app from "+TABLE_NAME+" where ID = "+id +" GROUP BY Best_app ORDER BY COUNT(Best_app) DESC;", null );
        res.moveToFirst();
        if(res.isAfterLast() == false) {
            app=res.getString(1);
        }

        res = db.rawQuery( "select COUNT(Type),Type from "+TABLE_NAME+" where ID = "+id +" GROUP BY Type ORDER BY COUNT(Type) DESC;", null );
        res.moveToFirst();
        if(res.isAfterLast() == false) {
            type=res.getString(1);
        }

        array_list.add(0, String.valueOf(time_spend));
        array_list.add(1,app);
        array_list.add(2,type);
        return array_list;
    }

    public boolean date(String date){
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean trouver=false;
        Cursor res =db.rawQuery("select * from "+TABLE_NAME+" where Date = '"+date+"'",null);
        res.moveToFirst();
        if(res.moveToFirst()==true){
            trouver=true;
        }
        return  trouver;

    }
    public int size(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =db.rawQuery("select * from "+TABLE_NAME+" where ID = "+id+" ", null);
        res.moveToFirst();
        if(res.moveToFirst()==false){
            return 0;
        }else{
            return res.getCount();
        }
    }
    public ArrayList icon(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> temp =new ArrayList<>();
        Cursor res = db.rawQuery( "select Best_app from "+TABLE_NAME+" where ID = "+id+" ", null );
        res.moveToFirst();
        if(res.moveToFirst()==false){

        }else {
            while (res.isAfterLast() == false) {
                temp.add(res.getString(0));
                res.moveToNext();
            }
        }
            return temp;
    }

    public ArrayList date(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> temp =new ArrayList<>();
        Cursor res = db.rawQuery( "select Date from "+TABLE_NAME+" where ID = "+id+" ", null );
        res.moveToFirst();
        if(res.moveToFirst()==false){

        }else {
            while (res.isAfterLast() == false) {
                temp.add(res.getString(0));
                res.moveToNext();
            }
        }
        return temp;
    }

    public ArrayList time(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> temp =new ArrayList<>();
        Cursor res = db.rawQuery( "select Time_spend from "+TABLE_NAME+" where ID = "+id+" ", null );
        res.moveToFirst();
        if(res.moveToFirst()==false){

        }else {
            while (res.isAfterLast() == false) {
                temp.add(res.getString(0));
                res.moveToNext();
            }
        }
        return temp;
    }
    public ArrayList all(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String data="";
        ArrayList<String> temp =new ArrayList<>();
        Cursor res = db.rawQuery( "select Time_spend,Type,Best_app,Date from "+TABLE_NAME+" where ID = "+id+" ", null );
        res.moveToFirst();
    if(res.moveToFirst()==false){
        temp.add("You don't have any data to this day ");
    }else {
        while(res.isAfterLast() == false) {
        data = "Date:" + res.getString(3) + " , apps is : " + res.getString(2) + "\ncategory:" + res.getString(1) + ",Time spent:" + Time(res.getFloat(0));
        temp.add(data);
        data = "";
        res.moveToNext();
    }
    }
        return temp;
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

}
