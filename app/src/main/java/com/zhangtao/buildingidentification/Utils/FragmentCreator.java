package com.zhangtao.buildingidentification.Utils;

import android.util.Log;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.zhangtao.buildingidentification.Base.BaseFragment;
import com.zhangtao.buildingidentification.Fragments.FiveFragment;
import com.zhangtao.buildingidentification.Fragments.FourFragment;
import com.zhangtao.buildingidentification.Fragments.HandleFragment;
import com.zhangtao.buildingidentification.Fragments.IndexFragment;
import com.zhangtao.buildingidentification.Fragments.ManagementFragment;
import com.zhangtao.buildingidentification.Fragments.SecondeFragment;
import com.zhangtao.buildingidentification.Fragments.ThreeFragment;

import java.util.HashMap;
import java.util.Map;


public class FragmentCreator {
    private final static  String TAG = "FragmentCreator";
    private static Map<Integer, BaseFragment> sCache = new HashMap();
    private static Map<Integer, BaseFragment> sSurveyCache = new HashMap();
    public static BaseFragment getFragment(int type, int index){
       switch (type){
           case Constant.MAIN_PAGE:
               return  createMainFragment(index);
           case Constant.SURVEY_PAGE:
               return createSurveyFragment(index);
           default:
                   return null;
       }
    }

    private static BaseFragment createSurveyFragment(int index) {
        Log.d(TAG, "createSurveyFragment: " + index);
        BaseFragment baseFragment = sSurveyCache.get(index);
        if(baseFragment != null){
            return baseFragment;
        }
        switch(index){
            case Constant.FRAGMENT_FOR_DATA_INDEX:
                baseFragment = new IndexFragment();
                break;
            case Constant.FRAGMENT_FOR_DATA_BASE:
                baseFragment = new SecondeFragment();
                break;
            case Constant.FRAGMENT_FOR_DATA_THREE:
                baseFragment = new ThreeFragment();
                break;
            case Constant.FRAGMENT_FOR_DATA_FOUR:
                baseFragment = new FourFragment();
                break;
            case Constant.FRAGMENT_FOR_DATA_FIVE:
                baseFragment = new FiveFragment();
                break;
            default:
                break;
        }
        sSurveyCache.put(index, baseFragment);
        Log.d(TAG, "createSurveyFragment: " + index);
        return baseFragment;
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
