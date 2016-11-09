/**
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年10月20日 下午7:19:26
 * Description:MineFragment
 *
 * @version V1.0
 */
package com.shssjk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shssjk.activity.R;
import com.shssjk.activity.common.shop.ProductActivity;
import com.shssjk.http.base.SPFailuredListener;
import com.shssjk.http.base.SPSuccessListener;
import com.shssjk.http.condition.ProductCondition;
import com.shssjk.http.shop.ShopRequest;
import com.shssjk.model.SPProduct;
import com.shssjk.utils.SMobileLog;
import com.shssjk.utils.SPDialogUtils;

import org.json.JSONObject;


/**
 *  商品 -> 详情
 *
 */
public class ProductPictureTextDetaiFragment extends BaseFragment {

    private String mHtml;
    private Context mContext;
    private WebView mWebView;
    boolean isFirstLoad;
    private String mGoodsID;
    private SPProduct mProduct;
    private String TAG = "ProductPictureTextDetaiFragment";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void setContent(String content) {
        this.mGoodsID = content;
          }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//		获取地址
        this.mHtml = ((ProductActivity) getActivity()).getContents();

        isFirstLoad = true;
        View view = inflater.inflate(R.layout.common_webview_main, null, false);

        super.init(view);


        return view;
    }

    public void loadData(String mHtml) {
//        mWebView.loadDataWithBaseURL(null, mHtml, "text/html", "utf-8", null);
//        this.mHtml = ((ProductActivity) getActivity()).getContents();
//        mWebView.loadDataWithBaseURL(null, mHtml, "text/html", "utf-8", null);
        if ( mWebView != null) {
        String    htmlData = mHtml.replace("<img", "<img style='max-width:100%;height:auto;'");
            mWebView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
            isFirstLoad = false;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void initSubView(View view) {

        mWebView = (WebView) view.findViewById(R.id.common_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setBuiltInZoomControls(false);
//        mWebView.getSettings().setSupportZoom(false);//设定支持缩放
//        mWebView.getSettings().setUseWideViewPort(true);//。
//        mWebView.getSettings().setLoadWithOverviewMode(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);




    }

    @Override
    public void initEvent() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initData() {
        getProductDetails();
//        loadData();
    }

    public void getProductDetails() {

        /** 此处参数-1 : 意味着返回的是左边分类  */
        ProductCondition condition = new ProductCondition();
        if (mGoodsID == null) {
            condition.goodsID = -1;
        } else {
            condition.goodsID = Integer.valueOf(mGoodsID);
        }

        showLoadingToast();
        ShopRequest.getProductByID(condition, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingToast();
                try {
                    mDataJson = (JSONObject) response;
                    if (mDataJson != null && mDataJson.has("product")) {
                        mProduct = (SPProduct) mDataJson.get("product");
                        //                设置图文详情信息
//                        ((ProductActivity) getActivity()).setContents(mProduct.getGoodsContent());
                        loadData(mProduct.getGoodsContent());
                    }


                } catch (Exception e) {
                    showToast(e.getMessage());
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingToast();
                SMobileLog.e(TAG, "onRespone ,msg : " + msg);
                SPDialogUtils.showToast(mContext, msg);
            }
        });
    }

}
