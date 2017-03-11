
package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.model.SPProduct;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.PopupMenu;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;
import com.shssjk.model.order.SPOrder;


/**
 * @author
 * 订单列表
 */
public class OrderAdapter extends BaseAdapter {

	private String TAG = "ArticleAdapter";

	private List<SPOrder> mArticle;
	private Context mContext ;
     private OkBtnClickListener mOkBtnClickListener;
	private CancelBtnClickListener mCancelBtnClickListener;
	private onItemClickListener   onItemClickListener;
	private int type=2;
	private String typyStr="";
	public OrderAdapter(Context context,OkBtnClickListener mOkBtnClickListener,
						CancelBtnClickListener mCancelBtnClickListener){
		this.mContext = context;
		this.mOkBtnClickListener=mOkBtnClickListener;
		this.mCancelBtnClickListener=mCancelBtnClickListener;
	}
	public OrderAdapter(Context context,OkBtnClickListener mOkBtnClickListener, CancelBtnClickListener
			mCancelBtnClickListener,onItemClickListener   onItemClickListener){
		this.mContext = context;
		this.mOkBtnClickListener=mOkBtnClickListener;
		this.mCancelBtnClickListener=mCancelBtnClickListener;
		this.onItemClickListener=onItemClickListener;

	}
	public OrderAdapter(Context context,OkBtnClickListener mOkBtnClickListener,
						CancelBtnClickListener mCancelBtnClickListene,String typy,
						onItemClickListener   onItemClickListener){
		this.mContext = context;
		this.mOkBtnClickListener=mOkBtnClickListener;
		this.mCancelBtnClickListener=mCancelBtnClickListener;
		this.typyStr=typy;
		this.onItemClickListener=onItemClickListener;
	}

	public OrderAdapter(Context context,OkBtnClickListener mOkBtnClickListener, CancelBtnClickListener
			mCancelBtnClickListene,onItemClickListener   onItemClickListener
			,String typy){
		this.mContext = context;
		this.mOkBtnClickListener=mOkBtnClickListener;
		this.mCancelBtnClickListener=mCancelBtnClickListener;
		this.onItemClickListener=onItemClickListener;
		this.typyStr=typy;
	}

	public OrderAdapter(Context context){
		this.mContext = context;
	}
	public OrderAdapter(Context context,int type){
		this.mContext = context;
		this.type=type;
	}
	public OrderAdapter(Context context,int type,onItemClickListener   onItemClickListener){
		this.mContext = context;
		this.type=type;
		this.onItemClickListener=onItemClickListener;
	}

	public void setData(List<SPOrder> articles){
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
//		if(mArticle.size()==0){
//			return null;
//		}
		return mArticle.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(mArticle == null) return 0;
		return Long.valueOf(mArticle.get(position).getOrderID());
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//category_left_item.xml
		final ViewHolder holder;
        if(convertView == null){
	          //使用自定义的list_items作为Layout
			convertView = LayoutInflater.from(mContext).inflate(R.layout.order_list_item_type, parent, false);
	        //使用减少findView的次数
			holder = new ViewHolder();
			holder.nameTxtv = ((TextView) convertView.findViewById(R.id.order_product_detail_txtv));
			holder.listView= ((ListView) convertView.findViewById(R.id.lv_goods));
			holder.btnOk= ((Button) convertView.findViewById(R.id.btn_2));
			holder.btnCancel= ((Button) convertView.findViewById(R.id.btn_1));

			//设置标记
			  convertView.setTag(holder);
        }else{
      	  holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
		final SPOrder SPOrder = (SPOrder) mArticle.get(position);
		String orderAmout = SPOrder.getOrderAmount();
//计算商品数量
		List<SPProduct> products	=SPOrder.getProducts();
		int num=0;
		for (int i = 0; i < products.size(); i++) {
			num=num +SSUtils.str2Int(products.get(i).getGoodsNum());
		}
		String goodsInfo = "共"+num+"件商品 实付款:¥"+orderAmout;
		GoodsAdapter goodsAdapter = new GoodsAdapter(mContext);
		holder.listView.setAdapter(goodsAdapter);
		goodsAdapter.setData(SPOrder.getProducts());
		SSUtils.setListViewHeightBasedOnChildren(holder.listView); //解决listview 嵌套，嵌套列表列表项只有一个的问题
		holder.btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCancelBtnClickListener!=null)mCancelBtnClickListener.cancelBtnClick(SPOrder);
			}
		});
		holder.btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOkBtnClickListener != null) mOkBtnClickListener.okClick(SPOrder);
			}
		});
		if(typyStr.equals("待收货")){
			holder.btnOk.setText("确认收货");
			holder.btnCancel.setVisibility(View.GONE);
		}
		holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (onItemClickListener != null) onItemClickListener.ItemClick(SPOrder);
			}
		});
		if(type==0){
			holder.btnOk.setVisibility(View.GONE);
			holder.btnCancel.setVisibility(View.GONE);
		}
		holder.nameTxtv.setText(goodsInfo);
		return convertView;
	}



	class ViewHolder{
		ImageView picIngv ;
		TextView nameTxtv;	//分类名称
		ListView listView;
		Button  btnOk;
		Button btnCancel;
	}
	public interface OkBtnClickListener{
		public void okClick(SPOrder order);//确认
	}
	public interface CancelBtnClickListener{
		public void cancelBtnClick(SPOrder order);//取消
	}
	public interface onItemClickListener{
		public void ItemClick(SPOrder order);//item listview的 单击事件
	}

}
