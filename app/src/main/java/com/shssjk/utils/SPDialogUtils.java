/******************************************************************************
 * Copyright (C) 2015 ShenZhen Moze Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳市么子信息技术有限公司开发研制，未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/

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
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}


}
 
