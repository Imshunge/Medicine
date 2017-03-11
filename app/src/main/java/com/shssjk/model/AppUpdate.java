package com.shssjk.model;

/**
 * 更新APP
 * Auto-generated: 2016-12-30 17:1:17
 */
public class AppUpdate implements Model {
    private String createDate;
    private String path;
    private String id;

    private String type;
    private String log;
    private String version;
    private String name;
    public void setPath(String path) {
         this.path = path;
     }
     public String getPath() {
         return path;
     }

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setCreateDate(String createDate) {
         this.createDate = createDate;
     }
     public String getCreateDate() {
         return createDate;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setLog(String log) {
         this.log = log;
     }
     public String getLog() {
         return log;
     }

    public void setVersion(String version) {
         this.version = version;
     }
     public String getVersion() {
         return version;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{"createDate","create_date"};

    }
}