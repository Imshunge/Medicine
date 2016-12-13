package com.shssjk.activity.common.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.dao.SPPersonDao;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPShopCartManager;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;
import com.shssjk.model.order.PayOrder;
import com.shssjk.model.order.SPOrder;
import com.shssjk.model.person.SPConsigneeAddress;
import com.shssjk.model.shop.SPCoupon;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.SPArrowRowView;
import com.shssjk.view.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 立即购买界面
 */
public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "ConfirmOrderActivity";

    TextView consigneeTxtv;

    TextView addressTxtv;


    LinearLayout mGallery;

    TextView productCountTxtv;

    SPArrowRowView deliverAview;

    SPArrowRowView couponAview;

//    SPArrowRowView invoceAview;

//    商品总额
    TextView feeGoodsFeeTxtv;

    TextView feeShoppingTxtv;
//    优惠券
    TextView feeCouponTxtv;
//    石头
    TextView feePointTxtv;

    TextView feeBalanceTxtv;

    TextView feeAmountTxtv;

//    支付总额
    TextView payfeeTxtv;

    SwitchButton bananceSth;

    TextView balanceTxtv;

    SwitchButton pointSth;

    EditText stoneEit; // 使用 石头数目

    TextView pointTxtv;
//    下单时间
    TextView buyTimeTxtv;

    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮

    Button payBtn;       //结算按钮

    TextView titleTxtv; //标题
    TextView nowUseStone;//现在使用石头
    private Context mContext;
    /********* 服务器拉取的数据源集合 *****************/
    SPConsigneeAddress consigneeAddress ;   //当前收货人信息
    List<SPProduct> products;               //商品列表
    List<SPCoupon> coupons;                  //优惠券列表
    JSONObject userinfoJson;                //用户信息(积分,余额)
    JSONObject amountDict;                   //结算金额汇总

    /********* 选中的数据 *****************/
    JSONObject selectedDeliverJson;         //当前选中的物流
    SPCoupon selectedCoupon;                //当前选中的优惠券
    int points=0;                             //使用的石头
    FrameLayout  addressFl;                 //收货地址列表
    private TextView stonesumTxtv;
    private JSONArray formDataArray;
    private Double mEarning;  //可用石头
    private Double priceSum=0.0;
    private Double mUsingEarning=0.0; // 使用石头
    private Double mCoupon=0.0; // 优惠券
    private Double mFee=0.0; // 运费
    private TextView coupontimeTxtv;
    private TextView couponsumTxtv;
    private TextWatcher watcherStoneEit = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {


        }
        @Override
        public void afterTextChanged(Editable s) {
//            变化 判断数量变化
            mUsingEarning = SSUtils.string2double(stoneEit.getText().toString().trim());
            if(mUsingEarning>mEarning){
                showToast("您没有那么多石头");
                stoneEit.setText(mEarning+"");
//                points= p;
            }  else{
                nowUseStone.setText("¥"+mUsingEarning);
                feePointTxtv.setText("¥" + mUsingEarning);
            }
            mUsingEarning = SSUtils.string2double(stoneEit.getText().toString().trim());
            refreshView();
        }
    };
    private String mSumpricsse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_confirm_order));
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_spconfirm_order);
        super.init();
    }

    @Override
    public void initSubViews() {
        Intent intent = getIntent();
        priceSum = intent.getDoubleExtra("sumFee", 10000.0);
        consigneeTxtv =(TextView) findViewById(R.id.order_consignee_txtv);
        addressTxtv =(TextView) findViewById(R.id.order_address_txtv);
        mGallery =(LinearLayout) findViewById(R.id.product_list_gallery_lyaout);
        productCountTxtv =(TextView) findViewById(R.id.order_product_count_txtv);
        //返回按钮
        backBtn     =(Button) findViewById(R.id.titlebar_back_btn);
        //结算按钮
        payBtn =(Button) findViewById(R.id.pay_btn);
        couponAview =(SPArrowRowView) findViewById(R.id.order_coupon_aview);

        feeGoodsFeeTxtv =(TextView) findViewById(R.id.fee_goodsfee_txtv);

        feeShoppingTxtv =(TextView) findViewById(R.id.fee_shopping_txtv);

        feeCouponTxtv =(TextView) findViewById(R.id.fee_coupon_txtv);

        feePointTxtv =(TextView) findViewById(R.id.fee_point_txtv);

//        feeBalanceTxtv =(TextView) findViewById(R.id.fee_balance_txtv);

        feeAmountTxtv =(TextView) findViewById(R.id.fee_amount_txtv);

        payfeeTxtv =(TextView) findViewById(R.id.payfee_txtv);

        bananceSth =(SwitchButton) findViewById(R.id.order_balance_sth);


        balanceTxtv =(TextView) findViewById(R.id.order_balance_txtv);


//        pointSth =(SwitchButton) findViewById(R.id.order_point_sth);

        stoneEit =(EditText) findViewById(R.id.edit_use_stone_num);
//
//        pointTxtv =(TextView) findViewById(R.id.order_point_txtv);

//        buyTimeTxtv =(TextView) findViewById(R.id.buy_time_txtv);
        addressFl =(FrameLayout) findViewById(R.id.order_confirm_consignee_layout);
        stonesumTxtv=(TextView) findViewById(R.id.stone_sum_txtv);
        nowUseStone=(TextView) findViewById(R.id.stone_sum_use_txtv);

        coupontimeTxtv=(TextView) findViewById(R.id.coupon_time_txtv);
        couponsumTxtv=(TextView) findViewById(R.id.coupon_sum_use_txtv);

    }

    @Override
    public void initData() {
        refreshData();
        ShowFee();
    }

    @Override
    public void initEvent() {

        addressFl.setOnClickListener(this);
        couponAview.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        payBtn.setOnClickListener(this);

        stoneEit.addTextChangedListener(watcherStoneEit);

    }


    public void refreshData(){
//        showLoadingToast();
        ShopRequest.getConfirmOrderData(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                mDataJson = (JSONObject) response;
                try {
                    if (mDataJson != null) {
                        if (mDataJson.has("consigneeAddress")) {
                            if (mDataJson.get("consigneeAddress") != null) {
                                consigneeAddress = (SPConsigneeAddress) mDataJson.get("consigneeAddress");
                            }
                        }
                        if (mDataJson.has("products")) {
                            products = (List<SPProduct>) mDataJson.get("products");
                        }
                        //coupon 网络电子优惠券
                        if (mDataJson.has("coupons")) {
                            coupons = (List<SPCoupon>) mDataJson.get("coupons");
                        }
                        if (mDataJson.has("userInfo")) {
                            userinfoJson = mDataJson.getJSONObject("userInfo");
                        }
                        dealModel();
                        refreshView();
                        //load 商品金额信息
                        loadTotalFee();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
        initEvent();
    }
    /**
     *  处理服务器获取的数据
     */
    public void dealModel(){

        if (products == null)return;

        try {
            //默认支付
            if (consigneeAddress != null ) {
                //NSString* firstAddress = [[SPDataBaseManager shareInstance] queryFirstAddress:self.consigneeAddress.province cityID:self.consigneeAddress.city districtID:self.consigneeAddress.district];
                String firstPart = SPPersonDao.getInstance(this).queryFirstRegion(consigneeAddress.getProvince() , consigneeAddress.getCity() , consigneeAddress.getDistrict() , consigneeAddress.getTown());
                consigneeAddress.setFullAddress(firstPart+consigneeAddress.getAddress());
            }
            //设置商品图片
            buildProductGallery();
            //显示 拥有的石头
            if( userinfoJson.has("do_earnings")){
                String earning = userinfoJson.getString("do_earnings");
                if(!SSUtils.isEmpty(earning)){
                    mEarning = SSUtils.string2double(earning);
                    stonesumTxtv.setText(""+mEarning+"");
                }
            }
            if (products != null || products.size() > 0 ){
                productCountTxtv.setText("共" + products.size() + "件商品");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void buildProductGallery(){
        for (int i = 0; i < products.size(); i++){
            String url = products.get(i).getImageThumlUrl();
            View view = LayoutInflater.from(this).inflate(R.layout.activity_index_gallery_item, mGallery, false);
            ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
            mGallery.addView(view);
        }
    }
    /**
     * 刷新页面数据
     */
    public void refreshView(){
        try {
            consigneeTxtv.setText(consigneeAddress.getConsignee() +"  "+consigneeAddress.getMobile());
            addressTxtv.setText(consigneeAddress.getFullAddress());
            if (products != null || products.size() > 0 ){
                productCountTxtv.setText("共" + products.size() + "件商品");
            }
             //优惠券
            if (selectedCoupon.getCouponType() == 1){
                couponAview.setSubText(selectedCoupon.getName());
                //优惠码
//                优惠券价格大于商品价格 按照商品价格显示
                if(SSUtils.string2double(selectedCoupon.getMoney())>priceSum){
                    mCoupon=priceSum;
                    feeCouponTxtv.setText("¥" + priceSum);
                    couponsumTxtv.setText(priceSum.toString());
                }else {
                    mCoupon=         SSUtils.string2double(selectedCoupon.getMoney());
                    feeCouponTxtv.setText("¥" + selectedCoupon.getMoney());
                    couponsumTxtv.setText(selectedCoupon.getMoney());
                }
                coupontimeTxtv.setText(SSUtils.stampToDate(selectedCoupon.getUseEndTime()));
            }else{
                couponAview.setSubText(selectedCoupon.getCode());
            }

            if(userinfoJson == null)return;
            //石头
            if( userinfoJson.has("do_earnings")){
                String earning = userinfoJson.getString("do_earnings");
                if(!SSUtils.isEmpty(earning)){
                    mEarning = SSUtils.string2double(earning);
                    stonesumTxtv.setText(""+mEarning+"");
                }
            }
//            private Double mEarning;  //可用石头
//            private Double priceSum=0.0;
//            private Double mUsingEarning=0.0; // 使用石头
//            private Double mCoupon=0.0; // 优惠券
//            private Double mFee=0.0; // 运费

//      String sumprice=      calculativeCost(priceSum,mFee,mUsingEarning,mCoupon);
            String sumpricsse=      calculativeCost(priceSum,mFee,mUsingEarning,mCoupon);
            payfeeTxtv.setText(sumpricsse + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        ShowFee();
    }

    /**
     *  更加当前设置信息, 获取商品数据(商品总价, 应付金额)
     */
    public void loadTotalFee(){
        refreshView();
    }
    /**
     *  获取请求参数
     *  @param type 请求类型, 1: 价格变动, 2: 生成订单
     *
     *  @return
     */


    public RequestParams getRequestParameter(int type){

        RequestParams params = new RequestParams();
        int Coupon=0; //优惠券
        int postage=0;//邮费
        try {
            if (type == 1) {
                //价格变动
//                params.put("act" , "order_price");
            }else{
                //提交订单

//                发票 保留数据 目前 传空
                params.put("invoice_title", "111");

            }
//            收货地址id
            if (consigneeAddress != null) {
                params.put("address_id" , consigneeAddress.getAddressID());
            }
            //优惠码
            if (selectedCoupon != null){
                params.put("coupon_id",selectedCoupon.getCouponID());   //

                params.put("coupon_price" ,mCoupon);
            }else{
                params.put("coupon_id","");
                params.put("coupon_price" ,"");
            }
//            private Double priceSum=0.0;
//            private Double mUsingEarning=0.0; // 使用石头
//            private Double mCoupon=0.0; // 优惠券
//            private Double mFee=0.0; // 运费

//            //物流  do_earnings 石头
            //使用石头
//            integral  石头       integral_money 石头     shipping_price运费

            params.put("integral" , mUsingEarning);
            params.put("integral_money" , mUsingEarning);

//            计算总价
//             int sum=   priceSum.intValue()- Coupon-points;
//              if(sum>200) {
//                  postage=10;
//                  //邮费
//              }
            //邮费
            params.put("shipping_price" , mFee);

            //应付款金额  最后（价格 ） order_amout
            params.put("order_amount" ,mSumpricsse);

            //订单总价  邮费+ 商品
            params.put("total_amount" ,mFee +priceSum);

            formDataArray = new JSONArray();
            //添加商品明细
            try {
                for (SPProduct  product : products) {
                    JSONObject formJson = new JSONObject();
                    formJson.put("goods_id", product.getGoodsID());
                    formJson.put("price", product.getMemberGoodsPrice());
                    formJson.put("number", product.getGoodsNum());
                    formDataArray.put(formJson);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (formDataArray!=null && formDataArray.length() > 0) {
                //表单数据: POST提交
                params.put("goods_info" ,formDataArray);
            }

              } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     *  更新底部商品总金额数据
     */
    public void refreshTotalFee(){

        try {
            String payables  = "0.00";
            if(amountDict!=null && amountDict.has("payables")) {
                payables = amountDict.getString("payables");
            }else{
                payables = "0";
            }
            String totalFeeFmt = "应付金额¥"+payables ;
            int startIndex = 4;
            int endIndex = totalFeeFmt.length() ;
            SpannableString totalFeeSpanStr = new SpannableString(totalFeeFmt);
            totalFeeSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
            payfeeTxtv.setText(totalFeeSpanStr);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     *  提交订单
     */
    public void orderCommint(){

        if (consigneeAddress == null) {
            showToast("请选择收货地址!");
            return;
        }

        //提交订单
        RequestParams params = getRequestParameter(2);
        //提交订单
        showLoadingToast("正在提交订单");
        ShopRequest.submitOrder(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                SPShopCartManager.getInstance(ConfirmOrderActivity.this).reloadCart();
                hideLoadingToast();
//                String orderID = (String) response;
                PayOrder order = (PayOrder) response;
                startUpPayBefor(order);
//                startUpPay(orderID);
            }
        }, new SPFailuredListener(ConfirmOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {

                hideLoadingToast();
                showToast(msg);
            }
        });

    }

    private void startUpPayBefor(PayOrder order) {
        Intent payIntent = new Intent(this , BeforPayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order",order);
        payIntent.putExtras(bundle);
        startActivity(payIntent);
    }

    /**
     *  启动支付页面支付
     *
     *  @param orderID order description
     */
    public void startUpPay(String orderID){

        //进入支付页面支付
        SPPersonRequest.getOrderDetail(orderID, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    SPOrder order = (SPOrder) response;
                    gotoPay(order);
                }
            }
        }, new SPFailuredListener(ConfirmOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    /**
     *  进入支付页面支付
     *
     *  @param order order description
     */
    public void gotoPay(SPOrder order){
        //?????????????????????????
        //余额或积分支付完成, 进入订单页, 否则进入支付页
        if (Integer.valueOf(order.getPayStatus())== 1) {
            //NSLog(@" gotoPayWithOrder 支付完成, 进入订单页... ");
        }else{
            Intent payIntent = new Intent(this , PayListActivity.class);
            payIntent.putExtra("order" , order);
            startActivity(payIntent);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            收货地址列表
            case R.id.order_confirm_consignee_layout:
                Intent resultIntent = new Intent(ConfirmOrderActivity.this , ConsigneeAddressListActivity.class);
                resultIntent.putExtra("getAddress" , "1");
                startActivityForResult(resultIntent , MobileConstants.Result_Code_GetAddress);
                break;

            case R.id.order_coupon_aview:
//            跳转    优惠券 列表
                MobileApplication.getInstance().list = coupons;
                Intent couponIntent = new Intent(this , AvailableCouponActivity.class);
                couponIntent.putExtra("coupon",selectedCoupon);
                startActivityForResult(couponIntent, 2);
                break;
            case R.id.pay_btn:
//                支付
                /*Intent completedIntent = new Intent(this, SPPayCompletedActivity_.class);
                completedIntent.putExtra("tradeFee" , "9809");
                completedIntent.putExtra("tradeNo" , "2016630142355");
                startActivity(completedIntent);*/
                orderCommint();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode){
            case 1:
                selectedDeliverJson = MobileApplication.getInstance().json;
                break;
            case 2:
                //优惠券
                selectedCoupon = (SPCoupon)data.getSerializableExtra("selectCoupon");
                break;
//            case MobileConstants.Result_Code_GetValue:
//                invoice = data.getStringExtra("value");
//                invoceAview.setSubText(invoice);
//                break;
            case MobileConstants.Result_Code_GetAddress:
                consigneeAddress  = (SPConsigneeAddress)data.getSerializableExtra("consignee");
                refreshView();
                break;



        }
        loadTotalFee();

    }

    private void ShowFee() {
        feeGoodsFeeTxtv.setText("¥"+priceSum);
        mSumpricsse = calculativeCost(priceSum,mFee,mUsingEarning,mCoupon);
        payfeeTxtv.setText("应付金额: ¥" +mSumpricsse + "");
        feeCouponTxtv.setText("¥" + mCoupon);
        feeAmountTxtv.setText("实付款: ¥" +mSumpricsse + "");
    }

    public String calculativeCost(Double goods,Double postfee,Double stone,Double coupon ){
         if(goods - coupon - stone>199){
             mFee=0.0;
         }else{
             mFee=10.0;
         }
        Double lastCount=goods-coupon-stone+postfee;
         return lastCount.toString();
     }






}
