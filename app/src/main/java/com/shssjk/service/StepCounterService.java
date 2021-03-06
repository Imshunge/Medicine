package com.shssjk.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.shssjk.activity.R;
import com.shssjk.activity.health.StepCounterActivity;
import com.shssjk.activity.health.StepDetector;
import com.shssjk.interfaces.StepCountListener;
import com.shssjk.model.health.Step;
import com.shssjk.utils.Logger;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StepCounterService extends Service {
    private final static String TAG = StepCounterService.class.getSimpleName();

    public static Boolean FLAG = false;// ������
    private SensorManager mSensorManager;// ����������
    private StepDetector detector;// ��������������
    private LocalBroadcastManager localBroadcastManager;
    private final static int GRAY_SERVICE_ID = -1001;
    private StepCountListener stepCountListener = new StepCountListener() {
        @Override
        public void onStept(int stept) {
            getNotificationManager().notify(1, getNotification("今日已跟我一起走了", stept));
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
        startForeground(1, getNotification("今日已跟我一共走", 0));
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        startTimeCount();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, StepCounterInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                steptemp = 0;
                if (steptemp != StepDetector.CURRENT_SETP) {
                    steptemp = StepDetector.CURRENT_SETP;
                    stepCountListener.onStept(steptemp);
                    Intent intent2 = new Intent("com.ssjk.stepchange.LocalReceiver");
                    localBroadcastManager.sendBroadcast(intent2);
                    isNewDay();
                }else{
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date now = new Date();
                    List<Step> stepList = DataSupport.select()
                            .where("time = ?", df.format(now)).find(Step.class);
                    if (stepList.size()>0&& stepList.get(0).getCount()!=0){
                        StepDetector.CURRENT_SETP =stepList.get(0).getCount();
                    }else if (stepList.size()==0 ){
                        clearSetp();
                    }
                }
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 300;
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent stepCounterServiceIntent = new Intent(this, StepCounterService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, stepCounterServiceIntent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
//  在运行onStartCommand后service进程被kill后，那将保留在开始状态，但是不保留那些传入的intent。
// 不久后service就会再次尝试重新创建，因为保留在开始状态，在创建
// service后将保证调用onstartCommand。如果没有传递任何开始命令给service，那将获取到null的intent。
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Intent sevice = new Intent(this, StepCounterService.class);
        this.startService(sevice);
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


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式3
        Date now = new Date();

//        List<Step> stepList = DataSupport.select()
//                .where("time = ?", df.format(now)).find(Step.class);
//        Logger.e("StepCounterService", stepList.size() + "");
        Step step = new Step();
        step.setTimestamp(Calendar.getInstance().getTimeInMillis() / 1000 + "");
        step.setCount(steptemp);
        step.setTime(df.format(now));
        step.saveOrUpdate("time=?", df.format(now));
        List<Step> stepList = DataSupport.select()
                .where("time = ?", df.format(now)).find(Step.class);
                Logger.e("StepCounterService", stepList.size() + "");
        isNewDay();
    }

    private void startTimeCount() {
        timeCount = new TimeCount(duration, 1000);
        timeCount.start();
    }

    private void clearSetp() {
        StepDetector.CURRENT_SETP = 0;
    }
    /**
     * 监听晚上0点变化初始化数据
     */
    private void isNewDay() {
        String time = "00:00";
        if (time.equals(new SimpleDateFormat("HH:mm").format(new Date()))) {
            StepDetector.CURRENT_SETP = 0;
        }
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class StepCounterInnerService extends Service {
        @Override
        public void onCreate() {
            Logger.e(TAG, "InnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Logger.e(TAG, "InnerService -> onStartCommand");
            startForeground(GRAY_SERVICE_ID, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            Logger.e(TAG, "InnerService -> onDestroy");
            super.onDestroy();
        }
    }

}
