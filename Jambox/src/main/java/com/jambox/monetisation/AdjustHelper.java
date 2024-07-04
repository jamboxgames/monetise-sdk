package com.jambox.monetisation;

import android.content.Context;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;

public class AdjustHelper
{
    public static void Initialize(Context context)
    {
        if (JamboxData.adjustId == null || JamboxData.adjustId.isEmpty())
        {
            JamboxLog.Info("Adjust ID is empty");
            return;
        }

        JamboxLog.Info("Initializing Adjust with appToken : " + JamboxData.adjustId);
        AdjustConfig config = new AdjustConfig(context, JamboxData.adjustId, AdjustConfig.ENVIRONMENT_PRODUCTION);
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
