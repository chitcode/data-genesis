package example.com.ioa;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import example.com.ioa.Util.Utils;

/**
 * Created by Gunjan.K.Kumar on 27-05-2017.
 */
public class AlarmService extends IntentService {

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        createDownloadNotification();
    }


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
            switch (btnclick){
                case R.id.btn1:
                    Log.d("buutton click","1");
                    Utils.performActionOnButtonClick(context,"Deep Indoor");
                    break;
                case R.id.btn2:
                    Utils.performActionOnButtonClick(context,"Just Indoor");
                    break;
                case R.id.btn3:
                    Log.d("buutton click","3");
                    Utils. performActionOnButtonClick(context,"Just Outdoor");
                    break;
                case R.id.btn4:
                    Log.d("buutton click","4");
                    Utils.performActionOnButtonClick(context,"Deep Outdoor");
                    break;
            }
            NotificationManager notificationManager =  (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationid);
        }
    }
}