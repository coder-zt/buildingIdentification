package com.zhangtao.buildingidentification.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.zhangtao.buildingidentification.Utils.Constant.TABLE_INFO_NAME;

public class DBDao {
    public static final String TAG = "DBDao";
    private final Context mContext;
    private final DBSurveryInfo dbHlper;

    public DBDao(Context context){
        mContext =context;
        dbHlper = new DBSurveryInfo(context);
    }


    /**
     * 向数据库插入一个完整的调查信息
     * @param dataObject
     * @return
     */
    public boolean insert(DataObject dataObject){
        SQLiteDatabase db = dbHlper.getWritableDatabase();
        String sql = " insert into " + TABLE_INFO_NAME +  "(indexcode, code, name, time_year, time_month, time_day, eare_name, intime , local , infoJson , bitmapJson)values(?,?,?,?,?,?,?,?,?,?,?)";
        try{
            List<String> dataInfos = Arrays.asList(dataObject.getInfoObject().getStringArrayObject());
            List<String> arrList = new ArrayList(dataInfos);
            //加入入库时间

            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date(System.currentTimeMillis());
            arrList.add(formatter.format(date));
            //加入地址着落
            arrList.add("代优化");
            //加入其他JSON信息
            arrList.add(dataObject.getInfoJson());
            //加入Bitmap的字符JSON信息
            arrList.add(dataObject.getBitmapJson());
            Log.d(TAG, "insert: " + arrList.toString());
            db.execSQL(sql, arrList.toArray());
            Log.d(TAG, "insert: " + arrList.toString());
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //数据查询
    /**
     * 所用用户主要信息查询
     */
    public List<DataObject> queryAll(){
        SQLiteDatabase db = dbHlper.getWritableDatabase();
        String sql = " select * from " + TABLE_INFO_NAME ;
        List<DataObject> infoList = new ArrayList<>();
        try{
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                infoObject info = new infoObject();
                StringBuilder sbInfo = new StringBuilder();
                sbInfo.append("{");
                String[] fields = cursor.getColumnNames();
                int count = 0;
                for (String field : fields) {
                    int fieldIndex = cursor.getColumnIndex(field);
                    if(fieldIndex > 7 || fieldIndex < 1){
                        continue;
                    }
                    String name = cursor.getString(fieldIndex);
                    String item = "\"" + field + "\":\"" +  name+"\"";
                    sbInfo.append(item);
                    if(count < 6){
                        sbInfo.append(",");
                        count++;
                    }
                }
                sbInfo.append("}");
                Gson gson = new Gson();
                Log.d(TAG, "queryAll: " + sbInfo.toString());
                String name = cursor.getString(8);
                int id = cursor.getInt(0);
                info = gson.fromJson(sbInfo.toString(), infoObject.class);

                infoList.add( new DataObject(id, info, name));
            }
            cursor.close();
            db.close();
            return infoList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
    //根据Id查询用户的主要信息
    public DataObject queryForId(int id) {
        DataObject dataObject = null;
        SQLiteDatabase db = dbHlper.getWritableDatabase();
        String sql = " select * from " + TABLE_INFO_NAME  + " where _id ="  + id;
        try{
            Cursor cursor = db.rawQuery(sql,null);
            while(cursor.moveToNext()){
                infoObject info = new infoObject();
                StringBuilder sbInfo = new StringBuilder();
                sbInfo.append("{");
                String[] fields = cursor.getColumnNames();
                int count = 0;
                for (String field : fields) {
                    int fieldIndex = cursor.getColumnIndex(field);
                    if(fieldIndex > 7 || fieldIndex < 1){
                        continue;
                    }
                    String name = cursor.getString(fieldIndex);
                    String item = "\"" + field + "\":\"" +  name+"\"";
                    sbInfo.append(item);
                    if(count < 6){
                        sbInfo.append(",");
                        count++;
                    }
                }
                sbInfo.append("}");
                Gson gson = new Gson();
                Log.d(TAG, "queryAll: " + sbInfo.toString());
                int fieldIndex = cursor.getColumnIndex("infoJson");
                String strJSON = cursor.getString(fieldIndex);
                fieldIndex = cursor.getColumnIndex("bitmapJson");
                String bitmapJSON = cursor.getString(fieldIndex);
                info = gson.fromJson(sbInfo.toString(), infoObject.class);

                dataObject = new DataObject(info, strJSON, bitmapJSON);
            }
            cursor.close();
            db.close();
            return dataObject;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

}
