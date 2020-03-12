package com.zhangtao.buildingidentification.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.zhangtao.buildingidentification.Utils.FragmentCreateor;
import static com.zhangtao.buildingidentification.Utils.Constant.COUNT_OF_VIEWPAGER;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FragmentCreateor.getFragment(position);
    }

    @Override
    public int getCount() {
        return COUNT_OF_VIEWPAGER;
    }
}
