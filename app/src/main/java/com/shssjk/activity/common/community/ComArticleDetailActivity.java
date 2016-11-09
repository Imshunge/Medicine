package com.shssjk.activity.common.community;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.adapter.ComCommentAdapter;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.shssjk.http.information.InformationRequest;
import com.shssjk.model.community.ComArticle;
import com.shssjk.model.community.ComComment;
import com.shssjk.model.info.Comment;
import com.shssjk.utils.MyColor;
import com.shssjk.utils.SPConfirmDialog;
import com.shssjk.utils.SSUtils;
import com.shssjk.view.ListViewNobar;

import java.util.ArrayList;
import java.util.List;


/**
 * 江湖  帖子详情
 */
public class ComArticleDetailActivity extends BaseActivity implements   View.OnClickListener , ComCommentAdapter.CommentClickListener,SPConfirmDialog.ConfirmDialogListener{
    private String  articleId;    //帖子ID
    private String cid;//江湖cid
    private String uid;//江湖cid

    private ComArticle mComArticle;//帖子

    FrameLayout titlbarFl;
    private TextView titleTxtv;
    private Context mContext;

    private String articleTitle;
    private ImageView likeImgv;//收藏图标
    private Button backBtn;//返回按钮
    private  Button  likeBtn;//收藏按钮
    private ListViewNobar mPinlunLists;


    private TextView mInfoTextView;
    private TextView mTitleTextView;

    //  评论
    private TextView mComNumTxtv;//评论数
    private TextView mSendComTxtv;//发表评论
    private EditText mComEdiT;//评论内容
    private ComCommentAdapter mCommentAdapter;

    private List<ComComment> mComment= new ArrayList<>();
    private String type="TIEZI";
    private TextView mCommentLoadMore;
    private  String  r ="20";
    private String offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.article_tiezi_detail), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_article_detail);
        mContext=this;
        super.init();
    }
    @Override
    public void initSubViews() {
        this.articleId =   getIntent().getStringExtra("article_id");
        this.cid =   getIntent().getStringExtra("cid");
        this.uid =   getIntent().getStringExtra("uid");


        likeBtn= (Button) findViewById(R.id.titlebar_menu_btn);
        likeBtn.setBackgroundResource(R.drawable.product_unlike);
        mTitleTextView= (TextView)findViewById(R.id.infor_title);
        mInfoTextView = (TextView)findViewById(R.id.infor_text);
        likeBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        backBtn= (Button) findViewById(R.id.titlebar_back_btn);


        mPinlunLists = (ListViewNobar) findViewById(R.id.list_pinglun);
        View footerView =  ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_footer, null, false);
        mPinlunLists.addFooterView(footerView);
        mCommentLoadMore= (TextView) footerView.findViewById(R.id.footer_hint_textview);
        mCommentLoadMore.setOnClickListener(this);
        mComNumTxtv = (TextView)findViewById(R.id.nums_pinglun);
        mSendComTxtv = (TextView)findViewById(R.id.send_pinglun);
        mComEdiT = (EditText)findViewById(R.id.edit_pinglun);
    }

    @Override
    public void initData() {
        getComArticleDetail();
    }

    @Override
    public void initEvent() {
        mCommentAdapter = new ComCommentAdapter(mContext,this);
        mPinlunLists.setAdapter(mCommentAdapter);

        mSendComTxtv.setOnClickListener(this);
        likeBtn.setOnClickListener(this);
        mComEdiT.addTextChangedListener(watcher);// 设置评论输入时的动态显示

    }

    //文章内容
    public void getComArticleDetail() {

        showLoadingToast("正在加载数据...");

        CommunityRequest.getComArticleDetailByID(articleId,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mComArticle = (ComArticle) response;
                            loadData(mComArticle);

                            getComArticleCommunity();
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
    public void loadData(final  ComArticle mHtml) {
//        this.mHtml = ((ProductActivity) getActivity()).getContents();
        new Thread() {
            @Override
            public void run() {
                // Auto-generated method stub
                String html = mHtml.getDescription();
                html = html.replaceAll("<blockquote>", "").replaceAll(
                        "</blockquote>", "");
                final Spanned sp = Html.fromHtml(html, new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        return null;
                    }
                }, null);
                mInfoTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        mInfoTextView.setText(sp, TextView.BufferType.SPANNABLE);
                    }
                });
            }
        }.start();
        mTitleTextView.setText(mComArticle.getTitle());
//
    }
