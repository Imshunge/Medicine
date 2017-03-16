package com.shssjk.activity.user;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.CouponTabAdapter;
import com.viewpagerindicator.TabPageIndicator;

/**
 *  优惠券
 */
public class CouponListActivity extends BaseActivity {

    private String TAG = "CouponListActivity";
    private static final int TAB_INDEX_FIRST = 0;
    private static final int TAB_INDEX_SECOND = 1;
    private static final int TAB_INDEX_THREE = 2;

    TabPageIndicator mPageIndicator;


    ViewPager mViewPager;

    FragmentPagerAdapter fragPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_coupon));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        super.init();
    }

    @Override
    public void initSubViews() {
         mPageIndicator= (TabPageIndicator) findViewById(R.id.coupon_page_indicator);
        mViewPager= (ViewPager) findViewById(R.id.coupon_view_pager);
        fragPagerAdapter = new CouponTabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(fragPagerAdapter);
        mPageIndicator.setViewPager(mViewPager, 0);
        mPageIndicator.setVisibility(View.VISIBLE);

    }
    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
