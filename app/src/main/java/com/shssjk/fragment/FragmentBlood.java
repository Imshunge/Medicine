package com.shssjk.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.shssjk.activity.R;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.health.BindDeviceActivity;
import com.shssjk.activity.shop.ProductListActivity;
import com.shssjk.adapter.BloodAdapter;
import com.shssjk.adapter.DeviceTypeAdapter;
import com.shssjk.adapter.DeviceRelationAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.health.HealthRequest;
import com.shssjk.model.health.All;
import com.shssjk.model.health.BloodDevice;
import com.shssjk.model.health.BloodTongJi;
import com.shssjk.model.health.Device;
import com.shssjk.model.health.One;
import com.shssjk.model.health.Three;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.DatePickerUtil;
import com.shssjk.utils.Logger;
import com.shssjk.utils.MyColor;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.RoundImageView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.NameValuePair;

/**
 * 健康云 血压
 */

public class FragmentBlood extends BaseFragment implements View.OnClickListener {
    Typeface mTf;
    protected ExecutorService pool = null;
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
    private LineData data0;
    private LineChart mChart;
    private TextView chart_title1, chart_title2, chart_title3, chart_bg;
    private RadioButton rd_father, rd_mother;
    public static int COL_BACK = Color.parseColor("#fafafa");
    public String type_sex = "1";
    private int clickId = 1;
    private ListView list_chart;   //血压列表
    private LinearLayout layout_list_chart, layout_tongji;
    private String code = "";
    private ArrayList<NameValuePair> nParams;
    private RadioGroup radioGroup_blood;
    // 声明一个独一无二的标识，来作为要显示DatePicker的Dialog的ID：
    static final int DATE_DIALOG_ID1 = 0;
    static final int DATE_DIALOG_ID2 = 1;
    private TextView tv_date1, tv_date2;
    private int dataID;
    private Date date_start, date_end;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private Spinner spiner;
    private String[] binds;
    private int addId = 0;
    private String deviceType = "BLOOD";   //（BLOOD 血压计；XTY 血糖仪）
    List<Device> devices=new ArrayList<>();
    private Context mContext;
    private String dateTime = "";
    private DatePickerUtil datePickDialog;
    private String startTime = "";
    private String endTime = "";
    private String orderTypr = "desc";   //; order、排序（asc、desc）
    private List<BloodDevice> bloodDatalist = new ArrayList<BloodDevice>();
    private BloodAdapter bloodAdapter;
    private String deviceId = "";
    private int psotion = 0;
    private DeviceListChangeReceiver mDeviceListChangeReceiver;
    private boolean isFast = true;
    private boolean isGoToLogin = false; //没登陆时去登录 记录状态

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //
                case 1:
                    getBloodData(devices);
                    break;
            }
        }
    };
    //   购买商品的分类id
    private String categoryId = "850";
    private DeviceTypeAdapter mDeviceTypeAdapter;
    private DeviceRelationAdapter resultAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_healthy_blood, null, false);
        super.init(view);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initSubView(View v) {
        chart_title1 = (TextView) v.findViewById(R.id.chart_title1);
        chart_title2 = (TextView) v.findViewById(R.id.chart_title2);
        chart_title3 = (TextView) v.findViewById(R.id.chart_title3);
        chart_title1.setOnClickListener(this);
        chart_title2.setOnClickListener(this);
        chart_title3.setOnClickListener(this);
        radioGroup_blood = (RadioGroup) v.findViewById(R.id.test_radioGroup);
        rd_father = (RadioButton) v.findViewById(R.id.test_radio1);
        rd_mother = (RadioButton) v.findViewById(R.id.test_radio2);
//        rd_father.setOnClickListener(this);
//        rd_mother.setOnClickListener(this);
        radioGroup_blood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // TODO Auto-generated method stub
                if (checkedId == rd_father.getId()) {
                    type_sex = "1";
                } else if (checkedId == rd_mother.getId()) {
                    type_sex = "2";
                }
            }
        });
        chart_bg = (TextView) v.findViewById(R.id.chart_bg);
        mChart = (LineChart) v.findViewById(R.id.chart);
        list_chart = (ListView) v.findViewById(R.id.list_chart);
        bloodAdapter = new BloodAdapter(mContext);
        list_chart.setAdapter(bloodAdapter);
        layout_list_chart = (LinearLayout) v.findViewById(R.id.layout_list_chart);
        layout_tongji = (LinearLayout) v.findViewById(R.id.layout_tongji);
        tv_date1 = (TextView) v.findViewById(R.id.tv_date1);
        tv_date2 = (TextView) v.findViewById(R.id.tv_date2);
        v.findViewById(R.id.text_date1).setOnClickListener(this);
        v.findViewById(R.id.text_date2).setOnClickListener(this);
        v.findViewById(R.id.btn_find).setOnClickListener(this);
        spiner = (Spinner) v.findViewById(R.id.blood_spin);
        spiner.setSelection(0, true);
        spiner.setClickable(true);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String addr = spiner.getSelectedItem().toString();
                psotion = position;
                chanageName(addr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void chanageName(String addr) {
        if (SSUtils.isEmpty(addr)) {
            return;
        }
        switch (addr.trim()) {
            case "爸爸妈妈":
                rd_father.setText("爸爸");
                rd_mother.setText("妈妈");
                break;
            case "岳父岳母":
                rd_father.setText("岳父");
                rd_mother.setText("岳母");
                break;
            case "爷爷奶奶":
                rd_father.setText("爷爷");
                rd_mother.setText("奶奶");
                break;
            case "外公外婆":
                rd_father.setText("外公");
                rd_mother.setText("外婆");
                break;
            case "我和老公":
                rd_father.setText("我");
                rd_mother.setText("老公");
                break;
            case "我和老婆":
                rd_father.setText("我");
                rd_mother.setText("老婆");
                break;
            case "朋友":
                rd_father.setText("用户1");
                rd_mother.setText("用户2");
                break;
            case "其他":
                rd_father.setText("用户1");
                rd_mother.setText("用户2");
                break;
            default:
                rd_father.setText("爸爸");
                rd_mother.setText("妈妈");
                break;
        }
    }
    @Override
    public void initEvent() {
//        监听 刷新 设备列表
        IntentFilter filter = new IntentFilter(MobileConstants.ACTION_HEALTH_LOADATA);
        mContext.registerReceiver(mDeviceListChangeReceiver = new DeviceListChangeReceiver(), filter);
    }
    @Override
    public void initData() {
        if (!MobileApplication.getInstance().isLogined) {
//            clearData();
        } else {
            getDeviceList();
        }
    }
    @Override
    public void onClick(View v) {
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            isGoToLogin = true;
            toLoginPage();
            return;
        }
        switch (v.getId()) {
            case R.id.chart_title1:
//                列表1
                clickId = 1;
                chart_title1.setTextColor(MyColor.BLUE_9f);
                chart_title2.setTextColor(MyColor.BLACK_9c);
                chart_title3.setTextColor(MyColor.BLACK_9c);
                mChart.setVisibility(View.GONE);
                layout_list_chart.setVisibility(View.VISIBLE);
                layout_tongji.setVisibility(View.GONE);
                orderTypr = "desc";   //; order、排序（asc、desc）
                if (bloodDatalist != null && bloodDatalist.size() > 1) {
//                    setChart();
                } else {
                    getBloodsList(deviceId, type_sex, orderTypr, startTime, endTime);
                }
//                getBloodsList(deviceId, type_sex, orderTypr, startTime, endTime);
                break;
            case R.id.chart_title2:
//       曲线图
                clickId = 2;
                chart_title2.setTextColor(MyColor.BLUE_9f);
                chart_title1.setTextColor(MyColor.BLACK_9c);
                chart_title3.setTextColor(MyColor.BLACK_9c);
                mChart.setVisibility(View.VISIBLE);
                layout_list_chart.setVisibility(View.GONE);
                layout_tongji.setVisibility(View.GONE);
//                getListData(code, type_sex);
                orderTypr = "asc";
                if (bloodDatalist != null && bloodDatalist.size() > 1) {
                    setChart();
                } else {
                    getBloodsList(deviceId, type_sex, orderTypr, startTime, endTime);
                }
                break;
            case R.id.chart_title3:
//   统计
                chart_title1.setTextColor(MyColor.BLACK_9c);
                chart_title2.setTextColor(MyColor.BLACK_9c);
                chart_title3.setTextColor(MyColor.BLUE_9f);
                clickId = 3;
                mChart.setVisibility(View.GONE);
                layout_list_chart.setVisibility(View.GONE);
                layout_tongji.setVisibility(View.VISIBLE);
                if (SSUtils.isEmpty(deviceId)) {
                    return;
                }
                getBloodsTongJi(deviceId, type_sex);
                break;
            case R.id.btn_find:
//                startTime = tv_date1.getText().toString().trim();
//                endTime = tv_date2.getText().toString().trim();
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
                } else {
                    return;
                }

                getBloodsList(deviceId, type_sex, orderTypr, startTime, endTime);
                break;
            case R.id.text_date1:
//               开始日期
                format = new SimpleDateFormat("yyyy年MM月dd日");
                dateTime = format.format(System.currentTimeMillis());
                datePickDialog = new DatePickerUtil(getActivity(), dateTime);
                datePickDialog.dateTimePicKDialog(tv_date1, 0);
                break;
            case R.id.text_date2:
//结束日期
                format = new SimpleDateFormat("yyyy年MM月dd日");
                dateTime = format.format(System.currentTimeMillis());
                datePickDialog = new DatePickerUtil(getActivity(), dateTime);
                datePickDialog.dateTimePicKDialog(tv_date2, 0);
                break;
//            case R.id.radio_1:
//                type_sex="1";
//                break;
//            case R.id.radio_2:
//                type_sex="2";
//                break;
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
                if (msg.equals("未绑定血压计设备")) {
                    //清除信息 ：
                    if(devices.size()>0){
                        clearData();
                    }
                    ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
                    builder.setMessage("未绑定血压计设备");
                    builder.setTitle("系统提示");
                    builder.setCancelable(true);
                    builder.setPositiveButton(R.string.bind, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                         startupBindDeviceActivity();
                        }
                    });
                    builder.setNegativeButton(R.string.buy,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getToBy();
                                }
                            });
                    builder.create().show();
                } else {
                    showToast(msg);
                }
                Logger.e(this,"getDeviceList "+msg+"");
            }
        });
    }

    /**
     * 绑定设备
     */
    public  void startupBindDeviceActivity(){
        if (!MobileApplication.getInstance().isLogined){
            showToastUnLogin();
            toLoginPage();
            return;
        }
        Intent carIntent = new Intent(getActivity() , BindDeviceActivity.class);
        carIntent.putExtra("isFromBlood",true);
        getActivity().startActivity(carIntent);
    }

    private void getBloodData(List<Device> devices) {
        try {
            psotion = getBloodDeviceId(devices);
            deviceId = devices.get(psotion).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getBloodsList(deviceId, type_sex, orderTypr, startTime, endTime);
        showTypes(devices, psotion);
    }
    private void showTypes(List<Device> devices, int psotion) {
        String[] types = getTyValue(devices);
        //            改变内容
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, types);
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

    private void getBloodsList(String imei, String type, String order, String start_time, String end_time) {
        showLoadingToast("正在加载数据");
        HealthRequest.getBloodsDataList(imei, type, order, start_time, end_time, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    bloodDatalist = (List<BloodDevice>) response;
                    bloodAdapter.setData(bloodDatalist);
                    if (clickId == 2) {
                        setChart();
                    }
                    hideLoadingToast();
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                bloodDatalist = new ArrayList<BloodDevice>();
                bloodAdapter.setData(bloodDatalist);
                showToast(msg);
                Logger.e(this, "getBloodsList " + msg + "");
            }
        });
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

        // mChart.animateX(2500);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend(); // 设置标示，就是那个一组y的value的
        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.CIRCLE);// 样式
        l.setFormSize(6f);// 字体
        l.setTextColor(Color.BLACK);// 颜色
        l.setTypeface(mTf);// 字体

        /**
         * 高压警示线
         */
        LimitLine ll1 = new LimitLine(140f, "140");
        ll1.setLineWidth(2f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTextColor(Color.RED);
        ll1.setTypeface(tf);

        /**
         * 低压警示线
         */
        LimitLine ll2 = new LimitLine(90f, "90");
        ll2.setLineWidth(2f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setTextColor(Color.RED);
        ll2.setTypeface(tf);

        XAxis xAxis = mChart.getXAxis();// x轴的标示
        xAxis.setTypeface(tf);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        YAxis leftAxis = mChart.getAxisLeft();// y轴的标示
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid
        // overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaxValue(220f);
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

    //   画点
    LineData getData(int count) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i <= count; i++) {
//        for (int i = count; i >=0; i--) {
            // x轴显示的数据，这里默认使用数字下标显示
            xVals.add(i + "");
        }
        if (count == -1) {
            xVals.clear();
        }

        // y轴的数据  收缩压
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i <= count; i++) {
//        for (int i = count; i >=0; i--) {
//            int i = count; i >=0; i--
//            int Min = strChart.path("data").path(i).path("sys").asInt();
            int Min = Integer.parseInt(bloodDatalist.get(i).getSys().trim());

            yVals1.add(new Entry(Min, i));
        }
        if (count == -1) {
            yVals1.clear();
        }
        // y轴的数据集合
        LineDataSet set1 = new LineDataSet(yVals1, "收缩压(mmHg)");
        set1.setFillAlpha(110);
        set1.setFillColor(Color.YELLOW);

        set1.setLineWidth(1.75f); // 线宽
        set1.setCircleSize(3f);// 显示的圆形大小
        set1.setColor(Color.RED);// 显示颜色
        set1.setCircleColor(Color.RED);// 圆形的颜色
        set1.setHighLightColor(Color.RED); // 高亮的线的颜色
        // y轴的数据
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        for (int i = 0; i <= count; i++) {
//        for (int i = count; i >=0; i--) {
//            int Max = strChart.path("data").path(i).path("dia").asInt();
            int Max = Integer.parseInt(bloodDatalist.get(i).getDia().trim());
            yVals2.add(new Entry(Max, i));
        }
        if (count == -1) {
            yVals2.clear();
        }
        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet set2 = new LineDataSet(yVals2, "舒张压(mmHg)");
        set2.setFillAlpha(110);
        set2.setFillColor(Color.YELLOW);

        set2.setLineWidth(1.75f); // 线宽
        set2.setCircleSize(3f);// 显示的圆形大小
        set2.setColor(Color.BLUE);// 显示颜色
        set2.setCircleColor(Color.BLUE);// 圆形的颜色
        set2.setHighLightColor(Color.BLUE); // 高亮的线的颜色

        // y轴的数据
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        for (int i = 0; i <= count; i++) {
//        for (int i = count; i >=0; i--) {
//            int Heart = strChart.path("data").path(i).path("pul").asInt();
            int Heart = Integer.parseInt(bloodDatalist.get(i).getPul().trim());
            yVals3.add(new Entry(Heart, i));
        }
        if (count == -1) {
            yVals3.clear();
        }
        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet set3 = new LineDataSet(yVals3, "心率(次/分钟)");
        set3.setFillAlpha(110);
        set3.setFillColor(Color.YELLOW);

        set3.setLineWidth(1.75f); // 线宽
        set3.setCircleSize(3f);// 显示的圆形大小
        set3.setColor(MyColor.COLOR18);// 显示颜色
        set3.setCircleColor(MyColor.COLOR18);// 圆形的颜色
        set3.setHighLightColor(MyColor.COLOR18); // 高亮的线的颜色

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2); // add the datasets
        dataSets.add(set3); // add the datasets
        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(MyColor.TOUMING);
        data.setValueTextSize(9f);
