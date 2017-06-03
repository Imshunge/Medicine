
package com.shssjk.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @version         1.0
 * @Description   Toast 统一处理
 */
public class DialogUtils {

	
	public static void showToast(Context context , String text){
		if(SSUtils.isEmpty(text)){
			return ;
		}
		if(context==null){
			return ;
		}
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
 
