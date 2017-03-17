package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.person.StoneType;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 石头充值
 */
public class StoneTypeAdapter extends BaseAdapter {

    private String TAG = "SearchhistoryAdapter";
    private List<StoneType> stoneTypes;
    private Context mContext;

    public StoneTypeAdapter(Context context) {
        this.mContext = context;

    }

    public void setData(List<StoneType> articles) {
        if (articles == null) return;
        this.stoneTypes = articles;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (stoneTypes == null) return 0;
        return stoneTypes.size();
    }

    @Override
    public Object getItem(int position) {
        if (stoneTypes == null) return null;
        return stoneTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (stoneTypes == null) return -1;
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //category_left_item.xml
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.stone_type_item, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder(convertView);
            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
        StoneType article = (StoneType) stoneTypes.get(position);
//
        if (!SPStringUtils.isEmpty(article.getPrice())) {
            holder.stonePriceTxtv.setText(article.getPrice());
        }
        if (!SPStringUtils.isEmpty(article.getPrice())) {
            holder.stoneDesTxtv.setText(article.getPrice() + "个石头");
        }

//        if(article.isSelect() == true){
//            holder.ll_type.setBackgroundColor(mContext.getResources().getColor(R.color.grays));
//        } else {
//            holder.ll_type.setBackgroundColor(mContext.getResources().getColor(R.color.gray_max));
//        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.stone_price_txtv)
        TextView stonePriceTxtv;
        @Bind(R.id.stone_des_txtv)
        TextView stoneDesTxtv;
        @Bind(R.id.ll_type)
        LinearLayout ll_type;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
