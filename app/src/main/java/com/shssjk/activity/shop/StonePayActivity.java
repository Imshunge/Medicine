package com.shssjk.activity.shop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.R;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.shop.StoneOrderInfo;
import com.shssjk.utils.SSUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 石头支付
 */
public class StonePayActivity extends BaseActivity {

    @Bind(R.id.order_ordersn_txtv)
    TextView orderOrdersnTxtv;
    @Bind(R.id.stone_sum_txtv)
    TextView stoneSumTxtv;
    @Bind(R.id.orderid_txtv)
    TextView orderidTxtv;
    @Bind(R.id.order_sum_txtv)
    TextView orderSumTxtv;
    @Bind(R.id.order_numstone_txtv)
    TextView orderNumstoneTxtv;
    @Bind(R.id.btn_withdraw)
    Button btnWithdraw;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.stone_pay));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone_pay_activity);
        ButterKnife.bind(this);
        super.init();
    }

    @Override
    public void initSubViews() {
        if (getIntent() != null && getIntent().getStringExtra("orderId") != null) {
            orderId = getIntent().getStringExtra("orderId");
        } else {
            finish();
            return;
        }
    }

    @Override
    public void initData() {
        getOrderInfo(orderId);
    }


    @Override
    public void initEvent() {

    }

    private void getOrderInfo(String orderId) {
        ShopRequest.getOrderInfoPayWithStone(orderId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    StoneOrderInfo orderInfo = (StoneOrderInfo) response;
                    showData(orderInfo);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    private void showData(StoneOrderInfo orderInfo) {
        if (!SSUtils.isEmpty(orderInfo.getOrderSn())) {
            orderidTxtv.setText(orderInfo.getOrderSn());
        }
        if (!SSUtils.isEmpty(orderInfo.getDoEarnings())) {
            stoneSumTxtv.setText(orderInfo.getDoEarnings());
        }
        if (!SSUtils.isEmpty(orderInfo.getOrderAmount())) {
            orderSumTxtv.setText(orderInfo.getOrderAmount());
            orderNumstoneTxtv.setText(orderInfo.getOrderAmount());
        }
    }

    @OnClick(R.id.btn_withdraw)
    public void onClick() {
        payWithStone(orderId);
    }
    /**
     * 石头支付
     */
    private void payWithStone(String orderId) {
        showLoadingToast("正在支付");
        ShopRequest.orderPayWithStone(orderId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    int id = (int) response;
                    if (id == 0) {
//                        dealWithPayAfter();
                        showToast(msg);
                      setResult(RESULT_OK);
                        finish();
                    }
                }
                hideLoadingToast();
                showToast(msg);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }


}
