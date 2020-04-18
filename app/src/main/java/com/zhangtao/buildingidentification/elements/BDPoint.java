package com.zhangtao.buildingidentification.elements;

import com.esri.core.geometry.Point;

/**
 * 草图中的点元素
 */
public class BDPoint {

    public BDPoint(Point point) {
        mPoint = point;
    }

    //点元素的点的位置信息
    private Point mPoint;
    //界址点的标志
    private boolean mIsBoundary;

    public Point getPoint() {
        return mPoint;
    }

    public void setPoint(Point point) {
        mPoint = point;
    }

    public boolean isBoundary() {
        return mIsBoundary;
    }

    public void setBoundary(boolean boundary) {
        mIsBoundary = boundary;
    }
}
