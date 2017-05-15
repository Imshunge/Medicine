package com.shssjk.model.health;
import com.shssjk.model.Model;
/**
 * Auto-generated: 2017-04-10 15:5:17
  */
public class StepHistory implements Model{

    private String date;
    private String id;
    private String step;
    private String userId;
    public void setDate(String date) {
         this.date = date;
     }
     public String getDate() {
         return date;
     }

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setStep(String step) {
         this.step = step;
     }
     public String getStep() {
         return step;
     }

    public void setUserId(String userId) {
         this.userId = userId;
     }
     public String getUserId() {
         return userId;
     }


    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "userId","user_id",
                   };
    }



}