package com.jambox.webview;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.json.JSONObject;

public class WebviewObject {

    private WebView webview;
    private Context context;
    private String ClientId;
    private ViewGroup.LayoutParams webviewLayout;
    private boolean isBannerOpenedByWebview = false;

    public WebviewObject(Context context, String ClientId)
    {
        this.context = context;
        this.ClientId = ClientId;
        this.webviewLayout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    public WebviewObject(Context context, String ClientId, ViewGroup.LayoutParams webviewLayout)
    {
        this.context = context;
        this.ClientId = ClientId;
        this.webviewLayout = webviewLayout;
    }

    public void StartWebview()
    {
        if (!JamboxAdsHelper.IsInitialized)
        {
            Log.e("JamboxWebview", "ERROR: Please make sure JamboxAdsHelper is initialized before starting H5 Games");
            return;
        }

        if (webview == null)
        {
            webview = new WebView(context);
            Activity activity = (Activity) context;
            //activity.setContentView(webview);

            FrameLayout.LayoutParams layout = (FrameLayout.LayoutParams)webviewLayout;
            layout.bottomMargin = JamboxAdsHelper.GetBannerHeightInPx() + 20;

            activity.addContentView(webview, layout);
        }

        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient());
        webview.addJavascriptInterface(new WebAppInterface(this), "Unity");
        webview.loadUrl("https://jamgame.jambox.games/?channel_id=" + ClientId);
        webview.setVisibility(View.VISIBLE);

        if (!JamboxAdsHelper.IsShowingBanner())
        {
            isBannerOpenedByWebview = true;
            JamboxAdsHelper.ShowBannerAd(JamboxAdsHelper.BannerPosition.BOTTOM);
        }
    }

    public void CloseWebview()
    {
        if (isBannerOpenedByWebview)
        {
            isBannerOpenedByWebview = false;
            JamboxAdsHelper.HideBannerAd();
        }

        ((ViewGroup) webview.getParent()).removeView(webview);
        webview.destroy();
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
            case "banner_enable":
                JamboxAdsHelper.ShowBannerAd(JamboxAdsHelper.BannerPosition.BOTTOM);
                break;
            case "banner_disable":
                JamboxAdsHelper.HideBannerAd();
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
