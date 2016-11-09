package com.shssjk.model.shop;


import com.shssjk.model.Model;

import java.io.Serializable;

public class Product  implements Model, Serializable {

    private String goods_id;
    private String cat_id;
    private String extend_cat_id;
    private String goods_sn;
    private String goods_name;
    private String click_count;
    private String brand_id;
    private String store_count;
    private String comment_count;
    private String weight;
    private String market_price;
    private String shop_price;
    private String cost_price;
    private String keywords;
    private String goods_remark;
    private String goods_content;
    private String original_img;
    private String is_real;
    private String is_on_sale;
    private String is_free_shipping;
    private String on_time;
    private String sort;
    private String is_recommend;
    private String is_new;
    private String is_hot;
    private String last_update;
    private String goodsType;
    private String spec_type;
    private String give_integral;
    private String exchange_integral;
    private String suppliers_id;
    private String sales_sum;
    private String prom_type;
    private String prom_id;
    private String commission;
//    private String spu;
//    private String sku;
    public void setGoodsId(String goods_id) {
         this.goods_id = goods_id;
     }
     public String getGoodsId() {
         return goods_id;
     }

    public void setCatId(String cat_id) {
         this.cat_id = cat_id;
     }
     public String getCatId() {
         return cat_id;
     }

    public void setExtendCatId(String extend_cat_id) {
         this.extend_cat_id = extend_cat_id;
     }
     public String getExtendCatId() {
         return extend_cat_id;
     }

    public void setGoodsSn(String goods_sn) {
         this.goods_sn = goods_sn;
     }
     public String getGoodsSn() {
         return goods_sn;
     }

    public void setGoodsName(String goods_name) {
         this.goods_name = goods_name;
     }
     public String getGoodsName() {
         return goods_name;
     }

    public void setClickCount(String click_count) {
         this.click_count = click_count;
     }
     public String getClickCount() {
         return click_count;
     }

    public void setBrandId(String brand_id) {
         this.brand_id = brand_id;
     }
     public String getBrandId() {
         return brand_id;
     }

    public void setStoreCount(String store_count) {
         this.store_count = store_count;
     }
     public String getStoreCount() {
         return store_count;
     }

    public void setCommentCount(String comment_count) {
         this.comment_count = comment_count;
     }
     public String getCommentCount() {
         return comment_count;
     }

    public void setWeight(String weight) {
         this.weight = weight;
     }
     public String getWeight() {
         return weight;
     }

    public void setMarketPrice(String market_price) {
         this.market_price = market_price;
     }
     public String getMarketPrice() {
         return market_price;
     }

    public void setShopPrice(String shop_price) {
         this.shop_price = shop_price;
     }
     public String getShopPrice() {
         return shop_price;
     }

    public void setCostPrice(String cost_price) {
         this.cost_price = cost_price;
     }
     public String getCostPrice() {
         return cost_price;
     }

    public void setKeywords(String keywords) {
         this.keywords = keywords;
     }
     public String getKeywords() {
         return keywords;
     }

    public void setGoodsRemark(String goods_remark) {
         this.goods_remark = goods_remark;
     }
     public String getGoodsRemark() {
         return goods_remark;
     }

    public void setGoodsContent(String goods_content) {
         this.goods_content = goods_content;
     }
     public String getGoodsContent() {
         return goods_content;
     }

    public void setOriginalImg(String original_img) {
         this.original_img = original_img;
     }
     public String getOriginalImg() {
         return original_img;
     }

    public void setIsReal(String is_real) {
         this.is_real = is_real;
     }
     public String getIsReal() {
         return is_real;
     }

    public void setIsOnSale(String is_on_sale) {
         this.is_on_sale = is_on_sale;
     }
     public String getIsOnSale() {
         return is_on_sale;
     }

    public void setIsFreeShipping(String is_free_shipping) {
         this.is_free_shipping = is_free_shipping;
     }
     public String getIsFreeShipping() {
         return is_free_shipping;
     }

    public void setOnTime(String on_time) {
         this.on_time = on_time;
     }
     public String getOnTime() {
         return on_time;
     }

    public void setSort(String sort) {
         this.sort = sort;
     }
     public String getSort() {
         return sort;
     }

    public void setIsRecommend(String is_recommend) {
         this.is_recommend = is_recommend;
     }
     public String getIsRecommend() {
         return is_recommend;
     }

    public void setIsNew(String is_new) {
         this.is_new = is_new;
     }
     public String getIsNew() {
         return is_new;
     }

    public void setIsHot(String is_hot) {
         this.is_hot = is_hot;
     }
     public String getIsHot() {
         return is_hot;
     }

    public void setLastUpdate(String last_update) {
         this.last_update = last_update;
     }
     public String getLastUpdate() {
         return last_update;
     }

    public void setGoodsType(String goodsType) {
         this.goodsType = goodsType;
     }
     public String getGoodsType() {
         return goodsType;
     }

    public void setSpecType(String spec_type) {
         this.spec_type = spec_type;
     }
     public String getSpecType() {
         return spec_type;
     }

    public void setGiveIntegral(String give_integral) {
         this.give_integral = give_integral;
     }
     public String getGiveIntegral() {
         return give_integral;
     }

    public void setExchangeIntegral(String exchange_integral) {
         this.exchange_integral = exchange_integral;
     }
     public String getExchangeIntegral() {
         return exchange_integral;
     }

    public void setSuppliersId(String suppliers_id) {
         this.suppliers_id = suppliers_id;
     }
     public String getSuppliersId() {
         return suppliers_id;
     }

    public void setSalesSum(String sales_sum) {
         this.sales_sum = sales_sum;
     }
     public String getSalesSum() {
         return sales_sum;
     }

    public void setPromType(String prom_type) {
         this.prom_type = prom_type;
     }
     public String getPromType() {
         return prom_type;
     }

    public void setPromId(String prom_id) {
         this.prom_id = prom_id;
     }
     public String getPromId() {
         return prom_id;
     }

    public void setCommission(String commission) {
         this.commission = commission;
     }
     public String getCommission() {
         return commission;
     }

//    public void setSpu(String spu) {
//         this.spu = spu;
//     }
//     public String getSpu() {
//         return spu;
//     }
//
//    public void setSku(String sku) {
//         this.sku = sku;
//     }
//     public String getSku() {
//         return sku;
//     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}