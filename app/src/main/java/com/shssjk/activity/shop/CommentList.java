package com.shssjk.activity.shop;

import com.shssjk.model.Model;

import java.io.Serializable;

/**
 * Auto-generated: 2016-12-20 9:0:57
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class CommentList implements Model ,Serializable {

    private String add_time;
    private String comment_id;
    private String content;
    private String deliver_rank;
    private String email;
    private String goods_id;
    private String goods_rank;
    private String img;
    private String ip_address;
    private String is_show;
    private String order_id;
    private String parent_id;
    private String service_rank;
    private String user_id;
    private String username;
    public void setAddTime(String add_time) {
         this.add_time = add_time;
     }
     public String getAddTime() {
         return add_time;
     }

    public void setCommentId(String comment_id) {
         this.comment_id = comment_id;
     }
     public String getCommentId() {
         return comment_id;
     }

    public void setContent(String content) {
         this.content = content;
     }
     public String getContent() {
         return content;
     }

    public void setDeliverRank(String deliver_rank) {
         this.deliver_rank = deliver_rank;
     }
     public String getDeliverRank() {
         return deliver_rank;
     }

    public void setEmail(String email) {
         this.email = email;
     }
     public String getEmail() {
         return email;
     }

    public void setGoodsId(String goods_id) {
         this.goods_id = goods_id;
     }
     public String getGoodsId() {
         return goods_id;
     }

    public void setGoodsRank(String goods_rank) {
         this.goods_rank = goods_rank;
     }
     public String getGoodsRank() {
         return goods_rank;
     }

    public void setImg(String img) {
         this.img = img;
     }
     public String getImg() {
         return img;
     }

    public void setIpAddress(String ip_address) {
         this.ip_address = ip_address;
     }
     public String getIpAddress() {
         return ip_address;
     }

    public void setIsShow(String is_show) {
         this.is_show = is_show;
     }
     public String getIsShow() {
         return is_show;
     }

    public void setOrderId(String order_id) {
         this.order_id = order_id;
     }
     public String getOrderId() {
         return order_id;
     }

    public void setParentId(String parent_id) {
         this.parent_id = parent_id;
     }
     public String getParentId() {
         return parent_id;
     }

    public void setServiceRank(String service_rank) {
         this.service_rank = service_rank;
     }
     public String getServiceRank() {
         return service_rank;
     }

    public void setUserId(String user_id) {
         this.user_id = user_id;
     }
     public String getUserId() {
         return user_id;
     }

    public void setUsername(String username) {
         this.username = username;
     }
     public String getUsername() {
         return username;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}