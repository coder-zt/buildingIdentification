package com.zhangtao.buildingidentification;

import android.view.View;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.zhangtao.buildingidentification.elements.BDElement;
import com.zhangtao.buildingidentification.elements.BDMutilLine;
import com.zhangtao.buildingidentification.elements.BDPoint;
import com.zhangtao.buildingidentification.interfaces.IOperationPanelCallback;

import java.util.List;

import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_CLOSE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POINT;

public class DataPresenter implements IOperationPanelCallback {

    private final PresenterCallback mCallback;
    private BDElement mCurrentElement;
    private DataMoudle mDataMoudle;
    private BDPoint mCurrentBDPoint;
    private BDMutilLine mCurrentBDMutilLine;

    public DataPresenter(PresenterCallback callbak){
        mCallback = callbak;
        mDataMoudle = new DataMoudle();
    }



    /**
     * 点的删除操作
     */
    @Override
    public void clickDeleted() {
        if (mDataMoudle != null) {
            mDataMoudle.deletePoint(mCurrentBDPoint);
        }
        if (mCallback != null) {
            mCallback.refresh();
        }
    }

    /**
     * 点的移动操作
     * @param dir 方向
     * @param unit 单位
     */
    @Override
    public void clickOrientationed(int dir, int unit) {
            if (mCurrentBDPoint == null) {
                return;
            }
            Point target = mCurrentBDPoint.getPoint();
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
        if (mCallback != null) {
            mCallback.refresh();
        }
    }

    @Override
    public void clickedAddLineToMiddle() {

    }

    @Override
    public void clickCompleted(String index) {
        if(index != null){
            mCurrentBDPoint.setBoundary(true, index);
        }
        if (mCallback != null) {
            mCallback.refresh();
        }
    }

    /**
     *创建新的元素
     * @param type
     */
    @Override
    public void createElement(int type) {
        if (mDataMoudle != null) {
            mDataMoudle.createElement(type);
        }
    }

    @Override
    public float getCurrentLineLength() {
        BDMutilLine line =  (BDMutilLine)mCurrentBDMutilLine;
return  line.getCurrentLineLen();
    }

    public void addPoint(Point point){
        if (mDataMoudle != null) {
            mDataMoudle.addPoint(point);
        }
    }

    public void setSelectTarget(int type, Geometry geometry) {
        if (mCurrentBDPoint != null) {
            mCurrentBDPoint.setSelect(false);
            mCurrentBDPoint = null;
        }
        if (mCurrentBDMutilLine != null) {
            mCurrentBDMutilLine.setTargetLine(-1);
            mCurrentBDMutilLine = null;
        }
        switch (type){
            case TYPE_POINT:
                mCurrentBDPoint = mDataMoudle.setSelectPointTarget((Point)geometry);
                break;
            case TYPE_LINE:
                mCurrentBDMutilLine = mDataMoudle.setSelectLineTarget((Polyline)geometry);
                break;
        }
    }

    public List<Graphic> getGraphics() {
        return mDataMoudle.getGraphics();
    }

    public void clearSelected() {
        if (mCurrentBDPoint != null) {
            mCurrentBDPoint.setSelect(false);
            mCurrentBDPoint = null;
        }
        if (mCurrentBDMutilLine != null) {
            mCurrentBDMutilLine.setTargetLine(-1);
            mCurrentBDMutilLine = null;
        }
    }

    public interface PresenterCallback{
        void refresh();
    }
}
