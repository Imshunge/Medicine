
package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.health.StepHistory;
import com.shssjk.utils.SSUtils;

import java.util.List;

/**
 * @author
 * 计步器列表   数据未改
 */
public class SteptAdapter extends BaseAdapter {

	private String TAG = "BloodAdapter";

	private List<StepHistory> mArticle;
	private Context mContext ;

	public SteptAdapter(Context context){
		this.mContext = context;

	}

	public void setData(List<StepHistory> articles){
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
		return position;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.stepts_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.stepTxtv = ((TextView) convertView.findViewById(R.id.item_chart1));
			holder.dateTxtv = ((TextView) convertView.findViewById(R.id.item_chart2));
			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
		StepHistory article = (StepHistory) mArticle.get(position);
		if (!SSUtils.isEmpty(article.getDate())){
			holder.dateTxtv.setText(article.getDate());
		}
		if (!SSUtils.isEmpty(article.getStep())){
			holder.stepTxtv.setText(article.getStep());
		}
		return convertView;
	}
	class ViewHolder{
		TextView dateTxtv;
		TextView stepTxtv;
	}
}
