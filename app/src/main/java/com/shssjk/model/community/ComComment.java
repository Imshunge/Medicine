package com.shssjk.model.community;

import com.shssjk.model.Model;

import java.io.Serializable;

/**
 * Auto-generated: 2016-10-19 11:37:0
 *
 */
public class ComComment implements Model, Serializable {

    private String content;
    private String uid;
    private String id;
    private String imgs;
    private String articleId;
    private String laud;
    private String status;
    private String create_time;
    private int cstate;
    private String mobile;
    private String parent_id;

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    private String cuid;


    public String getHeader_pic() {
        return header_pic;
    }

    public void setHeader_pic(String header_pic) {
        this.header_pic = header_pic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String nickname;
    private String header_pic;
    public void setContent(String content) {
         this.content = content;
     }
     public String getContent() {
         return content;
     }

    public void setUid(String uid) {
         this.uid = uid;
     }
     public String getUid() {
         return uid;
     }

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setImgs(String imgs) {
         this.imgs = imgs;
     }
     public String getImgs() {
         return imgs;
     }

    public void setArticleId(String articleId) {
         this.articleId = articleId;
     }
     public String getArticleId() {
         return articleId;
     }

    public void setLaud(String laud) {
         this.laud = laud;
     }
     public String getLaud() {
         return laud;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setCreateTime(String create_time) {
         this.create_time = create_time;
     }
     public String getCreateTime() {
         return create_time;
     }

    public void setCstate(int cstate) {
         this.cstate = cstate;
     }
     public int getCstate() {
         return cstate;
     }

    public void setMobile(String mobile) {
         this.mobile = mobile;
     }
     public String getMobile() {
         return mobile;
     }

    public void setParentId(String parent_id) {
         this.parent_id = parent_id;
     }
     public String getParentId() {
         return parent_id;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}