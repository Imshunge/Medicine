package com.shssjk.activity.common.shop;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.adapter.SPOrderCouponAdapter;
import com.shssjk.global.MobileApplication;
import com.shssjk.model.shop.SPCoupon;

import java.util.List;

/**
 * 优惠券
 */
public class AvailableCouponActivity extends BaseActivity implements View.OnClickListener{

    private  String TAG = "AvailableCouponActivity";

    //列表
    ListView couponListv;
    //使用按钮
    Button useBtn;
    //选择
    Button checkBtn;

    public static final int MSG_CODE_CHECK_CLICK = 1;
    Handler mHandler ;
    SPOrderCouponAdapter mAdapter;
    List<SPCoupon> coupons;
    SPCoupon selectCoupon;

    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true,true,getString(R.string.title_coupon));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spavailable_coupon);
        super.init();
    }

    @Override
    public void initSubViews() {
//        标题内容
        titlbarFl= (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this,R.color.white));

        couponListv= (ListView) findViewById(R.id.order_coupon_listv);
        useBtn= (Button) findViewById(R.id.coupon_use_btn);
        checkBtn= (Button) findViewById(R.id.coupon_check_btn);
//        couponEtxtv= (EditText) findViewById(R.id.coupon_edtv);

    }

    @Override
    public void initData() {
        coupons = (List<SPCoupon>) MobileApplication.getInstance().list;
        if (getIntent()!=null){
            selectCoupon = (SPCoupon)getIntent().getSerializableExtra("coupon");
        }
        if (selectCoupon!=null && selectCoupon.getCouponType() == 2){
            checkBtn.setBackgroundResource(R.drawable.icon_checked);
        }else{
            checkBtn.setBackgroundResource(R.drawable.icon_checkno);
        }
        mAdapter = new SPOrderCouponAdapter(this , coupons , selectCoupon);
        couponListv.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        couponListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkBtn.setBackgroundResource(R.drawable.icon_checkno);
                selectCoupon = coupons.get(position);
                selectCoupon.setCouponType(1);
                if (mAdapter!= null)mAdapter.setSelectCoupon(selectCoupon);
            }
        });
        useBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.coupon_use_btn:
                if (selectCoupon == null){
                    showToast("请选择优惠券");
                    return;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectCoupon" , selectCoupon);
                setResult(2, resultIntent);
                this.finish();
                break;
        }
//        if (v.getId() == R.id.coupon_check_btn){
//            //优惠码
//            selectCoupon = new SPCoupon();
//            selectCoupon.setCouponType(2);
//            checkBtn.setBackgroundResource(R.drawable.icon_checked);
//            if (mAdapter!= null)mAdapter.setSelectCoupon(selectCoupon);
//        }else if(v.getId() == R.id.coupon_use_btn){
//            if (selectCoupon == null){
//                showToast("请选择优惠券");
//                return;
//            }
//
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("selectCoupon" , selectCoupon);
//            setResult(2, resultIntent);
//            this.finish();
//        }
    }
}
