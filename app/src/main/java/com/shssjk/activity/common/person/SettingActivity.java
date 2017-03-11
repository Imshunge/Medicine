package com.shssjk.activity.common.person;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 首页  我  设置
 */
import com.shssjk.activity.R;
import com.shssjk.activity.common.ActivityCollector;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.activity.common.user.ChangePasswordActivity;
import com.shssjk.activity.common.user.MyMessageActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.utils.ExitDialog;
import com.shssjk.view.SPArrowRowView;

import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题

    //    SPArrowRowView myteamAview;  //我的团队
    SPArrowRowView messageAview;  //消息通知

    SPArrowRowView changepwdAview;  //更改密码


    SPArrowRowView helpAview;  //帮助

    RelativeLayout telephoneRelat; //尚尚热线

    SPArrowRowView serviceAview;  //尚尚服务

    SPArrowRowView exitAview;  //退出
    private TextView telNumTex;
    private Context mContext;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.string_setting));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        super.init();
        mContext=this;
    }

    @Override
    public void initSubViews() {
        //        标题内容
        titlbarFl = (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this, R.color.white));
        telephoneRelat = (RelativeLayout) findViewById(R.id.rl_tel);
//        myteamAview = (SPArrowRowView) findViewById(R.id.person_setting_myteam_aview);
        messageAview = (SPArrowRowView) findViewById(R.id.person_setting_message_aview);
        changepwdAview = (SPArrowRowView) findViewById(R.id.person_setting_changepwd_aview);
        helpAview = (SPArrowRowView) findViewById(R.id.person_setting_help_aview);
        serviceAview = (SPArrowRowView) findViewById(R.id.person_setting_service_aview);
        exitAview = (SPArrowRowView) findViewById(R.id.person_setting_exit_aview);
        telNumTex = (TextView) findViewById(R.id.sub_phonenum_txtv);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        telephoneRelat.setOnClickListener(this);
        messageAview.setOnClickListener(this);
        changepwdAview.setOnClickListener(this);
        helpAview.setOnClickListener(this);
        serviceAview.setOnClickListener(this);
        exitAview.setOnClickListener(this);
    }

    //退出
    public void showDialog() {
        final ExitDialog dialog = new ExitDialog(this);
        Window win = dialog.getWindow();
        LayoutParams params = new LayoutParams();
        params.x = 0;// 设置x坐标
        params.y = 100;// 设置y坐标
        win.setAttributes(params);
        dialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        dialog.show();
        TextView tv_exit = (TextView) win.findViewById(R.id.exit_user);
        TextView tv_close = (TextView) win.findViewById(R.id.exit_close);
        tv_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MobileApplication.getInstance().exitLogin();
                sendBroadcast(new Intent(MobileConstants.ACTION_LOGIN_CHNAGE));
                clearUserData();
                showToast("安全退出");
                finish();
            }
        });
        tv_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //程序关闭
                ActivityCollector.finishAll();                //后台运行
            }
        });
    }
//    清除用户名 密码
    private void clearUserData() {
        SPSaveData.clearLoginData(mContext);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_setting_message_aview:
                startupMyMessageActivity();
                break;
            case R.id.person_setting_changepwd_aview:
//                更改密码
                startupChangePwdActivity();
                break;
            case R.id.person_setting_help_aview:
                //	帮助
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.rl_tel:  //拨号
                try {
                    if (ActivityCompat.checkSelfPermission(SettingActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SettingActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return;
                    } else {
                        call();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.person_setting_service_aview:
                //	帮助
                Intent aboutIntent = new Intent(this, ServiceActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.person_setting_exit_aview:
                showDialog();
                break;
        }
    }

    private void call() {
        try {
            String number = telNumTex.getText().toString();
            Intent intent = new Intent();
            intent.setAction("android.intent.action.CALL");
            intent.setData(Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SettingActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            }
            startActivity(intent);
        } catch (Exception e) {

        }
    }
    /**
     * 修改密码
     */
    public void startupChangePwdActivity(){
        if (!MobileApplication.getInstance().isLogined){
            showToastUnLogin();
            toLoginPage();
            return;
        }
        Intent changePwdIntent = new Intent(this, ChangePasswordActivity.class);
        startActivity(changePwdIntent);
    }
    /**
     * 消息通知
     */
    public void startupMyMessageActivity(){
        if (!MobileApplication.getInstance().isLogined){
            showToastUnLogin();
            toLoginPage();
            return;
        }
        Intent changePwdIntent = new Intent(this, MyMessageActivity.class);
        startActivity(changePwdIntent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "拨打电话功能被禁止，将导联系客服功能异常", Toast.LENGTH_SHORT).show();
//                    showToast("拨打电话功能被禁止，将导功能联系客服异常");
                }
        }
    }
}
