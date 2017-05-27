/**
 * Description:	数据库操作类, 该类专门针对内置数据库操作
 * @version V1.0
 */
package com.shssjk.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


import com.shssjk.common.MobileConstants;
import com.shssjk.common.SPTableConstanct;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class SPMobileBuiltInDBHelper extends SQLiteOpenHelper {

	private final String TAG = "SPMobileBuiltInDBHelper";
	private static final int DBVVERSION = 1;       			// 数据库版本
	private static String DB2_PATH = "/data/data/com.shssjk.activity/databases/";
	private Context mContext;
	private SQLiteDatabase mDataBase;

	/**
	 * MyDBHelper构造方法
	 * @param context
	 */
	public SPMobileBuiltInDBHelper(Context context){
		super(context, MobileConstants.DB2_NAME, null, DBVVERSION);
		mContext = context;
		String packageName = context.getPackageName();
		DB2_PATH = "/data/data/"+packageName+"/databases/";
		try {
			createDataBase();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	/**
	 *该方法在 数据库第一次创建时是被调用，方法内应完成数据库表的完成
	 * 其中SQLiteDatabase类中包含对数据库的操作方法
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(SPTableConstanct.CREATE_TABLE_ADDRESS);  	//执行有更改的sql语句

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}


	/**
	 * 创建数据2
	 * Creates a empty database on the system and rewrites it with your own database.
	 * */
	public void createDataBase() throws IOException{
		boolean dbExist = checkDataBase();
		if(dbExist){
			//do nothing - database already exist
		}else{
			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * 检查数据库是否存在
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase(){
		SQLiteDatabase checkDB = null;
		try{
			String myPath = DB2_PATH + MobileConstants.DB2_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}catch(SQLiteException e){
			//database does't exist yet.
		}
		if(checkDB != null){
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * 内置数据库拷贝到db目录下
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		//Open your local db as the input stream
		InputStream myInput = mContext.getAssets().open(MobileConstants.DB2_NAME);

		// Path to the just created empty db
		String outFileName = DB2_PATH + MobileConstants.DB2_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}
		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException{
		//Open the database
		String dbPath = DB2_PATH + MobileConstants.DB2_NAME;
		mDataBase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		super.close();
		if(mDataBase != null) {
			mDataBase.close();
		}
	}

}
