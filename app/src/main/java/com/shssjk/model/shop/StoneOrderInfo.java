package com.shssjk.model.shop;
import com.shssjk.model.Model;

public class StoneOrderInfo implements Model{
    private String doEarnings;
    private String orderAmount;
    private String orderId;
    private String orderSn;
    private String userId;
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "doEarnings","do_earnings",
                "orderAmount","order_amount",
                "orderId","order_id",
                "orderSn","order_sn",
                "userId","user_id",
              };
    }
    public void setDoEarnings(String doEarnings) {
         this.doEarnings = doEarnings;
     }
     public String getDoEarnings() {
         return doEarnings;
     }

    public void setOrderAmount(String orderAmount) {
         this.orderAmount = orderAmount;
     }
     public String getOrderAmount() {
         return orderAmount;
     }

    public void setOrderId(String orderId) {
         this.orderId = orderId;
     }
     public String getOrderId() {
         return orderId;
     }

    public void setOrderSn(String orderSn) {
         this.orderSn = orderSn;
     }
     public String getOrderSn() {
         return orderSn;
     }

    public void setUserId(String userId) {
         this.userId = userId;
     }
     public String getUserId() {
         return userId;
     }

}