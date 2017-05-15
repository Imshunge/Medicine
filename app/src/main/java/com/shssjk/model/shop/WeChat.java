package com.shssjk.model.shop;

import com.shssjk.model.Model;

/**
 * Created by Administrator on 2017-03-31.
 */
public class WeChat implements Model{
    private String sign;

    private int timestamp;

    private String packageStr;

    private String partnerid;

    private String noncestr;

    private String appid;

    private String prepayid;

    public void setSign(String sign){
        this.sign = sign;
    }
    public String getSign(){
        return this.sign;
    }
    public void setTimestamp(int timestamp){
        this.timestamp = timestamp;
    }
    public int getTimestamp(){
        return this.timestamp;
    }
    public void setPackage(String packageStr){
        this.packageStr = packageStr;
    }
    public String getPackage(){
        return this.packageStr;
    }
    public void setPartnerid(String partnerid){
        this.partnerid = partnerid;
    }
    public String getPartnerid(){
        return this.partnerid;
    }
    public void setNoncestr(String noncestr){
        this.noncestr = noncestr;
    }
    public String getNoncestr(){
        return this.noncestr;
    }
    public void setAppid(String appid){
        this.appid = appid;
    }
    public String getAppid(){
        return this.appid;
    }
    public void setPrepayid(String prepayid){
        this.prepayid = prepayid;
    }
    public String getPrepayid(){
        return this.prepayid;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {


        return new String[]{
                "packageStr","package"

        };
    }
}
