package com.shssjk.activity.person;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.BaseActivity;

/**
 * 创业说明
 */
public class StartBusinessActivity extends BaseActivity {
    FrameLayout titlbarFl;
    Button backBtn;       //返回按钮
    TextView titleTxtv; //标题
    WebView mWebView;
    private ProgressBar pg1;

    private static String URL = "http://shssjk.com/index.php/Mobile/Partner/chuangye";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.startbusinessactivity_title));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        super.init();
    }

    @Override
    public void initSubViews() {
        //        标题内容
        titlbarFl = (FrameLayout) findViewById(R.id.titlebar_layout);
        pg1 = (ProgressBar) findViewById(R.id.progressBar1);
        titlbarFl.setBackgroundColor(ContextCompat.getColor(this, R.color.shop_title)); //or which ever color do you want
        titleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setTextColor(ContextCompat.getColor(this, R.color.white));
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
//       显示进度条
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pg1.setVisibility(View.GONE);//
                } else {
                    pg1.setVisibility(View.VISIBLE);//
                    pg1.setProgress(newProgress);//
                }
            }
        });



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
