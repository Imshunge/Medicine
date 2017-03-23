package com.shssjk.activity.shop;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.dao.SPPersonDao;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;
import com.shssjk.model.order.PayOrder;
import com.shssjk.model.person.SPConsigneeAddress;
import com.shssjk.model.shop.Coupon;
import com.shssjk.model.shop.Totalprice;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.SPArrowRowView;
import com.shssjk.view.SwitchButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.shssjk.utils.SSUtils.getFromAssets;

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

    TextView  title_coupon_txtv;
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
    RelativeLayout rl_coupon;
    Button payBtn;       //结算按钮

    TextView titleTxtv; //标题
    TextView nowUseStone;//现在使用石头
    private Context mContext;
    /********* 服务器拉取的数据源集合 *****************/
    SPConsigneeAddress consigneeAddress ;   //当前收货人信息
    List<SPProduct> products=new ArrayList<>();               //商品列表
    List<Coupon> coupons=new ArrayList<>();                  //优惠券列表
    JSONObject userinfoJson;                //用户信息(积分,余额)
    JSONObject amountDict;                   //结算金额汇总
    JSONObject totalPrice;                      //总价
    /********* 选中的数据 *****************/
    JSONObject selectedDeliverJson;         //当前选中的物流
    Coupon selectedCoupon;                //当前选中的优惠券
    int points=0;                             //使用的石头
    FrameLayout  addressFl;                 //收货地址列表
    private TextView stonesumTxtv;
    private JSONArray formDataArray;
    private Double mEarning=0.0;  //可用石头
    private Double priceSum=0.0;
    private Double mUsingEarning=0.0; // 使用石头
    private Double mCoupon=0.0; // 优惠券
    private Double mFee=0.0; // 运费
    private TextView coupontimeTxtv;
    private TextView couponsumTxtv;
    private String mSumpricsse;
    private Button btnStone;
    private double sum;
    private Totalprice totalPrice1;
//   积分兑换传的参数
    private String goodsId="";
    private String spec_key="";
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
        if(intent.getStringExtra("goodsId")!=null &&intent.getStringExtra("spec_key")!=null ){
            goodsId = intent.getStringExtra("goodsId");
            spec_key = intent.getStringExtra("spec_key");
        }
        consigneeTxtv =(TextView) findViewById(R.id.order_consignee_txtv);
        addressTxtv =(TextView) findViewById(R.id.order_address_txtv);
        mGallery =(LinearLayout) findViewById(R.id.product_list_gallery_lyaout);
        productCountTxtv =(TextView) findViewById(R.id.order_product_count_txtv);
        //返回按钮
        //结算按钮
        payBtn =(Button) findViewById(R.id.pay_btn);
        couponAview =(SPArrowRowView) findViewById(R.id.order_coupon_aview);

        feeGoodsFeeTxtv =(TextView) findViewById(R.id.fee_goodsfee_txtv);

        feeShoppingTxtv =(TextView) findViewById(R.id.fee_shopping_txtv);

        feeCouponTxtv =(TextView) findViewById(R.id.fee_coupon_txtv);

        feePointTxtv =(TextView) findViewById(R.id.fee_point_txtv);

        feeAmountTxtv =(TextView) findViewById(R.id.fee_amount_txtv);

        payfeeTxtv =(TextView) findViewById(R.id.payfee_txtv);

        bananceSth =(SwitchButton) findViewById(R.id.order_balance_sth);


        balanceTxtv =(TextView) findViewById(R.id.order_balance_txtv);

        stoneEit =(EditText) findViewById(R.id.edit_use_stone_num);
