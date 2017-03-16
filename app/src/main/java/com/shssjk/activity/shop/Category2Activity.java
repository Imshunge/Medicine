package com.shssjk.activity.shop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.CategoryExpandableListViewAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.shop.Data;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商品分类
 */
public class Category2Activity extends BaseActivity {
    @Bind(R.id.expandableList)
    ExpandableListView expandableList;
    private String TAG = "CategoryActivity";
    /**
     * 分类数据集合
     */
    Context mContext;
    private CategoryExpandableListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_category));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }
    @Override
    public void initSubViews() {
    }
    @Override
    public void initData() {
        getCategoryShop();
    }
    @Override
    public void initEvent() {
    }
    public void getCategoryShop() {
        //分类
        ShopRequest.getShopCategory(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                JSONObject mDataJson = (JSONObject) response;
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
                showToast(msg);
            }
        });
    }

    private void dealModels(List<Data> categories) {
        adapter = new CategoryExpandableListViewAdapter(Category2Activity.this,categories
        );
        expandableList.setAdapter(adapter);
        // 隐藏分组指示器
        expandableList.setGroupIndicator(null);
        // 展开所以
//        expandableListView.expandGroup(0);
        int groupCount = expandableList.getCount();
        for (int i=0; i<groupCount; i++) {
            expandableList.expandGroup(i);
        };
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
    }
}
