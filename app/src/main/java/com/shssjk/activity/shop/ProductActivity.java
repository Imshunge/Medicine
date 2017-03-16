package com.shssjk.activity.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.IViewController;
import com.shssjk.adapter.ProductDetailInnerTabAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;
import com.shssjk.model.shop.SPProductSpec;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SPShopUtils;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.tagview.Tag;
import com.shssjk.view.tagview.TagListView;
import com.shssjk.view.tagview.TagView;
import com.soubao.tpshop.utils.SPStringUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 商品情页  包含3个  商品", "详情", "评价
 */
public class ProductActivity extends BaseActivity implements View.OnClickListener, TagListView.OnTagClickListener {
    public static String[] productDetailInnerTitles = new String[]{"商品", "详情", "评价"};
    MagicIndicator mMagicIndicator;
    List<String> mDataList = Arrays.asList(productDetailInnerTitles);
    LinearLayout likeLl; //收藏
    RelativeLayout carRl; //购物车
    FrameLayout titlbarFl;
    TextView titleTxtv; //标题
    ImageView likeImgv;//收藏图标
    TextView likeTxtv;//收藏文本
    Button cartBtn;      //加入购物车
    TextView cartCountTxtv;//购物车数量
    EditText cartCountEtxtv;
    Button buyBtn;//立即购买
    int mCartCount = 1;
    JSONObject priceJson;
    JSONObject specJson;
    ShopCartChangeReceiver mShopCartChangeReceiver;
    private ViewPager mViewPager;
    private FragmentPagerAdapter fragPagerAdapter;
    private String goodsId;    //商品ID
    private String specs = null;   //商品规格
    private String contents;   //图文详情
    private int position;
    private SPProduct mProduct;  // 选择的商品
    private Context mContext;

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getCart_num() {
        return cart_num;
    }

    public void setCart_num(String cart_num) {
        this.cart_num = cart_num;
    }

    public  void setCartNumber(String cart_num){
        cartCountTxtv.setText(cart_num);
    }

    private String cart_num;//购物车 数量

    private String is_collect;//是否已收藏

    private Map<String, String> selectSpecMap;//保存选择的规格ID

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Map<String, String> getSelectSpecMap() {
        return selectSpecMap;
    }

    public void setSelectSpecMap(Map<String, String> selectSpecMap) {
        this.selectSpecMap = selectSpecMap;
    }

    public SPProduct getmProduct() {
        return mProduct;
    }

    public void setmProduct(SPProduct mProduct) {
        this.mProduct = mProduct;
    }

    public int getmCartCount() {
        return mCartCount;
    }

    public void setmCartCount(int mCartCount) {
        this.mCartCount = mCartCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.activity_product_title));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mContext = this;
        //监听购物车数据变化广播
//        IntentFilter filter = new IntentFilter(MobileConstants.ACTION_SHOPCART_CHNAGE);
//        this.registerReceiver(mShopCartChangeReceiver = new ShopCartChangeReceiver(), filter);
        super.init();
    }
    @Override
    public void initSubViews() {
        titlbarFl = (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(mContext, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        mMagicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        this.goodsId = getIntent().getStringExtra("goodsId");
        this.contents = getIntent().getStringExtra("content");
        this.position = getIntent().getIntExtra("position", 0);
        fragPagerAdapter = new ProductDetailInnerTabAdapter(getSupportFragmentManager(), goodsId, contents);
        mViewPager.setAdapter(fragPagerAdapter);
        // 当前页不定位到中间
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.sub_title));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.light_red));
                simplePagerTitleView.setTextSize(12);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }
            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setColors(ContextCompat.getColor(context, R.color.light_red));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        mMagicIndicator.setDelegate(new SimpleViewPagerDelegate(mViewPager));
        cartCountTxtv = (TextView) findViewById(R.id.productcart_count);
        cartBtn = (Button) findViewById(R.id.add_cart_btn);
        likeImgv = (ImageView) findViewById(R.id.product_like_imgv);
        likeTxtv = (TextView) findViewById(R.id.product_like_txtv);
        likeLl = (LinearLayout) findViewById(R.id.like_lyaout);
        //收藏
        carRl = (RelativeLayout) findViewById(R.id.product_cart_rlayout);
        //购物车
        cartCountEtxtv = (EditText) findViewById(R.id.cart_count_dtxtv);
        buyBtn = (Button) findViewById(R.id.buy_btn);
    }

    @Override
    public void initData() {
//        loadCartCount();
//        refreshCollectDatta();
    }

    @Override
    public void initEvent() {
        likeLl.setOnClickListener(this);
        carRl.setOnClickListener(this);
        cartBtn.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
    }
    /**
     * 刷新收藏按钮
     */
    public void refreshCollectButton() {
        if (SPShopUtils.isCollected(goodsId)) {
            //收藏
            likeImgv.setImageResource(R.drawable.product_like);
            likeTxtv.setText(getString(R.string.product_details_like));
        } else {
            //未收藏
            likeImgv.setImageResource(R.drawable.product_star);
            likeTxtv.setText(getString(R.string.product_details_unlike));
        }
    }
