package com.zhangtao.buildingidentification.WebViewUtils;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhangtao.buildingidentification.Utils.TableBrowserApi;

public class WebClient  extends  WebViewClient{


    public static final String TAG ="WebClient";
    private TableBrowserApi mApi;
    public WebClient(TableBrowserApi api){
        mApi = api;
    }
    @Override
    public void onPageFinished(WebView webView, String url) {
        Log.d(TAG, "onPageFinished: " + url);
        String[] splitUrl = url.split("/");
        String key = splitUrl[splitUrl.length - 1].split("\\.")[0] + "_page";
        webView.loadUrl("javascript:parseJson('" + mApi.getJson(key)+ "')");
    }
}
