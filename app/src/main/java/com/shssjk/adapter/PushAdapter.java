package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.Push;
import com.shssjk.utils.SSUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 推送消息列表
 */
public class PushAdapter extends BaseAdapter {

    private String TAG = "PushAdapter";

    private List<Push> pushList;
    private Context mContext;

    public PushAdapter(Context context) {
        this.mContext = context;

    }

    public void setData(List<Push> articles) {
        if (articles == null) return;
        this.pushList = articles;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (pushList == null) return 0;
        return pushList.size();
    }

    @Override
    public Object getItem(int position) {
        if (pushList == null) return null;
        return pushList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (pushList == null) return -1;
        return Long.valueOf(pushList.get(position).getId());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //category_left_item.xml
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_push, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder(convertView);
            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        Push push = (Push) pushList.get(position);
        if (!SSUtils.isEmpty(push.getTitle())) {
            holder.tvItemArticleTitle.setText(push.getTitle());
        }
        if (!SSUtils.isEmpty(push.getDescription())) {
            holder.tvItemHotContent.setText(push.getDescription());
        }
        if(!SSUtils.isEmpty(push.getPushTime()))
        {
            holder.tvDate.setText(push.getPushTime());
        }
        return convertView;
    }



    static class ViewHolder {
        @Bind(R.id.iv_item_article)
        ImageView ivItemArticle;
        @Bind(R.id.tv_item_article_title)
        TextView tvItemArticleTitle;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_item_hot_content)
        TextView tvItemHotContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
