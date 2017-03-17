package com.shssjk.activity.user;

import android.os.Bundle;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.model.Push;
import com.shssjk.utils.SSUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
//推送消息详情

public class PushMessageDetailActivity extends BaseActivity {

    @Bind(R.id.tv_source)
    TextView tvItemArticleTitle;
    @Bind(R.id.tv_date)
    TextView tvItemHotContent;
    @Bind(R.id.tv_data)
    TextView tvData;
    private Push push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.setting_message_detail));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_message_detail);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void initSubViews() {
        if (getIntent() == null || getIntent().getSerializableExtra("push") == null) {
            showToast(getString(R.string.toast_no_data));
            this.finish();
            return;
        }
        push = (Push) getIntent().getSerializableExtra("push");
        showData(push);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    public void showData(Push push) {
        if (!SSUtils.isEmpty(push.getTitle())) {
            tvItemArticleTitle.setText(push.getTitle());
        }
        if (!SSUtils.isEmpty(push.getDescription())) {
            tvItemHotContent.setText(push.getDescription());
        }
        if (!SSUtils.isEmpty(push.getPushTime())) {
            tvData.setText(push.getPushTime());
        }
    }
}
