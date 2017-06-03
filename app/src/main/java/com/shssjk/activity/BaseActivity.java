
package com.shssjk.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import com.shssjk.activity.user.LoginActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.global.SPSaveData;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPUserRequest;
import com.shssjk.model.SPUser;
import com.shssjk.utils.Logger;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.DialogUtils;
import com.shssjk.utils.LoadingDialog;
import com.shssjk.utils.SSUtils;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * @version 1.0
 * @Description BaseActivity   每个Activity都要继承BaseActivity
 * 统一标题、登录超时处理
 */
public abstract class BaseActivity extends FragmentActivity implements IViewController {

    private String TAG = "SPBaseActivity";
    public final int TITLE_HOME = 1;
    public final int TITLE_DEFAULT = 0;
    public final int TITLE_CATEGORY = 2;
    private SharedPreferences pref;

    public JSONObject mDataJson;        //包含网络请求所有结果
    public LoadingDialog mLoadingDialog;
    public boolean isCustomerTtitle;    //是否自定义标题栏
    public boolean isBackShow;            //是否显示返回箭头
    public boolean isMenuShow;            //是否显示功能按钮
    private String mTtitle;            //标题栏
    private Button mMenuBtn;
    private Button mBackBtn;
    private TextView mTitleTxtv;

    FrameLayout mTitleBarLayout;
    FrameLayout mDefaultLayout;
    LinearLayout mHomeLayout;
    LinearLayout mCategoryLayout;
    RelativeLayout fragmentView;
    private Context mContext;

    /**
     * 是否自定义标题 , 该方法必须在子Activity的 super.onCreate()之前调用, 否则无效
     *
     * @param customerTtitle
     */
    public void setCustomerTitle(boolean backShow, boolean customerTtitle, String title) {
        isCustomerTtitle = customerTtitle;
        isBackShow = backShow;
        mTtitle = title;
    }

    public void setCustomerTitle(boolean backShow, boolean customerTtitle, String title, boolean menuShow) {
        isCustomerTtitle = customerTtitle;
        isBackShow = backShow;
        isMenuShow = menuShow;
        mTtitle = title;
    }

    public void setTitle(String title) {
        mTtitle = title;
        if (mTitleTxtv != null) mTitleTxtv.setText(mTtitle);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        ActivityCollector.addActivity(this); //添加到集合
        if (isCustomerTtitle) {
            //自定义标题
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        }
        mContext = this;
    }

    public void bindClickListener(View v, View.OnClickListener listener) {
        if (v != null && listener != null) {
            v.setOnClickListener(listener);
        }
    }

