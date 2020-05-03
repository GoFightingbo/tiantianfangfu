package com.xiangxue.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DetailLinkActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;
    public final static String URL_KEY = "URL_KEY";
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_link);

        mWebView = findViewById(R.id.web_view);

        // 1.获取上个页面传递过来的url
        mUrl = getIntent().getStringExtra(URL_KEY);

        // 2.设置WebView的一些参数
        mWebSettings = mWebView.getSettings();// 获取WebView参数设置
        mWebSettings.setUseWideViewPort(false);  // 将图片调整到适合webview的大小
        mWebSettings.setJavaScriptEnabled(true); // 支持js
        mWebSettings.setLoadsImagesAutomatically(true);  // 支持自动加载图片

        // 3.利用WebView直接加载网页链接
        // 每次启动这个activity 所加载的url网页路径肯定是不一样的 ， Intent传值
        mWebView.loadUrl(mUrl);
    }
}
