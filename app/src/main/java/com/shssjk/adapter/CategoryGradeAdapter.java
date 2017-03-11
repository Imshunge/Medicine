package com.shssjk.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.model.shop.Tmenu;

//商品分类
public class CategoryGradeAdapter extends ArrayAdapter<Tmenu> {
    private int resourceId;
    private Context context;
    private List<Tmenu>tmenuList;
    public CategoryGradeAdapter(Context context, int textViewResourceId,
                                List<Tmenu> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.context = context;
    }
    public CategoryGradeAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        resourceId = textViewResourceId;
        this.context = context;
    }
    public void setData(List<Tmenu> products){
        if (products == null){
            tmenuList = new ArrayList<Tmenu>();
        }else{
            tmenuList = products;
        }
        this.notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tmenu Tmenu =tmenuList.get(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.category_item_r1_imgv);
            viewHolder.fruitName = (TextView) view.findViewById(R.id.category_item_r1_txtv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.fruitName.setText(Tmenu.getName());
        String imgUrl = MobileConstants.BASE_HOST + Tmenu.getImage();
        Glide.with(context).load(imgUrl).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.imageView);
        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView fruitName;
    }

}
