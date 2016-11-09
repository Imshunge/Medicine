package com.shssjk.activity.common.user;

import android.os.Bundle;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;

/**
 * 我的团队
 */
public class MyTeamActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.setting_team));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        super.init();
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
