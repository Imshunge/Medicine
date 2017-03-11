package com.shssjk.model.shop;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016-12-19.
 */
public class CommentCondition {
    public List<File> getImages() {
        return images;
    }

    public void setImages(List<File> images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getService_rank() {
        return service_rank;
    }

    public void setService_rank(String service_rank) {
        this.service_rank = service_rank;
    }

    public String getGoods_rank() {
        return goods_rank;
    }

    public void setGoods_rank(String goods_rank) {
        this.goods_rank = goods_rank;
    }

    public String getDeliver_rank() {
        return deliver_rank;
    }

    public void setDeliver_rank(String deliver_rank) {
        this.deliver_rank = deliver_rank;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    String   order_id;
    String goods_id;
    String goods_rank;
    String      deliver_rank;
    String   service_rank;
    String      content;   //reasom
    List<File> images;

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String order_sn;
    String type;

}
