package com.jambox.monetisation;

import android.app.Application;

public class AdjustApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        AdjustHelper.Initialize(this);
    }
}
