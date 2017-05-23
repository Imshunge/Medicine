package com.shssjk.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.shssjk.MainActivity;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.home.SPHomeRequest;
import com.shssjk.http.person.SPUserRequest;
import com.shssjk.model.AppUpdate;
import com.shssjk.model.SPUser;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.AlertDialog.Builder;

public class AppstartActivity extends BaseActivity {
    private Context mContext;
    private String visionCode;
    private AppUpdate appUpdate;
    /* 更新进度条 */
    private ProgressBar mProgress;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载结束 */
    private static final int DAY_SPLASH = 6;

    private Dialog mDownloadDialog;

    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    /* 下载保存路径 */
    private String mSavePath;

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 6;
    private SharedPreferences pref;
    private String username;
    private String pass;
    private String channalId;
    private String appId;
    private String spikey;
    private String deviceyppe;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                case 0:
                    installApk();
                    break;
                case DAY_SPLASH:
                    // installApk();
                    break;
                case 99:
                    // Toast.makeText(context.getActivity(), "网络异常，请确定你的手机已联网！",
                    // Toast.LENGTH_SHORT).show();
                   finish();
                    break;
            }
        }
    };
    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, appUpdate.getName());
        if (!apkfile.exists()) {
            return;
        }
        Logger.e("安装APK文件", "appUpdate.getName()"+appUpdate.getName());
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
      startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);
        mContext = this;
        super.init();
    }
    @Override
    public void initSubViews() {
    }
    @Override
    public void initData() {
        getAppVersion();
        visionCode = getVersionCode(mContext);
//
//        Long time=SPSaveData.getLong(this, "time");
//        Long hour=SSUtils.getTimeLag(time);
//        if(hour>20){
//            doLogin(username, pass, channalId, appId, spikey, deviceyppe);
//        }
//        Logger.e("AppstartActivity", time + " ");
    }
    @Override
    public void initEvent() {
    }
    //
    private void getAppVersion() {
        SPHomeRequest.getSoftVersion(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    appUpdate = (AppUpdate) response;
                    checkVersion(appUpdate.getVersion());
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }
    //    版本比较
    private void checkVersion(String path) {
        if (!TextUtils.isEmpty(path)) {

//            测试   if (visionCode.equals(path))
            if (visionCode.equals(path)) {
                startMainActivity();
                finish();
            } else {
                ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
                builder.setMessage(Html.fromHtml(appUpdate.getLog())+"");
                builder.setTitle("新版本升级");
                builder.setPositiveButton(R.string.update2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // android 6.0 动态权限申请
                        if (ContextCompat.checkSelfPermission(mContext,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions((Activity) mContext,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                        }else {
                            downloadApk();
                            showDownloadDialog();
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel2,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startMainActivity();
                            }
                        });
                builder.create().show();
            }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(mContext,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private String getVersionCode(Context context) {
        // int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            return context.getPackageManager().getPackageInfo(
                    "com.shssjk.activity", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     *
     * @author
     * @date
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                     mSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
//                    // 获得存储卡的路径
                    URL url = new URL("http://www.shssjk.com"+appUpdate.getPath());
                    Logger.e("", url + "");
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("GET");
//                    conn.setRequestProperty("Accept-Encoding", "identity");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setConnectTimeout(5000);
                    conn.connect();
                    // 获取文件大小
                    int currentLength = conn.getContentLength();
                    Logger.e("length", currentLength + "");
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, appUpdate.getName());
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    float   count = 0;
                    int numread=0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    int limit=0;
//                     float length = 0;
//                     写入到文件中
                    do {
//                        int limit=0;
                        numread = is.read(buf);
                        Logger.e("numread", numread + "");
                        count = count+numread;
                        Logger.e("count", count + "");
                        // 计算进度条位置
                        progress = (int) (((float) count / currentLength) * 100);
//                        progress = (int)(Float.parseFloat(SSUtils.getTwoPointFloatStr(count / length))*100);
                        Logger.e("progress", progress + "");
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            Logger.e("下载完成", "下载完成");
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                        limit++;
                        Logger.e("limit", limit + "");
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                    }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }
    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
//        .
        builder.setView(v);
        builder.setCancelable(false);
        // 取消更新
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置取消状态
                        cancelUpdate = true;
                        showFailureDialog();
                    }
                });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 下载文件
        downloadApk();
    }
    public void showFailureDialog() {
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
        builder.setMessage("下载失败！");
        builder.setTitle("系统提示");
        builder.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
                // android 6.0 动态权限申请
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = false;
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("暂不更新",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startMainActivity();
                    }
                });
        builder.create().show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                downloadApk();
                showDownloadDialog();
            } else
            {
                showToast("禁止使用储存权限将会导致软件更新功能异常!");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
