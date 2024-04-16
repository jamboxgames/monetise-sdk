package com.jambox.webview;

import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;

import java.util.concurrent.TimeUnit;

public class JamboxAdsHelper
{

    public static boolean IsInitialized;
    private static Context context;

    public static void InitializeAds(Context context, String interstitialId, String rewardedId, String bannerId)
    {
        if (IsInitialized)
            return;

        IsInitialized = true;
        JamboxAdsHelper.context = context;

        // Make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance(context).setMediationProvider("max");
        AppLovinSdk.getInstance(context).initializeSdk(new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration appLovinSdkConfiguration) {
                //SDK Initialized
                System.out.println("Applovin SDK Initialized");
                InitializeInterstitial(context, interstitialId);
                InitializeRewarded(context, rewardedId);
                JamboxAdsHelper.bannerId = bannerId;
            }
        });
    }

    private static MaxInterstitialAd interstitialAd;
    private static OnInterstitialAdListener interstitialAdListener;
    private static int interstitialRetryAttempt = 0;
    private static void InitializeInterstitial(Context context, String interstitialId)
    {
        interstitialAd = new MaxInterstitialAd( interstitialId, (Activity) context );
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
            public void onAdDisplayed(@NonNull MaxAd maxAd)
            {
                if (interstitialAdListener != null)
                {
                    interstitialAdListener.OnAdDisplayed();
                }
            }

            @Override
            public void onAdHidden(@NonNull MaxAd maxAd)
            {
                // Interstitial ad is hidden. Pre-load the next ad
                interstitialAd.loadAd();
                if (interstitialAdListener != null)
                {
                    interstitialAdListener.OnAdHidden();
                }
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
                if (interstitialAdListener != null)
                {
                    interstitialAdListener.OnAdDisplayFailed();
                }
            }
        });
        interstitialAd.loadAd();
    }

    public static void ShowInterstitial(OnInterstitialAdListener _interstitialAdListener)
    {
        interstitialAdListener = null;
        if (interstitialAd.isReady())
        {
            interstitialAdListener = _interstitialAdListener;
            interstitialAd.showAd();
        }
    }

    private static MaxRewardedAd rewardedAd;
    private static OnRewardedAdListener rewardedAdListener;
    private static int rewardedRetryAttempt = 0;
    private static void InitializeRewarded(Context context, String rewardedId)
    {
        rewardedAd = MaxRewardedAd.getInstance( rewardedId, (Activity) context );
        rewardedAd.setListener(new MaxRewardedAdListener()
        {
            @Override
            public void onUserRewarded(@NonNull MaxAd maxAd, @NonNull MaxReward maxReward)
            {
                // Rewarded ad was displayed and user should receive the reward
                if (rewardedAdListener != null)
                {
                    rewardedAdListener.OnAdCompleted();
                }
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
                if (rewardedAdListener != null)
                {
                    rewardedAdListener.OnAdHidden();
                }
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
                if (rewardedAdListener != null)
                {
                    rewardedAdListener.OnAdDisplayFailed();
                }
            }
        });
        rewardedAd.loadAd();
    }

    public static void ShowRewarded(OnRewardedAdListener _rewardedAdListener)
    {
        rewardedAdListener = null;
        if (rewardedAd.isReady())
        {
            rewardedAdListener = _rewardedAdListener;
            rewardedAd.showAd();
        }
    }

    private static String bannerId;
    private static MaxAdView bannerAdView;
    public static void ShowBannerAd(BannerPosition position)
    {
        if (bannerAdView != null && bannerAdView.isShown())
            return;

        bannerAdView = new MaxAdView(bannerId, context);
        bannerAdView.setListener(new MaxAdViewAdListener()
        {
            @Override
            public void onAdExpanded(@NonNull MaxAd maxAd) { }
            @Override
            public void onAdCollapsed(@NonNull MaxAd maxAd) { }
            @Override
            public void onAdLoaded(@NonNull MaxAd maxAd) { }
            @Override
            public void onAdDisplayed(@NonNull MaxAd maxAd) { }
            @Override
            public void onAdHidden(@NonNull MaxAd maxAd) { }
            @Override
            public void onAdClicked(@NonNull MaxAd maxAd) { }
            @Override
            public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) { }
            @Override
            public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) { }
        });

        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;

        // Get the adaptive banner height.
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( (Activity) context ).getHeight();
        int heightPx = AppLovinSdkUtils.dpToPx( context, heightDp );

        if (position == BannerPosition.TOP)
        {
            bannerAdView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx, Gravity.TOP) );
        }
        else
        {
            bannerAdView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx, Gravity.BOTTOM) );
        }
        bannerAdView.setExtraParameter( "adaptive_banner", "true" );
        bannerAdView.setLocalExtraParameter( "adaptive_banner_width", 400 );
        bannerAdView.getAdFormat().getAdaptiveSize( 400, context ).getHeight(); // Set your ad height to this value

        // Set background or background color for banners to be fully functional
        bannerAdView.setBackgroundColor(0);

        ViewGroup rootView = ((Activity) context).findViewById( android.R.id.content );
        rootView.addView( bannerAdView );

        // Load the ad
        bannerAdView.loadAd();

    }

    public static void HideBannerAd()
    {
        if (bannerAdView == null)
            return;

        bannerAdView.destroy();
        bannerAdView = null;
    }

    public static void MoveBannerToFront()
    {
        if (bannerAdView != null)
        {
            bannerAdView.bringToFront();
        }
    }

    public static class AdsCode
    {
        public static final int BEFORE_ADS_SHOWN = 100;
        public static final int ADS_DISMISSED = 110;
        public static final int ADS_WATCH_SUCCESS = 200;
        public static final int ADS_VIEWED_OR_DISMISSED = 201;
        public static final int ADS_NOT_SHOWN = 400;
    }

    public enum BannerPosition
    {
        TOP,
        BOTTOM
    }

}
