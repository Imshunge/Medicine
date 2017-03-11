package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.shssjk.activity.R;
import com.shssjk.model.shop.GoodsComment;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.StarView;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author
 *
 */
public class ProductDetailCommentAdapter extends BaseAdapter {

	private String TAG = "ProductDetailCommentAdapter";

	private List<GoodsComment> mComments ;
	private Context mContext ;

	public ProductDetailCommentAdapter(Context context){
		this.mContext = context;
	}
	
	public void setData(List<GoodsComment> comments){
		if(comments == null)return;
		this.mComments = comments;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mComments == null)return 0;
		return mComments.size();
	}

	@Override
	public Object getItem(int position) {
		if(mComments == null) return null;
		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return -1 ;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.product_details_comment_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.headImgv = ((ImageView) convertView.findViewById(R.id.comment_head_imgv));
			holder.usernameTxtv = ((TextView) convertView.findViewById(R.id.comment_username_txtv));
			holder.dateTxtv = ((TextView) convertView.findViewById(R.id.comment_addtime_txtv));
			holder.contentTxtv = ((TextView) convertView.findViewById(R.id.comment_content_txtv));
			holder.starView = ((StarView) convertView.findViewById(R.id.comment_star_layout));
			holder.gallery = ((LinearLayout) convertView.findViewById(R.id.comment_gallery_lyaout));
			holder.scrollv = ((HorizontalScrollView) convertView.findViewById(R.id.comment_product_scrollv));

			holder.starView.setIsResponseClickListener(false);
			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
		GoodsComment goodsComment = (GoodsComment)mComments.get(position);
		if (!SPStringUtils.isEmpty(goodsComment.getAddTime())){
			holder.dateTxtv.setText(SSUtils.TimeStamp2Date(goodsComment.getAddTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		Glide.with(mContext).load(goodsComment.getHead_pic()).placeholder(R.drawable.person_default_head).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.headImgv);

		holder.usernameTxtv.setText(goodsComment.getUsername());
		holder.contentTxtv.setText(goodsComment.getContent());

		if (goodsComment.getGoodsRank()!=null){
			int rank = Integer.valueOf(goodsComment.getGoodsRank());
			holder.starView.checkStart(rank-1);
		}

		List<String> images = goodsComment.getImages();
		if (SPCommonUtils.verifyArray(images)){
			buildProductGallery(holder.gallery,goodsComment);
			holder.scrollv.setVisibility(View.VISIBLE);
			convertView.setMinimumHeight(Float.valueOf(mContext.getResources().getDimension(R.dimen.comment_big_height)).intValue());
		}else{
			holder.scrollv.setVisibility(View.GONE);
			convertView.setMinimumHeight(Float.valueOf(mContext.getResources().getDimension(R.dimen.comment_normal_height)).intValue());
		}

        return convertView;
	}
	
	class ViewHolder{
		ImageView headImgv;
		TextView usernameTxtv;
		TextView dateTxtv;
		TextView contentTxtv;
		StarView starView;
		LinearLayout gallery;
		HorizontalScrollView scrollv;
	}

	private void buildProductGallery(LinearLayout gallery , GoodsComment goodsComment){
		gallery.removeAllViews();
		List<String> images = goodsComment.getImages();
		if (SPCommonUtils.verifyArray(images)){
			for (int i = 0; i < images.size(); i++){
				String url = images.get(i);
				View view = LayoutInflater.from(mContext).inflate(R.layout.activity_index_gallery_item, gallery, false);
				ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
				Glide.with(mContext).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
				gallery.addView(view);
			}
		}
	}

}
