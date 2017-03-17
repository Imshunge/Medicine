package com.shssjk.activity.person;
/**
 * 石头明细
 */

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.R;
import com.shssjk.adapter.StoneDetailAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.person.StoneDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StoneDetailActivity extends BaseActivity {
    @Bind(R.id.stonedetil_listv)
    ListView stonedetilListv;
    @Bind(R.id.list_view_frame)
    PtrClassicFrameLayout listViewFrame;
    @Bind(R.id.ll_listview)
    LinearLayout llListview;
    @Bind(R.id.empty_txtv)
    TextView emptyTxtv;
    @Bind(R.id.empty_lstv)
    RelativeLayout emptyLstv;
    @Bind(R.id.main_layout)
    FrameLayout mainLayout;
    private List<StoneDetail> mStoneDetaillist = new ArrayList<>();
    private Context mContext;
    private String offset="";
    private String r ="20";
    StoneDetailAdapter mStoneDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_mystone_detail));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone_detail);
        ButterKnife.bind(this);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        stonedetilListv.setEmptyView(emptyLstv);
        listViewFrame.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                getStoneDetailList();
            }
        });

        listViewFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //上拉加载更多
                loadMoreData();
            }
        });
    }

    @Override
    public void initData() {
        getStoneDetailList();
    }

    @Override
    public void initEvent() {
        mStoneDetailAdapter = new StoneDetailAdapter(mContext);
        stonedetilListv.setAdapter(mStoneDetailAdapter);
    }

    /**
     * 获取收藏列表
     */
    public void getStoneDetailList() {
        showLoadingToast("正在加载数据...");
        PersonRequest.getStoneDetailList("", "0",
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mStoneDetaillist = (List<StoneDetail>) response;
                            mStoneDetailAdapter.setData(mStoneDetaillist);
                            listViewFrame.setLoadMoreEnable(true);
                        } else {
                            showToast(msg);
                        }
                        listViewFrame.refreshComplete();
                    }
                }, new SPFailuredListener((IViewController) mContext) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        listViewFrame.refreshComplete();
                        showToast(msg);
                    }
                });
    }

    //    加载更多
    public void loadMoreData() {
        if (mStoneDetaillist.size() > 0) {
            int size = mStoneDetaillist.size();
            offset = size + "";
        }
        showLoadingToast();
        PersonRequest.getStoneDetailList(offset, r, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<StoneDetail> tempArticles = (List<StoneDetail>) response;
                    if (tempArticles.size() > 0) {
                        mStoneDetaillist.addAll(tempArticles);
                        mStoneDetailAdapter.setData(mStoneDetaillist);
                    } else {
                        showToast("没有更多数据了");
                        listViewFrame.setLoadMoreEnable(false);
                    }
                }
                listViewFrame.loadMoreComplete(true);
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                listViewFrame.loadMoreComplete(true);
                showToast(msg);
            }
        });
    }
}
