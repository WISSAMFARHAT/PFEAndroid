package com.example.time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Datagooltime extends SQLiteOpenHelper {



    public static final String DATABASE_NAME = "gooltime.db";
    public static final String TABLE_NAME = "gool";
    public static final String COL_1 = "Date";
    public static final String COL_2 = "Time_spend";
    public static final String COL_3 = "Best_app";
    public static final String COL_4 = "Best_dec";


    public Datagooltime(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(Date varchar(200) ,Time_spend Float , Best_app Text ,Best_dec TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
public boolean trouver(String date){
    SQLiteDatabase db = this.getReadableDatabase();
    Boolean trouver=false;
    Cursor res = db.rawQuery( "select Date from "+TABLE_NAME+" where Date = '"+date+"'", null );
    res.moveToFirst();
    if(res.moveToFirst()==true) {
        trouver=true;
    }
    return trouver;
}

    public Boolean insertData(String date ,Float time,String app,String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,date);
        contentValues.put(COL_2, time);
        contentValues.put(COL_3, app);
        contentValues.put(COL_4, description);
        Long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    public Integer deleteData (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "Best_dec = ?",new String[] {name});
    }

    public ArrayList select(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery( "select COUNT(Best_dec),Best_dec from "+TABLE_NAME+" GROUP BY Best_dec ORDER BY COUNT(Best_dec) DESC;", null );
        res.moveToFirst();
        if(res.isAfterLast() == false) {
            array_list.add(res.getString(1));
        }
        return array_list;
    }
    public int count(){
        int i=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select COUNT() from "+TABLE_NAME, null );
        res.moveToFirst();
        while (res.isAfterLast() == false){
            i=res.getInt(0);
            res.moveToNext();
        }
        return i;
    }
    public ArrayList selects(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery( "select Date from "+TABLE_NAME+" DESC LIMIT 7", null );
        res.moveToFirst();

            while (res.isAfterLast() == false) {
                array_list.add(res.getString(0));
                res.moveToNext();
            }

        return array_list;
    }
}

