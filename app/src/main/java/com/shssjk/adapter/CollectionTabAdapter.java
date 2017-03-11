package com.shssjk.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shssjk.fragment.CollectionGoodsFragment;
import com.shssjk.fragment.CollectionInformationFragment;
import com.shssjk.fragment.CollectionWikiFragment;
import com.shssjk.fragment.Order.ReturnGoodsFragment;
import com.shssjk.fragment.Order.WaitPayFragment;
import com.shssjk.fragment.Order.WaitReceiveFragment;
import com.shssjk.fragment.Order.WaitSendFragment;

/**
 *  负责控制商品Fragment 界面
 *  我的收藏 个人中心
 */

public class CollectionTabAdapter extends FragmentPagerAdapter {

	//Titles的标识
	public static String[] productDetailInnerTitles = new String[]{"资讯", "百科", "商品"};
	private CollectionWikiFragment wikiFragment;
	private CollectionGoodsFragment goodsFragment;
	private CollectionInformationFragment informationFragment;



	private String goodsId ;
	private String contents ;
	public CollectionTabAdapter(FragmentManager fm, String goodsId, String contents) {
		super(fm);
		this.goodsId = goodsId;
		this.contents = contents;
		informationFragment = new CollectionInformationFragment();

		wikiFragment = new CollectionWikiFragment();
		goodsFragment = new CollectionGoodsFragment();
	}

	@Override
	public Fragment getItem(int position) {

		if (position == 0){
			return informationFragment;
		}
		 if (position == 1){
			return wikiFragment;
		}else

			return goodsFragment;

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
