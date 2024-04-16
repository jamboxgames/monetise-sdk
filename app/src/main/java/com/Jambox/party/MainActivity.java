package com.Jambox.party;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jambox.webview.JamboxAdsHelper;
import com.jambox.webview.OnRewardedAdListener;
import com.jambox.webview.WebviewObject;

public class MainActivity extends AppCompatActivity {

    private WebviewObject webview = null;
    private Context context;
    private String interstitialId = "0ee55073fd46cb13";
    private String rewardedId = "7d64a59befe5cef9";
    private String bannerId = "ba924c1fc44d29ac";
    private String h5ClientId = "9285717016";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = this;

        //Ad initialization
        JamboxAdsHelper.InitializeAds(this, interstitialId, rewardedId, bannerId);

        SetButtonListeners();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                CloseWebview();
            }
        });
    }

    void SetButtonListeners()
    {
        Button startBtn = findViewById(R.id.btn_start);
        startBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                StartWebview(false);
            }
        });

        Button start_full = findViewById(R.id.start_full);
        start_full.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                StartWebview(true);
            }
        });

        Button btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CloseWebview();
            }
        });

        Button rw_btn = findViewById(R.id.rw_btn);
        rw_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ShowRW();
            }
        });

        Button is_btn = findViewById(R.id.is_btn);
        is_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ShowIS();
            }
        });

        Button banner_show = findViewById(R.id.banner_show);
        banner_show.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ShowBanner();
            }
        });

        Button banner_hide = findViewById(R.id.banner_hide);
        banner_hide.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HideBanner();
            }
        });
    }

    void StartWebview(boolean fullscreen)
    {
        if (webview != null)
            return;

        //H5 Games
        if (fullscreen)
        {
            webview = new WebviewObject(this, h5ClientId);
        }
        else
        {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 300, 0, 0);
            webview = new WebviewObject(this, h5ClientId, layoutParams);
        }
        webview.StartWebview();
    }

    void CloseWebview()
    {
        if (webview == null)
            return;

        webview.CloseWebview();
        webview = null;
    }

    void ShowBanner()
    {
        JamboxAdsHelper.ShowBannerAd(JamboxAdsHelper.BannerPosition.BOTTOM);
    }

    void HideBanner()
    {
        JamboxAdsHelper.HideBannerAd();
    }

    void ShowRW()
    {
        JamboxAdsHelper.ShowRewarded(new OnRewardedAdListener()
        {
            @Override
            public void OnAdDisplayFailed() { }
            @Override
            public void OnAdDisplayed() { }
            @Override
            public void OnAdCompleted()
            {
                System.out.println("User Rewarded");
            }
            @Override
            public void OnAdHidden() { }
        });
    }

    void ShowIS()
    {
        JamboxAdsHelper.ShowRewarded(null);
    }

}