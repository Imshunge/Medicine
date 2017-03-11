package com.shssjk.model.shop;

import com.shssjk.activity.common.shop.CommentList;
import com.shssjk.model.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2016-12-19 9:45:54
 *商品评价
 */
public class ProductCommnet  implements Model, Serializable {

    private String add_time;
    private String address;
    private String admin_note;
    private String bar_code;
    private String city;

    public List<CommentList> getCommentList() {
        return comment_list;
    }

    public void setCommentList(List<CommentList> commentList) {
        this.comment_list = commentList;
    }

    private List<CommentList> comment_list;
    private String confirm_time;
    private String consignee;
    private String country;
    private String coupon_price;
    private String delivery_id;
    private String discount;
    private String district;
    private String email;
    private String give_integral;
    private String goods_id;
    private String goods_name;
    private String goods_num;
    private String goods_price;
    private String goods_sn;
    private String integral;
    private String integral_money;
    private String invoice_title;
    private String is_comment;
    private String is_distribut;
    private int is_return;
    private String is_send;
    private String market_price;
    private String member_goods_price;
    private String mobile;
    private String order_amount;
    private String order_id;
    private String order_prom_amount;
    private String order_prom_id;
    private String order_sn;
    private String order_status;
    private String original_img;
    private String pay_code;
    private String pay_name;
    private String pay_status;
    private String pay_time;
    private String prom_id;
    private String prom_type;
    private String province;
    private String ratio;
    private String ratio_price;
    private String rec_id;
    private String shipping_code;
    private String shipping_name;
    private String shipping_price;
    private String shipping_status;
    private String shipping_time;
    private String sku;
    private String spec_key;
    private String spec_key_name;
    private String total_amount;
    private String ttotal;
    private String twon;
    private String type;
    private String user_id;
    private String user_money;
    private String user_note;
    private String zipcode;
    public void setAddTime(String add_time) {
         this.add_time = add_time;
     }
     public String getAddTime() {
         return add_time;
     }

    public void setAddress(String address) {
         this.address = address;
     }
     public String getAddress() {
         return address;
     }

    public void setAdminNote(String admin_note) {
         this.admin_note = admin_note;
     }
     public String getAdminNote() {
         return admin_note;
     }

