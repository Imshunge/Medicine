
package com.shssjk.http.home;

import com.loopj.android.http.JsonHttpResponseHandler;

import com.loopj.android.http.RequestParams;
import com.shssjk.common.MobileConstants;
import com.shssjk.common.MobileConstants.Response;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPMobileHttptRequest;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.model.AppUpdate;
import com.shssjk.model.SPHomeBanners;
import com.shssjk.model.SPHomeCategory;
import com.shssjk.model.SPPlugin;
import com.shssjk.model.SPProduct;
import com.shssjk.model.SPServiceConfig;
import com.shssjk.model.shop.Five;
import com.shssjk.model.shop.Product;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * @author
 *
 */
public class SPHomeRequest {
	private static String TAG = "SPHomeRequest";
	/**
	 *  查询系统配置信息
	 *  使用万能SQL: index.php?m=Api&c=Index&a=getConfig
	 *  @param successListener success description
	 *  @param failuredListener failure description
	 */
	public static void getServiceConfig(final SPSuccessListener successListener,  final SPFailuredListener failuredListener){
		assert(successListener!=null);
		assert(failuredListener!=null);
		String url =  SPMobileHttptRequest.getRequestUrl("Index", "getConfig");
		try{
			SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						String msg = (String) response.getString(MobileConstants.Response.MSG);
						int status = response.getInt(MobileConstants.Response.STATUS);
						if (status > 0) {
							JSONArray resultArray = (JSONArray) response.getJSONArray(MobileConstants.Response.RESULT);
							List<SPServiceConfig> serviceConfigs = SPJsonUtil.fromJsonArrayToList(resultArray, SPServiceConfig.class);
							successListener.onRespone("success", serviceConfigs);
						} else {
							failuredListener.onRespone(msg, -1);
						}
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
	 *  查询插件配置信息
	 *  使用万能SQL: index.php?m=Api&c=Index&a=getPluginConfig
	 *  @param successListener success description
	 *  @param failuredListener failure description
	 */
	public static void getServicePlugin(final SPSuccessListener successListener,  final SPFailuredListener failuredListener){
		assert(successListener!=null);
		assert(failuredListener!=null);

		String url =  SPMobileHttptRequest.getRequestUrl("Index", "getPluginConfig");

		try{
			SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {

						String msg = (String) response.getString(Response.MSG);

						int status = response.getInt(MobileConstants.Response.STATUS);
						if (status > 0) {
							JSONObject resultJson = (JSONObject) response.getJSONObject(Response.RESULT);
							List<SPPlugin> servicePlugins = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("payment"), SPPlugin.class);
							Map<String, SPPlugin> pluginMap = new HashMap<String, SPPlugin>();
							if (servicePlugins != null) {
								for (SPPlugin plugin : servicePlugins) {
									//插件安装后才可使用
									if (plugin.getStatus().equals("1")) {
										String key = plugin.getCode();
										pluginMap.put(key, plugin);
									}
								}
							}
							successListener.onRespone("success", pluginMap);
						} else {
							failuredListener.onRespone(msg, -1);
						}
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
	public static void getHomeData(final SPSuccessListener successListener,  final SPFailuredListener failuredListener) {
		assert (successListener != null);
		assert (failuredListener != null);
		String url =  SPMobileHttptRequest.getRequestUrl("Index", "home");
		try{
			SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

					try {
						String msg = (String) response.getString(Response.MSG);
						JSONObject resultJson = (JSONObject) response.getJSONObject(Response.RESULT);
						JSONObject dataJson = new JSONObject();
						if (resultJson != null) {
//							JSONArray result = resultJson.getJSONArray("result");

//							HomeResult HomeResult = SPJsonUtil.fromJsonToModel(resultJson, HomeResult.class);
							//商品列表
							if (!resultJson.isNull("goods")) {
								JSONArray goods = resultJson.getJSONArray("goods");
								if (goods != null) {
									List<SPHomeCategory> homeCategories = SPJsonUtil.fromJsonArrayToList(goods, SPHomeCategory.class);
									for (int i = 0; i < goods.length(); i++) {
										JSONObject entityObj = goods.getJSONObject(i);
										if (entityObj.isNull("goods_list")) {
											continue;  //空数据处理
										}
										JSONArray products = entityObj.getJSONArray("goods_list");
										List<SPProduct> pros = SPJsonUtil.fromJsonArrayToList(products, SPProduct.class);
										homeCategories.get(i).setGoodsList(pros);
									}
									dataJson.put("homeCategories", homeCategories);
								}
							}
							//ad
							if (!resultJson.isNull("ad")) {
								JSONArray ads = resultJson.getJSONArray("ad");
								if (ads != null) {
									List<SPHomeBanners> banners = SPJsonUtil.fromJsonArrayToList(ads, SPHomeBanners.class);
									dataJson.put("banners", banners);
								}
							}
							//five
							if (!resultJson.isNull("five")) {
								JSONArray five = resultJson.getJSONArray("five");
								if (five != null) {
									List<Five> fives = SPJsonUtil.fromJsonArrayToList(five, Five.class);
									dataJson.put("fives", fives);
								}
							}
							successListener.onRespone("success", dataJson);
						} else {
							failuredListener.onRespone(msg, -1);
						}
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
////
//	首页获取热门推荐商品接口：
//	地址：http://192.168.0.169:8080/index.php/Api/Goods/getHotgoods
//	参数：offset（默认为0）、r（默认为10）
	public static void getHomeRecommendProduct(String offset,String r,final SPSuccessListener successListener,  final SPFailuredListener failuredListener) {
		assert (successListener != null);
		assert (failuredListener != null);
		String url =  SPMobileHttptRequest.getRequestUrl("Goods", "getHotgoods");
		RequestParams params = new RequestParams();
		if (!SPStringUtils.isEmpty(offset)) {
			params.put("offset", offset);
		}
		if (!SPStringUtils.isEmpty(r)) {
			params.put("r", r);
		}
		try{
			SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						String msg = (String) response.getString(Response.MSG);

						JSONObject dataJson = new JSONObject();
						if (msg.equals("空数据")){
							successListener.onRespone(msg, 1);
						}else
						if (response != null){
							List<Product> servicePlugins = SPJsonUtil.fromJsonArrayToList(response.getJSONArray("data"), Product.class);
							dataJson.put("products", servicePlugins);
							successListener.onRespone("success", dataJson);
						}else {
							failuredListener.onRespone(msg , -1);
						}
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

//	获取软件版本
	public static void getSoftVersion(final SPSuccessListener successListener,  final SPFailuredListener failuredListener) {
		assert (successListener != null);
		assert (failuredListener != null);
		String url =  MobileConstants.Soft.GETVERSION;
		try{
			SPMobileHttptRequest.get(url, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					try {
						AppUpdate appUpdate = null;
						String msg = (String) response.getString(Response.MSG);
						int status = response.getInt(Response.STATUS);
						JSONObject resultJson = (JSONObject) response.getJSONObject(Response.DATA);
						if (status == 0) {
							appUpdate = SPJsonUtil.fromJsonToModel(resultJson, AppUpdate.class);
							successListener.onRespone("success", appUpdate);
						} else {
							failuredListener.onRespone(msg, -1);
						}
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
	 * 下载APP
	 * path 安装包所在路径
	 * @param path
	 * @param successListener
	 * @param failuredListener
	 */
public static void downloadSoft(String path,final SPSuccessListener successListener,  final SPFailuredListener failuredListener) {

	String url =  MobileConstants.Soft.GETVERSION;
	try{
		SPMobileHttptRequest.get(url,new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					AppUpdate appUpdate=null;
					String msg = (String) response.getString(Response.MSG);
					int status = response.getInt(Response.STATUS);
					JSONObject resultJson=  (JSONObject) response.getJSONObject(Response.DATA);
					if(status==0){
						appUpdate=SPJsonUtil.fromJsonToModel(resultJson, AppUpdate.class);
						successListener.onRespone("success", appUpdate);
					}else {
						failuredListener.onRespone(msg , -1);
					}
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




}
