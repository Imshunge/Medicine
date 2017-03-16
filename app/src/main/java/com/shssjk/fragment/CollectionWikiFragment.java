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
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.shssjk.activity.R;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.information.ArticleDetailActivity;
import com.shssjk.adapter.ArticleAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.information.InformationRequest;
import com.shssjk.model.info.Article;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的收藏 百科  BAIKE
 */
public class CollectionWikiFragment extends BaseFragment {

    @Bind(R.id.order_listv)
    ListView infoListv;
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
    private String type = "BAIKE";
    private String offset = "";    //起始地址
    private String r = "10";   //偏移量
    private ArticleAdapter mArticleAdapter;
    private Context mContext;
    private List<Article> mArticleList;

    public CollectionWikiFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static CollectionWikiFragment newInstance(String param1, String param2) {
        CollectionWikiFragment fragment = new CollectionWikiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_information, null, false);
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
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void initSubView(View view) {
//        emptyLstv.setVisibility(View.VISIBLE);
//        infoListv.setEmptyView(emptyLstv);
//        View emptyView = view.findViewById(R.id.empty_lstv);
//        infoListv.setEmptyView(emptyView);
    }

    @Override
    public void initEvent() {
        testListViewFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                getInfoList();
                testListViewFrame.refreshComplete();
            }
        });
//
        testListViewFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //上拉加载更多
                loadMoreData();
            }
        });
        mArticleAdapter = new ArticleAdapter(mContext);
        infoListv.setAdapter(mArticleAdapter);
        infoListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mArticleList.size() == 0 || position == mArticleList.size()) {
                    return;
                }
                Article article = (Article) mArticleList.get(position);
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra("article_id", article.getArticleId());
                intent.putExtra("article_title", article.getTitle());
                intent.putExtra("id", article.getCatId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        getInfoList();
    }

    /**
     * 获取收藏列表
     */
    public void getInfoList() {
        showLoadingToast("正在加载数据...");
        InformationRequest.getCollectArticleList(type, "", "",
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mArticleList = (List<Article>) response;
                            mArticleAdapter.setData(mArticleList);
                            testListViewFrame.setLoadMoreEnable(true);
                        } else {
                            showToast(msg);
                        }
                        if (mArticleList.size() == 0) {
                            llListview.setVisibility(View.GONE);
                            emptyLstv.setVisibility(View.VISIBLE);
                        } else {
                            llListview.setVisibility(View.VISIBLE);
                            emptyLstv.setVisibility(View.GONE);
                        }
                        testListViewFrame.refreshComplete();
                    }
                }, new SPFailuredListener((IViewController) mContext) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        if (!"空数据".equals(msg)) {
                            showToast(msg);
                        }
//                        showToast(msg);
                        testListViewFrame.refreshComplete();
                    }
                });
    }

    //    加载更多
    public void loadMoreData() {

        if (mArticleList.size() > 0) {
            int size = mArticleList.size();
            offset = size + "";
        }
        showLoadingToast();
        InformationRequest.getCollectArticleList(type, offset, r, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<Article> tempArticles = (List<Article>) response;
                    if (tempArticles.size() > 0) {
                        mArticleList.addAll(tempArticles);
                        mArticleAdapter.setData(mArticleList);
                    } else {
                        testListViewFrame.setLoadMoreEnable(false);
                    }
                }
                if (mArticleList.size() == 0) {
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
                hideLoadingToast();
                testListViewFrame.loadMoreComplete(true);
                if (!"空数据".equals(msg)) {
                    showToast(msg);
                }
            }
        });
    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }
}
