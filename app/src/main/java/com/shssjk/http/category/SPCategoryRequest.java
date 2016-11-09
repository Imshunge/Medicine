package com.shssjk.http.category;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.shssjk.common.MobileConstants;
import com.shssjk.common.MobileConstants.Response;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPMobileHttptRequest;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.model.SPCategory;
import com.soubao.tpshop.utils.SPJsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @author
 *
 */
public class SPCategoryRequest {
	
	private static String TAG = "SPCategoryRequest";
	
	/**
	 * 
	* @Description: 获取分类
	* @param parentID 如果parent < 1 则返回时的左边分类, 如果parent > 0 获取的是右边的分类
	* @param failuredListener
	* @throws JSONException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public static void getCategory (int parentID , final SPSuccessListener successListener,  final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);

		String url =  SPMobileHttptRequest.getRequestUrl("Goods", "goodsCategoryList");

		try{
			RequestParams params = new RequestParams();
			if(parentID >= 0){
				params.put("parent_id" , parentID);
			}

			SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray data = (JSONArray) response.getJSONArray(MobileConstants.Response.RESULT);
						List<SPCategory> categorys = SPJsonUtil.fromJsonArrayToList(data, SPCategory.class);
						successListener.onRespone("success", categorys);
						printCategory(categorys);
					} catch (Exception e) {
						failuredListener.onRespone(e.getMessage(), -1);
						e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @Description: 根据一级分类获取对应的二三级分类
	 * @param parentID 如果parent < 1 则返回时的左边分类, 如果parent > 0 获取的是右边的分类
	 * @param failuredListener
	 * @throws JSONException    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public static void goodsSecAndThirdCategoryList (int parentID , final SPSuccessListener successListener,  final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Goods", "goodsSecAndThirdCategoryList");
		try{
			RequestParams params = new RequestParams();
			if(parentID >= 0){
				params.put("parent_id" , parentID);
			}

			SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray data = (JSONArray) response.getJSONArray(Response.RESULT);
						List<SPCategory> categorys = SPJsonUtil.fromJsonArrayToList(data, SPCategory.class);
						for (SPCategory category : categorys){
							JSONArray array = category.getSubCategoryArray();
							if (array!=null ) {
								List<SPCategory> subCategorys = null;
								try {
									subCategorys = SPJsonUtil.fromJsonArrayToList(category.getSubCategoryArray(), SPCategory.class);
								}catch(Exception e){
								}
								category.setSubCategory(subCategorys);
							}
						}
						successListener.onRespone("success", categorys);
						printCategory(categorys);
					} catch (Exception e) {
						failuredListener.onRespone(e.getMessage(), -1);
						e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @Description: 获取所有分类  20161031修改接口
	 * @param successListener
	 * @param failuredListener
	 * @throws JSONException    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public static void getCategoryShop(final SPSuccessListener successListener,  final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);

		String url =  SPMobileHttptRequest.getRequestUrl("Goods", "get_goods_category_tree");
		try{

			SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray data = (JSONArray) response.getJSONArray(Response.RESULT);
//						List<SPCategory> categorys = SPJsonUtil.fromJsonArrayToList(data, SPCategory.class);
//						successListener.onRespone("success", categorys);
//						printCategory(categorys);
					} catch (Exception e) {
						failuredListener.onRespone(e.getMessage(), -1);
						e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void getAllCategory(final SPSuccessListener successListener,  final SPFailuredListener failuredListener){

		assert(successListener!=null);
		assert(failuredListener!=null);

		String url =  SPMobileHttptRequest.getRequestUrl("Goods", "goodsAllCategoryList");
		try{

			SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						JSONArray data = (JSONArray) response.getJSONArray(Response.RESULT);
						List<SPCategory> categorys = SPJsonUtil.fromJsonArrayToList(data, SPCategory.class);
						successListener.onRespone("success", categorys);
						printCategory(categorys);
					} catch (Exception e) {
						failuredListener.onRespone(e.getMessage(), -1);
						e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





	public static void printCategory(List<SPCategory> categorys){
		if(categorys!= null){
			for (int i = 0; i < categorys.size(); i++) {
			 SPCategory category = categorys.get(i);
			}
		}
	}

}
