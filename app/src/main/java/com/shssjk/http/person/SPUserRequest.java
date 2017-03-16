package com.shssjk.http.person;

import android.support.annotation.NonNull;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shssjk.model.person.TeamData;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPMobileHttptRequest;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.model.SPUser;
import com.shssjk.model.person.UploadPic;
import com.soubao.tpshop.utils.SPEncryptUtil;
import com.soubao.tpshop.utils.SPJsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ben on 2016/7/9.
 */
public class SPUserRequest {

    private static final String TAG = "SPUserRequest";

    /**
     * 用户登录接口
     *
     * @param phoneNumber      手机号码
     * @param password         密码
     * @param channalId
     * @param appId
     * @param spikey
     * @param deviceyppe
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     */
    public static void doLogin(String phoneNumber, String password, String channalId, String appId, String spikey, String deviceyppe, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        /** 组装用户登录URL */
        String url = SPMobileHttptRequest.getRequestUrl("User", "login");

        /** 组装请求参数 */
        RequestParams params = new RequestParams();

        try {
            String authPwd = MobileConstants.SP_AUTH_CODE + password;
            String md5pwd = SPEncryptUtil.md5Digest(authPwd);
            params.put("username", phoneNumber);
            params.put("password", password);
            params.put("channelid", channalId);
            params.put("appId", appId);
            params.put("apiKey", spikey);
            params.put("deviceName", deviceyppe);
            params.put("secretkey", "secretkey");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = response.getString(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status > 0) {
                        /** 工具类json转为User实体 **/
                        SPUser user = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.RESULT), SPUser.class);
                        successListener.onRespone(msg, user);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    successListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 注册
     *
     * @param phoneNumber      手机号
     * @param password         密码
     * @param code             验证码
     * @param inviter          邀请码
     * @param successListener  成功回调
     * @param failuredListener 失败
     */
    public static void doRegister(String phoneNumber, String password, String code, String inviter, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "reg");
        RequestParams params = new RequestParams();
        params.put("username", phoneNumber);
        params.put("password", password);
        params.put("password2", password);
        params.put("code", code);
        params.put("inviter", inviter);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = response.getString(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status > 0) {
                        /** 工具类json转为User实体 **/
//                        SPUser user = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.RESULT), SPUser.class);
//                        successListener.onRespone(msg, user);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    successListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

//

    /**
     * 找回密码
     *
     * @param phoneNumber      手机号
     * @param password         重置的密码
     * @param code             验证码
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     */
    public static void doRetrievePassword(String phoneNumber, String password, String code, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "retrieve_password");
        RequestParams params = new RequestParams();
        params.put("username", phoneNumber);
        params.put("password", password);
        params.put("code", code);
        SPMobileHttptRequest.post(url, params, getResponseHandler(successListener, failuredListener));
    }

    /**
     * 发送验证码
     *
     * @param phoneNumber      手机号
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     */
    public static void sendSMSRegCode(String phoneNumber, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "send_sms_reg_code");
        RequestParams params = new RequestParams();
        params.put("mobile", phoneNumber);
        SPMobileHttptRequest.post(url, params, getResponseHandler(successListener, failuredListener));
    }

    /**
     * 跟新用户信息
     *
     * @param user             用户
     * @param successListener
     * @param failuredListener
     */
    public static void updateUserInfo(SPUser user, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "updateUserInfo");
        RequestParams params = new RequestParams();
        params.put("user_id", user.getUserID());
        params.put("nickname", user.getNickname());
        params.put("head_pic", user.getHeadPic());
        params.put("sex", user.getSex());
        params.put("birthday", user.getBirthday());
//        SPMobileHttptRequest.post(url, params, getResponseHandler(successListener, failuredListener));

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status >= 0) {
                        if (status ==1) {
                            String result = response.getString(MobileConstants.Response.RESULT);
                            successListener.onRespone(msg, status);
                        } else {
                            failuredListener.onRespone(msg, -1);
                        }
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });


    }

    @NonNull
    private static JsonHttpResponseHandler getResponseHandler(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dataJson = new JSONObject();
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status >= 0) {
                        try {
//                            JSONObject resultObject = response.getJSONObject(MobileConstants.Response.RESULT);
                            if (response != null) {
                                successListener.onRespone(msg, status);
                            } else {
                                successListener.onRespone(msg, status);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            successListener.onRespone(msg, status);
                        }
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        };
    }


    //       注册
//    public static void doRegister(String phoneNumber, String password,String code,String inviter ,final SPSuccessListener successListener, final SPFailuredListener failuredListener){
//        assert(successListener!=null);
//        assert(failuredListener!=null);
//        String url =  SPMobileHttptRequest.getRequestUrl("User", "reg");
//        RequestParams params = new RequestParams();
//        params.put("username" , phoneNumber);
//        params.put("password" , password);
//        params.put("password2" , password);
//        params.put("code" , code);
//        params.put("inviter",inviter);
//        SPMobileHttptRequest.post(url, params, getResponseHandler(successListener, failuredListener));
//    }


    /**
     * 修改密码
     *
     * @param oldPassword      旧密码
     * @param newPassword      新密码
     * @param confirmPassword  确认密码
     * @param successListener
     * @param failuredListener
     */
    public static void doChangePassword(String oldPassword, String newPassword, String confirmPassword, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "password");
        RequestParams params = new RequestParams();
        params.put("old_password", oldPassword);
        params.put("new_password", newPassword);
        params.put("confirm_password", confirmPassword);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status >= 0) {
                        if (status > 0) {
                            String result = response.getString(MobileConstants.Response.RESULT);
                            successListener.onRespone(msg, result);
                        } else {
                            failuredListener.onRespone(msg, -1);
                        }
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 上传头像(图片上传)
     *
     * @param successListener
     * @param failuredListener
     * @URL 2.0 http://192.168.0.139:8080/index.php/Api/User/picture 上传头像
     * 参数：header,头像名称     3.0 1 成功
     */
    public static void uploadHeader(File file, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//        assert (successListener != null);
//        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "picture");

        RequestParams params = new RequestParams();

        if (file != null) {
            try {
                params.put("header", file, "image/png");
            } catch (FileNotFoundException e) {
                failuredListener.onRespone("file not found", -1);
                return;
            }
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status >= 0) {
                        if (status == 0) {
//                        String result = response.getString(MobileConstants.Response.DATA);
                            JSONObject resultObject = response.getJSONObject(MobileConstants.Response.DATA);
                            UploadPic result = SPJsonUtil.fromJsonToModel(resultObject, UploadPic.class);
                            successListener.onRespone(msg, result);
                        } else {
                            failuredListener.onRespone(msg, -1);
                        }
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 我的团队
     *
     * @param successListener
     * @param failuredListener
     */
    public static void getMyTeamList(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//        Information/collect
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Quack", "my_team");
        RequestParams params = new RequestParams();
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    List<TeamData> filters = new ArrayList<TeamData>();
                    if (status >= 0) {
                        if (status > 0) {
                            /** 工具类json转为User实体 **/
                            JSONArray resultJson = (JSONArray) response.getJSONArray(MobileConstants.Response.DATA);
                            filters = SPJsonUtil.fromJsonArrayToList(resultJson, TeamData.class);
                            successListener.onRespone(msg, filters);
                        } else {
                            failuredListener.onRespone(msg, 1);
                        }
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (JSONException e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }


}
