package com.jambox.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {

    private ApplovinMaxHelper applovinMaxHelper;

    public WebAppInterface(ApplovinMaxHelper applovinMaxHelper) {
        this.applovinMaxHelper = applovinMaxHelper;
    }

    /** Show a toast from the web page. */
    @JavascriptInterface
    public void call(String msg) {
        String eventName = msg.replace("form?msg=","");
        switch (eventName){
            case "RW":
                applovinMaxHelper.ShowRW();
                break;
            case "IS":
                applovinMaxHelper.ShowIS();
                break;
            default:
                System.out.println("No Match Found");
                break;
        }
        System.out.println(msg);
    }
}
