package com.shssjk.activity.person;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.person.Bank;
import com.shssjk.view.SwitchButton;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加 银行卡 修改银行卡
 */
public class BanAddOrkEditActivity extends BaseActivity {
    @Bind(R.id.bankcard_user_txtv)
    TextView bankcardUserTxtv;
    @Bind(R.id.bankcard_user_edtv)
    EditText bankcardUserEdtv;
    @Bind(R.id.bank_num_txtv)
    TextView bankNumTxtv;
    @Bind(R.id.bank_num_edtv)
    EditText bankNumEdtv;
    @Bind(R.id.bankcard_issuer_txtv)
    TextView bankcardIssuerTxtv;
    @Bind(R.id.bankcard_issuer_edtv)
    EditText bankcardIssuerEdtv;
    @Bind(R.id.bank_setdefault_txtv)
    TextView bankSetdefaultTxtv;
    //    @Bind(R.id.bank_setdefault_sth)
    SwitchButton bankSetdefaultSth;
    @Bind(R.id.submit_btn)
    Button submitBtn;
    @Bind(R.id.ban_sp)
    Spinner banSp;
    boolean isSpinnerFirst = true;
    private Bank bank;
    private boolean isAdd = false;//是否为添加 true 添加 ，false  修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.bankaddoreditactivity_title));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_edit);
        ButterKnife.bind(this);
        super.init();
    }
    @Override
    public void initSubViews() {
        //默认地址开关
        bankSetdefaultSth = (SwitchButton) findViewById(R.id.bank_setdefault_sth);
        bankSetdefaultSth.setOnChangeListener(new SwitchButton.OnChangeListener() {
            @Override
            public void onChange(SwitchButton sb, boolean state) {
                if (state) {
                    bank.setIs_default("1");
                } else {
                    bank.setIs_default("0");
                }
            }
        });
    }

    @Override
    public void initData() {
        if (getIntent() != null && getIntent().getSerializableExtra("bank") != null) {
            bank = (Bank) getIntent().getSerializableExtra("bank");
            isAdd = false;
        }
        if (bank == null) {
            bank = new Bank();
            isAdd = true;
            bank.setIs_default("0");
        } else {
            bankcardUserEdtv.setText(bank.getName());
            bankNumEdtv.setText(bank.getCode());
            bankcardIssuerEdtv.setText(bank.getBank());
            if ("1".equals(bank.getIs_default())) {
                bankSetdefaultSth.setSwitchOn(true);
            } else {
                bankSetdefaultSth.setSwitchOn(false);
            }
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
                    if(!isAdd){
                        bankcardIssuerEdtv.setText(bank.getBank());
                    }
                } else {
                    String[] banks = getResources().getStringArray(R.array.type_bank);
                    bankcardIssuerEdtv.setText(banks[pos]);
                }
                isSpinnerFirst = false;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    //是否为空数据
    private boolean checkData() {
        if (SPStringUtils.isEmpty(bankcardUserEdtv.getText().toString())) {
            showToast("请输入持卡人");
            return false;
        }
        bank.setName(bankcardUserEdtv.getText().toString());
        if (SPStringUtils.isEmpty(bankNumEdtv.getText().toString())) {
            showToast("请输入卡号");
            return false;
        }
        String number = bankNumEdtv.getText().toString();
        Pattern pattern = Pattern
                .compile("^([0-9]{16}|[0-9]{19})$");
        Matcher matcher = pattern.matcher(number);
        if (matcher.matches()) {
            bank.setCode(bankNumEdtv.getText().toString());
        } else {
            showToast("请输入卡号位数不对");
            return false;
        }

        if (SPStringUtils.isEmpty(bankcardIssuerEdtv.getText().toString())) {
            showToast("请输入发卡行");
            return false;
        }
        bank.setBank(bankcardIssuerEdtv.getText().toString());
        return true;
    }
    @OnClick({ R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                if (isAdd) {
                    addBand();
                } else {
                    updateData();
                }
                break;
        }
    }

    private void addBand() {
        if (!checkData()) {
            return;
        }
        RequestParams params = new RequestParams();
        params.put("name", bank.getName());
        params.put("code", bank.getCode());
        params.put("bank", bank.getBank());
        params.put("is_default", bank.getIs_default());
//        if (!SPStringUtils.isEmpty(bank.getIs_default())) {
//            params.put("address_id" , bank.getIs_default());
//        }
        showLoadingToast("正在保存数据");
        SPPersonRequest.addBank(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                setResult(Activity.RESULT_OK);
                finish();
            }
        }, new SPFailuredListener(BanAddOrkEditActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });

    }

    //    修改数据
    private void updateData() {
        if (!checkData()) {
            return;
        }
        RequestParams params = new RequestParams();
        params.put("name", bank.getName());
        params.put("code", bank.getCode());
        params.put("bank", bank.getBank());
        params.put("is_default", bank.getIs_default());
        params.put("bid", bank.getId());
        if (!SPStringUtils.isEmpty(bank.getIs_default())) {
            params.put("address_id", bank.getIs_default());
        }
        showLoadingToast("正在保存数据");
        SPPersonRequest.saveBank(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                setResult(Activity.RESULT_OK);
                finish();
            }
        }, new SPFailuredListener(BanAddOrkEditActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
}
