package com.zhangtao.buildingidentification.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zhangtao.buildingidentification.R;
import com.zhangtao.buildingidentification.Utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class TableBar extends LinearLayout {

    public static final String TAG = "TableBar";
    private onItemClick mCallback;
    private TextView tempTextBtn;
    private List<TextView> listBtns = new ArrayList<>();
    private Context mContext;
    private int currentPosition;

    public TableBar(Context context) {
        this(context, null);
    }

    public TableBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TableBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    private void initEvent() {

    }


    public void initView(String[] mDataList){
        int position = 0;
        for(String item : mDataList){
            final int  index = position;
            TextView itemBtn =(TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_text, this, false);
            LayoutParams itemParams = (LayoutParams)itemBtn.getLayoutParams();
            itemParams.weight = 1.0f;
            itemParams.height = DensityUtils.dp2px(mContext, 45);
//            itemParams.gravity= Gravity.CENTER_VERTICAL;
            itemBtn.setLayoutParams(itemParams);
            itemBtn.setText(item);
            itemBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.itemClick(index);
                        tempTextBtn= itemBtn;
                        flushStyleBtn();
                        currentPosition = index;
                    }
                }
            });
            listBtns.add(itemBtn);
            addView(itemBtn);
            position++;
        }
        tempTextBtn= listBtns.get(0);
        flushStyleBtn();
    }

    private void flushStyleBtn() {
        for(TextView item : listBtns){
            if (item == tempTextBtn) {
               item.setTextColor(mContext.getResources().getColor(R.color.black));
            }else{
                item.setTextColor(mContext.getResources().getColor(R.color.gray));
            }
        }
    }

    public void setOnItemClick(onItemClick listener){
        mCallback = listener;
    }

    public void setPosition(int position){
        listBtns.get(position).callOnClick();
    }
    public int getPosition(){
        return currentPosition;
    }

    public interface onItemClick{
        void itemClick(int position);
    }
}
