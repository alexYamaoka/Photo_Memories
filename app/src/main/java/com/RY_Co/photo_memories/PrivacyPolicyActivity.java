package com.RY_Co.photo_memories;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PrivacyPolicyActivity extends AppCompatActivity
{
    private WebView webView;
    private final String url = "https://www.iubenda.com/privacy-policy/38684895";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        webView = findViewById(R.id.web_view);

        webView.getSettings().setJavaScriptEnabled(true);       // enable javascript
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl(url);
    }
}
