package com.syntax.roadroller.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import static android.content.Context.MODE_PRIVATE;


public class UserfeedBack extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    String SUB,DES,RATING="0";
    EditText sub,des;
    Button send;
    RatingBar rt;

    public UserfeedBack() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_userfeed_back, container, false);
        sub=(EditText)view.findViewById(R.id.userfeedback_subject);
        des=(EditText)view.findViewById(R.id.userfeedback_details);
        send=(Button) view.findViewById(R.id.userfeedback_btnsend);
        rt=(RatingBar) view.findViewById(R.id.userfeedback_rating);

        rt.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                RATING= String.valueOf(rt.getRating());

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SUB=sub.getText().toString().trim();
                DES=des.getText().toString().trim();

                Toast.makeText(getContext(), "subdes ratr"+SUB+DES+ RATING, Toast.LENGTH_LONG).show();
                if (SUB.isEmpty()) {
                    sub.requestFocus();
                    sub.setError("ENTER VALID SUBJECT");
                } else if (DES.isEmpty()) {
                    des.requestFocus();
                    des.setError("ENTER VALID DETAILS");
                } else {
                    new addFeedBack().execute("addFeedBack",SUB,DES,RATING);
                }
            }
        });

        return  view;
    }



    public class addFeedBack extends AsyncTask<String, String, String>
    {

        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String str="";
            //Log.d("inside inback leejo","inside inback");
            SharedPreferences prefs = getContext().getSharedPreferences("SharedData", MODE_PRIVATE);
            final   String uid = prefs.getString("uid", "No logid");//"No name defined" is the default value.

            ArrayList<NameValuePair> pdat=new ArrayList<NameValuePair>(6);

            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("uid", uid));
            pdat.add(new BasicNameValuePair("subject", params[1]));
            pdat.add(new BasicNameValuePair("description", params[2]));
            pdat.add(new BasicNameValuePair("rating", params[3]));
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


            switch (Log.d("result: ", result)) {
            }
            try {
                if (!result.trim().equals("failed")) {


                    Toast.makeText(getContext(), "review added ..!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getContext(),UserHome.class));

                } else {
                    Toast.makeText(getContext(), "update failed..!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e)
            {
                Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
