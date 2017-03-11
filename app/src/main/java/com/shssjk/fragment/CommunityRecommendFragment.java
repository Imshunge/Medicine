package com.shssjk.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.shssjk.activity.R;
import com.shssjk.activity.common.community.ComArticleListActivity;
import com.shssjk.adapter.SchollAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.shssjk.model.community.SchoolList;

import java.util.List;

/**
  江湖 精彩推荐
 */
public class CommunityRecommendFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //门派列表
    private ListView articlesListv;
    private PtrClassicFrameLayout articlesPcf;
    private List<SchoolList> mSchoolList;
    private SchollAdapter mSchollAdapter;
    private Button rithtBtn;
    private Context mContext;
    private List<SchoolList> mQuacks;
    private String ofseet = "0";
    private int r = 10;

    public CommunityRecommendFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static CommunityRecommendFragment newInstance(String param1, String param2) {
        CommunityRecommendFragment fragment = new CommunityRecommendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_recommend, container, false);
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

    public void loadData() {
        getSchoolList();
    }

    @Override
    public void initSubView(View view) {
        articlesListv = (ListView)view.findViewById(R.id.school_listv);
        articlesPcf = (PtrClassicFrameLayout)view. findViewById(R.id.quack_list_view_ptr);



    }

    @Override
    public void initEvent() {
        mSchollAdapter =
                new SchollAdapter(getActivity());
        articlesListv.setAdapter(mSchollAdapter);
        articlesListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolList article = (SchoolList) mSchollAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ComArticleListActivity.class);
//                "uid": "200059",		 创建人(门主)
                intent.putExtra("uid", article.getUid());
                intent.putExtra("cid", article.getId());
                startActivity(intent);
            }
        });

        articlesPcf.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                getSchoolList();
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

    @Override
    public void initData() {
    }

    @Override
    public void gotoLoginPageClearUserDate() {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getSchoolList() {

//        showLoadingToast("正在加载数据...");

        CommunityRequest.getSchoolList(ofseet, "", r, "",
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
//                        hideLoadingToast();
                        if (response != null) {
                            mQuacks =      (List<SchoolList>) response;
                            mSchollAdapter.setData(mQuacks);
                        }
                        articlesPcf.refreshComplete();
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
//                        hideLoadingToast();
                        showToast(msg);
                    }
                });
    }
    public void loadMoreData() {
        if (mQuacks.size() > 0) {
            int size = mQuacks.size();
            ofseet = mQuacks.get(size - 1).getId();
        }
        showLoadingToast();
        CommunityRequest.getSchoolList(ofseet, "", r, "", new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    List<SchoolList> tempArticles = (List<SchoolList>) response;
                    mQuacks.addAll(tempArticles);
                    mSchollAdapter.setData(mQuacks);
                }
                articlesPcf.refreshComplete();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }



}
