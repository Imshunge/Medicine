package com.shssjk.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shssjk.fragment.Order.AlreadCommentFragment;
import com.shssjk.fragment.Order.WaitCommentFragment;

/**
 *  负责控制商品Fragment 界面
 */

public class CommentTabAdapter extends FragmentPagerAdapter {

	//Titles的标识
	public static String[] productDetailInnerTitles = new String[]{"待评价", "已评价"};
	private WaitCommentFragment waitCommentFragment;
	private AlreadCommentFragment alreadCommentFragment;


	private String goodsId ;
	private String contents ;
	public CommentTabAdapter(FragmentManager fm, String goodsId, String contents) {
		super(fm);
		this.goodsId = goodsId;
		this.contents = contents;
		waitCommentFragment = new WaitCommentFragment();
		alreadCommentFragment = new AlreadCommentFragment();

	}

	@Override
	public Fragment getItem(int position) {

		if (position == 0){
			waitCommentFragment.loadData();
			return waitCommentFragment;
		}
		 else {
			alreadCommentFragment.loadData();
			return alreadCommentFragment;
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
