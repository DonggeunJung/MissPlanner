package com.topsan.missplanner;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by Donggeun, Jung on 2018-06-21.
 * Copyright © 2018 Dennis Jung. All rights reserved.
 * Email : topsan72@gmail.com / Tel : 1-770-756-0073
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //ActionBar abar = getActionBar();
        //abar.hide();
        removeStatusBar(true);

        mTimer_RunMainActivity.sendEmptyMessageDelayed(0, 2000);
    }

    Handler mTimer_RunMainActivity = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

}
