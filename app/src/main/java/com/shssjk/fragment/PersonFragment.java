/**
 * @version V1.0
 */
package com.shssjk.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.shssjk.MainActivity;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.R;
import com.shssjk.activity.person.AllCollectActivity;
import com.shssjk.activity.person.BankListActivity;
import com.shssjk.activity.person.CameraListActivity;
import com.shssjk.activity.person.Level;
import com.shssjk.activity.person.MyStoneActivity;
import com.shssjk.activity.person.StartBusinessActivity;
import com.shssjk.activity.shop.OrderActivity;
import com.shssjk.activity.shop.OrderReturnedActivity;
import com.shssjk.activity.shop.OrderWaitcommentActivity;
import com.shssjk.activity.shop.ProductAllActivity;
import com.shssjk.activity.user.CouponList2Activity;
import com.shssjk.activity.user.LoginActivity;
import com.shssjk.activity.person.SettingActivity;
import com.shssjk.activity.person.UserDetailsActivity;
import com.shssjk.activity.shop.ConsigneeAddressListActivity;
import com.shssjk.activity.user.MyTeamActivity;
import com.shssjk.adapter.GuessYouLiketListAdapter;
import com.shssjk.adapter.SPGuessYouLikeAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;
import com.shssjk.model.SPUser;
import com.ipcamera.demo.BridgeService;
import com.shssjk.utils.OnClickEvent;
import com.shssjk.utils.SPStringUtils;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.CustomProgressBar;
import com.shssjk.view.GlideCircleTransform;
import com.shssjk.view.SPHomeListView;
import com.shssjk.view.SPMoreImageView;
import vstc2.nativecaller.NativeCaller;

