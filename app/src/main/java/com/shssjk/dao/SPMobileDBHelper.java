/**
  * Description:	数据库操作类
 * @version V1.0
 */
package com.shssjk.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shssjk.common.SPTableConstanct;


public class SPMobileDBHelper extends SQLiteOpenHelper {

	private final String TAG = "SPMobileDBHelper";
	private static final String DBNAME = "tpshop.db" ;   //数据库名称
	private static final int DBVVERSION = 1;       		// 数据库版本

	
	/**
	 * MyDBHelper构造方法
	 * @param context
	 */
	public SPMobileDBHelper(Context context) {
		super(context,DBNAME, null, DBVVERSION);
	}
	
	/**
	 *该方法在 数据库第一次创建时是被调用，方法内应完成数据库表的完成
	 * 其中SQLiteDatabase类中包含对数据库的操作方法
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(SPTableConstanct.CREATE_TABLE_CATEGORY);  	//执行有更改的sql语句
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
}
