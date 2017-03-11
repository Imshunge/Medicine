
package com.shssjk.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.RecyclerViewUtils;

import com.shssjk.MainActivity;
import com.shssjk.activity.R;
import com.shssjk.activity.common.shop.Category2Activity;
import com.shssjk.activity.common.shop.CollectListActivity;
import com.shssjk.activity.common.shop.OrderActivity;
import com.shssjk.activity.common.shop.ProductAllActivity;
import com.shssjk.activity.common.shop.ProductListActivity;
import com.shssjk.activity.common.shop.SearchCommonActivity;
import com.shssjk.activity.common.shop.ShopCartActivity;
import com.shssjk.adapter.HomeCategoryAdapter;
import com.shssjk.adapter.HomeCategoryAdapter2;
import com.shssjk.adapter.HomeProductListAdapter;
import com.shssjk.adapter.ImagePagerAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.home.SPHomeRequest;
import com.shssjk.model.SPHomeBanners;
import com.shssjk.model.SPHomeCategory;
import com.shssjk.model.shop.Five;
import com.shssjk.model.shop.Product;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.LoadListView;
import com.shssjk.view.MobileScrollLayout;
import com.shssjk.view.SPHomeListView;
import com.shssjk.view.ScrollBottomScrollView;
import com.shssjk.activity.common.shop.CaptureActivity;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * 健界
 */
public class ShopFragment extends BaseFragment implements View.OnClickListener,
		MobileScrollLayout.PageListener,
		HomeProductListAdapter.ItemClickListener ,ScrollBottomScrollView.ScrollBottomListener,
		ImagePagerAdapter.OnBinnerOnClickListener, LoadListView.ILoadListener {

	private String TAG = "ShopFragment";
	public final static int CATEGORY_FRAGMENT = 1;
	public final static int SHOPCART_FRAGMENT = 2;
	public final static String RR="10";
	private Context mContext;
	private String offset="0";

	View mHeaderView;
	RecyclerView mRecyclerView ;
//	分类
	View categoryLayout;
//	购物车
	View shopcartLayout;
	//订单
	View orderLayout;
	//优惠券
	View couponLayout;
	RelativeLayout homeTitleView;
	EditText searchText;
	MobileScrollLayout mScrolllayout;	//轮播广告scrollLayout
	ViewGroup mPointerLayout; //指示点Layout
	HomeCategoryAdapter mAdapter;
	HomeCategoryAdapter2 mAdapter2;

	ImageView upLeftImgv;			//上-> 左
	ImageView upRightTopImgv;		//上-> 右 -> 上
	ImageView upRightBottomImgv;	//上-> 右 -> 下

	ImageView bottomLeftImgv;	//下-> 左
	ImageView bottomRightImgv;	//下-> 右
	MainActivity mainActivity;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	TextView scanView;
	Button searchView;
	private List<SPHomeBanners> mBanners;
	private List<Five> homeFives;
//	SPHomeListView mHomeListView;
    LoadListView mHomeListView;
	private TextView mCommentLoadMore;
	private HomeProductListAdapter homeProductListAdapter;
    private ScrollBottomScrollView mScrollBottomScrollView;
	private List<Product> mProducts = new ArrayList<Product>();
	private AutoScrollViewPager viewPager;
	private ImagePagerAdapter imagePagerAdapter;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_shop, null,false);
		mHeaderView = inflater.inflate(R.layout.shop_header_view, null);
//		分类 收藏 购物车 订单 布局
		categoryLayout = mHeaderView.findViewById(R.id.home_menu_categroy_layout);
		shopcartLayout = mHeaderView.findViewById(R.id.home_menu_shopcart_layout);
		orderLayout = mHeaderView.findViewById(R.id.home_menu_order_layout);
		couponLayout = mHeaderView.findViewById(R.id.home_menu_coupon_layout);
		upLeftImgv = (ImageView)mHeaderView.findViewById(R.id.up_left_imgv);
		upRightTopImgv = (ImageView)mHeaderView.findViewById(R.id.up_right_top_imgv);
		upRightBottomImgv = (ImageView)mHeaderView.findViewById(R.id.up_right_bottom_imgv);
		bottomLeftImgv = (ImageView)mHeaderView.findViewById(R.id.bottom_left_imgv);
		bottomRightImgv = (ImageView)mHeaderView.findViewById(R.id.bottom_right_imgv);
		homeTitleView = (RelativeLayout) view.findViewById(R.id.toprela);
		mRecyclerView = (RecyclerView)view.findViewById(R.id.home_listv);
		mScrollBottomScrollView= (ScrollBottomScrollView)view.findViewById(R.id.scrollview);
