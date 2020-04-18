package com.zhangtao.buildingidentification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangtao.buildingidentification.Utils.DBDao;
import com.zhangtao.buildingidentification.Utils.DataObject;
import com.zhangtao.buildingidentification.Utils.ImageManage;
import com.zhangtao.buildingidentification.Utils.TableBrowserApi;
import com.zhangtao.buildingidentification.Utils.infoObject;
import com.zhangtao.buildingidentification.Views.TableBar;
import com.zhangtao.buildingidentification.WebViewUtils.WebClient;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.zhangtao.buildingidentification.Utils.Constant.INTENT_TO_SURVEY;
import static com.zhangtao.buildingidentification.Utils.ImageManage.getBitmap;

public class surveyActivity extends AppCompatActivity implements TableBar.onItemClick, View.OnClickListener, TableBrowserApi.toSignature, WebClient.onPageLoaded {

    public static final int TOLFET = 0;
    public static final int TORIGHT = 1;
    public static final int gestureLength = 300;
    private String[] mDataList;
    private final static String TAG = "surveyActivity";
    private WebView mTableView;
    float x1, x2, y2, y1;
    private TableBar mTableBar;
    private TableBrowserApi mApi;
    private String signatureId;
    private Map<String, Bitmap> mSignatureCache =  new HashMap<>();;
    private ImageView mBackBtn;
    private ImageView mCommitBtn;
    private View mPopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        initView();
        int id =  getIntent().getIntExtra(INTENT_TO_SURVEY, -1);
        if(id != -1){
            DBDao dao = new DBDao(this);
            DataObject dataObject = dao.queryForId(id);
            setBitmapCache(dataObject.getBitmapJson());
            mApi.setJsonAll(dataObject.getInfoJson());
            mApi.setBrowse(true);
        }


    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        mBackBtn = findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(this);
        mCommitBtn = findViewById(R.id.menu_btn);
        mCommitBtn.setOnClickListener(this);
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
        mApi = new TableBrowserApi(this,this);
        mTableView.addJavascriptInterface(mApi, "Android");
        mTableView.setWebViewClient(new WebClient(mApi, this));
        //导航栏
        mTableBar = findViewById(R.id.table_bar);
        mTableBar.initView(mData);
        mTableBar.setOnItemClick(this);
    }

    private void onScroll(int dir){
        if(dir == TOLFET && mTableBar.getPosition() > 0){
            mTableBar.setPosition(mTableBar.getPosition() - 1);
        }else if(dir == TORIGHT  && mTableBar.getPosition() < 5){
            mTableBar.setPosition(mTableBar.getPosition() + 1);
        }else if( mTableBar.getPosition() == 5|| mTableBar.getPosition() == 0){
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

                break;
            case 4:
                mTableView.loadUrl("file:///android_asset/signature.html");
                break;
            case 5:
                mTableView.loadUrl("file:///android_asset/check.html");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_btn:
                backToMain();
                break;
            case R.id.menu_btn:
                menuClicked();
                break;
        }
    }

    //菜单的点击事件
    private void menuClicked() {
        //记录当前页面的数据
        mTableView.loadUrl("javascript:toJson()");
        PopupWindow menuPopWindow = new PopupWindow(this);
        mPopView = LayoutInflater.from(this).inflate(R.layout.menu_pop, null);
        //这里要注意：设置setOutsideTouchable之前，先要设置：setBackgroundDrawable,
//        否则点击外部无法关闭pop.
        menuPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        menuPopWindow.setOutsideTouchable(true);
        menuPopWindow.setFocusable(true);
        //设置内容
        menuPopWindow.setContentView(mPopView);
        TextView commitBtn = mPopView.findViewById(R.id.commit_btn);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               saveAllData();
                menuPopWindow.dismiss();
            }
        });
        Log.d(TAG, "menuClicked: ");
        menuPopWindow.showAsDropDown(mCommitBtn, UIUtil.dip2px(this, -100), 0);
//                Log.d(TAG, "backToMain: " +  mApi.getJsonAll());


    }

    /**
     * 提取网页中的数据存储到数据库
     */
    private void saveAllData() {
        String sbBitmap = getBitmapStr();
        infoObject info = mApi.getMainInfo();
        String jsonAll = mApi.getJsonAll();
        if (jsonAll != null) {
            //保存数据到数据库 info jsonall bitmapStr
            DBDao dao = new DBDao(this);
            DataObject dataObject = new DataObject(info, jsonAll, sbBitmap);
            if (dao.insert(dataObject)) {
                Toast.makeText(this, "数据录入成功！",Toast.LENGTH_SHORT).show();
            }else{
//                Toast.makeText(this, "数据录入失败！请仔细检查",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 将缓存的bitmap转为字符串
     * @return
     */
    private String getBitmapStr() {
        Set<String> keys = mSignatureCache.keySet();
        StringBuilder sbBitmap = new StringBuilder("{\"bitmap\":{");
        int count = 0;
        for (String key : keys) {
            count++;
            Bitmap bitmap = mSignatureCache.get(key);
            String bitmapStr = getBitmap(bitmap);
            sbBitmap.append("\"" + key + "\":" + "\"" + bitmapStr + "\"");
            if(count<keys.size()){
                sbBitmap.append(",");
            }
        }
        sbBitmap.append("}}");
        return sbBitmap.toString();
    }

    private void setBitmapCache(String bitmapStrs){
        if (bitmapStrs != null && bitmapStrs.startsWith("\ufeff")) {
            bitmapStrs = bitmapStrs.substring(1);
        }
        try {
            JSONObject jsonsInfo = new JSONObject(bitmapStrs);
            JSONObject jsonsInfo1  = (JSONObject) jsonsInfo.get("bitmap");
            Iterator<String> keys = jsonsInfo1.keys();
            while(keys.hasNext()){
                String key = keys.next();
                String bitmapStr = (String)jsonsInfo1.get(key);
                mSignatureCache.put(key, ImageManage.base64ToBitmap(bitmapStr));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    //返回页的点击事件
    private void backToMain() {
        finish();
    }

    /**
     * 网页中的签名点击回调函数
     */
    @Override
    public void onClick(String id) {
        Intent intent = new Intent(surveyActivity.this, signatureActivity.class);
        startActivityForResult(intent, 1);
        signatureId = id;
    }


    /**
     * 签字成功后的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 2){
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("signature");
                if (bitmap != null) {
                    mSignatureCache.put(signatureId, bitmap);
                    bitmap = mSignatureCache.get(signatureId);
                    if (bitmap != null) {
                        String bitmapStr = "data:image/png;base64," + getBitmap(bitmap);
                        mTableView.loadUrl("javascript:setSignature('" + signatureId + "','" + bitmapStr + "')");
                        Log.d(TAG, "onActivityResult: " + mSignatureCache.size());
                    }
                }
            }
        }
    }


    /**
     * 网页加载完成的回调
     */
    @Override
    public void loaded() {
        if (mTableBar.getPosition() == 4 || mTableBar.getPosition() == 5) {
            Set<String> keySet = mSignatureCache.keySet();
//            Log.d(TAG, "loaded: 缓存图片的数量=" + keySet.size());
            for (String key : keySet) {
                Bitmap bitmap = mSignatureCache.get(key);
                if (bitmap != null) {
//                    Log.d(TAG, "loaded: 加载缓存图片");
                    String bitmapStr = "data:image/png;base64," + getBitmap(bitmap);
                    mTableView.loadUrl("javascript:setSignature('" + key + "','" + bitmapStr + "')");
                }
            }

        }
    }
}
