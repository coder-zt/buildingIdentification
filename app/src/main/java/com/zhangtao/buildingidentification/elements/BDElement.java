package com.zhangtao.buildingidentification.elements;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;

import java.util.List;

public abstract class BDElement {
    public abstract void elementSelect(int index);

    public void addPoint(Point point){

    }

    public abstract List<Graphic> getGraphic();

}
