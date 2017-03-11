package com.shssjk.activity.common.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.adapter.HotSearchAdapter;
import com.shssjk.adapter.SPSearchkeyListAdapter;
import com.shssjk.adapter.SeachKeyAdapter;
import com.shssjk.adapter.SearchhosAdapter;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.home.SPHomeRequest;
import com.shssjk.model.SPServiceConfig;
import com.shssjk.model.Searchhistory;
import com.shssjk.utils.SPDialogUtils;
import com.shssjk.utils.SPServerUtils;
import com.shssjk.view.SPSearchView;
import com.soubao.tpshop.utils.SPStringUtils;

import org.litepal.crud.DataSupport;

import java.util.List;


public class SearchCommonActivity extends BaseActivity implements SPSearchView.SPSearchViewListener {
    private static final int DATACHANGE = 3;
    SPSearchkeyListAdapter mAdapter;

    HotSearchAdapter hotSearchAdapter;//热门搜索
    SeachKeyAdapter mSeachKeyAdapter;
    List<String> mSearchkeys;
    List<Searchhistory> mSearchHistory; //搜索历史
    SearchhosAdapter  searchhosAdapter;//搜索历史
    ListView searchkeyListv;    //搜索历史
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
    private Button searchDeleteBtn;
    private GridView searchHotGridView;   //热门搜索

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_common);
        mContext = this;
        super.init();
    }
    @Override
    public void initSubViews() {
        searchView = (SPSearchView) findViewById(R.id.search_view);
        searchView.getSearchEditText().setFocusable(true);
        searchkeyListv = (ListView) findViewById(R.id.search_key_listv);
        searchHotGridView = (GridView) findViewById(R.id.search_key_grid);
        iamgeSearch = (ImageView) findViewById(R.id.search_icon);
        iamgeBack = (ImageView) findViewById(R.id.back_imgv);
        searchedTextView = (TextView) findViewById(R.id.search_edtv);
        searchDeleteBtn = (Button) findViewById(R.id.search_delete_btn);
    }

    @Override
    public void initData() {
        getHotKeyData();
        hotSearchAdapter =  new HotSearchAdapter(this,mHandler);
//        mAdapter = new SPSearchkeyListAdapter(this, mHandler);
        searchhosAdapter    = new SearchhosAdapter(this);
        mSeachKeyAdapter = new SeachKeyAdapter(this);
        searchHotGridView.setAdapter(hotSearchAdapter);
        searchkeyListv.setAdapter(searchhosAdapter);
        if (getIntent() != null && getIntent().getStringExtra("searchKey") != null) {
            String searchKey = getIntent().getStringExtra("searchKey");
            searchView.setSearchKey(searchKey);
        }
       /* InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(searchView.getSearchEditText()!=null)imm.showSoftInputFromInputMethod(searchView.getSearchEditText().getWindowToken(), 0);*/
        loadKey();
//        mAdapter.setData(mSearchkeys);
        List<String> hotKeys = SPServerUtils.getHotKeyword();
        searchhosAdapter.setData(mSearchHistory);
        hotSearchAdapter.setData(hotKeys);
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
                    SPDialogUtils.showToast(mContext, "搜索内容不能为空");
                    return;
                }
                startSearch(searchedTextView.getText().toString().trim());
            }
        });
//       键盘搜索按钮监听
        searchedTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
//                    ((InputMethodManager) searchedTextView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(
//                                    mContext.getCurrentFocus()
//                                            .getWindowToken(),
//                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //跳转activity
                    if (SPStringUtils.isEmpty(searchedTextView.getText().toString().trim())) {
                        SPDialogUtils.showToast(mContext, "搜索内容不能为空");
                    } else {
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
        searchDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteKet();
            }
        });
       searchkeyListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Searchhistory searchhistory = (Searchhistory) mSearchHistory.get(position);
               startSearch(searchhistory.getTitle());
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
    public void startSearch(String searchKey) {
        if (!SPStringUtils.isEmpty(searchKey)) {
            saveKey(searchKey);
        }
        Intent intent = new Intent(this, ProductListSearchResultActivity.class);
        intent.putExtra("searchKey", searchKey);
        startActivityForResult(intent, DATACHANGE);//888请求码
    }

    public void saveKey(String key) {
        List<Searchhistory> newsList = DataSupport.select("title")
                .where("title =?", key).find(Searchhistory.class);
        if(newsList.size()==0){
            Searchhistory searchhistory = new Searchhistory();
            searchhistory.setTitle(key);
            searchhistory.save();
        }

        List<Searchhistory> searchhistorie = DataSupport.findAll(Searchhistory.class);
        for (Searchhistory push : searchhistorie) {
            Log.e("MainActivity 搜索记录", push.getTitle());
        }
    }

    private void deleteKet() {
        DataSupport.deleteAll(Searchhistory.class);
        loadKey();
        searchhosAdapter.setData(mSearchHistory);
    }
    public void loadKey() {
         mSearchHistory = DataSupport.findAll(Searchhistory.class);
//        getHotKeyData();
    }

    private void getHotKeyData() {
        SPHomeRequest.getServiceConfig(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<SPServiceConfig> configs = (List<SPServiceConfig>) response;
                    MobileApplication.getInstance().setServiceConfigs(configs);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DATACHANGE: //
                loadKey();
//                mAdapter.setData(mSearchkeys);
//                mSeachKeyAdapter.setData(mSearchkeys);
                searchhosAdapter.setData(mSearchHistory);
                break;
        }
    }
}
