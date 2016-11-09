package com.shssjk.model.community;

import com.shssjk.model.Model;

import java.io.Serializable;

/**

 */
public class ComCategory implements Model, Serializable {

    private String create_time;
    private String icon;
    private String id;
    private String remark;
    private String status;
    private String title;
    public void setcreate_time(String create_time) {
         this.create_time = create_time;
     }
     public String getcreate_time() {
         return create_time;
     }

    public void setIcon(String icon) {
         this.icon = icon;
     }
     public String getIcon() {
         return icon;
     }

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}