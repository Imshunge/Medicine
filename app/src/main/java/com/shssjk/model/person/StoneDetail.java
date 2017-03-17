package com.shssjk.model.person;

import com.shssjk.model.Model;

/**
石头详情
 */
public class StoneDetail implements Model {
    private String createTime;
    private String date;
    private String id;
    private String num;
    private String source;
    private String userId;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    private String sign;
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }


    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "createTime", "create_time",
                "userId", "user_id"
        };
    }
}