//        逆序排列 在把数据顺序范过来
        Collections.reverse(bloodDatalist);
        return data;
    }

    public void setChart() {
        // data0 = getData(36, 100);

        int size = 0;
        if (bloodDatalist != null && !bloodDatalist.isEmpty()) {
            size = bloodDatalist.size();
        }
//      逆序排列
        Collections.reverse(bloodDatalist);
        LineData data0 = getData(size - 1);
        setupChart(mChart, data0, COL_BACK);
    }
    private void getBloodsTongJi(String imei, String type) {
        showLoadingToast("正在加载数据");
        HealthRequest.getBloodsTongji(imei, type, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    BloodTongJi bloodTongJi = (BloodTongJi) response;
                    showTongJi(bloodTongJi);
                    hideLoadingToast();
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
                Logger.e(this, "getBloodsTongJi " + msg + "");
            }
        });
    }

    private void showTongJi(BloodTongJi bloodTongJi) {
        if (SSUtils.isEmpty(bloodTongJi)) {
            return;
        }
//       近一个月
        One one = bloodTongJi.getOne();
        if (!SSUtils.isEmpty(one)) {
            int sum = one.getCount();
            int normal = one.getNcount();
            tvAll1.setText("共测试" + sum + "次");
            tongjiNum1.setText(one.getLcount() + "");
            tongjiNum2.setText(normal + "");
            tongjiNum3.setText(one.getHcount() + "");
            if (SSUtils.calculateReate(sum, normal)) {
                allImage1.setImageResource(R.drawable.tongji1);
            } else {
                allImage1.setImageResource(R.drawable.abnormal);
            }
        }

//       近3个月
        Three three = bloodTongJi.getThree();
        if (!SSUtils.isEmpty(three)) {
            int sum = three.getCount();
            int normal = three.getNcount();
            tvAll2.setText("共测试" + sum + "次");
            tongjiNum11.setText(three.getLcount() + "");
            tongjiNum12.setText(three.getNcount() + "");
            tongjiNum13.setText(three.getHcount() + "");
            if (SSUtils.calculateReate(sum, normal)) {
                allImage2.setImageResource(R.drawable.tongji1);
            } else {
                allImage2.setImageResource(R.drawable.abnormal);
            }
        }
        //      全部
        All all = bloodTongJi.getAll();
        if (!SSUtils.isEmpty(all)) {
            int sum = all.getCount();
            int normal = all.getNcount();
            tvAll3.setText("共测试" + sum + "次");
            tongjiAll1.setText(all.getLcount() + "");
            tongjiAll2.setText(all.getNcount() + "");
            tongjiAll3.setText(all.getHcount() + "");
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
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e("FragmentBlood", "onResume");
        if (isGoToLogin) {
//            getProductDetails();
            if (MobileApplication.getInstance().isLogined) {
                getDeviceList();
            }
        }
    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }

    //广播接收器  设备列表变化
    class DeviceListChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MobileConstants.ACTION_HEALTH_LOADATA)) {
                Logger.e("FragmentBlood", "DeviceListChangeReceiver");
                if (!MobileApplication.getInstance().isLogined) {
                    clearData();
                } else {
                    getDeviceList();
                }
            }
        }
    }

    /**
     * 去够购买
     */
    public void getToBy() {
        Intent intent = new Intent(mContext, ProductListActivity.class);
        intent.putExtra("category", categoryId);
        mContext.startActivity(intent);
    }

    //   清除数据
    private void clearData() {
//        数据列表
        bloodDatalist = new ArrayList<BloodDevice>();
        bloodAdapter.setData(bloodDatalist);
//       亲属关系
//        showTypes
         devices = new ArrayList<>();
        if(resultAdapter!=null){
            resultAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        解除 mDeviceListChangeReceiver
        mContext.unregisterReceiver(mDeviceListChangeReceiver);
    }
}
