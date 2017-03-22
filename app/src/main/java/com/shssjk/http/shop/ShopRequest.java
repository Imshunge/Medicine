
package com.shssjk.http.shop;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.shssjk.common.MobileConstants;
import com.shssjk.common.MobileConstants.Response;
import com.shssjk.global.MobileApplication;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPMobileHttptRequest;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.condition.ProductCondition;
import com.shssjk.model.SPProduct;
import com.shssjk.model.order.PayOrder;
import com.shssjk.model.person.Alipay;
import com.shssjk.model.person.SPConsigneeAddress;
import com.shssjk.model.shop.Data;
import com.shssjk.model.shop.Coupon;
import com.shssjk.model.shop.SPFilter;
import com.shssjk.model.shop.SPFilterItem;
import com.shssjk.model.shop.GoodsComment;
import com.shssjk.model.shop.ProductAttribute;
import com.shssjk.model.shop.SPProductSpec;
import com.shssjk.model.shop.SPShopOrder;
import com.shssjk.model.shop.StoneOrderInfo;
import com.shssjk.model.shop.Tmenu;
import com.shssjk.model.shop.Totalprice;
import com.shssjk.utils.Logger;
import com.shssjk.utils.SMobileLog;
import com.shssjk.utils.SSUtils;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.soubao.tpshop.utils.SPStringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @author
 */
public class ShopRequest {

    private static String TAG = "ShopRequest";

