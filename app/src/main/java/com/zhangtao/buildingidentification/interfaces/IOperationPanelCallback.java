package com.zhangtao.buildingidentification.interfaces;

import com.zhangtao.buildingidentification.Views.BDNumber;

public interface IOperationPanelCallback extends BDNumber.onTypeValueChange {

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

    /**
     * 点操作点击完成按钮
     */
    void clickCompleted(String index);

    /**
     * 创建元素
     * @param type
     */
    void createElement(int type);

    /**
     * 获取线段的长度
     */
    float getCurrentLineLength();

    /**
     * 在当前线段中创建一个新的点
     */
    void CreatePointOnLine();

    /**
     * 更新线段的信息
     */
    void commitInfoToLine(String type);

    /**
     * 给线添加注解
     */
    void addNoteForLine(String tag);

    /**
     * 添加额外的注解
     * @param note
     */
    void addNoteBySelf(String note);
}
