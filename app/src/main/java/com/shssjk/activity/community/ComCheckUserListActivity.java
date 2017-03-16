package com.shssjk.activity.community;

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
import com.shssjk.activity.BaseActivity;
import com.shssjk.adapter.ComUserAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.shssjk.model.community.ComUser;

import java.util.List;

/**
 * 审核用户列表
 */
public class ComCheckUserListActivity extends BaseActivity implements ComUserAdapter.RefuseBtnClickListener,ComUserAdapter.ThroughBtnClickListener {
    private TextView titleTxtv; //标题
    private String cid;//门派id
    private List<ComUser> mComUserList;
    //
    private ListView articlesListv;
    private PtrClassicFrameLayout articlesPcf;
    private Context mContext;
    private ComUserAdapter mComUserAdapter;
    int pageIndex;   //当前第几页:从1开始
    private String  DATASIZE="7";   //每次获取数据条数

    /**
     * 最大页数
     */
    boolean maxIndex;
    private String ofseet = "0";
    private String offset;
    private String REFUSE="-1";
    private String THOUGH="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.jh_persons_check_list));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_user_list);
        mContext=this;
        super.init();
    }

    @Override
    public void initSubViews() {
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        this.cid =   getIntent().getStringExtra("cid");
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
                new ComUserAdapter(mContext,"2",this,this);
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

    private void getUserList() {
        CommunityRequest.getComCheckUserListWithCID(cid,
                ofseet, DATASIZE, new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
//                        refreshCollectDatta();
                        mComUserList = (List<ComUser>) response;
                        mComUserAdapter.setData(mComUserList);
                        hideLoadingToast();
//                        showToast(msg);


                    }
                }, new SPFailuredListener(ComCheckUserListActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });
        articlesPcf.refreshComplete();

    }

    private void loadMoreData() {
        showLoadingToast("正在加载数据");
        String offset = mComUserList.size() + "";
        CommunityRequest.getComCheckUserListWithCID(cid, offset, DATASIZE, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                List<ComUser> tempComUserList = (List<ComUser>) response;
                mComUserList.addAll(tempComUserList);
                mComUserAdapter.setData(mComUserList);
                hideLoadingToast();
            }
        }, new SPFailuredListener(ComCheckUserListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
        articlesPcf.refreshComplete();
    }
    @Override
    public void refuseBtnBtnClick(ComUser comUser) {
        checkUserWithID(comUser,cid, REFUSE);
    }
    @Override
    public void throughClick(ComUser comUser) {
        checkUserWithID(comUser,cid, THOUGH);
    }

//    cid（门派id）、uid（待审核成员uid）、status（审核状态  1 通过  -1 拒绝）
    private void checkUserWithID(final ComUser comUser,String cid, String status) {
        showLoadingToast("正在加载数据");
        CommunityRequest.getComCheckUserWithCID(cid, comUser.getUid(), status, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
//                List<ComUser> tempComUserList = (List<ComUser>) response;
//                mComUserList.addAll(tempComUserList);
//                mComUserAdapter.setData(mComUserList);
                if (msg.equals("成功")) {
                    mComUserList.remove(comUser);
                    mComUserAdapter.setData(mComUserList);
                }
                showToast(msg);
                hideLoadingToast();
            }
        }, new SPFailuredListener(ComCheckUserListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
        articlesPcf.refreshComplete();
    }


}
