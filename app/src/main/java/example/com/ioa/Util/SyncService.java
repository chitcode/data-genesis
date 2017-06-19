package example.com.ioa.Util;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import example.com.ioa.Pojo.IOAData;

/**
 * Created by Gunjan.K.Kumar on 28-05-2017.
 */
public class SyncService  extends IntentService {

    private Context ctx;
    public SyncService() {
        super("SyncService");
    }
    private String inserted_id="";
    public SyncService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<IOAData> listData = new IOADBUtil(getApplicationContext()).getIOADATA();
        if (listData != null && listData.size() > 0) {
            for (IOAData data : listData) {
                if (null != data) {
                    Log.d("sending record id", "" + data.getId());
                    String res = new HttpUtil(getApplicationContext()).sendPostRequest(data.getJsonData());
                    ctx=getApplicationContext();


                     if (res != null && res.contains("status") && res.contains("success")) {
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
