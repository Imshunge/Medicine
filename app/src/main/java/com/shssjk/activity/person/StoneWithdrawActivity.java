package com.shssjk.activity.person;
/**
 * 石头提现
 */
import android.os.Bundle;

import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.R;

public class StoneWithdrawActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_mystone_withdraw));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone_withdraw);
        super.init();
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        getBankList();
    }


    @Override
    public void initEvent() {

    }

    private void getBankList() {

    }

}
