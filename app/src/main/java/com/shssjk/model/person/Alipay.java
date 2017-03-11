package com.shssjk.model.person;

/**
 * 支付宝接口返回数据
 * Created by Administrator on 2017-02-27.
 */
public class Alipay {
    private String private_key;

    private String sign_data;

    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getSign_data() {
        return sign_data;
    }

    public void setSign_data(String sign_data) {
        this.sign_data = sign_data;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }
}
