package com.shssjk.model.shop;

import com.shssjk.model.Model;

import java.io.Serializable;

/**

 */
public class Ad  implements Model, Serializable {

    private String ad_link;
    private String adName;
    private String ad_code;
    public void setad_link(String ad_link) {
         this.ad_link = ad_link;
     }
     public String getad_link() {
         return ad_link;
     }

    public void setAdName(String adName) {
         this.adName = adName;
     }
     public String getAdName() {
         return adName;
     }

    public void setad_code(String ad_code) {
         this.ad_code = ad_code;
     }
     public String getad_code() {
         return ad_code;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}