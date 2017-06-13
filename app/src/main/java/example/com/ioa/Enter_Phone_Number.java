package example.com.ioa;

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


        if(Utils.getPhoneNumber(getApplicationContext()).equals(""))
        {
            if(Utils.getDataFromSharedPref(getApplicationContext(),Utils.PHONE_NUMBER)==null)
            {

            }
            else
            if(Utils.getDataFromSharedPref(getApplicationContext(),Utils.PHONE_NUMBER).equals(null))
            {

            }
            else
            {
                nextPage();
            }
        }
        else{
            String phone_number=Utils.getPhoneNumber(getApplicationContext());
                Toast.makeText(getApplicationContext(),"Number found"+phone_number,Toast.LENGTH_LONG).show();
            Utils.saveDataInPref(getApplicationContext(),phone_number,Utils.PHONE_NUMBER);
            nextPage();
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
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
