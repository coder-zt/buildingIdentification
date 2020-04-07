package com.zhangtao.buildingidentification.Base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

public class BaseApplication extends Application {
    private static Context sContext = null;



    @Override
    public void onCreate() {
        super.onCreate();
        //获取上下文
        sContext = getBaseContext();
    }


    public static Context getContext(){
        return sContext;
    }

}
