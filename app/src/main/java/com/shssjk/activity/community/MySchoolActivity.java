package com.shssjk.activity.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.model.community.QuackDetail;

/**
 * 我的门派
 */

public class MySchoolActivity extends BaseActivity implements View.OnClickListener {
    private String mNoteId;

 //成员数
            TextView tv_bodys;

   //帖子数
            TextView tv_essays;

//门派名字
            TextView tv_name;

   //门派图标
            ImageView im_school;

//门派简介
            TextView school_content;

   //门主头像
            ImageView host_icon;

   //门主昵称
            TextView host_name;

   //成员列表
            TextView tv_bodylist;

  //创建日期
            TextView tv_date;
//   审核成员

    TextView tv_add;
    private String cid;
    private String uid;//门派创建人id

    private RelativeLayout rlCheckUser; //审核成员
    private LinearLayout rlUserManager; //成员管理
    private QuackDetail mQuacks;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true,true,getString(R.string.quack_my_menpai_list));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_my);
        mContext=this;
        super.init();
    }

    @Override
    public void initSubViews() {
        this.cid = getIntent().getStringExtra("cid");
        this.uid = getIntent().getStringExtra("uid");
        rlCheckUser=(RelativeLayout) findViewById(R.id.rl_check_user);
        rlUserManager=(LinearLayout) findViewById(R.id.layout_host);
        tv_add= (TextView) findViewById(R.id.sch_add);
        // 根据门主审核成员
        String id= MobileApplication.getInstance().getLoginUser().getUserID();
        if (uid.equals(id)){
            rlUserManager.setVisibility(View.VISIBLE);
        }else{
            rlUserManager.setVisibility(View.GONE);
        }
        tv_bodys = (TextView) findViewById(R.id.school_bodys);

        tv_essays = (TextView) findViewById(R.id.school_essays);

        tv_name = (TextView) findViewById(R.id.school_name);

        im_school = (ImageView) findViewById(R.id.school_image);


        school_content = (TextView) findViewById(R.id.school_content);

        host_icon = (ImageView) findViewById(R.id.school_host_icon);

        host_name = (TextView) findViewById(R.id.school_hosts);


        tv_bodylist = (TextView) findViewById(R.id.sch_bodylist);

        tv_date = (TextView) findViewById(R.id.sch_date);




    }

    @Override
    public void initData() {
//        getSchoolDetail();



    }

    @Override
    public void initEvent() {
        tv_bodylist.setOnClickListener(this);
        rlCheckUser.setOnClickListener(this);
        tv_add.setOnClickListener(this);
    }


    private void showData(QuackDetail mQuacks) {
        tv_bodys.setText(mQuacks.getPersons());
        tv_essays.setText(mQuacks.getArticles());
        //成员数


//门派名字
        tv_name.setText(mQuacks.getTitle());
        //门派图标
        String imgUrl1 = MobileConstants.BASE_HOST+mQuacks.getLogo();
        Glide.with(this).load(imgUrl1).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(im_school);
//门派简介
        school_content.setText(mQuacks.getRemark());
        //门主头像
        ImageView host_icon;

        //门主昵称
        host_name.setText(mQuacks.getUid());
        //成员列表
        TextView tv_bodylist;

        //创建日期
        TextView tv_date;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sch_bodylist: //    成员列表
                startComUserListActivity();
                break;
            case R.id.rl_check_user:  //    审核成员
                startChexkUserListActivity();
                break;
            case R.id.sch_add:  //    加入退出门派
//                addOrQuiteSchool();
                break;
        }
    }

    //    成员列表
    private void startComUserListActivity() {
        Intent intent = new Intent(mContext, ComUserListActivity.class);
        intent.putExtra("cid",mQuacks.getCid());
        intent.putExtra("uid",uid);
        startActivity(intent);
    }
//    审核成员
    private void startChexkUserListActivity() {
        Intent intent = new Intent(mContext, ComCheckUserListActivity.class);
        intent.putExtra("cid",mQuacks.getCid());
        startActivity(intent);
    }


}
