package com.shssjk.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.shssjk.activity.R;
import com.shssjk.activity.IViewController;
import com.shssjk.adapter.DeviceRelationAdapter;
import com.shssjk.adapter.SugarAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.health.HealthRequest;
import com.shssjk.model.health.AllSugar;
import com.shssjk.model.health.Device;
import com.shssjk.model.health.SuagrTongJi;
import com.shssjk.model.health.SugarData;
import com.shssjk.utils.DatePickerUtil;
import com.shssjk.utils.Logger;
import com.shssjk.utils.MyColor;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.RoundImageView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.NameValuePair;
/**
 * 血糖
 */
public class FragmentSurger extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.all_image1)
    RoundImageView allImage1;
    @Bind(R.id.tv_all1)
    TextView tvAll1;
    @Bind(R.id.tongji_num1)
    TextView tongjiNum1;
    @Bind(R.id.tongji_num2)
    TextView tongjiNum2;
    @Bind(R.id.tongji_num3)
    TextView tongjiNum3;
    @Bind(R.id.item_sg0)
    TextView itemSg0;
    @Bind(R.id.average_tv)
    TextView averageTv;
    @Bind(R.id.low_tv)
    TextView lowTv;
    @Bind(R.id.hight_tv)
    TextView hightTv;
    @Bind(R.id.ll_avg_1)
    LinearLayout llAvg1;
    @Bind(R.id.all_image2)
    RoundImageView allImage2;
    @Bind(R.id.tv_all2)
    TextView tvAll2;
    @Bind(R.id.tongji_num11)
    TextView tongjiNum11;
    @Bind(R.id.tongji_num12)
    TextView tongjiNum12;
    @Bind(R.id.tongji_num13)
    TextView tongjiNum13;
    @Bind(R.id.average1_tv)
    TextView average1Tv;
    @Bind(R.id.low1_tv)
    TextView low1Tv;
    @Bind(R.id.hight1_tv)
    TextView hight1Tv;
    @Bind(R.id.ll_avg_2)
    LinearLayout llAvg2;
    @Bind(R.id.all_image3)
    RoundImageView allImage3;
    @Bind(R.id.tv_all3)
    TextView tvAll3;
    @Bind(R.id.tongji_all1)
    TextView tongjiAll1;
    @Bind(R.id.tongji_all2)
    TextView tongjiAll2;
    @Bind(R.id.tongji_all3)
    TextView tongjiAll3;
    @Bind(R.id.average2_tv)
    TextView average2Tv;
    @Bind(R.id.low2_tv)
    TextView low2Tv;
    @Bind(R.id.hight3_tv)
    TextView hight3Tv;
    @Bind(R.id.ll_avg_3)
    LinearLayout llAvg3;
    @Bind(R.id.layout_tongji)
    LinearLayout layoutTongji;
    @Bind(R.id.list_sugar)
    ListView listSugar;
    @Bind(R.id.list_line)
    View listLine;
    private Context mContext;
    private TextView tv_date1, tv_date2, sg_title1, sg_title2, sg_title3, chart_bg;
    private ListView list_sugar;
    // 用来保存年月日：
    private int mYear, mMonth, mDay;
    private String code = "";
    private LineChart mChart;
    private LinearLayout lay_list_chart, layout_tongji;
    static final int DATE_DIALOG_ID1 = 1, DATE_DIALOG_ID2 = 2;
    private int dataID;
    private int clickId = 1;
    private LineData data0;
    public static int COL_BACK = Color.parseColor("#fafafa");
    Typeface mTf;
    private Spinner pop_spinner;
    private String name111 = "";
    private EditText edt_num;
    boolean flag = false;
    private PopupWindow popupWindow = null;
    ArrayList<NameValuePair> nParams = new ArrayList<NameValuePair>();
    //	private TextView sugar_bind;
    private Spinner spiner;
    private String[] binds;
    private View view;
    private String deviceType = "XTY";   //（BLOOD 血压计；XTY 血糖仪）
    List<Device> devices=new ArrayList<>();
    private String deviceId = "";
    private String orderTypr = "desc";   //; order、排序（asc、desc）
    private String startTime = "";
    private String endTime = "";
    //    foodstatus  1:空腹，2:早餐后，3:午餐前，4:午餐后，5:晚餐前，6:晚餐后，7:睡前，8:凌晨，
