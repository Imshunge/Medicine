package com.shssjk.model.info;
import com.shssjk.model.Model;

import java.util.List;

/**
 * Auto-generated: 2016-10-14 12:59:28
 *
  */
public class InformationBean  implements Model {

    private List<Information> data;
    private String msg;
    private int status;
    public void setInformation(List<Information> data) {
         this.data = data;
     }
     public List<Information> getInformation() {
         return data;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}