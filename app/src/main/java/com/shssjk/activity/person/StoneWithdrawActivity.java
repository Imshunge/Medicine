package com.shssjk.activity.person;
/**
 * 石头提现
 */
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.http.person.SPUserRequest;
import com.shssjk.model.person.Bank;
import com.shssjk.utils.SPStringUtils;
import com.shssjk.utils.SSUtils;
import com.shssjk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class StoneWithdrawActivity extends BaseActivity {
    @Bind(R.id.bankcard_issuer_txtv)
    TextView bankcardIssuerTxtv;
    @Bind(R.id.bankcard_issuer_edtv)
    EditText bankcardIssuerEdtv;
    @Bind(R.id.ban_sp)
    Spinner banSp;
    @Bind(R.id.bank_num_txtv)
    TextView bankNumTxtv;
    @Bind(R.id.bank_num_edtv)
    EditText bankNumEdtv;
    @Bind(R.id.code_txtv)
    TextView codeTxtv;
    @Bind(R.id.code_num_edtv)
    EditText codeNumEdtv;
    @Bind(R.id.btn_send_sms)
    Button btnSendSms;
    @Bind(R.id.btn_withdraw)
    Button btnWithdraw;
    private Context mContext;
    boolean isSpinnerFirst = true;
    private List<Bank> bankList = new ArrayList<>();
    private String mobile;
    private String bnakId;
    private String code;
    private String stone;
    private boolean isAdd = false;//是否为添加 true 添加 ，false  修改
    private double myCountStoeNum=0D;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_mystone_withdraw));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone_withdraw);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        bankcardIssuerEdtv.setEnabled(false);
    }

    @Override
    public void initData() {
        getBankList();
        bankcardIssuerEdtv.setText("");
        String strStoneNum =getIntent().getStringExtra("StoneNum");
        if (getIntent() != null || strStoneNum != null) {
            myCountStoeNum=  SSUtils.string2double(strStoneNum);
        }
    }
    @Override
    public void initEvent() {
        banSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if (isSpinnerFirst) {
                    bankcardIssuerEdtv.setText("");
                    if (!isAdd) {
//                        bankcardIssuerEdtv.setText(bank.getBank());
                    }
                } else {
                    bankcardIssuerEdtv.setText(bankList.get(pos).getCode());
                    bnakId = bankList.get(pos).getId();
                }
                isSpinnerFirst = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void getBankList() {
        showLoadingToast();
        PersonRequest.getBankList(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    bankList = (List<Bank>) response;
                    showBankList(bankList);
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener(StoneWithdrawActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    private void showBankList(List<Bank> bankList) {
        ArrayAdapter<Bank> adapter = new ArrayAdapter<Bank>(mContext,
                android.R.layout.simple_spinner_item, bankList);
        banSp.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.dropdown_stytle);
    }
    @OnClick({R.id.btn_send_sms, R.id.btn_withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_sms:
                codeNumEdtv.setText("");

                if (SPStringUtils.isEditEmpty(bankNumEdtv) ) {
                    showToast("请输提现金额");
                    return;
                }else{
                    if(myCountStoeNum<500){
                        showToast("提现金额下限为500,您账户的石头数目为 "+myCountStoeNum);
                        return;
                    }else{
                        stone = bankNumEdtv.getText().toString().trim();
                        if(SSUtils.string2double(stone)<500){
                            showToast("提现金额下限为500");
                            return;
                        }
                    }
                }
//                if(myCountStoeNum<500){
//                    showToast("提现金额不能小于500");
//                    return;
//                }
                onBtnReCodeClick();
                break;
            case R.id.btn_withdraw:
                if (SPStringUtils.isEditEmpty(bankcardIssuerEdtv)) {
                    showToast("银行卡号不能为空");
                    return;
                }
                if (SPStringUtils.isEditEmpty(bankNumEdtv) ) {
                    showToast("请输提现金额");
                    return;
                }else{
                    if(myCountStoeNum<500){
                        showToast("提现金额下限为500,您账户的石头数目为 "+myCountStoeNum);
                     return;
                    }else{
                        stone = bankNumEdtv.getText().toString().trim();
                        if(SSUtils.string2double(stone)<500){
                            showToast("提现金额下限为500");
                            return;
                        }
                    }
                }
                if (SPStringUtils.isEditEmpty(codeNumEdtv)) {
                    showToast("请输入验证码");
                    return;
                }else{
                    code = codeNumEdtv.getText().toString();
                }
            stoneApply();
            break;
        }
    }
    private void stoneApply() {
        PersonRequest.myStoneApply(bnakId,stone
                ,code,mobile, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                //if (response != null) {
                int status = (int) response;
                showToast(msg);
                if(status==0){
                    clearData();
                }

                //}
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    private void clearData() {
        bankNumEdtv.setText("");
        codeNumEdtv.setText("");

    }

    public void onBtnReCodeClick(){
        //send SMS
        mobile = MobileApplication.getInstance().getLoginUser().getMobile();
        if(TextUtils.isEmpty(mobile)){
            showToast("用户手机号为空");
            return;
        }
        SPUserRequest.sendSMSRegCode(mobile, new SPSuccessListener() {
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
        btnSendSms.setEnabled(false);
        countDownTimer.start();
    }


    public CountDownTimer countDownTimer = new CountDownTimer(60*1000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btnSendSms.setText(getString(R.string.register_btn_re_code,millisUntilFinished / 1000));
        }
        @Override
        public void onFinish() {
            btnSendSms.setText(getString(R.string.register_btn_re_code_done));
            btnSendSms.setEnabled(true);
        }
    };







}
