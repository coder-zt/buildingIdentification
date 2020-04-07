package com.zhangtao.buildingidentification.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class Circle_ImageView extends AppCompatImageView {
        private static float roundRatio = 0.5f;
        private Path path;
        private final String TAG = "RoundRectIamgeView";

    public Circle_ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
        protected void onDraw(Canvas canvas) {
            if(path == null){
                path = new Path();
                path.addRoundRect(new RectF(0-5,0-5,getWidth(), getHeight()), roundRatio*getWidth(), roundRatio*getHeight(), Path.Direction.CW);
            }
            canvas.save();
            canvas.clipPath(path);
            super.onDraw(canvas);
            canvas.restore();
        }
    }

