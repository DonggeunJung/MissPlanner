package com.topsan.missplanner;

import android.graphics.Point;

import java.util.Calendar;

/**
 * Created by Donggeun, Jung on 2018-06-19.
 * Copyright © 2018 Dennis Jung. All rights reserved.
 * Email : topsan72@gmail.com / Tel : 1-770-756-0073
 */

public class WeekInfo {
    public Calendar mDateStart = null;
    public Calendar mDateEnd = null;

    public WeekInfo() {
        initVariable();
    }

    public void initVariable() {
        Calendar dateToday = Calendar.getInstance();

        // Get Day of Week
        int dayOfWeek = dateToday.get(Calendar.DAY_OF_WEEK);
        // Start Day of Week
        mDateStart = Calendar.getInstance();
        mDateStart.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);

        setTime2Calendar();
    }

    public void movePrevWeek() {
        mDateStart.add(Calendar.DAY_OF_MONTH, -7);
        setTime2Calendar();
    }

    public void moveNextWeek() {
        mDateStart.add(Calendar.DAY_OF_MONTH, 7);
        setTime2Calendar();
    }

    public void setTime2Calendar() {
        mDateStart.set(Calendar.HOUR_OF_DAY, 0);
        mDateStart.set(Calendar.MINUTE, 0);
        mDateStart.set(Calendar.SECOND, 1);

        mDateEnd = (Calendar) mDateStart.clone();
        mDateEnd.add(Calendar.DAY_OF_MONTH, 6);
        mDateEnd.set(Calendar.HOUR_OF_DAY, 23);
        mDateEnd.set(Calendar.MINUTE, 59);
        mDateEnd.set(Calendar.SECOND, 59);
    }

    public Calendar getDateOfIndex(int nDayOfWeek) {
        Calendar date = (Calendar)mDateStart.clone();
        date.add(Calendar.DAY_OF_MONTH, nDayOfWeek);
        return date;
    }

    public static int getPosByItemAxis(int nDayOfWeek, int nHour) {
        int position = nDayOfWeek + 1 + (nHour) * 8;
        return position;
    }

    public static Point getAxisByItemPos(int position) {
        if( position % 8 == 0 ) return null;
        int nDayOfWeek = position % 8 - 1;
        int nHour = (position / 8);
        Point po = new Point(nDayOfWeek, nHour);
        return po;
    }

}
