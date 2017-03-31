package com.shssjk.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.shssjk.activity.R;
import com.shssjk.activity.health.StepCounterActivity;
import com.shssjk.activity.health.StepDetector;
import com.shssjk.interfaces.StepCountListener;
import com.shssjk.model.Push;
import com.shssjk.model.health.Step;
import com.shssjk.utils.Logger;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.List;


public class StepCounterService extends Service {

    public static Boolean FLAG = false;// ������
    private SensorManager mSensorManager;// ����������
    private StepDetector detector;// ��������������
    private PowerManager mPowerManager;// �������
    private WakeLock mWakeLock;// ��
    private LocalBroadcastManager localBroadcastManager;

    private StepCountListener stepCountListener = new StepCountListener() {
        @Override
        public void onStept(int stept) {
            getNotificationManager().notify(1, getNotification("今日已跟我一共走", stept));
        }
    };
    private int steptemp;
    StepCpunterBinder stepCpunterBinder = new StepCpunterBinder();
    public class StepCpunterBinder extends Binder {
        public void startCount() {
        }
        public int getStepCpunt() {
            return steptemp;
        }
    }
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    private TimeCount timeCount;
    //默认为30秒进行一次存储
    private static int duration = 30000;
    @Override
    public IBinder onBind(Intent intent) {
        return stepCpunterBinder;
    }
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onCreate() {
//		加速度 TYPE_ACCELEROMETER
        super.onCreate();
        FLAG = true;// ������������
        detector = new StepDetector(this);
//		获取传感器管理器的实例
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
//		TYPE_ACCELEROMETER 3轴加速器
        mSensorManager.registerListener(detector,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
//        mPowerManager = (PowerManager) this
//                .getSystemService(Context.POWER_SERVICE);
//        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
//                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
//        mWakeLock.acquire();
        startForeground(1, getNotification("今日已跟我一共走", 0));
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        startTimeCount();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                steptemp = 0;
                if (steptemp != StepDetector.CURRENT_SETP) {
                    steptemp = StepDetector.CURRENT_SETP;
                    stepCountListener.onStept(steptemp);
                    Intent intent2 = new Intent("com.ssjk.stepchange.LocalReceiver");
                    localBroadcastManager.sendBroadcast(intent2);
                }
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 300;
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent stepCounterServiceIntent = new Intent(this, StepCounterService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, stepCounterServiceIntent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        FLAG = false;// �
        if (detector != null) {
            mSensorManager.unregisterListener(detector);
        }
    }
    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, StepCounterActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon));
        builder.setContentIntent(pi);
        builder.setColor(Color.parseColor("#EAA935"));
        builder.setContentTitle(title);
        builder.setContentText(progress + "步");
        return builder.build();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            timeCount.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    private void save() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Step step = new Step();
        step.setCount(steptemp);
        step.setTime(df.toString());
        step.saveOrUpdate("time=?", df.toString());
        step.saveOrUpdateAsync("time=?", df.toString()).listen(new SaveCallback() {
            @Override
            public void onFinish(boolean b) {
                List<Step> pushs = DataSupport.select()
                        .order("id desc")
                        .find(Step.class);
                for (Step push : pushs) {
                    Logger.e("MainActivity", push.getTime() + "");
                    Logger.e("MainActivity", push.getCount() + "");
                }
            }
        });
    }
    private void startTimeCount() {
        timeCount = new TimeCount(duration, 1000);
        timeCount.start();
    }
    private void clear(){
        StepDetector.CURRENT_SETP = 0;
    }
}
