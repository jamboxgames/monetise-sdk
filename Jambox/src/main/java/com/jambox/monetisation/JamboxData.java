package com.jambox.monetisation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class JamboxData
{

    static boolean IsDataFetchCompleted = false;
    static String interstitialId;
    static String rewardedId;
    static String bannerId;
    static String nativeId;
    static String appOpenId;
    static String h5ClientId;
    static String adjustId;

    private static OnDataFetchListener listener;
    private static Context context;

    public static boolean Fetch(Context context, OnDataFetchListener listener)
    {
        JamboxData.context = context;
        JamboxData.listener = listener;

        SharedPreferences sharedPreferences = context.getSharedPreferences("jambox_pref", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("interstitial_id"))
        {
            JamboxLog.Info("Fetching data from saved");
            GetDataFromPref(sharedPreferences);
            DataFetchCompleted();
            return true;
        }
        else
        {
            //fetch data from server
            JamboxLog.Info("Fetching data from server");
            new FetchDataFromUrl().execute("https://aliendroid.jambox.games/api/v1/appconfig/findappbundle?bundle_id=" + context.getPackageName());
            return false;
        }
    }

    static void DataFetchCompleted()
    {
        IsDataFetchCompleted = true;
        if (listener != null)
            listener.OnDataFetched();
    }

    static void SavePref()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("jambox_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("interstitial_id", interstitialId);
        editor.putString("rewarded_id", rewardedId);
        editor.putString("banner_id", bannerId);
        editor.putString("native_id", nativeId);
        editor.putString("appopen_id", appOpenId);
        editor.putString("h5client_id", h5ClientId);
        editor.putString("adjustid", adjustId);
        editor.apply();
    }

    static void GetDataFromPref(SharedPreferences sharedPreferences)
    {
        interstitialId = sharedPreferences.getString("interstitial_id", "");
        rewardedId = sharedPreferences.getString("rewarded_id", "");
        bannerId = sharedPreferences.getString("banner_id", "");
        nativeId = sharedPreferences.getString("native_id", "");
        appOpenId = sharedPreferences.getString("appopen_id", "");
        h5ClientId = sharedPreferences.getString("h5client_id", "");
        adjustId = sharedPreferences.getString("adjustid", "");
    }

}

class FetchDataFromUrl extends AsyncTask<String, Void, String>
{

    @Override
    protected String doInBackground(String... strings)
    {
        try
        {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String data = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null)
                data = data + line;
            return data;
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(String dataString)
    {
        JamboxLog.Info("Data from server : " + dataString);

        Gson gson = new Gson();
        ResponseData responseData = new ResponseData();
        responseData = gson.fromJson(dataString, responseData.getClass());

        if (!responseData.success)
        {
            JamboxLog.Error("Failed to fetch data from server : " + responseData.message);
            return;
        }

        JamboxLog.Info("Data fetch success");
        JamboxData.interstitialId = responseData.app_config.get("interstitial_id");
        JamboxData.rewardedId = responseData.app_config.get("rewarded_id");
        JamboxData.bannerId = responseData.app_config.get("banner_id");
        JamboxData.appOpenId = responseData.app_config.get("appopen_id");
        JamboxData.nativeId = responseData.app_config.get("native_id");
        JamboxData.adjustId = responseData.app_config.get("adjust_id");
        JamboxData.h5ClientId = responseData.app_config.get("h5_app_id");

        JamboxData.DataFetchCompleted();
        JamboxData.SavePref();
    }
}

class ResponseData
{
    public boolean success;
    public HashMap<String, String> app_config;
    public String message;
}

interface OnDataFetchListener
{
    public void OnDataFetched();
}
