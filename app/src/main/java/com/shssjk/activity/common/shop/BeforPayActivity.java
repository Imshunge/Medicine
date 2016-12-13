package com.shssjk.activity.common.shop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.order.PayOrder;
import com.shssjk.utils.SSUtils;
import com.unionpay.UPPayAssistEx;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//支付之前页面
public class BeforPayActivity extends BaseActivity {

    @Bind(R.id.tv_orderid)
    TextView tvOrderid;
    @Bind(R.id.tv_paysum)
    TextView tvPaysum;
    @Bind(R.id.ll_frist)
    LinearLayout llFrist;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.tv_hiti)
    TextView tvHiti;
    @Bind(R.id.btn_gopay)
    Button btnGopay;
    @Bind(R.id.tv_state)
    TextView tvState;
    private PayOrder payOrder;
    private Context mContext;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    private final String mMode = "00";//01测试环境、00正式环境

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.activity_title_pay_befor));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_befor_pay);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
        payOrder = (PayOrder) intent.getSerializableExtra("order");
        showData(payOrder);
    }

    private void showData(PayOrder payOrder) {
        if (!SSUtils.isEmpty(payOrder)) {
            tvOrderid.setText("订单号:" + payOrder.getOrderSn());
            tvPaysum.setText("金额:" + payOrder.getOrderAmount() + " 元");
        }

    }

    @Override
    public void initEvent() {

    }

    @OnClick(R.id.btn_gopay)
    public void onClick() {
        payOrder(payOrder);
    }

    private void payOrder(PayOrder payOrder) {
        showLoadingToast("正在支付");

        Float sum1 = SSUtils.string2float(payOrder.getOrderAmount());
        String sum = SSUtils.float2String(sum1);
        ShopRequest.orderPay(payOrder.getOrderId(), sum, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    String id = (String) response;
                    dealWithPay(id);

                } else {

                }
                hideLoadingToast();

            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);

            }
        });
    }

    /**
     * 支付
     *
     * @param data
     */
    private void dealWithPay(String data) {
//        hud.dismiss();
        int ret = UPPayAssistEx.startPay(this, null, null, data.trim(), mMode);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            installPluns();
        }
//        hud.dismiss();
//            else {
//                UPPayAssistEx.startPayByJAR(getActivity(), PayActivity.class, null, null, data.trim(), mMode);
//            }
    }

    //    调用控件
    public void installPluns() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UPPayAssistEx.installUPPayPlugin(BeforPayActivity.this);
                dialog.dismiss();
//                hud.dismiss();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                hud.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        hud.show();
        if (requestCode == 10) {

            if (data == null) {
                return;
            }

            String str = data.getExtras().getString("pay_result");
            if (str.equals("success")) {
                tvState.setText("支付成功");
                tvHiti.setText("订单已支付!");
                btnGopay.setVisibility(View.GONE);
                showToast(" 支付成功！ ");
            } else if (str.equals("fail")) {
                showToast(" 支付失败！ ");
            } else if (str.equals("cancel")) {
                showToast(" 你已取消了本次订单的支付！ ");
            }
        }
    }
}
