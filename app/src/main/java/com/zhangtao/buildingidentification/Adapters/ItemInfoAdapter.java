package com.zhangtao.buildingidentification.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangtao.buildingidentification.R;
import com.zhangtao.buildingidentification.elements.DataObject;
import com.zhangtao.buildingidentification.surveyActivity;

import java.util.ArrayList;
import java.util.List;

import static com.zhangtao.buildingidentification.Utils.Constant.INTENT_TO_SURVEY;

public class ItemInfoAdapter extends RecyclerView.Adapter<ItemInfoAdapter.ItemHolder> {

    public static final String TAG = "ItemInfoAdapter";
    private List<DataObject> mData = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public ItemInfoAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemInfoAdapter.ItemHolder holder, int position) {
        holder.createView(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, surveyActivity.class);
                intent.putExtra(INTENT_TO_SURVEY, mData.get(position).get_id());
                mContext.startActivity(intent);
                Log.d(TAG, "onClick: " +  mData.get(position).get_id());
            }
        });
    }

    public void setData(List<DataObject> data){
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        if(mData != null ){
            return mData.size();
        }
        return 0;
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        ItemHolder(@NonNull View itemView) {
            super(itemView);
        }

        void createView(DataObject dataObject){
            TextView itemName = itemView.findViewById(R.id.name_tv);
            itemName.setText( "土地权利人：" + dataObject.getInfoObject().getName());
            TextView itemCode = itemView.findViewById(R.id.code_tv);
            itemCode.setText( "宗地代码：" + dataObject.getInfoObject().getCode());
            TextView itemTime = itemView.findViewById(R.id.time_tv);
            itemTime.setText(   "调查时间：" + dataObject.getInfoObject().getTime_year() +
                                "-" + dataObject.getInfoObject().getTime_month() + "-" +
                                dataObject.getInfoObject().getTime_day());
            TextView itemArea = itemView.findViewById(R.id.area_tv);
            itemArea.setText( "所属区县：" + dataObject.getInfoObject().getEare_name());
            TextView itemIndex = itemView.findViewById(R.id.index_tv);
            itemIndex.setText( "编号：" + dataObject.getInfoObject().getIndexcode());
        }
    }
}
