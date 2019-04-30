package com.syntax.roadroller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.syntax.roadroller.Admin.AdminHome;
import com.syntax.roadroller.Supervisor.SupervisorHome;
import com.syntax.roadroller.User.UserHome;

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

public class forgot extends AppCompatActivity {
    EditText email;
    String EMAIL;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        email = findViewById(R.id.email);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = "checkemail";
                EMAIL = email.getText().toString().trim();
                if (EMAIL.isEmpty()) {
                    email.requestFocus();
                    email.setError("enter email");
                } else {
                    new async_forgot().execute(code, EMAIL);


                }
            }
        });


    }

    public class async_forgot extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(forgot.this);
            pd.setCancelable(false);
            pd.setMessage("checking email ..");
            pd.setTitle("Please wait..");
            pd.show();

        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String str = "";
            //Log.d("inside inback leejo","inside inback");
            ArrayList<NameValuePair> pdat = new ArrayList<NameValuePair>(6);

            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("email", params[1]));


            HttpClient client = new DefaultHttpClient();
            HttpPost mypdat = new HttpPost(Utility.URL_PATTERN);
            try {
                mypdat.setEntity(new UrlEncodedFormEntity(pdat));
                //Log.d("post data","post data");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
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
            pd.dismiss();

            switch (Log.d("result: ", result)) {
            }
            try {
                if (!result.trim().equals("failed")) {
                    String phone = result.trim();

                    String otp = new String(Utility.OTP(4));

                    Utility.OTPVAL = otp;


                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, "use this otp " + otp, null, null);

                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();


                    Utility.emailID = EMAIL;

                    Intent i = new Intent(getApplicationContext(), otpverify.class);
//                    i.putExtra("email", EMAIL);
                    startActivity(i);

                } else {
                    Toast.makeText(getApplicationContext(), " failed..!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(forgot.this, e + "", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
