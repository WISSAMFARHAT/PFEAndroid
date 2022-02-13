package com.example.time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="App.db";
    public static final String TABLE_NAME="table_app";
    public static final String COL_2="NAME";
    public static final String COL_3="CATEGORY";




    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME TEXT ,CATEGORY INTEGER)");
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
    public Boolean insertData(String name,int category){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,category);
        Long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }

    public boolean isEmpty(){
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<String> arrayList=new ArrayList<String>();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);

        if(res.moveToFirst()==true){
            return true;
        }else{

            return  false;
        }
    }

    public boolean updateData(String name,int category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,category);
        db.update(TABLE_NAME, contentValues, "name = ?",new String[] { name });
        return true;
    }

    public ArrayList select(int category) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery( "select NAME from "+TABLE_NAME+" where CATEGORY = "+category, null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("NAME")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList selectall(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery( "select NAME from "+TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("NAME")));
            res.moveToNext();
        }
        return array_list;
    }
}
