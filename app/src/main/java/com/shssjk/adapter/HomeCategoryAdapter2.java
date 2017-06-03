
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
import com.shssjk.model.shop.Product;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeCategoryAdapter2 extends RecyclerView.Adapter implements HomeProductListAdapter.ItemClickListener {
	//private List<SPCategory> mCategroy ;
	private List<SPHomeCategory> mHomeCategory ;
	List<Product> mProduct;
	private Context mContext;
	private LayoutInflater mInflater;
	JSONObject mDataJson;
	private LayoutInflater mLayoutInflater;

	public HomeCategoryAdapter2(Context context){
		mContext = context;
		mDataJson = new JSONObject();
		mLayoutInflater = LayoutInflater.from(context);
		this.mProduct = new ArrayList<Product>();
	}
	public void setData(List<Product> homeProduct){
		if (homeProduct == null){
			this.mProduct = new ArrayList<Product>();
		}else{
			this.mProduct = homeProduct;
		}
		this.notifyDataSetChanged();
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mLayoutInflater.inflate(R.layout.home_list_item, null, false));
	}
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Product homeCategory = mProduct.get(position);
		ViewHolder viewHolder = (ViewHolder) holder;
		HomeProductListAdapter mAdapter = new HomeProductListAdapter(mContext,HomeCategoryAdapter2.this);
		if(homeCategory != null) {
			mAdapter.setData(mProduct);
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
	public void onItemClickListener(Product product) {
		Intent intent = new Intent(mContext , ProductAllActivity.class);
		intent.putExtra("goodsId", product.getGoodsId());
		mContext.startActivity(intent);
	}
}
