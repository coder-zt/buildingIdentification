package com.zhangtao.buildingidentification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangtao.buildingidentification.Adapters.IndicatorAdapter;
import com.zhangtao.buildingidentification.Adapters.ViewPagerAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import static com.zhangtao.buildingidentification.Utils.Constant.MAIN_PAGE;
import static com.zhangtao.buildingidentification.Utils.Constant.SURVEY_PAGE;
import static com.zhangtao.buildingidentification.Utils.Constant.selected;
import static com.zhangtao.buildingidentification.Utils.Constant.selector;

public class surveyActivity extends AppCompatActivity implements IndicatorAdapter.OnIndictorTapbClickListene {

    private ViewPager mViewPager;
    private String[] mDataList;
    private final static String TAG = "surveyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager);
        mDataList = this.getApplicationContext().getResources().getStringArray(R.array.survey_tab_name);//survey_tab_name
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(SURVEY_PAGE,supportFragmentManager, mDataList.length);
        mViewPager.setAdapter(viewPagerAdapter);
        MagicIndicator mMagicIndicator = findViewById(R.id.magic_indicator);
        mMagicIndicator.setBackgroundColor(Color.WHITE);
        //创建适配器
        IndicatorAdapter indicatorAdapter = new IndicatorAdapter(this);
        indicatorAdapter.setOnIndicatorTabClicklistener(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(indicatorAdapter);
        //把ViewPager和indicator绑定到一起
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);

    }

    @Override
    public void onTabClick(int index) {
        mViewPager.setCurrentItem(index);
    }
}
