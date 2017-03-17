package com.shssjk.activity.shop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.person.BankListActivity;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.order.PayOrder;
import com.shssjk.model.order.SPOrder;
import com.shssjk.model.person.Alipay;
import com.shssjk.utils.AuthResult;
import com.shssjk.utils.PayResult;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.SSUtils;
import com.unionpay.UPPayAssistEx;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 来源页面 ： 确认订单 、 订单列表（待支付 ）、 订单详情 （待支付）
 * 3中支付方式:   银联 石头 支付宝
 */
//支付之前页面
public class BeforPayActivity extends BaseActivity implements ConfirmDialog.ConfirmDialogListener {
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
    //    @Bind(R.id.btn_gopay)
//    Button btnGopay;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.ll_alipay_pay)
    LinearLayout llAlipayPay;
    @Bind(R.id.arrow_imgv)
    ImageView arrowImgv;
    @Bind(R.id.ll_union_pay)
    LinearLayout llUnionPay;
    private PayOrder payOrder;//确认订单跳转
    private SPOrder payOrderFromOrderList;
    private Context mContext;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private final String mMode = "00";//银联支付   01测试环境、00正式环境
    private Float sum1 = 0.00f;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        tvState.setText("支付成功");
//                        tvHiti.setText("订单已支付!");
                        showToast("支付成功");
                        dealWithPayAfter();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                        showToast("支付失败");
                        showToast(payResult.getMemo());
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                .show();
                        showToast("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                        showToast(String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };
    private void dealWithPayAfter() {
//        是否创业
        tvState.setText("支付成功");
        tvHiti.setText("订单已支付!");
        checkIsToWork();
    }

    private String orderAmount;
    private String orderId;
    private String orderSn;

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
        payOrderFromOrderList = (SPOrder) intent.getSerializableExtra("fromOrderList");

        if (getIntent() == null && getIntent().getSerializableExtra("order") == null && getIntent().getStringExtra("fromOrderList") == null) {
            showToast(getString(R.string.toast_no_ordersn_data));
            this.finish();
            return;
        }
        if (payOrder != null) {
            orderAmount = payOrder.getOrderAmount();
            orderId = payOrder.getOrderId();
            orderSn = payOrder.getOrderSn();

        } else {
            orderAmount = payOrderFromOrderList.getOrderAmount();
            orderId = payOrderFromOrderList.getOrderID();
            orderSn = payOrderFromOrderList.getOrderSN();
        }

        showData(orderSn, orderAmount);
    }

    private void showData(String orderId, String orderAmount) {
        if (!SSUtils.isEmpty(orderId)) {
            tvOrderid.setText("订单号:" + orderId);
        }
        if (!SSUtils.isEmpty(orderAmount)) {
            tvPaysum.setText("金额:" + orderAmount + " 元");
        }
    }
    @Override
    public void initEvent() {

    }
    //   银联支付
    private void payOrderWithUnionPay(String orderId, String orderAmount) {
        showLoadingToast("正在支付");
        String sum= SSUtils.stringMul100(orderAmount)+"";
        String sum2=SSUtils.RemoveStrPointAftet0(sum);
        ShopRequest.orderUnionPay(orderId, sum2, new SPSuccessListener() {
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
    // 支付宝  支付
    private void payWithAlipay(String orderId) {
        showLoadingToast("正在支付");
        ShopRequest.orderAliPay(orderId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    Alipay alipay = (Alipay) response;
                    AlipayV2(alipay);
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

    private void AlipayV2(Alipay alipay) {
        final String orderInfo = alipay.getString();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(BeforPayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 石头支付
     */
    private void payWithStone(String orerSn) {
        showLoadingToast("正在支付");
        ShopRequest.orderPayUseStone(orerSn, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    int id = (int) response;
                    if (id == 0) {
                        dealWithPayAfter();
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

    /**
     *银联支付支付
     *
     * @param data
     */
    private void dealWithPay(String data) {
        int ret = UPPayAssistEx.startPay(this, null, null, data.trim(), mMode);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            installPluns();
        }
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
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //    判断是否创业
    public void checkIsToWork() {
        PersonRequest.isWork(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
//                 （1、已创业；0未达到创业资格；2达到创业资格;
                    String str = (String) response;
                    if ("0".equals(str.trim())) {
//                        showConfirmDialog("您已达到创业资格，但还未开启创业，是否开启创业",
//                                "系统提示", BeforPayActivity.this, 1);

                        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
                        builder.setMessage("您已达到创业资格，但还未开启创业，是否开启创业");
                        builder.setTitle("系统提示");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //设置你的操作事项
                                startBankListActivity();
                            }
                        });

                        builder.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startupOrderList(1);
                                    }
                                });
                        builder.create().show();
                    } else {
                        startupOrderList(1);
                    }
                } else {
                    showToast(msg);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    //    我的银行卡 列表
    private void startBankListActivity() {
        Intent intent = new Intent();
        intent.setClass(mContext, BankListActivity.class);
        startActivity(intent);
        finish();
    }
    public void startupOrderList(int orderStatus) {
        //订单列表
        Intent allOrderList = new Intent(mContext , OrderActivity.class);
        allOrderList.putExtra("index", orderStatus);
        startActivity(allOrderList);
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data == null) {
                return;
            }
            String str = data.getExtras().getString("pay_result");
            if (str.equals("success")) {
                tvState.setText("支付成功");
                tvHiti.setText("订单已支付!");
                showToast(" 支付成功！ ");
                dealWithPayAfter();
            } else if (str.equals("fail")) {
                showToast(" 支付失败！ ");
            } else if (str.equals("cancel")) {
                showToast(" 你已取消了本次订单的支付！ ");
            }
        }
    }

    //    dialog  确认框 确认按钮
    @Override
    public void clickOk(int actionType) {
        startBankListActivity();
    }

    /**
     * 支付类型判断
     *
     * @param view
     */
    @OnClick({R.id.ll_alipay_pay, R.id.ll_union_pay})
    public void onClick(View view) {
        Float sum1 = SSUtils.string2float(orderAmount);
        if (sum1 == 0) {
            payWithStone(orderSn);
        } else {
            switch (view.getId()) {
                case R.id.ll_alipay_pay:
                    payWithAlipay(orderId);
                    break;
                case R.id.ll_union_pay:
                    payOrderWithUnionPay(orderId, orderAmount);
                    break;
            }
        }
    }
}
