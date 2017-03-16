package com.shssjk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shssjk.activity.user.LoginActivity;
import com.shssjk.global.MobileApplication;
import com.shssjk.model.SPProduct;
import com.shssjk.model.SPUser;
import com.shssjk.utils.SPStringUtils;
import com.shssjk.view.SPMoreImageView;

import java.util.List;

import butterknife.OnClick;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener{
    private String TAG = "PersonInfoActivity";

//    @Bind(R.id.head_mimgv)
//    SPMoreImageView headMimgv;
//    @Bind(R.id.nickname_txtv)
//    TextView nicknameTxtv;
//    @Bind(R.id.account_rlayout)
//    RelativeLayout accountRlayout;
//    private Context mContext;

    private Context mContext;

    //View addressView;
    View allOrderLayout;
    View waitPayLayout;			//待支付
    View waitReceiveLayout;		//待收货
    View waitCommentLayout;		//待评价
    View waitReturnLayout;		//退换货
    View collectLayout;			//收藏

    View integrateView;			//积分,余额
    View receiveAddressView;	//收货地址
    View couponView;			//优惠券
    //View settingsView;			//设置

    TextView balanceTxtv;			//余额
    TextView pointTxtv;				//积分
    TextView couponCountTxtv;		//优惠券数量
    //SPGuessYouLikeView  recommendProductView;

    TextView nicknameTxtv; 			//昵称

    RelativeLayout header_relayout;
    SPMoreImageView nickImage;
    TextView nickNameTxtv;

    //setting_btn
    Button settingBtn;

    //account_rlayout
    View accountView;

    //level_img
    ImageView levelImgv;

    //level_name_txtv
    TextView levelName;

    GridView mGridView;
//    SPGuessYouLikeAdapter mAdapter;
    List<SPProduct> mProducts ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(false, false, getString(R.string.login_title));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
//        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        allOrderLayout = findViewById(R.id.person_order_all_container);
        waitPayLayout =  findViewById(R.id.personal_order_waitpay_layout);
        waitReceiveLayout =  findViewById(R.id.personal_order_waitreceive_layout);
        waitCommentLayout =  findViewById(R.id.personal_order_waitcomment_layout);
        waitReturnLayout =  findViewById(R.id.personal_order_returned);
        collectLayout = findViewById(R.id.person_collect_aview);
        integrateView = findViewById(R.id.person_integrate_rlayout); 			//积分,余额
        receiveAddressView = findViewById(R.id.person_receive_address_aview);	//收货地址
        couponView =findViewById(R.id.person_coupon_aview);					//优惠券
        //settingsView = view.findViewById(R.id.person_settings_aview);				//设置

        balanceTxtv = (TextView)findViewById(R.id.person_stone_txtv);	//余额
        pointTxtv = (TextView)findViewById(R.id.person_point_txtv);		//积分
        couponCountTxtv = (TextView)findViewById(R.id.person_coupon_txtv);		//优惠券数量
        nicknameTxtv = (TextView)findViewById(R.id.nickname_txtv);		//昵称
        header_relayout= (RelativeLayout)findViewById(R.id.header_relayout);
        nickImage = (SPMoreImageView)findViewById(R.id.head_mimgv);
        mGridView = (GridView)findViewById(R.id.product_gdv);

        ////setting_btn
        settingBtn = (Button)findViewById(R.id.setting_btn);
        //account_rlayout
        accountView = findViewById(R.id.account_rlayout);
        //level_img
        levelImgv = (ImageView)findViewById(R.id.level_img);
        //level_name_txtv
        levelName = (TextView)findViewById(R.id.level_name_txtv);;

        String path= Environment.getExternalStorageDirectory().getPath();
        //showToast(path);
        Bitmap mBitmap = BitmapFactory.decodeFile(path + "/head.jpg");// 从sdcard中获取本地图片,通过BitmapFactory解码,转成bitmap
        if (mBitmap != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(mBitmap);
            nickImage.setImageDrawable(drawable);
        } else {
            /** 从服务器取,同时保存在本地 ,后续的工作 */
        }
        nickNameTxtv= (TextView)findViewById(R.id.nickname_txtv);
    }

    @Override
    public void initData() {

    }

    public void initEvent() {

        //addressView.setOnClickListener(this);

        allOrderLayout.setOnClickListener(this);
        waitPayLayout.setOnClickListener(this);
        waitReceiveLayout.setOnClickListener(this);
        waitCommentLayout.setOnClickListener(this);
        waitReturnLayout.setOnClickListener(this);
        collectLayout.setOnClickListener(this);

        integrateView.setOnClickListener(this);
        receiveAddressView.setOnClickListener(this);
        couponView.setOnClickListener(this);
        //settingsView.setOnClickListener(this);

        header_relayout.setOnClickListener(this);
        nickImage.setOnClickListener(this);
        nickNameTxtv.setOnClickListener(this);

        settingBtn.setOnClickListener(this);
        accountView.setOnClickListener(this);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "onItemClick...");

                if (mProducts != null && position >= 0 && position < mProducts.size()) {
                    SPProduct product = mProducts.get(position);
//                    startupActivity(product.getGoodsID());
                    Log.i(TAG, "onItemClick product.goodsName :"+product.getGoodsName() );
                }
            }
        });
    }

    public boolean checkLogin() {
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return false;
        }
        return true;
    }

    /**
     * doLogin or details
     *
     * @param flag true go detail, false go login
     */
    private void loginOrDetail(boolean flag) {
        if (flag) {
//            startActivity(new Intent(mContext, SPUserDetailsActivity_.class));
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }

    }


