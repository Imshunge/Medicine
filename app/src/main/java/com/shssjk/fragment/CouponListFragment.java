package com.shssjk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.shssjk.activity.R;
import com.shssjk.activity.user.CouponList2Activity;
import com.shssjk.adapter.CouponListAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.shop.Coupon;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 优惠券列表
 */
public class CouponListFragment extends BaseFragment {

    @Bind(R.id.ll_listview)
    LinearLayout llListview;
    private String TAG = "CouponListFragment";
    private Context mContext;
    private int mType;
    CouponListAdapter mAdapter;
    boolean hasLoadData = false;

    PtrClassicFrameLayout ptrClassicFrameLayout;
    ListView listView;
    List<Coupon> coupons=new ArrayList<Coupon>();
    int mPageIndex;   //当前第几页:从1开始
    boolean isFirstLoad;
    /**
     * 最大页数
     */
    boolean mIsMaxPage;
    private View emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hasLoadData = false;
        View view = inflater.inflate(R.layout.fragment_coupon_list, container, false);
        mAdapter = new CouponListAdapter(getActivity(), mType);
        super.init(view);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void initSubView(View view) {

        ptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.coupon_list_view_frame);
        listView = (ListView) view.findViewById(R.id.coupon_listv);
        emptyView = view.findViewById(R.id.empty_lstv);
        listView.setEmptyView(emptyView);
        listView.setAdapter(mAdapter);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                refreshData();
            }
        });
        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //上拉加载更多
                loadMoreData();
            }
        });

        loadData();
    }

    @Override
    public void initEvent() {

//        loadMoreData();

    }

    @Override
    public void initData() {
        refreshData();
    }

    public void setType(int type) {
        this.mType = type;
        hasLoadData = false;
        if (mAdapter != null)
            mAdapter.setType(mType);
    }


    /**
     * 加载数据
     *
     * @return void    返回类型
     * @throws
     * @Description: 加载数据
     */
    public void refreshData() {
        coupons = new ArrayList<Coupon>();
        mPageIndex = 1;
        mIsMaxPage = false;
        CouponList2Activity couponListActivity = (CouponList2Activity) getActivity();
        SPPersonRequest.getCouponListWithType(mType, mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    coupons = (List<Coupon>) response;
                    mIsMaxPage = false;
                    mAdapter.setData(coupons);
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                } else {
                    mIsMaxPage = true;
                    ptrClassicFrameLayout.setLoadMoreEnable(false);
                }
                if (coupons.size() == 0) {
                    llListview.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    llListview.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);



                }

                hasLoadData = true;
                ptrClassicFrameLayout.refreshComplete();
            }
        }, new SPFailuredListener(couponListActivity) {
            @Override
            public void onRespone(String msg, int errorCode) {
                ptrClassicFrameLayout.setLoadMoreEnable(false);
            }
        });
    }

    public void loadMoreData() {

        if (mIsMaxPage) {
            return;
        }
        mPageIndex++;
        SPPersonRequest.getCouponListWithType(mType, mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<Coupon> tempComment = (List<Coupon>) response;
                    coupons.addAll(tempComment);
                    //更新收藏数据
                    mAdapter.setData(coupons);
                    mIsMaxPage = false;
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                } else {
                    mPageIndex--;
                    mIsMaxPage = true;
                    ptrClassicFrameLayout.setLoadMoreEnable(false);
                }
                ptrClassicFrameLayout.loadMoreComplete(true);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                mPageIndex--;
            }
        });
    }

    public void loadData() {
        if (isFirstLoad && listView != null) {
            refreshData();
            isFirstLoad = false;
        }
    }


    @Override
    public void gotoLoginPageClearUserDate() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
