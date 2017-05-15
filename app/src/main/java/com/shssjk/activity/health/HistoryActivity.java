package com.shssjk.activity.health;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.R;
import com.shssjk.adapter.SteptAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.health.BloodDevice;
import com.shssjk.model.health.StepHistory;
import com.shssjk.model.person.StepPersonInfo;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SSUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryActivity extends BaseActivity {
    @Bind(R.id.chart1)
    BarChart mBarChart;
    @Bind(R.id.tv_step)
    TextView tvStep;
    @Bind(R.id.tv_calories)
    TextView tvCalories;
    @Bind(R.id.tv_mileage)
    TextView tvMileage;
    @Bind(R.id.tv_today_step)
    TextView tvTodayStep;
    @Bind(R.id.list_step)
    ListView listStep;
    private BarData mBarData;
    private SteptAdapter septAdapter;
    private List<StepHistory> stepHistories = new ArrayList<>();
    private Context mContext;
    private String count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "历史记录");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mContext = this;
        ButterKnife.bind(this);
        super.init();
    }
    @Override
    public void initSubViews() {
        septAdapter = new SteptAdapter(mContext);
        listStep.setAdapter(septAdapter);
    }
    @Override
    public void initData() {
        if (getIntent() != null) {
            String calories = getIntent().getStringExtra("calories");
            String distance = getIntent().getStringExtra("distance");
            count = getIntent().getStringExtra("count");
            tvCalories.setText(calories);
            tvMileage.setText(distance);
            tvStep.setText(count);
        }
        getStepHistory();
    }
    @Override
    public void initEvent() {
    }
    private void showBarChart(BarChart barChart, BarData barData) {
        barChart.setDrawBorders(false);  ////是否在折线图上添加边框
        barChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription("没有数据");
        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//
//      barChart.setBackgroundColor();// 设置背景
        barChart.setDrawBarShadow(true);
        barChart.setData(barData); // 设置数据
        Legend mLegend = barChart.getLegend(); // 设置比例图标示
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色
//      X轴设定
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateX(2500); // 立即执行的动画,x轴
    }
    private BarData getBarData(int count, float range, List<StepHistory> stepHistories) {

        Logger.e("","============================");
        for (int l = 0; l < stepHistories.size(); l++) {
            StepHistory temp = stepHistories.get(l);
             Logger.e(this, temp.getStep() + "stepHistories  " + l + "   "
                     + temp.getDate());
        }
        Collections.reverse(stepHistories);
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xValues.add(stepHistories.get(i).getDate());
        }
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        for (int i = 0; i < count; i++) {
            yValues.add(new BarEntry(SSUtils.string2float(stepHistories.get(i).getStep()),i));
        }
        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "日期");
        barDataSet.setColor(Color.rgb(114, 188, 223));
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets
        BarData barData = new BarData(xValues, barDataSets);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Date now = new Date();
        df.format(now);
        return barData;
    }
    public void getStepHistory() {
        PersonRequest.getSteptRecord(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<StepHistory> stepHistories = (List<StepHistory>) response;
                    dealWithStepHistory(stepHistories);
                } else {
                    showToast(msg);
                }
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                List<StepHistory> stepHistories= new ArrayList<StepHistory>();
                dealWithStepHistory(stepHistories);
            }
        });
    }
    private void dealWithStepHistory(List<StepHistory> stepHistories) {
        List<StepHistory> tempstepHistories = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();// 取时间
        for (int i = 0; i < 7; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(calendar.DATE, -i);// 把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            StepHistory tempHistory = new StepHistory();
            String dateStr = df.format(date);
            tempHistory.setDate(dateStr);
            tempHistory.setStep("0");
            if (i == 0) {
                tempHistory.setStep(count);
            }
            tempstepHistories.add(tempHistory);
        }
        for (int j = 0; j < stepHistories.size(); j++) {
            StepHistory stepHistorytrue = stepHistories.get(j);
            for (int k = 0; k < tempstepHistories.size(); k++) {
                StepHistory tempHis = tempstepHistories.get(k);
                if (stepHistorytrue.getDate().equals(tempHis.getDate())) {
                    tempHis.setStep(stepHistorytrue.getStep());
                    Logger.e(this, stepHistorytrue.getStep() + "1111" + stepHistorytrue.getDate());
                } else {
                    Logger.e(this, stepHistorytrue.getStep() + "1111" + stepHistorytrue.getDate());
                }
            }
        }
        mBarData = getBarData(7, 100, tempstepHistories);

        Collections.reverse(tempstepHistories);

        showBarChart(mBarChart, mBarData);
        septAdapter.setData(tempstepHistories);
    }
}
