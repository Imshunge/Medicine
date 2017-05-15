package com.shssjk.model.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 摄像的
 * Created by Administrator on 2017-04-05.
 */
public class VideoBean extends DataSupport implements Serializable {
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;
    private String data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

}
