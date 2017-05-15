package com.shssjk.activity.person;
/**
 * 摄像机 列表界面
 */

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ipcamera.demo.BridgeService;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.CameraAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.person.Camera;
import com.shssjk.utils.ContentCommon;
import com.shssjk.utils.SystemValue;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vstc2.nativecaller.NativeCaller;

public class CameraListActivity extends BaseActivity implements BridgeService.IpcamClientInterface,
        BridgeService.AddCameraInterface,BridgeService.CallBackMessageInterface {
    private static final int DATACHANGE = 3;
    @Bind(R.id.camera_listv)
    ListView camera_listv;
    @Bind(R.id.empty_txtv)
    TextView emptyTxtv;
    @Bind(R.id.empty_lstv)
    RelativeLayout emptyLstv;
    @Bind(R.id.add_address_btn)
    Button addAddressBtn;
    CameraAdapter cameraAdapter;
    List<Camera> cameraList;
    private Context mContext;
    private int option = ContentCommon.INVALID_OPTION;
    private String strUser = "admin";
    private String strPwd = "888888";
    private int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
    private static final String STR_MSG_PARAM = "msgparam";
    private static final String STR_DID = "did";
    private Intent intentbrod = null;
    private Boolean clicked = true;
    private Boolean intentData;//摄像头服务会出现反复连接问题，所以设置了一个跳转状态参数 true：可以跳转，false:不可跳转
    private Camera camera;
    private MyBroadCast receiver;
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            Intent in = new Intent(CameraListActivity.this, AddCameraActivity.class);
//            startActivity(in);
//            finish();
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.cameralistactivity_title));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_list);
        mContext=this;
        BridgeService.setAddCameraInterface(this);
        BridgeService.setCallBackMessage(this);
        receiver = new MyBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("finish");
        registerReceiver(receiver, filter);
        intentbrod = new Intent("drop");
        ButterKnife.bind(this);
        super.init();
    }

    @Override
    public void callBackSearchResultData(int cameraType, String strMac, String strName, String strDeviceID, String strIpAddr, int port) {

    }

    @Override
    public void CallBackGetStatus(String did, String resultPbuf, int cmd) {

    }

    private class MyBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            CameraListActivity.this.finish();
            Log.d("ip", "AddCameraActivity.this.finish()");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        intentData=true;