import java.util.ArrayList;
import java.util.List;
/**
 *  首页 -> 我的
 *
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener,
        GuessYouLiketListAdapter.ItemClickListener {
    private String TAG = "PersonFragment";
    private Context mContext;
    //View addressView;
    View allOrderLayout;
    View waitPayLayout;            //待支付
    View waitReceiveLayout;        //待收货
    View waitCommentLayout;        //待评价
    View waitReturnLayout;        //退换货
    View collectLayout;            //收藏
//    View integrateView;            //积分,余额
    View receiveAddressView;    //收货地址
    View couponView;            //优惠券
    View myteamView;			//我的团队
    View mycameraView;			//我的摄像机
    View mybankView;			//我的银行卡
    View mystoneView;			//我的石头

    TextView stoneTxtv;            //石头
    TextView pointTxtv;                //积分
    TextView couponCountTxtv;        //优惠券数量
    //SPGuessYouLikeView  recommendProductView;
    RelativeLayout header_relayout;
    ImageView nickImage;
//    TextView nicknameTxtv;            //昵称
    TextView nickNameTxtv;
    //setting_btn
    Button settingBtn;

    //account_rlayout  设置
    View accountView;

    //level_img
    ImageView levelImgv;

    //level_name_txtv
    TextView levelName;

    GridView mGridView;
    SPHomeListView mHomeListView;
    SPGuessYouLikeAdapter mAdapter;
    List<SPProduct> mProducts=new ArrayList<>();
    CustomProgressBar  customProgressBar;//等级进度条

    private GuessYouLiketListAdapter homeProductListAdapter;
    private static final int MY_PERMISSIONS_REQUEST_TAKE_PHOTO = 6;
    private static final int MY_PERMISSIONS_REQUEST_CHOSE_PHOTO = 7;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    int mPageIndex=1;   //当前第几页:从1开始
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_fragment, null, false);
        super.init(view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void initSubView(View view) {
        allOrderLayout = view.findViewById(R.id.person_order_all_container);
        waitPayLayout = view.findViewById(R.id.personal_order_waitpay_layout);
        waitReceiveLayout = view.findViewById(R.id.personal_order_waitreceive_layout);
        waitCommentLayout = view.findViewById(R.id.personal_order_waitcomment_layout);
        waitReturnLayout = view.findViewById(R.id.personal_order_returned);
        collectLayout = view.findViewById(R.id.person_collect_aview);
//        integrateView = view.findViewById(R.id.person_integrate_rlayout);            //积分，石头，优惠券
        receiveAddressView = view.findViewById(R.id.person_receive_address_aview);    //收货地址
        couponView = view.findViewById(R.id.person_coupon_aview);                    //优惠券
        myteamView = view.findViewById(R.id.person_myteam_aview);				//我的团队
        mycameraView = view.findViewById(R.id.person_camera_aview);				//我的摄像机
        mybankView = view.findViewById(R.id.person_bank_aview);				//我的银行卡
//        stoneTxtv = (TextView) view.findViewById(R.id.person_stone_txtv);    //石头
        mystoneView=view.findViewById(R.id.person_mystone_aview);				//我的石头
//        pointTxtv = (TextView) view.findViewById(R.id.person_point_txtv);        //积分
//        couponCountTxtv = (TextView) view.findViewById(R.id.person_coupon_txtv);        //优惠券数量
        nickNameTxtv = (TextView) view.findViewById(R.id.nickname_txtv);        //昵称
        header_relayout = (RelativeLayout) view.findViewById(R.id.header_relayout);
        nickImage = (ImageView) view.findViewById(R.id.head_mimgv);
//        mGridView = (GridView) view.findViewById(R.id.product_gdv);

        mHomeListView= (SPHomeListView) view.findViewById(R.id.sphome_listview);
        View footerView =  ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_footer, null, false);
        mHomeListView.addFooterView(footerView);
      TextView  mCommentLoadMore= (TextView) footerView.findViewById(R.id.footer_hint_textview);
        mCommentLoadMore.setOnClickListener(this);

        ////setting_btn
        settingBtn = (Button) view.findViewById(R.id.setting_btn);
        //account_rlayout  设置
        accountView = view.findViewById(R.id.account_rlayout);
        //level_img  等级
        levelImgv = (ImageView) view.findViewById(R.id.level_img);
        //level_name_txtv
        levelName = (TextView) view.findViewById(R.id.level_name_txtv);
        customProgressBar= (CustomProgressBar) view.findViewById(R.id.cpb_progresbar);

        if (MobileApplication.getInstance().isLogined){
            SPUser    spUser=   MobileApplication.getInstance().getLoginUser();
            String url ="";
            if(!SSUtils.isEmpty(spUser.getHeader_pic())){
                url=  MobileConstants.BASE_HOST+ spUser.getHeader_pic();
            }else{
                url=   spUser.getHeadPic();
            }
            Glide.with(this)
                    .load(url).placeholder(R.drawable.person_default_head).transform(new GlideCircleTransform(mContext)).
                    into(nickImage);

        }
    }

    @Override
    public void initEvent() {
        allOrderLayout.setOnClickListener(this);
        waitPayLayout.setOnClickListener(this);
        waitReceiveLayout.setOnClickListener(this);
        waitCommentLayout.setOnClickListener(this);
        waitReturnLayout.setOnClickListener(this);
        collectLayout.setOnClickListener(this);
//        integrateView.setOnClickListener(this);
        receiveAddressView.setOnClickListener(this);
        couponView.setOnClickListener(this);
        myteamView.setOnClickListener(this);
        mybankView.setOnClickListener(this);
        header_relayout.setOnClickListener(this);
        nickImage.setOnClickListener(this);
        nickNameTxtv.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        accountView.setOnClickListener(this);
        mystoneView.setOnClickListener(this);
        int delayTime=2000;
        mycameraView.setOnClickListener(new OnClickEvent(delayTime) {
            @Override
            public void singleClick(View v) {
//           我的摄像机 避免2次点击
                if (!checkLogin()) return;
                startCameraListActivity();
            }
        });

    }

    public void initData() {
        homeProductListAdapter = new GuessYouLiketListAdapter(mContext,this);

        mHomeListView.setAdapter(homeProductListAdapter);
        refreshData(1);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.person_order_all_container) {
            startupOrderList(0);
        } else if (v.getId() == R.id.personal_order_waitpay_layout) {
            startupOrderList(0);
        } else if (v.getId() == R.id.personal_order_waitreceive_layout) {
            startupOrderList(2);
        } else if (v.getId() == R.id.personal_order_waitcomment_layout) {
//            待评价
            startupOrderWaitcommentActivity();
        } else if (v.getId() == R.id.personal_order_returned) {
//            退货
            startupOrderList(3);
        } else if (v.getId() == R.id.person_collect_aview) {
            //我的收藏
            if (!checkLogin()) return;
			Intent collectIntent = new Intent(getActivity() , AllCollectActivity.class);
			startActivity(collectIntent);
        } else if (v.getId() == R.id.person_integrate_rlayout) {
        } else if (v.getId() == R.id.person_receive_address_aview) {
            //收货地址
            if (!checkLogin()) return;
            getActivity().startActivity(new Intent(getActivity(), ConsigneeAddressListActivity.class));
        } else if (v.getId() == R.id.person_coupon_aview) {
            //优惠券
			if (!checkLogin())return;
			getActivity().startActivity(new Intent(getActivity() , CouponList2Activity.class));
        } else if (v.getId() == R.id.setting_btn) {
            //设置
            getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
        }  else if (v.getId() == R.id.nickname_txtv) {
            loginOrDetail(MobileApplication.getInstance().isLogined);
        } else if (v.getId() == R.id.head_mimgv || v.getId() == R.id.account_rlayout) {
            loginOrDetail(MobileApplication.getInstance().isLogined);
        }else if(v.getId()==R.id.person_myteam_aview){
//            我的团队
            if (!checkLogin()) return;
            getActivity().startActivity(new Intent(getActivity(), MyTeamActivity.class));
        }
        else if(v.getId()==R.id.person_camera_aview){
////           我的摄像机
       }
        else if(v.getId()==R.id.person_bank_aview){
//           我的银行卡
            if (!checkLogin()) return;
            checkIsToWork();
        }
        switch (v.getId()) {
            case R.id.footer_hint_textview:
                loadMoreData();
                break;
            case R.id.person_mystone_aview:
                if (!checkLogin()) return;
                startMyStoneActivity();
                break;
        }
    }

    private void getPermissionsOther() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CHOSE_PHOTO);
        } else {
            startCameraListActivity();
        }

    }

    //    我的银行卡
    private void startBankListActivity() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BankListActivity.class);
        startActivity(intent);
    }
    //    我的石头
    private void startMyStoneActivity() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MyStoneActivity.class);
        startActivity(intent);
    }

    //    启动摄像机列表界面
    private void startCameraListActivity() {

        showLoadingToast("正在打开");
        Intent intent = new Intent();
        intent.setClass(getActivity(), BridgeService.class);
        getActivity().startService(intent);
//        NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
                    Thread.sleep(3000);
                    Message msg = new Message();
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        Intent in = new Intent(getActivity(), TestCameraActivity.class);
//        getActivity().startActivity(in);


        /**
         * 测试
         *
         */
