
package com.shssjk.adapter;

import android.content.Context;
import android.util.Log;
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
import com.shssjk.model.SPProduct;
import com.soubao.tpshop.utils.SPCommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 *
 */
public class SPGuessYouLikeAdapter extends BaseAdapter{

	private String TAG = "CategoryRightAdapter";
	private List<SPProduct> mProducts ;
	private Context mContext ;


	public SPGuessYouLikeAdapter(Context context){
		this.mContext = context;
	}

	@Override
	public int getCount() {
		if(mProducts == null)return 0;
		return mProducts.size();
	}
	
	public void setData(List<SPProduct> products){
		if (products == null){
			mProducts = new ArrayList<SPProduct>();
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
		return Integer.valueOf(mProducts.get(position).getGoodsID());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
	          convertView = LayoutInflater.from(mContext).inflate(R.layout.product_guess_you_like_item, parent, false);
	          //使用减少findView的次数
			  holder = new ViewHolder();
			  holder.priceTxtv = (TextView) convertView.findViewById(R.id.product_price_txtv);
			  holder.picImgv = (ImageView) convertView.findViewById(R.id.product_pic_imgv);
			  holder.nameTxtv = (TextView) convertView.findViewById(R.id.product_name_txtv);
			  holder.guessView = convertView.findViewById(R.id.guess_layout);
			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        
        if(mProducts== null){
        	Log.w(TAG, "getView mCategoryLst is null .");
        	return null;
        }
        SPProduct product = mProducts.get(position);
		String imgUrl = SPCommonUtils.getThumbnail(MobileConstants.FLEXIBLE_THUMBNAIL, product.getGoodsID());

		Glide.with(mContext).load(imgUrl).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv);
		String price = String.format(mContext.getResources().getString(R.string.product_price), Float.valueOf(product.getShopPrice()));
		holder.nameTxtv.setText(product.getGoodsName());
		holder.priceTxtv.setText(price);

		/*holder.guessView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "setOnClickListener onClick...");
			}
		});*/
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

	}

}
