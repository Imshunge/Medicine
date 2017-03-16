package com.shssjk.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.SPAddressListAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.dao.SPPersonDao;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.person.SPConsigneeAddress;
import com.shssjk.person.address.ConsigneeAddressEditActivity;
import com.shssjk.utils.Logger;
import com.shssjk.utils.ConfirmDialog;

import java.util.List;

/**
 * 收货地址 列表
 */
public class ConsigneeAddressListActivity extends BaseActivity implements SPAddressListAdapter
        .AddressListListener, ConfirmDialog.ConfirmDialogListener{
    private String TAG = "ConsigneeAddressListActivity";
    ListView addressLstv;

    PtrClassicFrameLayout ptrClassicFrameLayout;
    Button addAddressBtn;
    SPAddressListAdapter mAdapter;
    List<SPConsigneeAddress> consignees;
    SPConsigneeAddress selectConsignee;
    Context mContext;
    TextView qqIconTxt;
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_consignee_list));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignee_address_list);
        mContext=this;
        super.init();
    }
    @Override
    public void initSubViews() {
//       标题
        titlbarFl= (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(mContext, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(mContext,R.color.white));
        titleTxtv.setText(getString(R.string.title_consignee_list));
        addressLstv= (ListView) findViewById(R.id.address_listv);
        ptrClassicFrameLayout= (PtrClassicFrameLayout) findViewById(R.id.address_list_pcl);
        addAddressBtn= (Button) findViewById(R.id.add_address_btn);
        backBtn = (Button) findViewById(R.id.titlebar_back_btn);
        addressLstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getIntent() != null && getIntent().hasExtra("getAddress")) {
                    SPConsigneeAddress consigneeAddress = consignees.get(position);
                    Intent resultIntent = new Intent(ConsigneeAddressListActivity.this , ConfirmOrderActivity.class);
                    resultIntent.putExtra("consignee" , consigneeAddress);
                    setResult(MobileConstants.Result_Code_GetAddress , resultIntent);
                    ConsigneeAddressListActivity.this.finish();
                }
            }
        });
        View emptyView = findViewById(R.id.empty_lstv);
        addressLstv.setEmptyView(emptyView);
    }
    @Override
    public void initData() {
        mAdapter = new SPAddressListAdapter(this , this);
        addressLstv.setAdapter(mAdapter);
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                refreshData();
            }
        });
        refreshData();
    }
    @Override
    public void initEvent() {
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ConsigneeAddressEditActivity.class);
                startActivityForResult(intent, MobileConstants.Result_Code_Refresh);
            }
        });
    }
    @Override
    public void onItemDelete(SPConsigneeAddress consigneeAddress) {
        selectConsignee = consigneeAddress;
        showConfirmDialog("确定删除该地址吗?", "删除提醒", this, 1);
    }


    @Override
    public void onItemEdit(SPConsigneeAddress consigneeAddress) {
        Intent intent = new Intent(this, ConsigneeAddressEditActivity.class);
        intent.putExtra("consignee" , consigneeAddress);
        startActivityForResult(intent, MobileConstants.Result_Code_Refresh);
    }

    @Override
    public void onItemSetDefault(SPConsigneeAddress consigneeAddress) {
        Intent intent = new Intent(this, ConsigneeAddressEditActivity.class);
        intent.putExtra("consignee" , consigneeAddress);
        startActivityForResult(intent, MobileConstants.Result_Code_Refresh);
    }
    public void refreshData(){

        showLoadingToast();
        SPPersonRequest.getConsigneeAddressList(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    consignees = (List<SPConsigneeAddress>) response;
                    dealModels();
                    mAdapter.setData(consignees);
                }
                ptrClassicFrameLayout.refreshComplete();
                ptrClassicFrameLayout.setLoadMoreEnable(false);
                hideLoadingToast();
            }
        }, new SPFailuredListener(ConsigneeAddressListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
    public void dealModels(){
        if (consignees == null)return;
        for (SPConsigneeAddress consigneeAddress : consignees) {
            String firstAddress = SPPersonDao.getInstance(this).
                    queryFirstRegion(consigneeAddress.getProvince(),
                            consigneeAddress.getCity(), consigneeAddress.getDistrict(),
                            consigneeAddress.getTown());
            if (firstAddress != null){
                consigneeAddress.setFullAddress(firstAddress+consigneeAddress.getAddress());
                Logger.e("firstAddress", firstAddress);
                Logger.e("consigneeAddress.getAddress()",consigneeAddress.getAddress());
            }else{
                consigneeAddress.setFullAddress(consigneeAddress.getAddress());
            }
        }
    }

    @Override
    public void clickOk(int actionType) {
        showLoadingToast("正在删除");
        SPPersonRequest.delConsigneeAddressByID(selectConsignee.getAddressID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                refreshData();
            }
        }, new SPFailuredListener(ConsigneeAddressListActivity.this) {
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

        if(requestCode == MobileConstants.Result_Code_Refresh){
            refreshData();
        }
    }
}
