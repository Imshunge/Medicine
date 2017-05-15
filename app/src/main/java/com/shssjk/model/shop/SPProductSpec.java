package com.shssjk.model.shop;


import com.shssjk.model.Model;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/20.
 */
public class SPProductSpec implements Model,Serializable ,Comparable<SPProductSpec>  {

   /**
    *  规格ID
    */
    String itemID;

    /**
     *  规格值
     */
    String item;

    /**
     *  规格名称
     */
    String specName;

    /**
     *  URL
     */
    String src;


    public String getStore_count() {
        return store_count;
    }

    public void setStore_count(String store_count) {
        this.store_count = store_count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String price;
    String store_count;
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
            "itemID","item_id",
            "specName","spec_name"
        };
    }

    @Override
    public int compareTo(SPProductSpec another) {

        return this.specName.compareTo(another.specName);
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
