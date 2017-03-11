package com.shssjk.model;

import org.litepal.crud.DataSupport;

/**
 * 搜索历史
 * Created by Administrator on 2017-02-16.
 */
public class Searchhistory extends DataSupport {
    public int getIdd() {
        return idd;
    }

    public void setIdd(int idd) {
        this.idd = idd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int idd;
    private String title;
    private String description;
}
