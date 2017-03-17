
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
import com.shssjk.model.community.SchoolList;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author
 * 门派列表
 */
public class SchollAdapter extends BaseAdapter {

	private String TAG = "SchollAdapter";

	private List<SchoolList> mArticle;
	private Context mContext ;

	public SchollAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<SchoolList> articles){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.jh_school_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.picIngv = ((ImageView) convertView.findViewById(R.id.sch_image));
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.sch_name));
			holder.hostTxtv = ((TextView) convertView.findViewById(R.id.sch_host));
			holder.bodysTxtv = ((TextView) convertView.findViewById(R.id.sch_bodys));

			holder.articlesTxtv = ((TextView) convertView.findViewById(R.id.sch_essays));
			holder.headerIngv = ((ImageView) convertView.findViewById(R.id.sch_host_icon));
//			holder.commentTxtv = ((TextView) convertView.findViewById(R.id.tv_item_comment));

			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
		SchoolList article = (SchoolList) mArticle.get(position);
		String imgUrl1 = MobileConstants.BASE_HOST+article.getLogo();
		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.bg_image).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);
//		if (!SPStringUtils.isEmpty(article.getTitle())){
//			holder.nameTxtv.setText(article.getTitle());
//		}
		String imgUrlheader = MobileConstants.BASE_HOST+article.getHeader_pic();
		Glide.with(mContext).load(imgUrlheader).placeholder(R.drawable.icon_header).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.headerIngv);

		if (!SPStringUtils.isEmpty(article.getTitle())){
			holder.nameTxtv.setText(article.getTitle());
		}
		//	人数
		if (!SPStringUtils.isEmpty(article.getPersons())){
			holder.bodysTxtv.setText(article.getPersons());
		}
//
		if (!SPStringUtils.isEmpty(article.getNickname())){
			holder.hostTxtv.setText(article.getNickname());
		}
//		文章数
		if (!SPStringUtils.isEmpty(article.getArticles())){
			holder.articlesTxtv.setText(article.getArticles());
		}


		return convertView;
	}
	
	class ViewHolder{
		ImageView headerIngv ;
		ImageView picIngv ;
		TextView nameTxtv;	//分类名称
		TextView hostTxtv;	//hostTxtv
		TextView contentTxtv;	//描述
		TextView articlesTxtv;	//文章数
		TextView readTxtv;	//阅读数      sch_bodys
		TextView bodysTxtv;	//sch_bodys


	}


}
