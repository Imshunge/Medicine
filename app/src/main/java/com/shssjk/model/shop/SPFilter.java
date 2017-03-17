
package com.shssjk.model.shop;


import com.shssjk.model.Model;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by admin on 2016/6/18.
 */
public class SPFilter implements Model {
    private String filterId;
    private String name;
    private List<SPFilterItem> items;
    private JSONArray itemJsonArray;


    public SPFilter(){};

    public SPFilter(int type , String filterId ,String name , List<SPFilterItem> items){
        this.type = type;
        this.name = name;
        this.filterId = filterId;
        this.items = items;

    }


    /**
     * 类型 type ->  1:选中菜单 , 2:规格 ,3:属性 , 4:品牌 , 5:价格
     */
    private Integer type;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{"itemJsonArray","item"};
    }


    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SPFilterItem> getItems() {
        return items;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public JSONArray getItemJsonArray() {
        return itemJsonArray;
    }

    public void setItemJsonArray(JSONArray itemJsonArray) {
        this.itemJsonArray = itemJsonArray;
    }

    public void setItems(List<SPFilterItem> items) {
        this.items = items;
    }
}
