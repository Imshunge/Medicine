package com.shssjk.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;


public class StepCounterService extends Service {

	public static Boolean FLAG = false;// ������

	private SensorManager mSensorManager;// ����������
	private StepDetector detector;// ��������������

	private PowerManager mPowerManager;// �������
	private WakeLock mWakeLock;// ��

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@Override
	public void onCreate() {
//		加速度 TYPE_ACCELEROMETER
		// TODO Auto-generated method stub
		super.onCreate();
		FLAG = true;// ������������
		detector = new StepDetector(this);
//		获取传感器管理器的实例
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
//		TYPE_ACCELEROMETER 3轴加速器
		mSensorManager.registerListener(detector,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);
		mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
		mWakeLock.acquire();
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
		if (mWakeLock != null) {
			mWakeLock.release();
		}
	}

}
