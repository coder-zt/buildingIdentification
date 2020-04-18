package com.zhangtao.buildingidentification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.zhangtao.buildingidentification.Utils.TableBrowserApi;
import com.zhangtao.buildingidentification.Views.myCanvas;

import java.io.ByteArrayOutputStream;

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
        canvas.setPaintWidth(20);
        Button okBtn = findViewById(R.id.btn_ok);
        Button cannelBtn = findViewById(R.id.back_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Bitmap bitmap = canvas.getBitMap();
                Log.d(TAG, "onClick: " + bitmap.getHeight() + "===" + bitmap.getWidth());
                Intent intent = new Intent();
                bitmap = scaleBitmap(bitmap, 160, 80);
                intent.putExtra("signature", bitmap);
                setResult(2, intent);
                finish();
            }
        });
        cannelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.clear();
            }
        });
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

}
