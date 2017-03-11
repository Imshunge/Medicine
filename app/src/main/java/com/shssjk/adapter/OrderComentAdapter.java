package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.model.shop.ProductCommnet;
import com.shssjk.utils.SSUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 订单评价 列表
 */
public class OrderComentAdapter extends BaseAdapter {

    private String TAG = "ArticleAdapter";

    private List<ProductCommnet> mArticle;
    private Context mContext;
    private CommnetBtnClickListener mCommnetBtnClickListener;
    private ReturnBtnClickListener mReturnBtnClickListener;
    private int type = 2;   //type =1; 只显示 显示评论内容


    private String typyStr = "";    //type =1; 只显示 显示评论内容

    public OrderComentAdapter(Context context, CommnetBtnClickListener mCommnetBtnClickListener,
                              ReturnBtnClickListener mCancelBtnClickListener) {
        this.mContext = context;
        this.mCommnetBtnClickListener = mCommnetBtnClickListener;
        this.mReturnBtnClickListener = mCancelBtnClickListener;
    }

    public OrderComentAdapter(Context context, CommnetBtnClickListener mOkBtnClickListener,
                              ReturnBtnClickListener mCancelBtnClickListene, String typy) {
        this.mContext = context;
        this.mCommnetBtnClickListener = mOkBtnClickListener;
        this.mReturnBtnClickListener = mCancelBtnClickListene;
        this.typyStr = typy;
    }

    public OrderComentAdapter(Context context) {
        this.mContext = context;
    }

    public OrderComentAdapter(Context context, int type) {
        this.mContext = context;
        this.type = type;
    }

    public void setData(List<ProductCommnet> articles) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_commet_list_item, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder(convertView);
            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
        final ProductCommnet ProductCommnet = (ProductCommnet) mArticle.get(position);
        holder.orderId.setText("订单号："+ProductCommnet.getOrderSn());
        holder.nameTxtv.setText(ProductCommnet.getGoodsName());
        holder.numTxtv.setText("x "+ProductCommnet.getGoodsNum());
        holder.shopPriceTxtv.setText("¥"+ProductCommnet.getGoodsPrice());

        String imgUrlheader = MobileConstants.BASE_HOST+ProductCommnet.getOriginalImg();
        Glide.with(mContext).load(imgUrlheader).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImgv);

        if("1".equals(typyStr)){
            holder.btnCommnet.setText("已评价");
            holder.ll_commnent.setVisibility(View.VISIBLE);
            int size = ProductCommnet.getCommentList().size();
            if(size>0) {
                if (!SSUtils.isEmpty(ProductCommnet.getCommentList().get(0))) {
                    holder.comment_txtv.setText(ProductCommnet.getCommentList().get(0).getContent());
                }
            }
        }
        holder.btnCommnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommnetBtnClickListener != null) mCommnetBtnClickListener.commentClick(ProductCommnet);
            }
        });
        holder.btnReturngoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReturnBtnClickListener != null) mReturnBtnClickListener.returnBtnClick(ProductCommnet);
            }
        });

        return convertView;
    }



    public interface CommnetBtnClickListener {
        public void commentClick(ProductCommnet order);//
    }

    public interface ReturnBtnClickListener {
        public void returnBtnClick(ProductCommnet order);//取消
    }


    class ViewHolder {
        @Bind(R.id.order_id)
        TextView orderId;
        @Bind(R.id.position)
        TextView position;
        @Bind(R.id.pic_imgv)
        ImageView picImgv;
        @Bind(R.id.name_txtv)
        TextView nameTxtv;
        @Bind(R.id.shop_price_txtv)
        TextView shopPriceTxtv;
        @Bind(R.id.num_txtv)
        TextView numTxtv;
        @Bind(R.id.swipe)
        FrameLayout swipe;
        @Bind(R.id.order_product_detail_txtv)
        TextView orderProductDetailTxtv;
        @Bind(R.id.btn_returngoods)
        Button btnReturngoods;
        @Bind(R.id.btn_commnet)
        Button btnCommnet;
        @Bind(R.id.comment_txtv)
        TextView comment_txtv;
        @Bind(R.id.product_list_gallery_lyaout)
        LinearLayout product_list_gallery_lyaout;
        @Bind(R.id.ll_commnent)
        LinearLayout ll_commnent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