//                   9：随机，A:餐前，B:餐后
    private String foodstatus = "";
    private int psotion = 0;   //选择的位置
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String dateTime = "";
    private DatePickerUtil datePickDialog;
    private List<SugarData> sugarData = new ArrayList<SugarData>();
    private SugarAdapter sugarAdapter;
    private boolean isFast = true;
    private boolean isGoToLogin = false; //没登陆时去登录 记录状态
    private DeviceListSugarChangeReceiver mDeviceListSugarChangeReceiver;
    private DeviceRelationAdapter resultAdapter;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //
                case 1:
                    getSugarData(devices);
                    break;
            }
        }
    };
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        Logger.e("FragmentSurger", "onAttach");
    }

    @Override
    public void onStart(){
        super.onStart();
        isFast=false;
//        HealthyFragment healthyFragment = new HealthyFragment();
//        int index=  healthyFragment.mPager.getCurrentItem();
        Logger.e("FragmentSurger", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        Logger.e("FragmentSurger", "onResume");
        if (isGoToLogin) {
//            getProductDetails();
            if (MobileApplication.getInstance().isLogined) {
                getDeviceList();
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(this,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_healthy_sugger, null, false);
        super.init(view);
        ButterKnife.bind(this, view);
        Logger.e(this, "onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.e(this, "onViewCreated");
    }

    @Override
    public void initSubView(View v) {
        tv_date1 = (TextView) v.findViewById(R.id.sugar_date1);
        tv_date2 = (TextView) v.findViewById(R.id.sugar_date2);
        sg_title1 = (TextView) v.findViewById(R.id.sg_title1);
        sg_title2 = (TextView) v.findViewById(R.id.sg_title2);
        sg_title3 = (TextView) v.findViewById(R.id.sg_title3);

        sg_title1.setOnClickListener(this);
        sg_title2.setOnClickListener(this);
        sg_title3.setOnClickListener(this);
        list_sugar = (ListView) v.findViewById(R.id.list_sugar);
//		sugar_bind = (TextView) findViewById(R.id.sugar_bind);
        chart_bg = (TextView) v.findViewById(R.id.chart_bg);
        mChart = (LineChart) v.findViewById(R.id.sg_chart);
        lay_list_chart = (LinearLayout) v.findViewById(R.id.layout_list_chart);
        layout_tongji = (LinearLayout) v.findViewById(R.id.layout_tongji);
//        mChart.setOnChartValueSelectedListener(this);
        v.findViewById(R.id.sugar_btn1).setOnClickListener(this);
        v.findViewById(R.id.sugar_btn2).setOnClickListener(this);
        v.findViewById(R.id.btn_search).setOnClickListener(this);
        spiner = (Spinner) v.findViewById(R.id.relation_spin);
        spiner.setClickable(true);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                psotion = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    @Override
    public void initEvent() {
        sugarAdapter = new SugarAdapter(mContext);
        list_sugar.setAdapter(sugarAdapter);
        //        监听 刷新 设备列表
        IntentFilter filter = new IntentFilter(MobileConstants.ACTION_HEALTH_SUAGR_LOADATA);
        mDeviceListSugarChangeReceiver = new DeviceListSugarChangeReceiver();
        mContext.registerReceiver(mDeviceListSugarChangeReceiver, filter);
    }

    @Override
    public void initData() {
        if (!MobileApplication.getInstance().isLogined) {
//            clearData();
        } else {
//            getDeviceList();
        }
    }
    //   清除数据
    private void clearData() {
//        数据列表
        sugarData = new ArrayList<SugarData>();
        sugarAdapter.setData(sugarData);
//       亲属关系
//        showTypes
//        String[] types = new String[]{""};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
//                android.R.layout.simple_spinner_item, types);
//        spiner.setAdapter(adapter);
//        adapter.setDropDownViewResource(R.layout.dropdown_stytle);
        devices = new ArrayList<>();
        resultAdapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        if (!MobileApplication.getInstance().isLogined) {
            isGoToLogin = true;
            showToastUnLogin();
            toLoginPage();
            return;
        }
        switch (v.getId()) {
            case R.id.sugar_btn1:
//               开始日期
                format = new SimpleDateFormat("yyyy年MM月dd日");
                dateTime = format.format(System.currentTimeMillis());
                datePickDialog = new DatePickerUtil(getActivity(), dateTime);
                datePickDialog.dateTimePicKDialog(tv_date1, 0);
                break;
            case R.id.sugar_btn2:
//结束日期
                format = new SimpleDateFormat("yyyy年MM月dd日");
                dateTime = format.format(System.currentTimeMillis());
                datePickDialog = new DatePickerUtil(getActivity(), dateTime);
                datePickDialog.dateTimePicKDialog(tv_date2, 0);
                break;
            case R.id.btn_search:
                startTime = tv_date1.getText().toString().trim();
                if (startTime.equals("开始日期")) {
                    startTime = "";
                } else {
                    startTime = tv_date1.getText().toString().trim();
                }
                endTime = tv_date2.getText().toString().trim();
                if (endTime.equals("结束日期")) {
                    endTime = "";
                } else {
                    endTime = tv_date2.getText().toString().trim();
                }
                if (devices != null&&devices.size()>0) {
                    deviceId = devices.get(psotion).getValue().toString().trim();
                }
                if (SSUtils.isEmpty(deviceId)) {
                    return;
                }
                getSugarDataList(foodstatus, deviceId, orderTypr, startTime, endTime);
                break;

            case R.id.sg_title1:
                clickId = 1;
                sg_title1.setTextColor(MyColor.BLUE_9f);
                sg_title2.setTextColor(MyColor.BLACK_9c);
                sg_title3.setTextColor(MyColor.BLACK_9c);
                mChart.setVisibility(View.GONE);
                lay_list_chart.setVisibility(View.VISIBLE);
                layout_tongji.setVisibility(View.GONE);
//                getListData(code, clickId);// 获取血糖数据
                break;
            case R.id.sg_title2:
                clickId = 2;
                sg_title2.setTextColor(MyColor.BLUE_9f);
                sg_title1.setTextColor(MyColor.BLACK_9c);
                sg_title3.setTextColor(MyColor.BLACK_9c);
                mChart.setVisibility(View.VISIBLE);
                lay_list_chart.setVisibility(View.GONE);
                layout_tongji.setVisibility(View.GONE);
//              getListData(code, clickId);// 获取血糖数据
                setChart();
                break;
            case R.id.sg_title3:
                clickId = 3;
                sg_title3.setTextColor(MyColor.BLUE_9f);
                sg_title1.setTextColor(MyColor.BLACK_9c);
                sg_title2.setTextColor(MyColor.BLACK_9c);
                mChart.setVisibility(View.GONE);
                lay_list_chart.setVisibility(View.GONE);
                layout_tongji.setVisibility(View.VISIBLE);
//                getListAll(code);// 获取血糖数据
                if (SSUtils.isEmpty(deviceId)) {
                    return;
                }
                getSugarTongJi(deviceId);
                break;
            default:
                break;
        }
    }

    private void getDeviceList() {
        showLoadingToast("正在加载数据");
        HealthRequest.getDeviceList(deviceType, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    devices = (List<Device>) response;
                    hideLoadingToast();
                    Message msgtemp = Message.obtain();
                    msgtemp.what = 1;
                    mHandler.sendMessage(msgtemp);
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                //可见时才显示提示
                if(!isFast){
                    showToast(msg);
                    if ("未绑定血糖仪设备".equals(msg)) {
                        //清除信息 ：
                        if (devices.size() > 0) {
                            clearData();
                        }
                    }
                }
//                showToast(msg);
                Logger.e(this, "getDeviceList " + msg + "");
            }
        });
    }

    private void getSugarData(List<Device> devices) {
        try {
            psotion = getBloodDeviceId(devices);
            deviceId = devices.get(psotion).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSugarDataList(foodstatus, deviceId, orderTypr, startTime, endTime);
        showTypes(devices, psotion);
    }

    //    获取最关心设备id 下标
    private int getBloodDeviceId(List<Device> devices) {
        for (int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
//            default 、是否是最关心设备（1是 0 否）
            if ("1".equals(device.getDefault().trim())) {
                return i;
            }
        }

        return 0;
    }

    //    foodstatus、测试时间段 ; devicesn、设备号 ; start 、开始时间 ; end、结束时间 ; order 、 排序（asc 、desc）
    private void getSugarDataList(String foodstatus, String devicesn, String order, String start_time, String end_time) {
        showLoadingToast("正在加载数据");
        HealthRequest.getSugarDataList(foodstatus, devicesn, order, start_time, end_time, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    sugarData = (List<SugarData>) response;
                    sugarAdapter.setData(sugarData);
                    if (clickId == 2) {
                        setChart();
                    }
                    if (clickId == 3) {
                        getSugarTongJi(deviceId);
                    }
                    hideLoadingToast();
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
//                if (msg.equals("未绑定血糖仪设备")) {
//                    bloodDatalist = new ArrayList<BloodDevice>();
//                    bloodAdapter.setData(bloodDatalist);
//                }
                if (msg.equals("空数据")) {
                    sugarData = new ArrayList<SugarData>();
                    sugarAdapter.setData(sugarData);
                }
                if (clickId == 2) {
                    setChart();
                }
                //可见时才显示提示
                if(!isFast){
                    showToast(msg);
                }
                Logger.e(this, "getSugarDataList " + msg + "");
            }
        });
    }

    private void showTypes(List<Device> devices, int psotion) {
//        String[] types = getTyValue(devices);
//        //            改变内容
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
//                android.R.layout.simple_spinner_item, types);
//        spiner.setAdapter(adapter);
//        spiner.setSelection(psotion, true);
//        adapter.setDropDownViewResource(R.layout.dropdown_stytle);

        resultAdapter = new DeviceRelationAdapter(getActivity(), R.layout.devicetype_item,devices);
        spiner.setAdapter(resultAdapter);
        spiner.setSelection(psotion, true);
        resultAdapter.setDropDownViewResource(R.layout.dropdown_stytle);

    }

    //    获取结果数组
    private String[] getTyValue(List<Device> devices) {
        int size = devices.size();
        String[] types = new String[size];
        //            改变内容
        for (int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
//            default 、是否是最关心设备（1是 0 否）
            types[i] = device.getRelation();
        }
        return types;
    }


    public void setChart() {
        // data0 = getData(36, 100);
        int size = 0;
        if (sugarData != null && !sugarData.isEmpty()) {
            size = sugarData.size();
        }
        LineData data0 = getData(size - 1);
        setupChart(mChart, data0, COL_BACK);
    }

    LineData getData(int count) {
        // LineData getData(int count,float range) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = count; i >= 0; i--) {
            // x轴显示的数据，这里默认使用数字下标显示
            xVals.add(count - i + "");
        }
        if (count == -1) {
            xVals.clear();
        }

        // y轴的数据  血糖
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = count; i >= 0; i--) {
//            int Min = strChart.path("data").path(i).path("sys").asInt();
            Float Min = Float.parseFloat(sugarData.get(i).getResult().trim());
            yVals1.add(new Entry(Min, count - i));
        }
        if (count == -1) {
            yVals1.clear();
        }
        // y轴的数据集合
        LineDataSet set1 = new LineDataSet(yVals1, "血糖值(mmol)");
        set1.setFillAlpha(110);
        set1.setFillColor(Color.YELLOW);

        set1.setLineWidth(1.75f); // 线宽
        set1.setCircleSize(3f);// 显示的圆形大小
        set1.setColor(Color.RED);// 显示颜色
        set1.setCircleColor(Color.RED);// 圆形的颜色
        set1.setHighLightColor(Color.RED); // 高亮的线的颜色

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets
        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(MyColor.TOUMING);
        data.setValueTextSize(9f);
        return data;
    }
    public void setupChart(LineChart mChart, LineData data, int color) {
        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("数据为空");
        // enable value highlighting
        mChart.setHighlightEnabled(true);
        // enable touch gestures
        mChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        mChart.setDragEnabled(true);// 是否可以拖拽
        mChart.setTouchEnabled(true); // 设置是否可以触摸
        mChart.setScaleEnabled(true);// 是否可以缩放
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);//

        mChart.setBackgroundColor(color);// 设置背景

