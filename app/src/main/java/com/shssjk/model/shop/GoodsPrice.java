package com.shssjk.model.shop;

import java.io.Serializable;

/**
 
 */
public class GoodsPrice implements  Serializable {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStore_count() {
        return store_count;
    }

    public void setStore_count(String store_count) {
        this.store_count = store_count;
    }

    private  String key;
    private String price;
    private String store_count;

}