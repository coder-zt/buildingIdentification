package com.zhangtao.buildingidentification.Views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.zhangtao.buildingidentification.R;

public class mainToolBar extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private View mView;
    private TextView mCurrentScale;
    private ImageView mEditBtn;
    private boolean mIsEdit = false;
    private onItemClicked mOnItemClicked;
    private ImageView mFinshedBtn;
    private ImageView mCreateBtn;

    public mainToolBar(Context context) {
        this(context, null);
    }

    public mainToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public mainToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.main_tool_bar, this, true);
        initView();
        initEvent();
    }

    private void initEvent() {
        mFinshedBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);
        mCreateBtn.setOnClickListener(this);
    }

    public void setScale(String result){
        mCurrentScale.setText("1:" + result);

    }

    private void initView() {
        mCurrentScale = mView.findViewById(R.id.scale_tv);
        mEditBtn = mView.findViewById(R.id.edit_btn);
        mFinshedBtn = mView.findViewById(R.id.finshed_btn);
        mCreateBtn = mView.findViewById(R.id.new_btn);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.edit_btn:
                mIsEdit = !mIsEdit;
                if(mIsEdit){
                    mCreateBtn.setImageResource(R.mipmap.newable);
                }else{
                    mCreateBtn.setImageResource(R.mipmap.newbtn);
                }
                isEdit(mIsEdit);
                break;
            case R.id.finshed_btn:
                mOnItemClicked.finishedWorkedClick();
                break;
            case R.id.new_btn:
                if(mIsEdit){
                    mOnItemClicked.createElement();
                }
                break;
        }
    }

    private void isEdit(boolean isEdit) {
        if(isEdit){
            mEditBtn.setImageResource(R.mipmap.edit);
            Toast.makeText(mContext, "开始编辑",Toast.LENGTH_SHORT).show();
        }else{
            mEditBtn.setImageResource(R.mipmap.unedit);
            Toast.makeText(mContext, "已结束编辑",Toast.LENGTH_SHORT).show();
        }
        mOnItemClicked.editBtnClick(isEdit);
    }
    public void setOnItemClickedListener(onItemClicked listen){
        mOnItemClicked = listen;
    }

    public interface onItemClicked {

        void editBtnClick(boolean isEdit);

        void finishedWorkedClick();

        void createElement();
    }
}
