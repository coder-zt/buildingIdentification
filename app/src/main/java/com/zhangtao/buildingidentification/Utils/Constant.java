package com.zhangtao.buildingidentification.Utils;

import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;

import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.zhangtao.buildingidentification.R;

public class Constant {
    //ViewPager的类型
    public static final int MAIN_PAGE = 1;
    public static final int SURVEY_PAGE = 2;
    //ViewPager的数量
    public static final int COUNT_OF_VIEWPAGER = 2;

    //数据处理Fragment的代码
    static final int FRAGMENT_FOR_DATA_HANDLE = 0;
    //数据管理Fragment的代码
    static final int FRAGMENT_FOR_DATA_MANAGEMENT = 1;

    public static final int FRAGMENT_FOR_DATA_INDEX = 0;
    public static final int FRAGMENT_FOR_DATA_BASE = 1;
    public static final int FRAGMENT_FOR_DATA_THREE = 2;
    public static final int FRAGMENT_FOR_DATA_FOUR = 3;
    public static final int FRAGMENT_FOR_DATA_FIVE = 4;


    //tab的icon图片集
    public static final int[] selected = {R.mipmap.data_handle_selected, R.mipmap.data_manager_selected};
    public static final int[] selector = {R.mipmap.data_handle, R.mipmap.data_manager};

    //* * * * * 要素的样式 * * * * *
    //1.点样式
    //1.1 原始样式
    public static final SimpleMarkerSymbol POINT_ORIGIN_SYMBOL = new SimpleMarkerSymbol(Color.RED, 5,SimpleMarkerSymbol.STYLE.SQUARE);
    //1.2 选中样式
    public static final SimpleMarkerSymbol POINT_SELECTED_SYMBOL = new SimpleMarkerSymbol(Color.GREEN, 5, SimpleMarkerSymbol.STYLE.SQUARE);
    //2. 线样式
    //2.1 原始样式
    public static final SimpleLineSymbol LINE_ORIGIN_SYMBOL = new SimpleLineSymbol(Color.BLUE, 1, SimpleLineSymbol.STYLE.SOLID);
    //2.2 选中样式
    public static final SimpleLineSymbol LINE_SELECTED_SYMBOL = new SimpleLineSymbol(Color.GREEN, 1, SimpleLineSymbol.STYLE.SOLID);
    //3. 面样式
    //3.1 原始样式
    public static final SimpleFillSymbol POLYGON_ORIGIN_SYMBOL = new SimpleFillSymbol(Color.BLUE, SimpleFillSymbol.STYLE.DIAGONAL_CROSS);
    //3.2 选中样式
    public static final SimpleFillSymbol P0LYGON_SELECTED_SYMBOL = new SimpleFillSymbol(Color.GREEN, SimpleFillSymbol.STYLE.CROSS);

    // * * * * * 地图元素的属性 * * * * *
    public final static String TYPE_KEY = "TYPE";
    public final static String ID_KEY = "ID";
    //1. 点
    public final static int TYPE_POINT = 11;
    //2. 线
    public final static int TYPE_LINE = 12;
    //3. 面
    public final static int TYPE_POLYGON = 13;


    //调查信息数据表
    //数据库信息设置
    //数据库名称
    public static final String DATABASE_NAME = "BDDATABASE";
    //数据库版本
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_INFO_NAME = "surveyInfo";

    //Activity的信息传递相关字段
    public static final String INTENT_TO_SURVEY="dataShow";
}
