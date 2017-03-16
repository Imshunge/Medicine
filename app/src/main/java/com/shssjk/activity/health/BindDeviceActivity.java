package com.shssjk.activity.health;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.DeviceAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.health.HealthRequest;
import com.shssjk.model.health.Device;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.ListViewNobar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shssjk.utils.SSUtils.getFromAssets;

/**
 * 绑定设备
 */
public class BindDeviceActivity extends BaseActivity implements DeviceAdapter.InDelBtnClickListener{
//
    @Bind(R.id.bind_text)
    Spinner spbindDevice;   //设备
    @Bind(R.id.bind_spin)
    Spinner spUser;  //使用者
    @Bind(R.id.bind_num)
    EditText bindNum;
    @Bind(R.id.bind_check)
    CheckBox bindCheck;
    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.list_bind)
    ListViewNobar listBind;
    @Bind(R.id.layout_bind)
    LinearLayout layoutBind;
    @Bind(R.id.scrollView1)
    ScrollView scrollView1;

    private String deviceType="BLOOD";   //（BLOOD 血压计；XTY 血糖仪）
    private DeviceAdapter mDeviceAdapter;
    List<Device> devices;
    private Context mContext;
    private String orderId;
    private int state=1;   // 1 绑定设备   2 更新设备
    private String[] types;
    Button explainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "绑定设备",true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device);
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
                showConfirmDialog(getFromAssets(("guid_bind.txt"), mContext),"使用说明");
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        mDeviceAdapter= new DeviceAdapter(mContext,this);
        listBind.setAdapter(mDeviceAdapter);
        listBind.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Device device = (Device) mDeviceAdapter.getItem(position);
//                spbindDevice.
//                device.getRelation();
                spUser.getCount();
                int  count =spbindDevice.getCount() ;
                for (int i = 0; i < types.length; i++) {
                    String type = types[i];
                    if (types[i].toString().equals(device.getRelation().trim())) {
                        spUser.setSelection(i);
                    }
                }
                bindNum.setText(device.getValue());
                btnOk.setText("修改");
                orderId = device.getId();
                state = 2;
                String str = device.getDefault().trim();
//        default 、是否是最关心设备（1是 0 否）
                if ("1".equals(str)) {
                    bindCheck.setChecked(true);
                    bindCheck.setEnabled(false);
                }else{
                    bindCheck.setChecked(false);
                    bindCheck.setEnabled(true);
                }
            }
        });

        spbindDevice.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                bindCheck.setChecked(false);
                bindCheck.setEnabled(true);
                bindNum.setText("");
                updatalist(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void updatalist(int pos) {
        //（BLOOD 血压计；XTY 血糖仪）
        if(pos==0){
            deviceType="BLOOD";
            types = getResources().getStringArray(R.array.type);
//            改变内容
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, types);
            spUser.setAdapter(adapter);
            adapter.setDropDownViewResource(R.layout.dropdown_stytle);
        }else{
            deviceType="XTY";
            String[] languages = getResources().getStringArray(R.array.type_sugar);
//            改变内容
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, languages);
            //设置下拉列表的风格
            spUser.setAdapter(adapter);
            adapter.setDropDownViewResource(R.layout.dropdown_stytle);
        }


        getDeviceList();
    }

    @OnClick({ R.id.bind_check, R.id.btn_ok, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bind_check:
                break;
            case R.id.btn_ok:
                if(state==1){
                    bindDevice();
                }else{
                    if(!SSUtils.isEmpty(orderId)){
                        updataDevice(orderId);
                    }
                }
                break;
            case R.id.btn_cancel:
                clearData();
                break;
        }
    }

    private void clearData() {
        btnOk.setText("绑定设备");
        bindNum.setText("");
        bindCheck.setChecked(false);
        bindCheck.setEnabled(true);
    }
    private void bindDevice() {
        String  name   = spbindDevice.getSelectedItem().toString().trim();   //设备
        String relation = spUser.getSelectedItem().toString().trim();    //使用者
        String type="BLOOD";
        if("血糖仪".equals(name)){
            type="XTY";
        }
        String deviceNum= bindNum.getText().toString().trim();
        if(SSUtils.isEmpty(deviceNum)){
            showToast("请输入设备号");
            return;
        }
        showLoadingToast("正在操作");
        String   def = getBaindCheck();
        HealthRequest.addDevice(type, deviceNum, relation, def, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    int id = (int) response;
                    if (id == 0) {
                        showToast(getString(R.string.bandDeviceSucess));
                        afterDataChange();
//                        更新列表
                        hideLoadingToast();
                        getDeviceList();
                    } else {
                        showToast(msg);
                    }

                } else {
                    showToast(msg);
                }
                hideLoadingToast();

            }
        }, new SPFailuredListener(BindDeviceActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
//        清空数据 选择框选择未选中
    private void afterDataChange() {
        bindNum.setText(""); //清空数据
        bindCheck.setChecked(false); //选择框选择未选中
    }
    //绑定设备
    private void updataDevice(String id) {
        String  name   = spbindDevice.getSelectedItem().toString().trim();   //设备
        String relation = spUser.getSelectedItem().toString().trim();    //使用者
        String deviceNum= bindNum.getText().toString().trim();
        if(SSUtils.isEmpty(deviceNum)){
            showToast("请输入设备号");
            return;
        }
        String type="BLOOD";
        if("血糖仪".equals(name)){
            type="XTY";
        }
        showLoadingToast("正在操作");

        String   def = getBaindCheck();
        HealthRequest.saveDevice(id,type, deviceNum, relation, def, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    int id = (int) response;
                    if (id == 0) {
//                        showToast(getString(R.string.bandDeviceSucess));
                        afterDataChange();
                        btnOk.setText("绑定设备");
                        state=1;
                        hideLoadingToast();
//                        更新列表
                        getDeviceList();
//                        hideLoadingToast();
                    } else {
                        showToast(msg);
                    }

                } else {
                    showToast(msg);
                }
                hideLoadingToast();

            }
        }, new SPFailuredListener(BindDeviceActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
//        判断是否选择关心
    private String getBaindCheck() {
        String str ="0";
        if(bindCheck.isChecked()){
            str="1";
        }
        return str;
    }
    private void getDeviceList(){
        showLoadingToast("正在加载数据");
        HealthRequest.getDeviceList(deviceType, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    devices = (List<Device>) response;
                    mDeviceAdapter.setData(devices);
                    hideLoadingToast();
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener(BindDeviceActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                if (msg.equals("未绑定血压计设备")) {
                    devices = new ArrayList<Device>();
                    mDeviceAdapter.setData(devices);
                }
                if (msg.equals("未绑定血糖仪设备")) {
                    devices = new ArrayList<Device>();
                    mDeviceAdapter.setData(devices);
                }
                showToast(msg);
            }
        });
    }
    private void getDeleteDevice(String id){
        showLoadingToast("正在删除数据");

        HealthRequest.deleteDevice(id, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    int id = (int) response;
                    if (id == 0) {
                        showToast(msg);
                        hideLoadingToast();
                        //   更新列表
                        getDeviceList();
                    } else {
                        showToast(msg);
                    }
                }
                hideLoadingToast();

            }
        }, new SPFailuredListener(BindDeviceActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
    @Override
    public void delBtn(final Device device) {
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
        builder.setMessage("您确定要删除该设备吗？");
        builder.setTitle("删除提醒");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getDeleteDevice(device.getId());
            }
        });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("BindDeviceActivity","onDestroy");
        //更新设备列表
        if (mContext != null) {
            mContext.sendBroadcast(new Intent(MobileConstants.ACTION_HEALTH_LOADATA));
        }
    }
}
