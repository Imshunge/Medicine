package com.shssjk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.person.StoneDetail;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 石头明细
 */
public class StoneDetailAdapter extends BaseAdapter {

    private String TAG = "StoneDetailAdapter";

    private List<StoneDetail> mArticle;
    private Context mContext;

    public StoneDetailAdapter(Context context) {
        this.mContext = context;

    }

    public void setData(List<StoneDetail> articles) {
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
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //category_left_item.xml
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.stone_detil_list_item, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder(convertView);

            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
        StoneDetail stoneDetail = (StoneDetail) mArticle.get(position);
        if (!SPStringUtils.isEmpty(stoneDetail.getCreateTime())) {
            holder.tvDate.setText(stoneDetail.getCreateTime());
        }
		if (!SPStringUtils.isEmpty(stoneDetail.getSource())){
			holder.tvSource.setText(stoneDetail.getSource());
		}
        if (!SPStringUtils.isEmpty(stoneDetail.getNum())){
            holder.tvNum.setText(stoneDetail.getNum());
        }
        if (!SPStringUtils.isEmpty(stoneDetail.getSign())){
            holder.tvSign.setText(stoneDetail.getSign());
            if("-".equals(stoneDetail.getSign())){
                holder.tvNum.setTextColor(Color.BLACK);
                holder.tvSign.setTextColor(Color.BLACK);
            }else{
                holder.tvNum.setTextColor(Color.GREEN);
                holder.tvSign.setTextColor(Color.GREEN);
            }
        }

        return convertView;
    }
    class ViewHolder {
        @Bind(R.id.tv_source)
        TextView tvSource;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_sign)
        TextView tvSign;
        @Bind(R.id.tv_num)
        TextView tvNum;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
