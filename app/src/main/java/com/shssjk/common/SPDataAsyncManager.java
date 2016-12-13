
package com.shssjk.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;


import com.shssjk.MainActivity;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.category.SPCategoryRequest;
import com.shssjk.http.home.SPHomeRequest;
import com.shssjk.model.SPCategory;
import com.shssjk.model.SPPlugin;
import com.shssjk.model.SPServiceConfig;
import com.shssjk.utils.SMobileLog;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPMyFileTool;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/27.
 */
public class SPDataAsyncManager {

    private String TAG = "SPDataAsyncManager";
    private Context mContext;
    private static SPDataAsyncManager instance;
    SyncListener mSyncListener;
    Handler mHandler;

    private SPDataAsyncManager(){}

    public static SPDataAsyncManager getInstance(Context context , Handler handler){
        if (instance == null){
            instance = new SPDataAsyncManager(context , handler);
        }
        return instance;
    }

    private SPDataAsyncManager(Context context, Handler handler){
        this.mHandler = handler;
        this.mContext = context;
    }

    public void syncData(){


        //是否第一次启动
        boolean isFirstStartup = SPSaveData.getValue(mContext, MobileConstants.KEY_IS_FIRST_STARTUP, true);

        if (isFirstStartup){

        }


        SPMyFileTool.clearCacheData(mContext);
        SPMyFileTool.cacheValue(mContext, SPMyFileTool.key3, SSUtils.getHost(MobileConstants.BASE_HOST));
        SPMyFileTool.cacheValue(mContext, SPMyFileTool.key4, SSUtils.getHost(MobileConstants.BASE_HOST));

        //获取一级分类
        SPCategoryRequest.getCategory(0, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {

                if (response != null) {
                    List<SPCategory> categorys = (List<SPCategory>) response;
                    MobileApplication.getInstance().setTopCategorys(categorys);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SMobileLog.e(TAG, "getAllCategory FailuredListener :" + msg);
            }
        });

        //服务配置信息
        SPHomeRequest.getServiceConfig(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<SPServiceConfig> configs = (List<SPServiceConfig>) response;
                    MobileApplication.getInstance().setServiceConfigs(configs);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {

            }
        });

        //插件配置信息
        SPHomeRequest.getServicePlugin(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    Map<String, SPPlugin> pluginMap = (Map<String, SPPlugin>) response;
                    MobileApplication.getInstance().setServicePluginMap(pluginMap);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                mSyncListener.onFailure(msg);
            }
        });

        if (SSUtils.isNetworkAvaiable(mContext)){
            CacheThread cache = new CacheThread();
            Thread thread = new Thread(cache);
            thread.start();
        }
    }



    /**
     * 开始同步数据
     * @param listen
     */
    public void startSyncData(SyncListener listen) {
        this.mSyncListener = listen;
        if(mSyncListener!=null)mSyncListener.onPreLoad();
        if(mSyncListener!=null)mSyncListener.onLoading();
        syncData();
        if(mSyncListener!=null)mSyncListener.onPreLoad();

    }

    public interface SyncListener {
        public void onPreLoad();
        public void onLoading();
        public void onFinish();
        public void onFailure(String error);
    }

    class CacheThread implements Runnable{

        public CacheThread(){
            try {
                PackageManager packageManager = null;
                ApplicationInfo applicationInfo = null;
                if (mContext==null || (packageManager = mContext.getPackageManager()) == null || (applicationInfo = mContext.getApplicationInfo()) == null )return;

                String label = packageManager.getApplicationLabel(applicationInfo).toString();//应用名称
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key6, label);
                String deviceId = MobileApplication.getInstance().getDeviceId();
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key1, deviceId);
                PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                String version = packInfo.versionName;
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key2, version);
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key5, String.valueOf(System.currentTimeMillis()));
                SPMyFileTool.cacheValue(mContext, SPMyFileTool.key8, mContext.getPackageName());

            } catch (Exception e) {
                
            }
        }

        @Override
        public void run() {
            boolean startupaa = SPSaveData.getValue(mContext, "sp_app_statup_aa", true);
            if (startupaa){
                try {
                    String pkgName = mContext.getPackageName();
//                    boolean b = Checker.Init()   ;
//                    Checker.Check("aaa", pkgName);
//                    Checker.Finished();
                    SPSaveData.putValue(mContext, "sp_app_statup_aa", false);
                } catch (Exception e) {
                   
                }
           }
        }
    }

    private void sendMessage(String msg){

        if(MainActivity.getmInstance() == null || MainActivity.getmInstance().mHandler == null)return;
        Handler handler =  MainActivity.getmInstance().mHandler;
        Message message = handler.obtainMessage(MobileConstants.MSG_CODE_SHOW);
        message.obj = msg;
        handler.sendMessage(message);
    }

}
