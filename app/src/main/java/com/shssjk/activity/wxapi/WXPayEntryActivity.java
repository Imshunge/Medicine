package com.shssjk.activity.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.shssjk.common.MobileConstants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        api = WXAPIFactory.createWXAPI(this, MobileConstants.APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (baseResp.errCode) {
                case 0:
                    Toast.makeText(getBaseContext(), "支付成功", Toast.LENGTH_SHORT).show();
                    if (mContext != null) {
//                        通知PayActivity 改变界面
                        mContext.sendBroadcast(new Intent(MobileConstants.ACTION_PAY_CHANGE));
                    }
                    break;
                case -1:
                case -2:
                    Toast.makeText(getBaseContext(), "支付失败,请重试!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
//        关闭页面
        finish();
    }
}

