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
import com.shssjk.activity.R;
import com.shssjk.adapter.SteptAdapter;
import com.shssjk.model.health.BloodDevice;

import java.util.ArrayList;
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
    private List<BloodDevice>deviceList= new ArrayList<>();
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "历史记录");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mContext=this;
        ButterKnife.bind(this);
        mBarData = getBarData(7, 100);
        showBarChart(mBarChart, mBarData);
        super.init();
    }
    @Override
    public void initSubViews() {
        septAdapter = new SteptAdapter(mContext);
        listStep.setAdapter(septAdapter);
    }
    @Override
    public void initData() {
        if (getIntent() != null ) {
            String calories = getIntent().getStringExtra("calories");
            String distance = getIntent().getStringExtra("distance");
            String count = getIntent().getStringExtra("count");
            tvCalories.setText(calories);
            tvMileage.setText(distance);
            tvStep.setText(count);
        }
        BloodDevice device = new BloodDevice();
      for (int i=0;i<10;i++){
           device.setId(i+"");
          deviceList.add(device);
      }
        septAdapter.setData(deviceList);
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
    private BarData getBarData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xValues.add("第" + (i + 1) + "天");
        }
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        for (int i = 0; i < count; i++) {
            float value = (float) (Math.random() * range/*100以内的随机数*/) + 3;
            yValues.add(new BarEntry(value, i));
        }
        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "日期");
        barDataSet.setColor(Color.rgb(114, 188, 223));
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets
        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }
}
