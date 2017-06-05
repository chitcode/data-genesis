package example.com.ioa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import example.com.ioa.Util.Utils;

public class Settings extends AppCompatActivity {

    Calendar myCalendar;
    private int calender_flag,flag_time;//1-calender1 2-calender2 1=time1 2=time2
    private EditText time1,time2;
    private SeekBar seekBar_notification,seekBar_capture_duration;
    private TextView notification_text,duration_text;
    private RadioButton radioButton_point,radioButton_test;
    private Button save,select_day;
    private ArrayList<String> selected_day_array;
    private String json_day=null;
    private Boolean boolean_notification_on_off;
    private CheckBox cb_notification_on_off;
    private String start_date,end_date,start_time,end_time,notification_value,capture_duration,capture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myCalendar = Calendar.getInstance();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303F9F")));

        save=(Button)findViewById(R.id.btn_save);
        time1=(EditText)findViewById(R.id.et_time_start);
        cb_notification_on_off=(CheckBox)findViewById(R.id.checkbox_notification_on_off);
        select_day=(Button)findViewById(R.id.btn_select_date);
        time2=(EditText)findViewById(R.id.et_time_end);
        radioButton_point=(RadioButton)findViewById(R.id.rb_point);
        radioButton_test=(RadioButton)findViewById(R.id.rb_test);
        seekBar_capture_duration=(SeekBar)findViewById(R.id.seekBar_duration);
        seekBar_notification=(SeekBar)findViewById(R.id.seekBar_notification);
        notification_text=(TextView)findViewById(R.id.tv_seekbar1_value);
        duration_text=(TextView)findViewById(R.id.tv_seekbar2_value);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }

        };
        time1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag_time=1;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Settings.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                       update_time(selectedHour,selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        time2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag_time=2;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Settings.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        update_time(selectedHour,selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.saveDataInPref(getApplicationContext(),start_time,Utils.START_TIME);
                Utils.saveDataInPref(getApplicationContext(),end_time,Utils.END_TIME);
                Utils.saveDataInPref(getApplicationContext(),notification_value,Utils.NOTFICATION_TIME_KEY);
                Utils.saveDataInPref(getApplicationContext(),boolean_notification_on_off+"",Utils.NOTIFICATION_ON_OFF);

                Gson gson = new Gson();

                if(selected_day_array!=null)
                json_day = gson.toJson(selected_day_array);
                else
                {
                    //default
                }
                Utils.saveDataInPref(getApplicationContext(),json_day,Utils.SELECTED_DAY);
                Toast.makeText(getApplicationContext(),"Changes Applied",Toast.LENGTH_LONG).show();

            }
        });

        seekBar_notification.setOnSeekBarChangeListener(new CustomSeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progress=(int)(progress*0.6);
                notification_value=progress+"";
                notification_text.setText("Notification Time: "+progress+" Min");

            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        select_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Settings.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.select_day);
                // Set dialog title
                // set values for custom dialog components - text, image and button
                final CheckBox mon=(CheckBox)dialog.findViewById(R.id.cb_mon);
                final CheckBox tue=(CheckBox)dialog.findViewById(R.id.cb_tue);
                final CheckBox wed=(CheckBox)dialog.findViewById(R.id.cb_wed);
                final CheckBox thur=(CheckBox)dialog.findViewById(R.id.cb_thur);
                final CheckBox fri=(CheckBox)dialog.findViewById(R.id.cb_fri);
                final CheckBox sat=(CheckBox)dialog.findViewById(R.id.cb_sat);
                final CheckBox sun=(CheckBox)dialog.findViewById(R.id.cb_sun);


                Button ok;
                ok = (Button) dialog.findViewById(R.id.btn_select_day_ok);
                ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(selected_day_array!=null)
                            selected_day_array.clear();
                        selected_day_array=new ArrayList<String>();

                        if(mon.isSelected())
                        {
                            selected_day_array.add("monday");
                        }
                        if(tue.isSelected())
                        {
                            selected_day_array.add("tuesday");
                        }
                        if(wed.isSelected())
                        {
                            selected_day_array.add("wednesday");
                        }
                        if(thur.isSelected())
                        {
                            selected_day_array.add("thursday");
                        }
                        if(fri.isSelected())
                        {
                            selected_day_array.add("friday");
                        }
                        if(sat.isSelected())
                        {
                            selected_day_array.add("saturday");
                        }
                        if(sun.isSelected())
                        {
                            selected_day_array.add("sunday");
                        }

                        dialog.cancel();
                    }
                });

                dialog.show();

            }
        });
        seekBar_capture_duration.setOnSeekBarChangeListener(new CustomSeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progress=(int)(progress*0.6);
                capture_duration=progress+"";
                duration_text.setText("Capture Duration: "+progress+" Min");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cb_notification_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked==true) {
                    enable_all();
                }
                else {
                    disable_all();
                }
                boolean_notification_on_off=isChecked;

            }}
        );
    }
    private void update_time(int selectedHour,int selectedMinute) {
        if (flag_time == 1){
            time1.setText(selectedHour + ":" + selectedMinute);
        start_time = selectedHour + ":" + selectedMinute;
        }
        else if(flag_time==2) {
            time2.setText(selectedHour + ":" + selectedMinute);
            end_time = selectedHour + ":" + selectedMinute;
        }
    }
    
    void enable_all()
    {
        select_day.setEnabled(true);
        time1.setEnabled(true);
        time2.setEnabled(true);
        seekBar_notification.setEnabled(true);

    }
    void disable_all()
    {
        select_day.setEnabled(false);
        time1.setEnabled(false);
        time2.setEnabled(false);
        seekBar_notification.setEnabled(false);

    }
}

