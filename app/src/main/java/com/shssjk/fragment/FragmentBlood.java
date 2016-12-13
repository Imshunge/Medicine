package com.shssjk.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.shssjk.activity.R;
import com.shssjk.utils.MyColor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * 健康云 血压
 */

public class FragmentBlood extends BaseFragment implements View.OnClickListener {
    private Context mContext;
    Typeface mTf;
    protected ExecutorService pool = null;
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
    // 用来保存年月日：
    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView tv_date1, tv_date2;
    private int dataID;
    private Date date_start, date_end;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private Spinner spiner;
    private String[] binds;
    private int addId = 0;

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

        View view = inflater.inflate(R.layout.fragment_healthy_blood, null,false);
        super.init(view);
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
//        radioGroup_blood.setOnCheckedChangeListener(this);
        chart_bg = (TextView) v.findViewById(R.id.chart_bg);
        mChart = (LineChart) v.findViewById(R.id.chart);
//        mChart.setOnChartValueSelectedListener(this);
        list_chart = (ListView) v.findViewById(R.id.list_chart);
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
                // String addr = spiner.getSelectedItem().toString();
//                code = jsonNode.path("data").path(position).path("value").asText();// 绑定的血压计设备码，默认关心的设备
//                initTextView(jsonNode.path("data").path(position).path("relation").asText());// 称呼更改
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
    @Override
    public void onClick(View v) {
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
//                getListData(code, type_sex);
                break;
            case R.id.chart_title2:
//
                clickId = 2;
                chart_title2.setTextColor(MyColor.BLUE_9f);
                chart_title1.setTextColor(MyColor.BLACK_9c);
                chart_title3.setTextColor(MyColor.BLACK_9c);
                mChart.setVisibility(View.VISIBLE);
                layout_list_chart.setVisibility(View.GONE);
                layout_tongji.setVisibility(View.GONE);
//                getListData(code, type_sex);
                break;
            case R.id.chart_title3:
                clickId = 3;
//                chart_title3.setTextColor(MyColor.BLUE_9f);
//                chart_title1.setTextColor(MyColor.BLACK_9c);
//                chart_title2.setTextColor(MyColor.BLACK_9c);
                mChart.setVisibility(View.GONE);
                layout_list_chart.setVisibility(View.GONE);
                layout_tongji.setVisibility(View.VISIBLE);
//                getListAll(code, type_sex);
                break;
            case R.id.btn_find:
//                查询监听
//                if (!isLogin()) {
//                    notlogin();
//                    // return;
//                } else {
//                    if (clickId == 3) {
//                        getListAll(code, type_sex);
//                    } else {
//                        // getBloodID();// 5 获取血压计列表
//                        getListData(code, type_sex);
//                    }
//                }
                break;

            case R.id.text_date1:
//               开始日期
                dataID = DATE_DIALOG_ID1;
                DatePickerDialog(getActivity().getSupportFragmentManager(), mYear, mMonth, mDay);
                break;
            case R.id.text_date2:
//结束日期
                dataID = DATE_DIALOG_ID2;
                DatePickerDialog(getActivity().getSupportFragmentManager(), mYear, mMonth, mDay);
                // DatePicker date2 =new DatePicker(this);
                // showDialog(DATE_DIALOG_ID2);
                break;
            default:
                break;
        }
    }


    public void DatePickerDialog(FragmentManager supportFragmentManager, int mYear2, int mMonth2, int mDay2) {

        switch (dataID) {
            case DATE_DIALOG_ID1:
                onCreateDialog(dataID).show();
                break;
            case DATE_DIALOG_ID2:
                onCreateDialog(dataID).show();
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        Calendar c = null;
        c = Calendar.getInstance();
        dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                String m = "", d = "";
                mMonth = mMonth + 1;

                if (mMonth < 10) {
                    m = "0" + mMonth;
                } else {
                    m = String.valueOf(mMonth);
                }
                if (mDay < 10) {
                    d = "0" + mDay;
                } else {
                    d = String.valueOf(mDay);
                }
                if (dataID == DATE_DIALOG_ID1) {
                    tv_date1.setText(year + "-" + m + "-" + d);
                }
                if (dataID == DATE_DIALOG_ID2) {
                    tv_date2.setText(year + "-" + m + "-" + d);
                }
            }
        }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );

        return dialog;
    }
}
