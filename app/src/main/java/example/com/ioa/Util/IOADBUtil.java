package example.com.ioa.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import example.com.ioa.Pojo.IOAData;

/**
 * Created by Gunjan.K.Kumar on 28-05-2017.
 */
public class IOADBUtil extends DbConstant  {
    private final IOADb mdb;
    private Context mContext;


    public IOADBUtil(Context context){
        this.mContext=context;
        mdb = new IOADb(mContext);
    }



    public void addIOAData(String enqJson) {
        SQLiteDatabase db=mdb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_JSOON_DATA,enqJson);
      //  values.put(KEY_SUBMIT_TIME,System.currentTimeMillis()+"");

        // Inserting Row
        db.insert(TABLE_IOA, null, values);
        db.close(); // Closing database connection
    }


    public ArrayList<IOAData> getIOADATA(){
        ArrayList<IOAData> arrIOAData=new ArrayList<IOAData>();
        SQLiteDatabase db=mdb.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+TABLE_IOA+ " Order By "+KEY_ID+" DESC",null);
        if(cursor.moveToFirst()){
            do{
                IOAData data = new IOAData();
                String appid = cursor.getString(cursor.getColumnIndex(KEY_ID));
                String ioaData = cursor.getString(cursor.getColumnIndex(KEY_JSOON_DATA));

                data.setId(appid);
                data.setJsonData(ioaData);

                arrIOAData.add(data);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return  arrIOAData;
    }


    public void deleteRecord(int enqId) {
        SQLiteDatabase db = mdb.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_IOA+" WHERE "+KEY_ID+ " = '" + enqId + "'");
        db.close();
    }
}
