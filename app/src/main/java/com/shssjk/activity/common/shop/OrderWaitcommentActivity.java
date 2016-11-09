package com.shssjk.activity.common.shop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;

/**
 * 个人中心 待评价
 */
public class OrderWaitcommentActivity extends BaseActivity {
    private ListView waitcommentListv;
    private PtrClassicFrameLayout waitcommentPcf;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_order_waitcomment));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_waitcomment);
        mContext=this;
        super.init();
    }

    @Override
    public void initSubViews() {
        waitcommentListv = (ListView) findViewById(R.id.person_orderreturned_listv);
        waitcommentPcf = (PtrClassicFrameLayout) findViewById(R.id.waitcomment_list_view_ptr);

        View emptyView = findViewById(R.id.empty_lstv);
        waitcommentListv.setEmptyView(emptyView);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
