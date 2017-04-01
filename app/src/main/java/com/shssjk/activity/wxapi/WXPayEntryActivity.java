package com.shssjk.activity.wxapi;
import android.app.Activity;
import android.os.Bundle;

import com.shssjk.activity.R;
import com.shssjk.utils.Logger;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);
    }

    @Override
    public void onReq(BaseReq baseReq) {}

    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            switch (baseResp.errCode){
                case 0:
                    Logger.e(this,"errCode 0");
                    break;
                case -1:
                case -2:
                    Logger.e(this,"errCode"+baseResp.errCode);
                    break;
            }
        }

    }
}
