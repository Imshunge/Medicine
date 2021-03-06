
package com.shssjk.model.shop;


import com.shssjk.model.Model;

/**
 * Created by admin on 2016/6/21.
 */
public class SPCollect implements Model {

    // collect_id - 收藏ID
   String collectID;

    // goods_id - 商品ID
   String goodsID;

    // goods_name - 商品名称
   String goodsName;

    // shop_price - 商品价格
   String shopPrice;

/****额外增加字段, 在对应的数据库表中没有对应的字段*******/
    //商品缩略图
   String imageThumlUrl;


    public String getOriginal_img() {
        return original_img;
    }

    String  original_img="";

    
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
            "collectID","collect_id",
            "goodsID","goods_id",
            "goodsName","goods_name",
            "shopPrice","shop_price"
        };
    }

    public String getCollectID() {
        return collectID;
    }

    public void setCollectID(String collectID) {
        this.collectID = collectID;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getImageThumlUrl() {
        return imageThumlUrl;
    }

    public void setImageThumlUrl(String imageThumlUrl) {
        this.imageThumlUrl = imageThumlUrl;
    }
}
