package com.fireblack.zhihuibeijing;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.fireblack.zhihuibeijing.global.GlobalContants;

public class NewsDetailActivity extends Activity implements View.OnClickListener{

    private WebView mWebView;
    private ImageButton btnBack;
    private ImageButton btnShare;
    private ImageButton btnSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);

        mWebView = (WebView) findViewById(R.id.wv_web);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnSize = (ImageButton) findViewById(R.id.btn_size);

        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnSize.setOnClickListener(this);

        String url = getIntent().getStringExtra("url");
        url = url.replace("http://10.0.2.2:8080/zhbj",GlobalContants.SERVER_URI);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);// 表示支持js
        settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
        settings.setUseWideViewPort(true);// 支持双击缩放

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.loadUrl(url);//加载网页
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:
                showShare();
                break;
            case R.id.btn_size:
                showChooseDialog();
                break;
            default:
                break;
        }
    }

    private void showShare() {
    }

    private void showChooseDialog() {
    }
}
