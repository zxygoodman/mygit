package com.feicui.zxynews.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.feicui.zxynews.R;

public class NewsPageActivity extends AppCompatActivity {

    private WebView webViewNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        webViewNews = (WebView) findViewById(R.id.wv_news_item);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_progressBar);
        String link = getIntent().getStringExtra("link");
        webViewNews.loadUrl(link);
        webViewNews.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webViewNews.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                progressBar.setBackgroundColor(Color.RED);
                super.onProgressChanged(view, newProgress);
                if (progressBar.getProgress() == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK&&webViewNews.canGoBack()) {
            webViewNews.goBack();
        } else {
            finish();
        }
        return false;
    }
}
