package com.topsan.missplanner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Donggeun, Jung on 2018-06-19.
 * Copyright © 2018 Dennis Jung. All rights reserved.
 * Email : topsan72@gmail.com / Tel : 1-770-756-0073
 */

public class ServiceAlarmMngr extends Service {
    public static ServiceAlarmMngr self = null;
    protected App mApp = null;
    public static ArrayList<ScheduleData> mArSchedule = new ArrayList<ScheduleData>();
    final int ALARM_CHECK_INTERVER = 60 * 1000;

    static final int NOTI_ID = 1001;
    NotificationManager mNotiMgr;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    // Start Service Event
    public int onStartCommand(Intent intent, int flags, int startId){
        self = this;

        mApp = (App) getApplication();
        mApp.readAllRecords(mArSchedule);

        if( MainActivity.self != null ) {
            MainActivity.self.setAllRecords(mArSchedule);
        }

        // Get Alarm Manager
        mNotiMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mTimer_CheckAlarm.sendEmptyMessageDelayed(0, 3000);
        return Service.START_STICKY;
    }

    // Stop Service Event
    public void onDestroy() {
        Log.d("tag", "Stop Service");
    }

    Handler mTimer_CheckAlarm = new Handler() {
        public void handleMessage(Message msg) {
            checkSchedule();

            mTimer_CheckAlarm.sendEmptyMessageDelayed(0,ALARM_CHECK_INTERVER);
        }
    };

    public void checkSchedule() {
        if( mArSchedule == null )
            return;

        for( ScheduleData sd : mArSchedule ) {
            if( sd.alarmType == ScheduleData.ALARM_NONE )
                continue;
            // When Once Schedule
            if( sd.repeatType == ScheduleData.REPEAT_NONE ) {
                if( isScheduleSoon( sd.date ) ) {
                    startAlarm(sd);
                    return;
                }
            }
            // When repeat schedule
            else {
                Calendar dateThisWeek = Utils.getThisWeekDate( sd.date );
                if( isScheduleSoon( dateThisWeek ) ) {
                    startAlarm(sd);
                    return;
                }
            }
        }
    }

    public void startAlarm( ScheduleData sd ) {
        if( sd == null ) return;

        // Make movement
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(ServiceAlarmMngr.this, 0, intent, 0);

        // Make Alarm Object
        Notification noti = new Notification.Builder(this)
        // Set Icon
        .setSmallIcon(R.drawable.calendar_icon_gray)
        // Set Ticker message
        .setTicker("Schedule is On")
        // 타이틀 텍스트 지정
        .setContentTitle("Schedule" + sd.title)
        // Set message text
        .setContentText(sd.detail)
        // Set Noti run time
        .setWhen(System.currentTimeMillis())
        // Set Intent
        .setContentIntent(pIntent)
        // Make Noti Object
        .build();

        // Use Sound
        noti.defaults |= Notification.DEFAULT_SOUND;
        // When user select Noti, delete Noti.
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        // Start Notification
        mNotiMgr.notify(NOTI_ID, noti);
    }

    public boolean isScheduleSoon(Calendar dateItem) {
        Calendar dateNow = Calendar.getInstance();
        dateNow.add(Calendar.HOUR_OF_DAY, 1);
        long timeNow = dateNow.getTimeInMillis();
        long timeItem = dateItem.getTimeInMillis();

        if( timeNow <= timeItem && timeItem < (timeNow + ALARM_CHECK_INTERVER) ) {
            return true;
        }

        return false;
    }

}
