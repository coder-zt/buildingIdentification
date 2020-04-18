package com.zhangtao.buildingidentification.Utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * 图片处理类
 */
public class ImageManage {

    /**
     * 将bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String getBitmap(Bitmap bitmap){
        //先将bitmap转为byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] bytes = baos.toByteArray();
        //将byte[]转为base64
        String myBase64 = Base64.encodeToString(bytes,Base64.DEFAULT);
        return myBase64;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
