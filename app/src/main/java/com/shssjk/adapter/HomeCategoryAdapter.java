
package com.shssjk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.shssjk.activity.R;
import com.shssjk.activity.shop.ProductAllActivity;
import com.shssjk.model.SPHomeCategory;
import com.shssjk.model.SPProduct;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeCategoryAdapter extends RecyclerView.Adapter implements SPProductListAdapter.ItemClickListener {
	//private List<SPCategory> mCategroy ;
	private List<SPHomeCategory> mHomeCategory ;
	private Context mContext;
	private LayoutInflater mInflater;
	JSONObject mDataJson;
	private LayoutInflater mLayoutInflater;

	public HomeCategoryAdapter(Context context){
		mContext = context;
		mDataJson = new JSONObject();
		mLayoutInflater = LayoutInflater.from(context);
		this.mHomeCategory = new ArrayList<SPHomeCategory>();
	}

	public void setData(List<SPHomeCategory> homeCategory){
		if (homeCategory == null){
			this.mHomeCategory = new ArrayList<SPHomeCategory>();
		}else{
			this.mHomeCategory = homeCategory;
		}
		this.notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mLayoutInflater.inflate(R.layout.home_list_item, null, false));
	}
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

		SPHomeCategory homeCategory = mHomeCategory.get(position);
		ViewHolder viewHolder = (ViewHolder) holder;

		viewHolder.title1.setText(homeCategory.getName());
		SPProductListAdapter mAdapter = new SPProductListAdapter(mContext,HomeCategoryAdapter.this);
		if(homeCategory != null) {
			mAdapter.setData(homeCategory.getGoodsList());
			mAdapter.notifyDataSetChanged();
		}
		viewHolder.gridview.setAdapter(mAdapter);
	}
	@Override
	public int getItemCount() {
		if(mHomeCategory == null)return 0;
		return mHomeCategory.size();
	}
	private class ViewHolder extends RecyclerView.ViewHolder {

		public ListView gridview;
		public TextView title1;
		public TextView title2;

		public ViewHolder(View itemView) {
			super(itemView);
			title1 = (TextView)itemView.findViewById(R.id.home_list_item_header_title1);
			title2 = (TextView)itemView.findViewById(R.id.home_list_item_header_title2);
			gridview = (ListView)itemView.findViewById(R.id.home_lsit_item_grid);

			/*textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					ListItem listItem = mDataList.get(RecyclerViewUtils.getAdapterPosition(mRecyclerView, ViewHolder.this));
					startActivity(new Intent(MainActivity.this, listItem.activity));
				}
			});*/
		}
	}


	@Override
	public void onItemClickListener(SPProduct product) {
		Intent intent = new Intent(mContext , ProductAllActivity.class);
		intent.putExtra("goodsId", product.getGoodsID());
		mContext.startActivity(intent);
	}
}
