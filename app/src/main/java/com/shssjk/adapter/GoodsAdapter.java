
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
import com.shssjk.model.SPProduct;
import com.soubao.tpshop.utils.SPStringUtils;

import org.w3c.dom.Text;

import java.util.List;


/**
 * @author
 * 商品列表
 */
public class GoodsAdapter extends BaseAdapter {

	private String TAG = "GoodsAdapter";
	private String type="0";
	private List<SPProduct> mArticle;
	private Context mContext ;

	public GoodsAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<SPProduct> articles, String type){
		if(articles == null)return;
		this.mArticle = articles;
		this.notifyDataSetChanged();
		this.type=type;
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
		return Long.valueOf(mArticle.get(position).getGoodsID());
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_order_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.picIngv = ((ImageView) convertView.findViewById(R.id.pic_imgv));
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.name_txtv));
			holder.numTxtv = ((TextView) convertView.findViewById(R.id.num_txtv));
			holder.priceTxtv = ((TextView) convertView.findViewById(R.id.shop_price_txtv));
			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
		SPProduct article = (SPProduct) mArticle.get(position);

		String imgUrlheader = MobileConstants.BASE_HOST+article.getOriginalImg();
		Glide.with(mContext).load(imgUrlheader).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);
//
		if (!SPStringUtils.isEmpty(article.getGoodsName())){
			holder.nameTxtv.setText(article.getGoodsName());
		}
		if (!SPStringUtils.isEmpty(article.getGoodsNum())){
			holder.numTxtv.setText("数量:"+article.getGoodsNum());
		}

		if (!SPStringUtils.isEmpty(article.getGoodsPrice())){
			if("3".equals(type)){
				holder.priceTxtv.setText(article.getGoodsPrice()+" 积分");
			}else{
				holder.priceTxtv.setText("¥ "+article.getGoodsPrice());
			}
		}
		return convertView;
	}
	
	class ViewHolder{

		ImageView picIngv ;
		TextView nameTxtv;
		TextView numTxtv;
		TextView priceTxtv;
	}


}
