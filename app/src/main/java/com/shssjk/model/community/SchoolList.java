package com.shssjk.model.community;
import com.shssjk.model.Model;


import java.io.Serializable;

/**
 * Auto-generated: 2016-10-18 11:40:44
 * 江湖 分类 门派列表
 */
public class SchoolList implements Model, Serializable {

    private String uid;
    private String id;
    private String logo;
    private String title;
    private String articles;
    private String actives;
    private String status;
    private String remark;
    private String create_time;
    private String max_persons;
    private String persons;
    private String cid;

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

    public void setLogo(String logo) {
         this.logo = logo;
     }
     public String getLogo() {
         return logo;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setArticles(String articles) {
         this.articles = articles;
     }
     public String getArticles() {
         return articles;
     }

    public void setActives(String actives) {
         this.actives = actives;
     }
     public String getActives() {
         return actives;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

    public void setCreateTime(String create_time) {
         this.create_time = create_time;
     }
     public String getCreateTime() {
         return create_time;
     }

    public void setMaxPersons(String max_persons) {
         this.max_persons = max_persons;
     }
     public String getMaxPersons() {
         return max_persons;
     }

    public void setPersons(String persons) {
         this.persons = persons;
     }
     public String getPersons() {
         return persons;
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