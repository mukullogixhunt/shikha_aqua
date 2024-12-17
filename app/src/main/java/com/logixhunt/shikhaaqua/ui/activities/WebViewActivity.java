package com.logixhunt.shikhaaqua.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.logixhunt.shikhaaqua.R;
import com.logixhunt.shikhaaqua.databinding.ActivityWebViewBinding;
import com.logixhunt.shikhaaqua.ui.common.BaseActivity;
import com.logixhunt.shikhaaqua.utils.Constant;

public class WebViewActivity extends BaseActivity {

    private String TAG = WebViewActivity.class.getSimpleName();
    private ActivityWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialization();
    }

    private void initialization() {


        binding.webView.getSettings().setJavaScriptEnabled(true);
//        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.loadUrl(Constant.WEBVIEW_URL);
        binding.tvPageTitle.setText(Constant.WEBVIEW_TITLE);

        binding.webView.setVerticalScrollBarEnabled(true);
        binding.webView.setHorizontalScrollBarEnabled(true);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideLoader();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showLoader();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();//skip ssl error
        }
    }
}