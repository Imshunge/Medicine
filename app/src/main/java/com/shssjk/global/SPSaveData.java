
package com.shssjk.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.shssjk.common.MobileConstants;
import com.shssjk.model.SPUser;
import com.shssjk.utils.SSUtils;
//存储 信息

public class SPSaveData {

    public final static String KEY_IS_FIRST_STARTUP = "is_first_startup";
    private final static String TAG = "SPSaveData";
    static SharedPreferences mShare = null;

    private static SharedPreferences getShared(Context context) {
        if (mShare == null) {
            mShare = context.getSharedPreferences(MobileConstants.APP_NAME, Context.MODE_PRIVATE);
        }
        return mShare;
    }

    public static String getString(Context context, String key) {
        return getShared(context).getString(key, "");
    }

    public static Long getLong(Context context, String key) {
        return getShared(context).getLong(key, 0L);
    }


    public static boolean getValue(Context context, String key, boolean defaultValue) {
        return getShared(context).getBoolean(key, defaultValue);
    }

    public static boolean getValue(Context context, String key) {
        return getShared(context).getBoolean(key, false);
    }

    public static void putValue(Context context, String key, String val) {
        Editor editor = getShared(context).edit();
        editor.putString(key, val);
        editor.commit();
    }
    //
    public static SPUser loadUser(Context context) {
//
        SPUser user = new SPUser();
        user.setUserID(getShared(context).getString("userId", "-1"));
        user.setNickname(getShared(context).getString("nickName", user.getNickname()));
        String couponCount = getShared(context).getString("couponCount", "0");
        String userMoney = getShared(context).getString("userMoney", "0");
        String payPoints = getShared(context).getString("payPoints", "0");
        String level = getShared(context).getString("level", "0");
        String levelName = getShared(context).getString("levelName", "0");
        String token = getShared(context).getString("token", "0");

        String do_score = getShared(context).getString("do_score", "0");
        String do_earnings = getShared(context).getString("do_earnings", "0");
        String header_pic = getShared(context).getString("header_pic", "");
        String coupon = getShared(context).getString("coupon", "0");
        String birthday = getShared(context).getString("birthday","");
        String sex = getShared(context).getString("sex","0");
        String phone = getShared(context).getString("mobile","");


        user.setCouponCount(couponCount);
        user.setUserMoney(userMoney);
        user.setPayPoints(payPoints);
        user.setLevel(level);
        user.setLevelName(levelName);
        user.setToken(token);
        user.setHeader_pic(header_pic);
        user.setDo_score(do_score);
        user.setCoupon(coupon);
        user.setDo_earnings(do_earnings);
        user.setSex(sex);
        user.setBirthday(birthday);
        user.setMobile(phone);
        return user;
    }

    public static void clearUser(Context context) {
        Editor editor = getShared(context).edit();
        editor.putString("userId", "-1");
        editor.putString("nickName", "-1");
        editor.putString("couponCount", "0");
        editor.putString("userMoney", "0");
        editor.putString("payPoints", "0");
        editor.putString("level", "0");
        editor.putString("levelName", "");
        editor.putString("token", "");
        editor.putString("do_score", "");
        editor.putString("do_earnings","");
        editor.putString("header_pic","");
        editor.putString("coupon","");
        editor.putString("birthday","");
        editor.putString("sex","");
        editor.putLong("time", 0L);
        editor.putString("mobile","");
        editor.commit();
    }
    public static void saveUser(Context context, String key, SPUser user) {
//
        Editor editor = getShared(context).edit();
        editor.putString("userId", user.getUserID());
        editor.putString("nickName", user.getNickname());
        editor.putString("do_score", user.getDo_score());
        editor.putString("do_earnings", user.getDo_earnings());
        if (!SSUtils.isEmpty(user.getHeader_pic())) {
            editor.putString("header_pic", user.getHeader_pic());
        }else{
            editor.putString("header_pic", "");
        }
        editor.putString("coupon", user.getCoupon());
        editor.putString("payPoints", String.valueOf(user.getPayPoints()));
        editor.putString("couponCount", String.valueOf(user.getCouponCount()));
        editor.putString("userMoney", String.valueOf(user.getUserMoney()));
        editor.putString("payPoints", String.valueOf(user.getPayPoints()));
        editor.putString("level", String.valueOf(user.getLevel()));
        editor.putString("levelName", String.valueOf(user.getLevelName()));
        editor.putString("token", user.getToken());
        editor.putString("birthday", user.getBirthday());
        editor.putString("sex", user.getSex());
//        time 登录时间  计算token超时时间
        editor.putLong("time", System.currentTimeMillis());
        editor.putString("mobile",user.getMobile());

        editor.commit();
    }

//    保存 推送信息
    public static void saveLoginData(Context context, String key, SPUser user){


    }

    //
    public static void putValue(Context context, String key, int val) {
        Editor editor = getShared(context).edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public static void putValue(Context context, String key, float val) {
        Editor editor = getShared(context).edit();
        editor.putFloat(key, val);
        editor.commit();
    }

    public static void putValue(Context context, String key, boolean val) {
        Editor editor = getShared(context).edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public static void removeValue(Context context, String key) {
        Editor editor = getShared(context).edit();
        editor.remove(key);
        editor.commit();
    }

    // 保存 登陆信息 包括推送信息
    public static void saveLoginData(String s, String s1, String channalId, String appId, String spikey, String deviceyppe) {

    }
    // 保存 登陆信息 用户信息 用户密码
    public static void saveLoginData(Context context,String username , String pwd) {
        Editor editor = getShared(context).edit();
        editor.putString("username",username);
        editor.putString("pwd",pwd);
        editor.commit();
    }
//    清空 登陆数据
    public static void clearLoginData(Context context) {
        Editor editor = getShared(context).edit();
        editor.putString("username","");
        editor.putString("pwd","");
        editor.commit();
    }
    }
 
