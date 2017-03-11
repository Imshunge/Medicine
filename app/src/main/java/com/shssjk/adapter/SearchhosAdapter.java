
package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.Searchhistory;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author
 * 搜索历史记录
 */
public class SearchhosAdapter extends BaseAdapter {

	private String TAG = "SearchhistoryAdapter";

	private List<Searchhistory> mSearchhistory;
	private Context mContext ;

	public SearchhosAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<Searchhistory> articles){
		if(articles == null)return;
		this.mSearchhistory = articles;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mSearchhistory == null)return 0;
		return mSearchhistory.size();
	}

	@Override
	public Object getItem(int position) {
		if(mSearchhistory == null) return null;
		return mSearchhistory.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mSearchhistory == null) return -1;
		return position;
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.common_search_key_title_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.commentTxtv = ((TextView) convertView.findViewById(R.id.search_tittle_txtv));
			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
		Searchhistory article = (Searchhistory) mSearchhistory.get(position);
//		搜索历史内容
		if (!SPStringUtils.isEmpty(article.getTitle())){
			holder.commentTxtv.setText(article.getTitle());
		}
     	return convertView;
	}
	
	class ViewHolder{
		TextView commentTxtv;	//评论数
	}


}
