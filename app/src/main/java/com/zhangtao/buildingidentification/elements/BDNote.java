package com.zhangtao.buildingidentification.elements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.TextSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_KEY;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_NOTE;

public class BDNote extends BDElement {

    public static final String TAG = "BDNote";
    //位置
    Point mPoint;
    //内容
    String mContent;
    //方向
    int mAngle;
    //大小
    int mSize = 200;
    //文字样式
    TextSymbol mTextSymbol;
    //其他元素的附属
    BDElement mParent;
    private double mWidth;

    /**
     * 创建注解
     * @param content
     */
    public BDNote(String content, BDElement parent, Point point){
        this.mContent = content;
        this.mParent = parent;
        this.mPoint = point;
    }


    @Override
    public void elementSelect(int index) {

    }

    public void addPoint(Point point) {

    }

    @Override
    public List<Graphic> getGraphic() {
        List<Graphic> Graphics = new ArrayList<>();
        Map<String, Object> attribute = new HashMap<>();
        attribute.put(TYPE_KEY, TYPE_NOTE);
        double index = 1;
        double middle = (mContent.length() + 1)/2.0;
        for (Drawable drawable : createMapBitMap(mContent, mSize,-1*mAngle, Color.WHITE)) {
            PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(drawable);
            Graphics.add( new Graphic(new Point(mPoint.getX() + mWidth * Math.cos(mAngle/180.0 *Math.PI ) * (index-middle), mPoint.getY() + mWidth*Math.sin(mAngle/180.0 *Math.PI)*(index-middle)), markerSymbol, attribute));
            index++;
        }

        return Graphics;
    }

    public Point getPoint() {
        return mPoint;
    }

    public void setPoint(Point point) {
        mPoint = point;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }


    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public TextSymbol getTextSymbol() {
        return mTextSymbol;
    }

    public void setTextSymbol(TextSymbol textSymbol) {
        mTextSymbol = textSymbol;
    }

    public BDElement getParent() {
        return mParent;
    }

    public void setParent(BDElement parent) {
        mParent = parent;
    }

    /**
     * 文字转换BitMap列表集合
     * @param text
     * @return
     */
    public static List<Drawable> createMapBitMap(String text, int size, int angle, int color) {
        List<Drawable> noteList = new ArrayList<>();
        for (char item : text.toCharArray()) {
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setTextSize(size);
            paint.setAntiAlias(true);
            paint.setTextAlign(Paint.Align.CENTER);
            float textLength = paint.measureText(text);
            int padding = 15;
            int width = size + padding;
            int height = size + padding;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(bitmap);
            cv.drawColor(Color.parseColor("#00000000"));
            cv.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                    | Paint.FILTER_BITMAP_FLAG));
            cv.rotate(angle, width/2, height/2);
            cv.drawText(String.valueOf(item), width/2, height-padding, paint);
            cv.save();// 保存
            cv.restore();// 存储
            noteList.add(new BitmapDrawable(bitmap));
        }
        return noteList;

    }
}
