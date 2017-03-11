package com.shssjk.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shssjk.fragment.Order.ReturnGoodsFragment;
import com.shssjk.fragment.Order.WaitPayFragment;
import com.shssjk.fragment.Order.WaitReceiveFragment;
import com.shssjk.fragment.Order.WaitSendFragment;

/**
 *  负责控制商品Fragment 界面
 */

public class OrderTabAdapter extends FragmentPagerAdapter {

	//Titles的标识
	public static String[] productDetailInnerTitles = new String[]{"待付款", "待发货", "待收货", "退货"};
	private WaitSendFragment waitSendFragment;
	private WaitPayFragment waitPayFragment;
	private ReturnGoodsFragment returnGoodsFragment;
	private WaitReceiveFragment waitReceiveFragment;

	private String goodsId ;
	private String contents ;
	public OrderTabAdapter(FragmentManager fm, String goodsId, String contents) {
		super(fm);
		this.goodsId = goodsId;
		this.contents = contents;
		waitPayFragment = new WaitPayFragment();
		waitSendFragment = new WaitSendFragment();
		returnGoodsFragment = new ReturnGoodsFragment();
		waitReceiveFragment = new WaitReceiveFragment();
	}

	@Override
	public Fragment getItem(int position) {

		if (position == 0){
			waitPayFragment.loadData();
			return waitPayFragment;
		}
		 if (position == 1){
			waitSendFragment.loadData();
			return waitSendFragment;
		}
		if (position == 2){
			waitReceiveFragment.loadData();
			return waitReceiveFragment;
		}else{
			returnGoodsFragment.loadData();
			return returnGoodsFragment;
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
