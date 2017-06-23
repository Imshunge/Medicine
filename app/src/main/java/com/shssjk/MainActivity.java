
package com.shssjk;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.CategoryAdapter;
import com.shssjk.common.SPDataAsyncManager;
import com.shssjk.common.MobileConstants;
import com.shssjk.fragment.BaseFragment;
import com.shssjk.fragment.HealthyFragment2;
import com.shssjk.fragment.InforFragment;
import com.shssjk.fragment.PersonFragment;
import com.shssjk.fragment.ShopFragment;

import com.shssjk.model.info.Information;
import com.shssjk.service.StepCounterService;
import com.shssjk.utils.Logger;
import com.shssjk.utils.DialogUtils;
import com.shssjk.utils.Utils;


import org.litepal.tablemanager.Connector;

import java.net.URL;
import java.util.List;


public class MainActivity extends BaseActivity {

    public static final String SELECT_INDEX = "selectIndex";
    public static final String CACHE_SELECT_INDEX = "cacheSelectIndex";
    public static final int INDEX_INFOR = 0;
    public static final int INDEX_COMMUNITY = 1;
    public static final int INDEX_SHOP = 2;
    public static final int INDEX_HEALTH = 3;
    public static final int INDEX_PERSON = 4;
    private static MainActivity mInstance;
    public int mCurrentSelectIndex;
    //	侧滑
    static SlidingMenu menu;

    ListView categoryListv;

    CategoryAdapter mCategoryAdapter;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MobileConstants.MSG_CODE_LOAD_DATAE_CHANGE:
                    if (msg.obj != null) {
                        SaveAddressTask task = new SaveAddressTask();
                        task.execute();
                    }
                    break;
                case MobileConstants.MSG_CODE_SHOW:
                    if (msg.obj != null) {
                        DialogUtils.showToast(MainActivity.this, msg.obj.toString());
                    }
                    break;
            }
        }
    };
    FragmentManager mFragmentManager;
    InforFragment mInforFragment;
    HealthyFragment2 mHealthyFragment;
    ShopFragment mShopFragment;
    PersonFragment mPersonFragment;
    RadioGroup mRadioGroup;
    RadioButton rbtnHome;
    //	RadioButton rbtnCategory;
    RadioButton rbtnShopcart;
    RadioButton rbtnPerson;
    RadioButton rbtnMy; //我
    RadioButton mCurrRb;
    RadioButton mLastRb;
    private String TAG = "MainActivity";

    public List<Information> getInformations() {
        return informations;
    }

    public void setInformations(List<Information> informations) {
        this.informations = informations;
    }

    private List<Information> informations;
    //退出时的时间
    private long mExitTime;

    private ChageToHealthReceiver chageToHealthReceiver;

    public static MainActivity getmInstance() {
        return mInstance;
    }
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(false, false, getString(R.string.title_home));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mFragmentManager = this.getSupportFragmentManager();
        mContext = this;
        super.init();
        addFragment();
        hiddenFragment();
