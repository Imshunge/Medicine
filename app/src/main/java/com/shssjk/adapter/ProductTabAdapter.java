package com.shssjk.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shssjk.fragment.ProductCommentListFragment;
import com.shssjk.fragment.ProductPictureTextDetaiFragment;

/**
 *  负责控制商品Fragment 界面
 */

public class ProductTabAdapter extends FragmentPagerAdapter {
	//Titles的标识
	public static  String[] productDetailInnerTitles = new String[]{"详情", "评价"};
	private ProductPictureTextDetaiFragment pictureTextDetaiFragment;
	private ProductCommentListFragment commentListFragment;
	private String goodsId ;
	private String contents ;

	public ProductTabAdapter(FragmentManager fm, String goodsId, String contents) {
		super(fm);
		this.goodsId = goodsId;
		this.contents = contents;

		pictureTextDetaiFragment = new ProductPictureTextDetaiFragment();
		pictureTextDetaiFragment.setContent(this.goodsId);
		commentListFragment = new ProductCommentListFragment();
		commentListFragment.setGoodsId(this.goodsId);
	}
	@Override
	public Fragment getItem(int position) {
		 if (position == 0){
			pictureTextDetaiFragment.loadData("");
			return pictureTextDetaiFragment;
		}else{
			commentListFragment.loadData();
			return commentListFragment;
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
