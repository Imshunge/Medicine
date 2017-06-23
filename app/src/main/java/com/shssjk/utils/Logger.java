package com.shssjk.utils;

import android.util.Log;

/**
 * 日志打印
 * 上线项目时 isShowLog =false 将不打印log
 */
public class Logger {

	/** Log输出的控制开关 */
	public static boolean isShowLog = false;
	public static void e(Object objTag, String msg) {
		if (!isShowLog) {
			return;
		}
		String tag;
		// 如果objTag是String，则直接使用
		// 如果objTag不是String，则使用它的类名
		if (objTag instanceof String) {
			tag = (String) objTag;
		} else if (objTag instanceof Class) {
			tag = ((Class) objTag).getSimpleName();
		} else {
			tag = objTag.getClass().getSimpleName();
		}
		Log.e(tag, msg);
	}
}
