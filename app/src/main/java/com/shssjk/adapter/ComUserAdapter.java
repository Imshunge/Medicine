
package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.model.community.ComComment;
import com.shssjk.model.community.ComUser;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

import static com.shssjk.activity.R.*;

/**
 * @author
 * 江湖用户列表
 */
public class ComUserAdapter extends BaseAdapter implements View.OnClickListener {

    private String TAG = "ComUserAdapter";
    private ImageBtnClickListener mImageBtnClickListener;
    private ThroughBtnClickListener mThroughBtnClickListener;
    private RefuseBtnClickListener mRefuseBtnClickListener;
    private List<ComUser> mArticle;
    private Context mContext;
    private String isHost;//是否是门主  0 不是  1 是    2 审核列表

    public ComUserAdapter(Context context,String isHost, ImageBtnClickListener imageBtnClickListener) {
        this.mContext = context;
        this.isHost=isHost;
        this.mImageBtnClickListener = imageBtnClickListener;
    }
    public ComUserAdapter(Context context,String isHost, ThroughBtnClickListener throughBtnClickListener,RefuseBtnClickListener refuseBtnClickListener ) {
        this.mContext = context;
        this.isHost=isHost;
        this.mThroughBtnClickListener = throughBtnClickListener;
        this.mRefuseBtnClickListener = refuseBtnClickListener;
    }
    public void setData(List<ComUser> articles) {
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
        return Long.valueOf(mArticle.get(position).getUid());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //category_left_item.xml
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.jh_user_item, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder();
            holder.picIngv = ((ImageView) convertView.findViewById(id.img_essayuser));
            holder.delOrAddIngv = ((ImageView) convertView.findViewById(id.delete_image));
            holder.nameTxtv = ((TextView) convertView.findViewById(id.jc_nickname));
            holder.sexTxtv = ((TextView) convertView.findViewById(id.jc_sex));
            holder.sinTxtv = ((TextView) convertView.findViewById(id.jc_sin));
            holder.idTxtv = ((TextView) convertView.findViewById(id.jc_id));
            holder.ll_check= (LinearLayout) convertView.findViewById(id.ll_check);
//            TextView throughTxtv;    //通过
//            TextView refuseTxtv;//拒绝
            holder.throughTxtv=((TextView) convertView.findViewById(id.throughTxtv));
            holder.refuseTxtv=((TextView) convertView.findViewById(id.refuseTxtv));




            //设置标记
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
        ComUser article = (ComUser) mArticle.get(position);
        String imgUrl1 = MobileConstants.BASE_HOST + article.getHeader_pic();
        Glide.with(mContext).load(imgUrl1).placeholder(drawable.icon_header).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picIngv);

        if (!SPStringUtils.isEmpty(article.getNickname())) {
            holder.nameTxtv.setText(article.getNickname());
        }
        if (!SPStringUtils.isEmpty(article.getUid())) {
            holder.idTxtv.setText("ID:"+article.getUid());
        }
//性别（0 保密 1 男 2 女）
           String str="";
        if (!SPStringUtils.isEmpty(article.getSex())) {
            if(article.getSex().equals("1")){
                str="男";
            }else if(article.getSex().equals("1")){
                str="女";
            }else{
                str="保密";
            }
            holder.sexTxtv.setText(str);
        }
//		签名
		if (!SPStringUtils.isEmpty(article.getSign())){
			holder.sinTxtv.setText(article.getSign());
		}

//        删除按钮
        if(isHost.equals("0")){
            holder.delOrAddIngv.setVisibility(View.INVISIBLE);
        }else  if(isHost.equals("1")){
            holder.delOrAddIngv.setVisibility(View.VISIBLE);
        }else if(isHost.equals("2")){
            holder.ll_check.setVisibility(View.VISIBLE);
        }
        holder.throughTxtv.setTag(position);
        holder.refuseTxtv.setTag(position);
        holder.delOrAddIngv.setTag(position);
        holder.delOrAddIngv.setOnClickListener(this);
        holder.throughTxtv.setOnClickListener(this);
        holder.refuseTxtv.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = 0;
        ComUser comUser = null ;
        if (v.getTag()!=null && v.getTag().toString() != null){
            position = Integer.valueOf(v.getTag().toString());
        }
        if (mArticle!= null && position < mArticle.size() && position >= 0){
            comUser = mArticle.get(position);
        }
        switch (v.getId()){
            case R.id.delete_image:
                if (mImageBtnClickListener!=null)mImageBtnClickListener.deleteOrAddBtnClick(comUser);
                break;
            case R.id.refuseTxtv:
                if (mRefuseBtnClickListener!=null)mRefuseBtnClickListener.refuseBtnBtnClick(comUser);
                break;
            case R.id.throughTxtv:
                if (mThroughBtnClickListener!=null)mThroughBtnClickListener.throughClick(comUser);
                break;
        }
    }


    class ViewHolder {
        ImageView picIngv;//头像
        ImageView delOrAddIngv;//删除或 同意
        TextView nameTxtv;    //昵称
        TextView idTxtv;    //时间
        TextView sexTxtv;    //描述
        TextView sinTxtv;//签名
        TextView throughTxtv;    //通过
        TextView refuseTxtv;//拒绝

        LinearLayout ll_check;
    }
    public interface ImageBtnClickListener{
        public void deleteOrAddBtnClick(ComUser comUser);//删除 或同意加入门派
    }
    public interface ThroughBtnClickListener{
        public void throughClick(ComUser comUser);//同意加入门派
    }
    public interface RefuseBtnClickListener{
        public void refuseBtnBtnClick(ComUser comUser);//拒绝加入门派
    }


}
