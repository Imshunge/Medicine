package com.shssjk.activity.common.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.adapter.ComArticleAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.shssjk.model.community.ComArticle;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.view.PopupMenu;


import java.util.List;

/**
 * 江湖模块  帖子列表
 */
public class ComArticleListActivity extends BaseActivity implements View.OnClickListener,
        ComArticleAdapter.ComArtilceClickListener ,ConfirmDialog.ConfirmDialogListener{
    //江湖模块
    private ListView articlesListv;
    private PtrClassicFrameLayout articlesPcf;
    private ComArticleAdapter mComArticleAdapter;
    private Context mContext;
    private List<ComArticle> mQuacks;
    private ComArticle tempComArticle;
    int pageIndex=10;   //返回几条数据
    /**
     * 最大页数
     */
    boolean maxIndex;
    private String ofseet = "0";
    private Button rithtBtn;
    private String uid;
    private String cid; //门派id
    PopupMenu popupMenu; //弹出菜单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.article_menpai_list), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_article);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        this.cid = getIntent().getStringExtra("cid");
        this.uid = getIntent().getStringExtra("uid");
        popupMenu = new PopupMenu(this);
        articlesListv = (ListView) findViewById(R.id.school_articl_listv);
        articlesPcf = (PtrClassicFrameLayout) findViewById(R.id.articl_list_view_ptr);
        rithtBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        View emptyView = findViewById(R.id.empty_lstv);
        articlesListv.setEmptyView(emptyView);
    }

    @Override
    public void initData() {
         getgetSchoolArticleList();
    }

    @Override
    public void initEvent() {
        mComArticleAdapter =
                new ComArticleAdapter(mContext, this);
        articlesListv.setAdapter(mComArticleAdapter);
        articlesListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComArticle article = (ComArticle) mComArticleAdapter.getItem(position);
                Intent intent = new Intent(mContext, ComArticleDetailActivity.class);
                intent.putExtra("article_id", article.getId());
                intent.putExtra("cid", article.getCid());
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
        articlesPcf.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                getgetSchoolArticleList();
            }
        });
        articlesPcf.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //上拉加载更多
                loadMoreData();
            }
        });
        rithtBtn.setOnClickListener(this);
    }

    public void getgetSchoolArticleList() {
        showLoadingToast("正在加载数据...");
        CommunityRequest.getSchoolArticlList(ofseet, "", pageIndex, cid,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mQuacks = (List<ComArticle>) response;
                            mComArticleAdapter.setData(mQuacks, uid);
                        } else {

                        }
                        articlesPcf.refreshComplete();
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

    public void loadMoreData() {
//        if (maxIndex) {
//            return;
//        }
//        pageIndex++;
        if (mQuacks.size() > 0) {
            int size = mQuacks.size();
            ofseet = mQuacks.get(size - 1).getId();
        }

        showLoadingToast();
        CommunityRequest.getSchoolArticlList(ofseet, "", pageIndex, cid, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null && ((List<ComArticle>) response).size() > 0) {
                    List<ComArticle> tempArticles = (List<ComArticle>) response;
//                    mArticleAdapter.setData(tempArticles);
//                    articlesPcf.setLoadMoreEnable(true);
                    mQuacks.addAll(tempArticles);
                    mComArticleAdapter.setData(mQuacks, cid);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_menu_btn:
                popupMenu.showLocation(R.id.titlebar_menu_btn);// 设置弹出菜单弹出的位置
                popupMenu.setAnimationStyle(R.anim.anim_pop);  //设置加载动画
                // 设置回调监听，获取点击事件
                popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
                    @Override
                    public void onClick(PopupMenu.MENUITEM item, String str) {

                        switch (str) {
                            case "1":
                                ////                门派详情
                                Intent intent = new Intent(mContext, SchoolDetailActivity.class);
                                intent.putExtra("cid", cid);
                                intent.putExtra("uid", uid);
                                startActivity(intent);
                                break;
                            case "2":
////     发表帖子
                                Intent addComArticleintent = new Intent(mContext, AddComArticleActivity.class);
                                addComArticleintent.putExtra("cid", cid);
                                startActivityForResult(addComArticleintent, MobileConstants.Result_Code_Refresh);

                                break;
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void deleteComment(ComArticle comArticle) {
//        comArticle.getId();

        showConfirmDialog("您确定要删除帖子吗？", "删除提醒", this, MobileConstants.tagDelete);
        this.tempComArticle=comArticle;

    }

    private void deleteArticle(ComArticle comArticle) {
        CommunityRequest.ComDeleteArticleWithID(cid, comArticle.getId(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
//                        refreshCollectDatta();
                hideLoadingToast();
                showToast(msg);
                getgetSchoolArticleList();
            }
        }, new SPFailuredListener(ComArticleListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MobileConstants.Result_Code_Refresh){
            getgetSchoolArticleList();
        }
    }


    @Override
    public void clickOk(int actionType) {
        if (actionType == MobileConstants.tagDelete){
            deleteArticle(tempComArticle);        }
    }
}
