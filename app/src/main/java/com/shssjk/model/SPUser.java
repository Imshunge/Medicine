
package com.shssjk.model;


import java.io.Serializable;

/**
 * Created by admin on 2016/6/21.
 */
public class SPUser implements Model, Serializable {

    // user_id
    String userID;

    String email;

    String password;

    //用户金额 user_money
    String userMoney;

    //消费积分 pay_points
    String payPoints;

    //优惠券数量
    String couponCount;

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    String coupon;
    String mobile;

    //性别
    String sex;

    //出生日期
    String birthday;


    //第三方来源 wx weibo alipay
    String oauth;

    //head_pic 头像URL
    String headPic;

    //openid第 三方唯一标示
    String openid;

    //nickname 昵称
    String nickname;

    String token;

    //会员等级昵称
    String levelName;
    String level;

    /************
     * 以下是额外增加字段, 数据库表无对应
     **********/
    //校验码
    String checkCode;
    //确认密码
    String password2;

    public String getDo_earnings() {
        return do_earnings;
    }

    public void setDo_earnings(String do_earnings) {
        this.do_earnings = do_earnings;
    }

    //    石头
    String do_earnings;

    public String getDo_score() {
        return do_score;
    }

    public void setDo_score(String do_score) {
        this.do_score = do_score;
    }

    //    积分
    String do_score;
    public String getHeader_pic() {
        return header_pic;
    }

    public void setHeader_pic(String header_pic) {
        this.header_pic = header_pic;
    }

    String header_pic="";
    String is_to_work="";//是否创业 （1、已创业；0未达到创业资格；2达到创业资格，但是未创

    public String getExpendamount() {
        return expendamount;
    }

    public void setExpendamount(String expendamount) {
        this.expendamount = expendamount;
    }

    public String getIs_to_work() {
        return is_to_work;
    }

    public void setIs_to_work(String is_to_work) {
        this.is_to_work = is_to_work;
    }

    String expendamount;//累计消费金额
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "userID", "user_id",
                "userMoney", "user_money",
                "payPoints", "pay_points",
                "headPic", "head_pic",
                "couponCount", "coupon_count",
                "levelName", "level_name",
        };
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    public String getPayPoints() {
        return payPoints;
    }

    public void setPayPoints(String payPoints) {
        this.payPoints = payPoints;
    }

    public String getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(String couponCount) {
        this.couponCount = couponCount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOauth() {
        return oauth;
    }

    public void setOauth(String oauth) {
        this.oauth = oauth;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