//        Intent in = new Intent(getActivity(), CameraListActivity.class);
//        getActivity().startActivity(in);

    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Intent in = new Intent(getActivity(), CameraListActivity.class);
            getActivity().startActivity(in);
            hideLoadingToast();
        }
    };
    /**
     * doLogin or details
     * @param flag  true go detail, false go login
     */
    private void loginOrDetail(boolean flag) {
        Intent intent=null;
        if (flag) {
            intent = new Intent(getActivity(), UserDetailsActivity.class);
//            getActivity().startActivity(new Intent(getActivity(), UserDetailsActivity.class));

        } else {
            intent = new Intent(getActivity(), LoginActivity.class);
//            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        startActivityForResult(intent, 888);


    }

    boolean checkLogin() {
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return false;
        }
        return true;
    }

    public void startupOrderList(int orderStatus) {

        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return;
        }
//订单列表
//		Intent allOrderList = new Intent(getActivity() , OrderListActivity.class);
        Intent allOrderList = new Intent(getActivity() , OrderActivity.class);
        allOrderList.putExtra("index", orderStatus);
		getActivity().startActivity(allOrderList);
    }
    //待评价
    public void startupOrderWaitcommentActivity(){
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return;
        }
        Intent allOrderWaitcommentList = new Intent(getActivity() , OrderWaitcommentActivity.class);
		getActivity().startActivity(allOrderWaitcommentList);
    }
    public void startupOrderReturnedActivity(){
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return;
        }
        Intent allOrderWaitcommentList = new Intent(getActivity() , OrderReturnedActivity.class);
        getActivity().startActivity(allOrderWaitcommentList);
    }
    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }
    /**
     * 刷新View 数据
     */
    public void refreshView() {
        if (MobileApplication.getInstance().isLogined) {
            SPUser user = MobileApplication.getInstance().getLoginUser();
            if (!SPStringUtils.isEmpty(user.getNickname())) {
                nickNameTxtv.setText(user.getNickname());
            }
            if (!SPStringUtils.isEmpty(user.getLevelName())) {
                levelImgv.setVisibility(View.VISIBLE);
                levelName.setVisibility(View.VISIBLE);
                levelName.setText(user.getLevelName());
                switch (Integer.valueOf(user.getLevel())) {
                    case 1:
                        levelImgv.setImageResource(R.drawable.icon_level_one);
                        break;
                    case 2:
                        levelImgv.setImageResource(R.drawable.icon_level_two);
                        break;
                    case 3:
                        levelImgv.setImageResource(R.drawable.icon_level_three);
                        break;
                    case 4:
                        levelImgv.setImageResource(R.drawable.icon_level_four);
                        break;
                    case 5:
                        levelImgv.setImageResource(R.drawable.icon_level_five);
                        break;
                    case 6:
                        levelImgv.setImageResource(R.drawable.icon_level_six);
                        break;
                    default:
                        levelImgv.setImageResource(R.drawable.icon_level_one);
                        break;
                }
            }
            if (MobileApplication.getInstance().isLogined){
                SPUser mUser = MobileApplication.getInstance().getLoginUser();
                String url ="";
                if(!SSUtils.isEmpty(mUser.getHeader_pic())){
                    url=  MobileConstants.BASE_HOST+ mUser.getHeader_pic();
                }else{
                    url=   mUser.getHeadPic();
                }
                Glide.with(this)
                        .load(url).placeholder(R.drawable.person_default_head).transform(new GlideCircleTransform(mContext)).
                        into(nickImage);
            }
            accountView.setVisibility(View.VISIBLE);
            settingBtn.setVisibility(View.VISIBLE);
            customProgressBar.setVisibility(View.VISIBLE);
            getInfoLevel();
        } else {
            nickNameTxtv.setText("点击登录");
            levelImgv.setVisibility(View.INVISIBLE);
            levelName.setVisibility(View.INVISIBLE);
//            新增
            accountView.setVisibility(View.INVISIBLE);
            settingBtn.setVisibility(View.INVISIBLE);
            customProgressBar.setVisibility(View.INVISIBLE);
            if (MobileApplication.getInstance().isLogined){
                String url = MobileConstants.BASE_HOST+ MobileApplication.getInstance().getLoginUser().getHeadPic();
                Glide.with(this)
                        .load(url).transform(new GlideCircleTransform(mContext)).
                        into(nickImage);
            }else{
                Glide.with(this)
                        .load(R.drawable.person_default_head).transform(new GlideCircleTransform(mContext)).
                        into(nickImage);
            }
        }
    }
    public void getInfoLevel() {
        PersonRequest.getLevelInfo(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    Level level = (Level) response;
                    customProgressBar.setProgressDesc("");
                    customProgressBar.setMaxProgress(SSUtils.str2Int(level.getAll_score()));
                    customProgressBar.setProgressColor(Color.parseColor("#79aa6b"));
                    customProgressBar.setCurProgress(SSUtils.str2Int(level.getNow_score()));
                } else {
                    showToast(msg);
                }
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    public void refreshData(int page) {
        ShopRequest.guessYouLike(page, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    mProducts = (List<SPProduct>) response;
                    homeProductListAdapter.setData(mProducts);
                } else {
                    showToast(msg);
                }

            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }
//    判断是否创业
    public void checkIsToWork() {
        PersonRequest.isWork(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
//                 （1、已创业；0未达到创业资格；2达到创业资格;
                    String str = (String) response;
                    if ("0".equals(str.trim())) {
                        startBusinessActivity();
                    } else if ("1".equals(str.trim())) {
                        startBankListActivity();
                    } else if ("2".equals(str.trim())) {
                        startBankListActivity();
                    }
                } else {
                    showToast(msg);
                }
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }
    /**
     * 创业说明
     */
    private void startBusinessActivity() {
        Intent intent = new Intent(getActivity(), StartBusinessActivity.class);
        startActivity(intent);
    }

    private void setListViewHeightBasedOnChildren() {
        int count = Double.valueOf(Math.ceil(Double.valueOf(homeProductListAdapter.getCount() / 2.0)))
                .intValue();
        /**
         * 获取屏幕宽度和高度
         */
        DisplayMetrics metric = new DisplayMetrics();
        MainActivity.getmInstance().getWindowManager().getDefaultDisplay().getMetrics(metric);
        float itemheight = getResources().getDimension(R.dimen.product_item_height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.
                FILL_PARENT, Float.valueOf(count * itemheight).intValue());
        mHomeListView.setLayoutParams(params);
    }
    public void startupActivity(String goodsID) {
        Intent intent = new Intent(getActivity(), ProductAllActivity.class);
        intent.putExtra("goodsId", goodsID);
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 888: //
                if (resultCode == Activity.RESULT_OK) {
                    refreshView();
                }
                break;
        }
    }
    @Override
    public void onItemClickListener(SPProduct product) {
        if (product != null ) {
                    startupActivity(product.getGoodsID());
                }
    }
    public void loadMoreData() {
        mPageIndex ++;
        ShopRequest.guessYouLike(mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<SPProduct> tempProducts = (List<SPProduct>) response;
                    if (tempProducts.size() > 0) {
                        mProducts.addAll(tempProducts);
                        homeProductListAdapter.setData(mProducts);
                    } else {
                        mPageIndex--;
                        showToast("没有更多了");
                    }
                } else {
                    showToast(msg);
                }
                hideLoadingToast();
                setListViewHeightBasedOnChildren();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                hideLoadingToast();
            }
        });
    }
    @Override
    public void gotoLoginPageClearUserDate() {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_TAKE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startCameraListActivity();
                getPermissionsOther();
            } else {
                // Permission Denied
                showToast("禁止使用相机权限将会导致摄像机功能异常!");
            }
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_CHOSE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraListActivity();
            } else {
                // Permission Denied
                showToast("禁止选择图片将会导致上传图片功能异常!");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
