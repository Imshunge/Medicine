package com.shssjk.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-12-28.
 */
public class Push extends DataSupport implements Serializable{
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return idd;
    }

    public void setId(int idd) {
        this.idd = idd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomContentString() {
        return customContentString;
    }

    public void setCustomContentString(String customContentString) {
        this.customContentString = customContentString;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private int idd;
    private String title;
    private String description;
    private String customContentString;
    private String userId;

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    private String pushTime;


}
