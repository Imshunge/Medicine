package com.shssjk.activity.person;
/**
 * 我的石头
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shssjk.activity.BaseActivity;
import com.shssjk.activity.IViewController;
import com.shssjk.activity.R;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.person.PersonRequest;
import com.shssjk.model.person.StoneDetail;
import com.shssjk.utils.ConfirmDialog;
import com.shssjk.utils.OnClickEvent;
import com.shssjk.view.SPArrowRowView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyStoneActivity extends BaseActivity {
    @Bind(R.id.mystone_recharge_aview)
    SPArrowRowView mystoneRechargeAview;
    @Bind(R.id.mystone_withdraw_aview)
    SPArrowRowView mystoneWithdrawAview;
    @Bind(R.id.tv_stone_num)
    TextView tvStoneNum;
    @Bind(R.id.tv_point)
    TextView tvPoint;
    @Bind(R.id.ll_title2)
    LinearLayout llTitle2;
    private Button mBackBtn;
    private TextView tv_titlebar_right;
    private Button rightBtn;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_mystone), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stone);
        mContext=this;
        ButterKnife.bind(this);
        super.init();
    }
    @Override
    public void initSubViews() {
        DisplayMetrics display = new DisplayMetrics();
       getWindowManager().getDefaultDisplay()
                .getMetrics(display);
        llTitle2.setLayoutParams(new LinearLayout.LayoutParams(
                display.widthPixels, display.widthPixels * 2 / 5));
        tv_titlebar_right = (TextView) findViewById(R.id.ritht_title_txtv);
        rightBtn = (Button) findViewById(R.id.titlebar_menu_btn);
        rightBtn.setVisibility(View.GONE);
        tv_titlebar_right.setVisibility(View.VISIBLE);
        tv_titlebar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stoneDetailIntent = new Intent(MyStoneActivity.this, StoneDetailActivity.class);
                startActivity(stoneDetailIntent);
            }
        });
        tv_titlebar_right.setText("明细");
    }

    @Override
    public void initData() {
        getStoneInfo();
    }


    @Override
    public void initEvent() {
        int delayTime=2000;
        mystoneWithdrawAview.setOnClickListener(new OnClickEvent(delayTime) {
            @Override
            public void singleClick(View v) {
//            避免2次点击
//                checkIsToWork();
                showToast("该功能暂未开放");
            }
        });
    }
    private void getStoneInfo() {
        showLoadingToast("正在加载数据...");
        PersonRequest.getMyStoneInfo(
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingToast();
                        if (response != null) {
                          JSONObject mStoneInfo = (JSONObject) response;
                            showData(mStoneInfo);
//                            mStoneDetailAdapter.setData(mStoneDetaillist);
//                            listViewFrame.setLoadMoreEnable(true);
                        } else {
                            showToast(msg);
                        }
//                        listViewFrame.refreshComplete();
                    }
                }, new SPFailuredListener((IViewController) mContext) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingToast();
                        showToast(msg);
                    }
                });

    }

    private void showData(JSONObject mStoneInfo) {
        try {
            if (!mStoneInfo.isNull("do_earnings")) {
                tvStoneNum.setText(mStoneInfo.getString("do_earnings"));
            }
            if (!mStoneInfo.isNull("do_score")) {
                tvPoint.setText(mStoneInfo.getString("do_score"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.mystone_recharge_aview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mystone_recharge_aview:
//                Intent rechargeIntent = new Intent(MyStoneActivity.this, StoneRechargeActivity.class);
//                startActivityForResult(rechargeIntent, MobileConstants.Result_Code_Refresh);
                showToast("该功能暂未开放");
                break;
            default:
                break;
        }
    }
    private void startStoneWithdrawActivity() {
        Intent mystoneWithdrawIntent = new Intent(MyStoneActivity.this, StoneWithdrawActivity.class);
        mystoneWithdrawIntent.putExtra("StoneNum",tvStoneNum.getText().toString().trim());

        startActivity(mystoneWithdrawIntent);
    }
    //    判断是否创业
    public void checkIsToWork() {
        showLoadingToast("正在打开...");
        PersonRequest.isWork(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                if (response != null) {
//                 （1、已创业；0未达到创业资格；2达到创业资格;
                    String str = (String) response;
                    if ("0".equals(str.trim())) {
                        showToast("您还没有达到创业资格,暂不支持提现!");
                    } else if ("1".equals(str.trim())) {
                        startStoneWithdrawActivity();
//                        hintUserToWork();
                    } else if ("2".equals(str.trim())) {
                        hintUserToWork();
                    }
                } else {
                    showToast(msg);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                hideLoadingToast();
            }
        });
    }
    private void hintUserToWork() {
        ConfirmDialog.Builder builder = new ConfirmDialog.Builder(mContext);
        builder.setMessage("您已达到创业资格，但还未开启创业，是否开启创业");
        builder.setTitle("系统提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                startBankListActivity();
            }
        });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
   }
    //    我的银行卡 列表
    private void startBankListActivity() {
        Intent intent = new Intent();
        intent.setClass(mContext, BankListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MobileConstants.Result_Code_Refresh:
                getStoneInfo();
                break;
        }
    }
}
