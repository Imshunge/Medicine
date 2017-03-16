package com.shssjk.activity.person;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;

/**
 * 尚尚服务
 */
public class ServiceActivity extends BaseActivity {
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题
    WebView mWebView;
    private static String URL = "http://shssjk.com/index.php/Mobile/Partner/app_service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.setting_service));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        super.init();
    }
    @Override
    public void initSubViews() {
        //        标题内容
        titlbarFl= (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this,R.color.white));
        mWebView = (WebView) findViewById(R.id.about_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setSupportZoom(false);//设定支持缩放
    }

    @Override
    public void initData() {
        mWebView.loadUrl(URL);
    }

    @Override
    public void initEvent() {

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
