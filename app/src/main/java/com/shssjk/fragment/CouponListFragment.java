package com.shssjk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.shssjk.activity.R;
import com.shssjk.activity.common.user.CouponListActivity;
import com.shssjk.adapter.CouponListAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.shop.SPCoupon;

import java.util.ArrayList;
import java.util.List;

/**
优惠券列表
 */
public class CouponListFragment extends BaseFragment {

    private String TAG = "CouponListFragment";
    private Context mContext;
    private int mType ;
    CouponListAdapter mAdapter;
    boolean hasLoadData = false;

    PtrClassicFrameLayout ptrClassicFrameLayout;
    ListView listView;
    List<SPCoupon> coupons;
    int mPageIndex;   //当前第几页:从1开始
    /**
     *  最大页数
     */
    boolean mIsMaxPage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hasLoadData = false;
        View view = inflater.inflate(R.layout.fragment_coupon_list, container, false);
        mAdapter = new CouponListAdapter(getActivity() , mType);
        initSubView(view);
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

        ptrClassicFrameLayout = (PtrClassicFrameLayout)view.findViewById(R.id.coupon_list_view_frame);
        listView = (ListView)view.findViewById(R.id.coupon_listv);
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
    }

    @Override
    public void initEvent() {       loadMoreData();

    }

    @Override
    public void initData() {

    }
    public void setType(int type){
        this.mType = type;
        hasLoadData = false;
        if(mAdapter != null)mAdapter.setType(mType);
    }

    public void loadData() {
//        if (hasLoadData){
//            return;
//        }
        refreshData();
    }



    /**
     * 加载数据
     * @Description: 加载数据
     * @return void    返回类型
     * @throws
     */
    public void refreshData(){

        coupons = new ArrayList<SPCoupon>();
        mPageIndex = 1;
        mIsMaxPage = false;
        CouponListActivity couponListActivity = (CouponListActivity)getActivity();
        SPPersonRequest.getCouponListWithType(mType, mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    coupons = (List<SPCoupon>) response;
                    mIsMaxPage = false;
                    mAdapter.setData(coupons);
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                } else {
                    mIsMaxPage = true;
                    ptrClassicFrameLayout.setLoadMoreEnable(false);
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
                if(response!=null){
                    List<SPCoupon> tempComment = (List<SPCoupon>) response;
                    coupons.addAll(tempComment);
                    //更新收藏数据
                    mAdapter.setData(coupons);
                    mIsMaxPage = false;
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                }else{
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




}
