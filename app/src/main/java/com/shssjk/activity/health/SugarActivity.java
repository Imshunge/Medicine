package com.shssjk.activity.health;

import android.os.Bundle;

import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.R;
/**
 * 健康云 血糖在xml 中， Fragment xml 中配置
 */
public class SugarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "血糖");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar);
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
