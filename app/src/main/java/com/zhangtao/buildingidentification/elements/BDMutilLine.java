package com.zhangtao.buildingidentification.elements;

import android.app.backup.BackupDataOutput;
import android.util.Log;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhangtao.buildingidentification.Utils.Constant.ID_KEY;
import static com.zhangtao.buildingidentification.Utils.Constant.LINE_ORIGIN_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.LINE_SELECTED_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_KEY;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POINT;

public class BDMutilLine extends BDElement {

    public static final String TAG = "BDMutilLine";

    public List<BDPoint> getBDPoints() {
        return mBDPoints;
    }

    public void setBDPoints(List<BDPoint> BDPoints) {
        mBDPoints = BDPoints;
    }

    //组成多线段的点集合
    List<BDPoint> mBDPoints = new ArrayList<>();
    //多线段是否闭合
    boolean mIsClose = false;
    //多线段类型
    String mType;
    //被选中的线段
    int mTargetLine = -1;
    //该多线段所有边的集合
    List<Polyline> mPolylines = new ArrayList<>();

    public BDMutilLine(boolean isClose){
        mIsClose = isClose;
    }




    @Override
    public void elementSelect(int index) {

    }

    /**
     * 新增点元素
     * @param point
     */
    public void addPoint(Point point) {
        mBDPoints.add(new BDPoint(point));
    }

    @Override
    public List<Graphic> getGraphic() {
        List<Graphic> Graphics = new ArrayList<>();
        if(mBDPoints.size() == 0){
            return null;
        }
        int index = 0;
        if (mPolylines != null) {
            mPolylines.clear();
        }
        for (BDPoint point : mBDPoints) {
            if(!mIsClose && mBDPoints.size()-1 == index ){
                break;
            }
            Polyline line = new Polyline();
            line.startPath(point.getPoint());
            line.lineTo(mBDPoints.get((index+1)%mBDPoints.size()).getPoint());
            Map<String, Object> attribute = new HashMap<>();
            attribute.put(TYPE_KEY, TYPE_LINE);
            attribute.put(ID_KEY, index);
            mPolylines.add(line);
            if(index == mTargetLine){
                Graphics.add( new Graphic(line, LINE_SELECTED_SYMBOL, attribute));
            }else{
                Graphics.add( new Graphic(line, LINE_ORIGIN_SYMBOL, attribute));
            }
            index++;
        }
        return Graphics;
    }

    public void setTargetLine(int targetLine) {
        mTargetLine = targetLine;
    }

    public boolean hasLine(Polyline geometry){
        int index = 0;
        for (Polyline polyline : mPolylines) {
            if(polyline.equals( geometry)){
                mTargetLine = index;
                return true;
            }
            index++;
        }
        return false;
    }

    public float getCurrentLineLen(){

        Polyline polyline = mPolylines.get(mTargetLine);
        double x1 =  polyline.getPoint(0).getX();
        double y1 =  polyline.getPoint(0).getY();
        double x2 =  polyline.getPoint(1).getX();
        double y2 =  polyline.getPoint(1).getY();
        return (float) Math.abs(Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)));
    }

    public Point gerMiddlePoint(){

        Polyline polyline = mPolylines.get(mTargetLine);
        double x1 =  polyline.getPoint(0).getX();
        double y1 =  polyline.getPoint(0).getY();
        double x2 =  polyline.getPoint(1).getX();
        double y2 =  polyline.getPoint(1).getY();
        return new Point((x1+x2)/2, (y1+y2)/2);
    }

    public BDPoint CreateMiddlePoint(Point point) {
        BDPoint bdPoint = new BDPoint(point);
        mBDPoints.add(mTargetLine + 1,bdPoint );
        return bdPoint;
    }

    public void setType(String type) {
        mType = type;
    }
    public String getType(){
        return mType;
    }
}
