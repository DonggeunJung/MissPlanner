package com.topsan.missplanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
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

public class MainActivity extends BaseActivity {
    public static MainActivity self = null;

    ArrayList<ScheduleData> mArGrid = new ArrayList<ScheduleData>();
    ArrayList<ScheduleData> mArSchedule = null; //new ArrayList<ScheduleData>();
    //ArrayList<ScheduleData> mArSchedule = new ArrayList<ScheduleData>();
    GridView mGridWeek;
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
    }

    public void setAllRecords(ArrayList<ScheduleData> arSchedule) {
        mArSchedule = arSchedule;
        resetArraySchedule(false);
        initGridView();
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

    public void onGridItemSelected(Point selItem) {
        //mSelItem = selItem;
        int position = WeekInfo.getPosByItemAxis(selItem.x, selItem.y);
        ScheduleData sd = mArGrid.get(position);

        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra(ScheduleData.SCHEDULE, sd);

        // Start Sub Activity & Request result
        startActivityForResult(intent, ACT_SCHEDULE);
        //startActivity(intent);
    }

    public void onClickActionBar(View v) {
        switch ( v.getId() ) {
            case R.id.btnLeft :
                mWeekInfo.movePrevWeek();
                //initArraySchedule(true);
                resetArraySchedule(true);
                break;
            case R.id.btnRIght :
                mWeekInfo.moveNextWeek();
                //initArraySchedule(true);
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
            ScheduleAdaptor adaptor = (ScheduleAdaptor) mGridWeek.getAdapter();
            adaptor.notifyDataSetChanged();
        }
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

                // 1st Line - Show Day of Week
                /*if (j > 0 && i == 0) {
                    //Calendar date = mWeekInfo.getDateOfIndex(j-1);
                    cellText = DoWName[j - 1] + "\n" + (date.get(Calendar.MONTH) + 1) + "." + date.get(Calendar.DAY_OF_MONTH);
                }*/
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
        //mArSchedule = mApp.readAllRecords();

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
                            //initArraySchedule(true);
                            resetArraySchedule(true);
                            break;
                        }
                        case BaseActivity.FINISH_SAVE: {
                            if (sd == null)
                                return;
                            Log.d("tag", "onActivityResult()-1 ");
                            int index = getIndexOfScheduleByDbId(sd.dbId);
                            Log.d("tag", "onActivityResult()-2 " + index);
                            // New Schedule - Add
                            if (index == -1) {
                                mApp.addDB(sd);
                                Log.d("tag", "onActivityResult()-3 " + mArSchedule.size());
                                //mArSchedule.add(sd);
                                mApp.refreshScheduleDataList();
                                Log.d("tag", "onActivityResult()-4 " + mArSchedule.size());
                                //sd.Tag = mArSchedule.size() - 1;
                            }
                            // Existing Schedule - Update
                            else {
                                // Update on record from DataBase
                                mApp.updateDB(sd);
                                Log.d("tag", "onActivityResult()-5 " + mArSchedule.size());
                                mArSchedule.set(index, sd);
                                Log.d("tag", "onActivityResult()-6 " + mArSchedule.size());
                            }
                            //initArraySchedule(true);
                            resetArraySchedule(true);
                            break;
                        }
                    }
                }
                break;
        }
    }

}
