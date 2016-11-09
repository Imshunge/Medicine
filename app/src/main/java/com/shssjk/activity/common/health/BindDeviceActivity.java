package com.shssjk.activity.common.health;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.view.ListViewNobar;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * 绑定设备
 */
public class BindDeviceActivity extends BaseActivity {
    private String type = "", code = "", bloodId = "", def = "0";
    private Spinner sp_text, pop_spinner;
    private EditText edt_num;
    private Button btn_ok;
    private ListViewNobar list_bind;
    private ArrayList<NameValuePair> nParams;
    private CheckBox bind_check;
    private ArrayAdapter<String> popdapter;
    private RelativeLayout headerView;
    private ImageView ivBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "绑定设备");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device);
        super.init();
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
