package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.shssjk.activity.R;
import com.shssjk.model.shop.SPCoupon;
import com.soubao.tpshop.utils.SPCommonUtils;

import java.util.List;

/**
 * @author
 *
 */
public class SPOrderCouponAdapter extends BaseAdapter{

	private List<SPCoupon> mCoupons;
	private SPCoupon selectCoupon;
	private Context mContext ;

	public SPOrderCouponAdapter(Context context, List<SPCoupon> coupons, SPCoupon selectCoupon){
		this.mContext = context;
		if(coupons == null)return;
		this.mCoupons = coupons;
		this.selectCoupon = selectCoupon;
		notifyDataSetChanged();
	 }

	public void setSelectCoupon(SPCoupon selectCoupon){
		this.selectCoupon = selectCoupon;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(this.mCoupons == null)return 0;
		return this.mCoupons.size();
	}

	@Override
	public Object getItem(int position) {
		if(this.mCoupons == null) return null;
		return this.mCoupons.get(position);

	}

	@Override
	public long getItemId(int position) {
		 return -1;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
			//使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.order_coupon_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.checkImgv = ((ImageView) convertView.findViewById(R.id.order_coupon_check_imgv)) ;
			holder.titleTxtv = ((TextView) convertView.findViewById(R.id.order_coupon_title_txtv)) ;
			holder.subTitleTxtv = ((TextView) convertView.findViewById(R.id.order_coupon_sub_title_txtv)) ;
			//设置标记
			convertView.setTag(holder);
        }else{
      	  	holder = (ViewHolder) convertView.getTag();
        }

		//获取该行数据
		final SPCoupon coupon = mCoupons.get(position);
		if ( selectCoupon != null && selectCoupon.getCouponType() == 1 && selectCoupon.getCouponID().equals(coupon.getCouponID())){
			holder.checkImgv.setBackgroundResource(R.drawable.icon_checked);
		}else {
			holder.checkImgv.setBackgroundResource(R.drawable.icon_checkno);
		}

		holder.titleTxtv.setText(coupon.getName());

		if (coupon.getSendTime()!=null){
			String sendTime = SPCommonUtils.getDateShortTime(Long.valueOf(coupon.getSendTime().trim()));
			holder.subTitleTxtv.setText("发放时间:"+sendTime);
		}

        return convertView;
	}

	class ViewHolder{
		ImageView checkImgv;
		TextView titleTxtv;
		TextView subTitleTxtv;
	}



}
