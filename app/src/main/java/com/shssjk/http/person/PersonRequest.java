

package com.shssjk.http.person;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.shssjk.activity.person.Level;
import com.shssjk.activity.shop.CommentList;
import com.shssjk.common.MobileConstants;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPMobileHttptRequest;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.model.SPProduct;
import com.shssjk.model.community.Quack;
import com.shssjk.model.health.StepHistory;
import com.shssjk.model.order.SPExchange;
import com.shssjk.model.order.SPOrder;
import com.shssjk.model.person.Bank;
import com.shssjk.model.person.Camera;
import com.shssjk.model.person.SPConsigneeAddress;
import com.shssjk.model.person.SPRegionModel;
import com.shssjk.model.person.StepPersonInfo;
import com.shssjk.model.person.StoneDetail;
import com.shssjk.model.person.StoneType;
import com.shssjk.model.shop.CommentCondition;
import com.shssjk.model.shop.ProductCommnet;
import com.shssjk.model.shop.SPCollect;
import com.shssjk.model.shop.Coupon;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/6/21.
 */
public class PersonRequest {

    private static String TAG = "PersonRequest";

    /**
     * 收藏/取消收藏商品
     *
     * @param goodsID
     * @param type             操作类型: 0 添加收藏 1 删除收藏 , 该值为nil也代表收藏商品
     * @param successListener
     * @param failuredListener
     * @throws JSONException
     * @URL index.php?m=Api&c=Goods&a=collectGoods
     */
    public static void collectOrCancelGoodsWithID(String goodsID, String type, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Goods", "collectGoods");
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(goodsID)) {
            params.put("goods_id", goodsID);
        }
        if (!SPStringUtils.isEmpty(type)) {
            params.put("type", type);
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dataJson = new JSONObject();
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status > 0) {
                        successListener.onRespone(msg, msg);
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
        });
    }

    /**
     * 商品收藏列表
     *
     * @param successListener
     * @param failuredListener
     * @throws JSONException
     * @URL index.php?m=Api&c=User&a=getGoodsCollect
     */
    public static void getGoodsCollectWithSuccess(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "getGoodsCollect");

        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);

                    if (status > 0) {
                        JSONArray resultArray = response.getJSONArray(MobileConstants.Response.RESULT);
                        List<SPCollect> collects = SPJsonUtil.fromJsonArrayToList(resultArray, SPCollect.class);
                        successListener.onRespone(msg, collects);
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
                Log.e(TAG, "onFailure->headers.toString()");
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }


    /**
     * @URL index.php/Api/User/allAddress
     * 获取地址列表
     */
    public static void getAllAddress(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "allAddress");

        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);

                    String startDate = SPCommonUtils.getDateFullTime(System.currentTimeMillis());
                    if (status > 0) {
                        List<SPRegionModel> regions = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(MobileConstants.Response.RESULT), SPRegionModel.class);
                        successListener.onRespone(msg, regions);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
     * @URL index.php/Api/User/getAddressList
     * 获取收货人列表
     */
    public static void getConsigneeAddressList(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "getAddressList");

        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    List<SPConsigneeAddress> consignees = new ArrayList<SPConsigneeAddress>();
                    if (status > 0) {
                        if (!msg.equals("没有数据")) {
                            consignees = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(MobileConstants.Response.RESULT), SPConsigneeAddress.class);
                            successListener.onRespone(msg, consignees);
                        } else {
                            successListener.onRespone(msg, consignees);
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
     * 根据订单编号获取订单详情
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=getOrderDetail
     */
    public static void getOrderDetail(String orderID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "getOrderDetail");

        RequestParams params = new RequestParams();
        params.put("id", orderID);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);

                    if (status > 0) {
                        SPOrder order = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.RESULT), SPOrder.class);
                        if (order != null) {
                            List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(order.getProductsArray(), SPProduct.class);
                            order.setProducts(products);
                        }
                        successListener.onRespone(msg, order);
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
     * 所有订单
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=getOrderList
     */
    public static void getOrderListWithParams(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//        assert (successListener != null);
//        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "getOrderList");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);

                    if (status >= 0) {
                        if (status == 1) {
                            List<SPOrder> orders = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(MobileConstants.Response.RESULT), SPOrder.class);
                            if (orders != null) {
                                for (SPOrder order : orders) {
                                    List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(order.getProductsArray(), SPProduct.class);
                                    order.setProducts(products);
                                }
                            }
                            successListener.onRespone(msg, orders);
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
     * 根据订单编号获取订单详情
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=getOrderDetail
     */
    public static void getOrderDetailByID(String orderID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "getOrderDetail");

        RequestParams params = new RequestParams();
        params.put("id", orderID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);

                    if (status > 0) {
                        SPOrder order = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.RESULT), SPOrder.class);
                        if (order != null) {
                            List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(order.getProductsArray(), SPProduct.class);
                            order.setProducts(products);
                        }
                        successListener.onRespone(msg, order);
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
     * 取消订单
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL m=Api&c=User&a=cancelOrder
     */
    public static void cancelOrderWithOrderID(String orderID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "cancelOrder");

        RequestParams params = new RequestParams();
        params.put("order_id", orderID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);

                    if (status > 0) {
                        successListener.onRespone(msg, msg);
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
     * 确认收货
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL m=Api&c=User&a=confirmOrder
     */
    public static void confirmOrderWithOrderID(String orderID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "orderConfirm");

        RequestParams params = new RequestParams();
        params.put("order_id", orderID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status > 0) {
                        successListener.onRespone(msg, msg);
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
     * 查看物流
     *
     * @param orderID
     * @param successListener
     * @param failuredListener
     */
    public static void queryOrderWithOrderID(String orderID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "revice_shipping");

        RequestParams params = new RequestParams();
        params.put("order_id", orderID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    JSONObject data = response.getJSONObject(MobileConstants.Response.DATA);

                    JSONObject jsonObject = null;

                    if (status == 0) {
                        if (response.has("data") && !SSUtils.isEmpty(data)) {
                            jsonObject = response.getJSONObject(MobileConstants.Response.DATA);
                            successListener.onRespone(msg, jsonObject);
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
     * 获取退换货列表
     *
     * @param pageIndex        :
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=return_goods_list
     */
    public static void getExchangeListWithPage(int pageIndex, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "return_goods_list");

        RequestParams params = new RequestParams();
        params.put("p", pageIndex);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status > 0) {
                        List<SPExchange> exchanges = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(MobileConstants.Response.RESULT), SPExchange.class);
                        successListener.onRespone(msg, exchanges);
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
     *  查看某个售后详情
     *  @URL index.php?m=Api&c=User&a=return_goods_info
     *  @param exchangeId :
     *  @param successListener success description
     *  @param failuredListener failure description
     */
//    public static void getExchangeDetailWithId(String exchangeId , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
//        assert(successListener!=null);
//        assert(failuredListener!=null);
//        String url =  SPMobileHttptRequest.getRequestUrl("User", "return_goods_info");
//
//        RequestParams params = new RequestParams();
//        params.put("id" , exchangeId);
//        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                try {
//                    /** 针对返回的业务数据会重新包装一遍再返回到View */
//                    int status = response.getInt(MobileConstants.Response.STATUS);
//                    String msg = (String) response.get(MobileConstants.Response.MSG);
//                    if (status > 0) {
//                        SPExchange exchange = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.RESULT), SPExchange.class);
//                        List<String> images = SPMobileHttptRequest.convertJsonArrayToList(exchange.getImageArray());
//                        exchange.setImages(images);
//                        successListener.onRespone(msg, exchange);
//                    } else {
//                        failuredListener.onRespone(msg, -1);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    failuredListener.onRespone(e.getMessage(), -1);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                failuredListener.onRespone(throwable.getMessage(), statusCode);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                failuredListener.onRespone(throwable.getMessage(), statusCode);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                failuredListener.onRespone(throwable.getMessage(), statusCode);
//            }
//        });
//    }

    /**
     *  获取积分/余额历史记录
     *  @URL index.php?m=Api&c=User&a=account
     *  @param pageIndex :
     *  @param successListener success description
     *  @param failuredListener failure description
     */
//    public static void getWalletLogsWithPage(int pageIndex , final SPSuccessListener successListener, final SPFailuredListener failuredListener){
//        assert(successListener!=null);
//        assert(failuredListener!=null);
//
//        String url =  SPMobileHttptRequest.getRequestUrl("User", "account");
//
//        RequestParams params = new RequestParams();
//        params.put("p", pageIndex);
//
//        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                try {
//                    /** 针对返回的业务数据会重新包装一遍再返回到View */
//                    int status = response.getInt(MobileConstants.Response.STATUS);
//                    String msg = (String) response.get(MobileConstants.Response.MSG);
//                    if (status > 0) {
//                        List<SPWalletLog> walletLogs = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(MobileConstants.Response.RESULT), SPWalletLog.class);
//                        successListener.onRespone(msg, walletLogs);
//                    } else {
//                        failuredListener.handleResponse(msg, status);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    failuredListener.onRespone(e.getMessage(), -1);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                failuredListener.onRespone(throwable.getMessage(), statusCode);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                failuredListener.onRespone(throwable.getMessage(), statusCode);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                failuredListener.onRespone(throwable.getMessage(), statusCode);
//            }
//        });
//    }


    /**
     * @param params           params description
     * @param successListener  success description,n
     * @param failuredListener failure description
     * @URL index.php/Api/User/addAddress
     * 添加或编辑收货地址
     */
    public static void saveUserAddressWithParameter(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//        assert (successListener != null);
//        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "addAddress");

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status > 0) {
                        successListener.onRespone(msg, "");
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
     * 删除收货地址信息
     * 使用万能SQL: index.php?m=Api&c=User&a=del_address&id=100
     *
     * @param successListener  success description
     * @param failuredListener failure description
     */
    public static void delConsigneeAddressByID(String consigneeID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "del_address");
        RequestParams params = new RequestParams();
        params.put("id", consigneeID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status > 0) {
                        successListener.onRespone(msg, "1");
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
     * 获取优惠券列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=getCouponList&type=0&p=1
     */
    public static void getCouponListWithType(int type, int page, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "getCouponList");
        RequestParams params = new RequestParams();
        params.put("p", page);
        params.put("type", type);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status > 0) {
                        List<Coupon> coupons = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(MobileConstants.Response.RESULT), Coupon.class);
                        successListener.onRespone(msg, coupons);
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
     *  查看某个商品是否可以申请退换货
     *  @URL index.php?m=Api&c=User&a=return_goods_status
     *  @param successListener success description
     *  @param failuredListener failure description
     */
//    public static void queryReturnGoodsStatusWithOrderId(String orderId, String goodsId, String specKey, final SPSuccessListener successListener, final SPFailuredListener failuredListener){
//        assert(successListener!=null);
//        assert(failuredListener!=null);
//
//        String url =  SPMobileHttptRequest.getRequestUrl("User", "return_goods_status");
//
//        RequestParams params = new RequestParams();
//        params.put("order_id", orderId);
//        params.put("goods_id", goodsId);
//        params.put("spec_key", specKey);
//        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                try {
//                    /** 针对返回的业务数据会重新包装一遍再返回到View */
//                    int status = response.getInt(MobileConstants.Response.STATUS);
//                    String msg = (String) response.get(MobileConstants.Response.MSG);
//                    if (status > 0) {
//                        int result = response.getInt(MobileConstants.Response.RESULT);
//                        successListener.onRespone(msg, result);
//                    } else {
//                        failuredListener.onRespone(msg, -1);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    failuredListener.onRespone(e.getMessage(), -1);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                failuredListener.onRespone(throwable.getMessage(), statusCode);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                failuredListener.onRespone(throwable.getMessage(), statusCode);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                failuredListener.onRespone(throwable.getMessage(), statusCode);
//            }
//        });
//    }

    /**
     * 商品评论(图片上传)
     *
     * @param successListener
     * @param failuredListener
     * @URL ndex.php?m=Api&c=User&a=add_comment
     */
    public static void commentGoodsWithGoodsID(CommentCondition commentCondition, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "add_comment");


        RequestParams params = new RequestParams();
        params.put("order_id", commentCondition.getOrder_id());
        params.put("goods_id", commentCondition.getGoods_id());
//        params.put("spec_key", commentCondition.getSpecKey());

        params.put("goods_rank", commentCondition.getGoods_rank());
        params.put("deliver_rank", commentCondition.getDeliver_rank());
        params.put("service_rank", commentCondition.getService_rank());

        if (!SPStringUtils.isEmpty(commentCondition.getContent())) {
            params.put("content", commentCondition.getContent());
        }
        List<File> images = commentCondition.getImages();
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                File file = images.get(i);
                try {
                    params.put("img_file[" + i + "]", file, "image/png");
                } catch (FileNotFoundException e) {
                    failuredListener.onRespone("file not found", -1);
                    return;
                }
            }
        }


        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status > 0) {
                        String result = response.getString(MobileConstants.Response.RESULT);
                        successListener.onRespone(msg, result);
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
     * 申请退换货
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=return_goods
     * "user_id"   "token"   "order_id"   "unique_id   "goods_id"   "order_sn"
     * "type":@"0", @"reason"    @"img_file[%ld]"
     */
    public static void exchangeApplyWithParameter(CommentCondition commentCondition, final SPSuccessListener successListener,
                                                  final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        RequestParams params = new RequestParams();
        params.put("order_id", commentCondition.getOrder_id());
        params.put("goods_id", commentCondition.getGoods_id());

        params.put("order_sn", commentCondition.getOrder_sn());
        params.put("type", commentCondition.getType());
        params.put("reason", commentCondition.getContent());
        String url = SPMobileHttptRequest.getRequestUrl("User", "return_goods");
        List<File> images = commentCondition.getImages();

        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                File file = images.get(i);
                try {
                    params.put("img_file[" + i + "]", file, "image/png");
                } catch (FileNotFoundException e) {
                    failuredListener.onRespone("file not found", -1);
                    return;
                }
            }
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status > 0) {
//                        String result = response.getString(MobileConstants.Response.RESULT);
                        successListener.onRespone(msg, status);
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
     * User/comment_list
     * status 待评价 1 ，已评价 2
     *
     * @param
     * @param successListener
     * @param failuredListener
     */
    public static void getCommentListWithType(final String statusStr, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "comment_list");

        RequestParams params = new RequestParams();
        params.put("status", statusStr);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status >= 0) {

                        if (status == 0) {
//                        JSONObject resultJson = (JSONObject) response.getJSONObject(MobileConstants.Response.DATA);
                            //商品列表
                            if (!response.isNull("data")) {
                                JSONArray goods = response.getJSONArray("data");
                                if (goods != null) {
                                    List<ProductCommnet> exchanges = SPJsonUtil.fromJsonArrayToList(response.getJSONArray
                                            (MobileConstants.Response.DATA), ProductCommnet.class);
                                    if ("2".equals(statusStr)) {
                                        for (int i = 0; i < goods.length(); i++) {
                                            JSONObject entityObj = goods.getJSONObject(i);
                                            if (!entityObj.isNull("comment_list")) {
                                                JSONArray products = entityObj.getJSONArray("comment_list");
                                                List<CommentList> pros = SPJsonUtil.fromJsonArrayToList(products, CommentList.class);
                                                exchanges.get(i).setCommentList(pros);
                                            }
                                        }
                                    }
                                    successListener.onRespone(msg, exchanges);
                                }
                            }
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
     * 接口名称：摄像头列表
     * 接口地址：http://shssjk.com/index.php/Api/Camera/camera_list
     * 所传参数：user_id 当前登录用户的id
     *
     * @param successListener
     * @param failuredListener
     */
    public static void getCameraList(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Camera", "camera_list");
        RequestParams params = new RequestParams();
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status >= 0) {
                        if (status == 0) {
                            //摄像头列表
                            if (!response.isNull("data")) {
                                JSONArray data = response.getJSONArray("data");
                                if (data != null) {
                                    List<Camera> exchanges = SPJsonUtil.fromJsonArrayToList(response.getJSONArray
                                            (MobileConstants.Response.DATA), Camera.class);
                                    successListener.onRespone(msg, exchanges);
                                }
                            }
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
     * 接口名称:绑定摄像头
     * 接口地址：http://shssjk.com/index.php/Api/Camera/camera_bind
     * 所传参数：user_id 用户id、name 摄像头名称、did 摄像头UID
     *
     * @param name
     * @param did
     * @param successListener
     * @param failuredListener
     */
    public static void addCamera(final String name, final String did, final SPSuccessListener successListener,
                                 final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("Camera", "camera_bind");
        RequestParams params = new RequestParams();
        params.put("did", did);
        params.put("name", name);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status >= 0) {
                        if (status == 0) {
                            //摄像头列表
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

    /**
     * 接口名称：删除摄像头
     * 接口地址：http://shssjk.com/index.php/Api/Camera/camera_del
     * 所传参数：user_id 设备绑定的用户id、did 摄像头UID
     *
     * @param did
     * @param successListener
     * @param failuredListener Status	返回状态（0成功，1失败）
     */
    public static void deleteCameraByDid(final String did, final SPSuccessListener successListener,
                                         final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("Camera", "camera_del");
        RequestParams params = new RequestParams();
        params.put("did", did);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if (status >= 0) {
                        if (status == 0) {
                            //摄像头列表
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

    /**
     * 1．接口名称：判断是否创业
     * 接口地址：http://shssjk.com/index.php/Api/IsWork/is_work
     * 参数：user_id 登录用户的user_id ；传参方式：POST
     * 返回值：
     * {
     * "status": "0",		状态（0成功，1，失败）
     * "msg": "您已达到创业资格，但还未开启创业，是否开启创业资格",	返回信息
     * "data": "2"		返回值（1、已创业；0未达到创业资格；2达到创业资格，但是未创业）
     * }
     *
     * @param successListener
     * @param failuredListener
     */
    public static void isWork(final SPSuccessListener successListener,
                              final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("IsWork", "is_work");
        RequestParams params = new RequestParams();
//        params.put("did", did);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    String data = (String) response.get(MobileConstants.Response.DATA);
                    if (status >= 0) {
                        if (status == 0) {
                            successListener.onRespone(msg, data);
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
     * id
     * name
     * code
     * bank
     * is_default       0 否，1 是
     * successListener
     * failuredListener {
     * "status": "0",		状态（0成功，1，失败）
     * "msg": "添加成功",	返回信息
     * "data": "3"
     * }
     */
    public static void addBank(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//        assert (successListener != null);
//        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("IsWork", "add_bank");

//        RequestParams params = new RequestParams();
//        params.put("name", name);
//        params.put("code", code);
//        params.put("bank", bank);
//        params.put("is_default", is_default);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        if (status == 0) {
//						JSONObject result = (JSONObject) response.getJSONObject(Response.DATA);
//						/** 工具类json转为User实体 **/
//						ComArticle article = SPJsonUtil.fromJsonToModel(response.getJSONObject(Response.DATA), ComArticle.class);
                            successListener.onRespone(msg, status);

                        } else {
                            failuredListener.onRespone(msg, -1);
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


    /**
     * 修改银行卡
     * IsWork", "save_bank
     * <p/>
     * id
     * name
     * code
     * bank
     * is_default
     * bi
     * successListener
     * failuredListener
     */
    public static void saveBank(RequestParams params,
                                final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//        assert (successListener != null);
//        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("IsWork", "save_bank");
//
//        RequestParams params = new RequestParams();
//        params.put("name", name);
//        params.put("code", code);
//        params.put("bank", bank);
//        params.put("is_default", is_default);
//        params.put("bid", bid);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        if (status == 0) {
//						JSONObject result = (JSONObject) response.getJSONObject(Response.DATA);
//						/** 工具类json转为User实体 **/
//						ComArticle article = SPJsonUtil.fromJsonToModel(response.getJSONObject(Response.DATA), ComArticle.class);
                            successListener.onRespone(msg, status);

                        } else {
                            failuredListener.onRespone(msg, -1);
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

    /**
     * 删除银行卡
     * ("IsWork", "del_bank")
     * bid 需删除银行卡的id、token token值
     *
     * @param id
     * @param successListener
     * @param failuredListener
     */
    public static void deleteBank(String id, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("IsWork", "del_bank");
        RequestParams params = new RequestParams();
        params.put("bid", id);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        if (status == 0) {
                            successListener.onRespone(msg, status);
                        } else {
                            failuredListener.onRespone(msg, -1);
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

    /**
     * 获取银行卡列表
     * IsWork/bank_list
     *
     * @param successListener
     * @param failuredListener
     */
    public static void getBankList(
            final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//        assert (successListener != null);
//        assert (failuredListener != null);
//		HealthCloud/lists
        String url = SPMobileHttptRequest.getRequestUrl("IsWork", "bank_list");
        RequestParams params = new RequestParams();
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    List<Bank> filters = new ArrayList<Bank>();
                    if (status >= 0) {
                        if (status == 0) {
                            List<Bank> bankList = SPJsonUtil.fromJsonArrayToList(response.getJSONArray
                                    (MobileConstants.Response.DATA), Bank.class);
                            successListener.onRespone(msg, bankList);
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


    /**
     * 接口名称：石头明细
     * 接口地址：http://shssjk.com/index.php/Api/MyStone/stone_detail
     * 参数：token （token值）
     *
     * @param ofseet           offset（分页起始变量，默认为0）
     * @param r                r（分页结束变量，默认为20）
     * @param successListener
     * @param failuredListener
     */
    public static void getStoneDetailList(String ofseet, String r, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("MyStone", "stone_detail");
        RequestParams params = new RequestParams();
        params.put("offset", ofseet);
        params.put("r", r);
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    List<StoneDetail> filters = new ArrayList<StoneDetail>();
                    if (status >= 0) {
                        /** 工具类json转为User实体 **/
                        JSONArray resultJson = (JSONArray) response.getJSONArray(MobileConstants.Response.DATA);
                        filters = SPJsonUtil.fromJsonArrayToList(resultJson, StoneDetail.class);
                        successListener.onRespone(msg, filters);
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

    /**
     * 获取当前用户的 石头和积分数量
     * MyStone/stone_info   参数：token
     *
     * @param successListener
     * @param failuredListener
     */
    public static void getMyStoneInfo(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("MyStone", "stone_info");
        RequestParams params = new RequestParams();
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        /** 工具类json转为User实体 **/
                        JSONObject jsonObject = response.getJSONObject(MobileConstants.Response.DATA);
                        successListener.onRespone(msg, jsonObject);
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

    /**
     * 接口名称：石头充值金额列表
     * MyStone/stone_info   参数：token
     * http://shssjk.com/index.php/Api/MyStone/stone_list
     *
     * @param successListener
     * @param failuredListener
     */
    public static void getStoneList(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("MyStone", "stone_list");
        RequestParams params = new RequestParams();
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    List<StoneType> filters = new ArrayList<StoneType>();
                    if (status >= 0) {
                        /** 工具类json转为User实体 **/
                        filters = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(MobileConstants.Response.DATA), StoneType.class);
                        successListener.onRespone(msg, filters);
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

    /**
     * 接口名称：生成石头支付的订单
     * 接口地址：http://shssjk.com/index.php/Api/MyStone/add_order
     * 参数：token （token值）、goods_id（商品id）、key（商品key值）
     */
    public static void addStoneOrder(String goods_id, String key, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("MyStone", "add_order");
        RequestParams params = new RequestParams();
        params.put("goods_id", goods_id);
        params.put("key", key);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        JSONObject jsonObject = response.getJSONObject(MobileConstants.Response.DATA);
                        successListener.onRespone(msg, jsonObject);
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

    /**
     * 接口地址：http://shssjk.com/index.php/Api/MyStone/apply
     * 参数：token （token值）、bank_id（提现到银行卡id）、stone（提现石头数量）
     * 新增：code（验证码）  mobile（手机号）
     */
    public static void myStoneApply(String bank_id, String stone, String code, String mobile,
                                    final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("MyStone", "apply");
        RequestParams params = new RequestParams();
        params.put("bank_id", bank_id);
        params.put("stone", stone);
        params.put("code", code);
        params.put("mobile", mobile);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
//                        JSONArray jsonObject = response.getJSONArray(MobileConstants.Response.DATA);
                        successListener.onRespone(msg, status);
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

    /**
     * 1．接口名称：获取积分等级相关参数
     * 接口地址：http://shssjk.com/index.php/Api/User/level_info
     * 参数：token （token值）
     * 返回值：
     * {
     * "status": 0,		返回状态
     * "msg": "成功",		返回信息
     * "data": {
     * "now_score": "19584.70",		当前现有积分
     * "all_score": "6000"				下
     * }一级所需积分
     * }
     *
     * @param successListener
     * @param failuredListener
     */
    public static void getLevelInfo(
            final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("User", "level_info");
        RequestParams params = new RequestParams();

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        if (status == 0) {
                            Level level = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.DATA), Level.class);
                            successListener.onRespone(msg, level);
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

    /**
     * 接口名称：根据用户id 获取身高、体重、目标数等数据 接口
     * 用户id post方法 统一处理了
     *
     * @param successListener
     * @param failuredListener 0 成功、1 失败 2 未设置身高体重
     */
    public static void getUserSteptInfo(
            final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("Passometer", "user_info");
        RequestParams params = new RequestParams();
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        if (status == 0) {
                            StepPersonInfo stepPersonInfo = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.DATA), StepPersonInfo.class);
                            successListener.onRespone(msg, stepPersonInfo);
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


    /**
     * 接口名称：上传身高、体重、目标步数
     * user_id（用户id）、high（身高）、weight（体重）、target（目标步数）
     * 用户id post方法 统一处理了
     *
     * @param hight
     * @param weight
     * @param target
     * @param successListener
     * @param failuredListener
     */
    public static void addUserSteptInfo(String hight, String weight, String target,
                                        final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("Passometer", "add_info");
        RequestParams params = new RequestParams();
        if (!SSUtils.isEmpty(hight)) {
            params.put("high", hight);
        }
        if (!SSUtils.isEmpty(weight)) {
            params.put("weight", weight);
        }
        if (!SSUtils.isEmpty(target)) {
            params.put("target", target);
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        if (status == 0) {
                            successListener.onRespone(msg, response);
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

    /**
     * 接口名称：计步器记录
     * @param successListener
     * @param failuredListener
     */
    public static void getSteptRecord(
            final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("Passometer", "step_record");
        RequestParams params = new RequestParams();
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        List<StepHistory> stepHistories =  new ArrayList<StepHistory>();
                        if (status == 0) {
                            stepHistories = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(MobileConstants.Response.DATA), StepHistory.class);
                            successListener.onRespone(msg, stepHistories);
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

    /**
     * 接口名称：计步器记录
     * @param successListener
     * @param failuredListener
     * user_id（用户id）、step（步数）、date（日期 当前日期的时间戳）
     */
    public static void addStepRecord(RequestParams params,
            final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("Passometer", "add_record");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        List<StepHistory> stepHistories =  new ArrayList<StepHistory>();
                        if (status == 0) {
//                            stepHistories = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(MobileConstants.Response.DATA), StepHistory.class);
                            successListener.onRespone(msg, status);
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
//    last_record

    /**
     * http://shssjk.com/index.php/Api/Passometer/last_record
     * 返回状态：status 0 (成功)、1（失败）、2(空数据，没有上传过数据)
     * @param successListener
     * @param failuredListener
     */
    public static void getLastRecord(
            final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        String url = SPMobileHttptRequest.getRequestUrl("Passometer", "last_record");
        RequestParams params = new RequestParams();
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status >= 0) {
                        List<StepHistory> stepHistories =  new ArrayList<StepHistory>();
                        if (status == 0) {
                            successListener.onRespone(msg, response);
                        }
                        if(status==1){
                            failuredListener.onRespone(msg, status);
                        }
                        if(status==2){
                            failuredListener.onRespone(msg, status);
                        }
                        else {
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
