package com.shssjk.activity.health;

import android.os.Bundle;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.R;

/**
 * 健康云 血压    在xml 中 android:name="com.shssjk.fragment.FragmentBlood"
 xml 中配置
 */
public class BloodActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "血压");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);
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
