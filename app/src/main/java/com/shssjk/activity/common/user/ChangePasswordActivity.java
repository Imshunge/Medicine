package com.shssjk.activity.common.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPUserRequest;

import static com.shssjk.utils.SPStringUtils.isEditEmpty;


/**
 * 更改密码
 */
public class ChangePasswordActivity extends BaseActivity {
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题

    EditText pwdoldEdit;//旧密码

    EditText pwdnewEdit;//新密码

    EditText pwdconfirmEdit;//确认密码

    Button confirmBtn; //确认按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.change_pwd_title));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        super.init();
    }

    @Override
    public void initSubViews() {
//        标题内容
        titlbarFl = (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this, R.color.white));

        //旧密码
        pwdoldEdit = (EditText) findViewById(R.id.edit_password_old);

        //新密码
        pwdnewEdit = (EditText) findViewById(R.id.edit_password);

        //确认密码
        pwdconfirmEdit = (EditText) findViewById(R.id.edit_password2);
        //确认按钮
        confirmBtn = (Button) findViewById(R.id.btn_changepwd);

    }

    @Override
    public void initData() {


    }

    @Override
    public void initEvent() {

    }

    public void onChangePwdClick(View view) {
        //check input
        if (isEditEmpty(pwdoldEdit)) {
            pwdoldEdit.setError(Html.fromHtml("<font color='red'>" + getString(R.string.change_pwd_old_null) + "</font>"));
            return;
        }
        if (isEditEmpty(pwdnewEdit)) {
            pwdnewEdit.setError(Html.fromHtml("<font color='red'>" + getString(R.string.change_pwd_new_null) + "</font>"));
            return;
        }
        if (isEditEmpty(pwdconfirmEdit)) {
            pwdconfirmEdit.setError(Html.fromHtml("<font color='red'>" + getString(R.string.change_pwd_new2_null) + "</font>"));
            return;
        }

        SPUserRequest.doChangePassword(pwdoldEdit.getText().toString(), pwdnewEdit.getText().toString(), pwdconfirmEdit.getText().toString(),
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        if (response != null) {
                            showToast(msg);
                            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                        //txtErrorInfo.setText("注册成功");
//                        txtErrorInfo.setText(msg);
//                        txtErrorInfo.setVisibility(View.VISIBLE);
                    }
                });
    }
}
