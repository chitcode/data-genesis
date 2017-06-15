package example.com.ioa;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import example.com.ioa.Util.Utils;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.sql.DriverManager.println;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

   private ImageView imgBalcony;
    private ImageView imgLiving;
    private ImageView imgOutdoor;
    private ImageView imgKitchen;
    private ImageView imgTickBalcony;
    private ImageView imgTickLiving;
    private ImageView imgTickOutdoor;
    private ImageView imgTickKitchen;
    private ImageView info_just_indoor,info_deep_indoor,info_deep_outdoor,info_just_outdoor;
    private Button btnSubmit;
    private ProgressDialog loading;
    private TextView current_position,next_away;
    private String selectedItem;
    private String output="";
    private JSONObject jsonObject;
    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    AsyncTaskSync1 async_obj;
    private String temp_path;
    Random random ;
    private Context ctx;
    private View view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303F9F")));
        initView();
        updateTextView();
        if(TextUtils.isEmpty(Utils.getDataFromSharedPref(this,Utils.NOTFICATION_TIME_KEY)))
        startAlarm();

      //  asyncTaskRecord=new AsyncTaskRecord();
        //asyncTaskRecord.execute();

    }
// TO DO MODIFY ACORDING TO NEW SETTING SCREEN
    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        long when = System.currentTimeMillis();         // notification time
        Intent intent = new Intent(this, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        long notificationgap= Long.parseLong(Utils.getDataFromSharedPref(this,Utils.NOTFICATION_TIME_KEY))*60*1000;
        alarmManager.setRepeating(AlarmManager.RTC, when,notificationgap, pendingIntent);

        Utils.getAllCellInfo(getApplicationContext());

    }

    private void stopAlarm() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        if(alarmManager!=null && pendingIntent!=null)
            alarmManager.cancel(pendingIntent);
    }


    private void initView(){

        random=new Random();
        imgBalcony=(ImageView)findViewById(R.id.img_balcony);
        imgKitchen=(ImageView)findViewById(R.id.img_kitchen);
        imgLiving=(ImageView)findViewById(R.id.img_living);
        imgOutdoor=(ImageView)findViewById(R.id.img_outdoor);
        imgTickBalcony=(ImageView)findViewById(R.id.tmg_tick_balcony);
        imgTickKitchen=(ImageView)findViewById(R.id.tmg_tick_kitchen);
        imgTickLiving=(ImageView)findViewById(R.id.tmg_tick_living);
        imgTickOutdoor=(ImageView)findViewById(R.id.tmg_tick_out);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        info_deep_indoor=(ImageView)findViewById(R.id.iv_info_deep_indoor);
        info_just_indoor=(ImageView)findViewById(R.id.iv_info_just_indoor);
        info_deep_outdoor=(ImageView)findViewById(R.id.iv_info_deep_outdoor);
        info_just_outdoor=(ImageView)findViewById(R.id.iv_info_just_outdoor);
        SeekBar mSeekbar = (SeekBar) findViewById(R.id.seekbar);
        final TextView txtv=(TextView)findViewById(R.id.txttime);
        current_position=(TextView) findViewById(R.id.tv_activity_home_next_current_pos);
        next_away=(TextView)findViewById(R.id.tv_activity_home_next_position);
        imgBalcony.setOnClickListener(this);
        imgKitchen.setOnClickListener(this);
        imgLiving.setOnClickListener(this);
        imgOutdoor.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        ctx=this;
        if(TextUtils.isEmpty(Utils.getDataFromSharedPref(this,Utils.NOTFICATION_TIME_KEY)))
        Utils.saveDataInPref(HomeActivity.this,"5",Utils.NOTFICATION_TIME_KEY);//default notification time
        else {
            mSeekbar.setProgress(Integer.parseInt(Utils.getDataFromSharedPref(this, Utils.NOTFICATION_TIME_KEY)));
        }

        txtv.setText("Notification Time: "+Utils.getDataFromSharedPref(this, Utils.NOTFICATION_TIME_KEY)+" Min");


        mSeekbar.setOnSeekBarChangeListener(new CustomSeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progress=progress+5;
                txtv.setText("Notification Time: "+progress+" Min");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress=seekBar.getProgress()+5;
                Utils.saveDataInPref(HomeActivity.this,progress+"",Utils.NOTFICATION_TIME_KEY);
                Toast.makeText(HomeActivity.this,"notification time set to "+  progress+" min",Toast.LENGTH_SHORT).show();
                txtv.setText("Notification Time: "+progress+" Min");
                stopAlarm();
                startAlarm();
            }
        });


        info_just_indoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //flag=2
                show_dialog(2);
            }
        });

        info_deep_indoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //flag=1
                show_dialog(1);

            }
        });

        info_deep_outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //flag=4
                show_dialog(4);

            }
        });

        info_just_outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //flag=3
                show_dialog(3);

            }
        });
    }

    public void show_dialog(int flag)
    {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_info);
        // Set dialog title
        // set values for custom dialog components - text, image and button
        final TextView info=(TextView) dialog.findViewById(R.id.tv_dialog_info);
        final TextView info1=(TextView) dialog.findViewById(R.id.tv_dialog_info_1);
        switch(flag)
        {
            case 1:
                info1.setVisibility(View.VISIBLE);
                info.setText("Bounded by multiple walls");
                info1.setText("Away from window ( more than 2 meters)");


                break;
            case 2:
                info.setText("Indoor but close to window pane (less than 2 meters)");
                info1.setVisibility(View.GONE);
                break;
            case 3:
                info1.setVisibility(View.VISIBLE);
                info.setText("Outdoor but close to building");
                info1.setText("Outdoor but surrounded by buildings");
                break;
            case 4:
                info.setText("On Road or open spaces ");
                info1.setVisibility(View.GONE);
                break;
        }

        Button ok;
        ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    public void captureRecord(String selectedItem)
    {
        if(null!=selectedItem) {
            Toast.makeText(this,"Your record is being sent",Toast.LENGTH_SHORT).show();
            Utils.performActionOnButtonClick(this, selectedItem);
            Toast.makeText(this,"record captured",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"please select any image",Toast.LENGTH_SHORT).show();

    }

    public void updateTextView()
    {

        String current_position_string,next_away_string;
        current_position_string=Utils.getDataFromSharedPref(this,"current_position");
        next_away_string=Utils.getDataFromSharedPref(this,"next_position_away");
        if(current_position_string!=null)
        current_position.setText("Current Position:"+current_position_string);
        if(next_away_string!=null)
        next_away.setText("Next Position away:"+next_away_string);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_balcony:
                selectedItem="Balcony";
                imgTickBalcony.setVisibility(View.VISIBLE);
                imgTickKitchen.setVisibility(View.GONE);
                imgTickLiving.setVisibility(View.GONE);
                imgTickOutdoor.setVisibility(View.GONE);
                start_record_send(v);
                break;
            case R.id.img_kitchen:
                selectedItem="Kitchen";
                imgTickBalcony.setVisibility(View.GONE);
                imgTickOutdoor.setVisibility(View.GONE);
                imgTickKitchen.setVisibility(View.VISIBLE);
                imgTickLiving.setVisibility(View.GONE);
                start_record_send(v);
                break;
            case R.id.img_living:
                selectedItem="Living";
                imgTickBalcony.setVisibility(View.GONE);
                imgTickKitchen.setVisibility(View.GONE);
                imgTickLiving.setVisibility(View.VISIBLE);
                imgTickOutdoor.setVisibility(View.GONE);
                start_record_send(v);
                break;
            case R.id.img_outdoor:
                selectedItem="OutDoor";
                imgTickBalcony.setVisibility(View.GONE);
                imgTickKitchen.setVisibility(View.GONE);
                imgTickLiving.setVisibility(View.GONE);
                imgTickOutdoor.setVisibility(View.VISIBLE);
                start_record_send(v);
                break;
           case R.id.btnSubmit:
            /* if(null!=selectedItem) {
                   Utils.performActionOnButtonClick(this, selectedItem);
                   Toast.makeText(this,"record captured",Toast.LENGTH_SHORT).show();
               }
               else
               Toast.makeText(this,"please select any image",Toast.LENGTH_SHORT).show();
*/


              String device_id=Utils.getDeviceid(getApplicationContext());
               async_obj=new AsyncTaskSync1();
               async_obj.execute(device_id);


               break;

        }

    }

    void start_record_send(View v)
    {
        this.view1=v;
        start_recording(view1);
        loading = ProgressDialog.show(ctx, "Status", "Sending Data...",true,false);

        countDownTimer.start();
    }
    class AsyncTaskSync1 extends AsyncTask<String, Void, String> {

        int flag = 0, flag1 = 1;

        public AsyncTaskSync1() {
            flag = 0;
            flag1 = 1;
        }

        @Override
        protected String doInBackground(String... params) {
            String device_id = params[0];
            String register_url = "http://indooroutdoor.herokuapp.com/stats/" + device_id;
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
                output=result;
                Log.d("output",output);
                decode_details(output);
            }
        }

        @Override
        protected void onPreExecute() {


        }
    }


    public void decode_details(String myJSON)
    {
        try{
            try {
                jsonObject=new JSONObject(myJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String status,current_position,next_position_away;
               next_position_away=jsonObject.getString("next_position_away");
                current_position=jsonObject.getString("current_position");
                status=jsonObject.getString("status");
                Utils.saveDataInPref(this,next_position_away,"next_position_away");
                Utils.saveDataInPref(this,current_position,"current_position");
                updateTextView();

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }


    public void start_recording(View view){

        String path;
        try {

            temp_path=MediaRecorderReady();
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
            Log.d("errorstart",e+"");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("errorstart",e+"");
        }
       Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }
    public void stop(View view){

        Toast.makeText(getApplicationContext(), "Stop recording...",
                Toast.LENGTH_SHORT).show();

        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;

            Toast.makeText(getApplicationContext(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Log.d("errorstop",e+"");
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Log.d("errorstop",e+"");
        }

    }
    public byte[] toByteArray(InputStream in) throws IOException {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_activity_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent=new Intent(getApplicationContext(),Settings.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    @Override
    protected void onStop()
    {
        if(async_obj!=null)
        {
            async_obj.cancel(true);
        }
        super.onStop();
    }


    CountDownTimer countDownTimer=  new CountDownTimer(3000,1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            // do when countdown timer is started like show progressbar

        }

        @Override
        public void onFinish() {

            loading.cancel();

            Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_LONG).show();
            stop(view1);

            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(Uri.fromFile(new File(temp_path)));
                byte[] soundBytes = new byte[inputStream.available()];

                soundBytes = toByteArray(inputStream);
                String base64 = Base64.encodeToString(soundBytes, Base64.DEFAULT);
                Log.d("bytes1",soundBytes.toString()+" "+base64);

                Utils.saveDataInPref(ctx,base64,Utils.FILE_BASE64_KEY);
                captureRecord(selectedItem);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //write what ever you want to do after completing time like hiding progress bar
        }
    };

    String AudioSavePathInDevice = null;
    public String MediaRecorderReady(){

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

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}


