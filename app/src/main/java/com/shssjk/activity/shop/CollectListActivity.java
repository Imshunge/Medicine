package com.shssjk.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.CollectListAdapter;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.shop.SPCollect;

import java.util.List;

/**
 *  商品收藏
 */
public class CollectListActivity extends BaseActivity {


    private String TAG = "CollectListActivity";

    ListView productListv;

    CollectListAdapter mAdapter ;
    List<SPCollect> mCollects ;

    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true,true,getString(R.string.title_collect));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spcollect_list);
        super.init();
    }
//    initSubViews();
//    initEvent();
//    initData();
    @Override
    public void initSubViews() {
//        标题颜色
        titlbarFl= (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title));
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this, R.color.white));
        productListv = (ListView) findViewById(R.id.product_listv);
    }

    @Override
    public void initData() {
        mAdapter = new CollectListAdapter(this);
        productListv.setAdapter(mAdapter);
        showLoadingToast();
        SPPersonRequest.getGoodsCollectWithSuccess(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {

                if (response != null) {
                    mCollects = (List<SPCollect>) response;
                    MobileApplication.getInstance().collects = mCollects;
                    //更新收藏数据
                    mAdapter.setData(mCollects);
                }
                hideLoadingToast();
            }
        }, new SPFailuredListener(CollectListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                hideLoadingToast();
            }
        });
    }

    @Override
    public void initEvent() {
        productListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                SPCollect collect = (SPCollect)mAdapter.getItem(position);
                Intent intent = new Intent(CollectListActivity.this , ProductAllActivity.class);
                intent.putExtra("goodsId", collect.getGoodsID());
                CollectListActivity.this.startActivity(intent);
            }
        });
    }
}
