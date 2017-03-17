package com.shssjk.activity.shop;

import android.content.Intent;
import android.os.Bundle;

import com.shssjk.activity.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.order.SPOrder;


/**
 * @author
 *  订单 公共类
 */
public abstract class OrderBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }
    /**
     * 去支付
     * @param order
     */
    public void gotoPay(SPOrder order){
        Intent payIntent = new Intent(this , PayListActivity.class);
        payIntent.putExtra("order", order);
        startActivity(payIntent);
    }

    /**
     * 取消订单
     * @param orderId
     */
    public void cancelOrder(String orderId , SPSuccessListener successListener , SPFailuredListener failuredListener){
        PersonRequest.cancelOrderWithOrderID(orderId, successListener, failuredListener);
    }

    /**
     * 确认收货
     * @param
     */
    public void confirmOrderWithOrderID(String orderId , SPSuccessListener successListener , SPFailuredListener failuredListener){
        PersonRequest.confirmOrderWithOrderID(orderId, successListener, failuredListener);
    }



    /**
     * 联系客服
     */
    public void connectCustomer(){
//        String qq = SPServerUtils.getCustomerQQ();
//        String customerUrl = "mqqwpa://im/chat?chat_type=wpa&uin="+qq+"&version=1";
//        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(customerUrl)));
    }

    /**
     * 查看物流
     */
    public void lookShopping(SPOrder order){
        String shippingUrl = String.format(MobileConstants.SHIPPING_URL, order.getOrderID());
        startWebViewActivity(shippingUrl, "查看物流");
    }
}