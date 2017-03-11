package com.shssjk.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shssjk.fragment.CouponListFragment;

/**
 * 负责控制商品Fragment 界面
 */

public class CouponTab2Adapter extends FragmentPagerAdapter {


    private CouponListFragment unuseFragment;    //未用
    private CouponListFragment usedFragment;    //已用
    private CouponListFragment expireFragment;//过期
    //Titles的标识
    public static String[] productDetailInnerTitles = new String[]{"可用", "已用", "过期"};
    public CouponTab2Adapter(FragmentManager fm) {
        super(fm);
        unuseFragment = new CouponListFragment();
        unuseFragment.setType(0);

        usedFragment = new CouponListFragment();
        usedFragment.setType(1);

        expireFragment = new CouponListFragment();
        expireFragment.setType(2);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            unuseFragment.loadData();
            return unuseFragment;
        } else if (position == 1) {
            usedFragment.loadData();
            return usedFragment;
        } else {
            expireFragment.loadData();
            return expireFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return productDetailInnerTitles[position];
    }

    @Override
    public int getCount() {
        return productDetailInnerTitles.length;
    }

}
