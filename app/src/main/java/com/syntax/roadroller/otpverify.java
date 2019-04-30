package com.syntax.roadroller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class otpverify extends AppCompatActivity {

    Button bchange, bchange2;
    EditText oldpswd, checkotp;
    EditText newpswd;
    String oldpass, SOTP;
    String newpass;
    LinearLayout otp, pass;
    Intent in;
    String emailis = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        bchange = (Button) findViewById(R.id.submit2);
        checkotp = (EditText) findViewById(R.id.enterotp);
        bchange2 = (Button) findViewById(R.id.submit1);
//        oldpswd = (EditText) findViewById(R.id.enterold);
        newpswd = (EditText) findViewById(R.id.confirmnew);
        oldpswd = (EditText) findViewById((R.id.enternew));
        otp = (LinearLayout) findViewById(R.id.otp_check);
        pass = (LinearLayout) findViewById(R.id.otp_changepswd);


        //   emailis = in.getStringExtra("email");
        emailis = Utility.emailID;


        SOTP = Utility.OTPVAL;
        bchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oldpass = oldpswd.getText().toString();
                newpass = newpswd.getText().toString();

               Toast.makeText(getApplicationContext(), "Email and new Pass is\n " + newpass + " \n" + emailis, Toast.LENGTH_SHORT).show();

                if (!oldpass.matches( "^(?=.*[0-9])(?=.*[A-Z]).{8}$")) {
                    oldpswd.requestFocus();
                    oldpswd.setError("Enter valid password..field must contain 8 character including Upper/lowercase and numbers ");
                } else if (!newpass.matches( "^(?=.*[0-9])(?=.*[A-Z]).{8}$")) {
                    newpswd.requestFocus();
                    newpswd.setError("Enter valid password..field must contain 8 character including Upper/lowercase and numbers");
                } else if (!oldpass.equals(newpass)) {
                    newpswd.requestFocus();
                    newpswd.setError("passwords do not match! Try again");
                } else {


                  //  Toast.makeText(otpverify.this, "tadaaa", Toast.LENGTH_SHORT).show();
                     new ChangeTask().execute("changepswd", emailis, newpass);
                }

            }
            //  }


        });
        bchange2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cotp = checkotp.getText().toString().trim();
                if (!SOTP.equals(cotp)) {
                    checkotp.setError("enter valid OTP");
                } else {
                    otp.setVisibility(View.GONE);
                    pass.setVisibility(View.VISIBLE);

                }
            }

        });


    }


    public class ChangeTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(otpverify.this);
            pd.setCancelable(false);
            pd.setMessage("Verifying..!!");
            pd.setTitle("Checking your information");
            pd.show();
            Log.d("inside onpre", "onpre");
        }

        protected String doInBackground(String[] params) {
            // TODO Auto-generated method stub
            String str = "";
            List<NameValuePair> pdat = new ArrayList<NameValuePair>(8);
            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("newpass", params[2]));
            pdat.add(new BasicNameValuePair("email", params[1]));

            HttpClient client = new DefaultHttpClient();
            HttpPost mypdat = new HttpPost(Utility.URL_PATTERN);
            try {
                mypdat.setEntity(new UrlEncodedFormEntity(pdat));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                HttpResponse re = client.execute(mypdat);
                HttpEntity entity = re.getEntity();
                str = EntityUtils.toString(entity);
                int status = re.getStatusLine().getStatusCode();
                if (status == 200) {
                    return str;
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("#####", result);
            pd.dismiss();
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            try {
                if (!result.trim().equals("failed")) {

                    Log.d("#####", result);
                    Toast.makeText(getApplicationContext(), "Successfully Changed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));


                    // startActivity(new Intent(getApplicationContext(), changepswd.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Failed, please try again", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), forgot.class));
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
            }
        }

    }
}






