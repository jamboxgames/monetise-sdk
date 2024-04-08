package com.jambox.webview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebviewObject {

    private WebView webview;
    private Context context;
    private ApplovinMaxHelper applovinHelper;

    public WebviewObject(Context context)
    {
        this.context = context;
        ApplovinMaxHelper.InitializeAds(context);
    }

    public void StartWebview()
    {
        webview = new WebView(context);
        Activity activity = (Activity) context;
        activity.setContentView(webview);
        WebSettings webSettings = webview.getSettings();
        //webview.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Unity");
        //webview.loadUrl("https://play.playbo.in/");
        webview.loadUrl("http://test.jambox.games/test1.html");
        webview.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                TestFunction();
            }
        }, 10000);
    }

    public void CloseWebview()
    {
        webview.stopLoading();
        webview.setVisibility(View.GONE);
    }

    public void WebviewCallback(String msg)
    {
        String eventName = msg.replace("form?msg=","");
        switch (eventName){
            case "RW":
                ApplovinMaxHelper.ShowRewarded();
                break;
            case "IS":
                ApplovinMaxHelper.ShowInterstitial();
                break;
            default:
                System.out.println("No Match Found");
                break;
        }
        System.out.println(eventName);
    }

    public void TestFunction()
    {
        System.out.println("Function test!!");
        //webview.evaluateJavascript("callFromUnity()", null);
    }

}
