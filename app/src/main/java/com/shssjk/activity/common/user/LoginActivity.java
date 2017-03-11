package com.shssjk.activity.common.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shssjk.MainActivity;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPUserRequest;
import com.shssjk.model.SPUser;
import com.shssjk.utils.SPStringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    @Bind(R.id.edit_express_name)
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
    private SharedPreferences pref;
    private boolean isFristLogin = true;

    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.login_title));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        ButterKnife.bind(this);
        super.init();
    }

    @Override
    public void initSubViews() {
        titlbarFl = (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(mContext, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(mContext, R.color.white));

    }

    @Override
    public void initData() {
        if (getIntent() != null && getIntent().hasExtra("isFristLogin")) {
            isFristLogin = getIntent().getBooleanExtra("", false);
            String username =  getIntent().getStringExtra("username");
            editPhoneNum.setText(username);
        }
    }

    @Override
    public void initEvent() {
        pref = getSharedPreferences("com.shssjk.activity.push",
                MODE_PRIVATE);
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
        SharedPreferences.Editor editor = mContext.getSharedPreferences("com.shssjk.activity.user",
                MODE_PRIVATE).edit();
        editor.putString("user", editPhoneNum.getText().toString());
        editor.putString("pwd", editPassword.getText().toString());
        editor.commit();
        showLoadingToast("正在登录...");
        final String channalId = pref.getString("channalId", "");
        final String appId = pref.getString("appId", "");
        final String spikey = "yYcn0KjvpIte4HVs7qYczEQMbvGYkE98";
        final String deviceyppe = "android";
        SPUserRequest.doLogin(editPhoneNum.getText().toString(), editPassword.getText().toString(),
                channalId, appId, spikey, deviceyppe,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            SPSaveData.clearUser(getApplicationContext());
                            SPUser user = (SPUser) response;
                            MobileApplication.getInstance().setLoginUser(user);
//                            SPSaveData.saveLoginData(editPhoneNum.getText().toString(), editPassword.getText().toString(),
//                                    channalId, appId, spikey, deviceyppe);
                            SPSaveData.saveLoginData(mContext, editPhoneNum.getText().toString(), editPassword.getText().toString());
                            LoginActivity.this.sendBroadcast(new Intent(MobileConstants.ACTION_LOGIN_CHNAGE));
//                            startService(new Intent(LoginActivity.this, MyService.class));
                            showToast("登录成功");
                            if (isFristLogin) {
                                setResult(RESULT_OK);
                            } else {
//                               登录失效 再次登录
                                Intent intent = new Intent(mContext,
                                        MainActivity.class);
                                startActivity(intent);
                            }
                            finish();
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

    public void onRegisterClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void onForgetPwdClick(View view) {
        Intent intent = new Intent(this, ForgetPwdActivity.class);
        startActivityForResult(intent, 3);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3: //
                if (resultCode == Activity.RESULT_OK) {

                }
                break;
        }
    }
}
