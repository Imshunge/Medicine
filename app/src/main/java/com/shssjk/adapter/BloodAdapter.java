
package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.info.Article;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author
 * 血压列表   数据未改
 */
public class BloodAdapter extends BaseAdapter {

	private String TAG = "BloodAdapter";

	private List<Article> mArticle;
	private Context mContext ;

	public BloodAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<Article> articles){
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
		return Long.valueOf(mArticle.get(position).getCatId());
		
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

//			holder.picIngv = ((ImageView) convertView.findViewById(R.id.iv_item_article));
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
		Article article = (Article) mArticle.get(position);
//		String imgUrl1 = MobileConstants.BASE_HOST+article.getThumb();
//		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);
		if (!SPStringUtils.isEmpty(article.getTitle())){
			holder.dateTxtv.setText(article.getTitle());
		}
		if (!SPStringUtils.isEmpty(article.getClick())){
			holder.heightTxtv.setText(article.getClick());
		}
		if (!SPStringUtils.isEmpty(article.getClick())){
			holder.blowTxtv.setText(article.getClick());
		}
//		描述
//		if (!SPStringUtils.isEmpty(article.get)){
//			holder.contentTxtv.setText(article.getClick());
//		}
//		评论数
//		if (!SPStringUtils.isEmpty(article.getClick())){
//			holder.commentTxtv.setText(article.getClick());
//		}


		return convertView;
	}
	
	class ViewHolder{
//		ImageView picIngv ;
		TextView dateTxtv;	//分类名称
		TextView heightTxtv;	//高压
		TextView blowTxtv;	//低压
		TextView pulseTxtv;	//脉率

	}


}
