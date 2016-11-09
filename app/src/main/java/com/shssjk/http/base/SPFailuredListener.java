package com.shssjk.http.base;

import com.shssjk.activity.common.IViewController;
import com.shssjk.common.MobileConstants;


/**
 * 首先会验证token是否过期/失效,
 *  如果过期/失效会进入登录页面登录
 */
public abstract class SPFailuredListener {

    private IViewController viewController;

    public SPFailuredListener(){}

    public IViewController getViewController(){
        return viewController;
    }

    public SPFailuredListener(IViewController pViewController){
        viewController = pViewController;
    }

    /**
     * 预处理,
     * @param msg
     * @param errorCode
     */
    public void handleResponse(String msg , int errorCode){
        boolean isNeedLogin = preRespone(msg , errorCode);
        if (isNeedLogin){
            //去登陆
            if(viewController != null){
                viewController.gotoLoginPage();
                onRespone(msg , errorCode);
            }else{
                onRespone(msg , errorCode);
            }
        }else{
            onRespone(msg , errorCode);
        }
    }

    public abstract void onRespone(String msg , int errorCode);

    public boolean preRespone(String msg , int errorCode){
        if (errorCode == MobileConstants.Response.RESPONSE_CODE_TOEKN_EXPIRE
                || errorCode == MobileConstants.Response.RESPONSE_CODE_TOEKN_INVALID
                || errorCode == MobileConstants.Response.RESPONSE_CODE_TOEKN_EMPTY){
            return true;
        }
        return false;
    }

}
