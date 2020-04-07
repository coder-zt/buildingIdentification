package com.zhangtao.buildingidentification.interfaces;

public interface IOperation_Panel_Callback {

    /**
     * 点击了删除按钮
     */
    void clickDeleted();


    /**
     * 点击了方向按钮
     * @param dir 方向
     * @param unit 单位
     */
    void clickOrientationed(int dir, int unit);

    /**
     * 点击了线上添加中点
     */
    void clickedAddLineToMiddle();
}
