package com.jambox.webview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import org.json.JSONObject;

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
                ApplovinMaxHelper.ShowRewarded(new OnRewardedAdListener()
                {
                    @Override
                    public void OnAdDisplayFailed()
                    {
                        System.out.println("RW display Failed!!");
                        String script = "handleMaxRewardedCallback(" + GetCallbackJson(ApplovinMaxHelper.AdsCode.ADS_NOT_SHOWN) + ")";
                        webview.evaluateJavascript(script, null);
                    }

                    @Override
                    public void OnAdDisplayed()
                    {
                        System.out.println("RW Displayed!!");
                        String script = "handleMaxRewardedCallback(" + GetCallbackJson(ApplovinMaxHelper.AdsCode.BEFORE_ADS_SHOWN) + ")";
                        webview.evaluateJavascript(script, null);
                    }

                    @Override
                    public void OnAdCompleted()
                    {
                        System.out.println("RW completed!");
                        String script = "handleMaxRewardedCallback(" + GetCallbackJson(ApplovinMaxHelper.AdsCode.ADS_WATCH_SUCCESS) + ")";
                        webview.evaluateJavascript(script, null);
                    }

                    @Override
                    public void OnAdHidden()
                    {
                        System.out.println("RW Hidden!!");
                        String script = "handleMaxRewardedCallback(" + GetCallbackJson(ApplovinMaxHelper.AdsCode.ADS_VIEWED_OR_DISMISSED) + ")";
                        webview.evaluateJavascript(script, null);
                    }
                });
                break;
            case "IS":
                ApplovinMaxHelper.ShowInterstitial(null);
                break;
            default:
                System.out.println("No Match Found");
                break;
        }
        System.out.println(eventName);
    }

    private String GetCallbackJson(int code)
    {
        try
        {
            JSONObject json = new JSONObject();
            json.put("code", code);
            return json.toString();
        }
        catch (Exception e)
        {
            System.out.println("Error Json: " + e);
            return "";
        }
    }

}
