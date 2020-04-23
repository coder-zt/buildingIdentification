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
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.raster.FileRasterSource;
import com.zhangtao.buildingidentification.Base.BaseFragment;
import com.zhangtao.buildingidentification.DataPresenter;
import com.zhangtao.buildingidentification.Listeners.MyTouchListener;
import com.zhangtao.buildingidentification.R;
import com.zhangtao.buildingidentification.Views.mainToolBar;
import com.zhangtao.buildingidentification.Views.operationPopupWindow;
import com.zhangtao.buildingidentification.elements.BDElement;
import com.zhangtao.buildingidentification.elements.BDPoint;
import com.zhangtao.buildingidentification.interfaces.IMapEventCallback;
import com.zhangtao.buildingidentification.surveyActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.zhangtao.buildingidentification.Utils.Constant.ID_KEY;
import static com.zhangtao.buildingidentification.Utils.Constant.NOTE_INFO_X;
import static com.zhangtao.buildingidentification.Utils.Constant.NOTE_INFO_Y;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_CREATE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_KEY;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_NOTE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POINT;

public class HandleFragment extends BaseFragment implements mainToolBar.onItemClicked, IMapEventCallback {

    private MapView mMapView;
    private String TAG = "HandleFragment";
    private GraphicsLayer mGraphicsLayer;
    private View mView;
    private Context mContext;
    private mainToolBar mToolBar;

    private operationPopupWindow mOperationPopWindow;
    private List<BDPoint> mPoints =  new ArrayList<>();
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

    //当前是否处理可编辑状态
    private boolean mIsEdit = false;
    //选择的范围
    private final int mTolerance = 50;
    private DataPresenter mDataPresenter;


    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.test_map,container, false );

        mContext = container.getContext();
        initView();
        setupMap();
        initPresenter();
        initEvent();
        return mView;
    }

    private void initPresenter() {
        mDataPresenter = new DataPresenter(new DataPresenter.PresenterCallback() {
            @Override
            public void refresh() {
                drawOut();
            }
        });
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
            mMapView.addLayer(rasterLayer);
            mMapView.addLayer(mGraphicsLayer);
            mToolBar.setScale(Integer.toString((int)(10000)));
        }
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
            mOperationPopWindow = new operationPopupWindow(mContext);
        }

    }

    /**
     * 设计事件
     */
    private void initEvent() {

        //工具条的点击事件
        //单个按钮
        mToolBar.setOnItemClickedListener(this);
        mOperationPopWindow.registerViewCallback(mDataPresenter);
        mMyTouchListener = new MyTouchListener(mContext, mMapView, this);
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
    }

    /**
     * 工具条点击了编辑按钮的回调
     * @param isEdit
     */
    @Override
    public void editBtnClick(boolean isEdit) {
        mIsEdit = isEdit;
    }

    /**
     * 点击工具条完成按钮的回调
     */
    @Override
    public void finishedWorkedClick() {
        // TODO: 2020/4/19 添加过滤
        //if(mPoints.size() >= 0){
            Toast.makeText(mContext, "提取数据", Toast.LENGTH_SHORT ).show();
            startActivity(new Intent(mContext, surveyActivity.class));
        //}
    }

    /**
     * 点击工具条新建按钮的回调
     */
    @Override
    public void createElement() {
        //更具点属性设置面板参数
//        mCurrentPointIndex = getPointIndex((Point)currentGraphic.getGeometry());
        mOperationPopWindow.setPanel(TYPE_CREATE, null);
        mOperationPopWindow.showAsDropDown(mToolBar);
    }

    @Override
    public void onTapSingleTaped(MotionEvent point, boolean isMove){
        mDataPresenter.clearSelected();
        float x = point.getX();
        float y = point.getY();
        if(mIsEdit){
            //点击需要编辑的元素
            int ids[] =  mGraphicsLayer.getGraphicIDs(x, y, mTolerance);
            if(ids.length == 0){
                mDataPresenter.addPoint(mMapView.toMapPoint(x,y));
            }else{
                int grade = 0;
                for (int id : ids) {
                    Graphic currentGraphic =  mGraphicsLayer.getGraphic(id);
                    if (currentGraphic == null) {
                        return;
                    }
                    switch ((int)currentGraphic.getAttributes().get(TYPE_KEY)){
                        case TYPE_POINT :
                            if( grade == 3){
                                break;
                            }
                            grade = 3;
                            //根据点属性设置面板参数
                            mOperationPopWindow.setPanel(TYPE_POINT,   mDataPresenter.setSelectTarget(TYPE_POINT,currentGraphic.getGeometry()));
                            mOperationPopWindow.showAsDropDown(mToolBar);
                            break;
                        case TYPE_LINE :
                            if( grade > 2){
                                break;
                            }
                            grade = 2;
//                            mCurrentLineId = (int)currentGraphic.getAttributes().get(ID_KEY);
                            BDElement bdElement = mDataPresenter.setSelectTarget(TYPE_LINE, currentGraphic.getGeometry());
                            mOperationPopWindow.setPanel(TYPE_LINE, bdElement );
                            mOperationPopWindow.showAsDropDown(mToolBar);
                            break;
                        case TYPE_NOTE:
                            if( grade > 1){
                                break;
                            }
                            grade = 1;
                            //注解的坐标信息
                            Object X = currentGraphic.getAttributes().get(NOTE_INFO_X);
                            Object Y = currentGraphic.getAttributes().get(NOTE_INFO_Y);
                            if(X != null && Y != null){
                                Geometry target = new Point((Double)X, (Double)Y);
                                mOperationPopWindow.setPanel(TYPE_NOTE,  mDataPresenter.setSelectTarget(TYPE_NOTE,target));
                                mOperationPopWindow.showAsDropDown(mToolBar);
                            }
                            break;
                        default:

                            break;
                    }
                }
            }

        }
        drawOut();
    }

    private void drawOut() {
        mGraphicsLayer.removeAll();
        for (Graphic graphic : mDataPresenter.getGraphics()) {
            mGraphicsLayer.addGraphic(graphic);
        }
    }

    @Override
    public void onMapMove(MotionEvent from, MotionEvent to, boolean isMove) {

    }
