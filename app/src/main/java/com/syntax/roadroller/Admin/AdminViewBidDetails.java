package com.syntax.roadroller.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class AdminViewBidDetails extends AppCompatActivity {

    Button btnallocate;
    TextView sname,sphone,semail,subject,details,expdate,budget,bidamount,biddetails;
    String SNAME,SPHONE,SEMAIL,SUBJECT,DETAILS,EXPDATE,BUDGET,BIDAMOUNT,BIDDETAILS;
    String TID,BID,SID,STATUS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_bid_details);

        sname=findViewById(R.id.admin_view_bid_det_sname);
        sphone=findViewById(R.id.admin_view_bid_det_sphone);
        semail=findViewById(R.id.admin_view_bid_det_semail);
        subject=findViewById(R.id.admin_view_bid_det_sub);
        details=findViewById(R.id.admin_view_bid_det_details);
        expdate=findViewById(R.id.admin_view_bid_det_exppdate);
        budget=findViewById(R.id.admin_view_bid_det__budget);
        bidamount=findViewById(R.id.admin_view_bid_det__bidamount);
        biddetails=findViewById(R.id.admin_view_bid_det_biddetails);
        btnallocate=findViewById(R.id.admin_view_bid_det_btnallocate);

        String data[]=getIntent().getStringExtra("data").split(":");
        TID=data[0];
        BID=data[1];
        SID=data[2];
        SNAME=data[4];
        SPHONE=data[5];
        SEMAIL=data[6];
        SUBJECT=data[7];
        DETAILS=data[8];
        EXPDATE=data[9];
        BUDGET=data[10];
        BIDAMOUNT=data[11];
        BIDDETAILS=data[12];



        sname.setText(SNAME);
        sphone.setText(SPHONE);
        semail.setText(SEMAIL);
        subject.setText(SUBJECT);
        details.setText(DETAILS);
        expdate.setText("EXP Date  - "+EXPDATE);
        budget.setText("Budget    - "+BUDGET);
        bidamount.setText("Bid Amount   - "+BIDAMOUNT);
        biddetails.setText("Bid Details   - "+BIDDETAILS);

        btnallocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AllocateTender().execute("AllocateTender",TID,BID,SID);
            }
        });

    }


    public class AllocateTender extends AsyncTask<String, String, String>
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
            pdat.add(new BasicNameValuePair("tid", params[1]));
            pdat.add(new BasicNameValuePair("bid", params[2]));
            pdat.add(new BasicNameValuePair("sid", params[3]));



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
                    Toast.makeText(getApplicationContext(), "Allocated successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),AdminHome.class));
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
