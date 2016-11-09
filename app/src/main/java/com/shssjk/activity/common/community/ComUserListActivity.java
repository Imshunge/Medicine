package com.shssjk.activity.common.community;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.adapter.ComUserAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.shssjk.model.community.ComUser;

import java.util.List;

/**
 * 用户列表
 */
public class ComUserListActivity extends BaseActivity implements ComUserAdapter.ImageBtnClickListener {
    private TextView titleTxtv; //标题
    private String cid;//门派id
    private String ishost;//是否为门主
    private List<ComUser> mComUserList;

    //江湖模块
    private ListView articlesListv;
    private PtrClassicFrameLayout articlesPcf;
    private Context mContext;
    private ComUserAdapter mComUserAdapter;
     private String  DATASIZE="7";   //每次获取数据条数

    /**
     * 最大页数
     */
    boolean maxIndex;
    private String ofseet = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, "成员列表");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_user_list);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        this.cid = getIntent().getStringExtra("cid");
        this.ishost = getIntent().getStringExtra("ishost");
        articlesListv = (ListView) findViewById(R.id.school_articl_listv);
        articlesPcf = (PtrClassicFrameLayout) findViewById(R.id.articl_list_view_ptr);

        View emptyView = findViewById(R.id.empty_lstv);
        articlesListv.setEmptyView(emptyView);
    }
    @Override
    public void initData() {
        getUserList();
    }

    @Override
    public void initEvent() {

        mComUserAdapter =
                new ComUserAdapter(mContext, ishost,this);
        articlesListv.setAdapter(mComUserAdapter);

        articlesPcf.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                getUserList();
            }
        });

        articlesPcf.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                //上拉加载更多
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        showLoadingToast("正在加载数据");
        String offset = mComUserList.size() + "";
        CommunityRequest.getComUserListWithCID(cid, offset, DATASIZE, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
//                        refreshCollectDatta();
                List<ComUser> tempComUserList = (List<ComUser>) response;
                mComUserList.addAll(tempComUserList);
                mComUserAdapter.setData(mComUserList);
                hideLoadingToast();
//                showToast(msg);
            }
        }, new SPFailuredListener(ComUserListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
        articlesPcf.refreshComplete();
    }

    private void getUserList() {
        showLoadingToast("正在加载数据");
        CommunityRequest.getComUserListWithCID(cid,ofseet, DATASIZE, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                mComUserList = (List<ComUser>) response;
                mComUserAdapter.setData(mComUserList);
                hideLoadingToast();
//                showToast(msg);
            }
        }, new SPFailuredListener(ComUserListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
        articlesPcf.refreshComplete();
    }
    @Override
    public void deleteOrAddBtnClick(ComUser comUser) {
        deleteUser(comUser,cid);
    }

    private void deleteUser(final ComUser comUser, String cid) {
        showLoadingToast("正在加载数据");
        CommunityRequest.deleteQuackUserWithID(cid, comUser.getUid(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (msg.equals("已删除")) {
                    mComUserList.remove(comUser);
                    mComUserAdapter.setData(mComUserList);
                }
                hideLoadingToast();
                showToast(msg);
            }
        }, new SPFailuredListener(ComUserListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
        articlesPcf.refreshComplete();
    }


}
