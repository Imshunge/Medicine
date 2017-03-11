
package com.shssjk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.shssjk.activity.R;
import com.shssjk.model.health.BloodDevice;
import com.shssjk.utils.SSUtils;

import java.util.List;


/**
 * @author
 * 血压列表   数据未改
 */
public class BloodAdapter extends BaseAdapter {

	private String TAG = "BloodAdapter";

	private List<BloodDevice> mArticle;
	private Context mContext ;

	public BloodAdapter(Context context){
		this.mContext = context;

	}

	public void setData(List<BloodDevice> articles){
		if(articles == null)return;
		this.mArticle = articles;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mArticle == null)return 0;
		return mArticle.size();
	}

	@Override
	public Object getItem(int position) {
		if(mArticle == null) return null;
		return mArticle.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mArticle == null) return -1;
		return Long.valueOf(mArticle.get(position).getId());

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_charts, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.dateTxtv = ((TextView) convertView.findViewById(R.id.item_chart1));
			holder.heightTxtv = ((TextView) convertView.findViewById(R.id.item_chart2));
			holder.blowTxtv = ((TextView) convertView.findViewById(R.id.item_chart3));
			holder.pulseTxtv = ((TextView) convertView.findViewById(R.id.item_chart4));

			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
		BloodDevice article = (BloodDevice) mArticle.get(position);

		if (!SSUtils.isEmpty(article.getCreateTime())){
			holder.dateTxtv.setText(SSUtils.TimeStamp2Date(article.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		if (!SSUtils.isEmpty(article.getSys())){
			holder.heightTxtv.setText(article.getSys());
			if(SSUtils.str2Int(article.getSys())>139){
				holder.heightTxtv.setTextColor(Color.RED);
			}else if(SSUtils.str2Int(article.getSys())<90){
				holder.heightTxtv.setTextColor(Color.BLUE);
			}else{
				holder.heightTxtv.setTextColor(Color.BLACK);
			}
		}
		if (!SSUtils.isEmpty(article.getDia())){
			holder.blowTxtv.setText(article.getDia());
			if(SSUtils.str2Int(article.getDia())>89){
				holder.blowTxtv.setTextColor(Color.RED);
			}else if(SSUtils.str2Int(article.getDia())<60){
				holder.blowTxtv.setTextColor(Color.BLUE);
			}else{
				holder.blowTxtv.setTextColor(Color.BLACK);
			}
		}
		if (!SSUtils.isEmpty(article.getPul())){
			holder.pulseTxtv.setText(article.getPul());
		}
		return convertView;
	}
	class ViewHolder{
		TextView dateTxtv;	//时间
		TextView heightTxtv;	//高压
		TextView blowTxtv;	//低压
		TextView pulseTxtv;	//脉率
	}
}
