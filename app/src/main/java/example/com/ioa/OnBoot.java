package example.com.ioa;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import example.com.ioa.Util.Utils;

/**
 * Created by VIVEK on 22-06-2017.
 */

public class OnBoot extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        stopAlarm(context);
        startAlarm(context);

    }
    private void startAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        long when = System.currentTimeMillis();         // notification time
        Intent intent = new Intent(context, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        long notificationgap= Long.parseLong(Utils.getDataFromSharedPref(context,Utils.NOTFICATION_TIME_KEY))*60*1000;
        alarmManager.setRepeating(AlarmManager.RTC, when,notificationgap, pendingIntent);
        Utils.getAllCellInfo(context);

    }

    private void stopAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        if(alarmManager!=null && pendingIntent!=null)
            alarmManager.cancel(pendingIntent);
    }

}