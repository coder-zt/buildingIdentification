package com.zhangtao.buildingidentification.Listeners;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.zhangtao.buildingidentification.interfaces.IMapEvent;
import com.zhangtao.buildingidentification.interfaces.IMapEventCallback;

public class MyTouchListener extends MapOnTouchListener implements IMapEvent {

    //事件回调
    private IMapEventCallback mCallback;
    //地图是否滑动
    //在编辑数据时地图不动
    //反之可以滑动
    private boolean mIsMove = true;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public boolean onPinchPointersDown(MotionEvent event) {
        return super.onPinchPointersDown(event);
    }

    @Override
    public boolean onPinchPointersMove(MotionEvent event) {
        return super.onPinchPointersMove(event);
    }

    @Override
    public boolean onPinchPointersUp(MotionEvent event) {
        return super.onPinchPointersUp(event);
    }

    @Override
    public boolean onDoubleTap(MotionEvent point) {
        return super.onDoubleTap(point);
    }

    @Override
    public void onMultiPointersSingleTap(MotionEvent event) {
        super.onMultiPointersSingleTap(event);
    }

    @Override
    public void onLongPress(MotionEvent point) {
        super.onLongPress(point);
    }

    @Override
    public boolean onLongPressUp(MotionEvent point) {
        return super.onLongPressUp(point);
    }

    @Override
    public boolean onSingleTap(MotionEvent point) {
        if (mCallback != null) {
            mCallback.onTapSingleTaped(point, mIsMove);
        }
        return super.onSingleTap(point);
    }

    @Override
    public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
        return super.onFling(from, to, velocityX, velocityY);
    }

    @Override
    public boolean onDoubleTapDrag(MotionEvent from, MotionEvent to) {
        return super.onDoubleTapDrag(from, to);
    }

    @Override
    public boolean onDoubleTapDragUp(MotionEvent up) {
        return super.onDoubleTapDragUp(up);
    }

    private String TAG = "MyTouchListener";

    public MyTouchListener(Context context, MapView view, IMapEventCallback callback) {
        super(context, view);
        mCallback = callback;
    }

    @Override
    public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {

        if (mCallback != null) {
            mCallback.onMapMove(from, to, mIsMove);
        }
        if(mIsMove){
            return  super.onDragPointerMove(from, to);
        }else{
            return false;
        }
}

    @Override
    public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
        Log.d(TAG, "onDragPointerUp: " + from.toString() + "====" + to.toString());
        return super.onDragPointerUp(from, to);
    }

    @Override
    public void registerViewCallback(IMapEventCallback iMapEventCallback) {

    }

//    public void changeMove(View v){
//        mIsMove = !mIsMove;
//        if (mCallback != null) {
//            mCallback.isEdit(mIsMove, v);
//        }
//    }
    @Override
    public void unRegisterViewCallback(IMapEventCallback iMapEventCallback) {
        if (mCallback != null && iMapEventCallback == mCallback) {
            mCallback = null;
        }
    }
}
