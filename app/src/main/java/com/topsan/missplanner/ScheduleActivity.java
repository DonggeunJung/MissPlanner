package com.topsan.missplanner;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ScheduleActivity extends AppCompatActivity {
    Button mBtnDel;
    EditText mEditTitle;
    EditText mEditDetail;
    CheckBox mCheckRepeat;
    CheckBox mCheckAlarm;
    TextView mTextDate;
    TimePicker mTimePicker;

    ScheduleData mSd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initVariable();
    }

    public void initVariable() {
        mBtnDel = (Button)findViewById(R.id.btnDel);
        mEditTitle = (EditText)findViewById(R.id.editTitle);
        mEditDetail = (EditText)findViewById(R.id.editDetail);
        mCheckRepeat = (CheckBox)findViewById(R.id.checkRepeat);
        mCheckAlarm = (CheckBox)findViewById(R.id.checkAlarm);
        mTimePicker = (TimePicker)findViewById(R.id.timePicker);
        mTextDate = (TextView)findViewById(R.id.textDate);

        ScheduleData sd = (ScheduleData)this.getIntent().getSerializableExtra(ScheduleData.SCHEDULE);
        if( sd == null ) return;
        mSd = sd.clone();

        mEditTitle.setText(mSd.title);
        if( mSd.title.length() > 0 ) {
            mBtnDel.setVisibility(View.VISIBLE);
        }

        mEditDetail.setText(mSd.detail);

        if( mSd.repeatType == ScheduleData.REPEAT_NONE )
            mCheckRepeat.setChecked(false);
        else
            mCheckRepeat.setChecked(true);

        if( mSd.alarmType == ScheduleData.ALARM_NONE )
            mCheckAlarm.setChecked(false);
        else
            mCheckAlarm.setChecked(true);

        int year = mSd.date.get(Calendar.YEAR);
        int month = mSd.date.get(Calendar.MONTH);
        int day = mSd.date.get(Calendar.DAY_OF_MONTH);
        mTextDate.setText( String.format("%04d/%02d/%02d", year, month+1, day) );

        int hour = mSd.date.get(Calendar.HOUR_OF_DAY);
        int min = mSd.date.get(Calendar.MINUTE);
        //int sec = mSd.date.get(Calendar.SECOND);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(min);
    }

    public void onBtnSave(View v) {
        String strTitle = mEditTitle.getText().toString();
        if( strTitle.length() < 1 ) {
            Toast.makeText(this, "Input Title text.", Toast.LENGTH_SHORT).show();
            return;
        }
        mSd.title = strTitle;
        mSd.detail = mEditDetail.getText().toString();

        mSd.repeatType = mCheckRepeat.isChecked() ? ScheduleData.REPEAT_EVERY_WEEK : ScheduleData.REPEAT_NONE;
        mSd.alarmType = mCheckAlarm.isChecked() ? ScheduleData.ALARM_USE : ScheduleData.ALARM_NONE;

        int hour = mTimePicker.getHour();
        int min = mTimePicker.getMinute();
        mSd.date.set(Calendar.HOUR_OF_DAY, hour);
        mSd.date.set(Calendar.MINUTE, min);

        // Send data to Main Activity when close sub Activity
        Intent intent = new Intent();
        // Add data to Intent
        intent.putExtra(BaseActivity.FINISH, BaseActivity.FINISH_SAVE);
        intent.putExtra(ScheduleData.SCHEDULE, mSd);
        // Regist Intent
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onBtnDel(View v) {
        // Send data to Main Activity when close sub Activity
        Intent intent = new Intent();
        // Add data to Intent
        intent.putExtra(BaseActivity.FINISH, BaseActivity.FINISH_DEL);
        intent.putExtra(ScheduleData.SCHEDULE, mSd);
        // Regist Intent
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onBtnCancel(View v) {
        // Send data to Main Activity when close sub Activity
        Intent intent = new Intent();
        // Add data to Intent
        intent.putExtra(BaseActivity.FINISH, BaseActivity.FINISH_CANCEL);
        // Regist Intent
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
