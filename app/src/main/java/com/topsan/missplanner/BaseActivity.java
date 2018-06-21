package com.topsan.missplanner;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JDG on 2018-06-08.
 * Copyright © 2018 Dennis Jung. All rights reserved.
 * Email : topsan72@gmail.com / Tel : 1-770-756-0073
 */

public class BaseActivity extends AppCompatActivity {

    final static int ACT_MAIN = 0;
    final static int ACT_SCHEDULE = 1;

    protected static final String FINISH = "Finish";
    protected static final int FINISH_CANCEL = 0;
    protected static final int FINISH_SAVE = 1;
    protected static final int FINISH_DEL = 2;

    protected App mApp = null;          // Application Class instance
    protected ActionBar mActionBar;         // ActionBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init member variable
        initBaseVariable();
    }

    // init member variable
    public void initBaseVariable() {
        // get Application class instance
        mApp = (App) getApplication();

        // Init ActionBar
        mActionBar = getSupportActionBar();
    }

    // loading image from local storage
    protected Bitmap loadThumbnamlFromLocal(String imageId) {
        // get file name from url address
        imageId = Utils.filterFileName(imageId);

        // add extension to file name
        if( imageId.indexOf(".") < 0 )
            imageId += ".jpg";
        // get storage path
        String filePath = this.getFilesDir() + "/" + imageId;

        // Open file
        File file = new File( filePath );
        // if image file not exist, return
        if( file.exists() == false ) {
            return null;
        }
        // load image file and change to Bitmap
        Bitmap bmp = Utils.loadImage(filePath, 1);
        return bmp;
    }

    // Set Custom layout to ActionBar
    public void setActioBar_CustomLayout(int layoutId, int backColorId) {
        try {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            // Set layout to ActionBar
            View mCustomView = LayoutInflater.from(this).inflate(layoutId, null);
            mActionBar.setCustomView(mCustomView);
            //Remove margin from ActionBar End
            Toolbar parent = (Toolbar) mCustomView.getParent();
            parent.setContentInsetsAbsolute(0, 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void onClickActionBar(View v) {
    }

}
