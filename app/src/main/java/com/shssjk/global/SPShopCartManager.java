package com.shssjk.global;

import android.content.Context;
import android.content.Intent;

import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.utils.SMobileLog;


/**
 * Created by admin on 2016/6/21.
 * 购物车数量更新
 */
public class SPShopCartManager {

    private String TAG = "SPShopCartManager";
    private static SPShopCartManager instance ;
    private int shopCount;
    private Context mContent;

    private SPShopCartManager(){

    }

    public static SPShopCartManager getInstance(Context content){
        if (instance == null){
            instance = new SPShopCartManager();
            instance.mContent = content;
            if (MobileApplication.getInstance().isLogined){
                instance.initData();
            }
        }
        return instance;
    }

    public void initData(){
        shopCount = 0 ;
        //hasFirstStartup = NO;
        ShopRequest.getShopCartNumber(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                shopCount = Integer.valueOf(response.toString());
                SMobileLog.i(TAG, "ShopRequest.getShopCartNumber : " + shopCount);
                //购物车状态改变广播
                if (mContent != null)
                    mContent.sendBroadcast(new Intent(MobileConstants.ACTION_SHOPCART_CHNAGE));
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SMobileLog.i(TAG, "ShopRequest.getShopCartNumber msg : " + msg);
            }
        });
    }


    /**
    操作购物车商品数量
    @remark 商品详情对购物车的操作
    Cart/addCart?goods_spec[尺码]=3&goods_spec[颜色]=4&goods_num=2&goods_id=1
     */
    public void shopCartGoodsOperation(String goodsID , String specs , int number , final SPSuccessListener success  ,final SPFailuredListener failure){

        ShopRequest.shopCartGoodsOperation(goodsID, specs, number, new SPSuccessListener() {

            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    shopCount = Integer.valueOf(response.toString());
                    if (success != null) {
                        success.onRespone("success", shopCount);
                    }
                    if (mContent != null)
                        mContent.sendBroadcast(new Intent(MobileConstants.ACTION_SHOPCART_CHNAGE));
                }
            }
        }, new SPFailuredListener(failure.getViewController()) {

            @Override
            public void onRespone(String msg, int errorCode) {
                if (failure != null) {
                    failure.onRespone(msg, errorCode);
                }
            }
        });
    }

    public void reloadCart(){
        initData();
    }

    /**
     * 刷新购物车数据
     */
    public void resetShopCart(){
        shopCount = 0;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }
}
