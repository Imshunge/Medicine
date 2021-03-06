
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
import com.shssjk.adapter.ProductDetailCommentAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.shop.GoodsComment;


import java.util.List;


/**
 *  商品详情 -> 商品评论列表
 *
 */
public class ProductCommentListFragment extends BaseFragment {

	private String TAG = "ProductCommentListFragment";
	private Context mContext;
	private String goodsId ;

	PtrClassicFrameLayout ptrClassicFrameLayout;
	ListView commentListv;
	ProductDetailCommentAdapter mAdapter;
	List<GoodsComment> mComments ;

	boolean isFirstLoad;

	int pageIndex;   //当前第几页:从1开始
	/**
	 *  最大页数
	 */
	boolean maxIndex;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.mContext = context;
	}

	public void setGoodsId(String goodsId){
		this.goodsId = goodsId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isFirstLoad = true;
	    View view = inflater.inflate(R.layout.product_details_comment_list, null,false);
		initSubView(view);
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void initSubView(View view) {
		ptrClassicFrameLayout = (PtrClassicFrameLayout)view.findViewById(R.id.product_comment_list_view_frame);
		commentListv  = (ListView)view.findViewById(R.id.product_comment_listv);
		View  emptyView  =  view.findViewById(R.id.empty_lstv);
		commentListv.setEmptyView(emptyView);
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
		mAdapter = new ProductDetailCommentAdapter(getActivity());
		commentListv.setAdapter(mAdapter);
		loadData();

	}

	@Override
	public void initEvent() {

	}

	@Override
	public void initData() {

	}

	public void refreshData(){
		pageIndex = 1;
		maxIndex = false;
		//showLoadingToast();
		ShopRequest.getGoodsCommentWithGoodsID(goodsId, pageIndex, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
				if (response != null) {
					mComments = (List<GoodsComment>) response;
					//更新收藏数据
					mAdapter.setData(mComments);
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
				showToast(msg);
				hideLoadingToast();
			}
		});
	}


	public void loadMoreData() {

		if (maxIndex) {
			return;
		}
		pageIndex++;
		//showLoadingToast();
		ShopRequest.getGoodsCommentWithGoodsID(goodsId, pageIndex, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {

				if (response != null) {
					List<GoodsComment> tempComment = (List<GoodsComment>) response;
					mComments.addAll(tempComment);
					//更新收藏数据
					mAdapter.setData(mComments);
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

	public void loadData(){
		if (isFirstLoad && commentListv!=null) {
			refreshData();
			isFirstLoad = false;
		}
	}

	@Override
	public void gotoLoginPageClearUserDate() {

	}
}
