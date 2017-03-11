package com.shssjk.activity.common.shop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.order.SPExchange;
import com.shssjk.model.order.SPOrder;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.SSUtils;
import com.unionpay.UPPayAssistEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单 -> 退货详情
 */
public class ReturnOrderDetailActivity extends OrderBaseActivity implements ConfirmDialog.ConfirmDialogListener {

    @Bind(R.id.pic_imgv)
    ImageView picImgv;
    @Bind(R.id.name_txtv)
    TextView nameTxtv;

    @Bind(R.id.order_status_txtv)
    TextView orderStatusTxtv;
    @Bind(R.id.order_describe_txtv)
    TextView orderDescribeTxtv;
    @Bind(R.id.order_remarksection_txtv)
    TextView orderRemarksectionTxtv;
    @Bind(R.id.txt_3)
    TextView txt3;
    @Bind(R.id.tv_return_address)
    TextView tvReturnAddress;
    @Bind(R.id.txt_phone)
    TextView txtPhone;
    @Bind(R.id.edit_express_name)
    EditText edit_express_name;
    @Bind(R.id.txt_phone2)
    TextView txtPhone2;
    @Bind(R.id.edit_express_num)
    EditText edit_express_num;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;

    @Bind(R.id.product_list_gallery_lyaout)
    LinearLayout productListGalleryLyaout;
    @Bind(R.id.order_product_scrollv)
    HorizontalScrollView orderProductScrollv;
    @Bind(R.id.confirm_scrollv)
    ScrollView confirmScrollv;
    @Bind(R.id.ll_address)
    LinearLayout llAddress;
    @Bind(R.id.rl_pic)
    RelativeLayout rlPic;
    private String status = "";
    private Context mContext;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    private final String mMode = "00";//01测试环境、00
    SPExchange mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_returenorder_detail));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order_detail);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        if (getIntent() == null || getIntent().getSerializableExtra("order") == null) {
            showToast(getString(R.string.toast_no_data));
            this.finish();
            return;
        }
        mOrder = (SPExchange) getIntent().getSerializableExtra("order");
    }

    @Override
    public void initData() {
        showData(mOrder);
    }

    private void showData(SPExchange mOrder) {



//
        String imgUrl1 = MobileConstants.BASE_HOST + mOrder.getOriginal_img();
        Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(picImgv);

        if (!SSUtils.isEmpty(mOrder.getGoodsName())) {
            nameTxtv.setText(mOrder.getGoodsName());
        }


//        0:申请中, 1:处理中, 2:已完成

        String str = mOrder.getStatus().trim();
        if (!SSUtils.isEmpty(mOrder.getStatus())) {
            String temp = "";
            if ("0".equals(str)) {
                temp = "申请中";
                llAddress.setVisibility(View.GONE);
            }
            if ("1".equals(str)) {
                temp = "处理中";
                if (!SSUtils.isEmpty(mOrder.getExchangeId()) && !SSUtils.isEmpty(mOrder.getShipping_name())) {
                    edit_express_name.setEnabled(false);
                    edit_express_num.setEnabled(false);
                    btnConfirm.setVisibility(View.GONE);
                }
            }
            if ("2".equals(str)) {
                temp = "已完成";
            }
            orderStatusTxtv.setText(temp);
        }

        if (!SSUtils.isEmpty(mOrder.getReason())) {
            orderDescribeTxtv.setText(mOrder.getReason());
        }
        if (!SSUtils.isEmpty(mOrder.getRemark())) {
            orderRemarksectionTxtv.setText(mOrder.getRemark());
        }
//地址相关
        if (!SSUtils.isEmpty(mOrder.getReturn_address())) {
            tvReturnAddress.setText(mOrder.getReturn_address());
        }
        if (!SSUtils.isEmpty(mOrder.getExchangeId())) {
            edit_express_num.setText(mOrder.getExchangeId());
        }
        if (!SSUtils.isEmpty(mOrder.getShipping_name())) {
            edit_express_name.setText(mOrder.getShipping_name());
        }
        if (!SSUtils.isEmpty(mOrder.getImgs())) {
              dealModel(mOrder.getImgs());
//               rlPic.setVisibility(View.GONE);
        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void clickOk(int actionType) {

    }


    /**
     * 处理图片
     */
    public void dealModel(String products) {

        if (products == null) return;

        try {
            //设置问题商品图片

            String[] items = products.split(",");
            List<String> itemList = new ArrayList<String>(Arrays.asList(items));
            buildProductGallery(itemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildProductGallery(List<String> products) {
        for (int i = 0; i < products.size(); i++) {
            String url =MobileConstants.BASE_HOST + products.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.activity_index_gallery_item, productListGalleryLyaout, false);
            ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
            productListGalleryLyaout.addView(view);
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
                ReturnOrderDetailActivity.this.setResult(RESULT_OK);
                finish();
            }
        }, new SPFailuredListener(ReturnOrderDetailActivity.this) {
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


    private void payOrder(String shipping_name,String shipping_code) {
        showLoadingToast("正在提交数据");

        ShopRequest.ordeAddReturnAddress(shipping_name, shipping_name, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    String id = (String) response;
//                    dealWithPay(id);
                    showToast(msg);
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


    @OnClick(R.id.btn_confirm)
    public void onClick() {
         String name=    edit_express_name.getText().toString().trim();
        if(!SSUtils.isEmpty(name)){
            showToast("请输入"+getString(R.string.title_express_name));
            return;
        }
        String num=    edit_express_num.getText().toString().trim();
        if(!SSUtils.isEmpty(num)){
            showToast("请输入"+getString(R.string.title_express_num));
            return;
        }
        payOrder(name,num);
    }
}

