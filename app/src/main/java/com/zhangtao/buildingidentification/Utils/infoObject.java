package com.zhangtao.buildingidentification.Utils;

public class infoObject {

    /**
     * index_page : {"index":"lllsztdlp","code":"lllsztdlp","name":"张滔","time_year":"2020","time_month":"4","time_day":"11","eare_name":"开州区"}
     */

    private IndexPageBean index_page;

    public IndexPageBean getIndex_page() {
        return index_page;
    }

    public void setIndex_page(IndexPageBean index_page) {
        this.index_page = index_page;
    }

    public static class IndexPageBean {
        /**
         * index : lllsztdlp
         * code : lllsztdlp
         * name : 张滔
         * time_year : 2020
         * time_month : 4
         * time_day : 11
         * eare_name : 开州区
         */

        private String index;
        private String code;
        private String name;
        private String time_year;
        private String time_month;
        private String time_day;
        private String eare_name;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
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
    }
}