//
        addressFl =(FrameLayout) findViewById(R.id.order_confirm_consignee_layout);
        stonesumTxtv=(TextView) findViewById(R.id.stone_sum_txtv);
        nowUseStone=(TextView) findViewById(R.id.stone_sum_use_txtv);

        coupontimeTxtv=(TextView) findViewById(R.id.coupon_time_txtv);
        couponsumTxtv=(TextView) findViewById(R.id.coupon_sum_use_txtv);

        btnStone = (Button) findViewById(R.id.btn_stone);
        rl_coupon= (RelativeLayout) findViewById(R.id.rl_coupon);
        title_coupon_txtv=(TextView) findViewById(R.id.title_coupon_txtv);

    }

    @Override
    public void initData() {
         if(SSUtils.isEmpty(goodsId))  {
             refreshData();
             couponAview.setVisibility(View.VISIBLE);
             feeCouponTxtv.setVisibility(View.VISIBLE);
             title_coupon_txtv.setVisibility(View.VISIBLE);
         }else{
             getPointsConfirmOrderData();
             couponAview.setVisibility(View.GONE);
             feeCouponTxtv.setVisibility(View.INVISIBLE);
             title_coupon_txtv.setVisibility(View.INVISIBLE);
         }
        ShowFee();
    }
    @Override
    public void initEvent() {
        addressFl.setOnClickListener(this);
        couponAview.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        btnStone.setOnClickListener(this);
    }
    public void refreshData(){
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
                            coupons = (List<Coupon>) mDataJson.get("coupons");
                        }
                        if (mDataJson.has("userInfo")) {
                            userinfoJson = mDataJson.getJSONObject("userInfo");
                        }
                        if (mDataJson.has("totalPrice")) {
                            totalPrice1 = (Totalprice) mDataJson.get("totalPrice");
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
        }, new SPFailuredListener(ConfirmOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
//        initEvent();
    }

    public void getPointsConfirmOrderData( ){
        ShopRequest.getPointsConfirmOrderData(goodsId,spec_key,new SPSuccessListener() {
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
                            coupons = (List<Coupon>) mDataJson.get("coupons");
                        }
                        if (mDataJson.has("userInfo")) {
                            userinfoJson = mDataJson.getJSONObject("userInfo");
                        }
                        if(mDataJson.has("totalPrice")){
                            totalPrice1 = (Totalprice) mDataJson.get("totalPrice");
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
        }, new SPFailuredListener(ConfirmOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
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
            if(userinfoJson.has("do_earnings")){
                String earning = userinfoJson.getString("do_earnings");
                if(!SSUtils.isEmpty(earning)){
                    mEarning = SSUtils.string2double(earning);
                    stonesumTxtv.setText(""+mEarning+"");
//                   更新本地 石头信息
                    SPSaveData.putValue(mContext,"do_earnings",earning);
                }
            }
            if(userinfoJson.has("do_score")){
                String do_score = userinfoJson.getString("do_score");
                if(!SSUtils.isEmpty(do_score)){
//                   更新本地 优惠券 信息
                    SPSaveData.putValue(mContext,"do_score",do_score);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void buildProductGallery(){
        int num=0;
        for (int i = 0; i < products.size(); i++){
            String url = products.get(i).getImageThumlUrl();
            View view = LayoutInflater.from(this).inflate(R.layout.activity_index_gallery_item, mGallery, false);
            ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
            mGallery.addView(view);
             num=num +SSUtils.str2Int(products.get(i).getGoodsNum());
        }
        productCountTxtv.setText("共" + num + "件商品");
    }
    /**
     * 刷新页面数据
     */
    public void refreshView(){
        try {
            if(!SSUtils.isEmpty(consigneeAddress.getConsignee())) {
                consigneeTxtv.setText(consigneeAddress.getConsignee() +"  "+consigneeAddress.getMobile());
            }
            if(!SSUtils.isEmpty(consigneeAddress.getConsignee())&&!SSUtils.isEmpty(consigneeAddress.getFullAddress())){
                addressTxtv.setText(consigneeAddress.getFullAddress());
            }
            //优惠券
            if (selectedCoupon.getCouponType() == 1){
                rl_coupon.setVisibility(View.VISIBLE);
                couponAview.setSubText(selectedCoupon.getName());
                //优惠码
//                优惠券价格大于商品价格 按照商品价格显示
                if(SSUtils.string2double(selectedCoupon.getMoney())>priceSum){
                    mCoupon=priceSum;
                    feeCouponTxtv.setText("¥" + priceSum);
                    couponsumTxtv.setText(priceSum.toString());
                }else {
                    mCoupon=     SSUtils.string2double(selectedCoupon.getMoney());
                    feeCouponTxtv.setText("¥" + selectedCoupon.getMoney());
                    couponsumTxtv.setText("¥" +selectedCoupon.getMoney());
                }
                String sendTime=SSUtils.TimeStamp2Date(selectedCoupon.getUseEndTime().trim(), "yyyy-MM-dd HH:mm:ss");
                coupontimeTxtv.setText(sendTime);
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

//            积分商品
            if(!SSUtils.isEmpty(goodsId)){
                params.put("type" ,"3");
            }


            formDataArray = new JSONArray();
            //添加商品明细
            try {
                for (SPProduct  product : products) {
                    JSONObject formJson = new JSONObject();
                    formJson.put("goods_id", product.getGoodsID());
                    formJson.put("price", product.getGoodsPrice());
                    formJson.put("number", product.getGoodsNum());
                    formJson.put("spec_key", product.getSpecKey());
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
        if (SSUtils.isEmpty(consigneeAddress.getAddressID())) {
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
                hideLoadingToast();
                PayOrder order = (PayOrder) response;
                startUpPayBefor(order);
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
//     如果使用石头 更新 再次更新本地石头信息
        Double nowStone= mEarning -mUsingEarning;
        //                   更新本地 石头信息
        SPSaveData.putValue(mContext,"do_earnings",nowStone+"");
        Intent payIntent = new Intent(this , BeforPayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order",order);
        payIntent.putExtras(bundle);
        startActivity(payIntent);
        finish();
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
//                没有优惠券不跳转
                if(coupons.size()==0){
                    showToast("您没有可用的优惠券");
                    return;
                }
//            跳转    优惠券 列表
                MobileApplication.getInstance().list = coupons;
                Intent couponIntent = new Intent(this , AvailableCouponActivity.class);
                couponIntent.putExtra("coupon",selectedCoupon);
                startActivityForResult(couponIntent, 2);
                break;
            case R.id.pay_btn:
//           去结算
                orderCommint();
                break;
            case R.id.btn_stone:
//                showConfirmDialog(getFromAssets(("guid_stone.txt"), mContext),"使用说明");
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
                selectedCoupon = (Coupon)data.getSerializableExtra("selectCoupon");
                break;
            case MobileConstants.Result_Code_GetAddress:
                consigneeAddress  = (SPConsigneeAddress)data.getSerializableExtra("consignee");
                refreshView();
                break;
        }
        loadTotalFee();
    }
    private void ShowFee() {
        feeGoodsFeeTxtv.setText("¥ "+priceSum);
        mSumpricsse = calculativeCost(priceSum,mFee,mUsingEarning,mCoupon);
        payfeeTxtv.setText("应付金额: ¥ " +mSumpricsse + "");
        feeCouponTxtv.setText("¥" + mCoupon);
        feeAmountTxtv.setText("实付款: ¥ " +mSumpricsse + "");
        feeShoppingTxtv.setText("¥ "+mFee+"");
    }
//    所有用户订单，单笔订单总金额小于199元，则需每单承担10元基础运费；满199元则免基础运费。
    public String calculativeCost(Double goods,Double postfee,Double stone,Double coupon ){
         if(goods - coupon - stone>=199){
             mFee=0.0;
         }else{
             mFee=10.0;
         }
//扩大100倍，处理类似 10.00-9.60 =0.40000000000000036的情况
        Double lastCount=goods*100-coupon*100-stone*100+postfee*100;
        if(lastCount<0){
            lastCount=0.0;
            if(goods>0){ //商品价格不为0
                showToast("石头金额不能大于支付金额");
                stoneEit.setText("");
            }
        }
         Double lastCounts= lastCount/100;
         return lastCounts.toString();
     }

}
