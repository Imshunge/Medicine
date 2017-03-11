package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.health.SugarData;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 血糖列表   数据未改
 */
public class SugarAdapter extends BaseAdapter {

    private String TAG = "BloodAdapter";

    private List<SugarData> mArticle;
    private Context mContext;

    public SugarAdapter(Context context) {
        this.mContext = context;

    }

    public void setData(List<SugarData> articles) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sugar, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder(convertView);

            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        SugarData sugarData = (SugarData) mArticle.get(position);
        String  foodstatus = sugarData.getFoodstatus().trim();
        //        1:空腹，2:早餐后，3:午餐前，4:午餐后，5:晚餐前，6:晚餐后，7:睡前，8:凌晨，
//                           9：随机，A:餐前，B:餐后
        holder.itemSg1.setText("");
        holder.itemSg2.setText("");
        holder.itemSg3.setText("");
        holder.itemSg4.setText("");
        holder.itemSg5.setText("");
        holder.itemSg6.setText("");
        holder.itemSg7.setText("");
        holder.itemSg8.setText("");
        if(!SSUtils.isEmpty(foodstatus)){

            switch (foodstatus){
                case "1":
                    holder.itemSg1.setText(sugarData.getResult());
                    break;
                case "2":
                    holder.itemSg2.setText(sugarData.getResult());
                    break;
                case "3":
                    holder.itemSg3.setText(sugarData.getResult());
                    break;
                case "4":
                    holder.itemSg4.setText(sugarData.getResult());
                    break;
                case "5":
                    holder.itemSg5.setText(sugarData.getResult());
                    break;
                case "6":
                    holder.itemSg6.setText(sugarData.getResult());

                    break;
                case "7":
                    holder.itemSg7.setText(sugarData.getResult());
                    break;
                case "8":
                    holder.itemSg8.setText(sugarData.getResult());
                    break;
            }
        }
        if (!SPStringUtils.isEmpty(sugarData.getTesttime())){
            holder.itemSg0.setText(sugarData.getTesttime());
        }

        return convertView;
    }




    class ViewHolder {
        @Bind(R.id.item_sg0)
        TextView itemSg0;
        @Bind(R.id.item_sg1)
        TextView itemSg1;
        @Bind(R.id.item_sg2)
        TextView itemSg2;
        @Bind(R.id.item_sg3)
        TextView itemSg3;
        @Bind(R.id.item_sg4)
        TextView itemSg4;
        @Bind(R.id.item_sg5)
        TextView itemSg5;
        @Bind(R.id.item_sg6)
        TextView itemSg6;
        @Bind(R.id.item_sg7)
        TextView itemSg7;
        @Bind(R.id.item_sg8)
        TextView itemSg8;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
