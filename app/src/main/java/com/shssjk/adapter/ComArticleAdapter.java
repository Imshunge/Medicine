
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
import com.shssjk.global.MobileApplication;
import com.shssjk.model.community.ComArticle;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

/**
 * @author
 * 帖子列表
 */
public class ComArticleAdapter extends BaseAdapter implements View.OnClickListener {

	private String TAG = "ComArticleAdapter";

	private List<ComArticle> mArticle;
	private Context mContext ;
	private String cid;
	private ComArtilceClickListener mComArtilceClickListener;




	public ComArticleAdapter(Context context,ComArtilceClickListener comArtilceClickListener){
		this.mContext = context;
		mComArtilceClickListener=comArtilceClickListener;

	}
	
	public void setData(List<ComArticle> articles,String cid){
		if(articles == null)return;
		this.mArticle = articles;
		this.cid=cid;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.jh_note_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
//			holder.picIngv = ((ImageView) convertView.findViewById(R.id.iv_item_article));
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.jc_nickname));
			holder.contentTxtv = ((TextView) convertView.findViewById(R.id.jc_content));
			holder.dateTxtv = ((TextView) convertView.findViewById(R.id.jc_date));
			holder.delIngv = ((ImageView) convertView.findViewById(R.id.img_delete));

			holder.tzIngv = ((ImageView) convertView.findViewById(R.id.jc_image));
			holder.userIngv = ((ImageView) convertView.findViewById(R.id.img_essayuser));

			//设置标记
			  convertView.setTag(holder);


			holder.delIngv.setTag(position);
			holder.delIngv.setOnClickListener(this);
		}else{
      	  holder = (ViewHolder) convertView.getTag();
        }
//		if (!SPStringUtils.isEmpty(article.getImg())){
//			holder.contentTxtv.setText(article.getTitle());
//		}
        //获取该行数据
		ComArticle article = (ComArticle) mArticle.get(position);
		String imgUrl1 = MobileConstants.BASE_HOST+article.getImg();
		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.bg_image).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.tzIngv);
//
		String imgUrlheader = MobileConstants.BASE_HOST+article.getHeader_pic();
		Glide.with(mContext).load(imgUrlheader).placeholder(R.drawable.icon_header).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.userIngv);

		if (!SPStringUtils.isEmpty(article.getNickname())){
			holder.nameTxtv.setText(article.getNickname());
		}
		if (!SPStringUtils.isEmpty(article.getTitle())){
			holder.contentTxtv.setText(article.getTitle());
		}
//		时间
		if (!SPStringUtils.isEmpty(article.getCreateTime())){
			holder.dateTxtv.setText(article.getCreateTime());
		}
//		评论数
//		if (!SPStringUtils.isEmpty(article.getClick())){
//			holder.commentTxtv.setText(article.getClick());
//		}

		//	   删除图标
		String id= MobileApplication.getInstance().getLoginUser().getUserID();
		cid=article.getUid();
		String cuid =article.getCuid();
		if (!SSUtils.isEmpty(cuid)){
			if (cuid.equals(id)){
				holder.delIngv.setVisibility(View.VISIBLE);
			}else{
				holder.delIngv.setVisibility(View.GONE);
			}

		}


		return convertView;
	}

	@Override
	public void onClick(View v) {
		int position = 0;
		ComArticle comArticle = null ;
		if (v.getTag()!=null && v.getTag().toString() != null){
			position = Integer.valueOf(v.getTag().toString());
		}
		if (mArticle!= null && position < mArticle.size() && position >= 0){
			comArticle = mArticle.get(position);
		}
		switch (v.getId()){

			case R.id.img_delete:
				if (mComArtilceClickListener!=null)mComArtilceClickListener.deleteComment(comArticle);
				break;
		}
	}
	class ViewHolder{
		ImageView picIngv ;
		ImageView userIngv ;
		ImageView tzIngv ;
		ImageView delIngv ; //删除
		TextView nameTxtv;	//昵称
		TextView dateTxtv;	//时间
		TextView contentTxtv;	//描述
		TextView commentTxtv;	//评论数
		TextView readTxtv;	//阅读数

	}
	public interface ComArtilceClickListener{

		public void deleteComment(ComArticle comArticle);//删除评论
	}

}
