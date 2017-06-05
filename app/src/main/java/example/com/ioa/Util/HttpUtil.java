package example.com.ioa.Util;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Gunjan.K.Kumar on 28-05-2017.
 */
public class HttpUtil {

    private static final String POST_URL ="http://indooroutdoor.herokuapp.com/push" ;
    private static final String SYNC_URL ="http://indooroutdoor.herokuapp.com/stats/" ;

    public String sendPostRequest(String postdata){
        String response = null;

        BufferedReader reader=null;
        try {

            StringBuilder stringBuilder = new StringBuilder(POST_URL);
            stringBuilder.append("?record="+postdata);

            Log.d("Url:",stringBuilder.toString()+"");
           URL url = new URL(stringBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

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
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();

        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        return response;
    }

    public String sendSync(String postdata){
        String response = null;

        BufferedReader reader=null;
        try {

            StringBuilder stringBuilder = new StringBuilder(SYNC_URL);
            stringBuilder.append(""+postdata);
            Log.d("Url:",stringBuilder.toString()+"");
            Log.d("url",stringBuilder.toString());
            URL url = new URL(stringBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            int responseCode = conn.getResponseCode();
            Log.d("response code:",responseCode+"");
            InputStream IS=conn.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
            String line;
            line=bufferedReader.readLine();
            Log.d("line",line);
           /* if (responseCode == HttpsURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
               // String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();

            }*/
        }
        catch(Exception ex)
        {
            Log.d("error11",ex.toString());
            ex.printStackTrace();

        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        return response;
    }

}
