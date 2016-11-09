package com.shssjk.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.RequestParams;
import com.shssjk.activity.R;
import com.shssjk.activity.common.information.ArticleDetailActivity;
import com.shssjk.activity.common.information.ArticleSearchActivity;
import com.shssjk.adapter.ArticleAdapter;
import com.shssjk.adapter.CategoryAdapter;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.information.InformationRequest;
import com.shssjk.model.info.Article;
import com.shssjk.model.info.Information;
import com.shssjk.model.order.SPOrder;
import com.shssjk.view.MobileScrollLayout;


import java.util.List;

/**
 * 资讯
 */


public class InforFragment extends BaseFragment implements MobileScrollLayout.PageListener,View.OnClickListener  {
    private Context mContext;
    private Button menuBtn;
    private Button searchBtn;
    //	侧滑  分类
    static SlidingMenu menu;

    ListView categoryListv;

    CategoryAdapter mCategoryAdapter ;
    //资讯 列表
    private ListView articlesListv;
    private PtrClassicFrameLayout articlesPcf;
    private List<Article> articles;
    private Article  mArticle;
    private Information  mInformation;
    private TextView titileTv;


    //
    private List<Information> mInformations;
    private ArticleAdapter mArticleAdapter;

    int alertType;
    int pageIndex=20;   //取几条数据
    /**
     * 最大页数
     */
    boolean maxIndex;
    private String ofseet="0";


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
        View view = inflater.inflate(R.layout.fragment_infor, null, false);
        initSlidingMenu();
        super.init(view);
        return view;
    }
    @Override
    public void initSubView(View view) {
        menuBtn= (Button) view.findViewById(R.id.titlebar_menu_btn);
        searchBtn= (Button) view.findViewById(R.id.titlebar_menu_btn_right);
        articlesListv = (ListView) view.findViewById(R.id.articles_listv);
        articlesPcf = (PtrClassicFrameLayout) view.findViewById(R.id.articles_pcf);
        titileTv = (TextView) view.findViewById(R.id.titile_tv);
        View emptyView = view.findViewById(R.id.empty_lstv);
        articlesListv.setEmptyView(emptyView);

    }
    @Override
    public void initEvent() {
        menuBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        articlesListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = (Article) mArticleAdapter.getItem(position);
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra("article_id", article.getArticleId());
                intent.putExtra("article_title", article.getTitle());
                intent.putExtra("id", article.getCatId());
                startActivity(intent);
            }
        });


        articlesPcf.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                refreshData();
            }
        });

        articlesPcf.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                //上拉加载更多
                loadMoreData();
            }
        });


        mArticleAdapter =
                new ArticleAdapter(mContext);
        articlesListv.setAdapter(mArticleAdapter);

    }

    @Override
    public void initData() {
        getArticleList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.titlebar_menu_btn:
                startMenu();
                break;
            case  R.id.titlebar_menu_btn_right:
                startupArticleSearchActivity();
                break;
        }
    }

    @Override
    public void page(int page) {

    }
    //	显示侧滑菜单
    public static void startMenu(){
        menu.toggle();
    }

    /**
     * 侧滑菜单
     */
    private void initSlidingMenu() {
        menu = new SlidingMenu(mContext);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        menu.attachToActivity((Activity) mContext, SlidingMenu.SLIDING_WINDOW);
//        menu.attachToActivity((Activity)mContext, SlidingMenu.SLIDING_WINDOW);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.left_menu);
        categoryListv = (ListView) menu.findViewById(R.id.category_listv);
        mCategoryAdapter = new CategoryAdapter(mContext);
        categoryListv.setAdapter(mCategoryAdapter);
        categoryListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mInformation = (Information) mCategoryAdapter.getItem(position);
                titileTv.setText(mInformation.getCatName());
                refreshData();
                menu.toggle(false);
            }
        });


    }
    /**
     * 获取资讯分类列表
     */
    public void getInfomationCategory() {

        showLoadingToast("正在加载数据...");

        InformationRequest.getCategoryList(
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mInformations = (List<Information>) response;

                            mCategoryAdapter.setData(mInformations);
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

    public void getArticleList() {

//        pageIndex = 1;
        maxIndex = false;
        ofseet="0";
//        showLoadingToast();
        InformationRequest.getArticleList(mInformation, ofseet,"", pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {

                    articles = (List<Article>) response;
                    mArticleAdapter.setData(articles);
//                    articlesPcf.setLoadMoreEnable(true);
//                    hideLoadingToast();
                    getInfomationCategory();

                }

            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
//                hideLoadingToast();
                showToast(msg);
            }
        });
    }


    /**
     *  刷新数据
     */
    public void refreshData(){
//        pageIndex = 1;
//        maxIndex = false;
//        ofseet="0";
        showLoadingToast();
        InformationRequest.getArticleList(mInformation,ofseet,"",pageIndex, new SPSuccessListener() {
             @Override
             public void onRespone(String msg, Object response) {
                 if (response != null) {

                     articles = (List<Article>) response;
                     mArticleAdapter.setData(articles);
//                     articlesPcf.setLoadMoreEnable(true);

                 } else {
//                     maxIndex = true;
//                     articlesPcf.setLoadMoreEnable(false);
                 }

                 articlesPcf.refreshComplete();

                 hideLoadingToast();
             }
         }, new SPFailuredListener() {
             @Override
             public void onRespone(String msg, int errorCode) {
                 if(msg.equals("空数据")){
                     articles.clear();
                     mArticleAdapter.setData(articles);
                 }
                 hideLoadingToast();
                 showToast(msg);
             }
         });
    }




    public void loadMoreData(){
//        if(maxIndex)
//        {
//            return;
//        }
//        pageIndex++;
        if(articles.size()>0){
            int size =articles.size();
            ofseet = articles.get(size-1).getArticleId();
        }
//        String type = SPOrderUtils.getOrderTypeWithOrderStatus(orderStatus);
        RequestParams params = new RequestParams();

//        if (!SPStringUtils.isEmpty(type)) {
//            params.put("type" , type);
//            params.put("p" , pageIndex);
//        }

        showLoadingToast();
        InformationRequest.getArticleList(mInformation,ofseet,"",pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null && ((List<SPOrder>) response).size() > 0) {
                    List<Article> tempArticles = (List<Article>) response;
                    articles = (List<Article>) response;
//                    mArticleAdapter.setData(tempArticles);
//                    articlesPcf.setLoadMoreEnable(true);
                    articles.addAll(tempArticles);
                    mArticleAdapter.setData(articles);
//                    articlesPcf.setLoadMoreEnable(true);
                } else {
//                    pageIndex--;
//                    maxIndex = true;
//                    articlesPcf.setLoadMoreEnable(false);
                }
                articlesPcf.refreshComplete();
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
     * 搜索
     */
    public void startupArticleSearchActivity(){
        if (!MobileApplication.getInstance().isLogined){
            showToastUnLogin();
            toLoginPage();
            return;
        }
        Intent carIntent = new Intent(getActivity() , ArticleSearchActivity.class);
        getActivity().startActivity(carIntent);
    }


}