//		homeTitleView.getBackground().setAlpha(0);
		searchText = (EditText) homeTitleView.findViewById(R.id.searchkey_edtv);
		searchText.setFocusable(false);
		searchText.setFocusableInTouchMode(false);
		scanView= (TextView) view.findViewById(R.id.image_left);
//	    个人中心
		searchView = (Button) view.findViewById(R.id.btn_image_right);
		mHomeListView=(LoadListView) view.findViewById(R.id.home_lsit_item_grid);
		View footerView =  ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_footer, null, false);
		mHomeListView.addFooterView(footerView);
		mCommentLoadMore= (TextView) footerView.findViewById(R.id.footer_hint_textview);
		mCommentLoadMore.setOnClickListener(this);
		homeProductListAdapter = new HomeProductListAdapter(mContext,this);
		mHomeListView.setAdapter(homeProductListAdapter);
		mAdapter2  = new HomeCategoryAdapter2(mContext);
		HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter2);
		mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.addOnScrollListener(mOnScrollListener);
		RecyclerViewUtils.setHeaderView(mRecyclerView, mHeaderView);
		/** 设置listView header view : 广告轮播 */
//		mScrolllayout = (MobileScrollLayout)mHeaderView.findViewById(R.id.banner_slayout);
//		mScrolllayout = (MobileScrollLayout)mHeaderView.findViewById(R.id.banner_slayout);
		viewPager= (AutoScrollViewPager)mHeaderView.findViewById(R.id.view_pager);


//		viewPager = (viewPager) findViewById(R.id.test_top);
		// 获取屏幕像素
		DisplayMetrics display = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(display);
		viewPager.setLayoutParams(new LinearLayout.LayoutParams(
				display.widthPixels, display.widthPixels * 3 / 5));
		imagePagerAdapter = new ImagePagerAdapter(mContext, this).setInfiniteLoop(true);
//		viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
//		viewPager.setInterval(2000);
//		viewPager.startAutoScroll();
//		viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageIdList.size());
		super.init(view);
		return view;
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
		searchText.setOnClickListener(this);
		upLeftImgv.setOnClickListener(this);
		upRightTopImgv.setOnClickListener(this);
		upRightBottomImgv.setOnClickListener(this);
		bottomLeftImgv.setOnClickListener(this);
		bottomRightImgv.setOnClickListener(this);
		categoryLayout.setOnClickListener(this);
		shopcartLayout.setOnClickListener(this);
		orderLayout.setOnClickListener(this);
		couponLayout.setOnClickListener(this);
//		mScrolllayout.setPageListener(this);
		scanView.setOnClickListener(this);
		searchView.setOnClickListener(this);
		mScrollBottomScrollView.setScrollBottomListener(this);
	}

	@Override
	public void initData() {
		refreshData();
	}
