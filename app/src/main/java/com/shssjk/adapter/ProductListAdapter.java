
package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.model.shop.Tmenu;

import java.util.List;
/**
 * @author
 *
 */
public class ProductListAdapter extends BaseAdapter {

	private String TAG = "SPProductListAdapter";

	private List<Tmenu> mProductLst;
	private Context mContext;
	private ItemClickListener mListener;

	public ProductListAdapter(Context context, ItemClickListener listener) {
		this.mContext = context;
		this.mListener = listener;

	}

	public void setData(List<Tmenu> products) {
		if (products == null) return;
		this.mProductLst = products;
	}

	@Override
	public int getCount() {
		if (mProductLst == null) return 0;

		// 每列两项
		if (mProductLst.size() % 4 == 0) {
			return mProductLst.size() / 4;
		}
		return mProductLst.size() / 4 + 1;

	}

	@Override
	public Object getItem(int position) {
		if (mProductLst == null) return null;
		return mProductLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (mProductLst == null) return -1;
		return Long.valueOf(mProductLst.get(position).getId());

	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		//category_left_item.xml
		final ViewHolder holder;
		if (convertView == null) {
			//使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.product_list_item, parent, false);
			//使用减少findView的次数
			holder = new ViewHolder();

			holder.view1 = ((View) convertView.findViewById(R.id.product_cell_layout1));
			holder.nameTxtv1 = ((TextView) convertView.findViewById(R.id.product_name_txtv1));
			holder.priceTxtv1 = ((TextView) convertView.findViewById(R.id.product_price_txtv1));
			holder.picImgv1 = ((ImageView) convertView.findViewById(R.id.product_pic_imgv1));

			holder.view2 = ((View) convertView.findViewById(R.id.product_cell_layout2));
			holder.nameTxtv2 = ((TextView) convertView.findViewById(R.id.product_name_txtv2));
			holder.priceTxtv2 = ((TextView) convertView.findViewById(R.id.product_price_txtv2));
			holder.picImgv2 = ((ImageView) convertView.findViewById(R.id.product_pic_imgv2));

			holder.bottonLineView = ((View) convertView.findViewById(R.id.product_bottom_line_view));

			//设置标记
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//获取该行数据
		Tmenu product1 = (Tmenu) mProductLst.get(position * 4 );

		//设置数据到View
		//格式化价格
//        String price1 = String.format(mContext.getResources().getString(R.string.product_price), Float.valueOf(product1.getShopPrice()));

		holder.nameTxtv1.setText(product1.getName());
//        holder.priceTxtv1.setText(""+String.valueOf(price1));
//        holder.nameTxtv1.setText(product1.getGoodsName());

//		String imgUrl1 = SPCommonUtils.getThumbnail(MobileConstants.BASE_HOST, 400, 400,MobileConstants.BASE_HOST);
		String imgUrl1= MobileConstants.BASE_HOST +product1.getImage();
		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv1);
		holder.view1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onItemClickListener((Tmenu)mProductLst.get(position * 4) );
				}
			}
		 });
        if ((position * 4 + 1) < mProductLst.size()) {
			Tmenu product2 = (Tmenu) mProductLst.get(position * 4 + 1);

			//设置数据到View

			holder.nameTxtv2.setText(product2.getName());

			String imgUrl2 = MobileConstants.BASE_HOST +product2.getImage();
			Glide.with(mContext).load(imgUrl2).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv2);
//
//
            holder.view2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mListener != null){
						mListener.onItemClickListener((Tmenu)mProductLst.get(position*2+1));
					}
				}
			 });
		}
        
        
        return convertView;
	}
	
	class ViewHolder{
		View view1;
		ImageView picImgv1 ;
		TextView nameTxtv1;	//商品名称 
		TextView priceTxtv1; //商品价格
		
		View view2;
		ImageView picImgv2 ;
		TextView nameTxtv2;	//商品名称 
		TextView priceTxtv2; //商品价格

		View bottonLineView;
	}
	
	public interface ItemClickListener{
		public void onItemClickListener(Tmenu product);
	}

}
