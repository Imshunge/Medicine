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
import com.shssjk.adapter.SchollAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.shssjk.model.community.SchoolList;

import java.util.List;

/**
 * 门派列表
 */
public class QuackListActivity extends BaseActivity implements View.OnClickListener {
    //门派列表
    private ListView articlesListv;
    private PtrClassicFrameLayout articlesPcf;
    private List<SchoolList> mSchoolList;
    private SchollAdapter mSchollAdapter;
    private Button rithtBtn;
    private Context mContext;
    private String category_id;
    private String offset="0";  //起始位置
    private int r=10;   //取几条数据
    private List<SchoolList> mQuacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.quack_menpai_list), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quack_list);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        articlesListv = (ListView) findViewById(R.id.school_listv);
        articlesPcf = (PtrClassicFrameLayout) findViewById(R.id.quack_list_view_ptr);
        rithtBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        this.category_id =   getIntent().getStringExtra("category_id");

        articlesPcf.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                getgetSchoolList();
            }
        });
        articlesPcf.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //上拉加载更多
                loadMoreData();
            }
        });



    }

    private void loadMoreData() {
        if (mQuacks.size() > 0) {
            int size = mQuacks.size();
            offset = mQuacks.get(size - 1).getId();
        }
        CommunityRequest.getSchoolList(offset, "", r, category_id,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            List<SchoolList>   Quack = (List<SchoolList>) response;
                            mQuacks.addAll(Quack);
                            mSchollAdapter.setData(mQuacks);
                        }
                        articlesPcf.refreshComplete();
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });

    }

    @Override
    public void initData() {
        getgetSchoolList();
    }

    @Override
    public void initEvent() {
        mSchollAdapter =
                new SchollAdapter(mContext);
        articlesListv.setAdapter(mSchollAdapter);
        articlesListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolList article = (SchoolList) mSchollAdapter.getItem(position);
                Intent intent = new Intent(mContext, ComArticleListActivity.class);
//                "uid": "200059",		 创建人(门主)
                intent.putExtra("uid", article.getUid());
                intent.putExtra("cid", article.getId());
                startActivity(intent);
            }
        });
        rithtBtn.setOnClickListener(this);

    }

    public void getgetSchoolList() {

        showLoadingToast("正在加载数据...");

        CommunityRequest.getSchoolList(offset, "", r, category_id,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mQuacks = (List<SchoolList>) response;
//
                            mSchollAdapter.setData(mQuacks);
                        }
                        articlesPcf.refreshComplete();

                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_menu_btn:
//           添加门派
                goToaddSchollActivity();
                break;
        }
    }

    private void goToaddSchollActivity() {
        ////     发表帖子
        Intent addComArticleintent = new Intent(mContext, AddSchoolActivity.class);
        addComArticleintent.putExtra("category_id", category_id);
        startActivity(addComArticleintent);
    }
}
