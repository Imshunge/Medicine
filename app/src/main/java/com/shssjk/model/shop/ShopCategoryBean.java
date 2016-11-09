package com.shssjk.model.shop;
import com.shssjk.model.Model;

import java.io.Serializable;
import java.util.List;

/**

 */
public class ShopCategoryBean    implements Model, Serializable {

    private List<Data> data;
    private String msg;
    private int status;
    public void setData(List<Data> data) {
         this.data = data;
     }
     public List<Data> getData() {
         return data;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}