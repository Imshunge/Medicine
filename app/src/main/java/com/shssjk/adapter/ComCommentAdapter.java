
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
import com.shssjk.global.MobileApplication;
import com.shssjk.model.community.ComComment;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;


/**
 * @author 江湖  帖子评论列表
 */
public class ComCommentAdapter extends BaseAdapter implements View.OnClickListener {

    private String TAG = "ComCommentAdapter";

    private List<ComComment> mComment;
    private Context mContext;
    private CommentClickListener mCommentClickListener;
    private String cid;

    public ComCommentAdapter(Context context, CommentClickListener commentClickListener) {
        this.mContext = context;
        this.mCommentClickListener = commentClickListener;
    }

    public void setData(List<ComComment> comments, String cid) {
        if (comments == null) return;
        this.mComment = comments;
        this.cid = cid;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mComment == null) return 0;
        return mComment.size();
    }

    @Override
    public Object getItem(int position) {
        if (mComment == null) return null;
        return mComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mComment == null) return -1;
        return Long.valueOf(mComment.get(position).getId());

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //category_left_item.xml
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_pinglun, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder();

//			holder.pinglun_image = (RoundImageView) convertView.findViewById(R.id.pinglun_image);
            holder.pinglun_image = (ImageView) convertView.findViewById(R.id.pinglun_image);
            holder.titleTxtv = (TextView) convertView.findViewById(R.id.pinglun_title);
            holder.contentTxtv = (TextView) convertView.findViewById(R.id.pinglun_content);
            holder.dataTxtv = (TextView) convertView.findViewById(R.id.pinglun_date);

            holder.clickIngv = (ImageView) convertView.findViewById(R.id.praise_image);

            holder.deleteIngv = (ImageView) convertView.findViewById(R.id.delete_image);

            holder.clicksumsTxtv = (TextView) convertView.findViewById(R.id.click_sums);

//			holder.pinglun_ids = (TextView) convertView.findViewById(R.id.pinglun_ids);


            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取该行数据
        ComComment article = (ComComment) mComment.get(position);
        String imgUrl1 = MobileConstants.BASE_HOST + article.getHeader_pic();
        Glide.with(mContext).load(imgUrl1).placeholder(R.drawable.icon_header).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.pinglun_image);
//		用户名
        if (!SPStringUtils.isEmpty(article.getNickname())) {
            holder.titleTxtv.setText(article.getNickname());
        }
//
        if (!SPStringUtils.isEmpty(article.getContent())) {
            holder.contentTxtv.setText(article.getContent());
        }
        if (!SPStringUtils.isEmpty(article.getCreateTime())) {
            holder.dataTxtv.setText(article.getCreateTime());
        }
//		点赞数
        if (!SPStringUtils.isEmpty(article.getLaud())) {
            holder.clicksumsTxtv.setText(article.getLaud());
        }
//		点赞图标	当前用户是否已点赞（1 已点、0 未点）
        if (article.getCstate() == 0) {
            holder.clickIngv.setBackgroundResource(R.drawable.praise);
        } else {
            holder.clickIngv.setBackgroundResource(R.drawable.praise2);
        }
//	   删除图标  门主删除
        String id = MobileApplication.getInstance().getLoginUser().getUserID();
        String cuid = article.getCuid();
        if (cuid.equals(id)) {
            holder.deleteIngv.setVisibility(View.VISIBLE);
        } else {
            holder.deleteIngv.setVisibility(View.GONE);
        }

        holder.clickIngv.setTag(position);
        holder.deleteIngv.setTag(position);
        holder.clickIngv.setOnClickListener(this);
        holder.deleteIngv.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = 0;
        ComComment comment = null;
        if (v.getTag() != null && v.getTag().toString() != null) {
            position = Integer.valueOf(v.getTag().toString());
        }
        if (mComment != null && position < mComment.size() && position >= 0) {
            comment = mComment.get(position);
        }
        switch (v.getId()) {
            case R.id.praise_image:
                if (mCommentClickListener != null) mCommentClickListener.praiseComment(comment);
                break;
            case R.id.delete_image:
                if (mCommentClickListener != null) mCommentClickListener.deleteComment(comment);
                break;
        }
    }

    class ViewHolder {
        ImageView pinglun_image; //头像
        ImageView clickIngv; //点赞图标
        ImageView deleteIngv; //删除图标
        TextView titleTxtv;    //分类名称
        TextView contentTxtv;    //内容
        TextView dataTxtv;    //日期
        TextView idsTxtv;    //阅读数
        TextView clicksumsTxtv;//点击数
    }

    public interface CommentClickListener {
        public void praiseComment(ComComment comment);//点赞

        public void deleteComment(ComComment comment);//删除评论
    }
}
