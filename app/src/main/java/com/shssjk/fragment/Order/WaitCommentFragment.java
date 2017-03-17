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


import com.shssjk.activity.R;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.shop.CommentOrderDetailActivity;
import com.shssjk.activity.shop.OrderReturnGoodsActivity;
import com.shssjk.activity.shop.PublishCommentActivity;
import com.shssjk.adapter.OrderComentAdapter;
import com.shssjk.fragment.BaseFragment;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.order.SPOrder;
import com.shssjk.model.shop.ProductCommnet;
import com.shssjk.utils.SPOrderUtils;
import com.shssjk.utils.SSUtils;
import com.unionpay.UPPayAssistEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
//待评价

public class WaitCommentFragment extends BaseFragment implements OrderComentAdapter.CommnetBtnClickListener, OrderComentAdapter.ReturnBtnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.order_listv)
    ListView orderListv;

    @Bind(R.id.empty_txtv)
    TextView emptyTxtv;
    @Bind(R.id.empty_lstv)
    RelativeLayout emptyLstv;
    @Bind(R.id.main_layout)
    LinearLayout mainLayout;
    private OrderComentAdapter orderAdapter;
    private Context mContext;
    int pageIndex;   //当前第几页:从1开始
    SPOrderUtils.OrderStatus orderStatus;    //订单类型:
    List<ProductCommnet> orders=new ArrayList<ProductCommnet>();
    boolean isFirstLoad;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    private final String mMode = "00";//01测试环境、00正式环境
    /**
     * 最大页数
     */
    boolean maxIndex;
    private String status   ="1";
//    User/comment_list
//    status 待评价 1 ，已评价 2
    public WaitCommentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wait_comment, container, false);
        ButterKnife.bind(this, view);
        mContext=this.getActivity();
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
//        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty, null);
//        getActivity().setContentView(emptyView);


        orderAdapter = new OrderComentAdapter(mContext,this,this);
        orderListv.setAdapter(orderAdapter);
    }

    @Override
    public void initEvent() {
//        testListViewFrame.setPtrHandler(new PtrDefaultHandler() {
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                //下拉刷新
//                refreshData();
//            }
//        });

//        testListViewFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
//
//            @Override
//            public void loadMore() {
//                //上拉加载更多
//                loadMoreData();
//            }
//        });
        orderListv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(orders.size()==0||position==orders.size()){
                    return;
                }
                ProductCommnet order = orders.get(position);
                Intent detailIntent = new Intent(mContext, CommentOrderDetailActivity.class);
                detailIntent.putExtra("type", "待评价");
                detailIntent.putExtra("orderId", order.getOrderId());
                startActivityForResult(detailIntent, 888);
            }
        });

    }

    @Override
    public void initData() {
        refreshData();
    }


    public void refreshData(){
        pageIndex = 1;
        maxIndex = false;

        showLoadingToast();
        PersonRequest.getCommentListWithType(status, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    orders = (List<ProductCommnet>) response;
                    orderAdapter.setData(orders);
                } else {
                    maxIndex = true;
//                    testListViewFrame.setLoadMoreEnable(false);
                }

                if (orders.size() == 0) {
                    orderListv.setVisibility(View.GONE);
                    emptyLstv.setVisibility(View.VISIBLE);
                } else {
                    orderListv.setVisibility(View.VISIBLE);
                    emptyLstv.setVisibility(View.GONE);
                }
//                testListViewFrame.refreshComplete();
                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                if ("空数据".equals(msg)) {
                    if (orders.size() == 0) {
                        orderListv.setVisibility(View.GONE);
                        emptyLstv.setVisibility(View.VISIBLE);
                    } else {
                        orderListv.setVisibility(View.VISIBLE);
                        emptyLstv.setVisibility(View.GONE);
                    }
                } else {
                    showToast(msg);
                }
            }
        });
    }

    public void loadData(){
        if (isFirstLoad && orderListv!=null) {
            refreshData();
            isFirstLoad = false;
        }
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
        }, new SPFailuredListener(WaitCommentFragment.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    /**
     * 取消订单
     * @param orderId
     */
    public void cancelOrder(String orderId , SPSuccessListener successListener , SPFailuredListener failuredListener){
        PersonRequest.cancelOrderWithOrderID(orderId, successListener, failuredListener);
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
        String sum = SSUtils.float2String(sum1*100);
        ShopRequest.orderUnionPay(payOrder.getOrderID(), sum, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    String id = (String) response;
//                    dealWithPay(id);

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

        if (requestCode == 888) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData();
            }
        }
    }

    @Override
    public void commentClick(ProductCommnet order) {
        startCommentActivity(order);
    }

    private void startCommentActivity(ProductCommnet order) {
        Intent detailIntent = new Intent(mContext, PublishCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        detailIntent.putExtras(bundle);
        startActivityForResult(detailIntent, 888);
    }

    @Override
    public void returnBtnClick(ProductCommnet order) {
        Intent detailIntent = new Intent(mContext, OrderReturnGoodsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        detailIntent.putExtras(bundle);
        startActivityForResult(detailIntent, 888);
    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }
}
