package com.shssjk.activity.shop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.dao.SPPersonDao;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;
import com.shssjk.model.order.SPOrder;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.SPArrowRowView;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单  评论  详情
 */
public class CommentOrderDetailActivity extends OrderBaseActivity implements ConfirmDialog.ConfirmDialogListener {
    int points;                             //使用的积分
    float balance;                          //使用的余额
    String invoice;                         //发票
    String mOrderId;                        //订单编号
    SPOrder mOrder;                         //订单
    String detailAddress;                   //收货地址详情
    @Bind(R.id.order_ordersn_txtv)
    TextView orderOrdersnTxtv;
    @Bind(R.id.order_status_txtv)
    TextView orderStatusTxtv;
    @Bind(R.id.order_consignee_txtv)
    TextView orderConsigneeTxtv;
    @Bind(R.id.order_address_txtv)
    TextView orderAddressTxtv;
    @Bind(R.id.order_address_arrow_imgv)
    ImageView orderAddressArrowImgv;
    @Bind(R.id.order_confirm_consignee_layout)
    FrameLayout orderConfirmConsigneeLayout;
    @Bind(R.id.product_list_gallery_lyaout)
    LinearLayout productListGalleryLyaout;
    @Bind(R.id.order_product_scrollv)
    HorizontalScrollView orderProductScrollv;
    @Bind(R.id.order_product_count_txtv)
    TextView orderProductCountTxtv;
    @Bind(R.id.order_product_flayout)
    FrameLayout orderProductFlayout;
    @Bind(R.id.order_deliver_aview)
    SPArrowRowView orderDeliverAview;
    @Bind(R.id.order_coupon_aview)
    SPArrowRowView orderCouponAview;
    @Bind(R.id.order_invoce_aview)
    SPArrowRowView orderInvoceAview;
    @Bind(R.id.title_goodsfee_txtv)
    TextView titleGoodsfeeTxtv;
    @Bind(R.id.fee_goodsfee_txtv)
    TextView feeGoodsfeeTxtv;
    @Bind(R.id.title_shopping_txtv)
    TextView titleShoppingTxtv;
    @Bind(R.id.fee_shopping_txtv)
    TextView feeShoppingTxtv;
    @Bind(R.id.title_coupon_txtv)
    TextView titleCouponTxtv;
    @Bind(R.id.fee_coupon_txtv)
    TextView feeCouponTxtv;
    @Bind(R.id.title_point_txtv)
    TextView titlePointTxtv;
    @Bind(R.id.fee_point_txtv)
    TextView feePointTxtv;
    @Bind(R.id.fee_amount_txtv)
    TextView feeAmountTxtv;
    @Bind(R.id.confirm_scrollv)
    ScrollView confirmScrollv;
    @Bind(R.id.btn_1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
    @Bind(R.id.rl_btn)
    RelativeLayout rlBtn;
    @Bind(R.id.shipping_name_txtv)
    TextView shippingNameTxtv;
    @Bind(R.id.shipping_num_txtv)
    TextView shippingNumTxtv;
    @Bind(R.id.ll_shipping)
    LinearLayout llShipping;

    private String status = "";
    private Context mContext;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    private final String mMode = "00";//01测试环境、00
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_detail));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        if (getIntent() == null || getIntent().getStringExtra("type") == null || getIntent().getStringExtra("orderId") == null) {
            showToast(getString(R.string.toast_no_ordersn_data));
            this.finish();
            return;
        }
        rlBtn.setVisibility(View.GONE);
        status = getIntent().getStringExtra("type");
        orderId =      getIntent().getStringExtra("orderId");
    }

    @Override
    public void initData() {
        getOrderDetail(orderId);
    }

    private void showData(SPOrder mOrder) {
        switch (status) {
            case "待发货":
                rlBtn.setVisibility(View.GONE);
                break;
            case "待收货":
                btn1.setVisibility(View.GONE);
                btn2.setText("确认收货");
                llShipping.setVisibility(View.VISIBLE);
                queryOrderLogistics(mOrder);
                break;
                
        }
        if (!SSUtils.isEmpty(mOrder.getOrderID())) {
            orderOrdersnTxtv.setText("订单编号：" + mOrder.getOrderSN());
        }
        if (!SSUtils.isEmpty(status)) {
            orderStatusTxtv.setText(status);
        }
        if (!SSUtils.isEmpty(mOrder.getProvince()) && !SSUtils.isEmpty(mOrder.getProvince()) && !SSUtils.
                isEmpty(mOrder.getDistrict()) && !SSUtils.isEmpty(mOrder.getTown())) {
            String firstPart = SPPersonDao.getInstance(this).queryFirstRegion(mOrder.getProvince(),
                    mOrder.getCity(), mOrder.getDistrict(), mOrder.getTown());
            orderAddressTxtv.setText(firstPart);

        }
//地址
        if (!SSUtils.isEmpty(mOrder.getConsignee()) && !SSUtils.isEmpty(mOrder.getMobile())) {
            orderConsigneeTxtv.setText(mOrder.getConsignee() + "  " + mOrder.getMobile());
        }
//        商品总额
        if (!SSUtils.isEmpty(mOrder.getGoodsPrice())) {
            feeGoodsfeeTxtv.setText("¥ " + mOrder.getGoodsPrice());
        }
//        运费
        if (!SSUtils.isEmpty(mOrder.getCouponPrice())) {
            feeCouponTxtv.setText("¥ " + mOrder.getCouponPrice());
        }
//        优惠券
        if (!SSUtils.isEmpty(mOrder.getShippingPrice())) {
            feeShoppingTxtv.setText("¥ " + mOrder.getShippingPrice());
        }
//        石头
        if (!SSUtils.isEmpty(mOrder.getIntegralMoney())) {
            feePointTxtv.setText("¥ " + mOrder.getIntegralMoney());
        }
//
        if (!SSUtils.isEmpty(mOrder.getOrderAmount())) {
            feeAmountTxtv.setText("实付款：¥ " + mOrder.getOrderAmount());
        }
        dealModel(mOrder.getProducts());
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void clickOk(int actionType) {

    }

//    public void refreshData(){
//
//        showLoadingToast();
//        SPPersonRequest.getOrderDetailByID(mOrderId, new SPSuccessListener() {
//            @Override
//            public void onRespone(String msg, Object response) {
//                mOrder = (SPOrder) response;
//                try {
//                    if (mOrder != null) {
//                        dealModel();
////                        refreshView();
//                        //load 商品金额信息
//                    }
//                } catch (Exception e) {
//                    showToast(e.getMessage());
//                }
//                hideLoadingToast();
//            }
//        }, new SPFailuredListener(OrderDetailActivity.this) {
//            @Override
//            public void onRespone(String msg, int errorCode) {
//                hideLoadingToast();
//                showToast(msg);
//            }
//        });
//    }

    /**
     * 处理数据
     */
    public void dealModel(List<SPProduct> products) {

        if (products == null) return;

        try {
            //设置商品图片
            buildProductGallery(products);

            if (products != null || products.size() > 0) {
                orderProductCountTxtv.setText("共" + products.size() + "件商品");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildProductGallery(List<SPProduct> products) {
        for (int i = 0; i < products.size(); i++) {
            String url = products.get(i).getImageThumlUrl();
            View view = LayoutInflater.from(this).inflate(R.layout.activity_index_gallery_item, productListGalleryLyaout, false);
            ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
            productListGalleryLyaout.addView(view);
        }
    }


    @OnClick({R.id.btn_1, R.id.btn2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                if ("待支付".equals(status)) {
                    cancelOrder(mOrder);
                }

                break;
            case R.id.btn2:
                if ("待支付".equals(status)) {
                    payOrder(mOrder);
                }
                if ("待收货".equals(status)) {
                    confirmOrder(mOrder);
                }
                break;
        }
    }

    //   取消订单
    private void cancelOrder(final SPOrder order) {
        showLoadingToast("正在操作");
        cancelOrder(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                CommentOrderDetailActivity.this.setResult(RESULT_OK);
                finish();
            }
        }, new SPFailuredListener(CommentOrderDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    public void cancelOrder(String orderId, SPSuccessListener successListener, SPFailuredListener failuredListener) {
        SPPersonRequest.cancelOrderWithOrderID(orderId, successListener, failuredListener);
    }


    /**
     * 支付
     *
     * @param data
     */
    private void dealWithPay(String data) {
//        hud.dismiss();
        int ret = UPPayAssistEx.startPay(mContext, null, null, data.trim(), mMode);
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
                UPPayAssistEx.installUPPayPlugin(mContext);
                dialog.dismiss();
//                hud.dismiss();
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

    private void payOrder(SPOrder payOrder) {
        showLoadingToast("正在支付");

        Float sum1 = SSUtils.string2float(payOrder.getOrderAmount());
        String sum = SSUtils.float2String(sum1 * 100);
        ShopRequest.orderUnionPay(payOrder.getOrderID(), sum, new SPSuccessListener() {
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
     * 查看物流
     *
     * @param orderId
     * @param successListener
     * @param failuredListener
     */
    public void queryOrderLogistics(String orderId, SPSuccessListener successListener, SPFailuredListener failuredListener) {
        SPPersonRequest.queryOrderWithOrderID(orderId, successListener, failuredListener);
    }

    //   查看物流
    private void queryOrderLogistics(final SPOrder order) {
        showLoadingToast("正在操作");
        queryOrderLogistics(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
//                showToast(msg);
                JSONObject jsonObject = (JSONObject) response;
                try {
                    String invoice_no = jsonObject.get("invoice_no").toString();
                    String shipping_name = jsonObject.get("shipping_name").toString();
//                    TextView shippingNameTxtv;
//                    @Bind(R.id.shipping_num_txtv)
//                    TextView shippingNumTxtv;
                    shippingNameTxtv.setText("物流名称：" + shipping_name);
                    shippingNumTxtv.setText("物流单号：" + invoice_no);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new SPFailuredListener(CommentOrderDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
    /**
     * 确认收货
     * @param orderId
     */
    public void confirmOrder(String orderId, SPSuccessListener successListener, SPFailuredListener failuredListener) {
        SPPersonRequest.confirmOrderWithOrderID(orderId, successListener, failuredListener);
    }

    //   确认收货
    private void confirmOrder(final SPOrder order) {
        showLoadingToast("正在操作");
        confirmOrder(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                CommentOrderDetailActivity.this.setResult(RESULT_OK);
                finish();
            }
        }, new SPFailuredListener(CommentOrderDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
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
                showToast(" 支付成功！ ");
//                refreshData();
            } else if (str.equals("fail")) {
                showToast(" 支付失败！ ");

            } else if (str.equals("cancel")) {
                showToast(" 你已取消了本次订单的支付！ ");
            }
        }
    }
    /**
     *  启动支付页面支付
     *
     *  @param orderID order description
     */
    public void getOrderDetail(String orderID){
        showLoadingToast("正在操作");

        //进入支付页面支付
        SPPersonRequest.getOrderDetail(orderID, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    SPOrder order = (SPOrder) response;
                    showData(order);
                }
                hideLoadingToast();

            }
        }, new SPFailuredListener(CommentOrderDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();

                showToast(msg);
            }
        });
    }

}
