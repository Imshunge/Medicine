package com.shssjk.activity.common.information;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.loopj.android.http.RequestParams;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.adapter.ArticleAdapter;
import com.shssjk.adapter.CategoryAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.information.InformationRequest;
import com.shssjk.model.info.Article;
import com.shssjk.model.info.Information;
import com.shssjk.model.order.SPOrder;
import com.shssjk.utils.SPDialogUtils;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

public class ArticleSearchActivity extends BaseActivity  {
    ListView searchkeyListv;
    Context mContext;

    CategoryAdapter mCategoryAdapter ;
    //资讯 列表
    private ListView articlesListv;
    private PtrClassicFrameLayout articlesPcf;
    private List<Article> articles;
    private Article  mArticle;
    private Information mInformation;
    private TextView titileTv;
   //    分类
    private List<Information> informations;
    private ArticleAdapter mArticleAdapter;

    EditText searchText ;//搜索文本框
    ImageView backImgv;	//返回键

    int pageIndex=20;   //取几条数据
    /**
     * 最大页数
     */
    boolean maxIndex;
    private String ofseet="0"; //起始位置

    private ImageView iamgeSearch;
    private String searchStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 自定义标题栏 , 执行顺序必须是一下顺序, 否则无效果.  */
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_article_search);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.product_list_header);

        mContext=this;
        super.init();
    }

    @Override
    public void initSubViews() {
//        searchView = (SPSearchView) findViewById(R.id.search_view);
//        searchView.getSearchEditText().setFocusable(true);
//        searchkeyListv = (ListView) findViewById(R.id.search_key_listv);
//        iamgeSearch = (ImageView) findViewById(R.id.search_icon);
//        searchedTextView = (TextView) findViewById(R.id.search_edtv);
        iamgeSearch = (ImageView) findViewById(R.id.search_icon);

        backImgv = (ImageView)findViewById(R.id.title_back_imgv);
        searchText = (EditText)findViewById(R.id.search_edtv);
        searchText.setHint(R.string.hint_search_information);
        searchText.setFocusable(true);



        searchText.setFocusableInTouchMode(true);

        searchText.setCursorVisible(true);

        articlesListv = (ListView) findViewById(R.id.articles_listv);
        articlesPcf = (PtrClassicFrameLayout) findViewById(R.id.articles_pcf);


        View emptyView = findViewById(R.id.empty_lstv);
        articlesListv.setEmptyView(emptyView);
    }

    @Override
    public void initData() {
//        getArticleList();
    }

    @Override
    public void initEvent() {
        backImgv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArticleSearchActivity.this.finish();
            }
        });
//        searchText.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
////                搜索页面
//            }
//        });
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
//        articlesPcf.checkCanDoRefresh
//        articlesPcf.set
//        articlesPcf.setPullToRefresh(false);
//        articlesPcf.setPtrHandler()
        articlesPcf.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
//                getArticleList();
//                loadMoreData();
                articlesPcf.refreshComplete();
            }
        });
//
        articlesPcf.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                //上拉加载更多
                loadMoreData();
            }
        });
        mArticleAdapter =  new ArticleAdapter(mContext);
        articlesListv.setAdapter(mArticleAdapter);

        //        搜索图片 监听
        iamgeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SPStringUtils.isEmpty(searchText.getText().toString().trim())) {
                    SPDialogUtils.showToast(mContext, "搜索内容不能为空");
                    return;
                }
//                startSearch(searchText.getText().toString().trim());
                searchStr = searchText.getText().toString().trim();
                getArticleList();
            }
        });
//       键盘搜索按钮监听
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) searchText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //跳转activity
                    if (SPStringUtils.isEmpty(searchText.getText().toString().trim())) {
                        SPDialogUtils.showToast(mContext, "搜索内容不能为空");
                    }else{
//                        SPDialogUtils.showToast(mContext, "开始搜索");
                        searchStr = searchText.getText().toString().trim();
                        getArticleList();
                    }
                    return true;
                }
                return false;
            }
        });
    }


    public void getArticleList() {

//        pageIndex = 1;
//        maxIndex = false;
        ofseet="0";
        showLoadingToast();
        InformationRequest.getArticleList(mInformation, ofseet,searchStr, pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {

                    articles = (List<Article>) response;
                    mArticleAdapter.setData(articles);
//                    articlesPcf.setLoadMoreEnable(true);
                    hideLoadingToast();
                }
//                articlesPcf.refreshComplete();


            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
//                hideLoadingToast();
                showToast(msg);
//                articlesPcf.refreshComplete();

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
        showLoadingToast();
        InformationRequest.getArticleList(mInformation,ofseet,searchStr,pageIndex, new SPSuccessListener() {
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
//                pageIndex--;
            }
        });
    }


}
