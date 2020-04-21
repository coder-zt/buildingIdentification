package com.zhangtao.buildingidentification.elements;

import android.graphics.Color;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.TextSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhangtao.buildingidentification.Utils.Constant.POINT_BOUNDARY_ORIGIN_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.POINT_BOUNDARY_SELECTED_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.POINT_ORIGIN_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.POINT_SELECTED_SYMBOL;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_KEY;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POINT;

/**
 * 草图中的点元素
 */
public class BDPoint extends BDElement {

    public BDPoint(Point point) {
        mPoint = point;
    }

    //点元素的点的位置信息
    private Point mPoint;
    //界址点的标志
    private boolean mIsBoundary;

    public void setSelect(boolean select) {
        mIsSelect = select;
    }

    //编号
    private String mIndex;
    //选中
    private boolean mIsSelect;
    public Point getPoint() {
        return mPoint;
    }

    public void setPoint(Point point) {
        mPoint = point;
    }

    public boolean isBoundary() {
        return mIsBoundary;
    }

    public void setBoundary(boolean boundary, String index) {
        mIsBoundary = boundary;
        this.mIndex = index;
    }

    public String getIndex() {
        return mIndex;
    }

    @Override
    public void elementSelect(int index) {

    }


    @Override
    public List<Graphic> getGraphic() {
        List<Graphic> graphics = new ArrayList<>();
        Map<String, Object> attribute = new HashMap<>();
        attribute.put(TYPE_KEY, TYPE_POINT);
        if(mIsSelect){
            if(mIsBoundary){
//                TextSymbol textSymbol = new TextSymbol(22, mIndex, Color.BLUE);
//                graphics.add(new Graphic(mPoint,textSymbol));
                graphics.add(new Graphic(mPoint, POINT_BOUNDARY_SELECTED_SYMBOL, attribute));
            }else{
                graphics.add(new Graphic(mPoint, POINT_SELECTED_SYMBOL, attribute));
            }
        }else{
            if(mIsBoundary){
//                TextSymbol textSymbol = new TextSymbol(22, mIndex, Color.BLUE);
//                graphics.add(new Graphic(mPoint,textSymbol));
                graphics.add(new Graphic(mPoint, POINT_BOUNDARY_ORIGIN_SYMBOL, attribute));
            }else{
                graphics.add(new Graphic(mPoint, POINT_ORIGIN_SYMBOL, attribute));
            }
        }
        return graphics;
    }

    public boolean isSamePoint(Point point) {
        if(point.getX() == mPoint.getX() && point.getY() == mPoint.getY()){
            return true;
        }
        return false;
    }
}
