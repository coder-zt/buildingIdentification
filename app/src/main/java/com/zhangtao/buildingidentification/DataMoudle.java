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

    /**
     * 获取所有数据的图像
     * @return
     */
    public List<Graphic> getGraphics(){
        List<Graphic> graphics = new ArrayList<>();
        for (BDMutilLine bdMutilLine : mBDMutilLines) {
            if (bdMutilLine.getGraphic() != null) {
                graphics.addAll(bdMutilLine.getGraphic());
            }

        }
        for (BDNote bdNote : mBDNotes) {
            graphics.addAll(bdNote.getGraphic());
        }
        for (BDPoint bdPoint : mBDPoints) {
            graphics.addAll(bdPoint.getGraphic());
        }
        return graphics;
    }

    /**
     * 设置当前所选点元素
     * @param geometry
     * @return
     */
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

    /**
     * 删除点元素
     * @param currentBDPoint
     */
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

    /**
     * 设置选中的线元素
     * @param geometry
     * @return
     */
    public BDMutilLine setSelectLineTarget(Polyline geometry) {
        for (BDMutilLine bdMutilLine : mBDMutilLines) {
            if (bdMutilLine.hasLine(geometry)) {
                return bdMutilLine;
            }
        }
        return null;
    }

    /**
     * 在线段中插入新的点元素
     * @param currentBDMutilLine
     * @param point
     */
    public void createMiddlePoint(BDMutilLine currentBDMutilLine, Point point) {
        mBDPoints.add(currentBDMutilLine.CreateMiddlePoint(point));
    }

    /**
     * 创建备注元素
     * @param index
     * @param currentBDPoint
     */
    public void createBDNote(String index, BDPoint currentBDPoint) {
        BDNote note = new BDNote(index, currentBDPoint, currentBDPoint.getPoint());
        mBDNotes.add(note);
    }

    public  BDNote setSelectNoteTarget(Point geometry) {
        for (BDNote bdNote : mBDNotes) {
            if(BDPoint.isSamePoint(bdNote.getPoint(), geometry)){
                bdNote.setSelected(true);
                return bdNote;
            }
        }
        return null;
    }

    public void createBDNoteForLine(String tag) {
        if (mBDElement != null) {
            BDMutilLine line =  (BDMutilLine) mBDElement;
            Point point = line.gerMiddlePoint();
            if(tag == "type"){
                BDNote note = new BDNote(((BDMutilLine) mBDElement).getType(), mBDElement, point);
                mBDNotes.add(note);
            }else if(tag == "len"){
                BDNote note = new BDNote("" + ((BDMutilLine) mBDElement).getCurrentLineLen(), mBDElement, point);
                mBDNotes.add(note);
            }

        }
    }
}
