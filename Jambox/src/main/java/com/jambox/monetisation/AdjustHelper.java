package com.jambox.monetisation;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;

public class AdjustHelper
{
    public static void Initialize(Context context)
    {
        ApplicationInfo applicationInfo = null;
        try
        {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            System.out.println("ERROR: Adjust Initialization failed : " + e);
            return;
        }

        String appToken = applicationInfo.metaData.getString("adjust_app_token");
        AdjustConfig config = new AdjustConfig(context, appToken, AdjustConfig.ENVIRONMENT_PRODUCTION);
        Adjust.onCreate(config);
    }

    public static void TrackEvent(String eventId)
    {
        AdjustEvent event = new AdjustEvent(eventId);
        Adjust.trackEvent(event);
    }

    public static void onResume()
    {
        Adjust.onResume();
    }

    public static void onPause()
    {
        Adjust.onPause();
    }

    public enum AdjustEnv
    {
        ENVIRONMENT_SANDBOX,
        ENVIRONMENT_PRODUCTION
    }
}
