package com.shssjk.model.shop;
import com.shssjk.model.Model;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class HomeResult implements Model, Serializable {

    private List<Goods> goods;
    private List<Ad> ad;
    private List<Five> five;
    public void setGoods(List<Goods> goods) {
         this.goods = goods;
     }
     public List<Goods> getGoods() {
         return goods;
     }

    public void setAd(List<Ad> ad) {
         this.ad = ad;
     }
     public List<Ad> getAd() {
         return ad;
     }

    public void setFive(List<Five> five) {
         this.five = five;
     }
     public List<Five> getFive() {
         return five;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}