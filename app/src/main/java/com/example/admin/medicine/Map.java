package com.example.admin.medicine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;


public class Map extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    Button btn;
    Double p_lat,p_long,u_lat=0.0,u_long=0.0;
    Integer method;
    ImageView pointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        savedInstanceState= getIntent().getExtras();
        method = savedInstanceState.getInt("method");
        p_lat= savedInstanceState.getDouble("p_lat");
        p_long= savedInstanceState.getDouble("p_long");



        if (googleServicesAvailable()) {
            //Toast.makeText(Map.this, "running", Toast.LENGTH_LONG).show();

            SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
            if(lang.getInt("language",1)==1)
                setAppLocale("ar");

            setContentView(R.layout.activity_map);
            btn=(Button)findViewById(R.id.button3);
            pointer = (ImageView)findViewById(R.id.imageView3);
            if(method==1) {
                btn.setVisibility(View.GONE);
                pointer.setVisibility(View.INVISIBLE);
            }
            else if(method ==2) {
                btn.setVisibility(View.VISIBLE);
                pointer.setVisibility(View.VISIBLE);
            }
            initMap();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    u_lat=mGoogleMap.getCameraPosition().target.latitude;
                    u_long=mGoogleMap.getCameraPosition().target.longitude;
                    Intent intent = new Intent();
                    intent.putExtra("u_lat",u_lat);
                    intent.putExtra("u_long",u_long);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            });

        }
    }

    private void initMap() {
        MapFragment mapfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapfragment.getMapAsync(Map.this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            //Toast.makeText(this, "error eee", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        goToLocation(p_lat,p_long,15);
    }

    private void goToLocation(double lat, double lng, float z) {
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,z);
        MarkerOptions options = new MarkerOptions().title("الصيدلية")
                .position(new LatLng(p_lat,p_long));
        mGoogleMap.moveCamera(update);
        if(method==1)
            mGoogleMap.addMarker(options);
    }

    public  void setAppLocale(String localeCode){

        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf= res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}