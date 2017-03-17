package com.shssjk.fragment.Order;

import android.app.Activity;
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
import com.loopj.android.http.RequestParams;
import com.shssjk.activity.R;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.shop.OrderDetailActivity;
import com.shssjk.adapter.OrderAdapter;
import com.shssjk.fragment.BaseFragment;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.order.SPOrder;
import com.shssjk.utils.SPOrderUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//待收货
public class WaitReceiveFragment extends BaseFragment implements
        OrderAdapter.OkBtnClickListener,
        OrderAdapter.CancelBtnClickListener,
        OrderAdapter.onItemClickListener

{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //    @Bind(R.id.order_listv)
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
    private OrderAdapter orderAdapter;
    private Context mContext;
    int pageIndex;   //当前第几页:从1开始
    SPOrderUtils.OrderStatus orderStatus;    //订单类型:
    List<SPOrder> orders;
    boolean isFirstLoad;
    /**
     * 最大页数
     */
    boolean maxIndex;
    private String type = "receive";

    public WaitReceiveFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wait_receiver, container, false);
        orderListv = (ListView) view.findViewById(R.id.order_listv);
        View emptyView = view.findViewById(R.id.empty_lstv);
        orderListv.setEmptyView(emptyView);
//             orderListv.setVisibility(View.GONE);

//        ll_listview.setV
//        testListVie   wFrame.
//        llListview.setEmp
//        orderListv
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
//        View emptyView = view.findViewById(R.id.empty_lstv);
//        orderListv.setEmptyView(emptyView);
//        View emptyView = getLayoutInflater().inflate(R.layout.empty_lstv, null);
//        this.getListView().setEmptyView(emptyView);
        orderAdapter = new OrderAdapter(mContext, this, this, "待收货", this);
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
                loadMoreData();
            }
        });
        orderListv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (orders.size() == 0 || position == orders.size()) {
                    return;
                }
                SPOrder order = (SPOrder) orders.get(position);
                DetailActivity(order);
            }
        });
    }


    private void DetailActivity(SPOrder order) {
        Intent detailIntent = new Intent(mContext, OrderDetailActivity.class);
        detailIntent.putExtra("type", "待收货");
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        detailIntent.putExtras(bundle);
        startActivityForResult(detailIntent, 888);
    }

    @Override
    public void initData() {
        refreshData();
    }


    public void refreshData() {
        pageIndex = 1;
        maxIndex = false;
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("p", pageIndex);
        showLoadingToast();
        PersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    orders = (List<SPOrder>) response;
                    orderAdapter.setData(orders);
                    testListViewFrame.setLoadMoreEnable(true);
                    if (orders.size() == 0) {
                        llListview.setVisibility(View.GONE);
                        emptyLstv.setVisibility(View.VISIBLE);
                    } else {
                        llListview.setVisibility(View.VISIBLE);
                        emptyLstv.setVisibility(View.GONE);
                    }
                } else {
                    maxIndex = true;
                    testListViewFrame.setLoadMoreEnable(false);
                }
                testListViewFrame.refreshComplete();
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
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
            return;
        }
        pageIndex++;

        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(type)) {
            params.put("type", type);
            params.put("p", pageIndex);
        }
        showLoadingToast();
        PersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<SPOrder> tempOrders = (List<SPOrder>) response;
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
                testListViewFrame.refreshComplete();
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
                pageIndex--;
            }
        });
    }


    @Override
    public void cancelBtnClick(SPOrder order) {
//        查看物流
        showToast("取消");

    }

    @Override
    public void okClick(SPOrder order) {
//        确认收货
        confirmOrder(order);
    }

    //   确认收货
    private void confirmOrder(final SPOrder order) {
        showLoadingToast("正在操作");
        confirmOrder(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                orders.remove(order);
                orderAdapter.setData(orders);
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    //   查看物流
    private void queryOrderLogistics(final SPOrder order) {
        showLoadingToast("正在操作");
        queryOrderLogistics(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                orders.remove(order);
                orderAdapter.setData(orders);
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }


    /**
     * 确认收货
     *
     * @param orderId
     */
    public void confirmOrder(String orderId, SPSuccessListener successListener, SPFailuredListener failuredListener) {
        PersonRequest.confirmOrderWithOrderID(orderId, successListener, failuredListener);
    }

    /**
     * 查看物流
     *
     * @param orderId
     * @param successListener
     * @param failuredListener
     */
    public void queryOrderLogistics(String orderId, SPSuccessListener successListener, SPFailuredListener failuredListener) {
        PersonRequest.queryOrderWithOrderID(orderId, successListener, failuredListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 888) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData();
            }
        }
    }


    @Override
    public void ItemClick(SPOrder order) {
        DetailActivity(order);

    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }
}
