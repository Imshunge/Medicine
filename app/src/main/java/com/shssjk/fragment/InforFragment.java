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
import com.shssjk.activity.R;
import com.shssjk.activity.information.ArticleDetailActivity;
import com.shssjk.activity.information.ArticleSearchActivity;
import com.shssjk.adapter.ArticleAdapter;
import com.shssjk.adapter.CategoryAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.information.InformationRequest;
import com.shssjk.model.info.Article;
import com.shssjk.model.info.Information;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.MobileScrollLayout;


import java.util.List;
/**
 * 资讯
 */
public class InforFragment extends BaseFragment implements MobileScrollLayout.PageListener, View.OnClickListener {
    private static final int DATACHANGE = 888;
    private Context mContext;
    private Button menuBtn;
    private Button searchBtn;
    //	侧滑  分类
    static SlidingMenu menu;
    ListView categoryListv;
    CategoryAdapter mCategoryAdapter;
    //资讯 列表
    private ListView articlesListv;
    private PtrClassicFrameLayout articlesPcf;
    private List<Article> articles;
    private Article mArticle;
    private Information mInformation;
    private TextView titileTv;
    //
    private List<Information> mInformations;
    private ArticleAdapter mArticleAdapter;

    int alertType;
    String r = "20";   //取几条数据
    /**
     * 最大页数
     */
    boolean maxIndex;
    private String ofseet = "0";
    private Article article;

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
        menuBtn = (Button) view.findViewById(R.id.titlebar_menu_btn);
        searchBtn = (Button) view.findViewById(R.id.titlebar_menu_btn_right);
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
                if(position>=articles.size()){
                    return;
                }
                article = (Article) mArticleAdapter.getItem(position);
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra("article_id", article.getArticleId());
                intent.putExtra("article_title", article.getTitle());
                intent.putExtra("id", article.getCatId());
//                startActivity(intent);
                startActivityForResult(intent,DATACHANGE);
            }
        });
        articlesPcf.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 150);
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
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //模拟数据
////                        for (int i = 0; i <= 5; i++) {
////                            title.add(String.valueOf(i));
////                        }
////                        mAdapter.notifyDataSetChanged();
////                        mPtrFrame.loadMoreComplete(true);
//////                        Toast.makeText(MainActivity.this, "load more complete", Toast.LENGTH_SHORT)
//////                                .show();
////                        loadMoreData();
//                    }
//                }, 1000);
                loadMoreData();
            }
        });
        mArticleAdapter =
                new ArticleAdapter(mContext);
        articlesListv.setAdapter(mArticleAdapter);
    }
    @Override
    public void initData() {
//       初始化数据：
        getArticleList();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_menu_btn:
                startMenu();
                break;
            case R.id.titlebar_menu_btn_right:
                startupArticleSearchActivity();
                break;
        }
    }

    @Override
    public void page(int page) {

    }

    //	显示侧滑菜单
    public static void startMenu() {
        menu.toggle();
    }
    /**
     * 侧滑菜单
     */
    private void initSlidingMenu() {
        menu = new SlidingMenu(mContext);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
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
////       取第一条数据
        InformationRequest.getArticleList(mInformation, "0", "", r, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    Logger.e("refreshData response", response.toString());
                    articles = (List<Article>) response;
                    mArticleAdapter.setData(articles);
                    articlesPcf.setLoadMoreEnable(true);
                } else {
                    articlesPcf.setLoadMoreEnable(false);
                }
                articlesPcf.refreshComplete();
                hideLoadingToast();
                getInfomationCategory();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
    /**
     * 刷新数据
     */
    public void refreshData() {
        showLoadingToast();
        InformationRequest.getArticleList(mInformation, "0", "", r, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    Logger.e("refreshData response", response.toString());
                    articles = (List<Article>) response;
                    mArticleAdapter.setData(articles);
                    articlesPcf.setLoadMoreEnable(true);
                }
                articlesPcf.refreshComplete();
                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                if ("空数据".equals(msg)) {
                    articles.clear();
                    mArticleAdapter.setData(articles);
                }
                articlesPcf.refreshComplete();
                articlesPcf.setLoadMoreEnable(true);
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

//   加载更多
    public void loadMoreData() {
        if (articles.size() > 0) {
            int size = articles.size();
            ofseet = size + "";
            Logger.e("articles.size", ofseet);
        }
        showLoadingToast();
        InformationRequest.getArticleList(mInformation, ofseet, "", r, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                Logger.e("loadMoreData response", response.toString());
                if (response != null) {
                    List<Article> tempArticles = (List<Article>) response;
                    if(tempArticles.size()!=0&&articles!=null){
                        articles.addAll(tempArticles);
                        mArticleAdapter.setData(articles);
                        articlesPcf.setLoadMoreEnable(true);
                    }else if(tempArticles.size()==0){
                        articlesPcf.setLoadMoreEnable(false);
//                        setNoMoreData
//                        articlesPcf.setNoMoreData();
                        showToast("数据已全部加载");

                    }
                }else{
                    articlesPcf.setLoadMoreEnable(false);
                }
                articlesPcf.loadMoreComplete(true);  //
                hideLoadingToast();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                articlesPcf.loadMoreComplete(true);
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
    /**
     * 搜索
     */
    public void startupArticleSearchActivity() {
        Intent carIntent = new Intent(getActivity(), ArticleSearchActivity.class);
        getActivity().startActivity(carIntent);
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent resultCode) {
//        switch (requestCode) {
//            case 888: //
//                if (resultCode == Activity.RESULT_OK) {
//
//                }
//                break;
//        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case  DATACHANGE:
//                点开文章阅读量+1 ，本地更新数据
                Integer click  =SSUtils.str2Int(article.getClick());
                int nowclick=  ( click!=null)?click.intValue()+1:0;
                Logger.e(this,nowclick+"");
                article.setClick(nowclick+"");
                mArticleAdapter.setData(articles);//
                break;
        }
    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }
}
