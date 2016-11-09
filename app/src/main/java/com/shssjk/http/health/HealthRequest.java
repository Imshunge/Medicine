
package com.shssjk.http.health;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shssjk.common.MobileConstants.Response;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPMobileHttptRequest;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.model.community.ComArticle;
import com.shssjk.model.community.ComComment;
import com.shssjk.model.community.ComUser;
import com.shssjk.model.community.Quack;
import com.shssjk.model.community.QuackDetail;
import com.shssjk.model.community.SchoolList;
import com.shssjk.utils.SSUtils;
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
 * @author
 * 健康云
 */
public class HealthRequest {

	private static String TAG = "HealthRequest";

		/**
	 * @Description: 江湖分类列表
	 * 	* offset（分页起始变量）、r（分页页数）、keyword（模糊查询字段
	 * @param ofseet
	 * @param keyword
	 * @param r
	 * @param successListener
	 * @param failuredListener
	 */
	public static void getQuackCategoryList( String ofseet,String keyword,int r,final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "category");

		RequestParams params = new RequestParams();

		SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				/** 针对返回的业务数据会重新包装一遍再返回到View */
				try {
					String msg = (String) response.get(Response.MSG);

					int status = response.getInt(Response.STATUS);
					List<Quack> filters = new ArrayList<Quack>();
					if (status > 0) {
						/** 工具类json转为User实体 **/
						JSONArray resultJson = (JSONArray) response.getJSONArray(Response.DATA);
						filters = SPJsonUtil.fromJsonArrayToList(resultJson, Quack.class);
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

	/**
	 *  * @Description:门派列表
	 * offset（分页起始变量）、r（分页页数）、keyword（模糊查询字段）、category_id所属门派类别（在取所有门派的情况下可不传）
	 * @param ofseet
	 * @param keyword
	 * @param r
	 * @param category_id
	 * @param successListener
	 * @param failuredListener
	 */
	public static void  getSchoolList(String ofseet,String keyword,int r ,String  category_id,final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "quack_list");

		RequestParams params = new RequestParams();

		if(!SSUtils.isEmpty(ofseet)) {
			params.put("offset", ofseet);
		}

		if(!SSUtils.isEmpty(r)) {
			params.put("r", r);
		}

		if(!SSUtils.isEmpty(category_id)) {
			params.put("category_id", category_id);
		}

		if(!SSUtils.isEmpty(keyword)) {
			params.put("keyword", keyword);
		}

		SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				/** 针对返回的业务数据会重新包装一遍再返回到View */
				try {
					String msg = (String) response.get(Response.MSG);

					int status = response.getInt(Response.STATUS);
					List<SchoolList> filters = new ArrayList<SchoolList>();
					if (status > 0) {
						/** 工具类json转为User实体 **/
						JSONArray resultJson = (JSONArray) response.getJSONArray(Response.DATA);
						filters = SPJsonUtil.fromJsonArrayToList(resultJson, SchoolList.class);
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



	/**
	 *  * @Description 帖子列表
	 *  Quack/article_list
	 * offset（分页起始变量）、r（分页页数）、keyword（模糊查询字段）、category_id所属门派类别（在取所有门派的情况下可不传）
	 * offset（分页起始变量）、r（分页页数）、keyword（模糊查询字段）、cid所属门派id（在取所有帖子的情况下可不传）
	 * @param ofseet
	 * @param keyword
	 * @param r
	 * @param
	 * @param successListener
	 * @param failuredListener
	 */
	public static void  getSchoolArticlList(String ofseet,String keyword,int r ,String  cid,final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "article_list");

		RequestParams params = new RequestParams();

		if(!SSUtils.isEmpty(ofseet)) {
			params.put("offset", ofseet);
		}

		if(!SSUtils.isEmpty(r)) {
			params.put("r", r);
		}

		if(!SSUtils.isEmpty(cid)) {
			params.put("cid", cid);
		}

		if(!SSUtils.isEmpty(keyword)) {
			params.put("keyword", keyword);
		}

		SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				/** 针对返回的业务数据会重新包装一遍再返回到View */
				try {
					String msg = (String) response.get(Response.MSG);

					int status = response.getInt(Response.STATUS);
					List<ComArticle> filters = new ArrayList<ComArticle>();
					if (status > 0) {
						/** 工具类json转为User实体 **/
						JSONArray resultJson = (JSONArray) response.getJSONArray(Response.DATA);
						filters = SPJsonUtil.fromJsonArrayToList(resultJson, ComArticle.class);
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
	/**江湖帖子详情
	 *  Quack/article_detail
	 *  article_id  文章id  article_id	帖子id
	 * @param article_id
	 * @param successListener
	 * @param failuredListener
	 */
	public static void getComArticleDetailByID(String article_id ,final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "article_detail");

		RequestParams params = new RequestParams();

		params.put("article_id", article_id);

		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					/** 针对返回的业务数据会重新包装一遍再返回到View */
					String msg = (String) response.get(Response.MSG);
					int status = response.getInt(Response.STATUS);
					if (status > 0) {
						JSONObject result = (JSONObject) response.getJSONObject(Response.DATA);
						/** 工具类json转为User实体 **/
						ComArticle article = SPJsonUtil.fromJsonToModel(response.getJSONObject(Response.DATA), ComArticle.class);
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



	/**门派详情
	 * article_id	帖子id          quack_id	门派id
	 * @param quack_id
	 * @param successListener
	 * @param failuredListener
	 */
	public static void getComquackDetailByID(String quack_id ,final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "quack_detail");

		RequestParams params = new RequestParams();

		params.put("quack_id", quack_id);

		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					/** 针对返回的业务数据会重新包装一遍再返回到View */
					String msg = (String) response.get(Response.MSG);
					int status = response.getInt(Response.STATUS);
					if (status > 0) {
						JSONObject result = (JSONObject) response.getJSONObject(Response.DATA);
						/** 工具类json转为User实体 **/
						QuackDetail article = SPJsonUtil.fromJsonToModel(response.getJSONObject(Response.DATA), QuackDetail.class);
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
	 * 帖子评论列表  offset、r
	 * @param articleID
	 * @param offset
	 * @param r
	 * @param successListener
	 * @param failuredListener
	 */
	public static void getComArticleCommentWitArticleID(String articleID , String offset,String r, final SPSuccessListener successListener, final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);
//		Information/comment_list
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "comment_list");

		RequestParams params = new RequestParams();
		params.put("article_id" , articleID);

		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					/** 针对返回的业务数据会重新包装一遍再返回到View */
					String msg = (String) response.getString(Response.MSG);
					int status = response.getInt(Response.STATUS);
					List<ComComment> comments = null;
					if(status > 0) {
						String orderId = null ;
						if (response.has("data")) {
							comments = SPJsonUtil.fromJsonArrayToList(response.getJSONArray("data"), ComComment.class);
						}
						successListener.onRespone(msg ,comments);
					}else{
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
	 * 江湖 帖子发表评论   Quack/comment
	 * @param articleId
	 * @param parentId
	 * @param content
	 * @param successListener
	 * @param failuredListener
	 */
	public static void CompublishComment(String articleId,String parentId,String content, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "comment");

		RequestParams params = new RequestParams();
		params.put("article_id",articleId);
//		params.put("parent_id",parentId);
		params.put("content",content);
		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				/** 针对返回的业务数据会重新包装一遍再返回到View */
				try {
					String msg = (String) response.get(Response.MSG);
					int status = response.getInt(Response.STATUS);
					if (status > 0){
//						JSONArray resulJson = response.getJSONArray(Response.DATA);
						successListener.onRespone(msg, "");
					} else {
						failuredListener.onRespone("not found data", -1);
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
	 * 江湖  发表帖子   Quack/add_article
	 * cid(门派id)	  title(标题)  description（描述）    图片  多张
	 * @param successListener
	 * @param failuredListener
	 */
	public static void CompuPlishArticle(String cid,String title,String description  ,List<File> images , final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "add_article");

		RequestParams params = new RequestParams();
		params.put("cid",cid);
		params.put("description",description);
		params.put("title",title);
//		List<File> images = commentCondition.getImages();
		if (images !=null){
			for (int i=0; i<images.size(); i++){
				File file = images.get(i);
				try {
					params.put("img_file["+i+"]", file, "image/png");
				} catch (FileNotFoundException e) {
					failuredListener.onRespone("file not found", -1);
					return;
				}
			}
		}

		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				/** 针对返回的业务数据会重新包装一遍再返回到View */
				try {
					String msg = (String) response.get(Response.MSG);
					int status = response.getInt(Response.STATUS);
					if (status > 0){
//						JSONArray resulJson = response.getJSONArray(Response.DATA);
						successListener.onRespone(msg, "");
					} else {
						failuredListener.onRespone("not found data", -1);
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
	 * 江湖  创建门派  Quack/add_quack
	 *  cid （门派类别id）、title （门派名称）、logo（门派logo）、remark（门派描述）
	 * @param cid
	 * @param title
	 * @param remark
	 * @param file
	 * @param successListener
	 * @param failuredListener
	 */
	public static void CompuPlishSchool(String cid,String title,String remark  ,File file , final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "add_quack");

		RequestParams params = new RequestParams();
		params.put("cid",cid);
		params.put("remark",remark);
		params.put("title",title);

		if (file != null) {
			try {
				params.put("logo", file, "image/png");
			} catch (FileNotFoundException e) {
				failuredListener.onRespone("file not found", -1);
				return;
			}
		}

		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				/** 针对返回的业务数据会重新包装一遍再返回到View */
				try {
					String msg = (String) response.get(Response.MSG);
					int status = response.getInt(Response.STATUS);
//					if (status == -102){
//						successListener.onRespone(msg, "");
//					}
					if (status > 0){
//						JSONArray resulJson = response.getJSONArray(Response.DATA);
						successListener.onRespone(msg, "");
					} else {
						failuredListener.onRespone("not found data", -1);
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
	 * @URL
	 * @param goodsID
	 * @param type 操作类型: 0 添加收藏 1 删除收藏 , 该值为nil也代表收藏商品
	 * @param successListener
	 * @param failuredListener
	 * @throws JSONException
	 */
	public static void collectOrCancelArticleWithID(String goodsID, String type, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
		assert (successListener != null);
		assert (failuredListener != null);
		String url = SPMobileHttptRequest.getRequestUrl("Information", "collect");

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
					int status = response.getInt(Response.STATUS);
					String msg = (String) response.get(Response.MSG);
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




	/***
	 * 江湖  评论点赞
	 * @param commentid
	 * @param act
	 * @param type
	 * 	 * @param type comment_id（评论id）、type(评论类型，ZIXUN(资讯)、BAIKE(百科)、MENPAI(门派))、act（操作类型(add 点赞、cancel取消点赞)）
	 * @param successListener
	 * @param failuredListener
	 */
	public static void ComcollectOrCancelPraiseWithID(String  commentid,String act,String type, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
		assert (successListener != null);
		assert (failuredListener != null);
		String url = SPMobileHttptRequest.getRequestUrl("Information", "parise");

		RequestParams params = new RequestParams();
		if (!SPStringUtils.isEmpty(commentid)) {
			params.put("comment_id",commentid);
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
					int status = response.getInt(Response.STATUS);
					String msg = (String) response.get(Response.MSG);
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
	 * 江湖  删除帖子  Quack/del_article    cid(门派id)	  article_id(帖子id)
	 * @param cid
	 * @param article_id
	 * @param successListener
	 * @param failuredListener
	 */
	public static void ComDeleteArticleWithID(String  cid,String article_id, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
		assert (successListener != null);
		assert (failuredListener != null);
		String url = SPMobileHttptRequest.getRequestUrl("Quack", "del_article");

		RequestParams params = new RequestParams();
		if (!SPStringUtils.isEmpty(article_id)) {
			params.put("article_id",article_id);
		}
		if (!SPStringUtils.isEmpty(cid)) {
			params.put("cid", cid);
		}

		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					/** 针对返回的业务数据会重新包装一遍再返回到View */
					int status = response.getInt(Response.STATUS);
					String msg = (String) response.get(Response.MSG);
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
	 * 江湖  删除帖子评论  Quack/del_comment  cid(门派id)	  comment_id(评论id)
	 * @param cid
	 * @param comment_id
	 * @param successListener
	 * @param failuredListener
	 */
	public static void ComDeleteCommnentWithID(String  cid,String comment_id, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
		assert (successListener != null);
		assert (failuredListener != null);
		String url = SPMobileHttptRequest.getRequestUrl("Quack", "del_comment");
		RequestParams params = new RequestParams();
		if (!SPStringUtils.isEmpty(comment_id)) {
			params.put("comment_id",comment_id);
		}
		if (!SPStringUtils.isEmpty(cid)) {
			params.put("cid", cid);
		}
		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					/** 针对返回的业务数据会重新包装一遍再返回到View */
					int status = response.getInt(Response.STATUS);
					String msg = (String) response.get(Response.MSG);
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
	 * 成员列表   * cid（门派id）
	 * 地址：Quack/quack_users
	 * @param cid
	 * @param offset
	 * @param r
	 * @param successListener
	 * @param failuredListener
	 */
	public static void getComUserListWithCID(String cid, String offset, String r, final SPSuccessListener successListener, final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);
//		Information/comment_list
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "quack_users");

		RequestParams params = new RequestParams();
		params.put("cid" , cid);

		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					/** 针对返回的业务数据会重新包装一遍再返回到View */
					String msg = (String) response.getString(Response.MSG);
					int status = response.getInt(Response.STATUS);
					List<ComUser> comUser = null;
					if(status > 0) {
						String orderId = null ;
						if (response.has("data")) {
							comUser = SPJsonUtil.fromJsonArrayToList(response.getJSONArray("data"), ComUser.class);
						}
						successListener.onRespone(msg ,comUser);
					}else{
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
	 * 审核 成员列表   * cid（门派id）
	 * 地址：Quack/quack_check
	 * @param cid
	 * @param offset
	 * @param r
	 * @param successListener
	 * @param failuredListener
	 */
	public static void getComCheckUserWithCID(String cid , String offset,String r, final SPSuccessListener successListener, final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);
//		Information/comment_list
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "quack_check");

		RequestParams params = new RequestParams();
		params.put("cid" , cid);

		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					/** 针对返回的业务数据会重新包装一遍再返回到View */
					String msg = (String) response.getString(Response.MSG);
					int status = response.getInt(Response.STATUS);
					List<ComUser> comUser = null;
					if(status > 0) {
						String orderId = null ;
						if (response.has("data")) {
							comUser = SPJsonUtil.fromJsonArrayToList(response.getJSONArray("data"), ComUser.class);
						}
						successListener.onRespone(msg ,comUser);
					}else{
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
	 * 退出门派   * cid（门派id）
	 * ：Quack/quit
	 * 地址：Quack/quack_check
	 * @param cid
	 * @param offset
	 * @param r
	 * @param successListener
	 * @param failuredListener
	 */
	public static void quitQuackWithCID(String cid , final SPSuccessListener successListener, final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);
//		Information/comment_list
		String url =  SPMobileHttptRequest.getRequestUrl("Quack", "quit");

		RequestParams params = new RequestParams();
		params.put("cid" , cid);

		SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					/** 针对返回的业务数据会重新包装一遍再返回到View */
					String msg = (String) response.getString(Response.MSG);
					int status = response.getInt(Response.STATUS);

					if(status > 0) {
						String orderId = null ;
						if (response.has("data")) {
						}
						successListener.onRespone(msg ,msg);
					}else{
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
}
