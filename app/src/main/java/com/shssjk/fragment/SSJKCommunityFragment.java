package com.shssjk.fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.shssjk.activity.R;
import com.shssjk.activity.common.community.ComMyArticleListActivity;
import com.shssjk.activity.common.community.MyQuackListActivity;
import com.shssjk.activity.common.community.QuackListActivity;
import com.shssjk.adapter.SchollListAdapter;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.shssjk.model.community.ComCategory;
import com.shssjk.model.community.SchoolList;
import com.shssjk.view.MyGridView;

import java.util.List;

/**
 * 尚尚江湖
 */
public class SSJKCommunityFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private OnFragmentInteractionListener mListener;
    View   myMenPaiView;    //我的门派
    View myTzView;      //我的帖子
    View jhFlView;	//江湖分类
    MyGridView myGridView;
    SchollListAdapter schollListAdapter;
    private String offset="0";
    private int r =20;
    public SSJKCommunityFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SSJKCommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SSJKCommunityFragment newInstance(String param1, String param2) {
        SSJKCommunityFragment fragment = new SSJKCommunityFragment();
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
        View view =inflater.inflate(R.layout.fragment_ssjkcommunity, container, false);
        mContext=getActivity();
        super.init(view);
        return view;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setType(int i) {
    }

    public void loadData() {
        getSchoolcCategory();
    }

    @Override
    public void initSubView(View view) {
//        View myTzView;      //我的帖子
//        View jhFlView;	//江湖分类
        myMenPaiView = view.findViewById(R.id.frament_jh_mymenpai_aview);   //我的门派
        myTzView = view.findViewById(R.id.frament_jh_mypost_aview);			//我的帖子
        jhFlView = view.findViewById(R.id.frament_jh_fenlei_aview);          //门派分类
        myGridView= (MyGridView) view.findViewById(R.id.list_school_my);     //门派分类
    }

    @Override
    public void initEvent() {

        myMenPaiView.setOnClickListener(this);
        myTzView.setOnClickListener(this);
        jhFlView.setOnClickListener(this);

        schollListAdapter =  new SchollListAdapter(getActivity());
        myGridView.setAdapter(schollListAdapter);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ComCategory article = (ComCategory) schollListAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), QuackListActivity.class);
                intent.putExtra("category_id", article.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frament_jh_mymenpai_aview :
                startMyQuackListActivity();
                break;
            case R.id.frament_jh_mypost_aview :
                startComMyArticleListActivity();
                break;
            case R.id.frament_jh_fenlei_aview :


                break;

        }
    }

    private void startComMyArticleListActivity() {
        Intent mComMyArticleListActivity  =  new Intent(getActivity() , ComMyArticleListActivity.class);
        getActivity().startActivity(mComMyArticleListActivity);
    }
    private void startMyQuackListActivity() {
        Intent myQuackListintent  =  new Intent(getActivity() , MyQuackListActivity.class);
        getActivity().startActivity(myQuackListintent);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getSchoolcCategory() {

        CommunityRequest.getSchoolcCategory("", "", r, "",
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        if (response != null) {
                            List<ComCategory>    mQuacks = (List<ComCategory>) response;
                            schollListAdapter.setData(mQuacks);
                        }
                    }
                }, new SPFailuredListener() {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                    }
                });
    }



}
