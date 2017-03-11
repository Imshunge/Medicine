package com.shssjk.receiver;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.shssjk.activity.common.information.ArticleDetailActivity;
import com.shssjk.activity.common.shop.ProductActivity;
import com.shssjk.activity.common.shop.ProductAllActivity;
import com.shssjk.activity.common.user.PushMessageDetailActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.model.Push;

import com.shssjk.utils.Logger;


import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class MyPushMessageReceiver extends PushMessageReceiver {
    /** TAG to Log */
    public static final String TAG = MyPushMessageReceiver.class
            .getSimpleName();
    public final int MODE_PRIVATE = 0x0000;

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context
     *            BroadcastReceiver的执行Context
     * @param errorCode
     *            绑定接口返回值，0 - 成功
     * @param appid
     *            应用id。errorCode非0时为null
     * @param userId
     *            应用user id。errorCode非0时为null
     * @param channelId
     *            应用channel id。errorCode非0时为null
     * @param requestId
     *            向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
            String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Logger.e(TAG, responseString);

        if (errorCode == 0) {
            // 绑定成功
            Log.d(TAG, "绑定成功");
            // 保存数据
            SharedPreferences.Editor editor = context.getSharedPreferences("com.shssjk.activity.push",
                    MODE_PRIVATE).edit();
            editor.putString("channalId", channelId);
            editor.putString("userId", userId);
            editor.putString("appId", appid);
            editor.commit();

        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//        updateContent(context, responseString);
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context
     *            上下文
     * @param message
     *            推送的消息
     * @param customContentString
     *            自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
            String customContentString) {
        String messageString = "透传消息 onMessage=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);

        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//        updateContent(context, messageString);
    }

    /**
     * 接收通知到达的函数。
     *
     * @param context
     *            上下文
     * @param title
     *            推送的通知的标题
     * @param description
     *            推送的通知的描述
     * @param customContentString
     *            自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
            String description, String customContentString) {

        String notifyString = "通知到达 onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
//        updateContent(context, notifyString);
        saveData(title, description, customContentString);

    }

    private void saveData(String title, String description, String customContentString) {
        String uid="";
        if (MobileApplication.getInstance().isLogined){
             uid =MobileApplication.getInstance().getLoginUser().getUserID();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳

//        System.out.println(date);
        Push push = new Push();
        push.setCustomContentString(customContentString);
        push.setDescription(description);
        push.setTitle(title);
        push.setUserId(uid);
        push.setPushTime(date);
        push.save();
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context
     *            上下文
     * @param title
     *            推送的通知的标题
     * @param description
     *            推送的通知的描述
     * @param customContentString
     *            自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
            String description, String customContentString) {
        String notifyString = "通知点击 onNotificationClicked title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            String mytype = "";
            String myvalue = null;
            try {
                customJson = new JSONObject(customContentString);
                if (!customJson.isNull("TYPE")) {
                    mytype = customJson.getString("TYPE");
                    myvalue = customJson.getString("VALUE");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//
            Intent intent = new Intent();
           // 资讯
            if (mytype.equals("ZIXUN")) {// 资讯

                intent = new Intent(context.getApplicationContext(), ArticleDetailActivity.class);
                intent.putExtra("article_id", myvalue);
                intent.putExtra("article_title", "热门资讯");
                intent.putExtra("id", mytype);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
//
//                intent.setClass(context.getApplicationContext(),
//                        Infor_infor.class);
//                intent.putExtra("infomations", myvalue);// ID
//                intent.putExtra("infomations0", "热门资讯");// 名称
//                intent.putExtra("infomations00", mytype);// 类别
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.getApplicationContext().startActivity(intent);
            }
                else if (mytype.equals("GOOD")) {// 商品
				intent.setClass(context.getApplicationContext(),
                        ProductAllActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("goodsId", myvalue);// ID
                context.getApplicationContext().startActivity(intent);
			}
            else if (mytype.equals("TEST")) {// 健康云
                if (context != null) {
                    context.sendBroadcast(new Intent(MobileConstants.ACTION_HEALTH_CHANGE_FRAGMENT));
                }
            }
        }else{
            if (title.equals("血压测试通知")||title.equals("血糖测试通知")) {
                //跳转健康云界面
                if (context != null) {
                    context.sendBroadcast(new Intent(MobileConstants.ACTION_HEALTH_CHANGE_FRAGMENT));
                }
            }else if(title.equals("血糖测试通知")){
                //跳转健康云界面
                if (context != null) {
                    context.sendBroadcast(new Intent(MobileConstants.ACTION_HEALTH_CHANGE_FRAGMENT));
                }
            }else{
                updateContent(context, title, description, customContentString);
            }

        }
        Logger.e("MyPushMessageReceiver title", title + "");
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//        updateContent(context, notifyString);
    }    
    
    /**
     * setTags() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param
     *
     * @param failTags
     *            设置失败的tag
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * delTags() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param
     *
     * @param failTags
     *            删除失败的tag
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * listTags() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示列举tag成功；非0表示失败。
     * @param tags
     *            当前应用设置的所有tag。
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
            String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // 解绑定成功
            Log.d(TAG, "解绑成功");
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//        updateContent(context, responseString);

    }

    private void updateContent(Context context, String content) {
        this.updateContent(context,content,"","");
    }



    //消息查看详情

    private void updateContent(Context context, String title, String content,
                               String customsString) {
        Push push = new Push();
        push.setTitle(title);
        push.setDescription(content);
        push.setCustomContentString(customsString);
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), PushMessageDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("push", push);
        intent.putExtras(bundle);
        context.getApplicationContext().startActivity(intent);
    }


}
