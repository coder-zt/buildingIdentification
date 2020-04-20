package com.zhangtao.buildingidentification.elements;

public class DataObject{
    private int _id;
    private infoObject mInfoObject;
    private String inTime;
    private String bitmapJson;
    private String infoJson;

    /**
     * 没有Id，inTime的初始化函数
     * 插入数据库用
     */
    public DataObject(infoObject infoObject, String infoJson, String bitmapJson) {
        mInfoObject = infoObject;
        this.bitmapJson = bitmapJson;
        this.infoJson = infoJson;
    }

    /**
     *
     * 数据管理页面的初始化函数
     * @param _id
     * @param infoObject
     * @param inTime
     */
    public DataObject(int _id, infoObject infoObject, String inTime) {
        this._id = _id;
        mInfoObject = infoObject;
        this.inTime = inTime;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public infoObject getInfoObject() {
        return mInfoObject;
    }

    public void setInfoObject(infoObject infoObject) {
        mInfoObject = infoObject;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getBitmapJson() {
        return bitmapJson;
    }

    public void setBitmapJson(String bitmapJson) {
        this.bitmapJson = bitmapJson;
    }

    public String getInfoJson() {
        return infoJson;
    }

    public void setInfoJson(String infoJson) {
        this.infoJson = infoJson;
    }

}
