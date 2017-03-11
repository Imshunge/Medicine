package com.shssjk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.health.Device;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 设备列表 列表
 */
public class DeviceAdapter extends BaseAdapter {
    private InDelBtnClickListener inDelBtnClickListener;
    private String TAG = "DeviceAdapter";

    private List<Device> mArticle;
    private Context mContext;

    public DeviceAdapter(Context context) {
        this.mContext = context;
    }
    public DeviceAdapter(Context context,InDelBtnClickListener inDelBtnClickListener) {
        this.mContext = context;
        this.inDelBtnClickListener=inDelBtnClickListener;
    }

    public void setData(List<Device> articles) {
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
        return Long.valueOf(mArticle.get(position).getId());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //category_left_item.xml
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_device, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder(convertView);
            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
        final Device article = (Device) mArticle.get(position);
        holder.userTxtv.setText(article.getRelation());
        holder.devicenumTxtv.setText(article.getValue());
        String str =article.getDefault().trim();
//        default 、是否是最关心设备（1是 0 否）
        if("1".equals(str)){
            holder.userTxtv.setTextColor(Color.RED);
            holder.devicenumTxtv.setTextColor(Color.RED);
        }else{
            holder.userTxtv.setTextColor(Color.BLACK);
            holder.devicenumTxtv.setTextColor(Color.BLACK);
        }
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inDelBtnClickListener != null) inDelBtnClickListener.delBtn(article);
            }
        });


        return convertView;
    }

    public interface InDelBtnClickListener{
        public void delBtn(Device order);
    }
    static class ViewHolder {
        @Bind(R.id.user_txtv)
        TextView userTxtv;
        @Bind(R.id.devicenum_txtv)
        TextView devicenumTxtv;
        @Bind(R.id.delete_btn)
        Button deleteBtn;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
