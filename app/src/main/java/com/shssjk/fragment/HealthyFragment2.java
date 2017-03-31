package com.shssjk.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.health.BindDeviceActivity;
import com.shssjk.activity.health.BloodActivity;
import com.shssjk.activity.health.StepCounterActivity;
import com.shssjk.activity.health.SugarActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.utils.Logger;
import com.shssjk.view.MobileScrollLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 健康云
 */

public class HealthyFragment2 extends BaseFragment implements MobileScrollLayout.PageListener,
        View.OnClickListener {
    @Bind(R.id.titlebar_back_btn)
    Button titlebarBackBtn;
    @Bind(R.id.right_button)
    TextView rightButton;
    @Bind(R.id.bottom_tools4)
    TextView bottomTools4;
    @Bind(R.id.tv_blood)
    TextView tvBlood;
    @Bind(R.id.tv_sugar)
    TextView tvSugar;
    private Context mContext;
    public ViewPager mPager;
    private RadioGroup group_radio;
    private boolean isFromBlood = false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        Logger.e(context, "onAttach");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("onCreate", "onCreate");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_healthy2, null, false);
        super.init(view);
        Logger.e("", "onCreateView");
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void initSubView(View view) {
    }
    @Override
    public void initEvent() {
    }
    @Override
    public void initData() {
    }
    @Override
    public void page(int page) {
    }
    @Override
    public void gotoLoginPageClearUserDate() {
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    @OnClick({R.id.right_button,  R.id.tv_blood, R.id.tv_sugar,R.id.tv_stepcounter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_button:
                startupBindDeviceActivity();
                break;
            case R.id.tv_blood:
                Intent bloodActivityIntent =  new Intent(mContext, BloodActivity.class);
                startActivity(bloodActivityIntent);
                break;
            case R.id.tv_sugar:
                //更新设备列表 血糖仪
                Intent sugarActivityIntent =  new Intent(mContext, SugarActivity.class);
                startActivity(sugarActivityIntent);
                break;
            case R.id.tv_stepcounter:
                Intent stepCounterActivityIntent =  new Intent(mContext, StepCounterActivity.class);
                startActivity(stepCounterActivityIntent);
                break;
              }
    }
      /**
     * 绑定设备
     */
    public void startupBindDeviceActivity() {
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return;
        }
        Intent carIntent = new Intent(getActivity(), BindDeviceActivity.class);
        carIntent.putExtra("isFromBlood", isFromBlood);
        getActivity().startActivity(carIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e(mContext, "onResume");
    }
}
