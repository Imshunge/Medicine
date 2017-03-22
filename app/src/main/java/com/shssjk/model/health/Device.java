package com.shssjk.model.health;


import com.shssjk.model.Model;

import java.io.Serializable;

public class Device implements Model,Serializable{
    private String defaultStr="";  //default 、是否是最关心设备（1是 0 否）
    private String create_time="";

    private String group_name="";
    private String id="";
    private String name="";
    private String relation="";
    private String status="";
    private String title="";
    private String uid="";
    private String value="";
    public void setCreateTime(String create_time) {
         this.create_time = create_time;
     }
     public String getCreateTime() {
         return create_time;
     }

    public void setDefault(String defaultStr) {
         this.defaultStr = defaultStr;
     }
     public String getDefault() {
         return defaultStr;
     }

    public void setGroupName(String group_name) {
         this.group_name = group_name;
     }
     public String getGroupName() {
         return group_name;
     }

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

    public void setRelation(String relation) {
         this.relation = relation;
     }
     public String getRelation() {
         return relation;
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

    public void setUid(String uid) {
         this.uid = uid;
     }
     public String getUid() {
         return uid;
     }

    public void setValue(String value) {
         this.value = value;
     }
     public String getValue() {
         return value;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "defaultStr","default"
        };
    }
    @Override
    public String toString() {
        return ""+relation;
    }
}