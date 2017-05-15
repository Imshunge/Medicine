package com.shssjk.activity.person;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;


import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.R;
import com.shssjk.model.bean.VideoBean;
import com.shssjk.utils.DatabaseUtil;
import com.shssjk.utils.Logger;


import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LocalVideoListActivity extends BaseActivity {
    private ArrayList<Map<String, ArrayList<String>>> ListItem;
    private DatabaseUtil mDbUtil;
    private List<String> videotimes;
    private String strDID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_vide_list);
        init();
    }
    @Override
    public void initSubViews() {
    }
    @Override
    public void initData() {
        mDbUtil = new DatabaseUtil(this);
        ListItem = new ArrayList<Map<String, ArrayList<String>>>();
        videotimes = new ArrayList<String>();
//        initPicData();

        List<VideoBean> allVideo = DataSupport.findAll(VideoBean.class);
        for (VideoBean videoBean:allVideo){
            Logger.e(this,videoBean.getPath());
        }
//        initPicData(allVideo);
        initData2(allVideo);
    }

    @Override
    public void initEvent() {

    }
    //	读取路径
    public ArrayList<MyItem> initData2(List<VideoBean> allVideo) {
        ArrayList<MyItem> items = new ArrayList<MyItem>();
        ListItem.clear();
        ListItem.add(initPicData(allVideo));//从数据库加载图片跟视频文件到ListItem里
        ListItem.add(initVideoData(allVideo));//
        for (int i = 0; i < ListItem.size(); i++) {
            Map<String, ArrayList<String>> map = ListItem.get(i);
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                MyItem item = new MyItem();
                String data = it.next();
                item.data = data;
                item.paths = map.get(data);
                item.type = item.paths.get(0).endsWith("jpg") ? 1 : 2;
                items.add(item);
            }
        }
        return items;
    }

    public class MyItem {
        public String data;
        public ArrayList<String> paths;
        public int type = -1;
    }
    private Map<String, ArrayList<String>> initPicData(List<VideoBean> allVideo) {
        Map<String, ArrayList<String>> childMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> groupList = new ArrayList<String>();
//        mDbUtil.open();
//        Cursor cursor = mDbUtil.queryAllPicture(strDID);

        for (VideoBean videoBean:allVideo){
//            Logger.e(this,videoBean.getPath());
            String path = videoBean.getPath();
            String s1 = path.substring(path.lastIndexOf("/") + 1);
            String date = s1.substring(0, 10);
            if (!groupList.contains(date)) {
                groupList.add(date);
                ArrayList<String> list = new ArrayList<String>();
                list.add(path);
                childMap.put(date, list);
                Log.e("info", "groupList:" + groupList);
                Log.e("info", "childMap:" + childMap);
            } else {
                childMap.get(date).add(path);
            }
        }
//        while (cursor.moveToNext()) {
//            String filePath = cursor.getString(cursor
//                    .getColumnIndex(DatabaseUtil.KEY_FILEPATH));
////            File file = null;
////            try {
////                file = new File(filePath);
////                if (file == null || !file.exists()) {
////                    boolean delResult = mDbUtil.deleteVideoOrPicture(strDID,
////                            filePath, DatabaseUtil.TYPE_PICTURE);
//////                    Log.e(TAG, "delResult:" + delResult);
////                    continue;
////                }
////            } catch (Exception e) {
////            }
//
//            String s1 = filePath.substring(filePath.lastIndexOf("/") + 1);
//            String date = s1.substring(0, 10);
//            if (!groupList.contains(date)) {
//                groupList.add(date);
//                ArrayList<String> list = new ArrayList<String>();
//                list.add(filePath);
//                childMap.put(date, list);
//                Log.e("info", "groupList:" + groupList);
//                Log.e("info", "childMap:" + childMap);
//            } else {
//                childMap.get(date).add(filePath);
//            }
//        }
//        mDbUtil.close();
        Collections.sort(groupList, new Comparator<String>() {
            @Override
            public int compare(String object1, String object2) {
                return object2.compareTo(object1);
            }
        });
        return childMap;
    }

    private Map<String, ArrayList<String>> initVideoData(List<VideoBean> allVideo) {
        Map<String, ArrayList<String>> childMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> groupList = new ArrayList<String>();
        mDbUtil.open();
        Cursor cursor = mDbUtil.queryAllVideo(strDID);//查询
        while (cursor.moveToNext())
        {
            String filePath = cursor.getString(cursor
                    .getColumnIndex(DatabaseUtil.KEY_FILEPATH));//根据列的下标
            // sdcard
            File file = null;
            try {
                file = new File(filePath);
                if (file == null || !file.exists()) {
                    boolean delResult = mDbUtil.deleteVideoOrPicture(strDID,
                            filePath, DatabaseUtil.TYPE_VIDEO);
//                    Log.d(TAG, "delResult:" + delResult);
                    continue;//
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String s1 = filePath.substring(filePath.lastIndexOf("/") + 1);
            if (!videotimes.contains(s1)) {
                videotimes.add(s1);
            }
            String date = s1.substring(0, 10);
            Log.d("tag", "date:" + date);
            if (!groupList.contains(date))
            {
                groupList.add(date);
                ArrayList<String> list = new ArrayList<String>();
                list.add(filePath);
                childMap.put(date, list);
            } else {
                childMap.get(date).add(filePath);
            }
        }
        mDbUtil.close();
        Collections.sort(groupList, new Comparator<String>() {
            @Override
            public int compare(String object1, String object2) {
                return object2.compareTo(object1);
            }
        });
        return childMap;
    }
    public void sort(ArrayList<MyItem> items) {
        MyItem temps;
        MyItem pre;
        MyItem after;
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size() - 1; j++) {
                pre = items.get(j);
                after = items.get(j + 1);
                if (pre.data.compareTo(after.data) < 0)
                {
                    temps = items.get(j);
                    items.set(j, items.get(j + 1));
                    items.set(j + 1, temps);
                }
            }
        }
    }

//
}
