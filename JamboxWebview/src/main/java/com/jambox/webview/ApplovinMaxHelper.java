package com.jambox.webview;

import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import java.util.concurrent.TimeUnit;

public class ApplovinMaxHelper {

    private static MaxInterstitialAd interstitialAd;
    private static MaxRewardedAd rewardedAd;
    public static boolean IsInitialized;

    public static void InitializeAds(Context context)
    {
        if (IsInitialized)
            return;

        IsInitialized = true;

        // Make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance(context).setMediationProvider("max");
        AppLovinSdk.getInstance(context).initializeSdk(new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration appLovinSdkConfiguration) {
                //SDK Initialized
                System.out.println("Applovin SDK Initialized");
                InitializeInterstitial(context);
                InitializeRewarded(context);
            }
        });
    }

    private static int interstitialRetryAttempt = 0;
    private static void InitializeInterstitial(Context context)
    {
        interstitialAd = new MaxInterstitialAd( "0ee55073fd46cb13", (Activity) context );
        interstitialAd.setListener(new MaxAdListener()
        {
            @Override
            public void onAdLoaded(@NonNull MaxAd maxAd)
            {
                // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'
                // Reset retry attempt
                interstitialRetryAttempt = 0;
            }

            @Override
            public void onAdDisplayed(@NonNull MaxAd maxAd) { }

            @Override
            public void onAdHidden(@NonNull MaxAd maxAd)
            {
                // Interstitial ad is hidden. Pre-load the next ad
                interstitialAd.loadAd();
            }

            @Override
            public void onAdClicked(@NonNull MaxAd maxAd) { }

            @Override
            public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError)
            {
                // Interstitial ad failed to load
                // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)
                interstitialRetryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, interstitialRetryAttempt ) ) );
                new Handler().postDelayed( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        interstitialAd.loadAd();
                    }
                }, delayMillis );
            }

            @Override
            public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError)
            {
                // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
                interstitialAd.loadAd();
            }
        });
        interstitialAd.loadAd();
    }

    public static void ShowInterstitial()
    {
        if (interstitialAd.isReady()){
            interstitialAd.showAd();
        }
    }

    private static int rewardedRetryAttempt = 0;
    public static void InitializeRewarded(Context context)
    {
        rewardedAd = MaxRewardedAd.getInstance( "7d64a59befe5cef9", (Activity) context );
        rewardedAd.setListener(new MaxRewardedAdListener()
        {
            @Override
            public void onUserRewarded(@NonNull MaxAd maxAd, @NonNull MaxReward maxReward)
            {
                // Rewarded ad was displayed and user should receive the reward
            }

            @Override
            public void onAdLoaded(@NonNull MaxAd maxAd)
            {
                // Rewarded ad is ready to be shown. rewardedAd.isReady() will now return 'true'
                // Reset retry attempt
                rewardedRetryAttempt = 0;
            }

            @Override
            public void onAdDisplayed(@NonNull MaxAd maxAd) { }

            @Override
            public void onAdHidden(@NonNull MaxAd maxAd)
            {
                // rewarded ad is hidden. Pre-load the next ad
                rewardedAd.loadAd();
            }

            @Override
            public void onAdClicked(@NonNull MaxAd maxAd) { }

            @Override
            public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError)
            {
                // Rewarded ad failed to load
                // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                rewardedRetryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, rewardedRetryAttempt ) ) );
                new Handler().postDelayed( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        rewardedAd.loadAd();
                    }
                }, delayMillis );
            }

            @Override
            public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError)
            {
                // Rewarded ad failed to display. AppLovin recommends that you load the next ad.
                rewardedAd.loadAd();
            }
        });
        rewardedAd.loadAd();
    }

    public static void ShowRewarded()
    {
        if (rewardedAd.isReady()){
            rewardedAd.showAd();
        }
    }

}
