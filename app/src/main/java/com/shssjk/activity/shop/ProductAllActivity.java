package com.shssjk.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.R;
import com.shssjk.adapter.ProductDetailCommentAdapter;
import com.shssjk.adapter.ProductTabAdapter;
import com.shssjk.adapter.SPProductSpecListAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.condition.ProductCondition;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;
import com.shssjk.model.shop.GoodsComment;
import com.shssjk.model.shop.SPProductSpec;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SPDialogUtils;
import com.shssjk.utils.SPShopUtils;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.LoadListView;
import com.shssjk.view.SPPageView;
import com.shssjk.view.ScrollBottomScrollView;
import com.shssjk.view.tagview.Tag;
import com.shssjk.view.tagview.TagListView;
import com.shssjk.view.tagview.TagView;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品详情
 */
public class ProductAllActivity extends BaseActivity implements TagListView.OnTagClickListener,
        SPPageView.PageListener, ScrollBottomScrollView.ScrollBottomListener, LoadListView.ILoadListener {
    @Bind(R.id.banner_lyaout)
    LinearLayout bannerLyaout;
    @Bind(R.id.banner_slayout)
    SPPageView bannerSlayout;
    @Bind(R.id.pageindex_txtv)
    TextView pageindexTxtv;
    @Bind(R.id.details_name_txtv)
    TextView detailsNameTxtv;
    @Bind(R.id.details_now_price_txtv)
    TextView detailsNowPriceTxtv;
    @Bind(R.id.details_orignal_price_txtv)
    TextView detailsOrignalPriceTxtv;
    @Bind(R.id.product_spec_store_count_txtv)
    TextView productSpecStoreCountTxtv;
    @Bind(R.id.product_spec_cart_layout)
    LinearLayout productSpecCartLayout;
    @Bind(R.id.cart_minus_btn)
    Button cartMinusBtn;
    @Bind(R.id.cart_count_dtxtv)
    EditText cartCountDtxtv; //选择商品的数量
    @Bind(R.id.cart_plus_btn)
    Button cartPlusBtn;
    @Bind(R.id.product_spec_lstv)
    ListView productSpecLstv;
    @Bind(R.id.product_comment_listv)
    LoadListView commentListv;
    @Bind(R.id.ll_product)
    LinearLayout llProduct;
    //    @Bind(R.id.magic_indicator)
//    MagicIndicator magicIndicator;
//    @Bind(R.id.view_pager)
//    ViewPager viewPager;
//    @Bind(R.id.detail_scrollv)
//    ScrollView detailScrollv;
    @Bind(R.id.product_home_txtv)
    TextView productHomeTxtv;
    @Bind(R.id.product_cart_imgv)
    ImageView productCartImgv;
    @Bind(R.id.productcart_count)
    TextView productcartCount;
    @Bind(R.id.product_cart_rlayout)
    RelativeLayout productCartRlayout;
    @Bind(R.id.product_like_imgv)
    ImageView productLikeImgv;
    @Bind(R.id.product_like_txtv)
    TextView productLikeTxtv;
    @Bind(R.id.like_lyaout)
    LinearLayout likeLyaout;
    @Bind(R.id.add_cart_btn)
    Button addCartBtn;
    @Bind(R.id.buy_btn)
    Button buyBtn;
    @Bind(R.id.bottom)
    LinearLayout bottom;
    @Bind(R.id.common_webview)
    WebView mWebView;
    @Bind(R.id.detail_scrollv)
    ScrollBottomScrollView detailScrollv;
    @Bind(R.id.tab_separate_imgv)
    ImageView tabSeparateImgv;
    @Bind(R.id.rbtn_detail)
    RadioButton rbtnDetail;
    @Bind(R.id.rbtn_comment)
    RadioButton rbtnCommemt;
    @Bind(R.id.radioGroup)
    RadioGroup mRadioGroup;
    RadioButton mCurrRb;
    RadioButton mLastRb;
    @Bind(R.id.empty_txtv)
    TextView emptyTxtv;
    @Bind(R.id.empty_lstv)
    RelativeLayout emptyLstv;
    @Bind(R.id.product_home_imgv)
    ImageView productHomeImgv;
    @Bind(R.id.product_isinventories)
    TextView productIsinventories;

    @Bind(R.id.ll_buy_bottom)
    LinearLayout ll_buy_bottom;
    @Bind(R.id.buy_points_btn)
    Button buyPointsBtn;

    @Bind(R.id.rl_number_change)
    RelativeLayout rl_number_change;


    private String mGoodsID;
    private int gallerySize;//预览图数量
    private int galleryIndex;//预览图大小
    private int mCommentCount;//商品评论数量
    private String contents;   //图文详情
    private boolean isFristLoad;//首次加载
    //全部数据
    private SPProduct mProduct;
    private JSONArray mGalleryArray;
    private JSONObject priceJson;    //规格属性对于的价格
    private List<GoodsComment> mComments;
    private JSONObject specJson;//Map<String , List<SPProductSpec>>
    private String is_collect = "";
    private int shopCount;
//    private List<SPProductSpec> productSpecArr=new ArrayList<>();

    public Map<String, String> getSelectSpecMap() {
        return selectSpecMap;
    }

    public void setSelectSpecMap(Map<String, String> selectSpecMap) {
        this.selectSpecMap = selectSpecMap;
    }

    private Map<String, String> selectSpecMap;//保存选择的规格ID
    private String currShopPrice;//当前商品价格
    private Context mContext;
    private boolean isGoToLogin = false; //没登陆时去登录 记录状态
    private FragmentPagerAdapter fragPagerAdapter;
    private ProductTabAdapter productTabAdapter;
    SPProductSpecListAdapter specAdapter;  //产品规格
    int mCartCount = 1;    //商品数量显示
    public static String[] productDetailInnerTitles = new String[]{"详情", "评价"};
    List<String> mDataList = Arrays.asList(productDetailInnerTitles);
    ProductDetailCommentAdapter mAdapter;
    int currentIndex = 0;//记录当前选择的是页签 还是详情

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    private String specs = null;   //商品规格
    int pageIndex;   //当前第几页:从1开始
    /**
     * 最大页数
     */
    boolean maxIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.activity_product_title));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_all);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }

    public void initSubViews() {
        this.mGoodsID = getIntent().getStringExtra("goodsId");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.e(this, "ProductAllActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGoToLogin) {
            getProductDetails();
        }
        if (!MobileApplication.getInstance().isLogined) {
            getShopCarNum();
        }
    }
    @Override
    public void initData() {
        selectSpecMap = new HashMap<String, String>();
        getProductDetails();
        refreshData();
    }
    @Override
    public void initEvent() {
        commentListv.setVisibility(View.GONE);
        commentListv.setInterface(this);
        bannerSlayout.setPageListener(this);
        detailScrollv.setScrollBottomListener(this);
        specAdapter = new SPProductSpecListAdapter(mContext, this);
        productSpecLstv.setAdapter(specAdapter);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setBuiltInZoomControls(false);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mAdapter = new ProductDetailCommentAdapter(this);
        commentListv.setAdapter(mAdapter);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int key) {
                switch (key) {
                    case R.id.rbtn_detail:
//                        setSelectIndex(2);
                        commentListv.setVisibility(View.GONE);
                        mWebView.setVisibility(View.VISIBLE);
                        rbtnCommemt.setTextColor(getResources().getColor(R.color.color_tab_item_normal));
                        rbtnDetail.setTextColor(getResources().getColor(R.color.color_tab_item_fous));
                        Logger.e("rbtn_home", "rbtn_home");
                        break;
                    case R.id.rbtn_comment:
                        commentListv.setVisibility(View.VISIBLE);
                        mWebView.setVisibility(View.GONE);
                        Logger.e("rbtn_category", "rbtn_category");
                        rbtnDetail.setTextColor(getResources().getColor(R.color.color_tab_item_normal));
                        rbtnCommemt.setTextColor(getResources().getColor(R.color.color_tab_item_fous));

                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void setSelectIndex(int index) {
        switch (index) {
            case 1:
                changeTabtextSelector(rbtnCommemt);
                currentIndex = 1;
                break;
            case 2:
                changeTabtextSelector(rbtnDetail);
                currentIndex = 2;
                break;
        }
    }

    public void changeTabtextSelector(RadioButton rb) {
        mLastRb = mCurrRb;
        mCurrRb = rb;
        if (mLastRb != null) {
            mLastRb.setTextColor(getResources().getColor(R.color.color_tab_item_normal));
//            mLastRb.setSelected(false);
        }
        if (mCurrRb != null) {
            mCurrRb.setTextColor(getResources().getColor(R.color.color_tab_item_fous));
//            mCurrRb.setChecked(true);
        }
    }

    @OnClick({R.id.add_cart_btn, R.id.buy_btn, R.id.cart_minus_btn, R.id.cart_plus_btn,
            R.id.product_cart_rlayout, R.id.like_lyaout,R.id.buy_points_btn})
    public void onClick(final View view) {
        if (!checkLogin()) {
            isGoToLogin = true;
            return;
        }
        switch (view.getId()) {
            case R.id.cart_minus_btn:
                if (mCartCount <= 1) {
                    showToast(getString(R.string.toast_count_not_small_zero));
                    return;
                }
                mCartCount--;
                cartCountDtxtv.setText(String.valueOf(mCartCount));
                break;
            case R.id.cart_plus_btn:
                int storeCount = SPShopUtils.getShopStoreCount(priceJson, selectSpecMap.values());
                if (mCartCount >= storeCount) {
                    showToast(getString(R.string.toast_low_stocks));
                    return;
                }
                mCartCount++;
                cartCountDtxtv.setText(String.valueOf(mCartCount));
                break;
            case R.id.product_cart_rlayout:
                //				购物车
                Intent carIntent = new Intent(mContext, ShopCartActivity.class);
                startActivity(carIntent);
                break;
            case R.id.add_cart_btn:
            case R.id.buy_btn:
                //购买 加入购物车
                storeCount = SPShopUtils.getShopStoreCount(priceJson, selectSpecMap.values());
                if (mCartCount > storeCount) {
                    showToast(getString(R.string.toast_low_stocks));
                    return;
                }
                addShopCar(view);
                break;
            case   R.id.buy_points_btn:
                String spec_key = "";
                if(mProduct.getSpecArr().size()>0){
                    spec_key=mProduct.getSpecArr().get(0).getItemID();
                }
                Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
                intent.putExtra("goodsId", mProduct.getGoodsID());
                intent.putExtra("sumFee", 0.00);
                intent.putExtra("spec_key",spec_key);
                startActivity(intent);
                break;

            case R.id.like_lyaout:
                String type = "1";
                if (is_collect.equals("1")) {//收藏 -> 取消收藏
                    showLoadingToast("正在取消收藏");
                    type = "1";
                } else {
                    showLoadingToast("正在添加收藏");
                    type = "0";
                }
                final String finalType = type;
                PersonRequest.collectOrCancelGoodsWithID(mGoodsID, type, new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (finalType.equals("1")) {
                            is_collect = "0";
                        } else {
                            is_collect = "1";
                        }
                        refreshCollectButton(is_collect);
                        showToast(msg);
                    }
                }, new SPFailuredListener(ProductAllActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });
                break;
        }
    }

    private void addShopCar(final View view) {
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
//                            setCartNumber(response.toString());
                    productcartCount.setText(response.toString());
                    showToast(getString(R.string.toast_shopcart_action_success));
                    //                        //立即购买 先添加到购物车 在调转的购物车列表
                    if (view.getId() == R.id.buy_btn) {
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
    }

    public void getProductDetails() {
        /** 此处参数-1 : 意味着返回的是左边分类  */
        ProductCondition condition = new ProductCondition();
        if (mGoodsID == null) {
            condition.goodsID = -1;
        } else {
            condition.goodsID = Integer.valueOf(mGoodsID);
        }
        showLoadingToast();
        ShopRequest.getProductByID(condition, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                try {
                    mDataJson = (JSONObject) response;
                    if (mDataJson != null && mDataJson.has("product")) {
                        mProduct = (SPProduct) mDataJson.get("product");
                        //          设置图文详情信息
                        setContents(mProduct.getGoodsContent());
                        loadData(mProduct.getGoodsContent());
                        showBottomeLayout(mProduct);
//                        productSpecArr = SPJsonUtil.fromJsonArrayToList(mProduct.getSpecArr(), SPProductSpec.class);
                    }
                    if (mDataJson != null && mDataJson.has("gallery")) {
                        mGalleryArray = mDataJson.getJSONArray("gallery");
                    }
                    if (mDataJson != null && mDataJson.has("price")) {
                        priceJson = mDataJson.getJSONObject("price");
                        MobileApplication.getInstance().json = priceJson;
                    }
//                   评论
                    if (mDataJson != null && mDataJson.has("comments")) {
                        mComments = (List<GoodsComment>) mDataJson.get("comments");
//                        mCommentCount = mComments.size();
//                        mAdapter.setData(mComments);
                    }
                    if (mDataJson != null && mDataJson.has("cart_num")) {
                        String cart_num = mDataJson.getString("cart_num");
                        productcartCount.setText(cart_num);
                        Logger.e(this, "cart_num" + cart_num);
                    }
                    if (mDataJson != null && mDataJson.has("is_collect")) {
                        is_collect = mDataJson.getString("is_collect");
                        Logger.e(this, "is_collect" + is_collect);
                        refreshCollectButton(is_collect);
                    }
                    if (mDataJson != null && mDataJson.has("cart_num")) {
                        String cart_num = mDataJson.getString("cart_num");
                        productcartCount.setText(cart_num);
                        Logger.e(this, "cart_num" + cart_num);
                    }
                   dealModel();
                } catch (Exception e) {
                    showToast(e.getMessage());
                }
                onDataLoadFinish();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                SPDialogUtils.showToast(mContext, msg);
            }
        });
    }