    /**
     * @param productCondition
     * @param failuredListener
     * @return void    返回类型
     * @throws
     * @Description: 获取商品列表
     * @ 设定文件
     */
    public static void getProductList(ProductCondition productCondition, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Goods", "goodsList");

        RequestParams params = new RequestParams();
        params.put("p", productCondition.page);
        params.put("pagesize", MobileConstants.SizeOfPage);
        if (productCondition.href != null) {
            url = MobileConstants.BASE_HOST + productCondition.href;
        }
        /** 商品分类*/
        if (productCondition.categoryID > 0) {
            params.put("id", productCondition.categoryID);
        }

        /** 按照 productCondition.sort 字段进行 order 排序*/
        if (!SPStringUtils.isEmpty(productCondition.orderdesc) && !SPStringUtils.isEmpty(productCondition.orderby)) {
            params.put("orderby", productCondition.orderby);
            params.put("orderdesc", productCondition.orderdesc);
        }

        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
                    JSONObject resultJson = (JSONObject) response.getJSONObject(Response.RESULT);
                    JSONObject dataJson = new JSONObject();
                    List<SPFilter> filters = new ArrayList<SPFilter>();

                    if (resultJson != null) {
                        //排序URL
                        SPShopOrder shopOrder = SPJsonUtil.fromJsonToModel(resultJson, SPShopOrder.class);
                        if (shopOrder != null) {
                            dataJson.put("order", shopOrder);
                        }
                        //商品列表
                        if (!resultJson.isNull("goods_list")) {
                            JSONArray goodsList = resultJson.getJSONArray("goods_list");
                            if (goodsList != null) {
                                List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(goodsList, SPProduct.class);
                                dataJson.put("product", products);
                            }
                        }

                        //选中菜单
                        if (!resultJson.isNull("filter_menu")) {
                            JSONArray menuJson = resultJson.getJSONArray("filter_menu");
                            if (menuJson != null) {
                                List<SPFilterItem> menus = SPJsonUtil.fromJsonArrayToList(menuJson, SPFilterItem.class);
                                for (SPFilterItem item : menus) {
                                    item.setIsHighLight(true);
                                }
                                SPFilter menuFilter = new SPFilter(1, "1", "选择分类", menus);
                                dataJson.put("menu", menuFilter);
                            }
                        }

                        //规格
                        if (!resultJson.isNull("filter_spec")) {
                            JSONArray filterSpecJson = resultJson.getJSONArray("filter_spec");
                            if (filterSpecJson != null) {
                                List<SPFilter> specs = SPJsonUtil.fromJsonArrayToList(filterSpecJson, SPFilter.class);
                                for (SPFilter spec : specs) {
                                    spec.setItems(SPJsonUtil.fromJsonArrayToList(spec.getItemJsonArray(), SPFilterItem.class));
                                }
                                filters.addAll(specs);
                            }
                        }

                        //属性
                        if (!resultJson.isNull("filter_attr")) {
                            JSONArray attrJson = resultJson.getJSONArray("filter_attr");
                            if (attrJson != null) {
                                List<SPFilter> attrs = SPJsonUtil.fromJsonArrayToList(attrJson, SPFilter.class);
                                for (SPFilter attr : attrs) {
                                    attr.setItems(SPJsonUtil.fromJsonArrayToList(attr.getItemJsonArray(), SPFilterItem.class));
                                }
                                filters.addAll(attrs);
                            }
                        }


                        //品牌
                        if (!resultJson.isNull("filter_brand")) {
                            JSONArray brandJson = resultJson.getJSONArray("filter_brand");
                            if (brandJson != null) {
                                List<SPFilterItem> brands = SPJsonUtil.fromJsonArrayToList(brandJson, SPFilterItem.class);
                                SPFilter brandFilter = new SPFilter(4, "4", "品牌", brands);
                                filters.add(brandFilter);
                            }
                        }

                        //价格
                        if (!resultJson.isNull("filter_price")) {
                            JSONArray priceJson = resultJson.getJSONArray("filter_price");
                            if (priceJson != null) {
                                List<SPFilterItem> prices = SPJsonUtil.fromJsonArrayToList(priceJson, SPFilterItem.class);
                                SPFilter priceFilter = new SPFilter(5, "5", "价格", prices);
                                filters.add(priceFilter);
                            }
                        }
                        dataJson.put("filter", filters);
                        successListener.onRespone(msg, dataJson);
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
     * 商品分类接口：
     * 地址：http://192.168.0.169:8080/index.php/Api/Goods/get_goods_category_tree
     * 参数： 无
     *
     * @param successListener
     * @param failuredListener
     */
    public static void getShopCategory(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Goods", "get_goods_category_tree");

        RequestParams params = new RequestParams();
//		params.put("p",productCondition.page);
//		params.put("pagesize", MobileConstants.SizeOfPage);
//
//		if (productCondition.href !=null){
//			url = MobileConstants.BASE_HOST +productCondition.href;
//		}
//		/** 商品分类*/
//		if(productCondition.categoryID > 0){
//			params.put("id", productCondition.categoryID);
//		}
//
//		/** 按照 productCondition.sort 字段进行 order 排序*/
//		if(!SPStringUtils.isEmpty(productCondition.orderdesc) && !SPStringUtils.isEmpty(productCondition.orderby)){
//			params.put("orderby", productCondition.orderby);
//			params.put("orderdesc", productCondition.orderdesc );
//		}
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(MobileConstants.Response.MSG);
//					JSONObject resultJson = (JSONObject) response.getJSONObject(Response.DATA);
                    JSONObject dataJson = new JSONObject();

                    if (response != null) {
//						ShopCategoryBean shopOrder = SPJsonUtil.fromJsonToModel(resultJson, ShopCategoryBean.class);
                        //商品列表
                        if (!response.isNull("data")) {
                            JSONArray goods = response.getJSONArray("data");
                            if (goods != null) {
                                List<Data> homeCategories = SPJsonUtil.fromJsonArrayToList(goods, Data.class);
                                for (int i = 0; i < goods.length(); i++) {
                                    JSONObject entityObj = goods.getJSONObject(i);
                                    if (entityObj.isNull("tmenu")) {
                                        continue;  //空数据处理
                                    }
                                    JSONArray products = entityObj.getJSONArray("tmenu");
                                    List<Tmenu> pros = SPJsonUtil.fromJsonArrayToList(products, Tmenu.class);
                                    homeCategories.get(i).setTmenu(pros);
                                }
                                dataJson.put("Categories", homeCategories);
                            }
                        }

//						dataJson.put("filter", filters);
                        successListener.onRespone(msg, dataJson);
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
     * @Description: 搜索列表 -> 搜索结果
     * @url Api/Goods/search/q/相机
     */
    public static void searchResultProductListWithPage(int page, String searchKey, String href, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "search");
        if (!SPStringUtils.isEmpty(href)) {
            url = MobileConstants.BASE_HOST + href;
        }

        RequestParams params = new RequestParams();
        params.put("p", page);
        params.put("pagesize", MobileConstants.SizeOfPage);

        if (!SPStringUtils.isEmpty(searchKey)) {
            params.put("q", searchKey);
        }

        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    JSONObject resultJson = (JSONObject) response.getJSONObject(Response.RESULT);
                    JSONObject dataJson = new JSONObject();
                    List<SPFilter> filters = new ArrayList<SPFilter>();

                    if (resultJson != null) {
                        //排序URL
                        SPShopOrder shopOrder = SPJsonUtil.fromJsonToModel(resultJson, SPShopOrder.class);
                        if (shopOrder != null) {
                            dataJson.put("order", shopOrder);
                        }
                        //商品列表
                        if (!resultJson.isNull("goods_list")) {
                            JSONArray goodsList = resultJson.getJSONArray("goods_list");
                            if (goodsList != null) {
                                List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(goodsList, SPProduct.class);
                                dataJson.put("product", products);
                            }
                        }

                        //选中菜单
                        if (!resultJson.isNull("filter_menu")) {
                            JSONArray menuJson = resultJson.getJSONArray("filter_menu");
                            if (menuJson != null) {
                                List<SPFilterItem> menus = SPJsonUtil.fromJsonArrayToList(menuJson, SPFilterItem.class);
                                for (SPFilterItem item : menus) {
                                    item.setIsHighLight(true);
                                }
                                SPFilter menuFilter = new SPFilter(1, "1", "选择分类", menus);
                                dataJson.put("menu", menuFilter);
                            }
                        }

                        //规格
                        if (!resultJson.isNull("filter_spec")) {
                            JSONArray filterSpecJson = resultJson.getJSONArray("filter_spec");
                            if (filterSpecJson != null) {
                                List<SPFilter> specs = SPJsonUtil.fromJsonArrayToList(filterSpecJson, SPFilter.class);
                                for (SPFilter spec : specs) {
                                    spec.setItems(SPJsonUtil.fromJsonArrayToList(spec.getItemJsonArray(), SPFilterItem.class));

                                }
                                filters.addAll(specs);
                            }
                        }

                        //属性
                        if (!resultJson.isNull("filter_attr")) {
                            JSONArray attrJson = resultJson.getJSONArray("filter_attr");
                            if (attrJson != null) {
                                List<SPFilter> attrs = SPJsonUtil.fromJsonArrayToList(attrJson, SPFilter.class);
                                for (SPFilter attr : attrs) {
                                    attr.setItems(SPJsonUtil.fromJsonArrayToList(attr.getItemJsonArray(), SPFilterItem.class));
                                    ;
                                }
                                filters.addAll(attrs);
                            }
                        }
                        //品牌
                        if (!resultJson.isNull("filter_brand")) {
                            JSONArray brandJson = resultJson.getJSONArray("filter_brand");
                            if (brandJson != null) {
                                List<SPFilterItem> brands = SPJsonUtil.fromJsonArrayToList(brandJson, SPFilterItem.class);
                                SPFilter brandFilter = new SPFilter(4, "4", "品牌", brands);
                                filters.add(brandFilter);
                            }
                        }
                        //价格
                        if (!resultJson.isNull("filter_price")) {
                            JSONArray priceJson = resultJson.getJSONArray("filter_price");
                            if (priceJson != null) {
                                List<SPFilterItem> prices = SPJsonUtil.fromJsonArrayToList(priceJson, SPFilterItem.class);
                                SPFilter priceFilter = new SPFilter(5, "5", "价格", prices);
                                filters.add(priceFilter);
                            }
                        }
                        dataJson.put("filter", filters);
                        successListener.onRespone(msg, dataJson);
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
     * @param productCondition 参数(包含goodsID)
     * @param failuredListener
     * @return void    返回类型
     * @throws
     * @Description: 获取商品
     * @ 设定文件
     */
    public static void getProductByID(ProductCondition productCondition, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
//		assert(successListener!=null);
//		assert(failuredListener!=null);
        String url = SPMobileHttptRequest.getRequestUrl("Goods", "goodsInfo");

        RequestParams params = new RequestParams();
        if (productCondition.goodsID > 0) {
            params.put("id", productCondition.goodsID);
        }
//        SMobileLog.i(TAG, "getProductByID -> url : " + url + "&" + params.toString());
        SPMobileHttptRequest.post2(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dataJson = new JSONObject();
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.get(Response.MSG);
                    JSONObject result = (JSONObject) response.getJSONObject(Response.RESULT);
                    SPProduct product = null;
                    if (result.has("goods")) {
                        //属性
                        product = SPJsonUtil.fromJsonToModel(result.getJSONObject("goods"), SPProduct.class);
                        if (product.getAttrJsonArray() != null) {
                            product.setAttrArr(SPJsonUtil.fromJsonArrayToList(product.getAttrJsonArray(), ProductAttribute.class));
                        }
                        //规格
                        if (product.getSpecJsonArray() != null) {
                            product.setSpecArr(SPJsonUtil.fromJsonArrayToList(product.getSpecJsonArray(), SPProductSpec.class));
                        }
                    }
                    if (result.has("comment")) {
                        Object obj = result.get("comment");
                        if (!SSUtils.isEmpty(obj)) {
                            List<GoodsComment> comments = SPJsonUtil.fromJsonArrayToList(result.getJSONArray("comment"), GoodsComment.class);
                            dataJson.put("comments", comments);
                        }
                    }
                    if (product != null && result.has("gallery")) {
                        JSONArray jsonGarrys = result.getJSONArray("gallery");
                        dataJson.put("gallery", jsonGarrys);
                    }
                    if (product != null) {
                        dataJson.put("product", product);
                    }
                    if (result.has("spec_goods_price") && !SPStringUtils.isEmpty(result.getString("spec_goods_price"))) {
                        Object obj = result.get("spec_goods_price");
                        JSONObject priceJson = result.getJSONObject("spec_goods_price");
                        dataJson.put("price", priceJson);
                    }
                    if (result.has("cart_num")) {
                        String cart_num = result.getString("cart_num");
                        dataJson.put("cart_num", cart_num);
                    }
                    if (result.has("is_collect")) {
                        String is_collect = result.getString("is_collect");
                        dataJson.put("is_collect", is_collect);
                    }
                    successListener.onRespone(msg, dataJson);
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
     * @throws
     * @Description: 获取购物车商品列表 同一个接口(购物车所有商品数量)
     * @URL index.php/Api/Cart/cartList
     */
    public static void getShopCartNumber(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cartList");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dataJson = new JSONObject();
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject resultJson = response.getJSONObject("result");
                        JSONObject feeJson = resultJson.getJSONObject("total_price");
                        int count = 0;
                        if (feeJson.has("num")) {
                            count = feeJson.getInt("num");
                        }

                        successListener.onRespone(msg, count);
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
     * 添加商品到购物车（对购物车商品数量操作，数量增加或减少）
     *
     * @param goodsID          商品id
     * @param specs            商品规格id
     * @param number           数量
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL Api/Cart/addCart?goods_spec[尺码]=3&goods_spec[颜色]=4&goods_num=2&goods_id=1
     */
    public static void shopCartGoodsOperation(String goodsID, String specs, int number, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Cart", "addCart");
        RequestParams params = new RequestParams();
        params.put("goods_num", number);
        params.put("goods_id", goodsID);
        if (!SPStringUtils.isEmpty(specs)) {
            params.put("goods_spec", "[" + specs + "]");
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dataJson = new JSONObject();
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        int count = response.getInt(Response.RESULT);
                        successListener.onRespone(msg, count);
                    } else {
                        failuredListener.handleResponse(msg, status);
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
     * @URL index.php/Api/Cart/cartList
     * 获取购物车商品列表
     */
    public static void getShopCartList(JSONArray formDataArray, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cartList");

        RequestParams params = new RequestParams();

        String formData = formDataArray.toString();

        if (formDataArray != null && formDataArray.length() > 0) {
            //表单数据: POST提交
            params.put("cart_form_data", formData);
        }
//		params.put("test" ,"1");

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject jsonObject = new JSONObject();
                        if (response.has("result")) {
                            JSONObject resultJson = response.getJSONObject("result");
                            if (resultJson.has("cartList")) {
                                List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("cartList"), SPProduct.class);
                                jsonObject.put("products", products);
                            }
                            if (resultJson.has("total_price")) {
                                JSONObject feeJson = resultJson.getJSONObject("total_price");
                                if (feeJson != null && feeJson.has("total_fee")) {
                                    double totalFee = feeJson.getDouble("total_fee");
                                    jsonObject.put("totalFee", totalFee);//总金额(需要支付的金额
                                }
                                if (feeJson != null && feeJson.has("cut_fee")) {
                                    double cutFee = feeJson.getDouble("cut_fee");//节省金额
                                    jsonObject.put("cutFee", cutFee);
                                }
                            }
                        }
                        successListener.onRespone(msg, jsonObject);
                    } else {
                        failuredListener.handleResponse(msg, status);
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
     * @URL index.php/Api/Cart/cart2
     * 获取确认订单数据(购物车订单填写页)
     */
    public static void getConfirmOrderData(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cart2");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status >=0) {
                        if(status==1) {
                            JSONObject jsonObject = new JSONObject();
                            if (response.has("result")) {
                                JSONObject resultJson = response.getJSONObject("result");
//							地址信息
                                if (!resultJson.isNull("addressList") && resultJson.has("addressList")) {
                                    if (resultJson.getJSONObject("addressList") != null) {  //判断是否为空
                                        SPConsigneeAddress consignees = SPJsonUtil.fromJsonToModel(resultJson.getJSONObject("addressList"), SPConsigneeAddress.class);
                                        jsonObject.put("consigneeAddress", consignees);
                                    }
                                }
                                //物流信息
                                //商品列表
                                if (!resultJson.isNull("cartList") && resultJson.has("cartList")) {
                                    List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("cartList"), SPProduct.class);
                                    jsonObject.put("products", products);//总金额(需要支付的金额
                                }
                                //优惠券, 代金券
                                if (!resultJson.isNull("cartList") && resultJson.has("cartList")) {
                                    List<Coupon> coupons = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("couponList"), Coupon.class);
                                    jsonObject.put("coupons", coupons);
                                }
                                if (!resultJson.isNull("userInfo") && resultJson.has("userInfo")) {
                                    JSONObject userJson = resultJson.getJSONObject("userInfo");
                                    jsonObject.put("userInfo", userJson);
                                }
                                if (!resultJson.isNull("totalPrice") && resultJson.has("totalPrice")) {
                                    JSONObject totalPriceJson = resultJson.getJSONObject("totalPrice");
                                    Totalprice totalprice = SPJsonUtil.fromJsonToModel(totalPriceJson, Totalprice.class);
                                    jsonObject.put("totalPrice", totalprice);
                                }
                            }
                            successListener.onRespone(msg, jsonObject);
                        }else{
                            failuredListener.onRespone(msg, -1);
                        }
                    } else {
                        failuredListener.handleResponse(msg, status);
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
     * @URL index.php/Api/Cart/cart2
     * 获取确积分兑换商品   认订单数据(购物车订单填写页)
     * 接口更改为Cart/cart22 （参数unique_id      user_id       token      goods_id      spec_key
     */
    public static void getPointsConfirmOrderData(String goods_id,String spec_key,final SPSuccessListener
            successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cart22");
        RequestParams params = new RequestParams();
        params.put("goods_id", goods_id);
        params.put("spec_key", spec_key);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status >=0) {
                        if(status==1) {
                            JSONObject jsonObject = new JSONObject();
                            if (response.has("result")) {
                                JSONObject resultJson = response.getJSONObject("result");
//							地址信息
                                if (!resultJson.isNull("addressList") && resultJson.has("addressList")) {
                                    if (resultJson.getJSONObject("addressList") != null) {  //判断是否为空
                                        SPConsigneeAddress consignees = SPJsonUtil.fromJsonToModel(resultJson.getJSONObject("addressList"), SPConsigneeAddress.class);
                                        jsonObject.put("consigneeAddress", consignees);
                                    }
                                }
                                //物流信息
                                //商品列表
                                if (!resultJson.isNull("cartList") && resultJson.has("cartList")) {
                                    List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("cartList"), SPProduct.class);
                                    jsonObject.put("products", products);//总金额(需要支付的金额
                                }
                                //优惠券, 代金券
                                if (!resultJson.isNull("cartList") && resultJson.has("cartList")) {
                                    List<Coupon> coupons = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("couponList"), Coupon.class);
                                    jsonObject.put("coupons", coupons);
                                }
                                if (!resultJson.isNull("userInfo") && resultJson.has("userInfo")) {
                                    JSONObject userJson = resultJson.getJSONObject("userInfo");
                                    jsonObject.put("userInfo", userJson);
                                }
                                if (!resultJson.isNull("totalPrice") && resultJson.has("totalPrice")) {
                                    JSONObject totalPriceJson = resultJson.getJSONObject("totalPrice");
                                    Totalprice totalprice = SPJsonUtil.fromJsonToModel(totalPriceJson, Totalprice.class);
                                    jsonObject.put("totalPrice", totalprice);
                                }
                            }
                            successListener.onRespone(msg, jsonObject);
                        }else{
                            failuredListener.onRespone(msg, -1);
                        }
                    } else {
                        failuredListener.handleResponse(msg, status);
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
     * @URL index.php?m=Api&c=Cart&a=cart3
     * 根据选择的订单信息查询总价(物流, )
     */
    public static void getOrderTotalFee(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

//		assert(successListener!=null);
//		assert(failuredListener!=null);
        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cart3");

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject jsonObject = null;
                        if (response.has("result")) {
                            jsonObject = response.getJSONObject("result");
                        }
                        successListener.onRespone(msg, jsonObject);
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
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Cart&a=cart3
     * 提交订单(该方法与getOrderTotalFeeWithParams获取商品价格信息URL参数,
     * URL基本一致, 主要区别在于act参数)
     */
    public static void submitOrder(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cart3");

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    PayOrder payOrder = null;
                    if (status > 0) {
                        if (response.has("data")) {
                            payOrder = SPJsonUtil.fromJsonToModel(response.getJSONObject(Response.DATA), PayOrder.class);
                        }
                        successListener.onRespone(msg, payOrder);
                    } else {
                        failuredListener.handleResponse(msg, status);
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
     * @param goodsID          description
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Goods&a=getGoodsComment&goods_id=1
     * 获取商品评论
     */
    public static void getGoodsCommentWithGoodsID(String goodsID, int page, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Goods", "getGoodsComment");

        RequestParams params = new RequestParams();
        params.put("goods_id", goodsID);
        params.put("p", page);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    List<GoodsComment> comments = null;
                    if (status > 0) {
                        String orderId = null;
                        if (response.has("result")) {
                            comments = SPJsonUtil.fromJsonArrayToList(response.getJSONArray("result"), GoodsComment.class);
                            for (GoodsComment goodsComment : comments) {
                                if (goodsComment.getImageArray() == null) {
                                    continue;
                                }
                                List<String> images = SPMobileHttptRequest.convertJsonArrayToList(goodsComment.getImageArray());
                                goodsComment.setImages(images);
                            }
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
     * 获取服务器时间戳
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Base&a=getServerTime
     */
    public static void refreshServiceTime(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Base", "getServerTime");

        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        long serviceTime = response.getLong("result");
                        long localTime = SPCommonUtils.getCurrentTime();
                        long cutServiceTime = serviceTime - localTime;
                        MobileApplication.getInstance().setCutServiceTime(cutServiceTime);
                        successListener.onRespone(msg, cutServiceTime);
                    } else {
                        failuredListener.onRespone(msg, status);
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
     * 删除购物车的商品
     *
     * @param cartIds          多个商品ID用逗号分隔
     * @param successListener  <#success description#>
     * @param failuredListener <#failure description#>
     * @url index.php?m=Api&c=Cart&a=delCart
     */
    public static void deleteShopCartProductWithIds(String cartIds, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {

        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Cart", "delCart");

        RequestParams params = new RequestParams();
        params.put("ids", cartIds);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /** 针对返回的业务数据会重新包装一遍再返回到View */
                    String msg = (String) response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        int count = response.getInt(Response.RESULT);
                        successListener.onRespone(msg, count);
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
     * @param page
     * @param successListener
     * @param failuredListener
     * @return void    返回类型
     * @throws
     * @Description: 猜你喜欢/热门推荐
     * @ 设定文件
     */
    public static void guessYouLike(int page, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("Goods", "guessYouLike");
        RequestParams params = new RequestParams();
        params.put("p", page);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONArray resulJson = response.getJSONArray(Response.RESULT);
                        List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(resulJson, SPProduct.class);
                        successListener.onRespone(msg, products);
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
     * 银联支付
     * @param orderid
     * @param txnAmt           价格 单位 分
     * @param successListener
     * @param failuredListener
     */
    public static void orderUnionPay(String orderid, String txnAmt, final SPSuccessListener successListener,
                                     final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = MobileConstants.PAY_Union;
        RequestParams params = new RequestParams();
        params.put("orderid", orderid);
        params.put("txnAmt", txnAmt);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status == 0) {
                        String id = response.getString(Response.DATA);
                        successListener.onRespone(msg, id);
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
     * 支付宝 支付
     * @param orderid
     * @param successListener
     * @param failuredListener
     */

    public static void orderAliPay(String orderid, final SPSuccessListener successListener,
                                final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = MobileConstants.PAY_ALIPAY;
        RequestParams params = new RequestParams();
        params.put("order_id", orderid);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    Alipay alipay = null;
                    if (status == 0) {
                        if (response.has("data")) {
                            alipay = SPJsonUtil.fromJsonToModel(response.getJSONObject(Response.DATA), Alipay.class);
                        }
                        successListener.onRespone(msg, alipay);
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
     * 使用石头支付
     * @param order_sn
     * @param successListener
     * @param failuredListener
     */
    public static void orderPayUseStone(String order_sn, final SPSuccessListener successListener,
                                        final SPFailuredListener failuredListener) {

        assert (successListener != null);
        assert (failuredListener != null);
        String url = MobileConstants.PAY_STONE;
        RequestParams params = new RequestParams();
        params.put("order_sn", order_sn);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status == 0) {
//						String id = response.getString(Response.DATA);
                        successListener.onRespone(msg, status);
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
     * 2期接口
     * 使用石头支付
     * 接口地址：http://shssjk.com/index.php/UnionPay/Buy/stone_pay
     参数：order_id（订单id）
     * @param successListener
     * @param failuredListener
     */
    public static void orderPayWithStone(String order_id, final SPSuccessListener successListener,
                                        final SPFailuredListener failuredListener) {

        assert (successListener != null);
        assert (failuredListener != null);
        String url = MobileConstants.PAY_STONE_2;
        RequestParams params = new RequestParams();
        params.put("order_id", order_id);

        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status == 0) {
//						String id = response.getString(Response.DATA);
                        successListener.onRespone(msg, status);
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
     *
     * @param shipping_name
     * @param shipping_code
     * @param successListener
     * @param failuredListener
     */
    public static void ordeAddReturnAddress(String shipping_name, String shipping_code, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = SPMobileHttptRequest.getRequestUrl("User", "set_return_info");
        RequestParams params = new RequestParams();
        params.put("shipping_name", shipping_name);
        params.put("shipping_code", shipping_code);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status == 0) {
                        String id = response.getString(Response.DATA);
                        successListener.onRespone(msg, id);
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
     * index.php/UnionPay/Buy/order_info
       order_id
     * @param shipping_name
     * @param shipping_code
     * @param successListener
     * @param failuredListener
     */
    /**
     * @param order_id
     * @param successListener
     * @param failuredListener
     */
    public static void getOrderInfoPayWithStone(String order_id, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);
        String url = MobileConstants.PAY_STONE_INFO;

        RequestParams params = new RequestParams();
        params.put("order_id", order_id);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /** 针对返回的业务数据会重新包装一遍再返回到View */
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status == 0) {
                        StoneOrderInfo    orderInfo = SPJsonUtil.fromJsonToModel(response.getJSONObject(Response.DATA), StoneOrderInfo.class);
                        successListener.onRespone(msg, orderInfo);
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



}
