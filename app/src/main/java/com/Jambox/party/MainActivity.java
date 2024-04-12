package com.Jambox.party;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jambox.webview.JamboxAdsHelper;
import com.jambox.webview.WebviewObject;

public class MainActivity extends AppCompatActivity {

    private WebviewObject webview;

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

        //Ad initialization
        JamboxAdsHelper.InitializeAds(this, "0ee55073fd46cb13", "7d64a59befe5cef9");

        //Start Webview games
        webview = new WebviewObject(this);
        webview.StartWebview();
    }
}