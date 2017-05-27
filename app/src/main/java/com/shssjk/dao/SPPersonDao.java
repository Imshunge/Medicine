/**
 * Description:	用户中心数据操作类
 * @version V1.0
 */
package com.shssjk.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shssjk.common.SPTableConstanct;
import com.shssjk.model.person.SPRegionModel;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/27.
 */
public class SPPersonDao {

    public final static int LEVEL_PROVINCE = 1;
    public final static int LEVEL_CITY = 2;
    public final static int LEVEL_DISTRICT = 3;
    public final static int LEVEL_TOWN = 4;

    private final String TAG = "SPPersonDao";

    private SPMobileBuiltInDBHelper dbHelper = null;

    private static SPPersonDao instance = null;

    private SPPersonDao(Context context) {
        this.dbHelper = new SPMobileBuiltInDBHelper(context);
    }

    public static synchronized SPPersonDao getInstance(Context context){
        if(null==instance){
            instance = new SPPersonDao(context);
        }
        return instance;
    }

    public void insertRegionList(List<SPRegionModel> regions){

        if(regions == null)return;

        SQLiteDatabase dbwrite = null;

        try {
            dbwrite = dbHelper.getWritableDatabase();
            //1. 先删除旧数据
            String deleteSQL = "delete from sp_address";
            dbwrite.execSQL(deleteSQL);

            for(int i=0; i<regions.size(); i++){
                SPRegionModel region = regions.get(i);

                String name = region.getName();
                String regionID = region.getRegionID();
                String parent_id = region.getParentID();
                String level = region.getLevel();
                String insertSQL = "insert into sp_address(id , name , parent_id , level)values(?,?,?,?)";
                ContentValues cv = new ContentValues();
                cv.put("id", regionID);
                cv.put("name", name);
                cv.put("parent_id", parent_id);
                cv.put("level", level);
                dbwrite.insert( SPTableConstanct.TABLE_NAME_ADDRESS, null, cv );
            }
        } catch (Exception e) {
            Log.w(TAG, "insertBatch occur error : " + e.getMessage());
        }finally{
            dbwrite.close();
        }
    }

    /**
     *  获取某个级别的地址
     *
     *  @param level level description
     *
     *  @return return value description
     */
    public List<SPRegionModel> queryRegionByLevel(int level){

        List<SPRegionModel> list = new ArrayList<SPRegionModel>();
        SQLiteDatabase dbwrite = dbHelper.getWritableDatabase();

        String [] columns = new String[]{"id" , "name" , "parent_id" , "level"};
        String selection = " level = ? ";
        String [] selectionArgs = new String[]{String.valueOf(level)};
        try {
            Cursor cursor = dbwrite.query(SPTableConstanct.TABLE_NAME_ADDRESS, columns, selection, selectionArgs, null, null, null);
            try {
                if(cursor != null){
                    while(cursor.moveToNext()){
                        String regionID = cursor.getString(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String parentId = cursor.getString(cursor.getColumnIndex("parent_id"));

                        SPRegionModel region = new SPRegionModel();
                        region.setName(name);
                        region.setLevel(String.valueOf(level));
                        region.setParentID(parentId);
                        region.setRegionID(regionID);
                        list.add(region);
                    }
                }
            } catch (Exception e) {
                Log.w(TAG , " queryAddressByLevel occur error : "+e.getMessage());
            }finally{
                cursor.close();
            }

        } catch (Exception e) {
            Log.w(TAG , " queryAddressByLevel occur error : "+e.getMessage());
        }finally{
            dbwrite.close();
        }
        return list;
    }

    /**
     *  获取下级地址信息
     *  @return return value description
     */
    public List<SPRegionModel> queryRegionByParentID(String parentID){

        List<SPRegionModel> list = new ArrayList<SPRegionModel>();
        SQLiteDatabase dbwrite = dbHelper.getWritableDatabase();

        String [] columns = new String[]{"id" , "name" , "parent_id" , "level"};
        String selection = " parent_id = ? ";
        String [] selectionArgs = new String[]{parentID};
        try {
            Cursor cursor = dbwrite.query(SPTableConstanct.TABLE_NAME_ADDRESS, columns, selection, selectionArgs, null, null, null);
            try {
                if(cursor != null){
                    while(cursor.moveToNext()) {
                        String regionID = cursor.getString(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String level = cursor.getString(cursor.getColumnIndex("level"));

                        SPRegionModel region = new SPRegionModel();
                        region.setName(name);
                        region.setLevel(level);
                        region.setParentID(parentID);
                        region.setRegionID(regionID);

                        list.add(region);
                    }
                }
            }catch (Exception e) {
                Log.w(TAG , " queryAddressByParentID occur error : "+e.getMessage());
            }finally{
                cursor.close();
            }
        } catch (Exception e) {
            Log.w(TAG , " queryAddressByParentID occur error : "+e.getMessage());
        }finally{
            dbwrite.close();
        }
        return list;
    }


    /**
     *  根据ID, 获取省市县数据, 空格分隔
     *
     *  @return return value description
     */
    public String queryNameById(String reionID){

        String name = "";
        SQLiteDatabase dbwrite = dbHelper.getWritableDatabase();

        String [] columns = new String[]{"name"};
        String selection = " id = ? ";
        String [] selectionArgs = new String[]{reionID};

        try {
            Cursor cursor = dbwrite.query(SPTableConstanct.TABLE_NAME_ADDRESS, columns, selection, selectionArgs, null, null, null);
            try {
                if(cursor != null) {
                    while (cursor.moveToNext()) {
                       name = cursor.getString(cursor.getColumnIndex("name"));
                       break;
                    }
                }
            }catch (Exception e) {
                Log.w(TAG , " queryNameById occur error : "+e.getMessage());
            }finally{
                cursor.close();
            }
        } catch (Exception e) {
            Log.w(TAG , " queryNameById occur error : "+e.getMessage());
        }finally{
            dbwrite.close();
        }
        return name;
    }

    /**
     *  根据ID, 获取省市县数据, 空格分隔
     *
     *  @return return value description
     */
   public String queryFirstRegion(String provinceID , String cityID ,  String districtID , String streetID){

       List<String> list = new ArrayList<String>();
       SQLiteDatabase dbwrite = dbHelper.getWritableDatabase();

       String [] columns = new String[]{"name"};
       String selection = " id IN(? , ? , ?  , ? ) ";
       String [] selectionArgs = new String[]{provinceID , cityID , districtID , streetID};

       if (SPStringUtils.isEmpty(streetID)){
           selection = " id IN(? , ? , ? ) ";
           selectionArgs = new String[]{provinceID , cityID , districtID};
       }
       try {
           Cursor cursor = dbwrite.query(SPTableConstanct.TABLE_NAME_ADDRESS, columns, selection, selectionArgs, null, null, null);
           try {
               if(cursor != null) {
                   while (cursor.moveToNext()) {
                       String name = cursor.getString(cursor.getColumnIndex("name"));
                       list.add(name);
                   }
               }
            }catch (Exception e) {
               Log.w(TAG , " queryFirstAddress occur error : "+e.getMessage());
           }finally{
               cursor.close();
           }
       } catch (Exception e) {
           Log.w(TAG , " queryFirstAddress occur error : "+e.getMessage());
       }finally{
           dbwrite.close();
       }
       String firstAddr = null;
       if (list.size() > 0){
           firstAddr =  SPStringUtils.listToString(list, " ");
       }
        return firstAddr;
    }

}
