package com.syntax.roadroller.Admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.syntax.roadroller.Common.Base64;
import com.syntax.roadroller.R;
import com.syntax.roadroller.Utility;

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

public class AdminViewComplainDetails extends AppCompatActivity {

    ImageView image;
    TextView subject,date,details;
    ImageView track;
    String SUB,DATE,DETAILS,IMG,LAT,LONG,UID,UNAME,UPHONE,UEMAIL,CID;
    Button posttender,resolve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_complain_details);

        image=findViewById(R.id.admin_view_com_det_img);
        subject=findViewById(R.id.admin_view_com_det_subject);
        date=findViewById(R.id.admin_view_com_det_date);
        details=findViewById(R.id.admin_view_com_det_details);
        track=findViewById(R.id.admin_view_com_det_map);
        posttender=findViewById(R.id.admin_view_com_det_new_tender);
        resolve=findViewById(R.id.admin_view_com_det_resolve);

        String data[]=getIntent().getStringExtra("data").split(":");
         SUB=data[0];
         DATE=data[1];
         DETAILS=data[2];
         IMG=data[3];
         LAT=data[4];
         LONG=data[5];
         UID=data[6];
         UNAME=data[7];
         UPHONE=data[8];
         UEMAIL=data[9];
         CID=data[10];

        subject.setText(SUB);
        date.setText(DATE);
        details.setText(DETAILS);

        image.setScaleType(ImageView.ScaleType.FIT_XY);

        try {
            byte[] imageAsBytes = Base64.decode(IMG.getBytes());

            image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length) );
        } catch (IOException e) {

            e.printStackTrace();
        }

        track.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Toast.makeText(AdminViewComplainDetails.this, "track", Toast.LENGTH_SHORT).show();

                Intent i =new Intent(getApplicationContext(),AdminTrackLocation.class);
                i.putExtra("subject",SUB);
                i.putExtra("details",DETAILS);
                i.putExtra("latutude",LAT);
                i.putExtra("longitude",LONG);
                i.putExtra("date",DATE);
                startActivity(i);
                return false;
            }
        });

        posttender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCustomDialog();
            }
        });

        resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ResolveUserComplaint().execute("ResolveUserComplaint",CID);
            }
        });


    }


    protected void showCustomDialog() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(AdminViewComplainDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog);

        //final String pid=Pid;


        final Button post = (Button) dialog.findViewById(R.id.cus_dlg_admin_post_tender_btn_post);

        final EditText budget = (EditText) dialog.findViewById(R.id.cus_dlg_admin_post_tender_budget);
        final EditText comp_date = (EditText) dialog.findViewById(R.id.cus_dlg_admin_post_tender_comp_date);
        final EditText details = (EditText) dialog.findViewById(R.id.cus_dlg_admin_post_tender_details);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String BUDGET=budget.getText().toString();
                String COMP_DATE=comp_date.getText().toString();
                String DETAILS=details.getText().toString();

                if(BUDGET.isEmpty()){
                    budget.requestFocus();
                    budget.setError("enter budget");
                }else if(!COMP_DATE.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$")||COMP_DATE.isEmpty()){
                    comp_date.requestFocus();
                    comp_date.setError("enter valid date");
                }else if(DETAILS.isEmpty()){
                details.requestFocus();
                details.setError("enter details");
                }else{
                    new PostNewTender().execute("PostNewTender",CID,BUDGET,COMP_DATE,DETAILS);
                }

              // new async_PayPenalty().execute("PayPenalty",ACNUM,CVV,PASS,penalty);

            }
        });



        dialog.show();
    }


    public class ResolveUserComplaint extends AsyncTask<String, String, String>
    {


        ProgressDialog pd;
        protected void onPreExecute()
        {
            super.onPreExecute();

//            pd=new ProgressDialog(getApplicationContext());
//            pd.setCancelable(false);
//            pd.setMessage("");
//            pd.setTitle("Please wait..");
//            pd.show();

        }
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String str="";
            //Log.d("inside inback leejo","inside inback");

            ArrayList<NameValuePair> pdat=new ArrayList<NameValuePair>(6);

            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("cid", params[1]));


            // pdat.add(new BasicNameValuePair("uid", uid));
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
//            pd.dismiss();

            switch (Log.d("result: ", result)) {
            }
            try {
                if (!result.trim().equals("failed")) {
                    Toast.makeText(getApplicationContext(), "updated successfully!", Toast.LENGTH_LONG).show();

                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(""+UPHONE, null, "Your Complaint "+SUB+" Posted on :"+DATE+" Resolved. ", null, null);

                    startActivity(new Intent(getApplicationContext(),AdminHome.class));
                } else {
                    Toast.makeText(getApplicationContext(), "resolve failed..!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e+"", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class PostNewTender extends AsyncTask<String, String, String>
    {


        ProgressDialog pd;
        protected void onPreExecute()
        {
            super.onPreExecute();

//            pd=new ProgressDialog(getApplicationContext());
//            pd.setCancelable(false);
//            pd.setMessage("");
//            pd.setTitle("Please wait..");
//            pd.show();

        }
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String str="";
            //Log.d("inside inback leejo","inside inback");

            ArrayList<NameValuePair> pdat=new ArrayList<NameValuePair>(6);

            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("cid", params[1]));
            pdat.add(new BasicNameValuePair("budget", params[2]));
            pdat.add(new BasicNameValuePair("date", params[3]));
            pdat.add(new BasicNameValuePair("details", params[4]));


            // pdat.add(new BasicNameValuePair("uid", uid));
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
//            pd.dismiss();

            switch (Log.d("result: ", result)) {
            }
            try {
                if (!result.trim().equals("failed")) {
                    Toast.makeText(getApplicationContext(), "Post Added successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),AdminHome.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Post failed..!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e+"", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
