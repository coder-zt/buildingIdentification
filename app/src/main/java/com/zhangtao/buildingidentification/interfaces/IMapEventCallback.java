package com.zhangtao.buildingidentification.interfaces;

import android.view.MotionEvent;
import android.view.View;

public interface IMapEventCallback {

    /**
     * 地图被点击时的回调
     * @param point
     */
    void onTapSingleTaped(MotionEvent point, boolean isMove);

    /**
     * 地图滑动的额回调
     * @param from
     * @param to
     */
    void onMapMove(MotionEvent from, MotionEvent to, boolean isMove);

    /**
     * 是否处于编辑状态
     * @param isMove
     */
    //void isEdit(boolean isMove, View v);
}
