package com.zhangtao.buildingidentification.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.zhangtao.buildingidentification.Utils.Constant.DATABASE_NAME;
import static com.zhangtao.buildingidentification.Utils.Constant.DATABASE_VERSION;
import static com.zhangtao.buildingidentification.Utils.Constant.TABLE_INFO_NAME;

public class DBSurveryInfo extends SQLiteOpenHelper {




    public DBSurveryInfo(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //1.编写sql语句 ==id、土地权利人、编号、宗地代码、区县、时间、入库时间、土地着落、其他文文字信息、签名图片信息
        String sql = "create table " + TABLE_INFO_NAME + " (_id integer primary key autoincrement, " +
                                                        "indexcode varchar(50) not null,"+
                                                        "code varchar(50) not null,"+
                                                        "name varchar(50) not null,"+
                                                        "time_year varchar(50) not null,"+
                                                        "time_month varchar(50) not null,"+
                                                        "time_day char(50) not null,"+
                                                        "eare_name varechar(50) not null," +
                                                        "intime varchar(50) not null," +
                                                        "local varchar(50) not null," +
                                                        "infoJson varchar(50) not null," +
                                                        "bitmapJson varcher(50) not null)";
//2.执行sql语句
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
