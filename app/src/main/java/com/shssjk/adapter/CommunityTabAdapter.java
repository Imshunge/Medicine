package com.shssjk.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shssjk.fragment.CommunityRecommendFragment;
import com.shssjk.fragment.SSJKCommunityFragment;


public class CommunityTabAdapter extends FragmentPagerAdapter {

	private CommunityRecommendFragment communityRecommendFragment;	//精彩推荐
	private SSJKCommunityFragment ssjkCommunityFragment;//尚尚江湖


	//Titles的标识
	public static  String[] communityTitles = new String[]{"尚尚江湖", "精彩推荐"};

	public CommunityTabAdapter(FragmentManager fm) {
		super(fm);
		ssjkCommunityFragment = new SSJKCommunityFragment();

		communityRecommendFragment = new CommunityRecommendFragment();

	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0){
			ssjkCommunityFragment.loadData();
			return ssjkCommunityFragment;
		}else
			communityRecommendFragment.loadData();
			return communityRecommendFragment;

	}

	@Override
	public CharSequence getPageTitle(int position) {
		return communityTitles[position];
	}

	@Override
	public int getCount() {
		return communityTitles.length;
	}

}
