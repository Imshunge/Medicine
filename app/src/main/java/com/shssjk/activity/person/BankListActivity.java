package com.shssjk.activity.person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.BankListAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.person.Bank;
import com.shssjk.utils.ConfirmDialog;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的银行卡 列表
 */
public class BankListActivity extends BaseActivity implements BankListAdapter.BankListListener,ConfirmDialog.ConfirmDialogListener {

    @Bind(R.id.address_listv)
    ListView addressListv;
    @Bind(R.id.add_bank_btn)
    Button addBankBtn;
    List<Bank> bankList;
    BankListAdapter bankListAdapter;
    Bank deletebank;
    Button explainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.banklistactivity_title),true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        ButterKnife.bind(this);
        super.init();
    }
    @Override
    public void initSubViews() {
        View emptyView = findViewById(R.id.empty_lstv);
        addressListv.setEmptyView(emptyView);
        explainBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        explainBtn.setBackgroundResource(R.drawable.title_right_dot_selector);
        explainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBusinessActivity();
            }
        });
    }
    @Override
    public void initData() {
        getBankList();
    }

    @Override
    public void initEvent() {
        bankListAdapter = new BankListAdapter(this, this);
        addressListv.setAdapter(bankListAdapter);
        addressListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bank bank = bankList.get(position);
                Intent intent = new Intent(BankListActivity.this, BanAddOrkEditActivity.class);
                intent.putExtra("bank", bank);
                startActivityForResult(intent, MobileConstants.Result_Code_Refresh);
            }
        });
    }

    @OnClick(R.id.add_bank_btn)
    public void onClick() {
        Intent intent = new Intent(this, BanAddOrkEditActivity.class);
        startActivityForResult(intent, MobileConstants.Result_Code_Refresh);
    }
    /**
     * 创业说明
     */
    private void startBusinessActivity() {
        Intent intent = new Intent(this, StartBusinessActivity.class);
        startActivity(intent);
    }


    private void getBankList() {
        showLoadingToast();
        PersonRequest.getBankList(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    bankList = (List<Bank>) response;
                    bankListAdapter.setData(bankList);
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener(BankListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
    @Override
    public void onItemDelete(Bank bank) {
        showToast("删除");
         deletebank = bank;
        showConfirmDialog("确定删除该银行卡吗", "删除提醒", this, 1);
    }

    @Override
    public void onItemEdit(Bank bank) {
        Intent intent = new Intent(this, BanAddOrkEditActivity.class);
        intent.putExtra("bank", bank);
        startActivityForResult(intent, MobileConstants.Result_Code_Refresh);
    }

    @Override
    public void onItemSetDefault(Bank bank) {
        if ("1".equals(bank.getIs_default())) {
            showToast("该银行卡已是默认银行卡");
        } else {
            setDefaultBank(bank);
        }
    }
    private void setDefaultBank(Bank bank) {
        RequestParams params = new RequestParams();
        params.put("name", bank.getName());
        params.put("code", bank.getCode());
        params.put("bank", bank.getBank());
        params.put("is_default","1");
        params.put("bid", bank.getId());
        if (!SPStringUtils.isEmpty(bank.getIs_default())) {
            params.put("address_id", bank.getIs_default());
        }
        showLoadingToast("正在保存数据");
        PersonRequest.saveBank(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                getBankList();
            }
        }, new SPFailuredListener(BankListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case MobileConstants.Result_Code_Refresh: //
                if (resultCode == Activity.RESULT_OK) {
                    getBankList();
                }
                break;
        }
    }
    @Override
    public void clickOk(int actionType) {
        showLoadingToast("正在删除");
        PersonRequest.deleteBank(deletebank.getId(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                getBankList();
            }
        }, new SPFailuredListener(BankListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
}
