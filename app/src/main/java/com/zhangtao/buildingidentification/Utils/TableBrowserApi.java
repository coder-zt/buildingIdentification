package com.zhangtao.buildingidentification.Utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TableBrowserApi {

    public static final String TAG = "TableBrowserApi";
    //填写信息时的缓存
    private Map<String, String> cacheInfo = new HashMap<>();
    @SuppressLint("JavascriptInterface")
    public TableBrowserApi(){}

    @JavascriptInterface
    public void toJson(String result){
        Log.d(TAG, "toJson: " + result);
        try {
            JSONObject jsonInfo = new JSONObject(result);
            Iterator it = jsonInfo.keys();
            while(it.hasNext()){
                String key = (String) it.next();
//                Log.d(TAG, "toJson: " + key);
                cacheInfo.put(key, result);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getJson(String key){
        String info = cacheInfo.get(key);
        Log.d(TAG, key + "praseJson: " + info);
        return info;
    }
}
