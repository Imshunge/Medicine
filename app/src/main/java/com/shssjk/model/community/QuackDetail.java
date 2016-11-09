package com.shssjk.model.community;

import com.shssjk.model.Model;

import java.io.Serializable;

/**
 * Auto-generated: 2016-10-18 17:28:32
 *
 
 */
public class QuackDetail   implements Model, Serializable {

    private String id;
    private String cid;
    private String uid;
    private String title;
    private String logo;
    private String remark;
    private String actives;
    private String articles;
    private String status;
    private String persons;
    private String max_persons;
    private String create_time;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;

    public String getHeader_pic() {
        return header_pic;
    }

    public void setHeader_pic(String header_pic) {
        this.header_pic = header_pic;
    }

    private String header_pic;
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String nickname;

    public String getIs_in() {
        return is_in;
    }

    public void setIs_in(String is_in) {
        this.is_in = is_in;
    }

    private String is_in;

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setCid(String cid) {
         this.cid = cid;
     }
     public String getCid() {
         return cid;
     }

    public void setUid(String uid) {
         this.uid = uid;
     }
     public String getUid() {
         return uid;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setLogo(String logo) {
         this.logo = logo;
     }
     public String getLogo() {
         return logo;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

    public void setActives(String actives) {
         this.actives = actives;
     }
     public String getActives() {
         return actives;
     }

    public void setArticles(String articles) {
         this.articles = articles;
     }
     public String getArticles() {
         return articles;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setPersons(String persons) {
         this.persons = persons;
     }
     public String getPersons() {
         return persons;
     }

    public void setMaxPersons(String max_persons) {
         this.max_persons = max_persons;
     }
     public String getMaxPersons() {
         return max_persons;
     }

    public void setCreateTime(String create_time) {
         this.create_time = create_time;
     }
     public String getCreateTime() {
         return create_time;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}