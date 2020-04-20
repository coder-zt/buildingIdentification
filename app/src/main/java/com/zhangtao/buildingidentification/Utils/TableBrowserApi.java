package com.zhangtao.buildingidentification.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhangtao.buildingidentification.elements.infoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TableBrowserApi {

    public static final String TAG = "TableBrowserApi";
    private final toSignature mListener;
    private final Context mContext;
    //填写信息时的缓存
    private Map<String, String> cacheInfo = new HashMap<>();
    private infoObject mMainInfo;

    public void setBrowse(boolean browse) {
        mIsBrowse = browse;
    }

    //如果是浏览信息则不能修改信息
    private boolean mIsBrowse = false;

    @SuppressLint("JavascriptInterface")
    public TableBrowserApi(Context context, toSignature listener){
        mListener = listener;
        mContext = context;
    }

    /**
     * 网页的回调函数，返回每页的详细数据
     * @param result
     */
    @JavascriptInterface
    public void toJson(String result){
        if(mIsBrowse)
            return;
        try {
            JSONObject jsonInfo = new JSONObject(result);
            Iterator it = jsonInfo.keys();
            while(it.hasNext()){
                String key = (String) it.next();
                if(TextUtils.equals(key, "index_page")){
                    String indexStr = jsonInfo.get(key).toString();
                    Gson gson = new Gson();
                    mMainInfo = gson.fromJson(indexStr, infoObject.class);
                }
                cacheInfo.put(key, result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取单次录入数据的单个页面的数据
     * @param key
     * @return
     */
    public String getJson(String key){
        String info = cacheInfo.get(key);
        Log.d(TAG, key + "praseJson: " + info);
        return info;
    }

    @JavascriptInterface
    public void signatureClicked(String id){
        mListener.onClick(id);
    }


    /**
     * 点击网页签名位置的回调接口
     */
    public interface toSignature{
        /**
         * 签名位置的点击事件
         * @param id 点击控件的ID
         */
        void onClick(String id);
    }

    /**
     * 获取所有数据的json
     * @return
     */
    public String getJsonAll(){
        Set<String> keySet = cacheInfo.keySet();
        if(keySet.size() != 5){
            Toast.makeText(mContext, "数据录入不足==" + keySet.size(), Toast.LENGTH_SHORT).show();
        }
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("{\"data:\":[");
        Iterator<String> keyIt = keySet.iterator();
        while(keyIt.hasNext()){
           String key = keyIt.next();
            String res = cacheInfo.get(key);
            if (res != null) {
                sbuilder.append(res);
                if( keyIt.hasNext()) {
                    sbuilder.append(",");
                }
            }
        }
        sbuilder.append("]}");
        return  new String(sbuilder);
    }

    /**
     * 设置数据
     *
     * @return
     */
    public void setJsonAll(String jsonStr){
        if (jsonStr != null && jsonStr.startsWith("\ufeff")) {
            jsonStr = jsonStr.substring(1);
        }
        try {
            JSONObject jsonsInfo = new JSONObject(jsonStr);
            Iterator it = jsonsInfo.keys();
            while(it.hasNext()){
                String key = (String) it.next();
                JSONArray jsonArray = jsonsInfo.getJSONArray(key);
                Log.d(TAG, "setJsonAll: "+jsonArray.length());
                for(int i=0;i<jsonArray.length();i++){

                    String line = jsonArray.get(i).toString();
                    JSONObject jsonLine = new JSONObject(line);
                    Iterator innerIt = jsonLine.keys();
                    while(innerIt.hasNext()){
                        String innerkey = (String) innerIt.next();
                        cacheInfo.put(innerkey, line);
                        Log.d(TAG, "setJsonAll: "+line);
                }
            }
        } }catch (JSONException e) {
            e.printStackTrace();
        }}


    /**
     * 获取该对象主要信息
     */
    public infoObject getMainInfo(){
        return mMainInfo;
    }
}
