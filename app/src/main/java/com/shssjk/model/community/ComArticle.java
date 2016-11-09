package com.shssjk.model.community;

import com.shssjk.model.Model;

import java.io.Serializable;

/**
 * Auto-generated: 2016-10-18 14:20:14
 *
    江湖 帖子列表
 */
public class ComArticle  implements Model, Serializable {

    private String uid;
    private String id;
    private String title;
    private String status;
    private String description;
    private String create_time;
    private String img;
    private String view;
    private String comment;
    private String cid;
    private String is_collect;


    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    private String cuid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String nickname;

    public String getHeader_pic() {
        return header_pic;
    }

    public void setHeader_pic(String header_pic) {
        this.header_pic = header_pic;
    }

    private String header_pic;
//    is_collect 是否收藏字段，1已收藏、0 未收藏
    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
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

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setDescription(String description) {
         this.description = description;
     }
     public String getDescription() {
         return description;
     }

    public void setCreateTime(String create_time) {
         this.create_time = create_time;
     }
     public String getCreateTime() {
         return create_time;
     }

    public void setImg(String img) {
         this.img = img;
     }
     public String getImg() {
         return img;
     }

    public void setView(String view) {
         this.view = view;
     }
     public String getView() {
         return view;
     }

    public void setComment(String comment) {
         this.comment = comment;
     }
     public String getComment() {
         return comment;
     }

    public void setCid(String cid) {
         this.cid = cid;
     }
     public String getCid() {
         return cid;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}