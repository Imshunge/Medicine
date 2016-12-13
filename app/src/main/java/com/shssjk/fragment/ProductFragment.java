
package com.shssjk.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.activity.common.shop.ProductActivity;
import com.shssjk.adapter.ProductAttrListAdapter;
import com.shssjk.adapter.SPProductSpecListAdapter;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.condition.ProductCondition;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;
import com.shssjk.model.shop.GoodsComment;
import com.shssjk.model.shop.SPProductSpec;
import com.shssjk.utils.SMobileLog;
import com.shssjk.utils.SPDialogUtils;
import com.shssjk.utils.SPShopUtils;
import com.shssjk.view.SPPageView;
import com.shssjk.view.tagview.Tag;
import com.shssjk.view.tagview.TagListView;
import com.shssjk.view.tagview.TagView;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 商品 -> 详情 1 详情
 */
public class ProductFragment extends BaseFragment implements View.OnClickListener, SPPageView.PageListener, TagListView.OnTagClickListener {


    SPPageView mScroll; // MobileScrollLayout mScroll ;
    LinearLayout mGallery;
    TextView pageindexTxtv;    //图片索引提示
    TextView nameTxtv;            //产品名称
    TextView keywordsTxtv;        //关键字描述
    TextView orignalPriceTxtv;    //原价
    TextView nowPriceTxtv;        //现价
    Button minusBtn;        //数量减
    Button plusBtn;       //数量加
    TextView storeCountTxtv;    //库存数量显示
    EditText cartCounEtxtv; //选择的数量
    int mCartCount;    //商品数量显示
    ProductAttrListAdapter mAttrListAdapter;
    private String TAG = "ProductFragment";
    private String mGoodsID;
    private int gallerySize;//预览图数量
    private int galleryIndex;//预览图大小
    private int mCommentCount;//商品评论数量
    //全部数据
    private SPProduct mProduct;
    private JSONArray mGalleryArray;
    private JSONObject priceJson;    //规格属性对于的价格
    private List<GoodsComment> mComments;
    private JSONObject specJson;//Map<String , List<SPProductSpec>>
    private Map<String, String> selectSpecMap;//保存选择的规格ID
    private String currShopPrice;//当前商品价格
    private Context mContext;
    private boolean isFirstLoad;
    SPProductSpecListAdapter specAdapter;  //产品规格
    ListView specListv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void setContent(String content) {
        this.mGoodsID = content;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isFirstLoad = true;
        View view = inflater.inflate(R.layout.product_details, null, false);
        super.init(view);
        return view;
    }

