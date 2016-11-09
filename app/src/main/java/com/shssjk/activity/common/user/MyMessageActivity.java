package com.shssjk.activity.common.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;

/**
 * 消息通知
 */
public class MyMessageActivity extends BaseActivity {

    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.setting_message));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        super.init();
    }

    @Override
    public void initSubViews() {
        //        标题内容
        titlbarFl = (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
