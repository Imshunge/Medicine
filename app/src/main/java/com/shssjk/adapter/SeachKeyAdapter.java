
package com.shssjk.adapter;

import android.content.Context;
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
import com.shssjk.model.info.Article;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author
 * 搜索列表
 */
public class SeachKeyAdapter extends BaseAdapter {

	private String TAG = "SeachKeyAdapter";

	private List<Article> mArticle;
	private Context mContext ;
	private List<String> mSearchkeys ;
	public SeachKeyAdapter(Context context){
		this.mContext = context;
	}

	public void setData(List<String> searchkeys){
		if(searchkeys == null){
			this.mSearchkeys = new ArrayList<String>();
		}else{
			this.mSearchkeys = searchkeys;
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mSearchkeys == null)return 0;
		return mSearchkeys.size();
	}

	@Override
	public Object getItem(int position) {
		if(mSearchkeys == null) return null;
		return mSearchkeys.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mSearchkeys == null) return -1;
		return Long.valueOf(mSearchkeys.get(position));
		
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

			holder.btn = ((Button) convertView.findViewById(R.id.id_index_gallery_item_button));

			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
		String searchkey = mSearchkeys.get(position);
		if (!SPStringUtils.isEmpty(searchkey)) {
			holder.btn.setText(searchkey);
		}

     	return convertView;
	}
		class ViewHolder{

		Button btn;
	}


}
