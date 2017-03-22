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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shssjk.activity.R;
import com.shssjk.activity.health.BindDeviceActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.utils.Logger;
import com.shssjk.view.MobileScrollLayout;

import java.util.ArrayList;

/**
 * 健康云
 */

public class HealthyFragment extends BaseFragment implements MobileScrollLayout.PageListener,
        View.OnClickListener {
    private Context mContext;
    public ViewPager mPager;
    private RadioGroup group_radio;
    private  boolean isFromBlood=false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        Logger.e(context,"onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("onCreate", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_healthy, null, false);
        super.init(view);
        Logger.e("", "onCreateView");
        return view;
    }

    @Override
    public void initSubView(View view) {
        view.findViewById(R.id.right_button).setOnClickListener(this);
        // 初始化ViewPage
        mPager = (ViewPager) view.findViewById(R.id.jky_viewpager);
        group_radio = (RadioGroup) view.findViewById(R.id.bottom_tools4);
    }

    @Override
    public void initEvent() {
        group_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_1:
                        mPager.setCurrentItem(0);// 选择某一页
//                        mPager.getCurrentItem()
//                        mPager.getCurrentItem()
                        isFromBlood=true;
                        //更新设备列表 血压计
                        if (mContext != null) {
                            mContext.sendBroadcast(new Intent(MobileConstants.ACTION_HEALTH_LOADATA));
                        }
                        break;
                    case R.id.radio_2:
                        isFromBlood=false;
                        mPager.setCurrentItem(1);
                        //更新设备列表 血糖仪
                        if (mContext != null) {
                        mContext.sendBroadcast(new Intent(MobileConstants.ACTION_HEALTH_SUAGR_LOADATA));
                    }
                        break;
                }
            }
        });
        mPager.setAdapter(new FragmentAdapter(getActivity().getSupportFragmentManager()));
        mPager.setPageMargin(0);// 设置视图之间的间隔
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                switch (index) {
                    case 0:
                        ((RadioButton) group_radio.findViewById(R.id.radio_1)).setChecked(true);//用此语句setOnCheckedChangeListener不会多次执行
                        break;
                    case 1:
                        ((RadioButton) group_radio.findViewById(R.id.radio_2)).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void initData() {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button:
                startupBindDeviceActivity();
                break;
            default:
                break;
        }
    }

    @Override
    public void page(int page) {

    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }

    // mFragments适配器
    class FragmentAdapter extends FragmentPagerAdapter {
        public ArrayList<BaseFragment> mFragments;
        @SuppressWarnings("serial")
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            // Auto-generated constructor stub
            mFragments = new ArrayList<BaseFragment>() {
                {
                    add(new FragmentBlood());
                    add(new FragmentSurger());
                }
            };
        }

        @Override
        public Fragment getItem(int index) {
            // Auto-generated method stub
            return mFragments.get(index);
        }

        @Override
        public int getCount() {
            // Auto-generated method stub
            return mFragments.size();
        }
    }
    boolean checkLogin() {
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return false;
        }
        return true;
    }
    /**
     * 绑定设备
     */
        public  void startupBindDeviceActivity(){
        if (!MobileApplication.getInstance().isLogined){
            showToastUnLogin();
            toLoginPage();
            return;
        }
        Intent carIntent = new Intent(getActivity() , BindDeviceActivity.class);
            carIntent.putExtra("isFromBlood",isFromBlood);
        getActivity().startActivity(carIntent);
    }
    @Override
    public void onResume() {
        super.onResume();
        Logger.e(mContext, "onResume");
    }
}