//		initSlidingMenu();//侧滑菜单
        if (savedInstanceState != null) {
            mCurrentSelectIndex = savedInstanceState.getInt(CACHE_SELECT_INDEX, INDEX_INFOR);
        } else {
            mCurrentSelectIndex = INDEX_INFOR;
        }
        setSelectIndex(mCurrentSelectIndex);
        mInstance = this;
        initPush();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e("MainActivity", "onResume");
    }

    private void initPush() {
        // 启动百度push
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(MainActivity.this, "api_key"));
//	  数据库使用 LitePal ；项目地址是:https://github.com/LitePalFramework/LitePal
        Connector.getDatabase();
    }

    @Override
    public void initSubViews() {
        mInforFragment = new InforFragment();
//		mHealthyFragment = new HealthyFragment();
        mHealthyFragment = new HealthyFragment2();
//		mCommunityFragment = new CommunityFragment();
        mShopFragment = new ShopFragment();
//		mInforFragment.setMainActivity(this);
        mPersonFragment = new PersonFragment();

        mRadioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
        rbtnHome = (RadioButton) this.findViewById(R.id.rbtn_home);
        rbtnShopcart = (RadioButton) this.findViewById(R.id.rbtn_shopcart);
        rbtnPerson = (RadioButton) this.findViewById(R.id.rbtn_mine);
        rbtnMy = (RadioButton) this.findViewById(R.id.rbtn_my);
    }

    @Override
    public void initData() {
        //同步数据
        SPDataAsyncManager.getInstance(this, mHandler).startSyncData(new SPDataAsyncManager.SyncListener() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(String error) {

            }
        });


        //开启服务
        Intent service = new Intent(MainActivity.this, StepCounterService.class);
        startService(service);
    }

    @Override
    public void initEvent() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int key) {
                switch (key) {
                    case R.id.rbtn_home:
                        setSelectIndex(INDEX_INFOR);
                        break;
                    case R.id.rbtn_shopcart:
                        setSelectIndex(INDEX_SHOP);
                        break;
                    case R.id.rbtn_mine:
                        setSelectIndex(INDEX_HEALTH);
                        break;
                    case R.id.rbtn_my:
                        setSelectIndex(INDEX_PERSON);
                        break;
                    default:
                        break;
                }
            }
        });
        //        监听 消息推送信息
        IntentFilter filter = new IntentFilter(MobileConstants.ACTION_HEALTH_CHANGE_FRAGMENT);
        chageToHealthReceiver = new ChageToHealthReceiver();
        mContext.registerReceiver(chageToHealthReceiver, filter);



        //添加账号
