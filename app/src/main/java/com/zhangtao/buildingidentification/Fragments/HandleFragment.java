package com.zhangtao.buildingidentification.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.RasterLayer;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.raster.FileRasterSource;
import com.zhangtao.buildingidentification.Base.BaseFragment;
import com.zhangtao.buildingidentification.Listeners.MyTouchListener;
import com.zhangtao.buildingidentification.R;
import com.zhangtao.buildingidentification.Views.mainToolBar;
import com.zhangtao.buildingidentification.Views.operationPopupWindow;
import com.zhangtao.buildingidentification.elements.BDPoint;
import com.zhangtao.buildingidentification.interfaces.IMapEventCallback;
import com.zhangtao.buildingidentification.interfaces.IOperation_Panel_Callback;
import com.zhangtao.buildingidentification.surveyActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhangtao.buildingidentification.Utils.Constant.ID_KEY;
import static com.zhangtao.buildingidentification.Utils.Constant.LINE_ORIGIN_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.LINE_SELECTED_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.POINT_ORIGIN_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.POINT_SELECTED_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.POLYGON_ORIGIN_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_KEY;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POINT;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POLYGON;

public class HandleFragment extends BaseFragment implements View.OnClickListener, IMapEventCallback, mainToolBar.onItemClicked, IOperation_Panel_Callback {

    private MapView mMapView;
    private String TAG = "HandleFragment";
    private GraphicsLayer mGraphicsLayer;
    private List<BDPoint> mPoints =  new ArrayList<>();
    private View mView;
    private Context mContext;
    private MyTouchListener mMyTouchListener;
    private int mCurrentEditId = -1;
    private Polygon mPolygon;
    private long mCurrentPloygonId = -1;
    //当前所选点的Index
    private int mCurrentPointIndex = -1;
    //当前所选线的Index
    private int mCurrentLineId = -1;
    //当前所选线的对象
    private Polyline mCurrentLine = null;
    private operationPopupWindow mOperationPopWindow;
    private mainToolBar mToolBar;
    //当前是否处理可编辑状态
    private boolean mIsEdit = false;
    //选择的范围
    private final int mTolerance = 50;


    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.test_map,container, false );

        mContext = container.getContext();
        initView();
        setupMap();
        initEvent();
        initData();

        return mView;
    }

    private void initData() {

    }


    /**
     *初始化控件
     */
    private void initView() {
        mMapView = mView.findViewById(R.id.mapView);
        mToolBar = mView.findViewById(R.id.toolbar);
        mToolBar.setScale(Double.toString((mMapView.getScale())));
        if (mContext != null) {
            Log.d(TAG, "initEvent: mContext is not null");
            mOperationPopWindow = new operationPopupWindow(mContext);
//            mOperationPopWindow.registerViewCallback(this);

        }else{
            Log.d(TAG, "initEvent: mContext is null");
        }

    }

    /**
     * 设计事件
     */
    private void initEvent(){
        mMyTouchListener = new MyTouchListener(mContext, mMapView, this);
        //工具条的点击事件
        mToolBar.setOnClickListener(this);
        mToolBar.setOnItemClickedListener(this);
        //popupWindow的事件
        mOperationPopWindow.registerViewCallback(this);
        mMapView.setOnTouchListener(mMyTouchListener);
        mMapView.setOnZoomListener(new OnZoomListener() {
            @Override
            public void preAction(float v, float v1, double v2) {
                Log.d(TAG, "preAction: " + v1);
            }

            @Override
            public void postAction(float v, float v1, double v2) {
                mToolBar.setScale(Integer.toString((int)(mMapView.getScale())));

            }
        });

//        mOperationPopWindow.setWindowDismissedListener(this);

    }

    //向mPoins中添加点元素
    private void addPoint(float x, float y){
        mCurrentPointIndex = -1;
        mPoints.add(new BDPoint(mMapView.toMapPoint(x,y)));
    }

    //画点
    private void drawPoint() {
        if(mPoints.size() == 0){
            return;
        }
        int i = 0;
        for (BDPoint point : mPoints) {
            //画出被选中的点
            Map<String, Object> attribute = new HashMap<>();
            attribute.put(TYPE_KEY, TYPE_POINT);
            if(i == mCurrentPointIndex){
                Graphic graphic = new Graphic(point.getPoint(), POINT_SELECTED_SYMBOL, attribute);
                mGraphicsLayer.addGraphic(graphic);
            }else{
                if (point.isBoundary()) {
                    mGraphicsLayer.addGraphic(new Graphic(point.getPoint(), POINT_ORIGIN_SYMBOL, attribute));
                }else{
                    mGraphicsLayer.addGraphic(new Graphic(point.getPoint(), POINT_ORIGIN_SYMBOL, attribute));
                }
            }
            i++;
        }
    }
