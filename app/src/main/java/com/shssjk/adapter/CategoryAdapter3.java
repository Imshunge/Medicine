
package com.shssjk.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.shssjk.activity.R;
import com.shssjk.activity.common.shop.ProductListActivity;
import com.shssjk.model.SPCategory;
import com.shssjk.model.shop.Data;
import com.shssjk.model.shop.Tmenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 
 * 商品分类
 */
public class CategoryAdapter3 extends BaseAdapter{

	private String TAG = "CategoryAdapter3";
	private List<Data> mProducts ;
	private Context mContext ;
	private CategoryGradeAdapter adapter;

	public CategoryAdapter3(Context context){
		this.mContext = context;
	}

	@Override
	public int getCount() {
		if(mProducts == null)return 0;
		return mProducts.size();
	}
	
	public void setData(List<Data> products){
		if (products == null){
			mProducts = new ArrayList<Data>();
		}else{
			mProducts = products;
		}
		this.notifyDataSetChanged();
	}
	

	@Override
	public Object getItem(int position) {
		if(mProducts == null) return null;
		return mProducts.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mProducts == null) return -1;
		return Integer.valueOf(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
	          convertView = LayoutInflater.from(mContext).inflate(R.layout.product_category_item, parent, false);
	          //使用减少findView的次数
			  holder = new ViewHolder();
			  holder.priceTxtv = (TextView) convertView.findViewById(R.id.tv_title);
			  holder.gridView = (GridView) convertView.findViewById(R.id.gview);

			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        
        if(mProducts== null){
        	Log.w(TAG, "getView mCategoryLst is null .");
        	return null;
        }
        final Data product = mProducts.get(position);
		holder.gridView.setTag(position);
		holder.priceTxtv.setText(product.getName());
//		CategoryGradeAdapter adapter = new CategoryGradeAdapter(mContext,
//				R.layout.category_right_item, product.getTmenu());
		adapter = 	new CategoryGradeAdapter(mContext,
				R.layout.category_right_item);
		holder.gridView.setAdapter(adapter);
		adapter.setData(product.getTmenu());
//		holder.gridView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Log.i(TAG, "setOnClickListener onClick...");
//			}
//		});
		holder.gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tmenu tmenu =product.getTmenu().get(position);
				startupActivity(tmenu.getId());
			}
		});


        return convertView;
	}
	class HeaderViewHolder{
		TextView titleTxtv;
	}
	class ViewHolder{

		TextView nameTxtv;
		TextView priceTxtv;
		ImageView picImgv;
		View guessView;
		GridView gridView;
	}
	public void startupActivity(String categoryId){
		Intent intent = new Intent(mContext , ProductListActivity.class);
		intent.putExtra("category", categoryId);
		mContext.startActivity(intent);
	}
}