//        Account account = new Account("account_ssjk", "com.shssjk.activity.type");
//        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
//        accountManager.addAccountExplicitly(account, null, null);
//        //测试同步
//        ContentResolver.setIsSyncable(account, "com.shssjk.activity.accountsync.provider", 1);
//        ContentResolver.setSyncAutomatically(account, "com.shssjk.activity.accountsync.provider", true);
//        ContentResolver.addPeriodicSync(account, "com.shssjk.activity.accountsync.provider",
//                Bundle.EMPTY, 10);


    }

    public void setSelectIndex(int index) {
        switch (index) {
            case INDEX_INFOR:
                showFragment(mInforFragment);
                changeTabtextSelector(rbtnHome);
                setTitle(getString(R.string.title_home));
                mCurrentSelectIndex = INDEX_INFOR;
                break;
            case INDEX_COMMUNITY:

                break;
            case INDEX_SHOP:
                showFragment(mShopFragment);
                changeTabtextSelector(rbtnShopcart);
                setTitle(getString(R.string.tab_item_community));
                mCurrentSelectIndex = INDEX_SHOP;
                break;
            case INDEX_HEALTH:
                showFragment(mHealthyFragment);
                changeTabtextSelector(rbtnPerson);
                setTitle(getString(R.string.tab_item_healthy));
                mCurrentSelectIndex = INDEX_HEALTH;
                //更新设备列表
                if (mContext != null) {
                    mContext.sendBroadcast(new Intent(MobileConstants.ACTION_HEALTH_LOADATA));
                }
//				if (mContext != null) {
//					mContext.sendBroadcast(new Intent(MobileConstants.ACTION_HEALTH_SUAGR_LOADATA));
//				}
                break;
            case INDEX_PERSON:
                //setTitleType(TITLE_DEFAULT);
                showFragment(mPersonFragment);
                changeTabtextSelector(rbtnMy);
                setTitle(getString(R.string.tab_item_my));
                mCurrentSelectIndex = INDEX_PERSON;
                break;
        }
    }

    /**
     * @throws
     * @Title: showFragment
     * @Description:
     * @param: @param fragment
     * @return: void
     */
    private void showFragment(BaseFragment fragment) {
        hiddenFragment();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.show(fragment);
        mTransaction.commitAllowingStateLoss();
    }

    //add by zzx
    public void setShowFragment(Information information) {
        showFragment(mInforFragment);
        changeTabtextSelector(rbtnHome);
        menu.toggle(false);
    }

    /**
     * @throws
     * @Title: hiddenFragment
     * @Description:
     * @param:
     * @return: void
     */
    private void hiddenFragment() {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.hide(mInforFragment);
        mTransaction.hide(mHealthyFragment);
//		mTransaction.hide(mCommunityFragment);
        mTransaction.hide(mShopFragment);
        mTransaction.hide(mPersonFragment);
        mTransaction.commitAllowingStateLoss();
    }

    /**
     * @throws
     * @Title: addFragment
     * @Description:
     * @param:
     * @return: void
     */

    private void addFragment() {

        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.fragmentView, mInforFragment);
        mTransaction.add(R.id.fragmentView, mShopFragment);
        mTransaction.add(R.id.fragmentView, mHealthyFragment);
        mTransaction.add(R.id.fragmentView, mPersonFragment);
        mTransaction.commitAllowingStateLoss();
    }

    public void changeTabtextSelector(RadioButton rb) {
        mLastRb = mCurrRb;
        mCurrRb = rb;
        if (mLastRb != null) {
            mLastRb.setTextColor(getResources().getColor(R.color.color_tab_item_normal));
            mLastRb.setSelected(false);
        }
        if (mCurrRb != null) {
            mCurrRb.setTextColor(getResources().getColor(R.color.color_tab_item_fous));
            mCurrRb.setChecked(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Logger.d(TAG, "onRestart.." +getIntent().hasExtra(SELECT_INDEX));
        int selectIndex = -1;
        if (getIntent() != null && getIntent().hasExtra(SELECT_INDEX)) {
            selectIndex = getIntent().getIntExtra(SELECT_INDEX, -1);
            //Logger.d(TAG, "onRestart , selectIndex : " + selectIndex );
            if (selectIndex != -1) setSelectIndex(selectIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CACHE_SELECT_INDEX, mCurrentSelectIndex);
    }

    @Override
    public void onBackPressed() {
        if (mCurrRb == rbtnHome) {
            super.onBackPressed();
        } else {
            setSelectIndex(INDEX_INFOR);
        }
    }

    private class SaveAddressTask extends AsyncTask<URL, Integer, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

        }

        @Override
        protected Long doInBackground(URL... params) {


            return null;

        }
    }

    //	显示侧滑菜单
    public static void startMenu() {
        menu.toggle();
    }

    /**
     * 侧滑菜单
     */
    private void initSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        menu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_WINDOW);
//        menu.attachToActivity((Activity)mContext, SlidingMenu.SLIDING_WINDOW);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.left_menu);
        categoryListv = (ListView) menu.findViewById(R.id.category_listv);
        mCategoryAdapter = new CategoryAdapter(this);
        categoryListv.setAdapter(mCategoryAdapter);
        categoryListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Information collect = (Information) mCategoryAdapter.getItem(position);
                setShowFragment(collect);
//				Intent intent = new Intent(CollectListActivity.this, ProductActivity.class);
//				intent.putExtra("goodsId", collect.getGoodsID());
//				CollectListActivity.this.startActivity(intent);
            }
        });
    }

    //广播接收器  设备列表变化
    class ChageToHealthReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MobileConstants.ACTION_HEALTH_CHANGE_FRAGMENT)) {
                Logger.e("FragmentBlood", "ShowDataReceiver");
                setSelectIndex(INDEX_HEALTH);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(chageToHealthReceiver);
        Logger.e("====", "onResume()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            showToast("再按一次退出尚尚健康");
            mExitTime = System.currentTimeMillis();
        } else {
//      在activity中调用 moveTaskToBack (boolean nonRoot)方法即可将activity 退到后台，注意不是finish()退出。
            moveTaskToBack(true);
        }
    }
    @Override
    protected void onPause() {
        Logger.e("====", "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logger.e("====", "onStop()");
        super.onStop();
    }

      @Override
    protected void onStart() {
        Logger.e("====", "onStart()");
        super.onStart();
    }



}
