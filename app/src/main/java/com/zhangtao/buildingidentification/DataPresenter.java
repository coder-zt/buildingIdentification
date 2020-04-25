package com.zhangtao.buildingidentification;

import android.util.Log;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.zhangtao.buildingidentification.elements.BDElement;
import com.zhangtao.buildingidentification.elements.BDMutilLine;
import com.zhangtao.buildingidentification.elements.BDNote;
import com.zhangtao.buildingidentification.elements.BDPoint;
import com.zhangtao.buildingidentification.interfaces.IOperationPanelCallback;

import java.util.List;

import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_NOTE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POINT;


public class DataPresenter implements IOperationPanelCallback {

    public static final String TAG = "DataPresenter";
    private final PresenterCallback mCallback;
    private BDElement mCurrentElement;
    private DataMoudle mDataMoudle;
    private BDPoint mCurrentBDPoint;
    private BDMutilLine mCurrentBDMutilLine;
    private boolean mIsCreateMiddlePoint;
    private BDNote mCurrentBDNote;
    private String mAddNote;
    private boolean mIsAddNote;

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

    /**
     * 点元素完成编辑的提交
     * @param index
     */
    @Override
    public void clickCompleted(String index) {
        if(index != null){
            mCurrentBDPoint.setBoundary(true, index);
            if (mDataMoudle != null) {
                mDataMoudle.createBDNote(index, mCurrentBDPoint);
            }
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

    @Override
    public void CreatePointOnLine() {
        mIsCreateMiddlePoint = true;
        Log.d(TAG, "setSelectTarget: " + mCurrentBDMutilLine.toString());
    }

    @Override
    public void commitInfoToLine(String type) {
        if (mCurrentBDMutilLine != null) {
            mCurrentBDMutilLine.setType(type);
        }
    }

    @Override
    public void addNoteForLine(String tag) {
        if (mDataMoudle != null) {
            mDataMoudle.createBDNoteForLine(tag);
            mCallback.refresh();
        }
    }

    @Override
    public void addNoteBySelf(String note) {
        mAddNote = note;

        mIsAddNote = true;
    }

    public boolean getIsAddNote(){
        return mIsAddNote;
    }

    @Override
    public void valueChange(String name, int value) {
        switch (name){
            case "大小":
                if (mCurrentBDNote != null) {
                    mCurrentBDNote.setSize(value);
                    break;
                }
            case "角度":
                if (mCurrentBDNote != null) {
                    mCurrentBDNote.setAngle(value);
                }
                break;
            case "字间距":
                if (mCurrentBDNote != null) {
                    mCurrentBDNote.setWidth(value);
                }
                break;
        }
        mCallback.refresh();
    }

    public void addPoint(Point point){
        if (mDataMoudle != null && !mIsCreateMiddlePoint) {
            mDataMoudle.addPoint(point);
        }else if(mIsCreateMiddlePoint && mCurrentBDMutilLine != null){
            mDataMoudle.createMiddlePoint((BDMutilLine)mCurrentBDMutilLine, point);
            mIsCreateMiddlePoint = false;
        }
    }

    public BDElement setSelectTarget(int type, Geometry geometry) {
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
                return mCurrentBDPoint;
            case TYPE_LINE:
                mCurrentBDMutilLine = mDataMoudle.setSelectLineTarget((Polyline)geometry);
                return mCurrentBDMutilLine;
            case TYPE_NOTE:
                mCurrentBDNote =  mDataMoudle.setSelectNoteTarget((Point)geometry);
                return mCurrentBDNote;
        }
        return null;
    }

    public List<Graphic> getGraphics() {
        return mDataMoudle.getGraphics();
    }

    public void clearSelected() {
        if (mCurrentBDPoint != null) {
            mCurrentBDPoint.setSelect(false);
            mCurrentBDPoint = null;
        }
        if (mCurrentBDMutilLine != null && !mIsCreateMiddlePoint) {
            mCurrentBDMutilLine.setTargetLine(-1);
            mCurrentBDMutilLine = null;
        }
        if (mCurrentBDNote != null) {
            mCurrentBDNote.setSelected(false);
            mCurrentBDNote = null;
        }
    }

    public void addNoteForClick(Point toMapPoint) {
        if (mDataMoudle != null && mAddNote != null) {
            mDataMoudle.createBDNoteForClick(toMapPoint,mAddNote );
            mIsAddNote = false;
            mCallback.refresh();
        }
    }


    public interface PresenterCallback{
        void refresh();
    }
}
