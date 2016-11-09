package com.shssjk.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.model.SPProduct;
import com.shssjk.utils.SPOrderUtils;
import com.soubao.tpshop.utils.SPCommonUtils;

import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author
 *
 */
public class SPProductShowListAdapter extends BaseAdapter {

	private String TAG = "SPProductShowListAdapter";

	private List<SPProduct> mProductLst ;
	private Context mContext ;
	private Handler mHandler;

	public SPProductShowListAdapter(Context context , Handler handler){
		this.mContext = context;
		this.mHandler = handler;

	}
	
	public void setData(List<SPProduct> products){
		if(products == null)return;
		this.mProductLst = products;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mProductLst == null)return 0;
		return mProductLst.size();
	}

	@Override
	public Object getItem(int position) {
		if(mProductLst == null) return null;
		return mProductLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mProductLst == null) return -1;
		return Long.valueOf(mProductLst.get(position).getGoodsID());
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.product_show_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.picIngv = ((ImageView) convertView.findViewById(R.id.product_pic_imgv));
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.product_name_txtv));
			holder.specTxtv = ((TextView) convertView.findViewById(R.id.product_spec_txtv));
			holder.countTxtv = ((TextView) convertView.findViewById(R.id.product_count_txtv));
			holder.priceTxtv = ((TextView) convertView.findViewById(R.id.product_price_txtv));
			holder.applyReturnBtn = ((Button) convertView.findViewById(R.id.product_apply_return_btn));
			holder.commentBtn = ((Button) convertView.findViewById(R.id.product_apply_comment_btn));

			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        final SPProduct product = (SPProduct)mProductLst.get(position);

		String imgUrl1 = SPCommonUtils.getThumbnail(MobileConstants.FLEXIBLE_THUMBNAIL, product.getGoodsID());
		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);

		if (!SPStringUtils.isEmpty(product.getGoodsName())){
			holder.nameTxtv.setText(product.getGoodsName());
		}

		if (!SPStringUtils.isEmpty(product.getSpecKeyName())){
			holder.specTxtv.setText(product.getSpecKeyName());
		}

		if (!SPStringUtils.isEmpty(product.getGoodsNum())){
			holder.countTxtv.setText("x"+product.getGoodsNum());
		}

		if (!SPStringUtils.isEmpty(product.getGoodsPrice())){
			holder.priceTxtv.setText("¥" + product.getGoodsPrice());
		}

		if (product.getReturnBtn()==1 && product.getIsSend() == 1){
			holder.applyReturnBtn.setVisibility(View.VISIBLE);
			holder.applyReturnBtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					if(mHandler != null){
						Message msg = mHandler.obtainMessage(MobileConstants.MSG_CODE_AFTERSALE);
						msg.obj = product;
						mHandler.sendMessage(msg);
					}
				}
			});
		}else{
			holder.applyReturnBtn.setVisibility(View.INVISIBLE);
		}

		//订单状态处于待评价, 商品还未评价
		if ("0".equals(product.getIsComment()) && product.getOrderStatusCode().equals(SPOrderUtils.typeWaitComment)){
			holder.commentBtn.setVisibility(View.VISIBLE);
			holder.commentBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mHandler != null){
						Message msg = mHandler.obtainMessage(MobileConstants.MSG_CODE_COMMENT);
						msg.obj = product;
						mHandler.sendMessage(msg);
					}
				}
			});
		}else{
			holder.commentBtn.setVisibility(View.INVISIBLE);
		}
        return convertView;
	}
	
	class ViewHolder{
		ImageView picIngv ;
		TextView nameTxtv;	//商品名称
		TextView specTxtv; 	//商品规格
		TextView countTxtv;	//商品数量
		TextView priceTxtv;	//商品价格
		Button applyReturnBtn;	//申请售后
		Button commentBtn;	//评价
	}

}
