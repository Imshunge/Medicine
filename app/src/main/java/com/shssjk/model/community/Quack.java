package com.shssjk.model.community;

import com.shssjk.model.Model;

import java.io.Serializable;

/**
 * Auto-generated: 2016-10-18 10:56:45
 *江湖分类
 *
 */
public class Quack implements Model, Serializable {

    private String create_time;
    private String id;
    private String title;
    private String status;
    private String remark;
    public void setCreateTime(String create_time) {
         this.create_time = create_time;
     }
     public String getCreateTime() {
         return create_time;
     }

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}