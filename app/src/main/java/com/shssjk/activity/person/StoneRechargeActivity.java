package com.shssjk.activity.person;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.R;
import com.shssjk.adapter.StoneTypeAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.person.Alipay;
import com.shssjk.model.person.StoneType;
import com.shssjk.model.shop.WeChat;
import com.shssjk.utils.AuthResult;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.PayResult;
import com.shssjk.utils.SSUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 石头充值
 */
public class StoneRechargeActivity extends BaseActivity {
    @Bind(R.id.stone_type_grid)
    GridView stoneTypeGrid;
    @Bind(R.id.ll_alipay_pay)
    LinearLayout llAlipayPay;
    @Bind(R.id.arrow_imgv)
    ImageView arrowImgv;
    @Bind(R.id.ll_union_pay)
    LinearLayout llUnionPay;
    private IWXAPI msgApi;
    private Context mContext;
    private StoneTypeAdapter stoneTypeAdapter;
    private List<StoneType> mStoneList = new ArrayList<>();
    private JSONObject mStoneOdrerInfo = new JSONObject();
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private final String mMode = "00";//银联支付   01测试环境、00正式环境
    private String order_amount;
    private String order_id;
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
    class StateChangeRevicer extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            dealWithPayAfter();
        }
    }
    private IntentFilter intentFilter;//1.创建IntentFilter实例
    private StateChangeRevicer stateChangeRevicer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_mystone_recharge));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone_recharge);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }
    @Override
    public void initSubViews() {

    }
    @Override
    public void initData() {
        getStoneList();
        stoneTypeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mStoneList.size() > 0) {
                    StoneType stoneType = mStoneList.get(position);
                    addStoneOrder(stoneType);
                }
            }
        });
        msgApi = WXAPIFactory.createWXAPI(mContext, null);
        // 将该app注册到微信
        msgApi.registerApp(MobileConstants.APP_ID);

    }
    @Override
    public void initEvent() {
        stoneTypeAdapter = new StoneTypeAdapter(this);
        stoneTypeGrid.setAdapter(stoneTypeAdapter);
        stateChangeRevicer = new StateChangeRevicer();
        intentFilter = new IntentFilter(MobileConstants.ACTION_PAY_CHANGE);
        registerReceiver(stateChangeRevicer, intentFilter);
    }
    private void addStoneOrder(StoneType stoneType) {
//        showLoadingToast("正在加载数据...");
        PersonRequest.addStoneOrder(stoneType.getGoodsId(), stoneType.getKey(),
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mStoneOdrerInfo = (JSONObject) response;
                        } else {
                            showToast(msg);
                        }
                    }
                }, new SPFailuredListener((IViewController) mContext) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });
    }

    private void getStoneList() {
        showLoadingToast("正在加载数据...");
        PersonRequest.getStoneList(
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mStoneList = (List<StoneType>) response;
                            stoneTypeAdapter.setData(mStoneList);
                        } else {
                            showToast(msg);
                        }
                    }
                }, new SPFailuredListener((IViewController) mContext) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });
    }

    @OnClick({R.id.ll_alipay_pay, R.id.ll_union_pay,R.id.ll_wechat_pay})
    public void onClick(View view) {
        try {
            if (mStoneOdrerInfo!=null&&!mStoneOdrerInfo.isNull("order_amount")) {
                order_amount = mStoneOdrerInfo.getString("order_amount");
            }else {
                showToast("请先选择充值规格");
                return;
            }
            if (mStoneOdrerInfo!=null&&!mStoneOdrerInfo.isNull("order_id")) {
                order_id = mStoneOdrerInfo.getString("order_id");
            }else {
                showToast("请先选择充值规格");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (view.getId()) {
            case R.id.ll_alipay_pay:
                payWithAlipay(order_id);
                break;
            case R.id.ll_union_pay:
                payOrderWithUnionPay(order_id,order_amount);
                break;
            case R.id.ll_wechat_pay:
                startWeChatPay(order_id);
        }
    }

    /**
     * 微信支付
     * @param order_id
     */
    private void startWeChatPay(String order_id) {
        showLoadingToast("正在支付");
        ShopRequest.orderPayWithWeChat(order_id, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    WeChat weChat = (WeChat) response;
                    dealWithWeChat(weChat);
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
    private void dealWithWeChat(WeChat weChat) {
        if(!msgApi.isWXAppInstalled()){
            showToast("未检测到微信程序,不能使用微信支付功能!");
            return;
        }
        if(!msgApi.isWXAppSupportAPI()){
            showToast("当前微信版本过低，不支持支付");
            return;
        }
        PayReq req = new PayReq();
        req.appId=weChat.getAppid();
        req.partnerId=weChat.getPartnerid();
        req.prepayId=weChat.getPrepayid();
        req.nonceStr=weChat.getNoncestr();
        req.timeStamp=String.valueOf(weChat.getTimestamp());
        req.packageValue=weChat.getPackage();
        req.sign=weChat.getSign();
        req.extData="app data";
        msgApi.sendReq(req);
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
    /**
     *银联支付支付
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
                UPPayAssistEx.installUPPayPlugin(StoneRechargeActivity.this);
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
                    showToast(msg);
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
                PayTask alipay = new PayTask(StoneRechargeActivity.this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data == null) {
                return;
            }
            String str = data.getExtras().getString("pay_result");
            if (str.equals("success")) {
//                tvState.setText("支付成功");
//                tvHiti.setText("订单已支付!");
                showToast(" 支付成功！ ");
                dealWithPayAfter();
            } else if (str.equals("fail")) {
                showToast(" 支付失败！ ");
            } else if (str.equals("cancel")) {
                showToast(" 你已取消了本次订单的支付！ ");
            }
        }
    }
//   支付成功 弹窗
    private void dealWithPayAfter() {
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
        builder.setMessage("石头充值成功");
        builder.setTitle("系统提示");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mStoneOdrerInfo=null;
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.close,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stateChangeRevicer);
    }
}
