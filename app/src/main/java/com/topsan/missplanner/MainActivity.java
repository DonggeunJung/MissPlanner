package com.topsan.missplanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Donggeun, Jung on 2018-06-19.
 * Copyright © 2018 Dennis Jung. All rights reserved.
 * Email : topsan72@gmail.com / Tel : 1-770-756-0073
 */

public class MainActivity extends BaseActivity {
    public static MainActivity self = null;

    ArrayList<ScheduleData> mArGrid = new ArrayList<ScheduleData>();
    ArrayList<ScheduleData> mArSchedule = null;

    GridView mGridWeek = null;
    WeekInfo mWeekInfo;
    TextView mTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;

        // Set Custom layout to ActionBar
        setActioBar_CustomLayout(R.layout.bar_button_title_button, Color.WHITE);

        initVariable();

        if( ServiceAlarmMngr.self != null ) {
            setAllRecords( ServiceAlarmMngr.self.mArSchedule );
        }
        //mTimer_InitGrid.sendEmptyMessageDelayed(0, 500);
    }

    public void setAllRecords(ArrayList<ScheduleData> arSchedule) {
        mArSchedule = arSchedule;
        resetArraySchedule(false);
        initGridView();
    }

    public void onGridItemSelected(Point selItem) {
        int position = WeekInfo.getPosByItemAxis(selItem.x, selItem.y);
        ScheduleData sd = mArGrid.get(position);

        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra(ScheduleData.SCHEDULE, sd);

        // Start Sub Activity & Request result
        startActivityForResult(intent, ACT_SCHEDULE);
    }

    public void onClickActionBar(View v) {
        switch ( v.getId() ) {
            case R.id.btnLeft :
                mWeekInfo.movePrevWeek();
                resetArraySchedule(true);
                break;
            case R.id.btnRight :
                mWeekInfo.moveNextWeek();
                resetArraySchedule(true);
                break;
        }
    }

    public int getIndexOfScheduleByDbId(int dbId) {
        for(int i=0; i < mArSchedule.size(); i++) {
            ScheduleData sd = mArSchedule.get(i);
            if( sd.dbId == dbId )
                return i;
        }
        return -1;
    }

    protected void resetArraySchedule(boolean bRefresh) {
        initArraySchedule();

        // Apply Schedule data to Grid Data
        applySchedule2GridData();

        showDate2ActionBar();
        // Show Day of week title
        showDayTitle();

        if( bRefresh ) {
            refreshGridView();
        }
    }

    public boolean refreshGridView() {
        if( mGridWeek == null )
            return false;
        ScheduleAdaptor adaptor = (ScheduleAdaptor) mGridWeek.getAdapter();
        if( adaptor == null )
            return false;
        adaptor.notifyDataSetChanged();
        return true;
    }

    protected void initArraySchedule() {
        // Make ArrayList Item and save to ArrayList
        int hour = 0;
        boolean bAM = true;
        mArGrid.clear();

        //for (int i = 0; i < (24 + 1); i++) {
        for (int i = 0; i < 24; i++) {
            int k = i;
            if (k == 0) {
                hour = 12;
            } else if (k > 12)
                hour = k % 12;
            else
                hour = k;

            if (k >= 12)
                bAM = false;
            String strAmPm = (bAM) ? "AM" : "PM";
            String[] DoWName = {"Sun", "Mon", "Tue", "Wed", "Tur", "Fri", "Sat"};

            for (int j = 0; j < 8; j++) {
                String cellText = "";
                Calendar date = Calendar.getInstance();
                // Not 1st Column
                if (j > 0) {
                    date = mWeekInfo.getDateOfIndex(j - 1);
                }

                // 1st Column - Show Hour
                if (j == 0 && k >= 0) {
                    cellText = hour + "\n" + strAmPm;
                }
                // Enable select cell
                else if (k >= 0) {
                    date.set(Calendar.HOUR_OF_DAY, k);
                    date.set(Calendar.MINUTE, 0);
                    date.set(Calendar.SECOND, 0);
                }

                ScheduleData sd = new ScheduleData(cellText, "", date, 0, 0);
                mArGrid.add(sd);
            }
        }
    }

    // Apply Schedule data to Grid Data
    public void applySchedule2GridData() {
        //for (ScheduleData sd : mArSchedule) {
        int aa=0;
        for(int i=0; i < mArSchedule.size(); i++) {
            ScheduleData sd = mArSchedule.get(i);
            int nDayOfWeek = sd.date.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            int nHour = sd.date.get(Calendar.HOUR_OF_DAY);

            // When Schedule does not repeat & The date is out of range, ignore this item.
            if( sd.repeatType == ScheduleData.REPEAT_NONE ) {
                if( sd.date.getTimeInMillis() < mWeekInfo.mDateStart.getTimeInMillis() ||
                        sd.date.getTimeInMillis() > mWeekInfo.mDateEnd.getTimeInMillis() ) {
                    continue;
                }
            }
            int position = WeekInfo.getPosByItemAxis(nDayOfWeek, nHour);
            ScheduleData gridSD = sd.clone();
            //gridSD.Tag = i;
            mArGrid.set(position, gridSD);
        }
    }

    public void initVariable() {
        mGridWeek = (GridView) findViewById(R.id.gridWeek);
        mTextTitle = (TextView) findViewById(R.id.textTitle);

        mWeekInfo = new WeekInfo();
    }

    public void showDate2ActionBar() {
        int year = mWeekInfo.mDateStart.get(Calendar.YEAR);
        int month = mWeekInfo.mDateStart.get(Calendar.MONTH) + 1;
        int day1 = mWeekInfo.mDateStart.get(Calendar.DAY_OF_MONTH);
        int day2 = mWeekInfo.mDateEnd.get(Calendar.DAY_OF_MONTH);
        String strTitle = String.format("%04d.%02d.%02d - %02d", year, month, day1, day2);
        mTextTitle.setText(strTitle);
    }

    // Show Day of week title
    public void showDayTitle() {
        int[] textViewId = { R.id.textColTitle1, R.id.textColTitle2, R.id.textColTitle3, R.id.textColTitle4, R.id.textColTitle5, R.id.textColTitle6, R.id.textColTitle7 };
        String[] dayOfWeekTitle = { "Sun", "Mon", "Tue", "Wed", "Tur", "Fir", "Sat" };
        Calendar date = (Calendar)mWeekInfo.mDateStart.clone();

        for(int i=0; i < 7; i++) {
            String strTitle = dayOfWeekTitle[i] + "\n" + (date.get(Calendar.MONTH)+1) + "." + date.get(Calendar.DAY_OF_MONTH);
            TextView textView = (TextView)findViewById( textViewId[i] );
            textView.setText(strTitle);
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    // Sub Activity close event method
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACT_SCHEDULE :
                // When Result is OK
                if (resultCode == Activity.RESULT_OK) {
                    int finishType = data.getIntExtra(BaseActivity.FINISH, BaseActivity.FINISH_CANCEL);
                    ScheduleData sd = (ScheduleData)data.getSerializableExtra(ScheduleData.SCHEDULE);

                    switch (finishType) {
                        case BaseActivity.FINISH_CANCEL:
                            return;
                        case BaseActivity.FINISH_DEL: {
                            //if( sd == null || sd.Tag == -1 )
                            if (sd == null)
                                return;
                            // Delete one record from DataBase
                            mApp.delDB(sd.dbId);
                            int index = getIndexOfScheduleByDbId(sd.dbId);
                            if (index >= 0)
                                mArSchedule.remove(index);
                            resetArraySchedule(true);
                            break;
                        }
                        case BaseActivity.FINISH_SAVE: {
                            if (sd == null)
                                return;
                            int index = getIndexOfScheduleByDbId(sd.dbId);
                            // New Schedule - Add
                            if (index == -1) {
                                mApp.addDB(sd);
                                mApp.refreshScheduleDataList();
                            }
                            // Existing Schedule - Update
                            else {
                                // Update on record from DataBase
                                mApp.updateDB(sd);
                                mArSchedule.set(index, sd);
                            }
                            resetArraySchedule(true);
                            break;
                        }
                    }
                }
                break;
        }
    }

    protected void onResume() {
        super.onResume();
        scrollGridView( 7 );
    }

    public void initGridView() {
        ScheduleAdaptor Adapter = new ScheduleAdaptor(this, R.layout.schedule_item, mArGrid);
        mGridWeek.setAdapter(Adapter);

        mGridWeek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Point selItem = WeekInfo.getAxisByItemPos(position);
                if (selItem == null)
                    return;
                onGridItemSelected(selItem);
            }
        });


    }

    public boolean scrollGridView(int nRowCount) {
        int nScrollY = 0;
        View v = mGridWeek.getChildAt(0);
        if(v != null)
            nScrollY = (int) v.getTop();
        else
            nScrollY = 0;

        //mGridWeek.scrollTo(0, nRowCount * 30);
        if( ScheduleAdaptor.CELL_HEIGHT > 0 ) {
            mGridWeek.scrollTo(0, nRowCount * ScheduleAdaptor.CELL_HEIGHT);
            //mGridWeek.setSelectionFromTop(nRowCount, nScrollY);
        }
        else {
            return false;
        }

        return refreshGridView();
    }

    Handler mTimer_InitGrid = new Handler() {
        public void handleMessage(Message msg) {
            boolean bSuccess = scrollGridView( 7 );

            if( bSuccess == false )
                mTimer_InitGrid.sendEmptyMessageDelayed(0, 50);
        }
    };

}
