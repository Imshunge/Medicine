package com.shssjk.activity.health;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.SPUser;
import com.shssjk.model.person.StepPersonInfo;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.GlideCircleTransform;
import com.wx.wheelview.widget.WheelViewDialog;
import com.wx.wheelview.widget.WheelViewDialog.OnDialogItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 计步器 个人中心
 */
public class PersonActivity extends BaseActivity {
    @Bind(R.id.head_mimgv)
    ImageView headMimgv;
    @Bind(R.id.sex_txtv)
    TextView sexTxtv;
    @Bind(R.id.age_txtv)
    TextView ageTxtv;
    SPUser mUser = null;
    @Bind(R.id.name_txtv)
    TextView nameTxtv;
    @Bind(R.id.hight_txtv)
    TextView hightTxtv;
    @Bind(R.id.weight_txtv)
    TextView weightTxtv;
    @Bind(R.id.goal_txtv)
    TextView goalTxtv;
    @Bind(R.id.line1)
    View line1;
    private Context mContext;
    private String sexArray[] = {"保密", "男", "女"};
    private WheelViewDialog dialog;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "个人中心");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        mContext = this;
        ButterKnife.bind(this);
        super.init();
    }
    @Override
    public void initSubViews() {
        backBtn = (Button) findViewById(R.id.titlebar_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void initData() {
        mUser = MobileApplication.getInstance().getLoginUser();
        showData(mUser);
        getStepUserInfo();
    }
    @Override
    public void initEvent() {
    }
    private void showData(SPUser mUser) {
        if (mUser != null) {
            String strSex = mUser.getSex();
            String strBirthday = mUser.getBirthday();
            if (!SSUtils.isEmpty(strBirthday)) {
                ageTxtv.setText(SSUtils.getAgeByStr(strBirthday));
            }
            if (!SSUtils.isEmpty(strSex)) {
                int index = SSUtils.str2Int(strSex);
                sexTxtv.setText(sexArray[index]);
            }
            if (!TextUtils.isEmpty(mUser.getNickname())) {
                nameTxtv.setText(mUser.getNickname());
            } else {
                if (SSUtils.isNumber(mUser.getMobile())) {
                    nameTxtv.setText(SSUtils.userNameReplaceWithStar(mUser.getMobile()));
                } else {
                    nameTxtv.setText("");
                }
            }
            if (MobileApplication.getInstance().isLogined) {
                String url ="";
                if(!SSUtils.isEmpty(mUser.getHeader_pic())){
                    url=  MobileConstants.BASE_HOST+ mUser.getHeader_pic();
                }else{
                    url=   mUser.getHeadPic();
                }
                Glide.with(this)
                        .load(url).transform(new GlideCircleTransform(mContext)).
                        into(headMimgv);
            } else {
                Glide.with(this)
                        .load(R.drawable.person_default_head).transform(new GlideCircleTransform(mContext)).
                        into(headMimgv);
            }
        }
    }

    @OnClick({R.id.hight_txtv, R.id.weight_txtv, R.id.goal_txtv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hight_txtv:
                dialog = new WheelViewDialog(mContext);
                dialog.setTitle("身高(cm)").setItems(createHeightArrays()).setButtonText("确定").setDialogStyle(Color
                        .parseColor("#6699ff")).setCount(5).show();
                dialog.setSelection(50);
                dialog.setOnDialogItemClickListener(new OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object s) {
                        hightTxtv.setText((String) s);
                        if (!SSUtils.isEmpty(s)) {
                            SPSaveData.putValue(mContext, "height", (String) s);
                            addStepUserInfo((String) s, "","");
                        }
                    }
                });
                break;
            case R.id.weight_txtv:
                dialog = new WheelViewDialog(mContext);
                dialog.setTitle("体重(Kg)").setItems(createWeightArrays()).setButtonText("确定").setDialogStyle(Color
                        .parseColor("#6699ff")).setCount(5).show();
                dialog.setSelection(20);
                dialog.setOnDialogItemClickListener(new OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object s) {
                        weightTxtv.setText((String) s);
                        SPSaveData.putValue(mContext, "weight", (String) s);
                        addStepUserInfo("",(String) s,"");
                    }
                });
                break;
            case R.id.goal_txtv:
                dialog = new WheelViewDialog(mContext);
                dialog.setTitle("目标步数").setItems(createStepArrays()).setButtonText("确定").setDialogStyle(Color
                        .parseColor("#6699ff")).setCount(5).show();
                dialog.setSelection(1);
                dialog.setOnDialogItemClickListener(new OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object s) {
                        goalTxtv.setText((String) s);
                        SPSaveData.putValue(mContext, "goalStep", (String) s);
                        addStepUserInfo("", "",(String) s);
                    }
                });
                break;
        }
    }

    private ArrayList<String> createHeightArrays() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 100; i < 250; i++) {
            list.add(i + "");
        }
        return list;
    }

    private ArrayList<String> createStepArrays() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 5000; i <= 50000; i = i + 1000) {
            list.add(i + "");
        }
        return list;
    }

    private ArrayList<String> createWeightArrays() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 30; i < 251; i++) {
            list.add(i + "");
        }
        return list;
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
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    private void setStepInfo(StepPersonInfo stepPersonInfo) {
        if (!SSUtils.isEmpty(stepPersonInfo.getHigh())) {
            hightTxtv.setText(stepPersonInfo.getHigh());
        }
        if (!SSUtils.isEmpty(stepPersonInfo.getWeight())) {
            weightTxtv.setText(stepPersonInfo.getWeight());
        }
        if (!SSUtils.isEmpty(stepPersonInfo.getTarget())) {
            goalTxtv.setText(stepPersonInfo.getTarget());
        }
    }

    public void addStepUserInfo(String high, String weight, String target) {
        PersonRequest.addUserSteptInfo(high, weight, target, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {

                } else {
                    showToast(msg);
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
