package com.shssjk.model.info;

import com.shssjk.model.Model;

/**
 * Auto-generated: 2016-10-14 12:59:28
     资讯分类
 */
public class Information implements Model {

    private String cat_alias;
    private String cat_desc;
    private String cat_id;
    private String cat_name;
    private String cat_type;
    private String icon;
    private String keywords;
    private String parent_id;
    private String show_in_nav;
    private String sort_order;
    public void setCatAlias(String cat_alias) {
         this.cat_alias = cat_alias;
     }
     public String getCatAlias() {
         return cat_alias;
     }

    public void setCatDesc(String cat_desc) {
         this.cat_desc = cat_desc;
     }
     public String getCatDesc() {
         return cat_desc;
     }

    public void setCatId(String cat_id) {
         this.cat_id = cat_id;
     }
     public String getCatId() {
         return cat_id;
     }

    public void setCatName(String cat_name) {
         this.cat_name = cat_name;
     }
     public String getCatName() {
         return cat_name;
     }

    public void setCatType(String cat_type) {
         this.cat_type = cat_type;
     }
     public String getCatType() {
         return cat_type;
     }

    public void setIcon(String icon) {
         this.icon = icon;
     }
     public String getIcon() {
         return icon;
     }

    public void setKeywords(String keywords) {
         this.keywords = keywords;
     }
     public String getKeywords() {
         return keywords;
     }

    public void setParentId(String parent_id) {
         this.parent_id = parent_id;
     }
     public String getParentId() {
         return parent_id;
     }

    public void setShowInNav(String show_in_nav) {
         this.show_in_nav = show_in_nav;
     }
     public String getShowInNav() {
         return show_in_nav;
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