    public void loadData() {
        if (isFirstLoad) {
            isFirstLoad = false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void initSubView(View view) {
        mScroll = (SPPageView) view.findViewById(R.id.banner_slayout);
        mGallery = (LinearLayout) view.findViewById(R.id.banner_lyaout);
        pageindexTxtv = (TextView) view.findViewById(R.id.pageindex_txtv);
        nameTxtv = (TextView) view.findViewById(R.id.details_name_txtv);

        orignalPriceTxtv = (TextView) view.findViewById(R.id.details_orignal_price_txtv);
        nowPriceTxtv = (TextView) view.findViewById(R.id.details_now_price_txtv);

        minusBtn = (Button) view.findViewById(R.id.cart_minus_btn);
        plusBtn = (Button) view.findViewById(R.id.cart_plus_btn);
        storeCountTxtv = (TextView) view.findViewById(R.id.product_spec_store_count_txtv);
        cartCounEtxtv = (EditText) view.findViewById(R.id.cart_count_dtxtv);
        specListv = (ListView) view.findViewById(R.id.product_spec_lstv);

    }

    @Override
    public void initEvent() {
        minusBtn.setOnClickListener(this);
        plusBtn.setOnClickListener(this);
        mScroll.setPageListener(this);

        specAdapter = new SPProductSpecListAdapter(mContext, this);
        specListv.setAdapter(specAdapter);

    }

    @Override
    public void initData() {
        selectSpecMap = new HashMap<String,String>();

        getProductDetails();
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
                        ((ProductActivity) getActivity()).setmProduct(mProduct);
                        //                设置图文详情信息
                        ((ProductActivity) getActivity()).setContents(mProduct.getGoodsContent());
                    }

                    if (mDataJson != null && mDataJson.has("gallery")) {
                        mGalleryArray = mDataJson.getJSONArray("gallery");
                    }

                    if (mDataJson != null && mDataJson.has("price")) {
                        priceJson = mDataJson.getJSONObject("price");
                        MobileApplication.getInstance().json = priceJson;
                    }

                    if (mDataJson != null && mDataJson.has("comments")) {
                        mComments = (List<GoodsComment>) mDataJson.get("comments");
                        mCommentCount = mComments.size();
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
                SMobileLog.e(TAG, "onRespone ,msg : " + msg);
                SPDialogUtils.showToast(mContext, msg);
            }
        });
    }

    public void dealModel() {
        List<String> gallerys = new ArrayList<String>();
        if (mGalleryArray != null) {
            try {
                for (int i = 0; i < mGalleryArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) mGalleryArray.getJSONObject(i);
                    String url = jsonObject.getString("image_url");
                    gallerys.add(url);

                    if (mScroll != null) {
                        //mScroll.setDataSource(this , gallerys);
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
        mGallery.removeAllViews();
        int productImageWidth = Float.valueOf(getResources().getDimension(R.dimen.dp_300)).intValue();
        DisplayMetrics displayMetrics = MobileApplication.getInstance().getDisplayMetrics();

        for (int i = 0; i < gallerys.size(); i++) {
            String url = gallerys.get(i);
            ImageView imageView = new ImageView(mContext);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(displayMetrics.widthPixels, productImageWidth);
            imageView.setLayoutParams(lp);
            Glide.with(this).load(url).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            //mGallery.addView(imageView);
            mScroll.addPage(imageView);
        }
    }

    public void onDataLoadFinish() {

        refreshGalleryViewData();

//		String commentTxt = getString(R.string.product_details_comment) +"("+mCommentCount+")";
//		commentARView.setText(commentTxt);

        if (mProduct != null) {
            this.nameTxtv.setText(mProduct.getGoodsName());
            this.orignalPriceTxtv.setText("原价：￥" + mProduct.getMarketPrice());
            this.nowPriceTxtv.setText("现价：¥" + mProduct.getShopPrice());
            this.orignalPriceTxtv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            this.keywordsTxtv.setText(mProduct.getKeywords());
//			this.commentARView.setText("累计评价("+mProduct.getCommentCount()+")");

        }
    }

    private void refreshGalleryViewData() {
        String tIndex = (galleryIndex + 1) + "/" + gallerySize;
        this.pageindexTxtv.setText(tIndex);
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
            this.nowPriceTxtv.setText("现价: ¥" + currShopPrice);

        }
    }

    @Override
    public void onClick(View v) {
//        mCartCount = Integer.valueOf(cartCountEtxtv.getText().toString().trim());
        if (v.getId() == R.id.cart_minus_btn || v.getId() == R.id.cart_plus_btn) {
            if (v.getId() == R.id.cart_minus_btn) {
                if (mCartCount <= 1) {
                    showToast(getString(R.string.toast_count_not_small_zero));
                    return;
                }
                mCartCount--;
            } else {
                int storeCount = SPShopUtils.getShopStoreCount(priceJson, selectSpecMap.values());
                if (mCartCount > storeCount) {
                    showToast(getString(R.string.toast_low_stocks));
                    return;
                }
                mCartCount++;
            }
            cartCounEtxtv.setText(String.valueOf(mCartCount));
            ((ProductActivity) getActivity()).setmCartCount(mCartCount);
        }
    }

    @Override
    public void page(int page) {
        galleryIndex = page;
        refreshGalleryViewData();
    }

    @Override
    public void onTagClick(TagView tagView, Tag tag) {
        ((ProductActivity) getActivity()).setSpecs(tag.getValue());
    }
}
