package com.topsan.missplanner;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by JDG on 2018-06-08.
 * Copyright © 2018 Dennis Jung. All rights reserved.
 * Email : topsan72@gmail.com / Tel : 1-770-756-0073
 */

public class App extends Application {

    DbHelper mDbHelper;
    SQLiteDatabase mDb;
    // Alarm manager
    AlarmManager mAlarmMgr;

    // App start event function
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("tag", "Start MissPlanner App");
        // init mamber variable
        initVariable();

        startService();
    }

    public void startService() {
        stopService();
        Intent intent = new Intent(this, ServiceAlarmMngr.class);
        startService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(this, ServiceAlarmMngr.class);
        stopService(intent);
    }

    // App close event function
    @Override
    public void onTerminate() {
        stopService();
        super.onTerminate();
    }

    // init mamber variable
    protected void initVariable() {

        Utils.mApp = this;

        // Create DBHelper
        mDbHelper = new DbHelper(this);
        mDb = mDbHelper.getWritableDatabase();

        mAlarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);

    }

    // Add new record to DataBase
    public void addDB(ScheduleData sd) {
        // Make Query command by Schedule Data
        String strQuery = "insert into Schedule(title, detail, date, repeatType, alarmType) values (" +
                "'" + sd.title + "', '" + sd.detail + "', '" + sd.getDateString() + "', "
                + sd.repeatType + ", " + sd.alarmType + ");";
        // Add new record
        mDb.execSQL(strQuery);
    }

    // Delete one record from DataBase
    public void delDB(int dbId) {
        mDb.execSQL("delete from Schedule where _id = " + dbId);
    }

    // Update on record from DataBase
    public void updateDB(ScheduleData sd) {
        // Make Query command
        String strQuery = "update Schedule set title = '" + sd.title +
                "', detail = '" + sd.detail + "', date = '" + sd.getDateString() +
                "', repeatType = " + sd.repeatType + ", alarmType = " + sd.alarmType +
                " where _id = " + sd.dbId;
        // Run DB Query command
        mDb.execSQL(strQuery);
    }


    // Read all record from DataBase
    public void readAllRecords(ArrayList<ScheduleData> arSchedule) {

        // DB read
        String strQuery = "select _id, title, detail, date, repeatType, alarmType from Schedule";
        Cursor mCursor = mDb.rawQuery(strQuery, null);

        // Repeat loop as many as Record count
        for(int i=0; i < mCursor.getCount(); i++) {
            // Move Cursor to next reocrd
            mCursor.moveToNext();
            // Get ID of Record
            int dbId = mCursor.getInt(0);
            // Get 'title' data
            String title = mCursor.getString(1);
            // Get 'detail' data
            String detail = mCursor.getString(2);
            // Get 'date' data
            String date = mCursor.getString(3);
            // Get 'repeatType' data
            int repeatType = mCursor.getInt(4);
            // Get 'alarmType' data
            int alarmType = mCursor.getInt(5);

            ScheduleData sd = new ScheduleData(dbId, title, detail, date, repeatType, alarmType);
            arSchedule.add(sd);
        }

    }

    // Read Schedule Records from DataBase
    public void refreshScheduleDataList() {
        if( ServiceAlarmMngr.self == null ) return;
        ServiceAlarmMngr.self.mArSchedule.clear();

        readAllRecords(ServiceAlarmMngr.self.mArSchedule);
    }

}