//评论列表
    private void getComArticleCommunity() {
        CommunityRequest.getComArticleCommentWitArticleID(articleId, "", "",
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mComment = (List<ComComment>) response;
                            mCommentAdapter.setData(mComment, uid);//
                            mComEdiT.setText("");
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
    private void getArticleCommunityLoadMore() {
        if (SSUtils.isEmpty(mComment)) {
            showToast("暂无评论");
        }
        if (mComment.size() > 0) {
            offset = mComment.size() + "";
        }
        InformationRequest.getArticleCommentWitArticleID(articleId, offset, r,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            List<ComComment> tempComment = (List<ComComment>) response;
                            mComment.addAll(tempComment);
                            mCommentAdapter.setData(mComment,uid);//
                        }
                        if (msg.equals("空数据")) {
                            showToast("没有更多的数据了");
                        }
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                    }
                });
    }
    //发表评论
    private void sendPingLunData(String content) {
        CommunityRequest.CompublishComment(articleId, "", content, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
//                        refreshCollectDatta();
                hideLoadingToast();
                showToast(msg);
                getComArticleCommunity();
            }
        }, new SPFailuredListener(ComArticleDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
//                showToast(msg);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            case R.id.titlebar_menu_btn :
                //收藏
                if (!MobileApplication.getInstance().isLogined) {
                    showToastUnLogin();
                    toLoginPage();
                    return;
                }

                String  act = "";
                if (mComArticle.getIs_collect().equals("1")) {//收藏 -> 取消收藏
//                    showLoadingToast("正在取消收藏");
                    act = "cancel";
                } else {
//                    showLoadingToast("正在添加收藏");
                    act = "add";
                }
                collectOrCancelArtucile(type, act);
                break;

            case R.id.footer_hint_textview:
                getArticleCommunityLoadMore();
                break;
        }
    }


//    点赞
    public void praiseCommentLocal(final ComComment comment, final String act) {
        CommunityRequest.ComcollectOrCancelPraiseWithID(comment.getId(), act, "MENPAI", new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
//                        refreshCollectDatta();
//                hideLoadingToast();
//                showToast(msg);
//                getComArticleCommunity();
                if(msg.equals("成功")){
                    int size = SSUtils.str2Int(comment.getLaud()).intValue();
                    int location   =      mComment.lastIndexOf(comment);
//                    mComment.remove(location);
                    if(act.equals("add")){
                        comment.setCstate(1);
                        comment.setLaud(size + 1 + "");
                    }   // 1 已点 0 未点
                    if(act.equals("cancel")){
                        comment.setCstate(0);
                        comment.setLaud(size - 1 + "");
                    }
                    mComment.set(location,comment);
                    mCommentAdapter.setData(mComment,uid);//
                }
            }
        }, new SPFailuredListener(ComArticleDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
//                hideLoadingToast();
                showToast(msg);
            }
        });
    }
//   点赞 按钮监听
    @Override
    public void praiseComment(ComComment comment) {
        String act="add";
//        当前用户是否已点赞（1 已点、0 未点
        if(comment.getCstate()==1){
            act="cancel";
        }
        praiseCommentLocal(comment, act);
    }
//   删除 按钮监听

    @Override
    public void deleteComment(ComComment comment) {
        deleteCommentLocal(comment);
    }
//    删除评论
    private void deleteCommentLocal(final ComComment comment) {
        CommunityRequest.ComDeleteCommnentWithID(cid, comment.getId(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
//                        refreshCollectDatta();
                hideLoadingToast();
                showToast(msg);
//                getComArticleCommunity();
                mComment.remove(comment.getId());
                mCommentAdapter.setData(mComment, uid);//
            }
        }, new SPFailuredListener(ComArticleDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                showToast(msg);
            }
        });
    }

    //收藏方法
    private void collectOrCancelArtucile(String type,String act) {
        InformationRequest.collectOrCancelArticleWithID(mComArticle.getId(), type, act, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {

                showToast(msg);
                if (msg.equals("取消收藏成功")) {
//                    / 1已收藏 0未收藏
                    mComArticle.setIs_collect("0");
                } else if (msg.equals("成功")) {
                    mComArticle.setIs_collect("1");
                }
                refreshCollectButton();
            }
        }, new SPFailuredListener(ComArticleDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }
    /**
     * 刷新收藏按钮
     */
    public void refreshCollectButton() {
        if (mComArticle.getIs_collect().equals("1")) {
            //收藏

            likeBtn.setBackgroundResource(R.drawable.product_like);
        } else {
            //未收藏

            likeBtn.setBackgroundResource(R.drawable.product_unlike);

        }
    }

    @Override
    public void clickOk(int actionType) {

    }

    // 发表评论按钮事件
    TextWatcher watcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {

//            mSendComTxtv.setBackgroundColor(MyColor.COLOR7);
//            mSendComTxtv.setTextColor(MyColor.WHITE);
            // Auto-generated method stub
            if (temp.length() > 0) {
                mSendComTxtv.setBackgroundColor(MyColor.COLOR7);
                mSendComTxtv.setTextColor(MyColor.WHITE);
            } else {
                mSendComTxtv.setBackgroundColor(MyColor.WHITE);
                mSendComTxtv.setTextColor(MyColor.BLACK_64);
            }
//            nums_pinglun.setText(String.valueOf(128 - edit_pinglun.length()));
        }

    };






}
