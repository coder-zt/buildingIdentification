package com.zhangtao.buildingidentification.Fragments;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ManagementFragment extends com.zhangtao.buildingidentification.Base.BaseFragment {

    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {
        TextView textView = new TextView(container.getContext());
        textView.setText("Fragment-数据管理");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(24);
        container.addView(textView);
        return textView;
    }
}
