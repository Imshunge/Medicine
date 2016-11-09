package com.shssjk.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.community.QuackListActivity;
import com.shssjk.adapter.CommunityTabAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.shssjk.model.community.Quack;
import com.shssjk.model.community.SchoolList;
import com.shssjk.view.MobileScrollLayout;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

/**
 * 江湖
 */


public class CommunityFragment extends BaseFragment implements MobileScrollLayout.PageListener,View.OnClickListener  {
    private Context mContext;
    private TextView titleTxw;
    private Button backBtn;
    private Button menuBtn;
    //    分类
    private List<Quack> mQuacks;
    private static final int TAB_INDEX_FIRST = 0;
    private static final int TAB_INDEX_SECOND = 1;
    private static final int TAB_INDEX_THREE = 2;

    TabPageIndicator mPageIndicator;


    ViewPager mViewPager;

    FragmentPagerAdapter fragPagerAdapter;




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

        View view = inflater.inflate(R.layout.fragment_community, null,false);
        super.init(view);
        return view;
    }
    @Override
    public void initSubView(View view) {
//       标题
        titleTxw = (TextView) view.findViewById(R.id.titlebar_title_txtv);
        titleTxw.setText(R.string.tab_item_community);
        view.findViewById(R.id.titlebar_back_btn).setVisibility(View.INVISIBLE);
        menuBtn= (Button) view.findViewById(R.id.titlebar_menu_btn);
        menuBtn.setVisibility(View.INVISIBLE);
         menuBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//                 getQuackCategory();
                 Intent carIntent = new Intent(getActivity() , QuackListActivity.class);
                 getActivity().startActivity(carIntent);
//                 QuackListActivity();
             }
         });
        mPageIndicator= (TabPageIndicator)  view.findViewById(R.id.coupon_page_indicator);
        mViewPager= (ViewPager)  view.findViewById(R.id.coupon_view_pager);
        fragPagerAdapter = new CommunityTabAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(fragPagerAdapter);
        mPageIndicator.setViewPager(mViewPager, 0);
        mPageIndicator.setVisibility(View.VISIBLE);




    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
//        getQuackCategory();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void page(int page) {

    }


    /**
     * 江湖分类列表
     */
    public void getQuackCategory() {

        showLoadingToast("正在加载数据...");

        CommunityRequest.getQuackCategoryList("","",0,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mQuacks = (List<Quack>) response;
//
//                            mCategoryAdapter.setData(informations);
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

      //门派列表
    public void getgetSchoolList() {

        showLoadingToast("正在加载数据...");

        CommunityRequest.getSchoolList("", "", 0, "",
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                        List<SchoolList>    mQuacks = (List<SchoolList>) response;
//
//                            mCategoryAdapter.setData(informations);
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

}
