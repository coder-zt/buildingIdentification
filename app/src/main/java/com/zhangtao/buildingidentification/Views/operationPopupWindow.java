package com.zhangtao.buildingidentification.Views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangtao.buildingidentification.R;
import com.zhangtao.buildingidentification.Utils.DensityUtils;
import com.zhangtao.buildingidentification.elements.BDPoint;
import com.zhangtao.buildingidentification.interfaces.IOperationPanelCallback;
import com.zhangtao.buildingidentification.interfaces.IOperation_Panel_Event;

import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_CLOSE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_CREATE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_LINE;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_POINT;
import static com.zhangtao.buildingidentification.Utils.Constant.TYPE_NOTE;

public class operationPopupWindow extends PopupWindow implements View.OnClickListener, IOperation_Panel_Event {

    public static final String TAG = "operationPopupWindow";
    private final View mPopView;
    private View mLinePanel;
    private IWindowDismissed mIWindowDismissed;
    private View mPointPanel;
    private View mCreatePanel;
    private View mNotePanel;
    private ImageView mPointDeleteBtn;
    private IOperationPanelCallback mCallback;
    private RadioButton mRdUnitM;
    private ImageView mUpBtn;
    private ImageView mDownBtn;
    private ImageView mLeftBtn;
    private ImageView mRightBtn;
    private RadioButton mRdUnitCm;
    private RadioButton mRdUnitDm;
    private final Context mContext;
    private ImageView mLineAddBtn;
    private EditText mBoundaryIndex;
    private CheckBox mIsBoundary;
    private TextView mCreateNote;
    private TextView mCreateUncloseLine;
    private TextView mCreateCloseLine;
    private TextView mSelectTv;
    private EditText mAddType;
    private TextView mGetLenBtn;
    private TextView mLenTextTv;
    private Button mAddMiddlePoint;
    private Button mCompleteBtn;
    private PopupWindow mTypePopupWindow;
    private BDNumber mSetSize;

    public operationPopupWindow(Context context){
        //设置它宽高
        super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //这里要注意：设置setOutsideTouchable之前，先要设置：setBackgroundDrawable,
        //否则点击外部无法关闭pop.
//        setBackgroundDrawable(newbtn ColorDrawable(Color.TRANSPARENT));
//        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(true);
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
//        mLineAddBtn.setOnClickListener(this);
        mSelectTv.setOnClickListener(this);
        mGetLenBtn.setOnClickListener(this);
        mAddMiddlePoint.setOnClickListener(this);
        mCompleteBtn.setOnClickListener(this);
        //新建
        mCreateCloseLine.setOnClickListener(this);
        mCreateUncloseLine.setOnClickListener(this);
        mCreateNote.setOnClickListener(this);
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
        mIsBoundary = mPopView.findViewById(R.id.is_single);
        mBoundaryIndex = mPopView.findViewById(R.id.boundary_index_et);
        mIsBoundary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mBoundaryIndex.setVisibility(View.VISIBLE);
                    //优化，index自动增加
                }else{
                    mBoundaryIndex.setVisibility(View.INVISIBLE);
                }
            }
        });
        //完成按钮
        Button compleBtn = mPopView.findViewById(R.id.comple_btn);
        compleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBoundary.isChecked()) {
                    mCallback.clickCompleted(mBoundaryIndex.getText().toString());
                }else{
                    mCallback.clickCompleted(null);
                }
                dismiss();
            }
        });
        //线的操作控件
        mPointPanel = mPopView.findViewById(R.id.point_panel);
        mSelectTv = mPopView.findViewById(R.id.select_btn);
        mAddType = mPopView.findViewById(R.id.type_text);
        mGetLenBtn = mPopView.findViewById(R.id.get_len_btn);
        mLenTextTv = mPopView.findViewById(R.id.length_text);
        mAddMiddlePoint = mPopView.findViewById(R.id.create_middle_point);
        mCompleteBtn = mPopView.findViewById(R.id.complete_btn);
