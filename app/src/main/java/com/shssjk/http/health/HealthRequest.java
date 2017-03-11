
package com.shssjk.http.health;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shssjk.common.MobileConstants.Response;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPMobileHttptRequest;
import com.shssjk.http.base.SPSuccessListener;

import com.shssjk.model.health.All;
import com.shssjk.model.health.AllSugar;
import com.shssjk.model.health.BloodDevice;
import com.shssjk.model.health.BloodTongJi;
import com.shssjk.model.health.Device;
import com.shssjk.model.health.One;
import com.shssjk.model.health.SuagrTongJi;
import com.shssjk.model.health.SugarData;
import com.shssjk.model.health.Three;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPJsonUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @author 健康云
 */
public class HealthRequest {

    private static String TAG = "HealthRequest";
    /**
     * 获取设备列表
     * name 、设备类型（BLOOD 血压计；XTY 血糖仪）
     * @param name
     * @param successListener
     * @param failuredListener
     */
    public static void getDeviceList(String name, final SPSuccessListener successListener, final
    SPFailuredListener failuredListener) {

//		HealthCloud/lists
        String url = SPMobileHttptRequest.getRequestUrl("HealthCloud", "lists");

        RequestParams params = new RequestParams();

        if (!SSUtils.isEmpty(name)) {
            params.put("name", name);
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    List<Device> filters = new ArrayList<Device>();
                    if(status>=0) {
                        if (status == 0) {
                            /** 工具类json转为User实体 **/
                            JSONArray resultJson = (JSONArray) response.getJSONArray(Response.DATA);
                            filters = SPJsonUtil.fromJsonArrayToList(resultJson, Device.class);
                            successListener.onRespone(msg, filters);
                        } else {
                            failuredListener.onRespone(msg, 1);
                        }
                    }else{
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


    /**
     * i 修改设备
     * id;name 、设备类型（BLOOD 血压计；XTY 血糖仪）;imei 、设备号; relation、关系;
     * default 、是否是最关心设备（1是 0 否）
     * @param id
     * @param name
     * @param imei
     * @param relation
     * @param defaultStr
     * @param successListener
     * @param failuredListener
     */
    public static void saveDevice(String id, String name, String imei, String relation, String defaultStr, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("HealthCloud", "save");
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("name", name);
        params.put("imei", imei);
        params.put("relation", relation);
        params.put("default", defaultStr);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if(status>=0) {
                        if (status == 0) {
//						JSONObject result = (JSONObject) response.getJSONObject(Response.DATA);
//						/** 工具类json转为User实体 **/
//						ComArticle article = SPJsonUtil.fromJsonToModel(response.getJSONObject(Response.DATA), ComArticle.class);
                            successListener.onRespone(msg, status);

                        } else {
                            failuredListener.onRespone(msg, -1);
                        }
                    }else{
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

    /**
     * 添加设备
     * name 、设备类型（BLOOD 血压计；XTY 血糖仪）;imei 、设备号; relation、关系; default 、
     * 是否是最关心设备（1是 0 否）
     *
     * @param name
     * @param imei
     * @param relation
     * @param defaultStr
     * @param successListener  状态：0 成功、1失败
     * @param failuredListener
     */
    public static void addDevice(String name, String imei, String relation, String defaultStr, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("HealthCloud", "add");
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("imei", imei);
        params.put("relation", relation);
        params.put("default", defaultStr);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if(status>=0) {
                        if (status == 0) {
                            successListener.onRespone(msg, status);

                        } else {
                            failuredListener.onRespone(msg, -1);
                        }
                    }else{
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

    /**
     * 删除设备
     *
     * @param id
     * @param successListener
     * @param failuredListener
     */
    public static void deleteDevice(String id, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

//		id、需删除设备id
        String url = SPMobileHttptRequest.getRequestUrl("HealthCloud", "save");
        RequestParams params = new RequestParams();
        params.put("id", id);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if(status>=0) {
                        if (status == 0) {
                            successListener.onRespone(msg, status);
                        } else {
                            failuredListener.onRespone(msg, -1);
                        }
                    }else{
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

    /***
     * 获取血压数据列表
     * imei、设备号 ; type 数据类型（1 爸爸，2妈妈）; order、排序
     * （asc、desc）; start_time 、开始时间
     * ;end_time、结束时间
     *
     * @param imei
     * @param type
     * @param order
     * @param startTime
     * @param endTime
     * @param successListener
     * @param failuredListener
     */
    public static void getBloodsDataList(String imei, String type, String order, String startTime, String endTime,
                                         final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

//		HealthCloud/lists
        String url = SPMobileHttptRequest.getRequestUrl("HealthCloud", "blists");

        RequestParams params = new RequestParams();

        if (!SSUtils.isEmpty(imei)) {
            params.put("imei", imei);
        }
        if (!SSUtils.isEmpty(type)) {
            params.put("type", type);
        }
        if (!SSUtils.isEmpty(order)) {
            params.put("order", order);
        }
        if (!SSUtils.isEmpty(startTime)) {
            params.put("start_time", startTime);
        }
        if (!SSUtils.isEmpty(endTime)) {
            params.put("end_time", endTime);
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    List<BloodDevice> filters = new ArrayList<BloodDevice>();
                    if(status>=0) {
                        if (status == 0) {
                            /** 工具类json转为User实体 **/
                            JSONArray resultJson = (JSONArray) response.getJSONArray(Response.DATA);
                            filters = SPJsonUtil.fromJsonArrayToList(resultJson, BloodDevice.class);
                            successListener.onRespone(msg, filters);
                        } else {
                            failuredListener.onRespone(msg, 1);
                        }
                    }else{
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


    /**
     * 血压统计
     * HealthCloud", "btongji
     * imei、设备号 ; type 数据类型（1 爸爸，2妈妈）
     *
     * @param imei
     * @param type
     * @param successListener
     * @param failuredListener
     */
    public static void getBloodsTongji(String imei, String type,
                                       final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//	imei、设备号 ; type 数据类型（1 爸爸，2妈妈）

//		HealthCloud/lists
        String url = SPMobileHttptRequest.getRequestUrl("HealthCloud", "btongji");

        RequestParams params = new RequestParams();

        if (!SSUtils.isEmpty(imei)) {
            params.put("imei", imei);
        }
        if (!SSUtils.isEmpty(type)) {
            params.put("type", type);
        }

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);

                    int status = response.getInt(Response.STATUS);

                    if(status>=0) {
                        if (status == 0) {
                            /** 工具类json转为User实体 **/
                            BloodTongJi bloodTongJi = new BloodTongJi();
                            JSONObject resultJson = (JSONObject) response.getJSONObject(Response.DATA);
                            if (resultJson != null) {
                                JSONObject all = resultJson.getJSONObject("all");
                                All alltemp = SPJsonUtil.fromJsonToModel(all, All.class);
                                bloodTongJi.setAll(alltemp);
                                JSONObject one = resultJson.getJSONObject("one");
                                One onetemp = SPJsonUtil.fromJsonToModel(one, One.class);
                                bloodTongJi.setOne(onetemp);
                                JSONObject three = resultJson.getJSONObject("three");
                                Three threetemp = SPJsonUtil.fromJsonToModel(one, Three.class);
                                bloodTongJi.setThree(threetemp);
                            }
                            successListener.onRespone(msg, bloodTongJi);
                        } else {
                            failuredListener.onRespone(msg, 1);
                        }
                    }else{
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
//    foodstatus、测试时间段 ; devicesn、设备号 ; start 、开始时间 ; end、结束时间 ; order 、 排序（asc 、desc）

    /**
     *血糖数据列表及搜索
     * @param foodstatus  1:空腹，2:早餐后，3:午餐前，4:午餐后，5:晚餐前，6:晚餐后，7:睡前，8:凌晨，
     *                    9：随机，A:餐前，B:餐后
     * @param devicesn
     * @param order
     * @param startTime
     * @param endTime
     * @param successListener
     * @param failuredListener
     */
    public static void getSugarDataList(String foodstatus, String devicesn, String order, String startTime, String endTime,
                                         final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
      //		HealthCloud/lists
        String url = SPMobileHttptRequest.getRequestUrl("HealthCloud", "search");
        RequestParams params = new RequestParams();
        if (!SSUtils.isEmpty(foodstatus)) {
            params.put("foodstatus", foodstatus);
        }
        if (!SSUtils.isEmpty(devicesn)) {
            params.put("devicesn", devicesn);
        }
        if (!SSUtils.isEmpty(order)) {
            params.put("order", order);
        }
        if (!SSUtils.isEmpty(startTime)) {
            params.put("start", startTime);
        }
        if (!SSUtils.isEmpty(endTime)) {
            params.put("end", endTime);
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);

                    int status = response.getInt(Response.STATUS);
                    List<SugarData> filters = new ArrayList<SugarData>();
                    if(status>=0) {
                        if (status == 0) {
                            /** 工具类json转为User实体 **/
                            JSONArray resultJson = (JSONArray) response.getJSONArray(Response.DATA);
                            filters = SPJsonUtil.fromJsonArrayToList(resultJson, SugarData.class);
                            successListener.onRespone(msg, filters);
                        } else {
                            failuredListener.onRespone(msg, 1);
                        }
                    }else{
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

    /**
     * 血糖统计
     * HealthCloud/tongji  devicesn 设备号
     * @param devicesn
     * @param successListener
     * @param failuredListener
     */
    public static void getSugarTongji(String devicesn,
                                       final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
      //		HealthCloud/lists
        String url = SPMobileHttptRequest.getRequestUrl("HealthCloud", "tongji");

        RequestParams params = new RequestParams();

        if (!SSUtils.isEmpty(devicesn)) {
            params.put("devicesn", devicesn);
        }

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if(status>=0) {
                        if (status == 0) {
                            /** 工具类json转为User实体 **/
                            SuagrTongJi bloodTongJi = new SuagrTongJi();
                            JSONObject resultJson = (JSONObject) response.getJSONObject(Response.DATA);
                            if (resultJson != null) {
                                JSONObject all = resultJson.getJSONObject("all");
                                int countAll = all.getInt("count");
                                AllSugar alltemp = new AllSugar();
                                if (countAll > 0) {
                                    alltemp = SPJsonUtil.fromJsonToModel(all, AllSugar.class);
                                }
                                bloodTongJi.setAll(alltemp);
                                JSONObject one = resultJson.getJSONObject("one");
                                int countOne = one.getInt("count");
                                AllSugar oneTemp = new AllSugar();
                                if (countOne > 0) {
                                    oneTemp = SPJsonUtil.fromJsonToModel(one, AllSugar.class);
                                }
                                bloodTongJi.setOne(oneTemp);
                                JSONObject three = resultJson.getJSONObject("three");
                                int countThree = three.getInt("count");
                                AllSugar threetemp = new AllSugar();
                                if (countThree > 0) {
                                    threetemp = SPJsonUtil.fromJsonToModel(three, AllSugar.class);
                                }
                                bloodTongJi.setThree(threetemp);
                            }
                            successListener.onRespone(msg, bloodTongJi);
                        } else {
                            failuredListener.onRespone(msg, 1);
                        }
                    }else{
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