    /**
     * 子activity初始化
     *  在子类的 onCrate()中调用:super.init() 会调用
     *  initSubViews();
        initEvent();
         initData();
     * <p>
     *  例子
     * protected void onCreate(Bundle savedInstanceState) {
     * super.setCustomerTitle(true, true, getString(R.string.camera_add), true);
     * super.onCreate(savedInstanceState);
     * setContentView(R.layout.activity_add_camera);
     * super.init();   调用
     * }
     */
    public void init() {
        if (isCustomerTtitle) {
            //设置标题为某个layout
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        }
        mBackBtn = (Button) findViewById(R.id.titlebar_back_btn);
        if (isBackShow) {
            mBackBtn.setVisibility(View.VISIBLE);
            mBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.this.finish();
                }
            });
        } else {
            if (mBackBtn != null) mBackBtn.setVisibility(View.GONE);
        }

        mMenuBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        if (isMenuShow) {
            mMenuBtn.setVisibility(View.VISIBLE);
            mMenuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//					BaseActivity.this.finish();
                }
            });
        } else {
            if (mMenuBtn != null) mMenuBtn.setVisibility(View.GONE);
        }
        String title = this.getTitle().toString();
        mTitleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        if (mTitleTxtv != null) mTitleTxtv.setText(mTtitle);
        //setSearchViewStyle(homeSearch);
        //setSearchViewStyle(categorySearch);
        initSubViews();
        initEvent();
        initData();
        Logger.e("now is runing:", getClass().getSimpleName());
    }

    public void setTitleType(int type) {
        int padding = getResources().getDimensionPixelSize(R.dimen.height_tab_bottom_item);
        fragmentView.setPadding(0, padding - 10, 0, padding);
        mTitleBarLayout.setBackgroundResource(R.color.bg_activity);
        if (type == TITLE_HOME) {
            mHomeLayout.setVisibility(View.VISIBLE);
            mCategoryLayout.setVisibility(View.INVISIBLE);
            mDefaultLayout.setVisibility(View.INVISIBLE);
            fragmentView.setPadding(0, 0, 0, padding);
            mTitleBarLayout.setBackgroundResource(R.color.transparent);
        } else if (type == TITLE_CATEGORY) {
            mHomeLayout.setVisibility(View.INVISIBLE);
            mCategoryLayout.setVisibility(View.VISIBLE);
            mDefaultLayout.setVisibility(View.INVISIBLE);
        } else {
            mHomeLayout.setVisibility(View.INVISIBLE);
            mCategoryLayout.setVisibility(View.INVISIBLE);
            mDefaultLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setSearchViewStyle(SearchView search) {
        try {
            Field field = search.getClass().getDeclaredField("mSearchPlate");
            field.setAccessible(true);
            View tv = (View) field.get(search);
            tv.setBackgroundResource(R.color.transparent);

            Field field1 = search.getClass().getDeclaredField("mSearchEditFrame");
            field1.setAccessible(true);
            View tv1 = (View) field1.get(search);
            tv1.setBackgroundResource(R.color.transparentwhite);

            Field fieldimg = search.getClass().getDeclaredField("mCloseButton");
            fieldimg.setAccessible(true);
            ImageView image = (ImageView) fieldimg.get(search);
            image.setImageResource(R.drawable.icon_search_close);
            image.setBackgroundResource(R.color.transparent);

            Field[] fs = search.getClass().getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true);
                Object val = f.get(search);
                System.out.println("name:" + f.getName() + "\t value = " + val);
            }
            Field mQueryTextView = search.getClass().getDeclaredField("mQueryTextView");
            mQueryTextView.setAccessible(true);
            TextView tvsearch = (TextView) mQueryTextView.get(search);
            tvsearch.setTextColor(0xffffffff);
            tvsearch.setHintTextColor(0xffffffff);
        } catch (Exception e) {
            try {
                Field mQueryTextView = search.getClass().getDeclaredField("mSearchSrcTextView");
                mQueryTextView.setAccessible(true);
                TextView tvsearch = (TextView) mQueryTextView.get(search);
                tvsearch.setTextColor(0xffffffff);
                tvsearch.setHintTextColor(0xffffffff);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void showToast(String msg) {
        DialogUtils.showToast(this, msg);
    }

    public void showLoadingToast() {
        showLoadingToast(null);
    }

    public void showLoadingToast(String title) {
        mLoadingDialog = new LoadingDialog(this, title);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();
    }

    public void showConfirmDialog(String message, String title,
                                  final ConfirmDialog.ConfirmDialogListener confirmDialogListener,
                                  final int actionType) {
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                if (confirmDialogListener != null) confirmDialogListener.clickOk(actionType);
            }
        });

        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void showConfirmDialog(String message, String title) {
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void hideLoadingToast() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }


    public void showToastUnLogin() {
        showToast(getString(R.string.toast_person_unlogin));
    }

    public void toLoginPage() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    //	当账号在别处登录时调用
    public void toLoginPage2() {
        String username = SPSaveData.getString(mContext, "username");

//		清空用户信息
        MobileApplication.getInstance().exitLogin();
        SPSaveData.clearLoginData(mContext);
        ActivityCollector.finishAll();
        //跳转登录页面
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.putExtra("isFristLogin", false);
        loginIntent.putExtra("username", username);
        startActivity(loginIntent);
    }


    /**
     * 进入产品详情页
     *
     * @param orderID
     */
    public void gotoProductDetail(String orderID) {
//		Intent detailIntent = new Intent(this  , SPOrderDetailActivity_.class);
//		detailIntent.putExtra("orderId", orderID);
//		startActivity(detailIntent);
    }


    /**
     * 启动web Activity
     *
     * @param url
     */
    public void startWebViewActivity(String url, String title) {
//		Intent shippingIntent = new Intent(this , SPWebviewActivity_.class);
//		shippingIntent.putExtra("url" ,url);
//		shippingIntent.putExtra("title" , title);
//		startActivity(shippingIntent);
    }

    /**
     * 以下三个函数不需要再子类调用, 只需要在子类的
     * onCrate()中调用:super.init()方法即可
     * 基类函数,初始化界面
     */
    abstract public void initSubViews();

    /**
     * 基类函数, 初始化数据
     */
    abstract public void initData();

    /**
     * 基类函数, 绑定事件
     */
    abstract public void initEvent();

    /**
     * 处理网络加载过的数据
     */
    public void dealModel() {
    }

    @Override
    public void gotoLoginPageClearUserDate() {
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
        builder.setMessage("您的账号在其它设备登陆");
        builder.setTitle("系统提示");
//		builder.setCanceledOnTouchOutside(false);

        builder.setPositiveButton(R.string.to_login, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                toLoginPage2();
            }
        });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        toLoginPage2();
                    }
                });
        builder.create().show();
    }

    @Override
    public void gotoLoginPage() {
        /*if (!SPStringUtils.isEmpty(msg)){
			showToast(msg);
		}*/
//		ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
//		builder.setMessage("您的账号在其它设备登陆");
//		builder.setTitle("系统提示");
////		builder.setCanceledOnTouchOutside(false);
//
//		builder.setPositiveButton(R.string.to_login, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				//设置你的操作事项
//				toLoginPage();
//			}
//		});
//		builder.setNegativeButton(R.string.cancel,
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						toLoginPage();
//					}
//				});
//		builder.create().show();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Logger.e("BaseActivity", "onResume");
        Long time = SPSaveData.getLong(this, "time");
        Long hour = SSUtils.getTimeLag(time);
        /**
         * 登录超时处理 登录时会记录一个时间，在BaseActivity 的onResume 方法中计算时间差大于20H 自动登录一下 ，
         * 只要继承该方法的Activity 初始化都会调用该方法
         */
        if (hour > 20L) {
            doLogin();
        }
    }

    private void doLogin() {
        pref = getSharedPreferences("com.shssjk.activity.push",
                MODE_PRIVATE);
        String channalId = pref.getString("channalId", "");
        String appId = pref.getString("appId", "");
        String spikey = MobileConstants.API_KEY;
        String deviceyppe = "android";
        String username = SPSaveData.getString(getBaseContext(), "username");
        String pass = SPSaveData.getString(getBaseContext(), "pwd");
        SPUserRequest.doLogin(username, pass,
                channalId, appId, spikey, deviceyppe,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        if (response != null) {
                            SPUser user = (SPUser) response;
                            MobileApplication.getInstance().setLoginUser(user);
                            Logger.e("doLogin", "success" + user.getNickname());
                            Logger.e("doLogin  getToken", "success" + user.getToken());
                        }
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        Logger.e("doLogin", "FailuredListener");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