//     is_collect  1收藏  0 未收藏
    public void refreshCollectButton(String is_collect) {
        Logger.e(this, "is_collect" + is_collect);
            setIs_collect(is_collect);
        if (is_collect!=null&&"1".equals(is_collect.trim())) {
            //收藏
            likeImgv.setImageResource(R.drawable.product_like);
            likeTxtv.setText(getString(R.string.product_details_like));
        } else {
            //未收藏
            likeImgv.setImageResource(R.drawable.product_star);
            likeTxtv.setText(getString(R.string.product_details_unlike));
        }
    }
    /**
     * 刷新收藏数据
     */
    public void refreshCollectDatta() {

//        if (MobileApplication.getInstance().isLogined) {
//            SPPersonRequest.getGoodsCollectWithSuccess(new SPSuccessListener() {
//                @Override
//                public void onRespone(String msg, Object response) {
//                    MobileApplication.getInstance().collects = (List<SPCollect>) response;
//                    refreshCollectButton();
//                }
//            }, new SPFailuredListener() {
//                @Override
//                public void onRespone(String msg, int errorCode) {
//                    showToast(msg);
//                }
//            });
//        } else {
//            MobileApplication.getInstance().collects = null;
//        }
    }

//    /**
//     * 刷新购物车数据
//     */
//    public void loadCartCount() {
//        SPShopCartManager shopCartManager = SPShopCartManager.getInstance(this);
//        int shopCount = shopCartManager.getShopCount();
//        if (shopCount <= 0) {
//            SPShopCartManager.getInstance(this).reloadCart();
//            cartCountTxtv.setVisibility(View.INVISIBLE);
//        } else {
//            cartCountTxtv.setVisibility(View.VISIBLE);
//            cartCountTxtv.setText(String.valueOf(shopCount));
//        }
//    }
    @Override
    public void onClick(final View v) {
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return;
        }
        switch (v.getId()) {
            case R.id.like_lyaout:
                String type = "1";
                if (getIs_collect().equals("1")) {//收藏 -> 取消收藏
                    showLoadingToast("正在取消收藏");
                    type = "1";
                } else {
                    showLoadingToast("正在添加收藏");
                    type = "0";
                }
                final String finalType = type;
                SPPersonRequest.collectOrCancelGoodsWithID(goodsId, type, new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {

                        hideLoadingToast();
                        if(finalType.equals("1")){
                            setIs_collect("0");
                        }else{
                            setIs_collect("1");
                        }
                        refreshCollectButton(getIs_collect());
                        showToast(msg);
                    }
                }, new SPFailuredListener(ProductActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });
                break;
            case R.id.add_cart_btn:
            case R.id.buy_btn:
                //购买 加入购物车
                if (SSUtils.isEmpty(this.getSpecs())) {
                    this.getSelectSpecMap();
                    priceJson = MobileApplication.getInstance().json;
                    specJson = MobileApplication.getInstance().json1;
                    //获取每组规格中的第一个规格
                    selectSpecMap = new HashMap<String, String>();
                    Iterator<String> iterator = specJson.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        List<SPProductSpec> specs = null;
                        try {
                            specs = (List<SPProductSpec>) specJson.get(key);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (specs != null && specs.size() > 0) {
                            SPProductSpec productSpec = specs.get(0);
                            selectSpecMap.put(key, productSpec.getItemID());
                        }
                    }
                } else {
                    specs = this.getSpecs();
                }
                //加入购物车
//                Integer count = Integer.valueOf(cartCountEtxtv.getText().toString().trim());
                if (mCartCount < 1) {
                    showToast(getString(R.string.toast_not_datal));
                    return;
                }
//                规格
                if (SSUtils.isEmpty(this.getSpecs()) && selectSpecMap.values().size() > 0) {
                    specs = SPStringUtils.collectToString(getSelectSpecMap().values(), ",");
                }
                ShopRequest.shopCartGoodsOperation(mProduct.getGoodsID(), specs, mCartCount, new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        if (response != null) {
                            setCartNumber(response.toString());
                            showToast(getString(R.string.toast_shopcart_action_success));
                            //                        //立即购买 先添加到购物车 在调转的购物车列表
                        if (v.getId() == R.id.buy_btn) {
                            gotoShopcart();
                        }
                        }
                    }
                }, new SPFailuredListener((IViewController) mContext) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                    }
                });

                break;
            case R.id.product_cart_rlayout:
                //				购物车
                Intent carIntent = new Intent(mContext, ShopCartActivity.class);
                startActivity(carIntent);
                break;
        }
    }
//    添加购物车
    private void addShopCar() {
        ShopRequest.shopCartGoodsOperation(mProduct.getGoodsID(), specs, mCartCount, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    setCartNumber(response.toString());
                    showToast(getString(R.string.toast_shopcart_action_success));
                }
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    @Override
    public void onTagClick(TagView tagView, Tag tag) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        this.unregisterReceiver(mShopCartChangeReceiver);
    }

    public void gotoShopcart() {
        //进入购物车
        Intent shopcartIntetn = new Intent(this, ShopCartActivity.class);
        startActivity(shopcartIntetn);
    }
    //广播接收器  接收购物车的变化
    class ShopCartChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MobileConstants.ACTION_SHOPCART_CHNAGE)) {
//                loadCartCount();
            }
        }
    }
}
