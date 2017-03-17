package com.shssjk.common;

import android.os.Handler;
import android.os.Message;

import com.shssjk.MainActivity;
import com.shssjk.utils.SPStringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Checker {
	private String TAG = "'Checker";
	static {
//		System.loadLibrary("curl");
//        System.loadLibrary("SPMobile");
    }
	
	public static native boolean Init();    
    public static native int Check(String header, String url);    
    public static native void Finished();
    public void responseMessage(String mesage){
       try {
            if(!SPStringUtils.isEmpty(mesage) && MainActivity.getmInstance() != null && MainActivity.getmInstance().mHandler != null ){
                Handler handler = MainActivity.getmInstance().mHandler;
                Message message = handler.obtainMessage(MobileConstants.MSG_CODE_SHOW);
                JSONObject jsonObject = new JSONObject(mesage);
                if(jsonObject.has("msg")){
                    message.obj = jsonObject.getString("msg");
                }
                handler.sendMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
