package com.shssjk.activity.common.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPUserRequest;
import com.shssjk.model.SPUser;
import com.shssjk.utils.SPStringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.edit_password_old)
    EditText editPhoneNum;
    @Bind(R.id.edit_password)
    EditText editPassword;
    @Bind(R.id.wx_layout)
    LinearLayout wxLayout;
    @Bind(R.id.qq_icon_txt)
    TextView qqIconTxt;
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题
    Context mContext;
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.login_title));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        ButterKnife.bind(this);
        super.init();
    }

    @Override
    public void initSubViews() {
        titlbarFl= (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(mContext, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(mContext,R.color.white));
//        titleTxtv.setText(getString(R.string.login_title));

//        backBtn = (Button) findViewById(R.id.titlebar_back_btn);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    public void onLoginClick(View view) {
        if (SPStringUtils.isEditEmpty(editPhoneNum)) {
            editPhoneNum.setError(Html.fromHtml("<font color='red'>" + getString(R.string.login_phone_number_null) + "</font>"));
            return;
        }
        if (SPStringUtils.isEditEmpty(editPassword)) {
            editPassword.setError(Html.fromHtml("<font color='red'>" + getString(R.string.login_password_null) + "</font>"));
            return;
        }
        showLoadingToast("正在登录...");

        SPUserRequest.doLogin(editPhoneNum.getText().toString(), editPassword.getText().toString(),
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            SPUser user = (SPUser) response;
                            MobileApplication.getInstance().setLoginUser(user);
                            LoginActivity.this.sendBroadcast(new Intent(MobileConstants.ACTION_LOGIN_CHNAGE));
                            showToast("登录成功");
                            loginSuccess();
                        }
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });

    }

    private void loginSuccess() {
//        Intent intent = new Intent();
//        intent.setClass(this, MainActivity.class);
//        startActivity(intent);
        finish();
    }

    public void onRegisterClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void onForgetPwdClick(View view) {
        startActivity(new Intent(this, ForgetPwdActivity.class));
    }
    @OnClick({R.id.wx_layout, R.id.qq_icon_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wx_layout:
                break;
            case R.id.qq_icon_txt:
                break;
        }
    }
}