//         mChart.setValueTypeface(mTf);// 设置字体

        // add data
        mChart.setData(data); // 设置数据

        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend(); // 设置标示，就是那个一组y的value的

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.CIRCLE);// 样式
        l.setFormSize(6f);// 字体
        l.setTextColor(Color.BLACK);// 颜色
        l.setTypeface(mTf);// 字体

        XAxis xAxis = mChart.getXAxis();// x轴的标示
        xAxis.setTypeface(tf);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);
        /**
         * 血糖曲线
         */
        YAxis leftAxis = mChart.getAxisLeft();// y轴的标示
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid
        // overlapping lines
        leftAxis.setAxisMaxValue(25f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
        // leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        // animate calls invalidate()...
        mChart.animateX(2500); // 立即执行的动画,x轴
    }

    private void getSugarTongJi(String imei) {
        showLoadingToast("正在加载数据");
        HealthRequest.getSugarTongji(imei, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    hideLoadingToast();
                    SuagrTongJi bloodTongJi = (SuagrTongJi) response;
                    showTongJi(bloodTongJi);
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                //可见时才显示提示
                if(!isFast){
                    showToast(msg);
                }
                Logger.e(this, "getSugarTongJi " + msg + "");
            }
        });
    }

    private void showTongJi(SuagrTongJi suagrTongJi) {
        if (SSUtils.isEmpty(suagrTongJi)) {
            return;
        }
        AllSugar one = suagrTongJi.getOne();
        if (!SSUtils.isEmpty(one)) {
            int sum = one.getCount();
            tvAll1.setText("共测试" + sum + "次");
            int normal = one.getNcount();

            if (SSUtils.calculateReate(sum, normal)) {
                allImage1.setImageResource(R.drawable.tongji1);
            } else {
                allImage1.setImageResource(R.drawable.abnormal);
            }

            if (!SSUtils.isEmpty(one.getLcount() + "")) {
                tongjiNum1.setText(one.getLcount() + "");
            }
            if (!SSUtils.isEmpty(one.getNcount() + "")) {
                tongjiNum2.setText(one.getNcount() + "");
            }
            if (!SSUtils.isEmpty(one.getHcount() + "")) {
                tongjiNum3.setText(one.getHcount() + "");
            }
            averageTv.setText(one.getAvg() + "");
            lowTv.setText(one.getMin() + "");
            hightTv.setText(one.getMax() + "");

//            if (!SSUtils.calculateReate(sum, normal)){
//                allImage1.setImageResource(R.drawable.abnormal);
//            }

        }
        AllSugar three = suagrTongJi.getThree();
        if (!SSUtils.isEmpty(three)) {
            int sum = three.getCount();
            int normal = three.getNcount();
            tvAll2.setText("共测试" + sum + "次");
            if (!SSUtils.isEmpty(three.getLcount() + "")) {
                tongjiNum11.setText(three.getLcount() + "");
            }
            if (!SSUtils.isEmpty(three.getNcount() + "")) {
                tongjiNum12.setText(three.getNcount() + "");
            }
            if (!SSUtils.isEmpty(three.getHcount() + "")) {
                tongjiNum13.setText(three.getHcount() + "");
            }
            average1Tv.setText(three.getAvg() + "");
            low1Tv.setText(three.getMin() + "");
            hight1Tv.setText(three.getMax() + "");
            if (SSUtils.calculateReate(sum, normal)) {
                allImage2.setImageResource(R.drawable.tongji1);
            } else {
                allImage2.setImageResource(R.drawable.abnormal);
            }
        }
        AllSugar all = suagrTongJi.getAll();
        if (!SSUtils.isEmpty(all)) {
            int sum = all.getCount();
            int normal = all.getNcount();
            tvAll3.setText("共测试" + sum + "次");

            if (!SSUtils.isEmpty(all.getLcount() + "")) {
                tongjiAll1.setText(all.getLcount() + "");
            }
            if (!SSUtils.isEmpty(all.getNcount() + "")) {
                tongjiAll2.setText(all.getNcount() + "");
            }
            if (!SSUtils.isEmpty(all.getHcount() + "")) {
                tongjiAll3.setText(all.getHcount() + "");
            }
            average2Tv.setText(all.getAvg() + "");
            low2Tv.setText(all.getMin() + "");
            hight3Tv.setText(all.getMax() + "");
            if (SSUtils.calculateReate(sum, normal)) {
                allImage3.setImageResource(R.drawable.tongji1);
            } else {
                allImage3.setImageResource(R.drawable.abnormal);
            }
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mContext.unregisterReceiver(mDeviceListSugarChangeReceiver);
    }
    @Override
    public void gotoLoginPageClearUserDate() {

    }
    //广播接收器  血压数据  变化
    class DeviceListSugarChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MobileConstants.ACTION_HEALTH_SUAGR_LOADATA)) {
                Logger.e("FragmentBlood", "DeviceListChangeReceiver");
                if (!MobileApplication.getInstance().isLogined) {
                    clearData();//清除数据
                } else {
                    getDeviceList();
                }
            }
        }
    }


}
