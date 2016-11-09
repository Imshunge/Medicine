package com.shssjk.model.shop;
import com.shssjk.model.Model;

import java.io.Serializable;
import java.util.List;

/**

 */
public class Data   implements Model, Serializable {

    private String cat_group;
    private String commission_rate;
    private String id;
    private String image;
    private String is_hot;
    private String is_show;
    private String level;
    private String mobile_name;
    private String name;
    private String parentId;
    private String mobile_name_path;
    private String sort_order;
    private List<Tmenu> tmenu;
    public void setCatGroup(String cat_group) {
         this.cat_group = cat_group;
     }
     public String getCatGroup() {
         return cat_group;
     }

    public void setCommissionRate(String commission_rate) {
         this.commission_rate = commission_rate;
     }
     public String getCommissionRate() {
         return commission_rate;
     }

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setImage(String image) {
         this.image = image;
     }
     public String getImage() {
         return image;
     }

    public void setIsHot(String is_hot) {
         this.is_hot = is_hot;
     }
     public String getIsHot() {
         return is_hot;
     }

    public void setIsShow(String is_show) {
         this.is_show = is_show;
     }
     public String getIsShow() {
         return is_show;
     }

    public void setLevel(String level) {
         this.level = level;
     }
     public String getLevel() {
         return level;
     }

    public void setMobileName(String mobile_name) {
         this.mobile_name = mobile_name;
     }
     public String getMobileName() {
         return mobile_name;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setParentId(String parentId) {
         this.parentId = parentId;
     }
     public String getParentId() {
         return parentId;
     }

    public void setParentIdPath(String mobile_name_path) {
         this.mobile_name_path = mobile_name_path;
     }
     public String getParentIdPath() {
         return mobile_name_path;
     }

    public void setSortOrder(String sort_order) {
         this.sort_order = sort_order;
     }
     public String getSortOrder() {
         return sort_order;
     }

    public void setTmenu(List<Tmenu> tmenu) {
         this.tmenu = tmenu;
     }
     public List<Tmenu> getTmenu() {
         return tmenu;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}