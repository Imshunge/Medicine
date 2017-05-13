package com.shssjk.model.shop;
import com.shssjk.model.Model;

import java.io.Serializable;
import java.util.List;

/**
 
 */
public class GoodsPrice implements Model, Serializable {

    private String id;
    private String name;
    private List<GoodsList> goods_list;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setGoodsList(List<GoodsList> goods_list) {
         this.goods_list = goods_list;
     }
     public List<GoodsList> getGoodsList() {
         return goods_list;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}