// 积分商品 和普通商品
    private void showBottomeLayout(SPProduct mProduct) {
        if(MobileConstants.POINT_ID.equals(mProduct.getCategoryID())){
            ll_buy_bottom.setVisibility(View.GONE);
            buyPointsBtn.setVisibility(View.VISIBLE);
            rl_number_change.setVisibility(View.GONE);
            productSpecStoreCountTxtv.setText(getString(R.string.stock));
            detailsNowPriceTxtv.setText(mProduct.getMarketPrice() + "  积分");
            detailsOrignalPriceTxtv.setVisibility(View.INVISIBLE);
        }else{
            ll_buy_bottom.setVisibility(View.VISIBLE);
            buyPointsBtn.setVisibility(View.GONE);
            rl_number_change.setVisibility(View.VISIBLE);
            productSpecStoreCountTxtv.setText(getString(R.string.count));
            detailsOrignalPriceTxtv.setVisibility(View.VISIBLE);
            detailsNowPriceTxtv.setVisibility(View.VISIBLE);
            detailsOrignalPriceTxtv.setText("原价：￥" + mProduct.getMarketPrice());
            detailsNowPriceTxtv.setText("现价：¥" + mProduct.getShopPrice());
            detailsOrignalPriceTxtv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
    public void onDataLoadFinish() {
        refreshGalleryViewData();
        if (mProduct != null) {
            detailsNameTxtv.setText(mProduct.getGoodsName());
//            detailsOrignalPriceTxtv.setText("原价：￥" + mProduct.getMarketPrice());
//            detailsNowPriceTxtv.setText("现价：¥" + mProduct.getShopPrice());
//            detailsOrignalPriceTxtv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        int storeCount = SPShopUtils.getShopStoreCount(priceJson, selectSpecMap.values());
        if (storeCount == 0) {
            productIsinventories.setText("无货");
        }
    }
    private void refreshGalleryViewData() {
        String tIndex = (galleryIndex + 1) + "/" + gallerySize;
        this.pageindexTxtv.setText(tIndex);
    }

    public void dealModel() {
        List<String> gallerys = new ArrayList<String>();
        if (mGalleryArray != null) {
            try {
                for (int i = 0; i < mGalleryArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) mGalleryArray.getJSONObject(i);
                    String url = jsonObject.getString("image_url");
                    gallerys.add(url);
                    if (bannerSlayout != null) {
                        buildProductGallery(gallerys);
                        gallerySize = gallerys.size();
                        galleryIndex = 0;
                        refreshGalleryViewData();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dealProductSpec();
    }

    private void buildProductGallery(List<String> gallerys) {
        if (gallerys == null || gallerys.size() < 1) return;
        bannerLyaout.removeAllViews();
        int productImageWidth = Float.valueOf(getResources().getDimension(R.dimen.dp_300)).intValue();
        DisplayMetrics displayMetrics = MobileApplication.getInstance().getDisplayMetrics();

        for (int i = 0; i < gallerys.size(); i++) {
            String url = gallerys.get(i);
            ImageView imageView = new ImageView(mContext);
//            imageView.set
//            imageView.
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(displayMetrics.widthPixels, productImageWidth);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
//            mGallery.addView(imageView);
            bannerSlayout.addPage(imageView);
        }
    }

    /**
     * 处理商品规格
     * 1. 将每个商品根据 specName 为key 分组
     * 2. 把每组规格中的第一个规格设置为默认规格
     * 3. 根据默规格, 查询商品当前的价格
     */
    public void dealProductSpec() {
        specJson = new JSONObject();//清理之前的缓存数据
        try {
            if (mProduct != null) {
                if (mProduct.getSpecArr() != null && mProduct.getSpecArr().size() > 0) {
                    //排序
                    Collections.sort(mProduct.getSpecArr());
                    //循环获取商品规格
                    //并将商品规格以specName为key进行分类
                    //specName相同为一组
                    for (SPProductSpec productSpec : mProduct.getSpecArr()) {
                        List<SPProductSpec> specList = null;
                        if (specJson.has(productSpec.getSpecName())) {
                            specList = (List<SPProductSpec>) specJson.get(productSpec.getSpecName());
                            if (!(specList.contains(productSpec))) {
                                specList.add(productSpec);
                            }
                        } else {
                            specList = new ArrayList<SPProductSpec>();
                            specList.add(productSpec);
                        }
                        specJson.put(productSpec.getSpecName(), specList);
                        MobileApplication.getInstance().json1 = specJson;
                        specAdapter.setData(specJson);
                    }
                }
            }
            //获取每组规格中的第一个规格
            if (!selectSpecMap.isEmpty()) {
                selectSpecMap.clear();
            }
            Iterator<String> iterator = specJson.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                List<SPProductSpec> specs = (List<SPProductSpec>) specJson.get(key);
                if (specs != null && specs.size() > 0) {
                    SPProductSpec productSpec = specs.get(0);
                    selectSpecMap.put(key, productSpec.getItemID());
                    specAdapter.setData(selectSpecMap.values());
                    MobileApplication.getInstance().map = selectSpecMap;
                }
            }
            refreshPriceView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshPriceView() {
        if (selectSpecMap != null || selectSpecMap.size() > 0) {
            currShopPrice = SPShopUtils.getShopprice(priceJson, selectSpecMap.values());
            if (SPStringUtils.isEmpty(currShopPrice)) {
                currShopPrice = mProduct.getShopPrice();
            }
//            this.detailsNowPriceTxtv.setText("现价: ¥" + currShopPrice);
        }
    }

    //   规格单击
    @Override
    public void onTagClick(TagView tagView, Tag tag) {
        setSpecs(tag.getValue());
    }

    public void gotoShopcart() {
        //进入购物车
        Intent shopcartIntetn = new Intent(this, ShopCartActivity.class);
        startActivity(shopcartIntetn);
        finish();
    }
    //  产品图片点击事件
    @Override
    public void page(int page) {
        galleryIndex = page;
        refreshGalleryViewData();
    }

    //     is_collect  1收藏  0 未收藏
    public void refreshCollectButton(String is_collect) {
        Logger.e(this, "is_collect" + is_collect);
        if (is_collect != null && "1".equals(is_collect.trim())) {
            //收藏
            productLikeImgv.setImageResource(R.drawable.product_like);
            productLikeTxtv.setText(getString(R.string.product_details_like));
        } else {
            //未收藏
            productLikeImgv.setImageResource(R.drawable.product_star);
            productLikeTxtv.setText(getString(R.string.product_details_unlike));
        }
    }

    public void loadData(String mHtml) {
        if (mWebView != null) {
            String htmlData = mHtml + "<head><style>img{max-width:100%;height:auto;clear:both;" +
                    "display:block;margin:auto;} p{font-size:13;}</style></head>";
            mWebView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
        }
    }

    boolean checkLogin() {
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return false;
        }
        return true;
    }

    @Override
    public void scrollBottom() {
//        showToast("scrollBottom");
        if (currentIndex == 2) {
            onLoad();
        }
    }

    @Override
    public void onLoad() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取更多数据
//                getLoadData();
                //更新listview显示；
//                showListView(apk_list);
                //通知listview加载完毕
                loadMoreData();
                commentListv.loadComplete();
            }
        }, 2000);
    }

    public void loadMoreData() {
        if (maxIndex) {
//            showToast("没有更多数据了");
            return;
        }
        pageIndex++;
        //showLoadingToast();
        ShopRequest.getGoodsCommentWithGoodsID(mGoodsID, pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<GoodsComment> tempComment = (List<GoodsComment>) response;
                    if (tempComment.size() > 0) {
                        mComments.addAll(tempComment);
                        //更新收藏数据
                        mAdapter.setData(mComments);
                    } else {
                        pageIndex--;
                        maxIndex = true;
                    }
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
                pageIndex--;
            }
        });
    }

    public void refreshData() {
        pageIndex = 1;
        maxIndex = false;
        //showLoadingToast();
        ShopRequest.getGoodsCommentWithGoodsID(mGoodsID, pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    mComments = (List<GoodsComment>) response;
                    //更新收藏数据
                    mAdapter.setData(mComments);
                } else {
                    maxIndex = true;
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                hideLoadingToast();
            }
        });
    }

    public void getShopCarNum() {
        shopCount = 0;
        //hasFirstStartup = NO;
        ShopRequest.getShopCartNumber(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                shopCount = Integer.valueOf(response.toString());
                productcartCount.setText(shopCount + "");
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
            }
        });
    }


}
