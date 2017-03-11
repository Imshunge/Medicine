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

import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;

import java.util.List;

/**
 * @author
 * 热门搜索
 */
public class HotSearchAdapter extends BaseAdapter {
	private String TAG = "ArticleAdapter";
	private Handler mHandler;

	private List<String> mArticle;
	private Context mContext ;
	public HotSearchAdapter(Context context, Handler handler){
		this.mContext = context;
		this.mHandler = handler;
	}
	public void setData(List<String> articles){
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
		return  position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.order_button_gallery_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.button = ((Button) convertView.findViewById(R.id.id_index_gallery_item_button));
			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        
        holder.button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v instanceof Button) {
					Button hotBtn = (Button) v;
					if (mHandler != null) {
						Message msg = mHandler.obtainMessage(MobileConstants.MSG_CODE_SEARCHKEY);
						msg.obj = hotBtn.getText();
						mHandler.sendMessage(msg);
					}
				}
			}
		});
        holder.button.setText(mArticle.get(position));
		return convertView;
	}
	
	class ViewHolder{
		ImageView picIngv ;
		TextView nameTxtv;	//分类名称
		TextView contentTxtv;	//描述
		TextView commentTxtv;	//评论数
		TextView readTxtv;	//阅读数
		Button button;
		
	}


}