//        getList();
    }
    @Override
    public void initSubViews() {
        View emptyView = findViewById(R.id.empty_lstv);
        camera_listv.setEmptyView(emptyView);
        camera_listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                camera = (Camera) cameraAdapter.getItem(position);
                sendData(camera.getName(), camera.getDid());
            }
        });
        camera_listv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                final Camera camera = (Camera) cameraAdapter.getItem(pos);
                new AlertDialog.Builder(mContext)
                        .setMessage("您确定要删除该摄像机吗？").setTitle("系统提示")
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(true)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCamera(camera);
                            }
                        }).show();
                return true;
            }
        });





    }

    private void sendData(String strName, String strDID) {
//        mydialog = new MyPrograssDialog(Me_video.this, "设备连接中...");
//        mydialog.setCanceledOnTouchOutside(false);
//        mydialog.show();
        showLoadingToast("设备连接中...");
        Intent in = new Intent();

        if (strDID.length() == 0) {
            Toast.makeText(CameraListActivity.this, getResources().getString(R.string.input_camera_id), Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // if (strUser.length() == 0)
        // {
        // Toast.makeText(Me_video.this,"设备名不可为空", Toast.LENGTH_SHORT).show();
        // return;
        // }
        if (option == ContentCommon.INVALID_OPTION) {
            option = ContentCommon.ADD_CAMERA;
        }
        in.putExtra(ContentCommon.CAMERA_OPTION, option);
        in.putExtra(ContentCommon.STR_CAMERA_NAME, strName);
        in.putExtra(ContentCommon.STR_CAMERA_ID, strDID);
        in.putExtra(ContentCommon.STR_CAMERA_USER, strUser);
        in.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);
        in.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);
        SystemValue.deviceName = strName;
        SystemValue.deviceUser = strUser;
        SystemValue.deviceId = strDID;
        SystemValue.devicePass = strPwd;
        BridgeService.setIpcamClientInterface(this);// 启动服务
        NativeCaller.Init();
        new Thread(new StartPPPPThread()).start();
    }

    //   删除摄像机
    private void deleteCamera(final Camera camera) {
        showLoadingToast();
        PersonRequest.deleteCameraByDid(camera.getDid(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    int state = (int) response;
                    showToast(msg);
                    if (state == 0) {
//                        getDeviceListData();
                        cameraList.remove(camera);
                        cameraAdapter.setData(cameraList);
                    }
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener(CameraListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });

    }

    @Override
    public void initData() {
//        initCamera();
        getDeviceListData();
   }
//    初始化摄像机配置
    private void initCamera() {
//        Intent intent = new Intent();
//        intent.setClass(CameraListActivity.this, BridgeService.class);
//        startService(intent);
//        NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");

////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
//////                    Thread.sleep(3000);
//////                    Message msg = new Message();
//////                    mHandler.sendMessage(msg);
////                } catch (Exception e) {
////                 e.printStackTrace();
////                }
////            }
////        }).start();
    }

    private void getDeviceListData() {
        showLoadingToast();
        PersonRequest.getCameraList(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    cameraList = (List<Camera>) response;
//                    dealModels();
                    cameraAdapter.setData(cameraList);
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener(CameraListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    @Override
    public void initEvent() {
        cameraAdapter = new CameraAdapter(this);
        camera_listv.setAdapter(cameraAdapter);

    }

    @OnClick(R.id.add_address_btn)
    public void onClick() {
        Intent intent = new Intent(CameraListActivity.this, AddCameraActivity.class);
        startActivityForResult(intent, DATACHANGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getDeviceListData();
    }

    @Override
    public void BSMsgNotifyData(String did, int type, int param) {
        Bundle bd = new Bundle();
        Message msg = PPPPMsgHandler.obtainMessage();
        msg.what = type;
        bd.putInt(STR_MSG_PARAM, param);
        bd.putString(STR_DID, did);
        msg.setData(bd);
        PPPPMsgHandler.sendMessage(msg);
        if (type == ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
            intentbrod.putExtra("ifdrop", param);
            sendBroadcast(intentbrod);
        }
    }

    @Override
    public void BSSnapshotNotify(String did, byte[] bImage, int len) {

    }

    @Override
    public void callBackUserParams(String did, String user1, String pwd1, String user2, String pwd2, String user3, String pwd3) {

    }

    @Override
    public void CameraStatus(String did, int status) {

    }
    class StartPPPPThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
                startCameraPPPP();
            } catch (Exception e) {

            }
        }
    }

    // 连接摄像头
    private void startCameraPPPP() {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        int result = NativeCaller.StartPPPP(SystemValue.deviceId, SystemValue.deviceUser, SystemValue.devicePass, 1,
                "");// 开启摄像头
    }
    private Handler PPPPMsgHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bd = msg.getData();
            int msgParam = bd.getInt(STR_MSG_PARAM);
            int msgType = msg.what;
            Log.i("aaa", "====" + msgType + "--msgParam:" + msgParam);
            String did = bd.getString(STR_DID);
            switch (msgType) {
                case ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS:
                    int resid;
                    switch (msgParam) {
                        case ContentCommon.PPPP_STATUS_CONNECTING:// 0 连接中。。。
                            resid = R.string.pppp_status_connecting;
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_FAILED:// 3 连接失败
                            resid = R.string.pppp_status_connect_failed;
                            closeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_DISCONNECT:// 4 断线
                            resid = R.string.pppp_status_disconnect;
                            closeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_INITIALING:// 1 正在初始化
                            resid = R.string.pppp_status_initialing;
                            break;
                        case ContentCommon.PPPP_STATUS_INVALID_ID:// 5  ID号无效
                            resid = R.string.pppp_status_invalid_id;
                            closeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_ON_LINE:// 2 在线状态
                            resid = R.string.pppp_status_online;
                            // progressBar.setVisibility(View.GONE);
                            // 摄像机在线之后读取摄像机类型
                            String cmd = "get_status.cgi?loginuse=admin&loginpas=" + SystemValue.devicePass + "&user=admin&pwd="
                                    + SystemValue.devicePass;
                            NativeCaller.TransferMessage(did, cmd, 0);
                            closeDialog();
                            setIntent();
                            break;
                        case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE:// 6 不在线
                            resid = R.string.device_not_on_line;
                            closeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT:// 7 连接超时
                            resid = R.string.pppp_status_connect_timeout;
                            closeDialog();
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_ERRER:// 8 密码错误
                            resid = R.string.pppp_status_pwd_error;
                            closeDialog();
                            break;
                        default:
                            resid = R.string.pppp_status_unknown;
                            closeDialog();
                    }
                    showToast(getResources().getString(resid));
                    System.out.println(getResources().getString(resid));
                    if (msgParam == ContentCommon.PPPP_STATUS_ON_LINE) {
                        NativeCaller.PPPPGetSystemParams(did, ContentCommon.MSG_TYPE_GET_PARAMS);
                    }
                    if (msgParam == ContentCommon.PPPP_STATUS_INVALID_ID
                            || msgParam == ContentCommon.PPPP_STATUS_CONNECT_FAILED
                            || msgParam == ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE
                            || msgParam == ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT
                            || msgParam == ContentCommon.PPPP_STATUS_CONNECT_ERRER) {
                        NativeCaller.StopPPPP(did);
                    }
                    break;
                case ContentCommon.PPPP_MSG_TYPE_PPPP_MODE:
                    break;

            }

        }
    };
    //关闭进度框
    public void closeDialog(){

        hideLoadingToast();
//		Toast("连接成功");
        clicked = true;
    }
    // 跳转预览模式
    public void setIntent() {
        if (intentData) {
            intentData = false;
            Intent intent = new Intent(CameraListActivity.this, PlayActivity.class);
            intent.putExtra("name", camera.getName());
            intent.putExtra("id", camera.getDid());
            startActivity(intent);
        } else {
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 弹出确定退出对话框

//            if (mydialog!=null&&mydialog.isShowing()) {
//                mydialog.dismiss();
//                int result = ;// 开启摄像头
//                return false;
//            NativeCaller.StopPPPP(SystemValue.deviceId);
//            }
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                CameraListActivity.this.finish();
                return false;
            }
            // 这里不需要执行父类的点击事件，所以直接return
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
