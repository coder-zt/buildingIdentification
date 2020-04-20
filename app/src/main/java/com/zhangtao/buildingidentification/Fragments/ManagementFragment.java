package com.zhangtao.buildingidentification.Fragments;

import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangtao.buildingidentification.Adapters.ItemInfoAdapter;
import com.zhangtao.buildingidentification.R;
import com.zhangtao.buildingidentification.Utils.DBDao;
import com.zhangtao.buildingidentification.elements.DataObject;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class ManagementFragment extends com.zhangtao.buildingidentification.Base.BaseFragment {

    public static final String TAG = "ManagementFragment";
    private View mView;
    private Button mQueryBtn;
    private RecyclerView mDataShowList;
    private ItemInfoAdapter mItemInfoAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {
        mView = LayoutInflater.from(container.getContext()).inflate(R.layout.mangement_layout, container, false);
            initView();
            initEvent();
        return mView;
    }

    private void initEvent() {
        mQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBDao dao = new DBDao(getContext());
                List<DataObject> infoList = dao.queryAll();
                Log.d(TAG, "onClick: " + infoList.size());
                Log.d(TAG, "onClick: " + infoList.toString());
                mItemInfoAdapter.setData(infoList);

            }
        });
    }

    private void initView() {
        mQueryBtn = mView.findViewById(R.id.query_all);
        mDataShowList = mView.findViewById(R.id.info_area);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataShowList.setLayoutManager(linearLayoutManager);
        mDataShowList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom =UIUtil.dip2px(view.getContext(), 5);
                outRect.left =UIUtil.dip2px(view.getContext(), 5);
                outRect.right =UIUtil.dip2px(view.getContext(), 5);
            }
        });
        mItemInfoAdapter = new ItemInfoAdapter();
        mDataShowList.setAdapter(mItemInfoAdapter);
    }
}
