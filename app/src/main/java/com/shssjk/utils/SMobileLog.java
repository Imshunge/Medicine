package com.shssjk.utils;

import android.util.Log;

/**
 *
 */
public class SMobileLog {

	//发布的时候改成false
	public static boolean DEBUG = true;
	
	public static void i(String TAG , String  msg) {
		if(DEBUG)Log.i(TAG  , msg);
	}

	public static void e(String TAG , String  msg) {
		if(DEBUG)Log.e(TAG, msg);;
	}


}
