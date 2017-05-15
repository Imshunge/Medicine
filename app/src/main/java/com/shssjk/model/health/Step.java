package com.shssjk.model.health;

import org.litepal.crud.DataSupport;
import java.io.Serializable;

/**
 * Created by Administrator on 2017-03-29.
 * 计步器
 */

public class Step  extends DataSupport implements Serializable {
    private String time;
    private int count;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
