
package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.shop.Coupon;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

/**
 * @author
 *优惠券
 *
 */
public class CouponListAdapter extends BaseAdapter {

	private List<Coupon> mCoupons ;
	private Context mContext ;
	private int mType;

	public CouponListAdapter(Context context, int type){
		this.mContext = context;
		this.mType = type;
	}
	public void setType(int type){
		this.mType = type;
	}

	public void setData(List<Coupon> coupons){
		if(coupons == null)return;
		this.mCoupons = coupons;
	}

	@Override
	public int getCount() {
		if(mCoupons == null)return 0;
		return mCoupons.size();
	}

	@Override
	public Object getItem(int position) {
		if(mCoupons == null) return null;
		return mCoupons.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mCoupons == null) return -1;
		return Integer.valueOf(mCoupons.get(position).getCouponID());
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
        if(convertView == null){
	        //使用自定义的list_items作为Layout
	        convertView = LayoutInflater.from(mContext).inflate(R.layout.person_coupon_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.couponRlayout = ((View) convertView.findViewById(R.id.coupon_rlayout)) ;
			holder.rmbTxtv = ((TextView) convertView.findViewById(R.id.coupon_rmb_txtv)) ;
		 	holder.moneyTxtv = ((TextView) convertView.findViewById(R.id.coupon_money_txtv)) ;
			holder.titleTxtv = ((TextView) convertView.findViewById(R.id.coupon_title_txtv)) ;
			holder.timeTxtv = ((TextView) convertView.findViewById(R.id.coupon_time_txtv)) ;
			//设置标记
			convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        
        Coupon coupon = mCoupons.get(position);

		String money = coupon.getMoney();
		String title = coupon.getName();
		String userTimeText = "";

		int colorId = mContext.getResources().getColor(R.color.color_font_gray);
		switch (this.mType){
			case 0:
//				holder.dataTxtv.setText(SSUtils.TimeStamp2Date(article.getAddTime(), "yyyy-MM-dd HH:mm:ss"));
				colorId = mContext.getResources().getColor(R.color.color_font_blue);
				if(!SPStringUtils.isEmpty(coupon.getUseEndTime())){
//					userTimeText = "过期时间:\n"+ SPCommonUtils.getDateFullTime(Long.valueOf(coupon.getUseEndTime()));
					userTimeText = "过期时间:\n"+	SSUtils.TimeStamp2Date(coupon.getUseEndTime(), "yyyy-MM-dd HH:mm:ss");

				}
				holder.couponRlayout.setBackgroundResource(R.drawable.icon_coupon_unuse);
				break;
			case 1:
				colorId = mContext.getResources().getColor(R.color.color_font_gray);
				if(!SPStringUtils.isEmpty(coupon.getUseTime())){
//					userTimeText = "使用时间:\n"+ SPCommonUtils.getDateFullTime(Long.valueOf(coupon.getUseTime()));

					userTimeText = "过期时间:\n"+	SSUtils.TimeStamp2Date(coupon.getUseEndTime(), "yyyy-MM-dd HH:mm:ss");

				}
				holder.couponRlayout.setBackgroundResource(R.drawable.icon_coupon_used);
				break;
			case 2:
				colorId = mContext.getResources().getColor(R.color.color_font_gray);
				if(!SPStringUtils.isEmpty(coupon.getUseTime())){

//					userTimeText = "使用时间:\n"+ SPCommonUtils.getDateFullTime(Long.valueOf(coupon.getUseTime()));

					userTimeText = "过期时间:\n"+	SSUtils.TimeStamp2Date(coupon.getUseEndTime(), "yyyy-MM-dd HH:mm:ss");

				}
				holder.couponRlayout.setBackgroundResource(R.drawable.icon_coupon_used);
				break;
		}

		if (!SPStringUtils.isEmpty(money)){
			holder.moneyTxtv.setText(Double.valueOf(money).intValue()+"");
		}

		holder.titleTxtv.setText(title);
		holder.timeTxtv.setText(userTimeText);
		holder.rmbTxtv.setTextColor(colorId);
		holder.moneyTxtv.setTextColor(colorId);
        return convertView;
	}
	
	class ViewHolder{
		View couponRlayout;
		TextView rmbTxtv;
		TextView moneyTxtv;
		TextView titleTxtv;
		TextView timeTxtv;
	}

}
