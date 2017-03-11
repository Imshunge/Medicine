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
import com.shssjk.model.order.SPExchange;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 订单 退货
 */
public class OrderReturnGoodsAdapter extends BaseAdapter {


    private List<SPExchange> mArticle;
    private Context mContext;

    public OrderReturnGoodsAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<SPExchange> articles) {
        if (articles == null) return;
        this.mArticle = articles;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (mArticle == null) return 0;
        return mArticle.size();
    }
    @Override
    public Object getItem(int position) {
        if (mArticle == null) return null;
        return mArticle.get(position);
    }
    @Override
    public long getItemId(int position) {
        if (mArticle == null) return -1;
        return Long.valueOf(mArticle.get(position).getGoodsId());
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //category_left_item.xml
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_list_item_return, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder(convertView);
            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
        final SPExchange spExchange = (SPExchange) mArticle.get(position);

        if (!SPStringUtils.isEmpty(spExchange.getOrderSN())){
            holder.orderId.setText("订单号："+spExchange.getOrderSN());
        }
        if (!SPStringUtils.isEmpty(spExchange.getReason())){
            holder.orderReason.setText("原因："+spExchange.getReason());
        }
        if (!SPStringUtils.isEmpty(spExchange.getAddtime())){
            holder.orderAddtime.setText("退货时间："+SSUtils.TimeStamp2Date(spExchange.getAddtime(), "yyyy-MM-dd HH:mm:ss"));
        }
        String imgUrlheader = MobileConstants.BASE_HOST+spExchange.getOriginal_img();
        Glide.with(mContext).load(imgUrlheader).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv);

        if (!SPStringUtils.isEmpty(spExchange.getGoodsName())){
            holder.nameTxtv.setText(spExchange.getGoodsName());
        }
        return convertView;
    }
    static class ViewHolder {
        @Bind(R.id.order_id)
        TextView orderId;
        @Bind(R.id.order_addtime)
        TextView orderAddtime;
        @Bind(R.id.order_reason)
        TextView orderReason;
        @Bind(R.id.position)
        TextView position;
        @Bind(R.id.pic_imgv)
        ImageView picImgv;
        @Bind(R.id.name_txtv)
        TextView nameTxtv;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
