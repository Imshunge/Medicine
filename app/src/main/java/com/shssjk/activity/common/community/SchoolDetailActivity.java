package com.shssjk.activity.common.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shssjk.activity.R;
import com.shssjk.activity.common.BaseActivity;
import com.shssjk.common.MobileConstants;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.community.CommunityRequest;
import com.shssjk.model.community.QuackDetail;

/**
 * 门派详情
 */

public class SchoolDetailActivity extends BaseActivity implements View.OnClickListener {
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
    //   审核成员数
    TextView tv_num_mng2;


    private String cid;
    private String uid;//门派创建人id

    private RelativeLayout rlCheckUser; //审核成员
    private LinearLayout rlUserManager; //成员管理
    private QuackDetail mQuacks;
    private Context mContext;
    private String isHost = "0";   //0  不是   1是
    private Button rithtBtn;  //右侧按钮
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.article_menpai_detail), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_detail);
        mContext = this;
        super.init();
    }

    @Override
    public void initSubViews() {
        this.cid = getIntent().getStringExtra("cid");
        this.uid = getIntent().getStringExtra("uid");
        rlCheckUser = (RelativeLayout) findViewById(R.id.rl_check_user);
        rlUserManager = (LinearLayout) findViewById(R.id.layout_host);
        tv_add = (TextView) findViewById(R.id.sch_add);
        // 根据门主审核成员
        id = MobileApplication.getInstance().getLoginUser().getUserID();
        if (uid.equals(id)) {
            rlUserManager.setVisibility(View.VISIBLE);
            isHost = "1";
        } else {
            rlUserManager.setVisibility(View.GONE);
            isHost = "0";
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

        tv_num_mng2 = (TextView) findViewById(R.id.num_mng1);

        rithtBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        rithtBtn.setVisibility(View.INVISIBLE);
        rithtBtn.setOnClickListener(this);
//        rithtBtn.setBackground(null);
//        rithtBtn.setText(R.string.scan_explain);
    }

    @Override
    public void initData() {
        getSchoolDetail();
    }

    @Override
    public void initEvent() {
        tv_bodylist.setOnClickListener(this);
        rlCheckUser.setOnClickListener(this);
        tv_add.setOnClickListener(this);
    }

    public void getSchoolDetail() {
        showLoadingToast("正在加载数据...");
        CommunityRequest.getComquackDetailByID(cid,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                            mQuacks = (QuackDetail) response;
                            showData(mQuacks);
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

    private void showData(QuackDetail mQuacks) {
        tv_bodys.setText(mQuacks.getPersons());
        tv_essays.setText(mQuacks.getArticles());
//门派名字
        tv_name.setText(mQuacks.getTitle());
        //门派图标
        String imgUrl1 = MobileConstants.BASE_HOST + mQuacks.getLogo();
        Glide.with(this).load(imgUrl1).placeholder(R.drawable.bg_image).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(im_school);
//门派简介
        school_content.setText(mQuacks.getRemark());
//        门派详情返回当前登录用户是否加入门派字段：is_in（0未加入，1已加入）
        if (mQuacks.getIs_in().equals("0")) {
            tv_add.setVisibility(View.VISIBLE);
        } else {
            tv_add.setVisibility(View.GONE);
            rithtBtn.setVisibility(View.VISIBLE);
//            rithtBtn.setBackground(null);
//            rithtBtn.setText(R.string.jh_exit);
        }
        //门主头像
        String imgUrlhosticon = MobileConstants.BASE_HOST + mQuacks.getHeader_pic();
        Glide.with(this).load(imgUrlhosticon).placeholder(R.drawable.icon_body).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(host_icon);
        //门主昵称
        host_name.setText(mQuacks.getNickname());
        //成员列表
//        TextView tv_bodylist;
        //创建日期
        if (uid.equals(id)) {

        }

        tv_date.setText(mQuacks.getCreateTime());
        tv_num_mng2.setText(mQuacks.getCount());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sch_bodylist: //    成员列表
                startComUserListActivity();
                break;
            case R.id.rl_check_user:  //    审核成员
                startChexkUserListActivity();
                break;
            case R.id.sch_add:  //    加入门派
                addOrQuiteSchool("add");
                break;
            case R.id.titlebar_menu_btn:  //   退出门派

                showMenu();
                break;
        }
    }

    private void showMenu() {

        PopupMenu popup = new PopupMenu(mContext, rithtBtn);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.menu_schooldetail, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(mContext, "" + item.getItemId(), Toast.LENGTH_LONG).show();
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        addOrQuiteSchool("quit");
                        break;

                }
                return true;
            }
        });

        popup.show(); //showing popup menu


    }

    private void addOrQuiteSchool(String type) {
        showLoadingToast("正在操作...");
//        	 参数：cid 门派id 、act 操作类型 (add，加入；quit ,退出）
        CommunityRequest.quitQuackWithCID(type, cid,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
//
                            showToast(msg);
                            tv_add.setVisibility(View.INVISIBLE);
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

    //    成员列表
    private void startComUserListActivity() {
        Intent intent = new Intent(mContext, ComUserListActivity.class);
        intent.putExtra("cid", mQuacks.getId());
        intent.putExtra("ishost", isHost);
        startActivity(intent);
    }

    //    审核成员
    private void startChexkUserListActivity() {
        Intent intent = new Intent(mContext, ComCheckUserListActivity.class);
        intent.putExtra("cid", mQuacks.getId());
        startActivity(intent);
    }


}
