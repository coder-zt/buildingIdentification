package com.zhangtao.buildingidentification;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.zhangtao.buildingidentification.elements.BDElement;
import com.zhangtao.buildingidentification.elements.BDMutilLine;
import com.zhangtao.buildingidentification.elements.BDNote;
import com.zhangtao.buildingidentification.elements.BDPoint;

import org.achartengine.internal.GraphicalView;

import java.util.ArrayList;
import java.util.List;

import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_CLOSE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_LINE;

public class DataMoudle {
    List<BDPoint> mBDPoints = new ArrayList<>();
    List<BDMutilLine> mBDMutilLines = new ArrayList<>();
    List<BDNote> mBDNotes = new ArrayList<>();
    BDElement mBDElement;

    public void createElement(int type) {
        switch (type){
            //非闭合多线段
            case TYPE_LINE:
                BDMutilLine bdMutilLine = new BDMutilLine(false);
                mBDMutilLines.add(bdMutilLine);
                mBDElement = bdMutilLine;
                break;
            case TYPE_CLOSE_LINE:
                BDMutilLine bdCloseMutilLine = new BDMutilLine(true);
                mBDMutilLines.add(bdCloseMutilLine);
                mBDElement = bdCloseMutilLine;
                break;
        }
    }

    public void addPoint(Point point) {
        mBDPoints.add(new BDPoint(point));
        mBDElement.addPoint(point);
    }

    //增删改查

    public List<Graphic> getGraphics(){
        List<Graphic> graphics = new ArrayList<>();
        for (BDMutilLine bdMutilLine : mBDMutilLines) {
            if (bdMutilLine.getGraphic() != null) {
                graphics.addAll(bdMutilLine.getGraphic());
            }

        }
        for (BDPoint bdPoint : mBDPoints) {
            graphics.addAll(bdPoint.getGraphic());
        }
        return graphics;
    }

    public BDPoint setSelectPointTarget(Point geometry) {
        BDPoint mPoint = null;
        for (BDPoint bdPoint : mBDPoints) {
            if (bdPoint.isSamePoint(geometry)) {
                bdPoint.setSelect(true);
                mPoint = bdPoint;
            }else{
                bdPoint.setSelect(false);
            }
        }
        return mPoint;
    }

    public void deletePoint(BDPoint currentBDPoint) {
        for (BDPoint bdPoint : mBDPoints) {
            if (bdPoint.isSamePoint(currentBDPoint.getPoint())) {
                mBDPoints.remove(bdPoint);
                break;
            }
        }
        for (BDMutilLine bdMutilLine : mBDMutilLines) {
            for (BDPoint bdPoint : bdMutilLine.getBDPoints()) {
                if (bdPoint.isSamePoint(currentBDPoint.getPoint())) {
                    bdMutilLine.getBDPoints().remove(bdPoint);
                    break;
                }
            }
        }
    }

    public BDMutilLine setSelectLineTarget(Polyline geometry) {
        for (BDMutilLine bdMutilLine : mBDMutilLines) {
            if (bdMutilLine.hasLine(geometry)) {
                return bdMutilLine;
            }
        }
        return null;
    }
}
