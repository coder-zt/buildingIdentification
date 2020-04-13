package com.zhangtao.buildingidentification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhangtao.buildingidentification.Utils.TableBrowserApi;
import com.zhangtao.buildingidentification.Views.TableBar;
import com.zhangtao.buildingidentification.WebViewUtils.WebClient;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class surveyActivity extends AppCompatActivity implements TableBar.onItemClick, View.OnClickListener {

    public static final int TOLFET = 0;
    public static final int TORIGHT = 1;
    public static final int gestureLength = 300;
    private ViewPager mViewPager;
    private String[] mDataList;
    private final static String TAG = "surveyActivity";
    private WebView mTableView;
    private MagicIndicator mMagicIndicator;
    private CommonNavigator mCommonNavigator;
    float x1, x2, y2, y1;
    private TableBar mTableBar;
    private TableBrowserApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        initView();
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
//        mViewPager = findViewById(R.id.view_pager);
        String[] mData = getApplicationContext().getResources().getStringArray(R.array.survey_tab_name);
        mDataList = this.getApplicationContext().getResources().getStringArray(R.array.survey_tab_name);//survey_tab_name
        mTableView = findViewById(R.id.table_view);

        WebSettings webSettings = mTableView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //设置true,才能让Webivew支持<meta>标签的viewport属性
        webSettings.setUseWideViewPort(true);
        //设置可以支持缩放
        webSettings.setSupportZoom(true);
        //设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //设定缩放控件隐藏
        webSettings.setDisplayZoomControls(false);
        mTableView.setInitialScale(100);
        mTableView.loadUrl("file:///android_asset/index.html");
        mTableView.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //继承了Activity的onTouchEvent方法，直接监听点击事件
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = event.getX();
                    y2 = event.getY();
                     if(x1 - x2 > gestureLength) {
                         onScroll(TORIGHT);
                    } else if(x2 - x1 > gestureLength) {
                        onScroll(TOLFET);
                    }
                }
                return surveyActivity.super.onTouchEvent(event);
            }
        });
        mApi = new TableBrowserApi();
        mTableView.addJavascriptInterface(mApi, "Android");
        mTableView.setWebViewClient(new WebClient(mApi));
        //导航栏
        mTableBar = findViewById(R.id.table_bar);
        mTableBar.initView(mData);
        mTableBar.setOnItemClick(this);
    }

    private void onScroll(int dir) {
        if(dir == TOLFET && mTableBar.getPosition() > 0){
            mTableBar.setPosition(mTableBar.getPosition() - 1);

        }else if(dir == TORIGHT  && mTableBar.getPosition() < 4){
            mTableBar.setPosition(mTableBar.getPosition() + 1);
        }else if( mTableBar.getPosition() == 4 || mTableBar.getPosition() == 0){
            Toast.makeText(this, "别滑了！到边儿了！！！",Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void itemClick(int position) {
        mTableView.loadUrl("javascript:toJson()");
        switch (position){
            case 0:
                mTableView.loadUrl("file:///android_asset/index.html");

                break;
            case 1:
                mTableView.loadUrl("file:///android_asset/base.html");
                break;
            case 2:
                mTableView.loadUrl("file:///android_asset/boundary.html");
                break;
            case 3:
                mTableView.loadUrl("file:///android_asset/signatureTest.html");
                Intent intent = new Intent(surveyActivity.this, signatureActivity.class);
                startActivity(intent);
                break;
            case 4:
                mTableView.loadUrl("file:///android_asset/signature.html");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_btn:
                backToMain();
                break;
        }
    }

    private void backToMain() {
        finish();
    }
}
