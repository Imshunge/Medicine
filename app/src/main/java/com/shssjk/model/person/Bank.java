package com.shssjk.model.person;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-02-20.
 */
public class Bank implements Serializable{
    private String id;

    private String uid;

    private String bank;

    private String name;

    private String code;

    private String status;

    private String is_default;

    private String create_time;

    public String getBank_url() {
        return bank_url;
    }

    public void setBank_url(String bank_url) {
        this.bank_url = bank_url;
    }

    private String bank_url;


    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setUid(String uid){
        this.uid = uid;
    }
    public String getUid(){
        return this.uid;
    }
    public void setBank(String bank){
        this.bank = bank;
    }
    public String getBank(){
        return this.bank;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setIs_default(String is_default){
        this.is_default = is_default;
    }
    public String getIs_default(){
        return this.is_default;
    }
    public void setCreate_time(String create_time){
        this.create_time = create_time;
    }
    public String getCreate_time(){
        return this.create_time;
    }
    @Override
    public String toString(){
        return bank+" "+code;
    }
}