package com.topsan.missplanner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JDG on 2018-06-18.
 */

// GridView 와 데이터 배열을 연결해주는 커스텀 어댑터 클래스를 정의
public class ScheduleAdaptor extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<ScheduleData> mArSrc;
    int layout;

    // 생성자 함수에서 멤버변수 초기화
    ScheduleAdaptor(Context context, int alayout, ArrayList<ScheduleData> aarSrc) {
        //ScheduleAdaptor(Context context, ArrayList<ScheduleData> aarSrc) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mArSrc = aarSrc;
        layout = alayout;
    }

    // 항목 개수를 반환
    public int getCount() {
        return mArSrc.size();
    }

    // 특정 항목의 텍스트 데이터를 반환
    public String getItem(int position) {
        return mArSrc.get(position).title;
    }

    // 특정 항목의 ID 를 반환
    public long getItemId(int position) {
        return position;
    }

    // ListView 아이템 내부 각각의 엘리먼트에 데이터를 입력
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(layout, parent, false);
        }

        int backColor = Color.rgb(255, 196, 196);
        // Set background color
        //if( pos < 8 || pos % 8 == 0 ) {
        if( pos % 8 == 0 ) {
            backColor = Color.rgb(255, 255, 222);
        }

        // Input Data to TextView
        TextView textView1 =
                (TextView)convertView.findViewById(R.id.text1);
        textView1.setText(mArSrc.get(pos).title);
        textView1.setBackgroundColor(backColor);

        return convertView;
    }

}
