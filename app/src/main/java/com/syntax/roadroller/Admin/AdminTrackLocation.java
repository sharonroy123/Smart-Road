package com.syntax.roadroller.Admin;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.syntax.roadroller.R;

import java.util.Timer;
import java.util.TimerTask;

public class AdminTrackLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Timer mTimer1;
    double lat=0.0;
    double longi=0.0;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();
    private boolean run = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin_track_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

     String subject=getIntent().getStringExtra("subject");
     String details=getIntent().getStringExtra("details");
     Double lat=Double.parseDouble(getIntent().getStringExtra("latutude"));
     Double longi=Double.parseDouble(getIntent().getStringExtra("longitude"));

        mMap.addMarker(new MarkerOptions().position( new LatLng(lat, longi)).title(subject).snippet(details));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, longi)).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });


    }











        }
