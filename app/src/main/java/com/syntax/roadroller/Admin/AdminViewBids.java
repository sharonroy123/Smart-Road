package com.syntax.roadroller.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.syntax.roadroller.R;
import com.syntax.roadroller.Supervisor.Super_View_Tender_Details;
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


public class AdminViewBids extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    String bidlist[]={"no data fount "},fulldata[];
    ListView bids;
    public AdminViewBids() {
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


        View view= inflater.inflate(R.layout.fragment_admin_view_bids, container, false);
        bids=view.findViewById(R.id.admin_view_bids_list);

        ArrayAdapter ar=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,bidlist);
        bids.setAdapter(ar);
        new getbidsList().execute("getbidsList");

        bids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String data=fulldata[i];
                Intent in= new Intent(getContext(),AdminViewBidDetails.class);
                in.putExtra("data",data);
                startActivity(in);
            }
        });



        return view;
    }

    public class getbidsList extends AsyncTask<String, String, String>
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

            ArrayList<NameValuePair> pdat=new ArrayList<NameValuePair>(6);

            pdat.add(new BasicNameValuePair("key", params[0]));

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
           // pd.dismiss();

            switch (Log.d("result: ", result)) {
            }
            try {
                if (!result.trim().equals("failed")) {
                    String data[]=result.trim().split("#");
                    bidlist=data[0].split("&");
                    fulldata=data[1].split("&");
                    ArrayAdapter ar=new ArrayAdapter(getContext(),R.layout.cust_list,bidlist);
                    bids.setAdapter(ar);
                } else {
                    Toast.makeText(getContext(), "no data..!", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e)
            {
                Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
