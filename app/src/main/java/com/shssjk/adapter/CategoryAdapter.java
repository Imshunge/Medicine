
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
import com.shssjk.model.info.Information;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author
 * 资讯分类
 */
public class CategoryAdapter extends BaseAdapter {

	private String TAG = "CategoryAdapter";

	private List<Information> mInformationLst;
	private Context mContext ;

	public CategoryAdapter(Context context){
		this.mContext = context;

	}
	
	public void setData(List<Information> informations){
		if(informations == null)return;
		this.mInformationLst = informations;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mInformationLst == null)return 0;
		return mInformationLst.size();
	}

	@Override
	public Object getItem(int position) {
		if(mInformationLst == null) return null;
		return mInformationLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mInformationLst == null) return -1;
		return Long.valueOf(mInformationLst.get(position).getCatId());
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.information_category_list_item, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();

			holder.picIngv = ((ImageView) convertView.findViewById(R.id.product_pic_imgv));
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.product_name_txtv));
//			holder.priceTxtv = ((TextView) convertView.findViewById(R.id.product_price_txtv));

			  //设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
		Information information = (Information) mInformationLst.get(position);
		String imgUrl1 = MobileConstants.BASE_HOST+information.getIcon();
		Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);
		if (!SPStringUtils.isEmpty(information.getCatName())){
			holder.nameTxtv.setText(information.getCatName());
		}

		return convertView;
	}
	
	class ViewHolder{
		ImageView picIngv ;
		TextView nameTxtv;	//分类名称
	}


}