//        mLineAddBtn = mPopView.findViewById(R.id.btn_line_add);
        //注解的操作控件
        mNotePanel = mPopView.findViewById(R.id.note_panel);
        mSetSize = mPopView.findViewById(R.id.set_size);
        mSetSize.init("大小", 10, 0, 50);

        BDNumber setAngle = mPopView.findViewById(R.id.set_angle);
        setAngle.init("角度", 0, -180, 180);
        BDNumber setpadding = mPopView.findViewById(R.id.set_padding);
        setpadding.init("字间距", 0, 0, 50);
        //新建
        mCreatePanel = mPopView.findViewById(R.id.create_panel);
        mCreateCloseLine = mPopView.findViewById(R.id.line_close_btn);
        mCreateUncloseLine = mPopView.findViewById(R.id.line_btn);
        mCreateNote = mPopView.findViewById(R.id.note_btn);
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
            //创建新元素面板的事件
            case R.id.line_btn:
                mCallback.createElement(TYPE_LINE);
                dismiss();
                break;
            case R.id.line_close_btn:
                mCallback.createElement(TYPE_CLOSE_LINE);
                dismiss();
                break;
            case R.id.note_btn:
                mCallback.createElement(TYPE_NOTE);
                dismiss();
                break;
            case R.id.select_btn:
                showLineType(v);
                break;
            case R.id.get_len_btn:
                getLineLength();
                break;
            case R.id.create_middle_point:
                CreateMiddlePoint();
                break;
            case R.id.complete_btn:
                if (mCallback != null) {
                    String textBtn = mSelectTv.getText().toString();
                    if(!TextUtils.equals(textBtn, "点击选择")){
                        if(TextUtils.equals(textBtn, "其他")){
                            mCallback.commitInfoToLine(mAddType.getText().toString());
                        }else{
                            mCallback.commitInfoToLine(textBtn);
                        }
                    }
                }
                dismiss();
                break;
        }


    }

    /**
     * 在当前线段中插入一个点
     */
    private void CreateMiddlePoint() {
        if (mCallback != null) {
            mCallback.CreatePointOnLine();
            dismiss();
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        if (mTypePopupWindow.isShowing()) {
            mTypePopupWindow.dismiss();
        }
    }

    /**
     * 获取当前线段的长度
     */
    private void getLineLength() {
        if (mCallback != null) {
            float len = mCallback.getCurrentLineLength();
            String result = String.format("%.3f",len);
            mLenTextTv.setText(result + "m");
        }
    }

    /**
     * 设置多线段类型
     * @param view
     */
    private void showLineType(View view) {
        String[] types = mContext.getResources().getStringArray(R.array.line_type_name);
        mTypePopupWindow = new PopupWindow();
        mTypePopupWindow.setWidth(view.getWidth());
        mTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup mView = (ViewGroup)LayoutInflater.from(mContext).inflate(R.layout.pop_type, null, false);

        int index = 0;
        for (String type : types) {
            TextView tv = new TextView(mContext);
            tv.setText(type);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(DensityUtils.sp2px(mContext,8));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTv = (TextView)view;
                    TextView textView = (TextView)v;
                    mTv.setText(textView.getText());
                    if (TextUtils.equals(textView.getText(), "其他")) {
                        mAddType.setVisibility(View.VISIBLE);
                    }else{
                        mAddType.setVisibility(View.INVISIBLE);
                    }
                    mTypePopupWindow.dismiss();
                }
            });
            View hView = new View(mContext);
            hView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, 0.5f)));
            hView.setBackgroundColor(Color.BLACK);
            if(index < types.length){
                mView.addView(hView);
            }
            mView.addView(tv);
            index++;
        }
        mTypePopupWindow.setContentView(mView);
        mTypePopupWindow.showAsDropDown(view);
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
    public void registerViewCallback(IOperationPanelCallback listener) {
        mCallback = listener;
        mSetSize.setOnTypeValueChange(mCallback);
    }

    @Override
    public void unRegisterViewCallback(IOperationPanelCallback iOperation_panel_callback) {

    }

    public interface IWindowDismissed{
        void dismissed();
    }

    /**
     * 设置当前的操作面板
     * @param type
     * @param point
     */
    public void setPanel(int type, BDPoint point){
        mPointPanel.setVisibility(View.GONE);
        mLinePanel.setVisibility(View.GONE);
        mCreatePanel.setVisibility(View.GONE);
        mNotePanel.setVisibility(View.GONE);
        switch (type){
            case TYPE_POINT :
                mPointPanel.setVisibility(View.VISIBLE);
                if (point != null && point.isBoundary()) {
                    mIsBoundary.setChecked(true);
                    mBoundaryIndex.setText(point.getIndex());
                }else{
                    mIsBoundary.setChecked(false);
                }
                break;
            case TYPE_LINE :
                mLinePanel.setVisibility(View.VISIBLE);
                break;
            case TYPE_NOTE:
                mNotePanel.setVisibility(View.VISIBLE);
                break;
            case TYPE_CREATE:
                mCreatePanel.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}

