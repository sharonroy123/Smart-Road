package com.syntax.roadroller.Admin;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class AdminViewWorkProgressDetails extends AppCompatActivity {
    String SID,AID;
    ListView list;
    String arrworklist[]={"no data fount "},iddata[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_work_progress_details);
        list=findViewById(R.id.admin_view_work_progress_details_list);
        ArrayAdapter ar=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arrworklist);
        list.setAdapter(ar);

        SID=getIntent().getStringExtra("sid");
        AID=getIntent().getStringExtra("aid");

        new AsmingetWorkProgressDetails().execute("AsmingetWorkProgressDetails",SID,AID);

    }
    public class AsmingetWorkProgressDetails extends AsyncTask<String, String, String>
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
            pdat.add(new BasicNameValuePair("key", params[0]));
            pdat.add(new BasicNameValuePair("sid", params[1]));
            pdat.add(new BasicNameValuePair("aid", params[2]));

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
                    arrworklist=result.trim().split("#");
                    ArrayAdapter ar=new ArrayAdapter(getApplicationContext(),R.layout.cust_list,arrworklist);
                    list.setAdapter(ar);


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
