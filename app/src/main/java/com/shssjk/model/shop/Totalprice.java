package com.shssjk.model.shop;
import com.shssjk.model.Model;


/**
 * Auto-generated: 2017-01-13 15:26:38
 * 订单总价
  */
public class Totalprice implements Model{

    private double cutFee;
    private int num;
    private double totalFee;
    public void setCutFee(double cutFee) {
         this.cutFee = cutFee;
     }
     public double getCutFee() {
         return cutFee;
     }

    public void setNum(int num) {
         this.num = num;
     }
     public int getNum() {
         return num;
     }

    public void setTotalFee(double totalFee) {
         this.totalFee = totalFee;
     }
     public double getTotalFee() {
         return totalFee;
     }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{"cutFee","cut_fee","totalFee","total_fee"};
    }
}