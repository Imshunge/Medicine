package com.shssjk.activity.person;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;

public class HelpActivity extends BaseActivity {
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.setting_help));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        super.init();
    }

    @Override
    public void initSubViews() {
        //        标题内容
        titlbarFl= (FrameLayout) findViewById(R.id.titlebar_layout);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this,R.color.white));

        mWebView = (WebView) findViewById(R.id.help_webview);
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
        mWebView.loadUrl("file:///android_asset/set_help.html");
    }

    @Override
    public void initEvent() {

    }
}
