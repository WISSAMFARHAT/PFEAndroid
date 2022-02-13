package com.example.time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;

public class DatabaseEvent extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Aps.db";
    public static final String TABLE_NAME="Event";
    public static final String COL_1="Date";
    public static final String COL_2="Event";

    public DatabaseEvent(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(Date varchar(100) PRIMARY KEY  ,Event TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }


    public Boolean insertData(String date,String event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, event);
        contentValues.put(COL_1, date);
        Long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Integer deleteData (String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "Date = ?",new String[] {date});
    }

    public ArrayList<Event> selectall(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Event> array_list = new ArrayList<>();
        Cursor res = db.rawQuery( "select * from "+TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            array_list.add(new Event(Color.YELLOW,Long.parseLong(res.getString(res.getColumnIndex("Date")))
                    ,res.getString(res.getColumnIndex("Event"))));
            res.moveToNext();
        }
        return array_list;
    }

}
