package com.zhangtao.buildingidentification.interfaces;

import com.zhangtao.buildingidentification.Base.IBaseInterface;

public interface IOperation_Panel_Event extends IBaseInterface<IOperation_Panel_Callback> {

    /**
     * 点击了删除按钮
     */
    void clickDelete();


    /**
     * 点击了方向按钮
     */
    void clickOrientation(int dir);

    /**
     * 点击了添加中点按钮
     */
    void clickAddLineToMiddle();
}
