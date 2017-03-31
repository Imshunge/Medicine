
package com.shssjk.utils;

import android.content.Context;
import android.widget.Toast;

/** 
 * @author
 * @version
 * @Description
 * @category 
 */
public class SPDialogUtils {

	
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
 
