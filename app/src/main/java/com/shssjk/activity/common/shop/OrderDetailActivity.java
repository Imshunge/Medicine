package com.shssjk.activity.common.shop;

import android.app.Activity;
import android.os.Bundle;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.order.SPOrder;
import com.shssjk.utils.SPConfirmDialog;

/**
 *订单详情
 */
public class OrderDetailActivity extends OrderBaseActivity implements SPConfirmDialog.ConfirmDialogListener {
    int points;                             //使用的积分
    float balance;                          //使用的余额
    String invoice;                         //发票
    String mOrderId;                        //订单编号
    SPOrder mOrder;                         //订单
    String detailAddress;                   //收货地址详情

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_detail));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        super.init();
    }

    @Override
    public void initSubViews() {
        if (getIntent()==null || getIntent().getStringExtra("orderId")==null){
            showToast(getString(R.string.toast_no_ordersn_data));
            this.finish();
            return;
        }
        mOrderId = getIntent().getStringExtra("orderId");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void clickOk(int actionType) {

    }

    public void refreshData(){

        showLoadingToast();
        SPPersonRequest.getOrderDetailByID(mOrderId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                mOrder = (SPOrder) response;
                try {
                    if (mOrder != null) {
                        dealModel();
//                        refreshView();
                        //load 商品金额信息
                    }
                } catch (Exception e) {
                    showToast(e.getMessage());
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener(OrderDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }



}
