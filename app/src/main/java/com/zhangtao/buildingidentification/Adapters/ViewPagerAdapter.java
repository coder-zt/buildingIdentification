package com.zhangtao.buildingidentification.Adapters;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zhangtao.buildingidentification.Utils.FragmentCreator;

import static com.zhangtao.buildingidentification.Utils.Constant.SURVEY_PAGE;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final int mCount;
    private final int mType;

    public ViewPagerAdapter(int type, @NonNull FragmentManager fm, int size) {
        super(fm);
        mType = type;
        mCount = size;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment =  FragmentCreator.getFragment(mType,position);

        return fragment;
    }



    @Override
    public int getCount() {
        return mCount;
    }
}
