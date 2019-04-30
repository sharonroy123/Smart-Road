package com.syntax.roadroller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
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

public class Registration extends AppCompatActivity {

    EditText name,address,phone,email,password,qualification,experience;
    TextView login;
    Button btnreg;
    RadioGroup rggender;
    Switch switchuser;
    String NAME,GENDER,ADDRESS,PHONE,EMAIL,PASS,QUAL="0",EXPER="0",KEY="reg_user";
    RadioButton rdtemp;
    Boolean user_supervisor=false;
    TextInputLayout layoutqual,layoutexp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        name=findViewById(R.id.user_reg_name);
        address=findViewById(R.id.user_reg_address);
        phone=findViewById(R.id.user_reg_phone);
        email=findViewById(R.id.user_reg_email);
        password=findViewById(R.id.user_reg_password);
        btnreg=findViewById(R.id.user_reg_btnreg);
        login=findViewById(R.id.user_reg_login);
        rggender=findViewById(R.id.user_reg_rdgroup);
        qualification=findViewById(R.id.user_reg_qualification);
        experience=findViewById(R.id.user_reg_experience);

        switchuser=findViewById(R.id.user_reg_switchuser);

        layoutqual=findViewById(R.id.user_reg_layout_qualification);
        layoutexp=findViewById(R.id.user_reg_layout_experience);


        switchuser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    user_supervisor=true;
                    layoutqual.setVisibility(View.VISIBLE);
                    layoutexp.setVisibility(View.VISIBLE);

                    KEY="reg_supervisor";
                }else{
                    user_supervisor=false;
                    layoutqual.setVisibility(View.GONE);
                    layoutexp.setVisibility(View.GONE);
                    KEY="reg_user";
                }
            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NAME=name.getText().toString();
                int id=rggender.getCheckedRadioButtonId();
                rdtemp=findViewById(id);
                GENDER=rdtemp.getText().toString();
                ADDRESS=address.getText().toString();
                PHONE=phone.getText().toString();
                QUAL=qualification.getText().toString();
                EXPER=experience.getText().toString();
                EMAIL=email.getText().toString();
                PASS=password.getText().toString();




                if(!NAME.matches("[A-Z a-z]+")||NAME.isEmpty()){
                    name.requestFocus();
                    name.setError("Enter valid Name");
                }else if(GENDER.isEmpty()){
                    Toast.makeText(Registration.this, "Select Gender", Toast.LENGTH_SHORT).show();
                }else if(ADDRESS.isEmpty()){
                    address.requestFocus();
                    address.setError("Enter Address");
                }else if(!PHONE.matches("[0-9]{10}")){
                    phone.requestFocus();
                    phone.setError("Enter valid Phone");
                }
               else    if(QUAL.isEmpty() && KEY.equals("reg_supervisor")){
                        qualification.requestFocus();
                        qualification.setError("Enter Qualification");
                    }
                    else if(EXPER.isEmpty() &&KEY.equals("reg_supervgisor")) {
                        experience.requestFocus();
                        experience.setError("Enter Experience");
                    }

               else  if(!EMAIL.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")||EMAIL.isEmpty()){
                    email.requestFocus();
                    email.setError("Enter valid Email");
                }else if(!PASS.matches( "^(?=.*[0-9])(?=.*[A-Z]).{8}$")){
                    password.requestFocus();
                    password.setError("Enter valid password..field must contain 8 character including Upper/lowercase and numbers");
                }


                else{

                    new async_forgot().execute("checkemail", EMAIL);

                    //    new async_Registration().execute(KEY,NAME,GENDER,ADDRESS,PHONE,EMAIL,PASS,QUAL,EXPER);
                }
            }
        });

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                return false;
            }
        });
    }

    public class async_Registration extends AsyncTask<String, String, String>
    {

        ProgressDialog pd;
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd=new ProgressDialog(Registration.this);
            pd.setCancelable(false);
            pd.setMessage("Your Register going on..");
            pd.setTitle("Please wait..");
            pd.show();

        }
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String str="";
            //Log.d("inside inback leejo","inside inback");
            ArrayList<NameValuePair> pdat=new ArrayList<NameValuePair>(8);

            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("name", params[1]));
            pdat.add(new BasicNameValuePair("gender", params[2]));
            pdat.add(new BasicNameValuePair("address", params[3]));
            pdat.add(new BasicNameValuePair("phone", params[4]));
            pdat.add(new BasicNameValuePair("email", params[5]));
            pdat.add(new BasicNameValuePair("password", params[6]));
            pdat.add(new BasicNameValuePair("qual", params[7]));
            pdat.add(new BasicNameValuePair("exper", params[8]));

            HttpClient client=new DefaultHttpClient();
            HttpPost mypdat=new HttpPost(Utility.URL_PATTERN);
            try
            {
                mypdat.setEntity(new UrlEncodedFormEntity(pdat));
                //Log.d("post data","post data");
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try
            {
                HttpResponse re=client.execute(mypdat);
                HttpEntity entity=re.getEntity();
                str= EntityUtils.toString(entity);
                int status=re.getStatusLine().getStatusCode();
                if(status==200)
                {
                    return str;
                }
            }
            catch (ClientProtocolException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } return null;
        }

        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            pd.dismiss();

            switch (Log.d("result: ", result)) {
            }
            try {
                if (!result.trim().equals("failed")) {

                    Toast.makeText(getApplicationContext(), "Registration success..!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Login.class));

                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed..!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e)
            {
                Toast.makeText(Registration.this, e+"", Toast.LENGTH_SHORT).show();
            }
        }

    }


 //................................................................................
 public class async_forgot extends AsyncTask<String, String, String> {

     ProgressDialog pd;

     protected void onPreExecute() {
         super.onPreExecute();

         pd = new ProgressDialog(Registration.this);
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

                 Toast.makeText(getApplicationContext(), " email already exists", Toast.LENGTH_LONG).show();

             } else {

                     new async_Registration().execute(KEY,NAME,GENDER,ADDRESS,PHONE,EMAIL,PASS,QUAL,EXPER);

                 Toast.makeText(getApplicationContext(), "Registering... ", Toast.LENGTH_LONG).show();
             }
         } catch (Exception e) {
             Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
         }
     }


 }
}
