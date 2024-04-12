package com.jambox.webview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

public class WebviewObject {

    private WebView webview;
    private Context context;
    private JamboxAdsHelper applovinHelper;

    public WebviewObject(Context context)
    {
        this.context = context;
    }

    public void StartWebview()
    {
        webview = new WebView(context);
        Activity activity = (Activity) context;
        activity.setContentView(webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Unity");
        //webview.loadUrl("https://play.playbo.in/");
        webview.loadUrl("https://jamgame.jambox.games/");
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
        switch (msg){
            case "RW":
                JamboxAdsHelper.ShowRewarded(new OnRewardedAdListener()
                {
                    @Override
                    public void OnAdDisplayFailed()
                    {
                        System.out.println("RW display Failed!!");
                        String script = "handleMaxRewardedCallback(" + GetCallbackJson(JamboxAdsHelper.AdsCode.ADS_NOT_SHOWN) + ")";
                        webview.evaluateJavascript(script, null);
                    }

                    @Override
                    public void OnAdDisplayed()
                    {
                        System.out.println("RW Displayed!!");
                        String script = "handleMaxRewardedCallback(" + GetCallbackJson(JamboxAdsHelper.AdsCode.BEFORE_ADS_SHOWN) + ")";
                        webview.evaluateJavascript(script, null);
                    }

                    @Override
                    public void OnAdCompleted()
                    {
                        System.out.println("RW completed!");
                        String script = "handleMaxRewardedCallback(" + GetCallbackJson(JamboxAdsHelper.AdsCode.ADS_WATCH_SUCCESS) + ")";
                        webview.evaluateJavascript(script, null);
                    }

                    @Override
                    public void OnAdHidden()
                    {
                        System.out.println("RW Hidden!!");
                        String script = "handleMaxRewardedCallback(" + GetCallbackJson(JamboxAdsHelper.AdsCode.ADS_VIEWED_OR_DISMISSED) + ")";
                        webview.evaluateJavascript(script, null);
                    }
                });
                break;
            case "IS":
                JamboxAdsHelper.ShowInterstitial(null);
                break;
            default:
                System.out.println("No Match Found");
                break;
        }
        System.out.println(msg);
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
