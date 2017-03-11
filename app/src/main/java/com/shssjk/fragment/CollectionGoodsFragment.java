package com.shssjk.fragment;

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
import com.shssjk.activity.R;
import com.shssjk.activity.common.IViewController;
import com.shssjk.activity.common.shop.ProductAllActivity;
import com.shssjk.adapter.CollectListAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.SPPersonRequest;
import com.shssjk.model.shop.SPCollect;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的收藏 商品
 * 无加载更多
 */
public class CollectionGoodsFragment extends BaseFragment {
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
    CollectListAdapter mAdapter;
    List<SPCollect> mCollects;
    @Bind(R.id.ll_listview)
    LinearLayout llListview;
    private View inflate;
    private Context mContext;

    public CollectionGoodsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CollectionGoodsFragment newInstance(String param1, String param2) {
        CollectionGoodsFragment fragment = new CollectionGoodsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_collection_information, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, inflate);
        super.init(inflate);
        return inflate;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void initSubView(View view) {

    }

    @Override
    public void initEvent() {
        mAdapter = new CollectListAdapter(mContext);
        orderListv.setAdapter(mAdapter);
        orderListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCollects.size() == 0 || position == mCollects.size()) {
                    return;
                }
                SPCollect collect = mCollects.get(position);
                Intent intent = new Intent(mContext, ProductAllActivity.class);
                intent.putExtra("goodsId", collect.getGoodsID());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        getListData();
    }

    private void getListData() {
        showLoadingToast();
        SPPersonRequest.getGoodsCollectWithSuccess(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {

                if (response != null) {
                    mCollects = (List<SPCollect>) response;
                    //更新收藏数据
                    mAdapter.setData(mCollects);
                } else {
                    showToast(msg);
                }
                if (mCollects.size() == 0) {
                    llListview.setVisibility(View.GONE);
                    emptyLstv.setVisibility(View.VISIBLE);
                } else {
                    llListview.setVisibility(View.VISIBLE);
                    emptyLstv.setVisibility(View.GONE);
                }



                hideLoadingToast();
            }
        }, new SPFailuredListener((IViewController) mContext) {
            @Override
            public void onRespone(String msg, int errorCode) {
                if (!"空数据".equals(msg)) {
                    showToast(msg);
                }
//                showToast(msg);
                hideLoadingToast();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }
}
