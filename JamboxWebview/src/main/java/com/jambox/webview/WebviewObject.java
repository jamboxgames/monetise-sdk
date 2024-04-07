package com.jambox.webview;

import android.app.Activity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebviewObject {

    private WebView webview;
    private Activity activity;
    private ApplovinMaxHelper applovinHelper;

    public WebviewObject(Activity activity)
    {
        this.activity = activity;
        this.applovinHelper = new ApplovinMaxHelper(activity);
    }

    public void StartWebview()
    {
        webview = new WebView(activity);
        activity.setContentView(webview);
        WebSettings webSettings = webview.getSettings();
        //webview.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(applovinHelper), "Unity");
        //webview.loadUrl("https://play.playbo.in/");
        webview.loadUrl("http://test.jambox.games/test1.html");
        webview.setVisibility(View.VISIBLE);
    }

    public void CloseWebview()
    {
        webview.stopLoading();
        webview.setVisibility(View.GONE);
    }

}
