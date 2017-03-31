package com.shssjk.activity.health;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.R;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.service.StepCounterService;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SSUtils;
import com.shssjk.utils.StepMenuDialog;
import com.shssjk.view.CircleBar;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;


public class StepCounterActivity extends BaseActivity {
    @Bind(R.id.circlestep)
    CircleBar circleBar;//进度显示
    @Bind(R.id.tv_calories)
    TextView tvCalories;
    @Bind(R.id.tv_mileage)
    TextView tvMileage;
    private StepCounterService.StepCpunterBinder stepCpunterBinder;
    private IntentFilter intentFilter;// 创建IntentFilter实例
    private Button menuBtn;
    //   本地广播
    private StepCountChangeLocalReceiver stepCountChangeLocalReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            stepCpunterBinder = (StepCounterService.StepCpunterBinder) service;
            circleBar.setProgress(stepCpunterBinder.getStepCpunt(), 1);
        }
    };
    private Double distance = 0.0;// 行程
    private Double calories = 0.0;// 卡路里热量
    private Double velocity = 0.0;// 速度
    private int step_length = 50;  //步长
    private int weight = 50;       //体重
    private int total_step = 0;   //当前步数
    private Context mContext;

    // 本地广播
    class StepCountChangeLocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            total_step = stepCpunterBinder.getStepCpunt();
            circleBar.setProgress(total_step, 1);
            countDistanceAndCalories(stepCpunterBinder.getStepCpunt());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String step = SPSaveData.getString(getBaseContext(), "goalStep");
        String heightStr = SPSaveData.getString(getBaseContext(), "height");
        String weightStr = SPSaveData.getString(getBaseContext(), "weight");
        Logger.e("height :weight ", "weight " + weight + " height:" + weightStr);
        if (!SSUtils.isEmpty(heightStr)) {
            step_length = SSUtils.getStepLenth(SSUtils.str2Int(heightStr));
        }
        if (!SSUtils.isEmpty(weightStr)) {
            weight = SSUtils.str2Int(weightStr);
        }
        if (SSUtils.isEmpty(step)) {
            step = "5000";
        }
        circleBar.setMax(SSUtils.str2Int(step));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "计步器", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        menuBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        menuBtn.setBackgroundResource(R.drawable.title_right_dot_selector);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StepMenuDialog dialog = new StepMenuDialog(mContext);
                Window win = dialog.getWindow();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.gravity = Gravity.TOP | Gravity.RIGHT;
//                当设置了 Gravity.LEFT 或 Gravity.RIGHT 之后，x值就表示到特定边的距离。
                params.x = 25;// 设置x坐标
                params.y = 125;// 设置y坐标
                win.setAttributes(params);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                TextView tv_exit = (TextView) win.findViewById(R.id.menu_person);
                TextView tv_close = (TextView) win.findViewById(R.id.menu_hostory);
                tv_exit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (!MobileApplication.getInstance().isLogined) {
                            showToastUnLogin();
                            toLoginPage();
                            dialog.dismiss();
                            return;
                        }
                        Intent intent = new Intent(mContext, PersonActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                tv_close.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (!MobileApplication.getInstance().isLogined) {
                            showToastUnLogin();
                            toLoginPage();
                            dialog.dismiss();
                            return;
                        }
                        Intent intent = new Intent(mContext, HistoryActivity.class);
                        intent.putExtra("calories", tvCalories.getText().toString());
                        intent.putExtra("distance", tvMileage.getText().toString());
                        intent.putExtra("count", total_step + "");
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void initData() {
        String height = SPSaveData.getString(getBaseContext(), "height");
        String weight = SPSaveData.getString(getBaseContext(), "weight");
        Logger.e("height :weight ", "weight " + weight + " height:" + height);
    }

    @Override
    public void initEvent() {
        circleBar = (CircleBar) findViewById(R.id.circlestep);
        circleBar.startCustomAnimation();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.ssjk.stepchange.LocalReceiver");
        stepCountChangeLocalReceiver = new StepCountChangeLocalReceiver();
//      注册本地广播监听器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(stepCountChangeLocalReceiver, intentFilter);
        //        开启服务
        Intent service = new Intent(StepCounterActivity.this, StepCounterService.class);
        startService(service);
        bindService(service, connection, BIND_AUTO_CREATE); // 绑定服务
    }

    /**
     * 计算行程
     */
    private void countDistanceAndCalories(int count) {
        distance = count * step_length * 0.01;//步长试试cm   *0.01换算成米
        // 计算消耗的卡路里热量
        calories = weight * distance * 0.001;
        tvCalories.setText(formatDouble(calories));
        tvMileage.setText(formatDouble(distance));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(stepCountChangeLocalReceiver);
        unbindService(connection);
    }

    public String formatDouble(Double doubles) {
        DecimalFormat format = new DecimalFormat("####.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals(getString(R.string.zero)) ? getString(R.string.double_zero)
                : distanceStr;
    }
}
