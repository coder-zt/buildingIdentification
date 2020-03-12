package com.zhangtao.buildingidentification.Utils;

import com.zhangtao.buildingidentification.Base.BaseFragment;
import com.zhangtao.buildingidentification.Fragments.HandleFragment;
import com.zhangtao.buildingidentification.Fragments.ManagementFragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentCreateor {

    private static Map<Integer, BaseFragment> sCache = new HashMap();

    public static BaseFragment getFragment(int index){
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
        return  baseFragment;
    }
}
