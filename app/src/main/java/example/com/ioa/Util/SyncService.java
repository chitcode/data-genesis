package example.com.ioa.Util;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import example.com.ioa.Pojo.IOAData;

/**
 * Created by Gunjan.K.Kumar on 28-05-2017.
 */
public class SyncService  extends IntentService {

    public SyncService() {
        super("SyncService");
    }

    public SyncService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<IOAData> listData = new IOADBUtil(getApplicationContext()).getIOADATA();
        if (listData != null && listData.size() > 0) {
            for (IOAData data : listData) {
                if (null != data) {
                    Log.d("sending record id",""+data.getId());
                    String res = new HttpUtil().sendPostRequest(data.getJsonData());
                     if (res != null && res.contains("{'status':'success'}")) {
                         Log.d("delete record id",""+data.getId());
                         new IOADBUtil(getApplicationContext()).deleteRecord(Integer.parseInt(data.getId()));
                     }
                    else{
                         Log.d("error send data id",""+data.getId());
                     }
                }
            }
        }
    }


}
