package com.shssjk.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.model.person.TeamData;
import com.shssjk.adapter.MyTeamAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPUserRequest;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的团队
 */
public class MyTeamActivity extends BaseActivity {

    @Bind(R.id.order_listv)
    ListView orderListv;
//    @Bind(R.id.test_list_view_frame)
//    PtrClassicFrameLayout testListViewFrame;
    @Bind(R.id.empty_txtv)
    TextView emptyTxtv;
    @Bind(R.id.empty_lstv)
    RelativeLayout emptyLstv;
    @Bind(R.id.main_layout)
    LinearLayout mainLayout;
    MyTeamAdapter myTeamAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.setting_team));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        ButterKnife.bind(this);
        super.init();
    }

    @Override
    public void initSubViews() {
        View emptyView = findViewById(R.id.empty_lstv);
        orderListv.setEmptyView(emptyView);
    }

    @Override
    public void initData() {
        getTeamData();
    }

    private void getTeamData() {

        showLoadingToast("正在加载数据...");
        SPUserRequest.getMyTeamList(
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            List<TeamData> mTeamList = (List<TeamData>) response;
                            myTeamAdapter.setData(mTeamList);
                        }
                    }
                }, new SPFailuredListener(MyTeamActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        if("空数据".equals(msg)){
//                            showToast("");
                        }else{
                            showToast(msg);
                        }

                    }
                });
    }

    @Override
    public void initEvent() {
        myTeamAdapter = new MyTeamAdapter(this);

        orderListv.setAdapter(myTeamAdapter);
    }


}
