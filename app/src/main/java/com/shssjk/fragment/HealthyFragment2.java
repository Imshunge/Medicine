package com.shssjk.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.R;
import com.shssjk.activity.health.BindDeviceActivity;
import com.shssjk.activity.health.BloodActivity;
import com.shssjk.activity.health.StepCounterActivity;
import com.shssjk.activity.health.SugarActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.health.HealthRequest;
import com.shssjk.model.health.SuagrTongJi;
import com.shssjk.utils.Logger;
import com.shssjk.view.MobileScrollLayout;

import org.json.JSONObject;

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
//    @Bind(R.id.health_banner_imgv)
    ImageView healthBannerImgv;
    @Bind(R.id.tv_stepcounter)
    TextView tvStepcounter;
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
        healthBannerImgv = (ImageView) view.findViewById(R.id.health_banner_imgv);
        // 获取屏幕像素
        DisplayMetrics display = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(display);
        LinearLayout.LayoutParams para = null;
        para = (LinearLayout.LayoutParams) healthBannerImgv.getLayoutParams();
//        Log.d(TAG, "layout height0: " + para.height);
//        Log.d(TAG, "layout width0: " + para.width);

        para.width  = display.widthPixels;
        para.height = display.widthPixels * 3 / 5;
        healthBannerImgv.setLayoutParams(para);

    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        getBannerImg();
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

    @OnClick({R.id.right_button , R.id.rl_sugar,R.id.rl_blood, R.id.rl_stepcounter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_button:
                startupBindDeviceActivity();
                break;
            case R.id.rl_blood:
                Intent bloodActivityIntent = new Intent(mContext, BloodActivity.class);
                startActivity(bloodActivityIntent);
                break;
            case R.id.rl_sugar:
                //更新设备列表 血糖仪
                Intent sugarActivityIntent = new Intent(mContext, SugarActivity.class);
                startActivity(sugarActivityIntent);
                break;
            case R.id.rl_stepcounter:
                Intent stepCounterActivityIntent = new Intent(mContext, StepCounterActivity.class);
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


    private void getBannerImg() {
        showLoadingToast("正在加载数据");
        HealthRequest.getHealthBanner(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    String resultJson = (String) response;
                    showBannerImg(resultJson);
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                //可见时才显示提示
//                if (!isFast) {
//                    showToast(msg);
//                }
                Logger.e(this, "getSugarTongJi " + msg + "");
            }
        });
    }

    /**
     * 显示banner图
     * @param resultJson
     */
    private void showBannerImg(String resultJson) {
        Glide.with(mContext).load(resultJson).placeholder(R.drawable.product_default).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).into(healthBannerImgv);
    }


}
