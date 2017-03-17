package com.shssjk.fragment.Order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.shssjk.activity.R;
import com.shssjk.activity.shop.ReturnOrderDetailActivity;
import com.shssjk.adapter.OrderReturnGoodsAdapter;
import com.shssjk.fragment.BaseFragment;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.order.SPExchange;
import com.shssjk.utils.SPOrderUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//待发货
public class ReturnGoodsFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.order_listv)
    ListView orderListv;
    @Bind(R.id.test_list_view_frame)
    PtrClassicFrameLayout testListViewFrame;
    @Bind(R.id.empty_txtv)
    TextView emptyTxtv;
    @Bind(R.id.empty_lstv)
    RelativeLayout emptyLstv;
    @Bind(R.id.main_layout)
    LinearLayout mainLayout;
    @Bind(R.id.ll_listview)
    LinearLayout llListview;
    private OrderReturnGoodsAdapter orderAdapter;
    private Context mContext;
    int pageIndex;   //当前第几页:从1开始
    SPOrderUtils.OrderStatus orderStatus;    //订单类型:
    List<SPExchange> orders;
    boolean isFirstLoad;
    /**
     * 最大页数
     */
    boolean maxIndex;
    private String type = "send";

    public ReturnGoodsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wait_pay, container, false);
        ButterKnife.bind(this, view);
        mContext = this.getActivity();
        super.init(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void initSubView(View view) {
        View emptyView = view.findViewById(R.id.empty_lstv);
        orderListv.setEmptyView(emptyView);

        orderAdapter = new OrderReturnGoodsAdapter(mContext);
        orderListv.setAdapter(orderAdapter);
    }

    @Override
    public void initEvent() {
        testListViewFrame.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                refreshData();
            }
        });
        testListViewFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //上拉加载更多
                if (orders.size() > 0) {
                    loadMoreData();
                }
            }
        });
        orderListv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (orders.size() == 0 || position == orders.size()) {
                    return;
                }
                SPExchange order = orders.get(position);
                Intent detailIntent = new Intent(mContext, ReturnOrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", order);
                detailIntent.putExtras(bundle);
                startActivityForResult(detailIntent, 888);
            }
        });
    }

    @Override
    public void initData() {
        refreshData();
    }

    public void refreshData() {
        pageIndex = 1;
        showLoadingToast();
        PersonRequest.getExchangeListWithPage(pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    orders = (List<SPExchange>) response;
                    orderAdapter.setData(orders);
                    testListViewFrame.setLoadMoreEnable(true);
                } else {
                    maxIndex = true;
                    testListViewFrame.setLoadMoreEnable(false);
                }
                if (orders.size() == 0) {
                    llListview.setVisibility(View.GONE);
                    emptyLstv.setVisibility(View.VISIBLE);
                } else {
                    llListview.setVisibility(View.VISIBLE);
                    emptyLstv.setVisibility(View.GONE);
                }

                testListViewFrame.refreshComplete();
                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    public void loadData() {
        if (isFirstLoad && orderListv != null) {
            refreshData();
            isFirstLoad = false;
        }
    }

    public void loadMoreData() {
        if (maxIndex) {
            testListViewFrame.loadMoreComplete(true);
            return;
        }
        pageIndex++;
        showLoadingToast();
        PersonRequest.getExchangeListWithPage(pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<SPExchange> tempOrders = (List<SPExchange>) response;
                    if (tempOrders.size() > 0) {
                        orders.addAll(tempOrders);
                        orderAdapter.setData(orders);
                        testListViewFrame.setLoadMoreEnable(true);
                    } else {
                        pageIndex--;
                        maxIndex = true;
                        testListViewFrame.setLoadMoreEnable(false);
                        showToast("没有更多数据了");
                    }
                }
                if (orders.size() == 0) {
                    llListview.setVisibility(View.GONE);
                    emptyLstv.setVisibility(View.VISIBLE);
                } else {
                    llListview.setVisibility(View.VISIBLE);
                    emptyLstv.setVisibility(View.GONE);
                }
                testListViewFrame.loadMoreComplete(true);
                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
                pageIndex--;
            }
        });
    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }
}
