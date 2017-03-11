package com.shssjk.activity.common.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.adapter.PushAdapter;
import com.shssjk.global.MobileApplication;
import com.shssjk.model.Push;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息通知
 */
public class MyMessageActivity extends BaseActivity {
    PushAdapter pushAdapter;
    @Bind(R.id.product_listv)
    ListView productListv;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.setting_message));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        ButterKnife.bind(this);
        mContext=this;
        super.init();
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        getPushData();
    }

    private void getPushData() {
        String uid="";
        if (MobileApplication.getInstance().isLogined){
            uid =MobileApplication.getInstance().getLoginUser().getUserID();
        }
        List<Push> pushs = DataSupport.select()
//                .where("userId > ?", uid)
                .order("id desc")
                .find(Push.class);
//        List<Push> pushs = DataSupport.findAll(Push.class);
//        for (Push push : pushs) {
//            Logger.e("MainActivity", push.getId() + "");
//            Logger.e("MainActivity", push.getTitle());
//            Logger.e("MainActivity", push.getCustomContentString());
//            Logger.e("MainActivity", push.getDescription());
//            Logger.e("MainActivity", push.getUserId());
//        }
        pushAdapter.setData(pushs);
    }

    @Override
    public void initEvent() {
        pushAdapter= new PushAdapter(mContext);
        productListv.setAdapter(pushAdapter);

        productListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Push push = (Push) pushAdapter.getItem(position);
                Intent intent = new Intent(mContext, PushMessageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("push",push);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
