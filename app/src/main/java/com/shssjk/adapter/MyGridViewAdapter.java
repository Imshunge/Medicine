package com.shssjk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.model.shop.Tmenu;

import java.util.List;


/**
 * GridView 适配器
 */
public class MyGridViewAdapter extends BaseAdapter {
    private Context mContext;
    /**
     * 每个分组下的每个子项的 GridView 数据集合
     */
    private List<Tmenu> itemGridList;

    public MyGridViewAdapter(Context mContext, List<Tmenu> itemGridList) {
        this.mContext = mContext;
        this.itemGridList = itemGridList;
    }
    @Override
    public int getCount() {
        return itemGridList.size();
    }
    @Override
    public Object getItem(int position) {
        return itemGridList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.category_right_item, null);
        }
        TextView tvGridView = (TextView) convertView.findViewById(R.id.category_item_r1_txtv);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.category_item_r1_imgv);
        tvGridView.setText(itemGridList.get(position).getName());
        String imgUrl = MobileConstants.BASE_HOST + itemGridList.get(position).getImage();
        Glide.with(mContext).load(imgUrl).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        return convertView;
    }
}
