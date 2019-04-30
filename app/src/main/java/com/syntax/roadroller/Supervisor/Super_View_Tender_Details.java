package com.syntax.roadroller.Supervisor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.syntax.roadroller.Admin.AdminHome;
import com.syntax.roadroller.Admin.AdminTrackLocation;
import com.syntax.roadroller.Admin.AdminViewComplainDetails;
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

public class Super_View_Tender_Details extends AppCompatActivity {


    ImageView image, map;
    TextView subject, details, budget, compdate, description;
    Button btnbid;
    String SUB, DETAILS, BUDGET, COMPDATE, DESC, LONG, LAT, CID, TID, IMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_super__view__tender__details);

        subject = findViewById(R.id.super_view_tend_det_sub);
        details = findViewById(R.id.super_view_tend_det_details);
        budget = findViewById(R.id.super_view_tend_det_post_budget);
        compdate = findViewById(R.id.super_view_tend_det_post_compdate);
        description = findViewById(R.id.super_view_tend_det_post_description);
        image = findViewById(R.id.super_view_tend_det_img);
        map = findViewById(R.id.super_view_tend_det__map);
        btnbid = findViewById(R.id.super_view_tend_det_btnbid);

        String data[] = getIntent().getStringExtra("data").split(":");
        CID = data[0];
        SUB = data[1];
        DETAILS = data[2];
        BUDGET = data[8];
        IMG = data[3];
        LAT = data[4];
        LONG = data[5];
        TID = data[6];
        COMPDATE = data[9];
        DESC = data[10];

        subject.setText(SUB);
        details.setText(DETAILS);
        budget.setText("Budget         : " + BUDGET);
        compdate.setText("Exp Date      : " + COMPDATE);
        description.setText("TENDER DESCRIPTION ....\n " + DESC);


        image.setScaleType(ImageView.ScaleType.FIT_XY);

        try {
            byte[] imageAsBytes = Base64.decode(IMG.getBytes());

            image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        } catch (IOException e) {

            e.printStackTrace();
        }

        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Toast.makeText(Super_View_Tender_Details.this, "track", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), AdminTrackLocation.class);
                i.putExtra("subject", SUB);
                i.putExtra("details", DETAILS);
                i.putExtra("latutude", LAT);
                i.putExtra("longitude", LONG);
                startActivity(i);
                return false;
            }
        });

        btnbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

    }

    protected void showCustomDialog() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(Super_View_Tender_Details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_bid_dialog);

        //final String pid=Pid;


        final Button btnbid = (Button) dialog.findViewById(R.id.cus_dlg_super_bid_btn_bid);

        final EditText bidamount = (EditText) dialog.findViewById(R.id.cus_dlg_super_bid_amount);
        final EditText biddetails = (EditText) dialog.findViewById(R.id.cus_dlg_super_bid_details);


        btnbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String BIDAMOUNT = bidamount.getText().toString();
                String BIDDETAILS = biddetails.getText().toString();

                if (BIDAMOUNT.isEmpty()) {
                    bidamount.requestFocus();
                    bidamount.setError("Amount");
                } else if (BIDDETAILS.isEmpty()) {
                    biddetails.requestFocus();
                    biddetails.setError("details");
                } else {
                    new AddBids().execute("AddBids", TID, BIDAMOUNT, BIDDETAILS);

                }


            }
        });


        dialog.show();
    }


    public class AddBids extends AsyncTask<String, String, String> {


        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();

//            pd=new ProgressDialog(getContext());
//            pd.setCancelable(false);
//            pd.setMessage("Your Register going on..");
//            pd.setTitle("Please wait..");
//            pd.show();

        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String str = "";
            //Log.d("inside inback leejo","inside inback");
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedData", MODE_PRIVATE);
            final String uid = prefs.getString("uid", "No logid");//"No name defined" is the default value.

            ArrayList<NameValuePair> pdat = new ArrayList<NameValuePair>(6);
            pdat.add(new BasicNameValuePair("uid", uid));
            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("tid", params[1]));
            pdat.add(new BasicNameValuePair("bamount", params[2]));
            pdat.add(new BasicNameValuePair("bdetails", params[3]));


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
            //  pd.dismiss();

            switch (Log.d("result: ", result)) {
            }
            try {
                if (!result.trim().equals("failed")) {
                    Toast.makeText(getApplicationContext(), "success..!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), SupervisorHome.class));
                } else {
                    Toast.makeText(getApplicationContext(), " failed..!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
