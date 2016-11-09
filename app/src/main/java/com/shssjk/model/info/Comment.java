package com.shssjk.model.info;

import com.shssjk.model.Model;

import java.io.Serializable;

/**
 * Auto-generated: 2016-10-17 15:20:53
 * 评论列表
 */
public class Comment implements Model, Serializable {

    private String add_time;
    private String click;
    private String comment_id;
    private String content;
    private int cstate;
    private String goods_id;
    private String parent_id;
    private String status;
    private String user_id;
    private String username;

    public String getHeader_pic() {
        return header_pic;
    }

    public void setHeader_pic(String header_pic) {
        this.header_pic = header_pic;
    }

    private String header_pic;


    public void setAddTime(String add_time) {
         this.add_time = add_time;
     }
     public String getAddTime() {
         return add_time;
     }

    public void setClick(String click) {
         this.click = click;
     }
     public String getClick() {
         return click;
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

    public void setCstate(int cstate) {
         this.cstate = cstate;
     }
     public int getCstate() {
         return cstate;
     }

    public void setGoodsId(String goods_id) {
         this.goods_id = goods_id;
     }
     public String getGoodsId() {
         return goods_id;
     }

    public void setParentId(String parent_id) {
         this.parent_id = parent_id;
     }
     public String getParentId() {
         return parent_id;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
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