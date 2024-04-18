package com.Jambox.party;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.jambox.webview.JamboxAdsHelper;

public class AppOpenManager implements LifecycleObserver
{
    private final Context context;

    public AppOpenManager(final Context context)
    {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        this.context = context;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart()
    {
        JamboxAdsHelper.ShowAppOpenAd();
    }
}