package com.shssjk.activity.common.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.adapter.SPSearchkeyListAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.SPSaveData;
import com.shssjk.utils.SPDialogUtils;
import com.shssjk.view.SPSearchView;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.ArrayList;
import java.util.List;



public class SearchCommonActivity extends BaseActivity implements SPSearchView.SPSearchViewListener {


    SPSearchkeyListAdapter mAdapter;
    List<String> mSearchkeys;
    ListView searchkeyListv;
    Context mContext;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MobileConstants.MSG_CODE_SEARCHKEY:
                    startSearch(msg.obj.toString());
                    break;
            }
        }
    };

    private SPSearchView searchView;
    private ImageView iamgeSearch;
    private ImageView iamgeBack;
    private TextView searchedTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_common);
        mContext=this;
        super.init();
    }

    @Override
    public void initSubViews() {
        searchView = (SPSearchView) findViewById(R.id.search_view);
        searchView.getSearchEditText().setFocusable(true);
        searchkeyListv = (ListView) findViewById(R.id.search_key_listv);
        iamgeSearch = (ImageView) findViewById(R.id.search_icon);
        iamgeBack= (ImageView) findViewById(R.id.back_imgv);
        searchedTextView = (TextView) findViewById(R.id.search_edtv);
    }

    @Override
    public void initData() {
        mAdapter = new SPSearchkeyListAdapter(this, mHandler);
        searchkeyListv.setAdapter(mAdapter);
        if (getIntent() != null && getIntent().getStringExtra("searchKey") != null) {
            String searchKey = getIntent().getStringExtra("searchKey");
            searchView.setSearchKey(searchKey);
        }

       /* InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(searchView.getSearchEditText()!=null)imm.showSoftInputFromInputMethod(searchView.getSearchEditText().getWindowToken(), 0);*/

        loadKey();
        mAdapter.setData(mSearchkeys);
    }
    @Override
    public void initEvent() {
        if (searchView.getSearchEditText() != null) {
            searchView.getSearchEditText().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER) {
                        String searchKey = searchView.getSearchEditText().getText().toString();
                        startSearch(searchKey);
                    }
                    return false;
                }
            });
        }
//        搜索图片 监听
        iamgeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SPStringUtils.isEmpty(searchedTextView.getText().toString().trim())) {
                    SPDialogUtils.showToast(mContext,"搜索内容不能为空");
                    return;
                }
                startSearch(searchedTextView.getText().toString().trim());

            }
        });
//       键盘搜索按钮监听
       searchedTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView  v, int actionId, KeyEvent event) {



                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        // 先隐藏键盘
//                    ((InputMethodManager) searchedTextView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(
//                                    mContext.getCurrentFocus()
//                                            .getWindowToken(),
//                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //跳转activity

                    if (SPStringUtils.isEmpty(searchedTextView.getText().toString().trim())) {
                        SPDialogUtils.showToast(mContext,"搜索内容不能为空");
                    }else {
                        startSearch(searchedTextView.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
        });

        iamgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackClick() {
        this.finish();
    }

    @Override
    public void onSearchBoxClick(String keyword) {
//        startSearch(keyword.trim());
    }

//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.search_view:
//
//                break;
//            case R.id.search_key_listv:
//
//                break;
//
//        }
//    }

    public void startSearch(String searchKey) {
        if (!SPStringUtils.isEmpty(searchKey)) {
            saveKey(searchKey);
        }
        Intent intent = new Intent(this, ProductListSearchResultActivity.class);
        intent.putExtra("searchKey", searchKey);
        startActivity(intent);
        this.finish();
    }

    public void saveKey(String key) {
        String searchKey = SPSaveData.getString(this, MobileConstants.KEY_SEARCH_KEY);
        if (!SPStringUtils.isEmpty(searchKey) && !searchKey.contains(key)) {
            searchKey += "," + key;
        } else {
            searchKey = key;
        }
        SPSaveData.putValue(this, MobileConstants.KEY_SEARCH_KEY, searchKey);
    }

    public void loadKey() {
        mSearchkeys = new ArrayList<String>();
        String searchKey = SPSaveData.getString(this, MobileConstants.KEY_SEARCH_KEY);
        if (!SPStringUtils.isEmpty(searchKey)) {
            String[] keys = searchKey.split(",");
            if (keys != null)
                for (int i = 0; i < keys.length; i++) {
                    if (!SPStringUtils.isEmpty(keys[i])) {
                        mSearchkeys.add(keys[i]);
                    }
                }
        }/*else{
            mSearchkeys.add("香水");
            mSearchkeys.add("充值卡");
        }*/

    }

}
