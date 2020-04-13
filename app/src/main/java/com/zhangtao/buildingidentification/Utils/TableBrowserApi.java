package com.zhangtao.buildingidentification.Utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
        result = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAA6cAAAH0CAYAAADBmVsAAAAgAElEQVR4Xu3dB7RtS13n+28DgiASJeckQTISJCOg0ER5tgQjipi1VRqfSguYGzGiYEARA8YnIEEQEImS8QISFCSDBEkCkvuNsuexD5cb9jl77bXmXPOzxtjj3MtZq+a/PjXvZv921az6L3kRIECAAAECBAgQIECAAIEdC/yXHV/f5QkQIECAAAECBAgQIECAQMKpm4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4gnO58CBRAgAABAgQIECBAgAABAsKpe4AAAQIECBAgQIAAAQIEdi4g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";
        byte[] decode = Base64.decode(result, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        Log.d(TAG, "toJson: " + bitmap.getWidth() + "=====" + bitmap.getHeight());
//        return bitmap;

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
