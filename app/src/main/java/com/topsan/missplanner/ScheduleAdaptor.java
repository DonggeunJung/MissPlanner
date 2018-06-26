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
 * Created by Donggeun, Jung on 2018-06-19.
 * Copyright © 2018 Dennis Jung. All rights reserved.
 * Email : topsan72@gmail.com / Tel : 1-770-756-0073
 */

// Adapter class which connect GridView & Data Array
public class ScheduleAdaptor extends BaseAdapter {
    public static int CELL_HEIGHT = 0;

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<ScheduleData> mArSrc;
    int layout;

    // Constructor function - init member variable
    ScheduleAdaptor(Context context, int alayout, ArrayList<ScheduleData> aarSrc) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mArSrc = aarSrc;
        layout = alayout;
    }

    // Return item count
    public int getCount() {
        return mArSrc.size();
    }

    // Return Text data of special item
    public String getItem(int position) {
        return mArSrc.get(position).title;
    }

    // Return ID of special item
    public long getItemId(int position) {
        return position;
    }

    // Input data to inside controls of ListView items
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(layout, parent, false);
        }

        int backColor = Color.rgb(255, 196, 196);
        // Set background color
        if( pos % 8 == 0 ) {
            backColor = Color.rgb(255, 255, 222);
        }

        // Input Data to TextView
        TextView textView1 =
                (TextView)convertView.findViewById(R.id.text1);
        textView1.setText(mArSrc.get(pos).title);
        textView1.setBackgroundColor(backColor);

        if( CELL_HEIGHT < 1 )
            CELL_HEIGHT = convertView.getHeight();

        return convertView;
    }

}
