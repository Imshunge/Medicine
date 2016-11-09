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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.shssjk.activity.R;
import com.shssjk.utils.MyColor;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * 血糖
 */
public class FragmentSurger extends BaseFragment implements View.OnClickListener{
    private Context mContext;
    private TextView tv_date1, tv_date2, sg_title1, sg_title2,sg_title3, chart_bg;
    private ListView list_sugar;
    // 用来保存年月日：
    private int mYear, mMonth, mDay;
    private String code = "";
    private LineChart mChart;
    private LinearLayout lay_list_chart,layout_tongji;
    static final int DATE_DIALOG_ID1 = 1, DATE_DIALOG_ID2 = 2;
    private int dataID;
    private int clickId = 1;
    private LineData data0;
    public static int COL_BACK = Color.parseColor("#fafafa");
    Typeface mTf;
    private Spinner pop_spinner;
    private String name111 = "";
    private EditText edt_num;
    boolean flag =false;
    private PopupWindow popupWindow=null;
    ArrayList<NameValuePair> nParams = new ArrayList<NameValuePair>();
    //	private TextView sugar_bind;
    private Spinner spiner;
    private String[] binds;
    private View view;


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

        View view = inflater.inflate(R.layout.fragment_healthy_sugger, null,false);
        super.init(view);
        return view;
    }

    @Override
    public void initSubView(View v) {
        tv_date1 = (TextView)v. findViewById(R.id.sugar_date1);
        tv_date2 = (TextView)v. findViewById(R.id.sugar_date2);
        sg_title1 = (TextView)v. findViewById(R.id.sg_title1);
        sg_title2 = (TextView)v. findViewById(R.id.sg_title2);
        sg_title3 = (TextView)v. findViewById(R.id.sg_title3);

        sg_title1.setOnClickListener(this);
        sg_title2.setOnClickListener(this);
        sg_title3.setOnClickListener(this);


        list_sugar = (ListView)v. findViewById(R.id.list_sugar);
//		sugar_bind = (TextView) findViewById(R.id.sugar_bind);
        chart_bg = (TextView)v. findViewById(R.id.chart_bg);
        mChart = (LineChart)v. findViewById(R.id.sg_chart);
        lay_list_chart = (LinearLayout)v. findViewById(R.id.layout_list_chart);
        layout_tongji = (LinearLayout)v. findViewById(R.id.layout_tongji);
//        mChart.setOnChartValueSelectedListener(this);
        v.findViewById(R.id.sugar_btn1).setOnClickListener(this);
        v.findViewById(R.id.sugar_btn2).setOnClickListener(this);
        v.findViewById(R.id.btn_search).setOnClickListener(this);
        spiner = (Spinner)v. findViewById(R.id.relation_spin);
        spiner.setClickable(true);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
//				String addr = spiner.getSelectedItem().toString();
//                code = jsonNode.path("data").path(position).path("value").asText();// 绑定的血压计设备码，默认关心的设备
//				initTextView(jsonNode.path("data").path(position).path("relation").asText());//称呼更改
//				spiner.set
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
            case R.id.sugar_btn1:
                dataID = DATE_DIALOG_ID1;
                DatePickerDialog(getActivity().getSupportFragmentManager(), mYear, mMonth, mDay);
                break;
            case R.id.sugar_btn2:
                dataID = DATE_DIALOG_ID2;
                DatePickerDialog(getActivity().getSupportFragmentManager(), mYear, mMonth, mDay);
                break;
            case R.id.btn_search:
//                if (!isLogin()) {
//                    notlogin();
////				 return;
//                }else{
////                    getListData(code, clickId);// 获取血糖数据
//                }
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
//                getListData(code, clickId);// 获取血糖数据
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
                break;
            default:
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

}
