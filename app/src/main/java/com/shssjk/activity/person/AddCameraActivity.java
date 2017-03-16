package com.shssjk.activity.person;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ipcamera.demo.BridgeService;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.SearchListAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.utils.ContentCommon;
import com.shssjk.utils.SSUtils;
import com.shssjk.utils.SystemValue;

import java.util.Map;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vstc2.nativecaller.NativeCaller;

import static com.shssjk.utils.SSUtils.getFromAssets;

/**
 * 添加摄像机
 */
public class AddCameraActivity extends BaseActivity implements BridgeService.AddCameraInterface
        , BridgeService.IpcamClientInterface, BridgeService.CallBackMessageInterface {
    @Bind(R.id.bankcard_user_txtv)
    TextView consigneeNameTxtv;
    @Bind(R.id.bankcard_user_edtv)
    EditText consigneeNameEdtv;
    @Bind(R.id.device_name_txtv)
    TextView deviceNameTxtv;
    @Bind(R.id.device_id_edtv)
    EditText deviceIdEdtv;
    @Bind(R.id.btn_link)
    Button btnLink;
    @Bind(R.id.btn_preview)
    Button btnPreview;
    @Bind(R.id.btn_searchCamera)
    Button btnSearchCamera;
    @Bind(R.id.btn_settingwifi)
    Button btnSettingwifi;
    @Bind(R.id.camera_state_txtv)
    TextView cameraStateTxtv;
    @Bind(R.id.main_model_progressBar1)
    ProgressBar progressBar;
    @Bind(R.id.login_top_linear)
    LinearLayout loginTopLinear;
    private EditText didEdit = null;
    private TextView textView_top_show = null;
    private Button done;
//    @Bind(R.id.titlebar_menu_btn)
     Button explainBtn;
    private static final int SEARCH_TIME = 3000;
    private int option = ContentCommon.INVALID_OPTION;
    private int CameraType = ContentCommon.CAMERA_TYPE_MJPEG;
    // private Button btnSearchCamera;
    private SearchListAdapter listAdapter = null;
    private ProgressDialog progressdlg = null;
    private boolean isSearched;
    private MyBroadCast receiver;
    private WifiManager manager = null;
//    private ProgressBar progressBar = null;
    private static final String STR_DID = "did";
    private static final String STR_MSG_PARAM = "msgparam";
    private MyWifiThread myWifiThread = null;
    private boolean blagg = false;
    private Intent intentbrod = null;
    private WifiInfo info = null;
    boolean bthread = true;
    private Button button_play = null;
    private Button button_setting = null;
    private Button pic_video = null;
    private Button button_linkcamera = null;
    private int tag = 0;
    private String strUser = "admin";
    private String strPwd = "888888";
    private Button button_setwifi;
    private EditText nameEdit;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.camera_add),true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera);
        ButterKnife.bind(this);
        mContext=this;
        super.init();
    }
    @Override
    public void initSubViews() {
        explainBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        explainBtn.setBackgroundResource(R.drawable.title_right_dot_selector);
        explainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(getFromAssets(("guild_camera.txt"), mContext), "使用说明");
            }
        });
        listAdapter = new SearchListAdapter(this);
        progressdlg = new ProgressDialog(this);
        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdlg.setMessage(getString(R.string.searching_tip));
    }
    @Override
    public void initData() {

    }
    @Override
    public void initEvent() {
        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        BridgeService.setAddCameraInterface(this);
        BridgeService.setCallBackMessage(this);
        receiver = new MyBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("finish");
        registerReceiver(receiver, filter);
        intentbrod = new Intent("drop");
//        explainBtn.setOnClickListener(this);
    }
    @Override
    public void callBackSearchResultData(int cameraType, String strMac, String strName, String strDeviceID,
                                         String strIpAddr, int port) {
        if (!listAdapter.AddCamera(strMac, strName, strDeviceID)) {
            return;
        }
    }
    @Override
    public void CallBackGetStatus(String did, String resultPbuf, int cmd) {
        if (cmd == ContentCommon.CGI_IEGET_STATUS) {
            String cameraType = spitValue(resultPbuf, "upnp_status=");
            int intType = Integer.parseInt(cameraType);
            int type14 = (int) (intType >> 16) & 1;// 14位 来判断是否报警联动摄像机
            if (intType == 2147483647) {// 特殊值
                type14 = 0;
            }
            if (type14 == 1) {
                updateListHandler.sendEmptyMessage(2);
            }
        }
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
    public void callBackUserParams(String did, String user1, String pwd1, String user2, String pwd2,
                                   String user3, String pwd3) {

    }

    @Override
    public void CameraStatus(String did, int status) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        blagg = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (myWifiThread != null) {
            blagg = false;
        }
        progressdlg.dismiss();
        NativeCaller.StopSearch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        NativeCaller.Free();
        Intent intent = new Intent();
        intent.setClass(this, BridgeService.class);
        stopService(intent);
        tag = 0;
    }
    class MyTimerTask extends TimerTask {

        public void run() {
            updateListHandler.sendEmptyMessage(100000);
        }
    }

    private class MyBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            finish();
            Log.d("ip", "AddCameraActivity.this.finish()");
        }
    }

    public static String int2ip(long ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    private void startSearch() {
        listAdapter.ClearAll();
        progressdlg.setMessage(getString(R.string.searching_tip));
        progressdlg.show();
        new Thread(new SearchThread()).start();
        updateListHandler.postDelayed(updateThread, SEARCH_TIME);
    }

    //停止、关闭
    private void stopCameraPPPP() {
        NativeCaller.StopPPPP(SystemValue.deviceId);
    }

    Runnable updateThread = new Runnable() {

        public void run() {
            NativeCaller.StopSearch();
            progressdlg.dismiss();
            Message msg = updateListHandler.obtainMessage();
            msg.what = 1;
            updateListHandler.sendMessage(msg);
        }
    };
    Handler updateListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                listAdapter.notifyDataSetChanged();
                if (listAdapter.getCount() > 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                            AddCameraActivity.this);
                    dialog.setTitle(getResources().getString(
                            R.string.add_search_result));
                    dialog.setPositiveButton(
                            getResources().getString(R.string.refresh),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    startSearch();
                                }
                            });
                    dialog.setNegativeButton(
                            getResources().getString(R.string.cancel), null);
                    dialog.setAdapter(listAdapter,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int arg2) {
                                    Map<String, Object> mapItem = (Map<String, Object>) listAdapter.getItemContent(arg2);
                                    if (mapItem == null) {
                                        return;
                                    }
                                    String strName = (String) mapItem
                                            .get(ContentCommon.STR_CAMERA_NAME);
                                    String strDID = (String) mapItem
                                            .get(ContentCommon.STR_CAMERA_ID);
//									strUser = ContentCommon.DEFAULT_USER_NAME;
//									strPwd = ContentCommon.DEFAULT_USER_PWD;
                                    consigneeNameEdtv.setText(strName);
                                    deviceIdEdtv.setText(strDID);
                                }
                            });

                    dialog.show();
                } else {
                    showToast(getResources().getString(R.string.add_search_no));
                    isSearched = false;//
                }
            }
            if (msg.what == 2) {
                button_linkcamera.setVisibility(View.VISIBLE);
            }

        }
    };

    @OnClick({R.id.btn_link, R.id.btn_preview, R.id.btn_searchCamera, R.id.btn_settingwifi

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_link:
                if (checkIsEmpty()) return;
                if (tag == 1) {
                    showToast("设备已经是在线状态了");
                } else if (tag == 2) {
                    showToast("设备不在线");
//                    Toast.makeText(Me_video_add.this, "设备不在线",Toast.LENGTH_SHORT).show();
                } else {
                    done();
                }
                break;
            case R.id.btn_preview:
                if (checkIsEmpty()) return;
                Intent playIntent = new Intent(AddCameraActivity.this, PlayActivity.class);
                playIntent.putExtra("name",consigneeNameEdtv.getText().toString().trim());
                playIntent.putExtra("id",deviceIdEdtv.getText().toString().trim());
                startActivity(playIntent);
                break;
            case R.id.btn_searchCamera:
                stopCameraPPPP();
                //把相机状态，设备id置空
                tag=0;
                cameraStateTxtv.setText(R.string.login_stuta_camer);
                SystemValue.deviceId=null;
                searchCamera();
                break;
            case R.id.btn_settingwifi:
                Intent setwifi = new Intent(AddCameraActivity.this, SetwifiActivity.class);
                startActivity(setwifi);
                break;
//            case R.id.titlebar_menu_btn:
////                Intent setwifi = new Intent(AddCameraActivity.this, SetwifiActivity.class);
////                startActivity(setwifi);
//                showToast("说明");
//                break;
        }
    }

    private boolean checkIsEmpty() {
        if(SSUtils.isEmpty(consigneeNameEdtv.getText())){
            showToast("设备名不能为空");
            return true;
        }
        if(SSUtils.isEmpty(deviceIdEdtv.getText())){
            showToast("设备ID不能为空");
            return true;
        }
        return false;
    }

    private class SearchThread implements Runnable {
        @Override
        public void run() {
            Log.d("tag", "startSearch");
            NativeCaller.StartSearch();
        }
    }

    //连接摄像机
    private void done() {
        Intent in = new Intent();
        String strName = consigneeNameEdtv.getText().toString();
        String strDID = deviceIdEdtv.getText().toString();
        if (strDID.length() == 0) {
//            Toast.makeText(AddCameraActivity.this,
//                    getResources().getString(R.string.input_camera_id), Toast.LENGTH_SHORT).show();
            showToast(getString(R.string.input_camera_id));
            return;
        }

        if (strUser.length() == 0) {
            showToast(getString(R.string.input_camera_id));
            Toast.makeText(AddCameraActivity.this,
                    getResources().getString(R.string.input_camera_user), Toast.LENGTH_SHORT).show();
            return;
        }
        if (option == ContentCommon.INVALID_OPTION) {
            option = ContentCommon.ADD_CAMERA;
        }
        in.putExtra(ContentCommon.CAMERA_OPTION, option);
        in.putExtra(ContentCommon.STR_CAMERA_NAME, strName);//设备归属人
        in.putExtra(ContentCommon.STR_CAMERA_ID, strDID);//设备UID
        in.putExtra(ContentCommon.STR_CAMERA_USER, strUser);//登陆用户名  默认admin
        in.putExtra(ContentCommon.STR_CAMERA_PWD, strPwd);//登陆密码
        in.putExtra(ContentCommon.STR_CAMERA_TYPE, CameraType);
        progressBar.setVisibility(View.VISIBLE);
        SystemValue.deviceName = strName;
        SystemValue.deviceUser = strUser;
        SystemValue.deviceId = strDID;
        SystemValue.devicePass = strPwd;
        BridgeService.setIpcamClientInterface(this);//启动服务
        NativeCaller.Init();
        new Thread(new StartPPPPThread()).start();
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

    //连接摄像头
    private void startCameraPPPP() {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        int result = NativeCaller.StartPPPP(SystemValue.deviceId, SystemValue.deviceUser,
                SystemValue.devicePass, 1, "");//开启摄像头
//        Log.i("ip", "result:"+result);
//        Logger.e("ip", "result:" + result);
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
                        case ContentCommon.PPPP_STATUS_CONNECTING://0正在连接
                            resid = R.string.pppp_status_connecting;
                            progressBar.setVisibility(View.VISIBLE);
                            tag = 2;
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_FAILED://3连接失败
                            resid = R.string.pppp_status_connect_failed;
                            progressBar.setVisibility(View.GONE);
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_DISCONNECT://4断线
                            resid = R.string.pppp_status_disconnect;
                            progressBar.setVisibility(View.GONE);
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_INITIALING://1 已连接，正在初始化
                            resid = R.string.pppp_status_initialing;
                            progressBar.setVisibility(View.VISIBLE);
                            tag = 2;
                            break;
                        case ContentCommon.PPPP_STATUS_INVALID_ID://5ID无效
                            resid = R.string.pppp_status_invalid_id;
                            progressBar.setVisibility(View.GONE);
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_ON_LINE://2 在线状态
                            resid = R.string.pppp_status_online;
                            progressBar.setVisibility(View.GONE);
                            //摄像机在线之后读取摄像机类型
                            String cmd = "get_status.cgi?loginuse=admin&loginpas=" + SystemValue.devicePass
                                    + "&user=admin&pwd=" + SystemValue.devicePass;
                            NativeCaller.TransferMessage(did, cmd, 1);
                            tag = 1;
                            send();//保存数据
                            break;
                        case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE://6
                            resid = R.string.device_not_on_line;
                            progressBar.setVisibility(View.GONE);
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT://7
                            resid = R.string.pppp_status_connect_timeout;
                            progressBar.setVisibility(View.GONE);
                            tag = 0;
                            break;
                        case ContentCommon.PPPP_STATUS_CONNECT_ERRER://8密码错误
                            resid = R.string.pppp_status_pwd_error;
                            progressBar.setVisibility(View.GONE);
                            tag = 0;
                            break;
                        default:
                            resid = R.string.pppp_status_unknown;
                    }
                    cameraStateTxtv.setText(getResources().getString(resid));
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

    // 添加设备到用户
    public void send() {
        String name = consigneeNameEdtv.getText().toString();
        String id = deviceIdEdtv.getText().toString();
        showLoadingToast();
        SPPersonRequest.addCamera(name, id, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    showToast(msg);
                }
                hideLoadingToast();
//                setResult(MobileConstants.Result_Code_Refresh);

            }
        }, new SPFailuredListener(AddCameraActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    class MyWifiThread extends Thread {
        @Override
        public void run() {
            while (blagg == true) {
                super.run();

                updateListHandler.sendEmptyMessage(100000);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private String spitValue(String name, String tag) {
        String[] strs = name.split(";");
        for (int i = 0; i < strs.length; i++) {
            String str1 = strs[i].trim();
            if (str1.startsWith("var")) {
                str1 = str1.substring(4, str1.length());
            }
            if (str1.startsWith(tag)) {
                String result = str1.substring(str1.indexOf("=") + 1);
                return result;
            }
        }
        return -1 + "";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AddCameraActivity.this.finish();
            return false;
        }
        return false;
    }

    private void searchCamera() {
        if (!isSearched) {
            isSearched = true;
            startSearch();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(
                    AddCameraActivity.this);
            dialog.setTitle(getResources()
                    .getString(R.string.add_search_result));
            dialog.setPositiveButton(
                    getResources().getString(R.string.refresh),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSearch();
                        }
                    });
            dialog.setNegativeButton(
                    getResources().getString(R.string.str_cancel), null);
            dialog.setAdapter(listAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg2) {
                            Map<String, Object> mapItem = (Map<String, Object>) listAdapter
                                    .getItemContent(arg2);
                            if (mapItem == null) {
                                return;
                            }
                            String strName = (String) mapItem
                                    .get(ContentCommon.STR_CAMERA_NAME);
                            String strDID = (String) mapItem
                                    .get(ContentCommon.STR_CAMERA_ID);
                            String strUser = ContentCommon.DEFAULT_USER_NAME;
//                            String strPwd = ContentCommon.DEFAULT_USER_PWD;
//                            userEdit.setText(strUser);
//                            pwdEdit.setText(strPwd);
                            consigneeNameEdtv.setText(strName);
                            deviceIdEdtv.setText(strDID);
                        }
                    });
            dialog.show();
        }
    }

}
