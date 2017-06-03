package com.shssjk.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.CategoryRightAdapter;
import com.shssjk.adapter.ShopCategoryAdapter;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.category.SPCategoryRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPCategory;

import com.shssjk.model.shop.Data;
import com.shssjk.utils.SMobileLog;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;

/**
 * 商品分类
 */
public class CategoryActivity extends BaseActivity {
    private String TAG = "CategoryActivity";
    private RecyclerView mRecyclerView ;

    /**
     * 分类数据集合
     */
    private List<SPCategory> mLeftCategorys;
    private List<SPCategory> mRightCategorys;
    private StickyGridHeadersGridView mRightGdv;    //右边小分类listview


    CategoryRightAdapter mRightAdapter;
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题
    Context mContext;

    private HashMap<Integer, List<SPCategory>> mRightCategorysCache;
    private int category=844; // 读取第一个分类
    ShopCategoryAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_category));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mRightCategorysCache = new HashMap<Integer, List<SPCategory>>();
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {

        backBtn = (Button) findViewById(R.id.titlebar_back_btn);
        mRecyclerView   = (RecyclerView)findViewById(R.id.category_listv);

        mAdapter  = new ShopCategoryAdapter(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {

//        selectLeftCategory(category);
//        getFirstCategory();

        getCategoryShop();

    }

    @Override
    public void initEvent() {

//        mRightGdv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                SPCategory category = (SPCategory) mRightAdapter.getItem(position);
//                if (category.isBlank()) {
//                    return;
//                }
//                startupActivity(category);
//            }
//        });
    }

    /**
     * @return void    返回类型
     * @throws
     * @Description: 左边分类点击
     */
    public void selectLeftCategory(int category) {
        if (category == 0) return;
//        mLeftCategory = category;
        /** 先从缓存获取, 如果缓存中已经存在, 则不从服务器拉取数据 */
        if (mRightCategorysCache != null && mRightCategorysCache.get(category) != null) {
            List<SPCategory> cacheCategorys = mRightCategorysCache.get(category);
            mRightAdapter.setData(cacheCategorys);
            mRightAdapter.notifyDataSetChanged();
            return;
        }
        //showLoadingToast();
        SPCategoryRequest.goodsSecAndThirdCategoryList(category, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    mRightCategorys = (List<SPCategory>) response;
                    thirdCategoryDateChange(mRightCategorys);
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    public void thirdCategoryDateChange(List<SPCategory> thirdCategory) {
        mRightCategorys = thirdCategory;
        if (mRightCategorys != null) {
            mRightAdapter.setData(mRightCategorys);
            /** 以category id 为key  ,  mRightCategorys 为值, 缓存每一个大分类的子分类 */
            mRightCategorysCache.put(category, mRightCategorys);
            mRightAdapter.notifyDataSetChanged();
        }
    }
    public void startupActivity(SPCategory category){
        Intent intent = new Intent(mContext , ProductListActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    public void getFirstCategory(){
        //获取一级分类
        SPCategoryRequest.getCategory(0, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {

                if (response != null) {
                    List<SPCategory> categorys = (List<SPCategory>) response;
                    MobileApplication.getInstance().setTopCategorys(categorys);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SMobileLog.e(TAG, "getAllCategory FailuredListener :" + msg);
            }
        });

    }


    public void getCategoryShop(){
        //分类
        ShopRequest.getShopCategory(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                mDataJson = (JSONObject) response;
                try {
                    if (mDataJson.has("Categories")) {
                        List<Data> Categories = (List<Data>) mDataJson.get("Categories");
                        dealModels(Categories);
                    }

                } catch (Exception e) {
                    showToast(e.getMessage());
                    e.printStackTrace();
                }


                if (response != null) {


                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SMobileLog.e(TAG, "getAllCategory FailuredListener :" + msg);
            }
        });

    }

    private void dealModels(List<Data> categories) {
        mAdapter.setData(categories);
    }


}
