package com.shssjk.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ipcamera.demo.BridgeService;
import com.shssjk.activity.R;
import com.shssjk.activity.person.CameraListActivity;

import vstc2.nativecaller.NativeCaller;

public class TestCameraActivity extends Activity {
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Intent in = new Intent(TestCameraActivity.this, CameraListActivity.class);
            startActivity(in);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);
        Intent intent = new Intent();
        intent.setClass(TestCameraActivity.this, BridgeService.class);
        startService(intent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
                    Thread.sleep(3000);
                    Message msg = new Message();
                    mHandler.sendMessage(msg);
                } catch (Exception e) {

                }
            }
        }).start();

    }
}
