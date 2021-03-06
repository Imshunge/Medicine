package com.shssjk.model.shop;


import com.shssjk.model.Model;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by admin on 2016/6/20.
 */
public class GoodsComment implements Model {

    // comment_id - 评论ID
    String commentID;

    // goods_id - 商品ID
    String goodsID;

    // username - 用户名称
    String username;

    // content - 内容
    String content;

    // add_time - 评论名称
    String addTime;

    // goods_rank - 评级
    String goodsRank;

    // service_rank - 评级
    String serviceRank;

    // deliver_rank - 评级
    String deliverRank;

    // goods_rank - 评论图片
    List<String> images;
    JSONArray imageArray;

    // head_url - 用户头像
    String headUrl;

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    String head_pic ;
    
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
            "commentID","comment_id",
            "goodsID","goods_id",
            "addTime","add_time",
            "goodsRank","goods_rank",
            "serviceRank","service_rank",
            "deliverRank","deliver_rank",
            "imageArray","img"
        };
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getGoodsRank() {
        return goodsRank;
    }

    public void setGoodsRank(String goodsRank) {
        this.goodsRank = goodsRank;
    }

    public String getServiceRank() {
        return serviceRank;
    }

    public void setServiceRank(String serviceRank) {
        this.serviceRank = serviceRank;
    }

    public String getDeliverRank() {
        return deliverRank;
    }

    public void setDeliverRank(String deliverRank) {
        this.deliverRank = deliverRank;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public JSONArray getImageArray() {
        return imageArray;
    }

    public void setImageArray(JSONArray imageArray) {
        this.imageArray = imageArray;
    }
}
