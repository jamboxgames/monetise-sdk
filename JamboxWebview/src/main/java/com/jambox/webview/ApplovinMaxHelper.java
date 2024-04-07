package com.jambox.webview;

import android.app.Activity;

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

public class ApplovinMaxHelper implements MaxAdListener, MaxRewardedAdListener {

    private Activity activity;
    private MaxInterstitialAd interstitialAd;
    private MaxRewardedAd rewardedAd;

    public  ApplovinMaxHelper(Activity activity) {
        this.activity = activity;
        InitializeAds();
    }

    void InitializeAds() {
        // Make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance(activity.getApplicationContext()).setMediationProvider("max");
        AppLovinSdk.getInstance(activity).initializeSdk(new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration appLovinSdkConfiguration) {
                //SDK Initialized
                System.out.println("Applovin SDK Initialized");
                createRewardedAd();
                createInterstitialAd();
            }
        });
    }

    public void ShowIS() {
        if (interstitialAd.isReady()){
            interstitialAd.showAd();
        }
    }

    public void ShowRW() {
        if (rewardedAd.isReady()){
            rewardedAd.showAd();
        }
    }

    void createInterstitialAd() {
        interstitialAd = new MaxInterstitialAd( "0ee55073fd46cb13", activity );
        interstitialAd.setListener(this);

        // Load the first ad
        interstitialAd.loadAd();
    }

    @Override
    public void onAdLoaded(@NonNull MaxAd maxAd) {

    }

    @Override
    public void onAdDisplayed(@NonNull MaxAd maxAd) {
        if(!rewardedAd.isReady()){
            createRewardedAd();
        }
        if(!interstitialAd.isReady()){
            createInterstitialAd();
        }
    }

    @Override
    public void onAdHidden(@NonNull MaxAd maxAd) {

    }

    @Override
    public void onAdClicked(@NonNull MaxAd maxAd) {

    }

    @Override
    public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {

    }

    @Override
    public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {

    }

    void createRewardedAd()
    {
        rewardedAd = MaxRewardedAd.getInstance( "7d64a59befe5cef9", activity );
        rewardedAd.setListener( this );

        rewardedAd.loadAd();
    }

    @Override
    public void onUserRewarded(@NonNull MaxAd maxAd, @NonNull MaxReward maxReward) {

    }

}
