package com.jambox.webview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

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
        //webview.evaluateJavascript("callFromUnity()", null);
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
                ApplovinMaxHelper.ShowRewarded(new OnRewardedCompleted()
                {
                    @Override
                    public void OnComplete()
                    {
                        System.out.println("RW completed!");
                    }
                });
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

}
