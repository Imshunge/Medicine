package com.shssjk.model.shop;

import com.shssjk.model.Model;

import java.io.Serializable;

/**

 */
public class Tmenu   implements Model, Serializable {

    private String cat_group;
    private String commission_rate;
    private String id;
    private String image;
    private String is_hot;
    private String is_show;
    private String level;
    private String mobile_name;
    private String name;
    private String parent_id;
    private String parent_id_path;
    private String sort_order;
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

    public void setParentId(String parent_id) {
         this.parent_id = parent_id;
     }
     public String getParentId() {
         return parent_id;
     }

    public void setParentIdPath(String parent_id_path) {
         this.parent_id_path = parent_id_path;
     }
     public String getParentIdPath() {
         return parent_id_path;
     }

    public void setSortOrder(String sort_order) {
         this.sort_order = sort_order;
     }
     public String getSortOrder() {
         return sort_order;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}