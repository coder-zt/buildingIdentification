package com.zhangtao.buildingidentification.Fragments;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zhangtao.buildingidentification.R;

import java.io.File;

public class IndexFragment extends com.zhangtao.buildingidentification.Base.BaseFragment {

    private View mView;
    private final String TAG = "IndexFragment";

    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.frist_fargment_layout, null);
        initView();
        return mView;
    }

    private void initView() {
        WebView content = mView.findViewById(R.id.content_wv);
        content.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = content.getSettings();
        //设置true,才能让Webivew支持<meta>标签的viewport属性
        webSettings.setUseWideViewPort(true);
        //设置可以支持缩放
        webSettings.setSupportZoom(true);
        //设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //设定缩放控件隐藏
        webSettings.setDisplayZoomControls(false);
        content.setInitialScale(100);
        content.loadUrl("file:///android_asset/base.html");
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/tables/test.html");
        if(file.exists()){
            Log.d(TAG, "initView: 存在" + content.getUrl());
        }else{
            Log.d(TAG, "initView:不存在 " + content.getUrl());
        }
    }


}
