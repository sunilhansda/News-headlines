package com.example.sunilhansda.inshorts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunilhansda.inshorts.R;

/**
 * Created by SUNIL HANSDA on 16-Sep-17.
 */

public class Webview extends AppCompatActivity {

    private WebView myWebview;
    private Toolbar toolbar;
    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        String url = intent.getExtras().getString("URL");
        String title = intent.getExtras().getString("TITLE");

         myWebview = (WebView) findViewById(R.id.webview);
        final TextView txtview = (TextView)findViewById(R.id.tV1);
        pbar = (ProgressBar) findViewById(R.id.pB1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        myWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && pbar.getVisibility() == ProgressBar.GONE){
                    pbar.setVisibility(ProgressBar.VISIBLE);
                    txtview.setVisibility(View.VISIBLE);
                }

                pbar.setProgress(progress);
                if(progress == 100) {
                    pbar.setVisibility(ProgressBar.GONE);
                    txtview.setVisibility(View.GONE);
                }
            }
        });

        myWebview.setWebViewClient(new WebViewClient() {
          @Override
          public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
          super.onReceivedError(view, request, error);
          Toast.makeText(Webview.this, "Cannot load page", Toast.LENGTH_SHORT).show();
              }
        });


        myWebview.loadUrl(url);

        WebSettings webSettings = myWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebview.setHorizontalScrollBarEnabled(true);
        myWebview.setVerticalScrollBarEnabled(true);
        myWebview.getSettings().setLoadWithOverviewMode(true);
        myWebview.getSettings().setUseWideViewPort(true);
        myWebview.getSettings().setBuiltInZoomControls(true);

    }
}
