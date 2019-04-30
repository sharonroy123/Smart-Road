package com.syntax.roadroller.Supervisor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class Super_Approved_Tender_Details extends AppCompatActivity {

    Button update;
    SeekBar seekbar;
    TextView name,budget,statrdate,enddate,details,perc;
    String AID,TID,DETAILS,PERC,PREVIOUS;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_super__approved__tender__details);
        name=findViewById(R.id.super_tend_approved_details_subject);
        budget=findViewById(R.id.super_tend_approved_details_budget);
        statrdate=findViewById(R.id.super_tend_approved_details_startdate);
        enddate=findViewById(R.id.super_tend_approved_details_enddate);
        details=findViewById(R.id.super_tend_approved_details_details);
        seekbar=findViewById(R.id.super_tend_approved_details_details_seekbar);
        perc=findViewById(R.id.super_tend_approved_details_details_perc);
        update=findViewById(R.id.super_tend_approved_details_details_upadate);

        new getWorkProgress().execute("getWorkProgress",TID);
        String data[]=getIntent().getStringExtra("data").split(":");
        name.setText(data[0]);
        budget.setText(data[1]);
        statrdate.setText(data[2]);
        enddate.setText(data[3]);
        details.setText(data[4]);
        AID=data[5];
        TID=data[6];
        perc.setText("35 %");
        seekbar.setProgress(35);
        seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
    }

    protected void showCustomDialog() {
        // TODO Auto-generated method stub
        // final Dialog dialog = new Dialog(User_View_Recipie_Details.this);
        dialog = new Dialog(Super_Approved_Tender_Details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_super_workupdate);

        //final String pid=Pid;
        final Button update = (Button) dialog.findViewById(R.id.cus_dlg_super_workupdate_update);
        final TextView perc = (TextView) dialog.findViewById(R.id.cus_dlg_super_workupdate_details_perc);
        final SeekBar seelbar1 = (SeekBar) dialog.findViewById(R.id.cus_dlg_super_workupdate_details_seekbar);
        final EditText details = (EditText) dialog.findViewById(R.id.cus_dlg_super_workupdate_details);
        final TextView previous = (TextView) dialog.findViewById(R.id.cus_dlg_super_workupdate_details_previous);
        previous.setText("previous status "+PREVIOUS+"%");
        seelbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                perc.setText(i+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DETAILS=details.getText().toString();
                String ar[]=perc.getText().toString().split("%");
                PERC=ar[0];
                if(Integer.parseInt(PREVIOUS)>=Integer.parseInt(PERC)) {
                    perc.setError("");
                    Toast.makeText(Super_Approved_Tender_Details.this, "update percentage", Toast.LENGTH_SHORT).show();
                } else if(DETAILS.isEmpty()) {
                    details.requestFocus();
                    details.setError("details");
                }else{
                    new updateWorkProgress().execute("updateWorkProgress",TID,PERC,DETAILS,AID);
                }



            }
        });


        dialog.show();
    }

    public class getWorkProgress extends AsyncTask<String, String, String>
    {


        ProgressDialog pd;
        protected void onPreExecute()
        {
            super.onPreExecute();

//            pd=new ProgressDialog(getContext());
//            pd.setCancelable(false);
//            pd.setMessage("Your Register going on..");
//            pd.setTitle("Please wait..");
//            pd.show();

        }
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String str="";
            //Log.d("inside inback leejo","inside inback");
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedData", MODE_PRIVATE);
            final   String uid = prefs.getString("uid", "No logid");//"No name defined" is the default value.

            ArrayList<NameValuePair> pdat=new ArrayList<NameValuePair>(6);
            pdat.add(new BasicNameValuePair("sid", uid));
            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("tid", params[1]));

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
            //  pd.dismiss();

            switch (Log.d("result: ", result)) {
            }
            try {
                if (!result.trim().equals("failed")) {

                        PREVIOUS = result.trim();
                    if(PREVIOUS.equals("nodata")){
                        PREVIOUS="0";
                        perc.setText("0%");
                        seekbar.setProgress(0);
                    }else{
                        perc.setText(PREVIOUS+"%");
                        seekbar.setProgress(Integer.parseInt(PREVIOUS));
                    }


                } else {
                    Toast.makeText(getApplicationContext(), " failed..!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e+"", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public class updateWorkProgress extends AsyncTask<String, String, String>
    {


        ProgressDialog pd;
        protected void onPreExecute()
        {
            super.onPreExecute();

//            pd=new ProgressDialog(getContext());
//            pd.setCancelable(false);
//            pd.setMessage("Your Register going on..");
//            pd.setTitle("Please wait..");
//            pd.show();

        }
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String str="";
            //Log.d("inside inback leejo","inside inback");
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedData", MODE_PRIVATE);
            final   String uid = prefs.getString("uid", "No logid");//"No name defined" is the default value.

            ArrayList<NameValuePair> pdat=new ArrayList<NameValuePair>(6);
            pdat.add(new BasicNameValuePair("sid", uid));
            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("tid", params[1]));
            pdat.add(new BasicNameValuePair("percentage", params[2]));
            pdat.add(new BasicNameValuePair("details", params[3]));
            pdat.add(new BasicNameValuePair("aid", params[4]));


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
            //  pd.dismiss();

            switch (Log.d("result: ", result)) {
            }
            try {
                if (!result.trim().equals("failed")) {
                    Toast.makeText(getApplicationContext(), "success..!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),SupervisorHome.class));
                } else {
                    Toast.makeText(getApplicationContext(), " failed..!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e+"", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
