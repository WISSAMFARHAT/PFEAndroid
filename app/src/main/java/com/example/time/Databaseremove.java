package com.example.time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Databaseremove extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Appe.db";
    public static final String TABLE_NAME="WhatshList";
    public static final String COL_1="ID";
    public static final String COL_2="NAME";




    public Databaseremove(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }
    public void droptable(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertData(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(COL_2,name);
        Long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }

    public Integer deleteData (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "NAME = ?",new String[] {name});
    }

    public  void deleteall(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+TABLE_NAME);
    }

    public ArrayList selectall(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery( "select * from "+TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("NAME")));
            res.moveToNext();
        }
        return array_list;
    }
}
