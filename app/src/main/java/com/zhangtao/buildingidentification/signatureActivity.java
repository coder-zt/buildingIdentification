package com.zhangtao.buildingidentification;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.zhangtao.buildingidentification.Utils.TableBrowserApi;
import com.zhangtao.buildingidentification.Views.myCanvas;

public class signatureActivity extends AppCompatActivity {

    public static final String TAG = "signatureActivity";
    private WebView mSignWebView;
    private TableBrowserApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        myCanvas canvas = findViewById(R.id.signature_area);
        canvas.setPenColor(Color.BLACK);
        canvas.setBackColor(Color.WHITE);
        Button okBtn = findViewById(R.id.btn_ok);
        Button cannelBtn = findViewById(R.id.back_btn);
        ImageView imageView = findViewById(R.id.res);
        imageView.setImageResource(R.mipmap.unedit);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Bitmap bitmap = canvas.getBitMap();
                Log.d(TAG, "onClick: " + bitmap.getHeight() + "===" + bitmap.getWidth());
//                bitmap.setPixel(100, 270, Color.BLACK);


                imageView.setImageBitmap(bitmap);
            }
        });
        cannelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.clear();
            }
        });
//        WebSettings webSettings = mSignWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        //设置true,才能让Webivew支持<meta>标签的viewport属性
//        webSettings.setUseWideViewPort(true);
//        //设置可以支持缩放
//        webSettings.setSupportZoom(true);
//        //设置出现缩放工具
//        webSettings.setBuiltInZoomControls(true);
//        //设定缩放控件隐藏
//        webSettings.setDisplayZoomControls(false);
//
//        mApi = new TableBrowserApi();
//        mSignWebView.addJavascriptInterface(mApi, "Android");
//        mSignWebView.loadUrl("file:///android_asset/signatureTest.html");

    }
}
