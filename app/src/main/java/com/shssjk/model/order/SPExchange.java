
package com.shssjk.model.order;


import com.shssjk.model.Model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/7/4.
 */
public class SPExchange implements Model ,Serializable {

    String exchangeId;
    String orderId;
    String orderSN;
    String goodsId;
    String goodsName;

    public String getOriginal_img() {
        return original_img;
    }

    public void setOriginal_img(String original_img) {
        this.original_img = original_img;
    }

    public String getReturn_address() {
        return return_address;
    }

    public void setReturn_address(String return_address) {
        this.return_address = return_address;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public String getShipping_code() {
        return shipping_code;
    }

    public void setShipping_code(String shipping_code) {
        this.shipping_code = shipping_code;
    }

    String original_img;
    String return_address;
    String shipping_name;
    String shipping_code;
    /**
     * 0:退货 ,  1:换货
     */
    String type;

    /**
     * 退换货原因
     */
    String reason;

    /**
     * 申请时间
     */
    String addtime;

    /**
     * 0:申请中, 1:处理中, 2:已完成
     */
    String status;

    /**
     * 客服备注
     */
    String remark;

    //问题图片
    JSONArray imageArray;

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    String   imgs;
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "orderId", "order_id",
                "orderSN", "order_sn",
                "goodsId", "goods_id",
                "exchangeId", "id",
                "goodsName", "goods_name",

        };
    }


    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public JSONArray getImageArray() {
        return imageArray;
    }

    public void setImageArray(JSONArray imageArray) {
        this.imageArray = imageArray;
    }
}
