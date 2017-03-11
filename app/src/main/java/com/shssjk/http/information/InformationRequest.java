
package com.shssjk.http.information;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shssjk.common.MobileConstants;
import com.shssjk.common.MobileConstants.Response;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPMobileHttptRequest;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.model.info.Article;
import com.shssjk.model.info.Comment;
import com.shssjk.model.info.Information;


import com.shssjk.utils.Logger;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @author 资讯
 */
public class InformationRequest {

    private static String TAG = "InformationRequest";

    /**
     * @param failuredListener
     * @return void    返回类型
     * @throws
     * @Description: 获取资讯分类列表
     * @
     */
    public static void getCategoryList(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

//        assert (successListener != null);
//        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Information", "category");

        RequestParams params = new RequestParams();

        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);

                    int status = response.getInt(MobileConstants.Response.STATUS);
                    List<Information> filters = new ArrayList<Information>();
                    if (status > 0) {
                        /** 工具类json转为User实体 **/
//						SPUser user = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.RESULT), SPUser.class);

                        JSONArray resultJson = (JSONArray) response.getJSONArray(Response.DATA);
                        filters = SPJsonUtil.fromJsonArrayToList(resultJson, Information.class);
                        successListener.onRespone(msg, filters);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
     * @param information
     * @param ofseet           分页起始变量
     * @param r                取几条数据
     * @param successListener
     * @param failuredListener
     * @Description: 资讯（百科）列表
     */
    public static void getArticleList(Information information, String ofseet, String keyword, String r, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {


        String url = SPMobileHttptRequest.getRequestUrl("Information", "article_list");
        RequestParams params = new RequestParams();
//   offset（分页起始变量）、r（）、keyword（模糊查询字段）

        if (!SSUtils.isEmpty(ofseet)) {
            params.put("offset", ofseet);

        }

        if (!SSUtils.isEmpty(r)) {

            params.put("r", r);
        }

        if (!SSUtils.isEmpty(information) && !SSUtils.isEmpty(information.getCatId())) {
            params.put("category_id", information.getCatId());
        }
        if (!SSUtils.isEmpty(information) && !SSUtils.isEmpty(information.getCatType())) {
            params.put("type", information.getCatType());
        }
        if (!SSUtils.isEmpty(keyword)) {
            params.put("keyword", keyword);
        }

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);

                    int status = response.getInt(MobileConstants.Response.STATUS);
                    List<Article> filters = new ArrayList<Article>();
                    if (status >= 0) {
                        /** 工具类json转为User实体 **/
                        JSONArray resultJson = (JSONArray) response.getJSONArray(Response.DATA);
                        filters = SPJsonUtil.fromJsonArrayToList(resultJson, Article.class);
                        successListener.onRespone(msg, filters);
                    } else {
                        failuredListener.onRespone(msg, 1);
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
     * @param id               资讯（百科）id
     * @param article_id       文章id
     * @param successListener
     * @param failuredListener
     * @Description: 资讯详情
     */

    public static void getArticleDetailByID(String id, String article_id, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//        assert (successListener != null);
//        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Information", "article_detail");

        RequestParams params = new RequestParams();

        params.put("id", id);
        params.put("article_id", article_id);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONObject result = (JSONObject) response.getJSONObject(Response.DATA);
                        /** 工具类json转为User实体 **/
                        Article article = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.DATA), Article.class);
                        successListener.onRespone(msg, article);

                    } else {
                        failuredListener.onRespone(msg, -1);
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
     * 获资讯评论  offset、r
     *
     * @param articleID
     * @param offset
     * @param r
     * @param successListener
     * @param failuredListener
     */
    public static void getArticleCommentWitArticleID(String articleID, String offset, String r, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

//        assert (successListener != null);
//        assert (failuredListener != null);
//		Information/comment_list
        String url = SPMobileHttptRequest.getRequestUrl("Information", "comment_list");

        RequestParams params = new RequestParams();
        params.put("article_id", articleID);
        params.put("offset", offset);
        params.put("r", r);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    List<Comment> comments = null;
                    if (status >= 0) {
                        String orderId = null;
                        if (response.has("data")) {
                            comments = SPJsonUtil.fromJsonArrayToList(response.getJSONArray("data"), Comment.class);
                        }
                        successListener.onRespone(msg, comments);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
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
     * 发表评论
     * goods_id（资讯或百科商品id）、parent_id（评论父级id,若无可不传）、content(评论内容)
     *
     * @param goodsId
     * @param parentId
     * @param content
     * @param successListener
     * @param failuredListener
     */
    public static void publishComment(String goodsId, String parentId, String content,
                                      final SPSuccessListener successListener, final SPFailuredListener
                                              failuredListener) {

//        assert (successListener != null);
//        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Information", "comment");

        RequestParams params = new RequestParams();
        params.put("goods_id", goodsId);
//		params.put("parent_id",parentId);
        params.put("content", content);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if(status>=0) {
                        if (status > 0) {
                            String resulJson = response.getString(Response.DATA);
                            successListener.onRespone(msg, resulJson);
                        } else {
                            failuredListener.onRespone("not found data", -1);
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
     * 收藏/取消收藏文章
     *
     * @param goodsID
     * @param type        操作类型: 0 添加收藏 1 删除收藏 , 该值为nil也代表收藏商品   act 操作类型（add 添加收藏、cancel 取消收藏）
     * @param successListener
     * @param failuredListener
     * @throws JSONException
     * @URL
     */
    public static void collectOrCancelArticleWithID(String goodsID, String type, String act, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//        assert (successListener != null);
//        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Information", "collect");

        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(goodsID)) {
            params.put("goods_id", goodsID);
        }
        if (!SPStringUtils.isEmpty(type)) {
            params.put("type", type);
        }
        if (!SPStringUtils.isEmpty(act)) {
            params.put("act", act);
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dataJson = new JSONObject();
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    if(status>=0) {
                        if (status > 0) {
                            successListener.onRespone(msg, msg);
                        } else {
                            failuredListener.handleResponse(msg, status);
                        }
                    }else{
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
     * 收藏/取消 点赞
     * @URL
     * @param commentid
     * @param type comment_id（评论id）、type(评论类型，ZIXUN(资讯)、BAIKE(百科)、MENPAI(门派))、act（操作类型(add 点赞、cancel取消点赞)）
     * @param successListener
     * @param failuredListener
     * @throws JSONException
     */
    /***
     * @param commentid
     * @param act
     * @param type             * @param type comment_id（评论id）、type(评论类型，ZIXUN(资讯)、BAIKE(百科)、MENPAI(门派))、act（操作类型(add 点赞、cancel取消点赞)）
     * @param successListener
     * @param failuredListener
     */
    public static void collectOrCancelPraiseWithID(String commentid, String act, String type,
                                                   final SPSuccessListener successListener,
                                                   final SPFailuredListener failuredListener) {
//        assert (successListener != null);
//        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Information", "parise");

        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(commentid)) {
            params.put("comment_id", commentid);
        }
        if (!SPStringUtils.isEmpty(act)) {
            params.put("act", act);
        }
        if (!SPStringUtils.isEmpty(type)) {
            params.put("type", type);
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    Logger.e(" status",status+"");
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
     * 收藏列表(资讯、百科)
     * // type（收藏类别,ZIXUN、BAIKE、GOODS）、offset、r、
     *
     * @param ofseet
     * @param type
     * @param r
     * @param successListener
     * @param failuredListener
     */
    public static void getCollectArticleList(String type, String ofseet, String r, final SPSuccessListener
            successListener, final SPFailuredListener failuredListener) {
//        Information/collect
//        assert (successListener != null);
//        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Information", "collect_list");

        RequestParams params = new RequestParams();

        if (!SSUtils.isEmpty(ofseet)) {
            params.put("offset", ofseet);
        }
        if (!SSUtils.isEmpty(r)) {
            params.put("r", r);
        }
        if (!SSUtils.isEmpty(type)) {
            params.put("type", type);
        }

        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(MobileConstants.Response.STATUS);
                    List<Article> filters = new ArrayList<Article>();
                    if (status >=0) {
                        /** 工具类json转为User实体 **/
//						SPUser user = SPJsonUtil.fromJsonToModel(response.getJSONObject(MobileConstants.Response.RESULT), SPUser.class);

                        JSONArray resultJson = (JSONArray) response.getJSONArray(Response.DATA);
                        filters = SPJsonUtil.fromJsonArrayToList(resultJson, Article.class);
                        successListener.onRespone(msg, filters);
                    } else {
//                        failuredListener.onRespone(msg, 1);
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
