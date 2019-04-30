package com.syntax.roadroller.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

public class UserViewComplaintDetails extends AppCompatActivity {

    ImageView image;
    TextView subject,date,details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_complaint_details);


        image=findViewById(R.id.user_view_com_det_img);
        subject=findViewById(R.id.user_view_com_det_subject);
        date=findViewById(R.id.user_view_com_det_date);
        details=findViewById(R.id.user_view_com_det_details);

        String data[]=getIntent().getStringExtra("data").split(":");
        String SUB=data[0];
        String DATE=data[1];
        String DETAILS=data[2];
        String IMG=data[3];

        subject.setText(SUB);
        date.setText(DATE);
        details.setText(DETAILS);

//        image.getLayoutParams().height=200;
//        image.getLayoutParams().width=300;
        image.setScaleType(ImageView.ScaleType.FIT_XY);

        try {
            byte[] imageAsBytes = Base64.decode(IMG.getBytes());

            image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length) );
        } catch (IOException e) {

            e.printStackTrace();
        }

    }



}