//    @OnClick({R.id.head_mimgv, R.id.nickname_txtv})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.head_mimgv:
//                loginOrDetail(MobileApplication.getInstance().isLogined);
//                break;
//            case R.id.nickname_txtv:
//                loginOrDetail(false);
//                break;
//        }
//    }

    @OnClick(R.id.account_rlayout)
    public void onClick() {
        loginOrDetail(MobileApplication.getInstance().isLogined);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    /**
     * 刷新View 数据
     */
    public void refreshView(){

        if (MobileApplication.getInstance().isLogined){
            SPUser user = MobileApplication.getInstance().getLoginUser();
            balanceTxtv.setText(String.valueOf(user.getUserMoney()));
            pointTxtv.setText(String.valueOf(user.getPayPoints()));
            if(SPStringUtils.isEmpty(user.getCouponCount())){
                couponCountTxtv.setText("0");
            }else{
                couponCountTxtv.setText(String.valueOf(user.getCouponCount()));
            }
            if (!SPStringUtils.isEmpty(user.getNickname())){
                nicknameTxtv.setText(user.getNickname());
            }

            if (!SPStringUtils.isEmpty(user.getLevelName())){
                levelImgv.setVisibility(View.VISIBLE);
                levelName.setVisibility(View.VISIBLE);
                levelName.setText(user.getLevelName());

                switch (Integer.valueOf(user.getLevel())){
                    case 1:
                        levelImgv.setImageResource(R.drawable.icon_level_one);
                        break;
                    case 2:
                        levelImgv.setImageResource(R.drawable.icon_level_two);
                        break;
                    case 3:
                        levelImgv.setImageResource(R.drawable.icon_level_three);
                        break;
                    default:
                        levelImgv.setImageResource(R.drawable.icon_level_one);
                        break;
                }

            }

            RoundedBitmapDrawable roundedBitmap = getCycleBitmpa(false);
            if (roundedBitmap != null) {
                nickImage.setImageDrawable(roundedBitmap);
            } else {
                /** 从服务器取,同时保存在本地 ,后续的工作 */
            }
        }else{
            balanceTxtv.setText("0");
            pointTxtv.setText("0");
            couponCountTxtv.setText("0");
            nickNameTxtv.setText("点击登录");
            levelImgv.setVisibility(View.INVISIBLE);
            levelName.setVisibility(View.INVISIBLE);

            RoundedBitmapDrawable roundedBitmap = getCycleBitmpa(true);
            if (roundedBitmap != null) {
                nickImage.setImageDrawable(roundedBitmap);
            } else {
                /** 从服务器取,同时保存在本地 ,后续的工作 */
            }
        }
    }

    public RoundedBitmapDrawable getCycleBitmpa(boolean isDefault){

        RoundedBitmapDrawable circularBitmapDrawable = null;
        String path= Environment.getExternalStorageDirectory().getPath();
        Bitmap mBitmap = BitmapFactory.decodeFile(path + "/head.jpg");
        if (isDefault){
            Drawable defaultDrawable = getResources().getDrawable(R.drawable.person_default_head);
            BitmapDrawable bd = (BitmapDrawable) defaultDrawable;
            mBitmap = bd.getBitmap();
        }else{
            mBitmap = BitmapFactory.decodeFile(path + "/head.jpg");
        }

        if (mBitmap != null) {
            circularBitmapDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), mBitmap);
            circularBitmapDrawable.setCornerRadius(this.getResources().getDimension(R.dimen
                    .head_corner_35));
            //nickImage.setImageDrawable(circularBitmapDrawable);
        }
        return circularBitmapDrawable;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_rlayout:
                loginOrDetail(MobileApplication.getInstance().isLogined);
                break;
            case R.id.nickname_txtv:
                loginOrDetail(false);
                break;
        }
    }
}
