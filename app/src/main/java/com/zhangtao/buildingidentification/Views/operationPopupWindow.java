package com.zhangtao.buildingidentification.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import com.zhangtao.buildingidentification.R;
import com.zhangtao.buildingidentification.interfaces.IOperation_Panel_Callback;
import com.zhangtao.buildingidentification.interfaces.IOperation_Panel_Event;

import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POINT;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POLYGON;

public class operationPopupWindow extends PopupWindow implements View.OnClickListener, IOperation_Panel_Event {

    private final View mPopView;
    private View mLinePanel;
    private IWindowDismissed mIWindowDismissed;
    private View mPointPanel;
    private View mPoloygnPanel;
    private ImageView mPointDeleteBtn;
    private IOperation_Panel_Callback mCallback;
    private RadioButton mRdUnitM;
    private ImageView mUpBtn;
    private ImageView mDownBtn;
    private ImageView mLeftBtn;
    private ImageView mRightBtn;
    private RadioButton mRdUnitCm;
    private RadioButton mRdUnitDm;
    private final Context mContext;
    private ImageView mLineAddBtn;

    public operationPopupWindow(Context context){
        //设置它宽高
        super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //这里要注意：设置setOutsideTouchable之前，先要设置：setBackgroundDrawable,
        //否则点击外部无法关闭pop.
//        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        setOutsideTouchable(true);
//        setTouchable(true);
//        setFocusable(true);
        mContext = context;
        //载进来View
        mPopView = LayoutInflater.from(context).inflate(R.layout.operation_pop, null);
        //设置内容
        setContentView(mPopView);
//        设置窗口进入和退出的动画
//        setAnimationStyle(R.style.pop_animation);
        initView();
        initEvent();
    }

    private void initEvent() {
        //点操作的点击事件
        mPointDeleteBtn.setOnClickListener(this);
        mUpBtn.setOnClickListener(this);
        mDownBtn.setOnClickListener(this);
        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        //线操作的事件
        mLineAddBtn.setOnClickListener(this);
    }
    private void initView() {
        //点的操作控件
        mLinePanel = mPopView.findViewById(R.id.line_panel);
        mPointDeleteBtn = mPopView.findViewById(R.id.delete_btn);
        mUpBtn = mPopView.findViewById(R.id.to_up);
        mDownBtn = mPopView.findViewById(R.id.to_down);
        mLeftBtn = mPopView.findViewById(R.id.to_left);
        mRightBtn = mPopView.findViewById(R.id.to_right);
        mRdUnitM = mPopView.findViewById(R.id.unit_m);
        mRdUnitDm = mPopView.findViewById(R.id.unit_dm);
        mRdUnitCm = mPopView.findViewById(R.id.unit_cm);
        //线的操作控件
        mPointPanel = mPopView.findViewById(R.id.point_panel);
        mLineAddBtn = mPopView.findViewById(R.id.btn_line_add);

        mPoloygnPanel = mPopView.findViewById(R.id.polygon_panel);
    }




    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.delete_btn:
                clickDelete();
                break;
            case R.id.to_right:
                clickOrientation(0);
                break;
            case R.id.to_down:
                clickOrientation(1);
                break;
            case R.id.to_left:
                clickOrientation(2);
                break;
            case R.id.to_up:
                clickOrientation(3);
                break;
            case R.id.btn_line_add:
                clickAddLineToMiddle();
                break;
        }


    }



    public void setWindowDismissedListener(IWindowDismissed listener){
        mIWindowDismissed = listener;
    }

    @Override
    public void clickDelete() {
        mCallback.clickDeleted();
    }

    @Override
    public void clickOrientation(int dir) {
        int unit = 0;
        if (mRdUnitM.isChecked()) {
            unit = 100;
        }
        if (mRdUnitDm.isChecked()) {
            unit = 10;
        }
        if (mRdUnitCm.isChecked()) {
            unit = 1;
        }
        if(unit == 0){
            Toast.makeText(mContext, "请选择单位!", Toast.LENGTH_LONG).show();
            return;
        }
        if (mCallback != null) {
            mCallback.clickOrientationed(dir,unit);
        }
    }

    @Override
    public void clickAddLineToMiddle() {
        if (mCallback != null) {
            mCallback.clickedAddLineToMiddle();
        }
    }

    @Override
    public void registerViewCallback(IOperation_Panel_Callback iOperation_panel_callback) {
        mCallback = iOperation_panel_callback;
    }

    @Override
    public void unRegisterViewCallback(IOperation_Panel_Callback iOperation_panel_callback) {

    }

    public interface IWindowDismissed{

        void dismissed();
    }

    public void setPanel(int type){
        mPointPanel.setVisibility(View.GONE);
        mLinePanel.setVisibility(View.GONE);
        mPoloygnPanel.setVisibility(View.GONE);
        switch (type){
            case TYPE_POINT :
                mPointPanel.setVisibility(View.VISIBLE);
                break;
            case TYPE_LINE :
                mLinePanel.setVisibility(View.VISIBLE);
                break;
            case TYPE_POLYGON :
                mPoloygnPanel.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}

