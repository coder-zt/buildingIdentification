package com.zhangtao.buildingidentification.Utils;


import com.zhangtao.buildingidentification.Base.BaseFragment;
import com.zhangtao.buildingidentification.Fragments.HandleFragment;
import com.zhangtao.buildingidentification.Fragments.ManagementFragment;

import java.util.HashMap;
import java.util.Map;


public class FragmentCreator {
    private final static  String TAG = "FragmentCreator";
    private static Map<Integer, BaseFragment> sCache = new HashMap();
    private static Map<Integer, BaseFragment> sSurveyCache = new HashMap();
    public static BaseFragment getFragment(int type, int index){
        return  createMainFragment(index);
    }



    private static BaseFragment createMainFragment(int index) {
        BaseFragment baseFragment = sCache.get(index);
        if(baseFragment != null){
            return baseFragment;
        }
        switch(index){
            case Constant.FRAGMENT_FOR_DATA_HANDLE:
                baseFragment = new HandleFragment();
                break;
            case Constant.FRAGMENT_FOR_DATA_MANAGEMENT:
                baseFragment = new ManagementFragment();
                break;
            default:
                break;
        }
        sCache.put(index, baseFragment);
        return baseFragment;
    }
}
