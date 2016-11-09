package com.shssjk.model.shop;

/**

 */
public class GoodsList {
    private String goods_id;
    private String goods_name;
    private String original_img;
    private String shop_price;
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

    public void setOriginalImg(String original_img) {
         this.original_img = original_img;
     }
     public String getOriginalImg() {
         return original_img;
     }

    public void setShopPrice(String shop_price) {
         this.shop_price = shop_price;
     }
     public String getShopPrice() {
         return shop_price;
     }

}