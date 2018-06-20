package com.topsan.missplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JDG on 2018-06-19.
 */

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "Planner", null, 1);
    }

    // Runned once when Application start
    public void onCreate(SQLiteDatabase db) {
        // Create Table
        db.execSQL("create table Schedule (" +
                "_id integer PRIMARY KEY autoincrement, " +
                "title TEXT, detail TEXT, date TEXT, repeatType integer, alarmType integer);");
        }

    // DataBase Upgrade event method
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                         int newVersion) {
        // Delete Table & Create again
        db.execSQL("drop table if exists Student");
        onCreate(db);
    }

}
