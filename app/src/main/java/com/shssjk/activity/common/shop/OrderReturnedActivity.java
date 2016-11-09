package com.shssjk.activity.common.shop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;

/**
 * 个人中心  退货
 */
public class OrderReturnedActivity extends BaseActivity {
    private ListView orderReturnedListv;
    private PtrClassicFrameLayout orderReturnedPcf;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_order_returned));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_returned);
        mContext=this;
        super.init();
    }
    @Override
    public void initSubViews() {
        orderReturnedListv = (ListView) findViewById(R.id.person_orderreturned_listv);
        orderReturnedPcf = (PtrClassicFrameLayout) findViewById(R.id.orderreturned_list_view_ptr);
        View emptyView = findViewById(R.id.empty_lstv);
        orderReturnedListv.setEmptyView(emptyView);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