//	测试添加
	@Override
	public void onResume() {
		super.onResume();
		// start auto scroll when onResume
		viewPager.startAutoScroll();
//		refreshData();
	}
	@Override
	public void onPause() {
		super.onPause();
		// stop auto scroll when onPause
		viewPager.stopAutoScroll();
	}

	public void refreshData() {
		SPHomeRequest.getHomeData(new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
				hideLoadingToast();
				mDataJson = (JSONObject) response;
				try {
					if (mDataJson.has("homeCategories")) {
						List<SPHomeCategory> homeCategories = (List<SPHomeCategory>) mDataJson.get("homeCategories");
//						dealModels(homeCategories);
					}
//					推荐的
					if (mDataJson.has("fives")) {
						homeFives = (List<Five>) mDataJson.get("fives");
						dealHomeFive(homeFives);
					}
					if (mDataJson.has("banners")) {
						mBanners = (List<SPHomeBanners>) mDataJson.get("banners");
//						mScrolllayout.removeAllViews();
//						for (SPHomeBanners banner : mBanners) {
//							ImageView img = getImageViewByBg(R.drawable.b_m);
//							Glide.with(mContext).load(String.format(banner.getAdCode())).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
//							img.setScaleType(ImageView.ScaleType.FIT_XY);
////							mScrolllayout.addView(img);
//						}
//						mScrolllayout.invalidate();
						bulidBanner(mBanners);
						buildPointer();
					}
				} catch (Exception e) {
					showToast(e.getMessage());
					e.printStackTrace();
				}
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				hideLoadingToast();
				Log.e(TAG, "zzx==>error msg: " + msg);
			}
		});
		SPHomeRequest.getHomeRecommendProduct("", "", new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
//				hideLoadingToast();
				mDataJson = (JSONObject) response;
				try {
					if (mDataJson.has("products")) {
						mProducts = (List<Product>) mDataJson.get("products");
						dealModels(mProducts);
					}
				} catch (Exception e) {
					showToast(e.getMessage());
					e.printStackTrace();
				}
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				hideLoadingToast();
				Log.e(TAG, "zzx==>error msg: " + msg);
			}
		});

	}

	private void bulidBanner(List<SPHomeBanners> mBanners) {
		imagePagerAdapter = new ImagePagerAdapter(mContext, this).setInfiniteLoop(true);
		imagePagerAdapter.setData(mBanners);
		viewPager.setAdapter(imagePagerAdapter);
		viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setInterval(2000);
		viewPager.startAutoScroll();
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mBanners.size());
	}
	private void dealHomeFive(List<Five> homeFives) {
//		显示 顺序
//		0  1
//		   2
//		4  3

//		有机系列
		if(homeFives.size()>=5) {
			String url1 = MobileConstants.BASE_HOST + homeFives.get(1).getad_code();
			Glide.with(mContext).load(url1).placeholder(R.drawable.product_default).
					diskCacheStrategy(DiskCacheStrategy.SOURCE).into(upRightTopImgv);
		}
		//		case R.id.bottom_right_imgv:
////	 黑粮系列
		if(homeFives.size()>=5) {
			String url2 = MobileConstants.BASE_HOST + homeFives.get(4).getad_code();
			Glide.with(mContext).load(url2).placeholder(R.drawable.product_default).
					diskCacheStrategy(DiskCacheStrategy.SOURCE).into(bottomRightImgv);
		}
		//		case R.id.bottom_left_imgv:
////     食品加工
		if(homeFives.size()>=5) {
			String url3 = MobileConstants.BASE_HOST + homeFives.get(3).getad_code();
			Glide.with(mContext).load(url3).placeholder(R.drawable.product_default)
					.diskCacheStrategy(DiskCacheStrategy.SOURCE).into(bottomLeftImgv);
		}
//		待变   	case R.id.up_left_imgv:
////	自我检测设备
		if(homeFives.size()>=5) {
			String url4 = MobileConstants.BASE_HOST + homeFives.get(0).getad_code();
			Glide.with(mContext).load(url4).placeholder(R.drawable.product_default).
					diskCacheStrategy(DiskCacheStrategy.SOURCE).into(upLeftImgv);
		}
		////    菌菇系列
//		case R.id.up_right_bottom_imgv:
		if(homeFives.size()>=5) {
			String url5 = MobileConstants.BASE_HOST + homeFives.get(2).getad_code();
			Glide.with(mContext).load(url5).placeholder(R.drawable.product_default).
					diskCacheStrategy(DiskCacheStrategy.SOURCE).into(upRightBottomImgv);
		}
	}
	private ImageView getImageViewByBg(int imageResId){
		ImageView imageView = new ImageView(mContext);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT ,
				ViewGroup.LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(lp);
		imageView.setBackgroundResource(imageResId);
		return imageView;
	}
	/**
	 * 构建轮播广告"圆点指示器"
	 */
	public void buildPointer(){
		// 获取子view个数, 用来计算圆点指示器个数
//		int pageCount = mScrolllayout.getChildCount();
//		mPointerLayout = (ViewGroup)mHeaderView.findViewById(R.id.pointer_layout);
//		ImageView[] pointerImgv = new ImageView[pageCount];
//		mPointerLayout.removeAllViews();
//		for (int i = 0; i < pageCount; i++) {
//			ImageView imageView = new ImageView(this.mContext);
//			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20 , 20);//圆点指示器宽高
//			lp.setMargins(8, 0, 8, 0);
//			imageView.setLayoutParams(lp);
//			imageView.setPadding(20, 0, 20, 0);
//			pointerImgv[i] = imageView;
//			if (i == 0) {
//				//默认选中第一张图片
//				pointerImgv[i].setBackgroundResource(R.drawable.ic_home_arrows_focus);
//			} else {
//				pointerImgv[i].setBackgroundResource(R.drawable.ic_home_arrows_normal);
//			}
//			mPointerLayout.addView(pointerImgv[i]);
//		}
	}

	private void dealModels(List<Product> cate){
		homeProductListAdapter.setData(cate);
	}
	private int getScrolledDistance() {
		LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
		View firstVisibleItem = mRecyclerView.getChildAt(0);
		int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
		int itemHeight = firstVisibleItem.getHeight();
		int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
		return (firstItemPosition + 1) * itemHeight - firstItemBottom;
	}

	private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);

			int scrollY = getScrolledDistance();

			if (scrollY == 0) {

			} else {

			}
		}
	};

	public void setMainActivity(MainActivity mainActivity){
		this.mainActivity = mainActivity;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()){
			case R.id.searchkey_edtv:
			case R.id.btn_image_right://
                //跳转搜索页面
				startupSearchActivity();
				break;
				//		显示 顺序
              //		0  1
              //		   2
               //		4  3
			case R.id.up_left_imgv:
//				自我检测设备
				if(homeFives.size()>=5) {
					startupProductListActivity(homeFives.get(0));
				}
				break;
			case R.id.up_right_top_imgv:
//              有机系列
				if(homeFives.size()>=5) {
					startupProductListActivity(homeFives.get(1));
				}
				break;
			case R.id.up_right_bottom_imgv:
//              菌菇系列
				if(homeFives.size()>=5) {
					startupProductListActivity(homeFives.get(2));
				}
				break;
			case R.id.bottom_left_imgv:
//               食品加工
				if(homeFives.size()>=5) {
					startupProductListActivity(homeFives.get(4));
				}
				break;
			case R.id.bottom_right_imgv:
//			 黑粮系列
				if(homeFives.size()>=5) {
					startupProductListActivity(homeFives.get(3));
				}
				break;
			case R.id.home_menu_categroy_layout :
//				产品分类
				Intent categoryIntent = new Intent(getActivity() , Category2Activity.class);
				getActivity().startActivity(categoryIntent);
				break;
			case R.id.home_menu_shopcart_layout :
//				购物车
				startupProductCarActivity();
				break;
			case R.id.home_menu_order_layout :
				startupOrderList(0);
				break;
//			收藏
			case R.id.home_menu_coupon_layout :
				startupCollection();
				break;
			case R.id.image_left:  //扫描
				startupActivityCapture();
				break;
			case R.id.footer_hint_textview:
				showLoadingToast("正在加载数据");
				laodaMoreData();
				break;
		}
	}
	//搜索界面
	private void startupSearchActivity() {
		Intent SearchCommonIntent = new Intent(getActivity() , SearchCommonActivity.class);
		getActivity().startActivity(SearchCommonIntent);
	}


	boolean checkLogin(){
		if (!MobileApplication.getInstance().isLogined){
			showToastUnLogin();
			toLoginPage();
			return false;
		}
		return true;
	}
	//收藏
	public void startupCollection(){
		if (!checkLogin())return;
		Intent collectIntent = new Intent(getActivity() , CollectListActivity.class);
		getActivity().startActivity(collectIntent);
	}
	//订单列表
	public void startupOrderList(int orderStatus){
		if (!MobileApplication.getInstance().isLogined){
			showToastUnLogin();
			toLoginPage();
			return;
		}
		Intent allOrderList = new Intent(getActivity() , OrderActivity.class);
		allOrderList.putExtra("index" , orderStatus);
		getActivity().startActivity(allOrderList);
	}
