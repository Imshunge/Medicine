package com.shssjk.service;

import java.util.Timer;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPUserRequest;
import com.shssjk.model.SPUser;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SSUtils;

/**
 * 自动登录
 * 1.打开程序开始运行
 * 2.登陆过后开始运行
 * 3.取消登陆 停止服务
 */
public class MyService extends Service {

    public static final String TAG = "MyService";

    private MyBinder mBinder = new MyBinder();
    Timer timer = new Timer();
    private SharedPreferences pref;
    private String username;
    private String pass;
    private String channalId;
    private String appId;
    private String spikey;
    private String deviceyppe;
//	其中onCreate()方法会在服务创建的时候调用，
//	onStartCommand()方法会在每次服务启动的时候调用，
//	onDestroy()方法会在服务销毁的时候调用。
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e(TAG, "onCreate() executed");

    }
    private void getUserInfo() {
//        SharedPreferences userInfo = getSharedPreferences(
//                "com.shssjk.activity.user", MODE_PRIVATE);
        pref = getSharedPreferences("com.shssjk.activity.push",
                MODE_PRIVATE);
//        username = userInfo.getString("user", "");
//        pass = userInfo.getString("pwd", "");
        channalId = pref.getString("channalId", "");
        appId = pref.getString("appId", "");
        spikey = "yYcn0KjvpIte4HVs7qYczEQMbvGYkE98";
        deviceyppe = "android";
        username= SPSaveData.getString(getBaseContext(),"username");
        pass=SPSaveData.getString(getBaseContext(),"pwd");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.e(TAG, "onStartCommand() executed");
//		SharedPreferences userInfo = getSharedPreferences(
//				"com.shssjk.activity.user", MODE_PRIVATE);
//		pref = getSharedPreferences("com.shssjk.activity.push",
//				MODE_PRIVATE);
//		final String username = userInfo.getString("user", "");
//		final String pass = userInfo.getString("pwd", "");
//		final String channalId = pref.getString("channalId", "");
//		final String appId = pref.getString("appId", "");
//		final String spikey="yYcn0KjvpIte4HVs7qYczEQMbvGYkE98";
//		final String deviceyppe= "android";
        new Thread(new Runnable() {
            @SuppressWarnings({"deprecation", "static-access"})
            public void run() {
//				ThreadClient client = new ThreadClient();
//				client.setContext(MyService.this);
                while (true) {
                    Message message = new Message();
                    message.what = 1;
//					ClientResult result = client.getJson(Urls.USER.INFO
//							+ "user/" + username + "/pwd/" + pass, TYPE.POST,
//							null);
                    handler.sendMessage(message);// 发送消息
					doLogin(username, pass, channalId, appId, spikey, deviceyppe);
                    try {
                        Thread.sleep(10000);// 10秒更新一次 为了 石头  积分  优惠券 的及时更新，单位毫秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Logger.e("MyService", "InterruptedException");
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void doLogin(String username, String pass, String channalId, String appId, String spikey, String deviceyppe) {
        SPUserRequest.doLogin(username, pass,
                channalId, appId, spikey, deviceyppe,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        if (response != null) {
                            SPUser user = (SPUser) response;
                            MobileApplication.getInstance().setLoginUser(user);
                            Logger.e("MyService", "success" + user.getNickname());
                            Logger.e("MyService  getToken", "success"+user.getToken());
                        }
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        Logger.e("MyService", "FailuredListener");
                    }
                });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            if (msg.what == 0) {
            }
            // 登录
            if (msg.what == 1) {
                getUserInfo();
                if(!SSUtils.isEmpty(username)) {
                    doLogin(username, pass, channalId, appId, spikey, deviceyppe);
                    Logger.e(TAG, username);
                }
                Logger.e(TAG,username);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e(TAG, "onDestroy() executed");
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class MyBinder extends Binder {

        public void startDownload() {
            Logger.e("TAG", "startDownload() executed");
            // 执行具体的下载任务
        }

    }

}