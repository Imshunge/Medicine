package com.shssjk.activity.common.information;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.adapter.CommentAdapter;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.information.InformationRequest;
import com.shssjk.model.info.Article;
import com.shssjk.model.info.Comment;
import com.shssjk.utils.MyColor;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.ListViewNobar;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 资讯详情
 */
public class ArticleDetailActivity extends BaseActivity implements View.OnClickListener, CommentAdapter.CommentClickListener {
    FrameLayout titlbarFl;
    private TextView titleTxtv;
    private Context mContext;

    private String articleId;
    private String articleTitle;
    private String id;
    private Article mArticle;
    private ImageView likeImgv;//收藏图标
    private Button backBtn;//返回按钮
    private Button likeBtn;//收藏按钮
    private ListViewNobar mPinlunLists;

    private TextView mInfoTextView;
    private TextView mTitleTextView;
    private TextView tv_footer;
    //  评论
    private TextView mComNumTxtv;//评论数
    private TextView mSendComTxtv;//发表评论
    private EditText mComEdiT;//评论内容
    private CommentAdapter mCommentAdapter;
    private List<Comment> mComment = new ArrayList<Comment>();
    private HtmlTextView htmlTextView;//显示内容
    private TextView mCommentLoadMore;
    private String r = "20";
    private String offset;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.article_title_detail), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artil_detail);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        likeBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        likeBtn.setBackgroundResource(R.drawable.product_unlike);
        mTitleTextView = (TextView) findViewById(R.id.infor_title);
        backBtn = (Button) findViewById(R.id.titlebar_back_btn);
        this.articleId = getIntent().getStringExtra("article_id");
        this.articleTitle = getIntent().getStringExtra("article_title");
        this.id = getIntent().getStringExtra("id");
        mPinlunLists = (ListViewNobar) findViewById(R.id.list_pinglun);
        View footerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.listview_footer, null, false);
        mPinlunLists.addFooterView(footerView);
        mCommentAdapter = new CommentAdapter(mContext, this);
        mPinlunLists.setAdapter(mCommentAdapter);
        mCommentLoadMore = (TextView) footerView.findViewById(R.id.footer_hint_textview);

        mCommentLoadMore.setOnClickListener(this);

        mComNumTxtv = (TextView) findViewById(R.id.nums_pinglun);
        mSendComTxtv = (TextView) findViewById(R.id.send_pinglun);
        mComEdiT = (EditText) findViewById(R.id.edit_pinglun);

        mWebView = (WebView) findViewById(R.id.common_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setBuiltInZoomControls(false);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);



    }

    @Override
    public void initData() {
        getData();
    }
    /**
     * 获取数据
     */
    /**
     * 获取详情信息
     */
    public void getData() {
        showLoadingToast("正在加载数据...");
        InformationRequest.getArticleDetailByID(id, articleId,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mArticle = (Article) response;
                            refreshCollectButton();
                            loadData(mArticle.getContent());
                            getArticleCommunity();
                        } else {
                            showToast(msg);
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

    //获取评论列表
    private void getArticleCommunityLoadMore() {
//        if (SSUtils.isEmpty(mComment)) {
//            showToast("暂无评论");
//        }
        mCommentLoadMore.setVisibility(View.VISIBLE);
        if (mComment.size() > 0) {
            offset = mComment.size() + "";
        }
        InformationRequest.getArticleCommentWitArticleID(mArticle.getArticleId(), offset, r,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            List<Comment> tempComment = (List<Comment>) response;
                            if (tempComment.size() != 0 && mComment != null) {
                                Collections.reverse(tempComment); // 倒序排列
                                mComment.addAll(0, tempComment);
                                mCommentAdapter.setData(mComment);
                            } else if (tempComment.size() == 0) {
                                showToast("没有更多评论了");
                            }
                        } else {
                            showToast(msg);
                        }
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                    }
                });
    }
    //获取评论时间
    private void getArticleCommunity() {
        InformationRequest.getArticleCommentWitArticleID(mArticle.getArticleId(), "", "",
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mComment = (List<Comment>) response;
                            if (mComment.size() != 0) {
                                mComment = (List<Comment>) response;
                                Collections.reverse(mComment);   //逆序排列
                                mCommentAdapter.setData(mComment);//
                            } else if (mComment.size() == 0) {
                                mCommentLoadMore.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                    }
                });
    }

    @Override
    public void initEvent() {
        backBtn.setOnClickListener(this);
        likeBtn.setOnClickListener(this);
        mSendComTxtv.setOnClickListener(this);
        mCommentAdapter = new CommentAdapter(mContext, this);
        mPinlunLists.setAdapter(mCommentAdapter);
        mComEdiT.addTextChangedListener(watcher);// 设置评论输入时的动态显示
    }
    /**
     * 显示富文本内容
     * @param mHtml
     */
    public void loadData(final String mHtml) {
        mTitleTextView.setText(mArticle.getTitle());
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, mHtml, "text/html", "utf-8", null);
        }
    }
    /**
     * 刷新收藏按钮
     */
    public void refreshCollectButton() {
        if (mArticle.getIs_collect().equals("1")) {
            //收藏
            likeBtn.setBackgroundResource(R.drawable.product_like);
        } else {
            //未收藏
            likeBtn.setBackgroundResource(R.drawable.product_unlike);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_back_btn:
                finish();
                break;
            case R.id.titlebar_menu_btn:
                //收藏
                if (!MobileApplication.getInstance().isLogined) {
                    showToastUnLogin();
                    toLoginPage();
                    return;
                }
                String type = mArticle.getType();
                String act = "";
                if (mArticle.getIs_collect().equals("1")) {//收藏 -> 取消收藏
                    act = "cancel";
                } else {
                    act = "add";
                }
                collectOrCancelArtucile(type, act);
                break;
            case R.id.send_pinglun:
                if (!MobileApplication.getInstance().isLogined) {
                    showToastUnLogin();
                    toLoginPage();
                    return;
                }
                if (mComEdiT.length() == 0) {
                    showToast("评论不能为空啊亲！");
                    return;
                }
                sendPingLunData(mComEdiT.getText().toString().trim());
                break;
            case R.id.footer_hint_textview:
                getArticleCommunityLoadMore();
                break;
        }
    }

    //收藏方法 1已收藏 0未收藏
    private void collectOrCancelArtucile(String type, String act) {
        InformationRequest.collectOrCancelArticleWithID(mArticle.getArticleId(), type, act, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                showToast(msg);
                if (msg.equals("取消收藏成功")) {
                    mArticle.setIs_collect("0");
                } else if (msg.equals("成功")) {
                    mArticle.setIs_collect("1");
                }
                refreshCollectButton();
            }
        }, new SPFailuredListener(ArticleDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    //发表评论
    private void sendPingLunData(String content) {
        InformationRequest.publishComment(mArticle.getArticleId(), "", content, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                showToast(msg);
                mComEdiT.setText("");
                mComNumTxtv.setText("评论");
                getArticleCommunityLoadMore();
            }
        }, new SPFailuredListener(ArticleDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }
//    点赞 回调
    @Override
    public void praiseComment(Comment comment) {
        if (!MobileApplication.getInstance().isLogined) {
            showToastUnLogin();
            toLoginPage();
            return;
        }
        String type = "add";
//        当前用户是否已点赞（1 已点、0 未点
        if (comment.getCstate() == 1) {
            type = "cancel";
        }
        praiseCommentLocal(comment, type);
    }
//    点赞 成功 本地修改数据  省去再次调用接口
    public void praiseCommentLocal(final Comment comment, final String type) {
        InformationRequest.collectOrCancelPraiseWithID(comment.getCommentId(), type, mArticle.getType(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (msg.equals("成功")) {
                    int size = SSUtils.str2Int(comment.getClick()).intValue();
                    int location = mComment.lastIndexOf(comment);
                    if (type.equals("add")) {
                        comment.setCstate(1);
                        comment.setClick(size + 1 + "");
                    }   // 1 已点 0 未点
                    if (type.equals("cancel")) {
                        comment.setCstate(0);
                        comment.setClick(size - 1 + "");
                    }
                    mComment.set(location, comment);
                    mCommentAdapter.setData(mComment);//
                }
            }
        }, new SPFailuredListener(ArticleDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    // 发表评论按钮事件
    TextWatcher watcher = new TextWatcher() {
        private CharSequence temp;
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 0) {
                mSendComTxtv.setBackgroundColor(MyColor.COLOR7);
                mSendComTxtv.setTextColor(MyColor.WHITE);
            } else {
                mSendComTxtv.setBackgroundColor(MyColor.WHITE);
                mSendComTxtv.setTextColor(MyColor.BLACK_64);
            }
            int textSize = 128 - mComEdiT.length();
            if(textSize>0){
                mComNumTxtv.setText(String.valueOf(textSize));
            }else{
                String temp=mComEdiT.getText().toString().substring(0, 128);
                mComEdiT.setText(temp);
            }
        }
    };
}
