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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.R;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.SPUser;
import com.shssjk.model.health.Step;
import com.shssjk.model.person.StepPersonInfo;
import com.shssjk.service.StepCounterService;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SSUtils;
import com.shssjk.utils.StepMenuDialog;
import com.shssjk.view.CircleBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 计步器
 */
public class StepCounterActivity extends BaseActivity {
    @Bind(R.id.circlestep)
    CircleBar circleBar;//进度显示
    @Bind(R.id.tv_calories)
    TextView tvCalories;
    @Bind(R.id.tv_mileage)
    TextView tvMileage;
    @Bind(R.id.tv_hint_setting)
    TextView tvHintSetting;
    private StepCounterService.StepCpunterBinder stepCpunterBinder;
    private IntentFilter intentFilter;// 创建IntentFilter实例
    private Button menuBtn;
    private int CALCALORIES = 2;   //计算卡路里
    private boolean isShowCalcalories = false;   //气泡提示是否显示卡路里
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

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    private String lastTime; //最后上传的时间


    // 本地广播
    class StepCountChangeLocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(!SSUtils.isEmpty(stepCpunterBinder.getStepCpunt())){
                total_step = stepCpunterBinder.getStepCpunt();
                circleBar.setProgress(total_step, 1);
                countDistanceAndCalories(stepCpunterBinder.getStepCpunt());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MobileApplication.getInstance().isLogined) {
            getStepUserInfo();
        } else {

        }
    }
    private void setStepInfo(String step, String heightStr, String weightStr) {
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
        if (MobileApplication.getInstance().isLogined) {
            SPUser user = MobileApplication.getInstance().getLoginUser();
//            getStepUserInfo();
            addStepData(user.getUserID());
        }
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


        tvHintSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MobileApplication.getInstance().isLogined) {
                    showToastUnLogin();
                    toLoginPage();
                    return;
                }
                Intent intent = new Intent(mContext, PersonActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 计算行程
     */
    private void countDistanceAndCalories(int count) {
        distance = count * step_length * 0.01 * 0.001;//步长试试cm    公里
        // 计算消耗的卡路里热量
        calories = weight * distance  ;
        tvCalories.setText(formatDouble(calories));
        tvMileage.setText(formatDouble(distance));
        setHintStr(calories + "", 2);
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

    public void getStepUserInfo() {
        PersonRequest.getUserSteptInfo(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    StepPersonInfo stepPersonInfo = (StepPersonInfo) response;
                    setStepInfo(stepPersonInfo);
                } else {
                    showToast(msg);
                }
                countDistanceAndCalories(stepCpunterBinder.getStepCpunt());
                tvHintSetting.setEnabled(false);
                isShowCalcalories = true;
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                if (!SSUtils.isEmpty(errorCode) && errorCode == 1) {
                    setHintStr(msg, errorCode);
                    isShowCalcalories = false;
                }
            }
        });
    }

    /**
     * 设置气泡提示
     *
     * @param msg
     * @param errorCode
     */
    private void setHintStr(String msg, int errorCode) {
        if (1 == errorCode) {
            tvHintSetting.setEnabled(true);
            tvHintSetting.setText("点击设置身高体重，获取消耗的卡路里");
        } else if (errorCode == CALCALORIES) {
            if (isShowCalcalories) {
                tvHintSetting.setEnabled(false);
                tvHintSetting.setText(getString(R.string.stepcount, formatDouble(calories)));
            }
        }
    }

    private void setStepInfo(StepPersonInfo stepPersonInfo) {
        if (!SSUtils.isEmpty(stepPersonInfo.getHigh())) {
            step_length = SSUtils.getStepLenth(SSUtils.str2Int(stepPersonInfo.getHigh()));
        }
        if (!SSUtils.isEmpty(stepPersonInfo.getWeight())) {
            weight = SSUtils.str2Int(stepPersonInfo.getWeight());
        }
        if (SSUtils.isEmpty(stepPersonInfo.getTarget())) {
            stepPersonInfo.setTarget("5000");
        }
        circleBar.setMax(SSUtils.str2Int(stepPersonInfo.getTarget()));
    }

    private void addStepData(final String userID) {
        PersonRequest.getLastRecord(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    JSONObject lastRecoerd = (JSONObject) response;
                    try {
                        setLastTime(SSUtils.TimeStamp2Date(lastRecoerd.getString("data"), "yyyy-MM-dd"));
                        addStep2(userID, 2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    };
                }
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
//                showToast(msg);
                if(!SSUtils.isEmpty(errorCode)&&2==errorCode){
                    addStep2(userID, 1);
                }
            }
        });



    }

    private void addStep2(String userID, int type) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        List<Step> stepList = DataSupport.select()
                .where("time = ?", df.format(now)).find(Step.class);
        //添加计步明细
        int stepListSize = stepList.size();
        long differLocal = 0;
        long differService = 0;
        try {
            if(type==2){
                differLocal = SSUtils.getDistanceDays(stepList.get(stepListSize-1).getTime(), getLastTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int intdifferLocal = new Long(differLocal).intValue();

        RequestParams params = new RequestParams();
        if(intdifferLocal==0){
            Step stepTemp = stepList.get(stepListSize-1);
            String step = "arr[" + 0 + "][step]";
            params.put(step, stepTemp.getCount());
            String date = "arr[" + 0 + "][date]";
            params.put(date, stepTemp.getTimestamp());
            String userId = "arr[" + 0 + "][user_id]";
            params.put(userId, userID);
        }else{
            for (int i = 0; i <((intdifferLocal >stepListSize)? stepListSize : intdifferLocal); i++) {
                Step stepTemp = stepList.get(stepListSize-1-i);
                String step = "arr[" + i + "][step]";
                params.put(step, stepTemp.getCount());
                String date = "arr[" + i + "][date]";
                params.put(date, stepTemp.getTimestamp());
                String userId = "arr[" + i + "][user_id]";
                params.put(userId, userID);
            }
        }

        PersonRequest.addStepRecord(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
//                    JSONObject lastRecoerd = (JSONObject) response;
                }
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }


}
