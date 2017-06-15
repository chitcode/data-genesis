package example.com.ioa.Util;

import android.util.Log;
import android.widget.Toast;

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

   // private static final String POST_URL ="http://192.168.43.79:5000/push" ;
    private static final String POST_URL ="http://indooroutdoor.herokuapp.com/push" ;
    private static final String SYNC_URL ="http://indooroutdoor.herokuapp.com/stats/" ;

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

            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        return response;
    }


}
