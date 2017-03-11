package com.shssjk.model.person;
/**
 * Auto-generated: 2017-02-17 10:23:16
 * 摄像头
 */
public class Camera {
    private String id;
    private String uid;
    private String name;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    private String did;
    private String type;
    private String state;
    private String create_time;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setUid(String uid) {
         this.uid = uid;
     }
     public String getUid() {
         return uid;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setDid(String did) {
         this.did = did;
     }
     public String getDid() {
         return did;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setState(String state) {
         this.state = state;
     }
     public String getState() {
         return state;
     }

    public void setCreateTime(String create_time) {
         this.create_time = create_time;
     }
     public String getCreateTime() {
         return create_time;
     }

}