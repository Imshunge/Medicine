package com.shssjk.model.person;

import com.shssjk.model.Model;

/**
 * Auto-generated: 2017-03-16 17:46:19
 * 类型
  */
public class StoneType implements Model{

    private String storeShopPrice;
    private String price;
    private String barCode;
    private String jhPrice;
    private String keyName;
    private String storePrice;
    private String goodsId;
    private String zdPrice;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    private boolean isSelect;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "storeShopPrice", "store_shop_price",
                "barCode", "bar_code",
                "jhPrice", "jh_price",
                "keyName", "key_name",
                "storePrice", "store_price",
                "goodsId", "goods_id",
                "zdPrice", "zd_price",
                "storeCount", "store_count"
        };
    }
    private String storeCount;
    private String sku;
    private String commission;
    private String key;
    public void setStoreShopPrice(String storeShopPrice) {
         this.storeShopPrice = storeShopPrice;
     }
     public String getStoreShopPrice() {
         return storeShopPrice;
     }

    public void setPrice(String price) {
         this.price = price;
     }
     public String getPrice() {
         return price;
     }

    public void setBarCode(String barCode) {
         this.barCode = barCode;
     }
     public String getBarCode() {
         return barCode;
     }

    public void setJhPrice(String jhPrice) {
         this.jhPrice = jhPrice;
     }
     public String getJhPrice() {
         return jhPrice;
     }

    public void setKeyName(String keyName) {
         this.keyName = keyName;
     }
     public String getKeyName() {
         return keyName;
     }

    public void setStorePrice(String storePrice) {
         this.storePrice = storePrice;
     }
     public String getStorePrice() {
         return storePrice;
     }

    public void setGoodsId(String goodsId) {
         this.goodsId = goodsId;
     }
     public String getGoodsId() {
         return goodsId;
     }

    public void setZdPrice(String zdPrice) {
         this.zdPrice = zdPrice;
     }
     public String getZdPrice() {
         return zdPrice;
     }

    public void setStoreCount(String storeCount) {
         this.storeCount = storeCount;
     }
     public String getStoreCount() {
         return storeCount;
     }

    public void setSku(String sku) {
         this.sku = sku;
     }
     public String getSku() {
         return sku;
     }

    public void setCommission(String commission) {
         this.commission = commission;
     }
     public String getCommission() {
         return commission;
     }

    public void setKey(String key) {
         this.key = key;
     }
     public String getKey() {
         return key;
     }


}