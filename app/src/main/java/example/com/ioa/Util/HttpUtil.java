package example.com.ioa.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Gunjan.K.Kumar on 28-05-2017.
 */
public class HttpUtil {

    private AsyncTaskSyncUndo asyncTaskSyncUndo;
    private static final String POST_URL ="http://192.168.43.79:5000/push" ;
   // private static final String POST_URL ="http://indooroutdoor.herokuapp.com/push" ;
    private static final String SYNC_URL ="http://indooroutdoor.herokuapp.com/stats/" ;
    private Context context;
    private String inserted_id;
    public HttpUtil(Context context)
    {
        this.context=context;
    }
    public String sendPostRequest(String postdata){
        String response = null;

        BufferedReader reader=null;
        try {

            StringBuilder stringBuilder = new StringBuilder(POST_URL);
          //  stringBuilder.append("?record="+postdata);
            Log.d("postdata",postdata);
            Log.d("Url:",stringBuilder.toString()+"");
            URL url = new URL(stringBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            OutputStream OS=conn.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            String data= /*URLEncoder.encode("record","UTF-8")+"="+URLEncoder.encode(postdata,"UTF-8");*/postdata;

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            int responseCode = conn.getResponseCode();
            Log.d("response code:",responseCode+"");
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();
                Log.d("urlresponse",response+"abc");
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.d("urlresponse","error cathc"+ex);


        }
        finally
        {
            Log.d("urlresponse","final");
            if(response!=null)
            {

                if(Utils.getDataFromSharedPref(context,Utils.UNDO_OPTION)!=null) {
                    if (Utils.getDataFromSharedPref(context, Utils.UNDO_OPTION).equals("1")) {
                        Utils.saveDataInPref(context, "0", Utils.UNDO_OPTION);
                        inserted_id=decode_details(response);
                        asyncTaskSyncUndo=new AsyncTaskSyncUndo();
                        asyncTaskSyncUndo.execute(inserted_id);

                    }
                }

            }
            try
            {
                reader.close();
            }

            catch(Exception ex) {}
        }

        return response;
    }


    class AsyncTaskSyncUndo extends AsyncTask<String, Void, String> {

        int flag = 0, flag1 = 1;

        public AsyncTaskSyncUndo() {
            flag = 0;
            flag1 = 1;
        }

        @Override
        protected String doInBackground(String... params) {
            String inserted_id = params[0];
            String register_url = "http://192.168.43.79:5000/undo/" + inserted_id;
            //String register_url = "http://indooroutdoor.herokuapp.com/undo/" + inserted_id;
            Log.d("url1", register_url);

            try {
                URL url = new URL(register_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setDoInput(true);
                String line = "nothing";
                int responseCode = conn.getResponseCode();
                Log.d("response code:", responseCode + "");
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("response code:", responseCode + "");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    line = sb.toString();
                }
                return line;
            } catch (Exception e) {
                flag1 = 0;
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (flag1 != 0) {
            }
        }

        @Override
        protected void onPreExecute() {

        }
    }

    JSONObject jsonObject;
    public String decode_details(String myJSON)
    {
        try{
            try {
                jsonObject=new JSONObject(myJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String status,inserted_id;
            inserted_id=jsonObject.getString("inserted_id");
            status=jsonObject.getString("status");
            Log.d("inserted_id",inserted_id);
            Utils.saveDataInPref(context,inserted_id,Utils.LAST_INSERT_ID);
            return inserted_id;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return "";
    }


}
