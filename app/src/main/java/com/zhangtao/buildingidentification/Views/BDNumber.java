package com.zhangtao.buildingidentification.Views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zhangtao.buildingidentification.R;

public class BDNumber extends RelativeLayout {

    public static final String TAG = "BDNumber";
    private Context mContext;
    private View mView;
    private EditText mValueArea;
    private ImageView mPulsBtn;
    private ImageView mReduceBtn;
    private TextView mTitle;
    private int mValue;
    private int mMaxValue;
    private int mMinValue;
    private String mName;
    private boolean mIsAuto = true;
    private onTypeValueChange mCallback;

    public void init(String title, int defValue, int minValue , int maxValue){
        mName = title;
        mValue = defValue;
        mMaxValue = maxValue;
        mMinValue = minValue;
        mTitle.setText(mName);
        mValueArea.setText(Integer.toString(mValue));
    }
    public BDNumber(Context context) {
        this(context, null);
    }

    public BDNumber(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BDNumber(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.bd_view_number, this, true);

        initView();
        initEvent();
    }

    private void initEvent() {
        mReduceBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mValue >= mMinValue){
                    mValue--;
                    mValueArea.setText(Integer.toString(mValue));
                }
            }
        });

        mPulsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mValue <= mMaxValue){
                    mValue++;
                    mValueArea.setText(Integer.toString(mValue));
                }

            }
        });
        mValueArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    try{
                        int tempValue = Integer.parseInt(s.toString());
                        if(tempValue > mMaxValue){
                            mValue = mMaxValue;
                            mIsAuto = false;
                        }else if(tempValue < mMinValue){
                            mValue = mMinValue;
                            mIsAuto = false;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                if(!mIsAuto){
                    mIsAuto = true;
                    Log.d(TAG, "onTextChanged: " + Integer.toString(mValue) + mIsAuto);
                    mValueArea.setText(Integer.toString(mValue));

                }else{
                    if (mCallback != null) {
                        mCallback.valueChange(mName, mValue);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        mTitle = findViewById(R.id.title);
        mTitle.setText(mName);
        mReduceBtn = findViewById(R.id.reduce_btn);
        mPulsBtn = findViewById(R.id.puls_btn);
        mValueArea = findViewById(R.id.value);
    }

    public int getValue() {
        return mValue;
    }

    public void setOnTypeValueChange(onTypeValueChange listener){
        mCallback = listener;
    }

    public interface onTypeValueChange{
        void valueChange(String name, int value);
    }
}
