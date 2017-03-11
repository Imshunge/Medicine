package com.shssjk.fragment.Order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.shssjk.activity.common.IViewController;
import com.shssjk.activity.common.shop.BeforPayActivity;
import com.shssjk.activity.common.shop.OrderDetailActivity;
import com.shssjk.adapter.OrderAdapter;
import com.shssjk.fragment.BaseFragment;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.order.SPOrder;
import com.shssjk.utils.SPOrderUtils;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.unionpay.UPPayAssistEx;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
//待支付

public class WaitPayFragment extends BaseFragment implements OrderAdapter.OkBtnClickListener,
        OrderAdapter.CancelBtnClickListener, OrderAdapter.onItemClickListener {
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
    private OrderAdapter orderAdapter;
    private Context mContext;
    int pageIndex;   //当前第几页:从1开始
    SPOrderUtils.OrderStatus orderStatus;    //订单类型:
    List<SPOrder> orders;
    boolean isFirstLoad;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    private final String mMode = "00";//01测试环境、00正式环境
    /**
     * 最大页数
     */
    boolean maxIndex;
    private String type = "pay";

    public WaitPayFragment() {
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
        orderAdapter = new OrderAdapter(mContext, this, this, this);
        orderListv.setAdapter(orderAdapter);

        View emptyView = view.findViewById(R.id.empty_lstv);
        orderListv.setEmptyView(emptyView);

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
                Intent detailIntent = new Intent(mContext, OrderDetailActivity.class);
                detailIntent.putExtra("type", "待支付");
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
        maxIndex = false;
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("p", pageIndex);
        showLoadingToast();
        SPPersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    orders = (List<SPOrder>) response;
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
//        type = SPOrderUtils.getOrderTypeWithOrderStatus(orderStatus);
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(type)) {
            params.put("type", type);
            params.put("p", pageIndex);
        }
        showLoadingToast();
        SPPersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                List<SPOrder> results = (List<SPOrder>) response;
                if (response != null) {
                    if (results.size() > 0) {
                        List<SPOrder> tempOrders = (List<SPOrder>) response;
                        orders.addAll(tempOrders);
                        orderAdapter.setData(orders);
                        testListViewFrame.setLoadMoreEnable(true);
                    } else if (results.size() == 0) {
                        testListViewFrame.setLoadMoreEnable(false);
                        showToast("没有更多数据了");
                        pageIndex--;
                    }
                } else {
                    pageIndex--;
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
                testListViewFrame.loadMoreComplete(true);
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                testListViewFrame.loadMoreComplete(true);
                hideLoadingToast();
                showToast(msg);
                pageIndex--;
            }
        });
    }

    @Override
    public void cancelBtnClick(SPOrder order) {
        cancelOrder(order);
    }

    @Override
    public void okClick(SPOrder order) {
        startUpPayBefor(order);
    }

    //   取消订单
    private void cancelOrder(final SPOrder order) {
        showLoadingToast("正在操作");
        cancelOrder(order.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
//                refreshData();
                orders.remove(order);
                orderAdapter.setData(orders);
            }
        }, new SPFailuredListener(WaitPayFragment.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    public void cancelOrder(String orderId, SPSuccessListener successListener, SPFailuredListener failuredListener) {
        SPPersonRequest.cancelOrderWithOrderID(orderId, successListener, failuredListener);
    }

    /**
     * 跳转支付界面
     *
     * @param order
     */
    private void startUpPayBefor(SPOrder order) {
        Intent payIntent = new Intent(mContext, BeforPayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("fromOrderList", order);
        payIntent.putExtras(bundle);
        startActivity(payIntent);
    }


    /**
     * 支付
     *
     * @param data
     */
    private void dealWithPay(String data) {
//        hud.dismiss();
        int ret = UPPayAssistEx.startPay(mContext, null, null, data.trim(), mMode);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            installPluns();
        }
//        hud.dismiss();
//            else {
//                UPPayAssistEx.startPayByJAR(getActivity(), PayActivity.class, null, null, data.trim(), mMode);
//            }
    }

    //    调用控件
    public void installPluns() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UPPayAssistEx.installUPPayPlugin(mContext);
                dialog.dismiss();
//                hud.dismiss();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void payOrder(SPOrder payOrder) {
        showLoadingToast("正在支付");

        Float sum1 = SSUtils.string2float(payOrder.getOrderAmount());
        String sum = SSUtils.float2String(sum1 * 100);
        ShopRequest.orderUnionPay(payOrder.getOrderID(), sum, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    String id = (String) response;
                    dealWithPay(id);

                } else {

                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        hud.show();
        if (requestCode == 10) {

            if (data == null) {
                return;
            }

            String str = data.getExtras().getString("pay_result");
            if (str.equals("success")) {
                showToast(" 支付成功！ ");
                refreshData();
            } else if (str.equals("fail")) {
                showToast(" 支付失败！ ");

            } else if (str.equals("cancel")) {
                showToast(" 你已取消了本次订单的支付！ ");

            }
        }
        if (requestCode == 888) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData();
            }
        }
    }


    //    使用石头支付
    private void payWithStone(SPOrder order) {
        showLoadingToast("正在支付");
//        sum1 = SSUtils.string2float(payOrder.getOrderAmount());
//        String sum = SSUtils.float2String(sum1 *100);
        ShopRequest.orderPayUseStone(order.getOrderSN(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                if (response != null) {
                    int id = (int) response;
                    if (id == 0) {

                        showToast(" 支付成功！ ");
                        refreshData();
                    } else {
                        showToast(msg);
                    }
                } else {
                    showToast(msg);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);

            }
        });
    }

    @Override
    public void ItemClick(SPOrder order) {
        Intent detailIntent = new Intent(mContext, OrderDetailActivity.class);
        detailIntent.putExtra("type", "待支付");
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        detailIntent.putExtras(bundle);
        startActivityForResult(detailIntent, 888);
    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }
}