    public void setBarCode(String bar_code) {
         this.bar_code = bar_code;
     }
     public String getBarCode() {
         return bar_code;
     }

    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }



    public void setConfirmTime(String confirm_time) {
         this.confirm_time = confirm_time;
     }
     public String getConfirmTime() {
         return confirm_time;
     }

    public void setConsignee(String consignee) {
         this.consignee = consignee;
     }
     public String getConsignee() {
         return consignee;
     }

    public void setCountry(String country) {
         this.country = country;
     }
     public String getCountry() {
         return country;
     }

    public void setCouponPrice(String coupon_price) {
         this.coupon_price = coupon_price;
     }
     public String getCouponPrice() {
         return coupon_price;
     }

    public void setDeliveryId(String delivery_id) {
         this.delivery_id = delivery_id;
     }
     public String getDeliveryId() {
         return delivery_id;
     }

    public void setDiscount(String discount) {
         this.discount = discount;
     }
     public String getDiscount() {
         return discount;
     }

    public void setDistrict(String district) {
         this.district = district;
     }
     public String getDistrict() {
         return district;
     }

    public void setEmail(String email) {
         this.email = email;
     }
     public String getEmail() {
         return email;
     }

    public void setGiveIntegral(String give_integral) {
         this.give_integral = give_integral;
     }
     public String getGiveIntegral() {
         return give_integral;
     }

    public void setGoodsId(String goods_id) {
         this.goods_id = goods_id;
     }
     public String getGoodsId() {
         return goods_id;
     }

    public void setGoodsName(String goods_name) {
         this.goods_name = goods_name;
     }
     public String getGoodsName() {
         return goods_name;
     }

    public void setGoodsNum(String goods_num) {
         this.goods_num = goods_num;
     }
     public String getGoodsNum() {
         return goods_num;
     }

    public void setGoodsPrice(String goods_price) {
         this.goods_price = goods_price;
     }
     public String getGoodsPrice() {
         return goods_price;
     }

    public void setGoodsSn(String goods_sn) {
         this.goods_sn = goods_sn;
     }
     public String getGoodsSn() {
         return goods_sn;
     }

    public void setIntegral(String integral) {
         this.integral = integral;
     }
     public String getIntegral() {
         return integral;
     }

    public void setIntegralMoney(String integral_money) {
         this.integral_money = integral_money;
     }
     public String getIntegralMoney() {
         return integral_money;
     }

    public void setInvoiceTitle(String invoice_title) {
         this.invoice_title = invoice_title;
     }
     public String getInvoiceTitle() {
         return invoice_title;
     }

    public void setIsComment(String is_comment) {
         this.is_comment = is_comment;
     }
     public String getIsComment() {
         return is_comment;
     }

    public void setIsDistribut(String is_distribut) {
         this.is_distribut = is_distribut;
     }
     public String getIsDistribut() {
         return is_distribut;
     }

    public void setIsReturn(int is_return) {
         this.is_return = is_return;
     }
     public int getIsReturn() {
         return is_return;
     }

    public void setIsSend(String is_send) {
         this.is_send = is_send;
     }
     public String getIsSend() {
         return is_send;
     }

    public void setMarketPrice(String market_price) {
         this.market_price = market_price;
     }
     public String getMarketPrice() {
         return market_price;
     }

    public void setMemberGoodsPrice(String member_goods_price) {
         this.member_goods_price = member_goods_price;
     }
     public String getMemberGoodsPrice() {
         return member_goods_price;
     }

    public void setMobile(String mobile) {
         this.mobile = mobile;
     }
     public String getMobile() {
         return mobile;
     }

    public void setOrderAmount(String order_amount) {
         this.order_amount = order_amount;
     }
     public String getOrderAmount() {
         return order_amount;
     }

    public void setOrderId(String order_id) {
         this.order_id = order_id;
     }
     public String getOrderId() {
         return order_id;
     }

    public void setOrderPromAmount(String order_prom_amount) {
         this.order_prom_amount = order_prom_amount;
     }
     public String getOrderPromAmount() {
         return order_prom_amount;
     }

    public void setOrderPromId(String order_prom_id) {
         this.order_prom_id = order_prom_id;
     }
     public String getOrderPromId() {
         return order_prom_id;
     }

    public void setOrderSn(String order_sn) {
         this.order_sn = order_sn;
     }
     public String getOrderSn() {
         return order_sn;
     }

    public void setOrderStatus(String order_status) {
         this.order_status = order_status;
     }
     public String getOrderStatus() {
         return order_status;
     }

    public void setOriginalImg(String original_img) {
         this.original_img = original_img;
     }
     public String getOriginalImg() {
         return original_img;
     }

    public void setPayCode(String pay_code) {
         this.pay_code = pay_code;
     }
     public String getPayCode() {
         return pay_code;
     }

    public void setPayName(String pay_name) {
         this.pay_name = pay_name;
     }
     public String getPayName() {
         return pay_name;
     }

    public void setPayStatus(String pay_status) {
         this.pay_status = pay_status;
     }
     public String getPayStatus() {
         return pay_status;
     }

    public void setPayTime(String pay_time) {
         this.pay_time = pay_time;
     }
     public String getPayTime() {
         return pay_time;
     }

    public void setPromId(String prom_id) {
         this.prom_id = prom_id;
     }
     public String getPromId() {
         return prom_id;
     }

    public void setPromType(String prom_type) {
         this.prom_type = prom_type;
     }
     public String getPromType() {
         return prom_type;
     }

    public void setProvince(String province) {
         this.province = province;
     }
     public String getProvince() {
         return province;
     }

    public void setRatio(String ratio) {
         this.ratio = ratio;
     }
     public String getRatio() {
         return ratio;
     }

    public void setRatioPrice(String ratio_price) {
         this.ratio_price = ratio_price;
     }
     public String getRatioPrice() {
         return ratio_price;
     }

    public void setRecId(String rec_id) {
         this.rec_id = rec_id;
     }
     public String getRecId() {
         return rec_id;
     }

    public void setShippingCode(String shipping_code) {
         this.shipping_code = shipping_code;
     }
     public String getShippingCode() {
         return shipping_code;
     }

    public void setShippingName(String shipping_name) {
         this.shipping_name = shipping_name;
     }
     public String getShippingName() {
         return shipping_name;
     }

    public void setShippingPrice(String shipping_price) {
         this.shipping_price = shipping_price;
     }
     public String getShippingPrice() {
         return shipping_price;
     }

    public void setShippingStatus(String shipping_status) {
         this.shipping_status = shipping_status;
     }
     public String getShippingStatus() {
         return shipping_status;
     }

    public void setShippingTime(String shipping_time) {
         this.shipping_time = shipping_time;
     }
     public String getShippingTime() {
         return shipping_time;
     }

    public void setSku(String sku) {
         this.sku = sku;
     }
     public String getSku() {
         return sku;
     }

    public void setSpecKey(String spec_key) {
         this.spec_key = spec_key;
     }
     public String getSpecKey() {
         return spec_key;
     }

    public void setSpecKeyName(String spec_key_name) {
         this.spec_key_name = spec_key_name;
     }
     public String getSpecKeyName() {
         return spec_key_name;
     }

    public void setTotalAmount(String total_amount) {
         this.total_amount = total_amount;
     }
     public String getTotalAmount() {
         return total_amount;
     }

    public void setTtotal(String ttotal) {
         this.ttotal = ttotal;
     }
     public String getTtotal() {
         return ttotal;
     }

    public void setTwon(String twon) {
         this.twon = twon;
     }
     public String getTwon() {
         return twon;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setUserId(String user_id) {
         this.user_id = user_id;
     }
     public String getUserId() {
         return user_id;
     }

    public void setUserMoney(String user_money) {
         this.user_money = user_money;
     }
     public String getUserMoney() {
         return user_money;
     }

    public void setUserNote(String user_note) {
         this.user_note = user_note;
     }
     public String getUserNote() {
         return user_note;
     }

    public void setZipcode(String zipcode) {
         this.zipcode = zipcode;
     }
     public String getZipcode() {
         return zipcode;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}