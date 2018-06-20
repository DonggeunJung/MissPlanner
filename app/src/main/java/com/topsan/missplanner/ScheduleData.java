package com.topsan.missplanner;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JDG on 2018-06-18.
 * Data of Schedule
 */

public class ScheduleData implements Serializable {
    public static final String SCHEDULE = "Schedule";
    public static final String TAG = "Tag";
    // Repeat Type
    public static final int REPEAT_NONE = 0;
    public static final int REPEAT_EVERY_DAY = 1;
    public static final int REPEAT_EVERY_WEEKDAY = 2;
    public static final int REPEAT_EVERY_WEEK = 3;

    // Alarm Type
    public static final int ALARM_NONE = 0;
    public static final int ALARM_USE = 1;

    public int dbId = -1;
    public String title = "";
    public String detail = "";
    public Calendar date = null;
    public int repeatType = REPEAT_NONE;
    public int alarmType = ALARM_NONE;
    //public int Tag = -1;

    public ScheduleData() {

    }

    public ScheduleData(String title, String detail, Calendar date, int repeatType, int alarmType) {
        this.title = title;
        this.detail = detail;
        this.date = date;
        this.repeatType = repeatType;
        this.alarmType = alarmType;
    }

    public ScheduleData(String title, String detail, String strDate, int repeatType, int alarmType) {
        this(title, detail, ScheduleData.string2Date(strDate), repeatType, alarmType);
    }

    public ScheduleData(int dbId, String title, String detail, String strDate, int repeatType, int alarmType) {
        this(title, detail, ScheduleData.string2Date(strDate), repeatType, alarmType);
        this.dbId = dbId;
    }

    public ScheduleData clone() {
        Calendar cal = (Calendar)this.date.clone();
        ScheduleData sd = new ScheduleData(this.title, this.detail, cal, this.repeatType, this.alarmType);
        sd.dbId = this.dbId;
        return sd;
    }

    public String getDateString() {
        String strDate = String.format("%04d%02d%02d%02d%02d%02d",
                date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), date.get(Calendar.SECOND));
        return strDate;
    }

    public void setDate(String strDate) {
        date = string2Date(strDate);
    }

    public static Calendar string2Date(String strDate) {
        if( strDate == null || strDate.length() < 14 )
            return null;
        Calendar cal = Calendar.getInstance();
        int value = Integer.parseInt( strDate.substring(0, 4) );
        cal.set(Calendar.YEAR, value);
        strDate = strDate.substring(4);

        value = Integer.parseInt( strDate.substring(0, 2) );
        cal.set(Calendar.MONTH, value);
        strDate = strDate.substring(2);

        value = Integer.parseInt( strDate.substring(0, 2) );
        cal.set(Calendar.DAY_OF_MONTH, value);
        strDate = strDate.substring(2);

        value = Integer.parseInt( strDate.substring(0, 2) );
        cal.set(Calendar.HOUR_OF_DAY, value);
        strDate = strDate.substring(2);

        value = Integer.parseInt( strDate.substring(0, 2) );
        cal.set(Calendar.MINUTE, value);
        strDate = strDate.substring(2);

        value = Integer.parseInt( strDate );
        cal.set(Calendar.SECOND, value);
        return cal;
    }

}
