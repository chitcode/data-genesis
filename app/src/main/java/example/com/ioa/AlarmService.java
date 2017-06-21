package example.com.ioa;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import example.com.ioa.Util.Utils;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Gunjan.K.Kumar on 27-05-2017.
 */
public class AlarmService extends IntentService {

    public AlarmService() {
        super("AlarmService");
    }

    private static String selected_item;
    private static Context context1;
    @Override
    protected void onHandleIntent(Intent intent) {
        createDownloadNotification();
    }

    private static MediaRecorder myRecorder;


    private void createDownloadNotification() {
        setNotificationHandler(R.id.btn1);
        setNotificationHandler(R.id.btn2);
        setNotificationHandler(R.id.btn3);
        setNotificationHandler(R.id.btn4);
    }


    private void setNotificationHandler(int btnid){
        int notificationid=1;
        Intent settingsIntent = new Intent(Context.NOTIFICATION_SERVICE);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putInt("btnclick", btnid);
        settingsBundle.putInt("notificationid", notificationid);
        settingsIntent.putExtras(settingsBundle);
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification);

        NotificationCompat.Builder builder = (NotificationCompat.Builder)
                new NotificationCompat.Builder(this).setSmallIcon(R.drawable.app_logo).
                        setTicker("Ticker Text").setContent(notificationView) .setAutoCancel(true);

        PendingIntent pendingsettingsIntent =PendingIntent.
                getBroadcast(getBaseContext(), btnid, settingsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationView.setOnClickPendingIntent(btnid, pendingsettingsIntent);
        notificationManager.notify(notificationid, builder.build());
    }



    public static class DownloadCancelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle answerBundle = intent.getExtras();
            Log.d("buttonclick","1");
            int btnclick = answerBundle.getInt("btnclick");
            int notificationid = answerBundle.getInt("notificationid");
            context1=context;
            switch (btnclick){
                case R.id.btn1:
                    Log.d("buutton click","1");
                    selected_item="Deep Indoor";
                    start_record_send();
                    //  Utils.performActionOnButtonClick(context,"Deep Indoor");
                    break;
                case R.id.btn2:
                    selected_item="Just indoor";
                    start_record_send();
                    //    Utils.performActionOnButtonClick(context,"Just Indoor");
                    break;
                case R.id.btn3:
                    Log.d("button click","3");
                    selected_item="Just Outdoor";
                    start_record_send();
                    //        Utils. performActionOnButtonClick(context,"Just Outdoor");
                    break;
                case R.id.btn4:

                    Log.d("button click","4");
                    selected_item="Deep Outdoor";
                    start_record_send();
                    Toast.makeText(context,"start re",Toast.LENGTH_LONG).show();
              //      Utils.performActionOnButtonClick(context,"Deep Outdoor");
                    break;
            }
            NotificationManager notificationManager =  (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationid);
        }
    }


    private static String temp_path;
    static void  start_record_send()
    {

        Log.d("heree","1");
        start_recording();
        long current_timestamp= System.currentTimeMillis();
        Utils.saveDataInPref(context1,current_timestamp+"",Utils.LAST_DATA_SENT_TIMESTAMP);
        //loading = ProgressDialog.show(ctx, "Status", "Sending Data...",true,false);

        countDownTimer.start();
    }

    public static void start_recording(){

        Log.d("heree","2");
        String path;
        try {

            temp_path=MediaRecorderReady();
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            Log.d("heree","3");
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
            Log.d("errorstart",e+"");
        } catch (IOException e) {
            Log.d("heree","4");
            e.printStackTrace();
            Log.d("errorstart",e+"");
        }
        ///  Toast.makeText(getApplicationContext(), "Start recording...",
        //        Toast.LENGTH_SHORT).show();
    }
    public static void stop(){

        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;
            Log.d("heree","6");

            //  Toast.makeText(getApplicationContext(), "Stop recording...",
            //        Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Log.d("errorstop",e+"");
            Log.d("heree","7");
            e.printStackTrace();
        } catch (RuntimeException e) {
            Log.d("heree","8");
            e.printStackTrace();
            Log.d("errorstop",e+"");
        }

    }
    static  String AudioSavePathInDevice = null;
    public static String MediaRecorderReady(){

        if(checkPermission()) {
            Log.d("abcabc1","1");
            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            "AudioRecording.mp3";
            myRecorder = new MediaRecorder();
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
            myRecorder.setOutputFile(AudioSavePathInDevice);
        }
        return AudioSavePathInDevice;
    }

    public static boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context1,
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context1,
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

   static CountDownTimer countDownTimer=  new CountDownTimer(3000,1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            // do when countdown timer is started like show progressbar

        }

        @Override
        public void onFinish() {

            stop();
            Log.d("recording","stop");

            InputStream inputStream;
            try {
                inputStream = context1.getContentResolver().openInputStream(Uri.fromFile(new File(temp_path)));
                byte[] soundBytes = new byte[inputStream.available()];

                soundBytes = toByteArray(inputStream);
                String base64 = Base64.encodeToString(soundBytes, Base64.DEFAULT);
                Log.d("bytes1",soundBytes.toString()+" "+base64);

                File file=new File(Environment.getExternalStorageDirectory(),temp_path);
                if(file.exists())
                {
                    Log.d("filestatus","deleted");
                    file.delete();
                }
                Utils.saveDataInPref(context1,base64,Utils.FILE_BASE64_KEY);
                Utils.performActionOnButtonClick(context1,selected_item);

                /// captureRecord(selectedItem);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //write what ever you want to do after completing time like hiding progress bar
        }
    };

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer,0,read);
        }
        out.close();
        return out.toByteArray();
    }


}