// 跳转分类界面
	public void startupProductListActivity(Five category){
//		产品分类
		String categoryId =SSUtils.getNumbers(category.getad_link()) ;
		Intent intent = new Intent(getActivity() , ProductListActivity.class);
		if (category!=null){
			intent.putExtra("category", categoryId);
		}
		getActivity().startActivity(intent);
	}
	/**
	 * 购物车
	 */
	public void startupProductCarActivity(){
		if (!MobileApplication.getInstance().isLogined){
			showToastUnLogin();
			toLoginPage();
			return;
		}
		Intent carIntent = new Intent(getActivity() , ShopCartActivity.class);
		getActivity().startActivity(carIntent);
	}
	/**
	 * 开始扫描
	 */
	public void startupActivityCapture(){
		if (!MobileApplication.getInstance().isLogined){
			showToastUnLogin();
			toLoginPage();
			return;
		}
		Intent carIntent = new Intent(getActivity() , CaptureActivity.class);
		getActivity().startActivity(carIntent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode){
				case SCANNIN_GREQUEST_CODE:
					Bundle bundle = data.getExtras();
					showToast(bundle.getString("result"));
					break;
			}
		}
	}

	@Override
	public void page(int page) {
	String url=	mBanners.get(page).getAdLink();
	String googdsId=SSUtils.getNumbers(url);
		startupActivity(googdsId);
	}
	public void startupActivity(String goodsID) {
		Intent intent = new Intent(getActivity(), ProductAllActivity.class);
		intent.putExtra("goodsId", goodsID);
		startActivity(intent);
	}


	@Override
	public void onItemClickListener(Product product) {
		startupActivity(product.getGoodsId());
	}

	@Override
	public void scrollBottom() {
//		showLoadingToast("正在加载数据");
//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				//获取更多数据
//				laodaMoreData();
//			}
//		}, 2000);
//		onLoad();
	}
	private void laodaMoreData() {
		if (mProducts.size() > 0) {
			int size = mProducts.size();
			offset = size+"";
		}
		SPHomeRequest.getHomeRecommendProduct(offset, RR, new SPSuccessListener() {
			@Override
			public void onRespone(String msg, Object response) {
//				hideLoadingToast();
				if (msg.equals("空数据")){
					hideLoadingToast();
					showToast("没有更多了");
					return;
				}
				mDataJson = (JSONObject) response;
				try {
					if (mDataJson.has("products")) {
						List<Product>tempProducts = (List<Product>) mDataJson.get("products");
//						if (SSUtils.isEmpty(tempProducts)){
//							showToast("没有更多了");
//							return;
//						}
						mProducts.addAll(tempProducts);
						dealModels(mProducts);
					}

				} catch (Exception e) {
					showToast(e.getMessage());
					e.printStackTrace();
				}
				hideLoadingToast();
			}
		}, new SPFailuredListener() {
			@Override
			public void onRespone(String msg, int errorCode) {
				hideLoadingToast();
				Log.e(TAG, "zzx==>error msg: " + msg);
			}
		});

	}

	@Override
	public void onBinnerClick(int index) {
		String url=	mBanners.get(index).getAdLink();
		String googdsId=SSUtils.getNumbers(url);
		startupActivity(googdsId);
	}

	@Override
	public void gotoLoginPageClearUserDate() {

	}

	@Override
	public void onLoad() {
//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				//获取更多数据
////                getLoadData();
//				//更新listview显示；
////                showListView(apk_list);
//				//通知listview加载完毕
//				synchronized(this){
//					laodaMoreData();
//				}
//				mHomeListView.loadComplete();
//			}
//		}, 2000);
	}

	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
//			indexText.setText(new StringBuilder().append((position) %imageIdList.size() + 1).append("/")
//					.append(imageIdList.size()));
		}
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

}
