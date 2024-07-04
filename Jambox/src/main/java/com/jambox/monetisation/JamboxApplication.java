package com.jambox.monetisation;

import android.app.Application;
import android.content.Context;

public class JamboxApplication extends Application
{
    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        JamboxData.Fetch(this, new OnDataFetchListener()
        {
            @Override
            public void OnDataFetched()
            {
                JamboxAdsHelper.OnDataFetched();
                AdjustHelper.Initialize(context);
            }
        });
    }
}
