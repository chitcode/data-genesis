package example.com.ioa;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import example.com.ioa.Util.Utils;

public class Enter_Phone_Number extends AppCompatActivity {


    private Button submit;
    private EditText phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_phone_number);
        phone_number=(EditText)findViewById(R.id.et_phone_number);
        submit=(Button)findViewById(R.id.btn_next);


        if(Utils.getPhoneNumber(getApplicationContext())==null) {
                if (Utils.getDataFromSharedPref(getApplicationContext(), Utils.PHONE_NUMBER) == null) {

                } else if (Utils.getDataFromSharedPref(getApplicationContext(), Utils.PHONE_NUMBER).equals(null)) {

                } else {
                    nextPage();
                }
        }
        else if(!Utils.getPhoneNumber(getApplicationContext()).equals(""))
            {
            String phone_number=Utils.getPhoneNumber(getApplicationContext());
                Toast.makeText(getApplicationContext(),"Number found"+phone_number,Toast.LENGTH_LONG).show();
            Utils.saveDataInPref(getApplicationContext(),phone_number,Utils.PHONE_NUMBER);
            nextPage();
        }
        else
        {
            if(Utils.getDataFromSharedPref(getApplicationContext(), Utils.PHONE_NUMBER)!=null) {
                if(!Utils.getDataFromSharedPref(getApplicationContext(), Utils.PHONE_NUMBER).equals(""))
                nextPage();
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(phone_number.getText().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter mobile number!",Toast.LENGTH_LONG).show();
                }
                else if(phone_number.getText().length()>10)
                {
                    Toast.makeText(getApplicationContext(),"Not a valid number",Toast.LENGTH_LONG).show();
                }
                else if(phone_number.getText().length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Not a valid number",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Utils.saveDataInPref(getApplicationContext(),phone_number.getText().toString(),Utils.PHONE_NUMBER);
                    nextPage();
                }
            }
        });
    }

    private void nextPage()
    {

        if(Utils.getDataFromSharedPref(getApplicationContext(),Utils.NOTFICATION_TIME_KEY)==null)
        {
            Utils.saveDataInPref(getApplicationContext(),"5",Utils.NOTFICATION_TIME_KEY);
            startAlarm();
        }

        if(Utils.getDataFromSharedPref(getApplicationContext(),Utils.PHONE_NUMBER)!=null) {
            Log.d("phone_nuber", Utils.getDataFromSharedPref(getApplicationContext(), Utils.PHONE_NUMBER));
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        long when = System.currentTimeMillis();         // notification time
        Intent intent = new Intent(this, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        Log.d("alarmstart","alarmstart");
        long notificationgap;
        if(Utils.getDataFromSharedPref(getApplicationContext(),Utils.NOTFICATION_TIME_KEY)!=null) {
            notificationgap = Long.parseLong(Utils.getDataFromSharedPref(this, Utils.NOTFICATION_TIME_KEY)) * 60 * 1000;
            alarmManager.setRepeating(AlarmManager.RTC, when, notificationgap, pendingIntent);
            Utils.getAllCellInfo(getApplicationContext());
        }

    }

}

