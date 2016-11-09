package com.shssjk.model.info;

import com.shssjk.model.Model;

import java.io.Serializable;

/**
 * Auto-generated: 2016-10-15 9:25:55
 *资讯分类
 *
 */
public class Article implements Model, Serializable {

    private String add_time;
    private String article_id;
    private String article_type;
    private String author;
    private String author_email;
    private String cat_id;
    private String click;
    private String content;
    private String file_url;
    private String is_open;
    private String keywords;
    private String link;
    private String open_type;
    private String publish_time;
    private String thumb;
    private String title;
    private String type;

    public String getCcount() {
        return ccount;
    }

    public void setCcount(String ccount) {
        this.ccount = ccount;
    }

    private String ccount;
    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    private String is_collect;  // 1已收藏 0未收藏

    public void setAddTime(String add_time) {
         this.add_time = add_time;
     }
     public String getAddTime() {
         return add_time;
     }

    public void setArticleId(String article_id) {
         this.article_id = article_id;
     }
     public String getArticleId() {
         return article_id;
     }

    public void setArticleType(String article_type) {
         this.article_type = article_type;
     }
     public String getArticleType() {
         return article_type;
     }

    public void setAuthor(String author) {
         this.author = author;
     }
     public String getAuthor() {
         return author;
     }

    public void setAuthorEmail(String author_email) {
         this.author_email = author_email;
     }
     public String getAuthorEmail() {
         return author_email;
     }

    public void setCatId(String cat_id) {
         this.cat_id = cat_id;
     }
     public String getCatId() {
         return cat_id;
     }

    public void setClick(String click) {
         this.click = click;
     }
     public String getClick() {
         return click;
     }

    public void setContent(String content) {
         this.content = content;
     }
     public String getContent() {
         return content;
     }

    public void setFileUrl(String file_url) {
         this.file_url = file_url;
     }
     public String getFileUrl() {
         return file_url;
     }

    public void setIsOpen(String is_open) {
         this.is_open = is_open;
     }
     public String getIsOpen() {
         return is_open;
     }

    public void setKeywords(String keywords) {
         this.keywords = keywords;
     }
     public String getKeywords() {
         return keywords;
     }

    public void setLink(String link) {
         this.link = link;
     }
     public String getLink() {
         return link;
     }

    public void setOpenType(String open_type) {
         this.open_type = open_type;
     }
     public String getOpenType() {
         return open_type;
     }

    public void setPublishTime(String publish_time) {
         this.publish_time = publish_time;
     }
     public String getPublishTime() {
         return publish_time;
     }

    public void setThumb(String thumb) {
         this.thumb = thumb;
     }
     public String getThumb() {
         return thumb;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}