//
////        mOperationPopWindow.setWindowDismissedListener(this);
//
//    }
//
//
//
//
//
//    //向mPoins中添加点元素
//    private void addPoint(float x, float y){
//        mCurrentPointIndex = -1;
//        mPoints.add(new BDPoint(mMapView.toMapPoint(x,y)));
//    }
//
//    //画点
//    private void drawPoint() {
//        if(mPoints.size() == 0){
//            return;
//        }
//        int i = 0;
//        for (BDPoint point : mPoints) {
//            //画出被选中的点
//            Map<String, Object> attribute = new HashMap<>();
//            //设置属性
//            attribute.put(TYPE_KEY, TYPE_POINT);
//            if(i == mCurrentPointIndex){
//
//                if (point.isBoundary()) {
//                    mGraphicsLayer.addGraphic(new Graphic(point.getPoint(), POINT_BOUNDARY_ORIGIN_SYMBOL, attribute));
//                    TextSymbol ts = new TextSymbol(12, point.getIndex(), Color.BLUE);
//                    ts.setFontFamily("DroidSans.ttf");
//                    mGraphicsLayer.addGraphic(new Graphic(point.getPoint(), ts , attribute));
//                }else{
//                    mGraphicsLayer.addGraphic(new Graphic(point.getPoint(), POINT_SELECTED_SYMBOL, attribute));
//                }
//            }else{
//                if (point.isBoundary()) {
//                    mGraphicsLayer.addGraphic(new Graphic(point.getPoint(), POINT_BOUNDARY_ORIGIN_SYMBOL, attribute));
//                    TextSymbol ts = new TextSymbol(12, point.getIndex(), Color.RED);
//                    ts.setFontFamily("DroidSans.ttf");
//
//                    mGraphicsLayer.addGraphic(new Graphic(point.getPoint(),ts, attribute));
//                }else{
//                    mGraphicsLayer.addGraphic(new Graphic(point.getPoint(), POINT_ORIGIN_SYMBOL, attribute));
//                }
//            }
//            i++;
//        }
//    }
//
//    //画线
//    private void drawLine() {
//        if(mPoints.size() == 0){
//            return;
//        }
//        int index = 0;
//        for (BDPoint point : mPoints) {
//            Polyline line = new Polyline();
//            line.startPath(point.getPoint());
//            line.lineTo(mPoints.get((index+1)%mPoints.size()).getPoint());
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
////
////    //画面
////    private void drawPolygon() {
////        if(mPoints.size() == 0){
////            return;
////        }
////        if (mPolygon == null) {
////            mPolygon = newbtn Polygon();
////        }else{
////            mPolygon.setEmpty();
////        }
////        if (mPoints.size()>1) {
////            for (int i = 0; i < mPoints.size(); i++) {
////                if (i == 0) {
////                    mPolygon.startPath(mPoints.get(i));
////                } else {
////                    mPolygon.lineTo(mPoints.get(i));
////                }
////            }
////            mPolygon.lineTo(mPoints.get(0));
////            Graphic currentGraphic  = newbtn Graphic(mPolygon, POLYGON_ORIGIN_SYMBOL);
////            mCurrentPloygonId = mGraphicsLayer.addGraphic(currentGraphic);
////        }
////    }
//
//    private void getLength(Point point, Point point1) {
//        double length = Math.sqrt(Math.pow((point.getX()-point1.getX()),2) + Math.pow((point.getY()-point1.getY()),2));
//        Log.d(TAG, length + "");
//    }
//
//    @Override
//    public void onClick(View v) {
//       switch (v.getId()){
//           //工具条的点击事件
//           case R.id.toolbar:
//               break;
//       }
//    }
//
//    /**
//     * 地图被点击的回调
//     * @param point
//     */
//    @Override
//    public void onTapSingleTaped(MotionEvent point, boolean isMove) {
//        float x = point.getX();
//        float y = point.getY();
//        if(mIsEdit){
//            mCurrentPointIndex = -1;
//            //点击需要编辑的元素
//            int ids[] =  mGraphicsLayer.getGraphicIDs(x, y, mTolerance);
//            if(ids.length == 0){
//                addPoint(x,y);
//            }else{
//                int grade = 0;
//                for (int id : ids) {
//                    Graphic currentGraphic =  mGraphicsLayer.getGraphic(id);
//                    switch ((int)currentGraphic.getAttributes().get(TYPE_KEY)){
//                        case TYPE_POINT :
//                            Log.d(TAG, "onTapSingleTaped:选中了点元素");
//                            if( grade == 3){
//                                break;
//                            }
//                            grade = 3;
//                            //更具点属性设置面板参数
//                            mCurrentPointIndex = getPointIndex((Point)currentGraphic.getGeometry());
//                            mOperationPopWindow.setPanel(TYPE_POINT, mPoints.get(mCurrentPointIndex));
//                            mOperationPopWindow.showAsDropDown(mToolBar);
//
//
//                            mCurrentLineId = -1;
//                            break;
//                        case TYPE_LINE :
//                            Log.d(TAG, "onTapSingleTaped:选中了线元素");
//                            if( grade > 2){
//                                break;
//                            }
//                            grade = 2;
//                            mCurrentLineId = (int)currentGraphic.getAttributes().get(ID_KEY);
//                            mCurrentLine = (Polyline) currentGraphic.getGeometry();
//                            mCurrentPointIndex = -1;
//                            mOperationPopWindow.setPanel(TYPE_LINE, null);
//                            mOperationPopWindow.showAsDropDown(mToolBar);
//                            break;
//                        case TYPE_NOTE:
//                            if( grade > 1){
//                                break;
//                            }
//                            grade = 1;
//                            mOperationPopWindow.setPanel(TYPE_NOTE, null);
//                            mOperationPopWindow.showAsDropDown(mToolBar);
//                            Log.d(TAG, "onTapSingleTaped:选中了面元素");
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
//
//        }
//        drawOut();
//    }
//
//
//
//    /**
//     *
//     * @param target
//     * @return
//     */
//    private int getPointIndex(Point target) {
//        int i = 0;
//        for (BDPoint point : mPoints) {
//            if (point.getPoint().equals(target)) {
//                return i;
//            }
//            i++;
//        }
//        return -1;
//    }
//
//    /**
//     * 地图上滑动的回调
//     * @param from
//     * @param to
//     * @param isMove
//     */
//    @Override
//    public void onMapMove(MotionEvent from, MotionEvent to, boolean isMove) {
//        if(!isMove){
//            //点击需要编辑的元素
//            int ids[] =  mGraphicsLayer.getGraphicIDs(from.getX(),from.getY(),20 ,1);
//            if(ids.length > 0){
//                if(mCurrentEditId != -1){
//                    mGraphicsLayer.updateGraphic(mCurrentEditId, POINT_ORIGIN_SYMBOL);
//                }
//
//                int id = ids[0];
//                Point target = (Point)mGraphicsLayer.getGraphic(id).getGeometry();
//                target.setY(target.getY() + (from.getY() - to.getY()));
//                target.setX(target.getX() -  (from.getX() - to.getX()));
//                mCurrentEditId = id;
//                mGraphicsLayer.updateGraphic(id, target);
//                mGraphicsLayer.updateGraphic(id, POINT_SELECTED_SYMBOL);
//
//            }else{
//                mGraphicsLayer.updateGraphic(mCurrentEditId, POINT_ORIGIN_SYMBOL);
//            }
//        }
//    }
//
//
//    private void drawOut() {
//        mGraphicsLayer.removeAll();
////        drawPolygon();
//        drawLine();
//        drawPoint();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (mMyTouchListener != null) {
//            mMyTouchListener.unRegisterViewCallback(this);
//        }
//        if (mOperationPopWindow != null) {
////            mOperationPopWindow.unRegisterViewCallback(this);
//        }
//    }
//
//
//    @Override
//    public void editBtnClick(boolean isEdit) {
//        mIsEdit = isEdit;
//        if(!isEdit){
//            mOperationPopWindow.dismiss();
//            initElement();
//            drawOut();
//        }
//    }
//
//    @Override
//    public void finishedWorkedClick() {
//        if(mPoints.size() >= 0){
//            Toast.makeText(mContext, "提取数据", Toast.LENGTH_SHORT ).show();
//            startActivity(new Intent(mContext, surveyActivity.class));
//        }
//    }
//
//
//
//    private void initElement() {
//        mCurrentPointIndex = -1;
//        mCurrentLineId = -1;
//        mCurrentLine = null;
//    }
//
//    /**
//     * 点的删除操作
//     */
//    @Override
//    public void clickDeleted() {
//        mPoints.remove(mCurrentPointIndex);
//        mCurrentPointIndex = -1;
//        drawOut();
//    }
//
//    /**
//     * 点的移动操作
//     * @param dir 方向
//     * @param unit 单位
//     */
//        @Override
//        public void clickOrientationed ( int dir, int unit){
//            if (mCurrentPointIndex == -1) {
//                return;
//            }
//            Point target = mPoints.get(mCurrentPointIndex).getPoint();
//            switch (dir) {
//                //向右
//                case 0:
//                    target.setX(target.getX() + unit / 100.0);
//                    break;
//                //向下
//                case 1:
//                    target.setY(target.getY() - unit / 100.0);
//                    break;
//                //向左
//                case 2:
//                    target.setX(target.getX() - unit / 100.0);
//                    break;
//                //向上
//                case 3:
//                    target.setY(target.getY() + unit / 100.0);
//                    break;
//            }
//            Log.d(TAG, target.toString());
//            drawOut();
//        }
//
//    @Override
//    public void clickedAddLineToMiddle() {
//        if(mCurrentLine != null){
//            Log.d(TAG, "clickedAddLineToMiddle:  " + mCurrentLine.getPathCount());
//            Log.d(TAG, "clickedAddLineToMiddle:  " + mCurrentLine.getPoint(0));
//            Log.d(TAG, "clickedAddLineToMiddle:  " + mCurrentLine.getPoint(1));
//            Point pointStart = mCurrentLine.getPoint(1);
//            Point pointEnd = mCurrentLine.getPoint(0);
//            BDPoint newPoint = new BDPoint(new Point((pointStart.getX() + pointEnd.getX())/2,(pointStart.getY() + pointEnd.getY())/2));
//            mPoints.add( mPoints.indexOf(pointStart), newPoint);
//            mCurrentLineId = -1;
//            mCurrentLine = null;
//            drawOut();
//
//        }
//    }
//
//    /**
//     * 点属性编辑完成，设置属性
//     * @param index
//     */
//    @Override
//    public void clickCompleted(String index) {
//        if(mCurrentPointIndex == -1){
//            return;
//        }
//        if (index != null) {
//            mPoints.get(mCurrentPointIndex).setBoundary(true, index);
//        }else{
//            mPoints.get(mCurrentPointIndex).setBoundary(false, null);
//        }
//        mOperationPopWindow.dismiss();
//        drawOut();
//    }
}
