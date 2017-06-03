package com.shssjk.activity;

import java.util.ArrayList;
import java.util.List;
/**
 * @version 1.0
 * @Description Activity集合
 *
 *  在BaseActivity  调用的 onCreate方法中调用 ，确保每个Activity都加入到集合中
 *   @Override protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
    ActivityCollector.addActivity(this); //添加到集合
    }
 *
 * */
import android.app.Activity;

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 程序退出时调用，清除所以Activity
     *  ActivityCollector.finishAll();
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
