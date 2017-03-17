
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
import com.shssjk.model.community.ComCategory;
import com.shssjk.model.community.SchoolList;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author
 * 首页 门派列表
 */
public class SchollListAdapter extends BaseAdapter {

	private String TAG = "SchollListAdapter";

	private List<ComCategory> mArticle;
	private Context mContext ;

	public SchollListAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<ComCategory> articles){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_title_school, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.picIngv = ((ImageView) convertView.findViewById(R.id.list_sch_icon));
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.list_sch_name));


			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
		ComCategory article = (ComCategory) mArticle.get(position);
		String imgUrl1 = MobileConstants.BASE_HOST+article.getIcon();
		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);
		if (!SPStringUtils.isEmpty(article.getTitle())){
			holder.nameTxtv.setText(article.getTitle());
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView picIngv ;
		TextView nameTxtv;	//分类名称
	}


}
