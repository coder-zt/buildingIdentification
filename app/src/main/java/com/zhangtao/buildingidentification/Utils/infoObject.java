package com.zhangtao.buildingidentification.Utils;

public class infoObject {

    /**
     * indexcode : lllsztdlp
     * code : lllsztdlp
     * name : 张滔
     * time_year : 2020
     * time_month : 4
     * time_day : 11
     * eare_name : 开州区
     */

    private String indexcode;
    private String code;
    private String name;
    private String time_year;
    private String time_month;
    private String time_day;
    private String eare_name;

    public String getIndexcode() {
        return indexcode;
    }

    public void setIndexcode(String indexcode) {
        this.indexcode = indexcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime_year() {
        return time_year;
    }

    public void setTime_year(String time_year) {
        this.time_year = time_year;
    }

    public String getTime_month() {
        return time_month;
    }

    public void setTime_month(String time_month) {
        this.time_month = time_month;
    }

    public String getTime_day() {
        return time_day;
    }

    public void setTime_day(String time_day) {
        this.time_day = time_day;
    }

    public String getEare_name() {
        return eare_name;
    }

    public void setEare_name(String eare_name) {
        this.eare_name = eare_name;
    }

    public String[] getStringArrayObject(){
        //indexcode, code, name, time_year, time_month, time_day, eare_name
        return new String[]{indexcode,code,name,time_year,time_month,time_day,eare_name};
    }
}
