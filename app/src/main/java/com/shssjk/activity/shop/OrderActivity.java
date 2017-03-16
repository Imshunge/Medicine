package com.shssjk.activity.shop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.OrderTabAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity {
    public static String[] productDetailInnerTitles = new String[]{"待付款", "待发货", "待收货", "退货"};
    List<String> mDataList = Arrays.asList(productDetailInnerTitles);
    @Bind(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private FragmentPagerAdapter fragPagerAdapter;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.order_list_titiel));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mContext = this;
        ButterKnife.bind(this);
        super.init();
    }
    @Override
    public void initSubViews() {
//       跳转至相应的页面
        fragPagerAdapter = new OrderTabAdapter(getSupportFragmentManager(), "", "");
        viewPager.setAdapter(fragPagerAdapter);
        // 当前页不定位到中间
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.sub_title));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.order_tab));
                simplePagerTitleView.setTextSize(12);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }
            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setYOffset(UIUtil.dip2px(context, 4));
                indicator.setColors(ContextCompat.getColor(context, R.color.order_tab));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        mMagicIndicator.setDelegate(new SimpleViewPagerDelegate(viewPager));
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            int index = getIntent().getIntExtra("index",0);
            viewPager.setCurrentItem(index);
        }
    }

    @Override
    public void initEvent() {

    }
}
