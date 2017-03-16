package com.shssjk.activity.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPUserRequest;
import com.soubao.tpshop.utils.SPStringUtils;

/**
 * 找回密码
 */
public class ForgetPwdActivity extends BaseActivity {

    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题

    RelativeLayout layoutFirst;

    RelativeLayout layoutSecond;

    RelativeLayout layoutThird;


    EditText editPhoneNumber;

    Button btnNext1;

    TextView txtInfo;

    EditText editCode;

    EditText editPwd;

    EditText editRePwd;

    Button btnNext2;


    Button btnSendSMS;

    TextView txtErrorInfo;

    String phoneNumber;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.login_forget_pwd));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        super.init();
    }

    @Override
    public void initSubViews() {
        //标题
        titlbarFl= (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this,R.color.white));


        layoutFirst = (RelativeLayout) findViewById(R.id.layout_first);

        layoutSecond = (RelativeLayout) findViewById(R.id.layout_second);

        layoutThird = (RelativeLayout) findViewById(R.id.layout_third);


        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNum);

        btnNext1 = (Button) findViewById(R.id.btn_next_1);

        txtInfo = (TextView) findViewById(R.id.txt_register_phone);

        editCode = (EditText) findViewById(R.id.edit_code);

        editPwd = (EditText) findViewById(R.id.edit_password);

        editRePwd = (EditText) findViewById(R.id.edit_re_password);

        btnNext2 = (Button) findViewById(R.id.btn_next_2);

        btnSendSMS = (Button) findViewById(R.id.btn_send_sms);

        txtErrorInfo = (TextView) findViewById(R.id.txt_error_info);


    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }


    public void onBtnReCodeClick(View view){
        //send SMS
        SPUserRequest.sendSMSRegCode(phoneNumber, new SPSuccessListener(){
            @Override
            public void onRespone(String msg, Object response) {
                //if (response != null) {
                showToast(msg);
                //}
            }
        },new SPFailuredListener(){
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
        btnSendSMS.setEnabled(false);
        countDownTimer.start();
    }

    /**
     * First Next click
     * @param view
     */
    public void onBtnNextOneClick(View view){
        //check input
        if (isEditEmpty(editPhoneNumber)) {
            editPhoneNumber.setError(Html.fromHtml("<font color='red'>" + getString(R.string.register_phone_number_null) + "</font>"));
            return;
        }
        phoneNumber= editPhoneNumber.getText().toString();
        //send SMS
        SPUserRequest.sendSMSRegCode(phoneNumber, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                //if (response != null) {
                showToast(msg);
                //}
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });

        layoutFirst.setVisibility(View.GONE);
        layoutSecond.setVisibility(View.VISIBLE);
        layoutThird.setVisibility(View.GONE);
        txtInfo.setText(getResources().getString(R.string.register_sub_title, phoneNumber));
        btnSendSMS.setEnabled(false);
        countDownTimer.start();
    }

    public void onBtnNextTowClick(View view){
        if (SPStringUtils.isEditEmpty(editCode)) {
            editCode.setError(Html.fromHtml("<font color='red'>"+getString(R.string.edit_code_null)+"</font>"));
            return;
        }
        layoutFirst.setVisibility(View.GONE);
        layoutSecond.setVisibility(View.GONE);
        layoutThird.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
    }

    public void onBtnNextThreeClick(View view){
        String pwd = editPwd.getText().toString();
        if (isEditEmpty(editPwd)) {
            editPwd.setError(Html.fromHtml("<font color='red'>"+getString(R.string.register_password_null)+"</font>"));
            return;
        }
        if (pwd.length()< 6 || pwd.length() > 20) {
            txtErrorInfo.setText(getString(R.string.register_error_forgetpwd_info));
            txtErrorInfo.setVisibility(View.VISIBLE);
            return;
        }
        //找回密码
        SPUserRequest.doRetrievePassword(phoneNumber, pwd, editCode.getText().toString(),
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        if (response != null) {
                            showToast(msg);
//                            startActivity(new Intent(ForgetPwdActivity.this, LoginActivity.class));
                            ForgetPwdActivity.this.setResult(RESULT_OK);
                            finish();
                        }
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                        //txtErrorInfo.setText("注册成功");
                        txtErrorInfo.setText(msg);
                        txtErrorInfo.setVisibility(View.VISIBLE);
                    }
                });
    }
    private  boolean isEditEmpty(EditText text){
        return  text == null || "".equals(text.getText().toString());
    }
    public CountDownTimer countDownTimer = new CountDownTimer(60*1000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btnSendSMS.setText(getString(R.string.register_btn_re_code,millisUntilFinished / 1000));
        };
        @Override
        public void onFinish() {
            btnSendSMS.setText(getString(R.string.register_btn_re_code_done));
            btnSendSMS.setEnabled(true);
        };
    };
}
