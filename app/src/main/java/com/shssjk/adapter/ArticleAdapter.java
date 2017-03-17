
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
import com.shssjk.model.info.Article;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author
 * 资讯列表
 */
public class ArticleAdapter extends BaseAdapter {

	private String TAG = "ArticleAdapter";

	private List<Article> mArticle;
	private Context mContext ;

	public ArticleAdapter(Context context){
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
		return position;
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.information_article_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.picIngv = ((ImageView) convertView.findViewById(R.id.iv_item_article));
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.tv_source));
			holder.contentTxtv = ((TextView) convertView.findViewById(R.id.tv_date));
			holder.readTxtv = ((TextView) convertView.findViewById(R.id.tv_num));
			holder.commentTxtv = ((TextView) convertView.findViewById(R.id.tv_sign));

			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
		Article article = (Article) mArticle.get(position);
		String imgUrl1 = MobileConstants.BASE_HOST+article.getThumb();
		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);
		if (!SPStringUtils.isEmpty(article.getTitle())){
			holder.nameTxtv.setText(article.getTitle());
		}
		if (!SPStringUtils.isEmpty(article.getClick())){
			holder.readTxtv.setText(article.getClick());
		}
		if (!SPStringUtils.isEmpty(article.getDescription())){
			holder.contentTxtv.setText(article.getDescription());
		}
//		描述
//		if (!SPStringUtils.isEmpty(article.get)){
//			holder.contentTxtv.setText(article.getClick());
//		}
//		评论数
		if (!SPStringUtils.isEmpty(article.getCcount())){
			holder.commentTxtv.setText(article.getCcount());
		}
     	return convertView;
	}
	
	class ViewHolder{
		ImageView picIngv ;
		TextView nameTxtv;	//分类名称
		TextView contentTxtv;	//描述
		TextView commentTxtv;	//评论数
		TextView readTxtv;	//阅读数

	}


}
