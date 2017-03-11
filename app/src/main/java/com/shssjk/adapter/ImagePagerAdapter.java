/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.model.SPHomeBanners;

import java.util.List;


/**
 * ImagePagerAdapter
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {
    private LayoutInflater inflater;

    private Context context;
    private List<Integer> imageIdList;
    private List<SPHomeBanners> spHomeBannersList;
    private int size;
    private boolean isInfiniteLoop;
    private OnBinnerOnClickListener onBinnerOnClickListener;

    public ImagePagerAdapter(Context context, List<Integer> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
        this.size = imageIdList.size();
        isInfiniteLoop = false;
    }

    public ImagePagerAdapter(Context mContext, List<SPHomeBanners> spHomeBannersList, OnBinnerOnClickListener onBinnerOnClickListener) {
        this.context = mContext;
        this.spHomeBannersList = spHomeBannersList;
        this.size = spHomeBannersList.size();
        isInfiniteLoop = false;
        this.onBinnerOnClickListener = onBinnerOnClickListener;
        inflater = LayoutInflater.from(context);
    }

    public ImagePagerAdapter(Context mContext, OnBinnerOnClickListener onBinnerOnClickListener) {
        this.context = mContext;
        isInfiniteLoop = false;
        this.onBinnerOnClickListener = onBinnerOnClickListener;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<SPHomeBanners> spHomeBannersList) {
        if (spHomeBannersList == null) return;
        this.size = spHomeBannersList.size();
        this.spHomeBannersList = spHomeBannersList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
    }
    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.binner_item, null);
            holder.imageView = (ImageView) view.findViewById(R.id.iv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SPHomeBanners article = (SPHomeBanners) spHomeBannersList.get(getPosition(position));
//        holder.imageView.setImageResource(imageIdList.get(getPosition(position)));
//        Glide.with(mContext).load(String.format(banner.getAdCode())).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
        Glide.with(context).load(article.getAdCode()).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBinnerOnClickListener != null)
                    onBinnerOnClickListener.onBinnerClick(getPosition(position));
            }
        });
        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    public interface OnBinnerOnClickListener {
        void onBinnerClick(int index);
    }
}

