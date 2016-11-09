package com.shssjk.activity.common.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.loopj.android.http.RequestParams;
import com.shssjk.activity.R;
import com.shssjk.adapter.OrderListAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.order.SPOrder;
import com.shssjk.utils.SPConfirmDialog;
import com.shssjk.utils.SPOrderUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

/**
 * 订单列表 （全部订单）
 */
public class OrderListActivity extends OrderBaseActivity implements SPConfirmDialog.ConfirmDialogListener{
    private String TAG = "OrderListActivity";

    ListView orderListv;

    PtrClassicFrameLayout ptrClassicFrameLayout;


    SPOrderUtils.OrderStatus orderStatus;    //订单类型:
    List<SPOrder> orders;
    private SPOrder currentSelectOrder;    //选中的订单
    private SPOrder cancelOrder;    //取消订单
    /**
     * 取消订单
     * @param order
     */
    public void cancelOrder(SPOrder order){
        cancelOrder=order;
        showConfirmDialog("您确定要取消订单吗？", "订单提醒", this, MobileConstants.tagCancel);

        }
    /**
     * 确认收货
     * @param order
     */
    public void confirmReceive(SPOrder order){

        showLoadingToast("正在操作");
            confirmOrderWithOrderID(order.getOrderID(), new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingToast();
                    showToast(msg);
                    refreshData();
                }
            }, new SPFailuredListener(OrderListActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingToast();
                    showToast(msg);
                }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            SPOrder order = (SPOrder)msg.obj;
            switch (msg.what){
                case MobileConstants.tagPay:
                    gotoPay(order);
                    break;
                case MobileConstants.tagCancel:
                    currentSelectOrder = order;
                    cancelOrder(order);
                    break;
                case MobileConstants.tagCustomer:
//                    connectCustomer();
                    break;
                case MobileConstants.tagShopping:
                    lookShopping(order);
                    break;
                case MobileConstants.tagReceive:
                    confirmReceive(order);
                    break;
                case MobileConstants.tagReturn:
                    break;
                case MobileConstants.tagBuyAgain:
                    gotoProductDetail(order.getOrderID());
                    break;
                case MobileConstants.MSG_CODE_ORDER_LIST_ITEM_ACTION:
//                    Intent detailIntent = new Intent(SPOrderListActivity.this  , SPOrderDetailActivity_.class);
//                    detailIntent.putExtra("orderId", order.getOrderID());
//                    startActivity(detailIntent);
                    break;
            }
        }
    };


    /**
     * 弹框类型 , 1: 取消框; 2:收货确认
     */
    int alertType;
    int pageIndex;   //当前第几页:从1开始
    /**
     * 最大页数
     */
    boolean maxIndex;

    OrderListAdapter mAdapter;

    /**
     * 标题能容
     */
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.order_list_titiel));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        super.init();
    }

    @Override
    public void initSubViews() {
        //        标题内容
        titlbarFl = (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this, R.color.white));

        orderListv = (ListView) findViewById(R.id.order_listv);

        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.test_list_view_frame);



        View emptyView = findViewById(R.id.empty_lstv);
        orderListv.setEmptyView(emptyView);

    }

    @Override
    public void initData() {

        if (getIntent()!=null){
            int value = getIntent().getIntExtra("orderStatus" , 0);
            orderStatus = SPOrderUtils.getOrderStatusByValue(value);
        }

        String title = SPOrderUtils.getOrderTitlteWithOrderStatus(orderStatus);
        setTitle(title);


        mAdapter = new OrderListAdapter(this , mHandler);
        orderListv.setAdapter(mAdapter);
        refreshData();
    }

    @Override
    public void initEvent() {


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

        orderListv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SPOrder order = (SPOrder)mAdapter.getItem(position);
                Intent detailIntent = new Intent(OrderListActivity.this  , OrderDetailActivity.class);
                detailIntent.putExtra("orderId", order.getOrderID());
                startActivity(detailIntent);
            }
        });



    }

    /**
     *  刷新数据
     */
    public void refreshData(){
        pageIndex = 1;
        maxIndex = false;
        String type = SPOrderUtils.getOrderTypeWithOrderStatus(orderStatus);
        RequestParams params = new RequestParams();

        params.put("type", type);
        params.put("p", pageIndex);

        showLoadingToast();
        SPPersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    orders = (List<SPOrder>) response;
                    mAdapter.setData(orders);
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                } else {
                    maxIndex = true;
                    ptrClassicFrameLayout.setLoadMoreEnable(false);
                }

                ptrClassicFrameLayout.refreshComplete();

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




    public void loadMoreData(){
        if(maxIndex)
        {
            return;
        }
        pageIndex++;

        String type = SPOrderUtils.getOrderTypeWithOrderStatus(orderStatus);
        RequestParams params = new RequestParams();

        if (!SPStringUtils.isEmpty(type)) {
            params.put("type" , type);
            params.put("p" , pageIndex);
        }

        showLoadingToast();
        SPPersonRequest.getOrderListWithParams(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null && ((List<SPOrder>) response).size() > 0) {
                    List<SPOrder> tempOrders = (List<SPOrder>) response;
                    orders.addAll(tempOrders);
                    mAdapter.setData(orders);
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                } else {
                    pageIndex--;
                    maxIndex = true;
                    ptrClassicFrameLayout.setLoadMoreEnable(false);
                }
                ptrClassicFrameLayout.refreshComplete();
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


    /**
     *  处理服务器获取的数据
     */
    public void dealModel(){
        //处理product缩略图url

    }


    @Override
    public void clickOk(int actionType) {
        if (actionType == MobileConstants.tagCancel){
            cancelOrder();
        }
    }
//   取消订单
    private void cancelOrder() {
        showLoadingToast("正在操作");
        cancelOrder(cancelOrder.getOrderID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                refreshData();
            }
        }, new SPFailuredListener(OrderListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });

    }
}