//
//    //画线
//    private void drawLine() {
//        if(mPoints.size() == 0){
//            return;
//        }
//        int index = 0;
//        for (Point point : mPoints) {
//            Polyline line = new Polyline();
//            line.startPath(point);
//            line.lineTo(mPoints.get((index+1)%mPoints.size()));
//            Map<String, Object> attribute = new HashMap<>();
//            attribute.put(TYPE_KEY, TYPE_LINE);
//            attribute.put(ID_KEY, index);
//            if(index == mCurrentLineId){
//                mGraphicsLayer.addGraphic(new Graphic(line, LINE_SELECTED_SYMBOL, attribute));
//            }else{
//                mGraphicsLayer.addGraphic(new Graphic(line, LINE_ORIGIN_SYMBOL, attribute));
//            }
//            index++;
//        }
//
//    }
//
//    //画面
//    private void drawPolygon() {
//        if(mPoints.size() == 0){
//            return;
//        }
//        if (mPolygon == null) {
//            mPolygon = new Polygon();
//        }else{
//            mPolygon.setEmpty();
//        }
//        if (mPoints.size()>1) {
//            for (int i = 0; i < mPoints.size(); i++) {
//                if (i == 0) {
//                    mPolygon.startPath(mPoints.get(i));
//                } else {
//                    mPolygon.lineTo(mPoints.get(i));
//                }
//            }
//            mPolygon.lineTo(mPoints.get(0));
//            Graphic currentGraphic  = new Graphic(mPolygon, POLYGON_ORIGIN_SYMBOL);
//            mCurrentPloygonId = mGraphicsLayer.addGraphic(currentGraphic);
//        }
//    }

    private void getLength(Point point, Point point1) {
        double length = Math.sqrt(Math.pow((point.getX()-point1.getX()),2) + Math.pow((point.getY()-point1.getY()),2));
        Log.d(TAG, length + "");
}

    private void setupMap() {
        if (mMapView != null) {
            String rasterPath = Environment.getExternalStorageDirectory().getPath() + "/resoures/温江区谷歌卫星04231042.tif";
            FileRasterSource rasterSource = null;
            try {
                rasterSource = new FileRasterSource(rasterPath);
            } catch (IllegalArgumentException ie) {
                Log.d(TAG, "null or empty path");
            } catch (FileNotFoundException fe) {
                Log.d(TAG, "raster file doesn't exist");
            } catch (RuntimeException re) {
                Log.d(TAG, "raster file can't be opened");
            }
            mGraphicsLayer = new GraphicsLayer();
            RasterLayer rasterLayer = new RasterLayer(rasterSource);
            Envelope envelope = rasterLayer.getFullExtent();
            Log.d(TAG, "setupMap: " + envelope.toString());
            mMapView.setMinScale(100000);
            mMapView.setMaxScale(500);
//            mMapView.setScale(10000);
//            mMapView.setExtent(envelope);
            mMapView.addLayer(rasterLayer);
            mMapView.addLayer(mGraphicsLayer);

            mToolBar.setScale(Integer.toString((int)(10000)));
        }
    }


    @Override
    public void onClick(View v) {
       switch (v.getId()){
           //工具条的点击事件
           case R.id.toolbar:
               break;
       }
    }

    /**
     * 地图被点击的回调
     * @param point
     */
    @Override
    public void onTapSingleTaped(MotionEvent point, boolean isMove) {
        float x = point.getX();
        float y = point.getY();
        if(mIsEdit){
            mCurrentPointIndex = -1;
            //点击需要编辑的元素
            int ids[] =  mGraphicsLayer.getGraphicIDs(x, y, mTolerance);
            if(ids.length == 0){
                addPoint(x,y);
            }else{
                int grade = 0;
                for (int id : ids) {
                    Graphic currentGraphic =  mGraphicsLayer.getGraphic(id);
                    switch ((int)currentGraphic.getAttributes().get(TYPE_KEY)){
                        case TYPE_POINT :
                            Log.d(TAG, "onTapSingleTaped:选中了点元素");
                            if( grade == 3){
                                break;
                            }
                            grade = 3;
                            mOperationPopWindow.setPanel(TYPE_POINT);
                            mOperationPopWindow.showAsDropDown(mToolBar);
                            mCurrentPointIndex = getPointIndex((Point)currentGraphic.getGeometry());
                            mCurrentLineId = -1;
                            break;
                        case TYPE_LINE :
                            Log.d(TAG, "onTapSingleTaped:选中了线元素");
                            if( grade > 2){
                                break;
                            }
                            grade = 2;
                            mCurrentLineId = (int)currentGraphic.getAttributes().get(ID_KEY);
                            mCurrentLine = (Polyline) currentGraphic.getGeometry();
                            mCurrentPointIndex = -1;
                            mOperationPopWindow.setPanel(TYPE_LINE);
                            mOperationPopWindow.showAsDropDown(mToolBar);
                            break;
                        case TYPE_POLYGON :
                            if( grade > 1){
                                break;
                            }
                            grade = 1;
                            mOperationPopWindow.setPanel(TYPE_POLYGON);
                            mOperationPopWindow.showAsDropDown(mToolBar);
                            Log.d(TAG, "onTapSingleTaped:选中了面元素");
                            break;
                        default:
                            break;
                    }
                }
            }

        }
        drawOut();
    }



    /**
     *
     * @param target
     * @return
     */
    private int getPointIndex(Point target) {
        int i = 0;
        for (BDPoint point : mPoints) {
            if (point.getPoint().equals(target)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * 地图上滑动的回调
     * @param from
     * @param to
     * @param isMove
     */
    @Override
    public void onMapMove(MotionEvent from, MotionEvent to, boolean isMove) {
        if(!isMove){
            //点击需要编辑的元素
            int ids[] =  mGraphicsLayer.getGraphicIDs(from.getX(),from.getY(),20 ,1);
            if(ids.length > 0){
                if(mCurrentEditId != -1){
                    mGraphicsLayer.updateGraphic(mCurrentEditId, POINT_ORIGIN_SYMBOL);
                }

                int id = ids[0];
                Point target = (Point)mGraphicsLayer.getGraphic(id).getGeometry();
                target.setY(target.getY() + (from.getY() - to.getY()));
                target.setX(target.getX() -  (from.getX() - to.getX()));
                mCurrentEditId = id;
                mGraphicsLayer.updateGraphic(id, target);
                mGraphicsLayer.updateGraphic(id, POINT_SELECTED_SYMBOL);

            }else{
                mGraphicsLayer.updateGraphic(mCurrentEditId, POINT_ORIGIN_SYMBOL);
            }
        }
    }


    private void drawOut() {
        mGraphicsLayer.removeAll();
//        drawPolygon();
//        drawLine();
        drawPoint();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMyTouchListener != null) {
            mMyTouchListener.unRegisterViewCallback(this);
        }
        if (mOperationPopWindow != null) {
//            mOperationPopWindow.unRegisterViewCallback(this);
        }
    }


    @Override
    public void editBtnClick(boolean isEdit) {
        mIsEdit = isEdit;
        if(!isEdit){
            mOperationPopWindow.dismiss();
            initElement();
            drawOut();
        }
    }

    @Override
    public void finishedWorkedClick() {
        if(mPoints.size() >= 0){
            Toast.makeText(mContext, "提取数据", Toast.LENGTH_SHORT ).show();
            startActivity(new Intent(mContext, surveyActivity.class));
        }
    }

    private void initElement() {
        mCurrentPointIndex = -1;
        mCurrentLineId = -1;
        mCurrentLine = null;
    }

    /**
     * 点的删除操作
     */
    @Override
    public void clickDeleted() {
        mPoints.remove(mCurrentPointIndex);
        mCurrentPointIndex = -1;
        drawOut();
    }

    /**
     * 点的移动操作
     * @param dir 方向
     * @param unit 单位
     */
        @Override
        public void clickOrientationed ( int dir, int unit){
            if (mCurrentPointIndex == -1) {
                return;
            }
            Point target = mPoints.get(mCurrentPointIndex).getPoint();
            switch (dir) {
                //向右
                case 0:
                    target.setX(target.getX() + unit / 100.0);
                    break;
                //向下
                case 1:
                    target.setY(target.getY() - unit / 100.0);
                    break;
                //向左
                case 2:
                    target.setX(target.getX() - unit / 100.0);
                    break;
                //向上
                case 3:
                    target.setY(target.getY() + unit / 100.0);
                    break;
            }
            Log.d(TAG, target.toString());
            drawOut();
        }

    @Override
    public void clickedAddLineToMiddle() {
        if(mCurrentLine != null){
            Log.d(TAG, "clickedAddLineToMiddle:  " + mCurrentLine.getPathCount());
            Log.d(TAG, "clickedAddLineToMiddle:  " + mCurrentLine.getPoint(0));
            Log.d(TAG, "clickedAddLineToMiddle:  " + mCurrentLine.getPoint(1));
            Point pointStart = mCurrentLine.getPoint(1);
            Point pointEnd = mCurrentLine.getPoint(0);
            BDPoint newPoint = new BDPoint(new Point((pointStart.getX() + pointEnd.getX())/2,(pointStart.getY() + pointEnd.getY())/2));
            mPoints.add( mPoints.indexOf(pointStart), newPoint);
            mCurrentLineId = -1;
            mCurrentLine = null;
            drawOut();

        }
    }
}
