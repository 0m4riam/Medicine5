package com.example.admin.medicine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class PList extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    java.util.List<com.example.admin.medicine.pharmacy> pharmacies = new ArrayList<pharmacy>();
    String name,city,location,data,u_comment="";
    Integer id;
    Double latitude,longitude,u_latitude,u_longitude,m_latitude,m_longitude,distance;
    Integer user_id,product_id,prescription_id,service_type,pharmacy_id;
    Integer type,location_type=0;
    ListView listView;
    static final int REQUEST_LOCATION=1;
    LocationManager locationManager;
    EditText search_distance;
    CheckBox checkBox;
    Spinner search_range;
    String meds_details;
    TextView location_message,no_of_pharmacies;
    LinearLayout no_of_pharmacies_layout;
    Integer total_meds_count;

    Button cont,show_location,loction_current,location_map;

    JSONArray pharmacies_distances;



    Location mLocation;
    GoogleApiClient mGoogleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    ProgressDialog progressDialog,progressDialog1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");

        setContentView(R.layout.activity_plist);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        savedInstanceState=getIntent().getExtras();

        type =savedInstanceState.getInt("type");

        if(type==1) {
            product_id = savedInstanceState.getInt("product_id");
            prescription_id = savedInstanceState.getInt("prescription_id");
            meds_details=savedInstanceState.getString("meds_details");
            total_meds_count=savedInstanceState.getInt("total_meds_count");

        }else{
            data=savedInstanceState.getString("data");
            total_meds_count=savedInstanceState.getInt("total_meds");
            // Toast.makeText(PList.this,data,Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(PList.this,prescription_id.toString()+"    "+product_id.toString(),Toast.LENGTH_LONG).show();

        final EditText editText = (EditText)findViewById(R.id.editText6);
        //search_distance = (EditText)findViewById(R.id.editText7);
        final Button button =(Button)findViewById(R.id.search2);

        location_message=(TextView)findViewById(R.id.location_message);
        location_map=(Button)findViewById(R.id.location_map);
        loction_current=(Button)findViewById(R.id.location_current);
        show_location=(Button)findViewById(R.id.show_location);
        search_range=(Spinner)findViewById(R.id.search_range);

        no_of_pharmacies=(TextView)findViewById(R.id.num_of_pharmacies);
        no_of_pharmacies_layout=(LinearLayout)findViewById(R.id.no_of_pharmacies_layout);

        String[] items = new String[]{getResources().getString(R.string.near), getResources().getString(R.string.medium), getResources().getString(R.string.far)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        search_range.setAdapter(adapter);
        search_range.setSelection(2);


        loction_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Toast.makeText(PList.this, getResources().getString(R.string.location_service), Toast.LENGTH_LONG).show();
                }else {
                    location_type = 1;
                    progressDialog = ProgressDialog.show(PList.this, getResources().getString(R.string.getting_location), getResources().getString(R.string.please_wait), false, false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(true);
                    if(mLocation!=null) {
                        progressDialog.dismiss();
                        location_message.setText(getResources().getString(R.string.location_found));
                        location_message.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.correct,0,0,0);
                    }
                }
            }
        });

        final SharedPreferences sharedPreferences =  getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);

        location_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location_type=2;
                Intent intent=new Intent(getApplication(),Map.class);

                if(m_longitude!=null && m_latitude!=null) {
                    intent.putExtra("p_lat", m_latitude);
                    intent.putExtra("p_long", m_longitude);
                }else{
                    if(mLocation!=null && mLocation.hasAccuracy()){
                        intent.putExtra("p_lat", mLocation.getLatitude());
                        intent.putExtra("p_long", mLocation.getLongitude());
                        //createList("");
                    } else{
                        if(Double.parseDouble(sharedPreferences.getString("latitude","0.0")) != 0.0 && Double.parseDouble(sharedPreferences.getString("longitude","0.0")) != 0.0  ){
                            intent.putExtra("p_lat",Double.parseDouble(sharedPreferences.getString("latitude","0.0")));
                            intent.putExtra("p_long",Double.parseDouble(sharedPreferences.getString("longitude","0.0")));
                        }else {
                            intent.putExtra("p_lat", 15.5827389);
                            intent.putExtra("p_long", 32.5239187);
                        }
                    }
                }
                intent.putExtra("method",2);
                startActivityForResult(intent,7);
            }
        });





        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);



        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog1 = ProgressDialog.show(PList.this, getResources().getString(R.string.searching), getResources().getString(R.string.please_wait), false, false);
                progressDialog1.setCancelable(true);
                progressDialog1.setCanceledOnTouchOutside(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createList("");
                    }
                }, 500);
            }
        });



        cont = (Button)findViewById(R.id.button9);
        //pharmacies.add(0, new pharmacy("","","",0,0.0,0.0));
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PList.this);
                builder.setCancelable(true).
                        setPositiveButton(getResources().getString(R.string.Continue), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog1 = ProgressDialog.show(PList.this, getResources().getString(R.string.searching), getResources().getString(R.string.please_wait), false, false);
                                progressDialog1.setCancelable(true);
                                progressDialog1.setCanceledOnTouchOutside(false);
                                if (type == 1)
                                    createOrder(0);
                                else
                                    createOrder2(0);
                            }
                        }).setNegativeButton(getResources().getString(R.string.Back), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNeutralButton(getResources().getString(R.string.add_comment), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), Comment.class);
                        intent.putExtra("index", 0);
                        intent.putExtra("u_comment", u_comment);
                        startActivityForResult(intent, 1);

                    }
                });
                AlertDialog a = builder.create();
                a.setTitle(getResources().getString(R.string.please_confirm));
                if (pharmacies.size() > 0)
                    a.show();
                else
                    Toast.makeText(PList.this, getResources().getString(R.string.no_pharmacies), Toast.LENGTH_LONG).show();
            }
        });



        checkBox=(CheckBox)findViewById(R.id.checkBox2);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    listView = (ListView)findViewById(R.id.plistview);
                    Button cont = (Button)findViewById(R.id.button9);
                    cont.setVisibility(View.VISIBLE);
                    //pharmacies.add(0, new pharmacy("","","",0,0.0,0.0));
                    cont.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder= new AlertDialog.Builder(PList.this);
                            builder.setCancelable(true).
                                    setPositiveButton(getResources().getString(R.string.Continue), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(type==1)
                                                createOrder(0);
                                            else
                                                createOrder2(0);
                                        }
                                    }).setNegativeButton(getResources().getString(R.string.Back), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).setNeutralButton(getResources().getString(R.string.add_comment), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(),Comment.class);
                                    intent.putExtra("index",0);
                                    intent.putExtra("u_comment",u_comment);
                                    startActivityForResult(intent,1);

                                }
                            });
                            AlertDialog a= builder.create();
                            a.setTitle(getResources().getString(R.string.please_confirm_to_continue));
                            if(pharmacies.size()>0)
                                a.show();
                            else
                                Toast.makeText(PList.this,getResources().getString(R.string.no_pharmacies),Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    listView = (ListView)findViewById(R.id.plistview);
                    listView.setVisibility(View.VISIBLE);
                    Button cont = (Button)findViewById(R.id.button9);
                    cont.setVisibility(View.INVISIBLE);
                    createList("");
                }


            }
        });

    }


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }



    private void createList(String search) {


        switch (search_range.getSelectedItemPosition()){
            case 0: distance=2000.0;
                break;
            case 1: distance=5000.0;
                break;
            case 2: distance=10000.0;
                break;
        }


        SharedPreferences sharedPreferences =  getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();


        if(location_type==0) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.location_not_detrmined),Toast.LENGTH_LONG).show();
            progressDialog1.dismiss();
            return;
        }
        pharmacies.clear();
        pharmacies_distances= new JSONArray();
        Session session = new Session();
        session.getAll(PList.this);
        database db = new database(PList.this);
        try {
            final String result;
            result = db.execute("7", session.getSession_key(),search).get();

            //Toast.makeText(PList.this,result,Toast.LENGTH_LONG).show();

            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name=jsonObject.getString("name");
                city =jsonObject.getString("city");
                location=jsonObject.getString("location");
                id=Integer.parseInt(jsonObject.getString("id"));
                latitude= Double.parseDouble(jsonObject.getString("latitude"));
                longitude= Double.parseDouble(jsonObject.getString("longitude"));
                Double lat1=0.0,lat2,long1=0.0,long2,theta,dist ;
                if(location_type==1) {
                    lat1 = u_latitude;
                    long1 = u_longitude;
                    editor.putString("latitude",u_latitude.toString());
                    editor.putString("longitude",u_longitude.toString());
                }else if (location_type==2){
                    lat1 = m_latitude;
                    long1 = m_longitude;
                    editor.putString("latitude",m_latitude.toString());
                    editor.putString("longitude",m_longitude.toString());
                }

                editor.commit();

                lat2=  latitude;
                long2= longitude;

                theta=long1-long2;
                dist=Math.sin(Math.toRadians(lat1))*Math.sin(Math.toRadians(lat2))+Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*Math.cos(Math.toRadians(theta));
                dist=Math.acos(dist);
                dist=Math.toDegrees(dist);
                dist=dist * 60 * 1.1515*1.609344 * 1000;
                if(dist<=distance) {
                    pharmacies.add(new pharmacy(name, city, location, id, latitude, longitude, dist));
                    JSONObject pharmacy_distance = new JSONObject();
                    pharmacy_distance.put("pharmacy_id",id);
                    pharmacy_distance.put("distance",dist);
                    pharmacies_distances.put(pharmacy_distance);

                }
            }

            //no_of_pharmacies_layout.setVisibility(View.VISIBLE);
            no_of_pharmacies.setText(String.valueOf(pharmacies.size()));
            cont.setVisibility(View.VISIBLE);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog1.dismiss();

        ArrayAdapter<pharmacy> adapter = new pharmacyAdapter();
        listView= (ListView)findViewById(R.id.plistview);
        listView.setAdapter(adapter);

    }


    public void createOrder(int i){
        Session session = new Session();
        session.getAll(PList.this);
        user_id=session.getId();
        pharmacy_id= 0;
        String result;
        Integer order_id=0;




        database db = new database(PList.this);
        try {
            if(!checkBox.isChecked()){
                result = db.execute("6",user_id.toString(),prescription_id.toString(),pharmacy_id.toString(),session.getSession_key(),u_comment,type.toString(),"0",meds_details,total_meds_count.toString()).get();
                progressDialog1.dismiss();
            }
            else {
                String pharmas="";
                for(int w =0; w< pharmacies.size();w++)
                    pharmas=pharmas+pharmacies.get(w).getId().toString()+",";
                //Toast.makeText(PList.this,pharmas,Toast.LENGTH_LONG).show();
                result = db.execute("6", user_id.toString(), prescription_id.toString(), pharmas, session.getSession_key(), u_comment, type.toString(),"0",meds_details,total_meds_count.toString()).get();
                progressDialog1.dismiss();
            }
            order_id = Integer.parseInt(result);

            if (order_id != 0) {
                Intent intent = new Intent(getApplicationContext(), Waiting.class);
                intent.putExtra("order_id",order_id);
                intent.putExtra("pharmacies_distances",  pharmacies_distances.toString());
                startActivity(intent);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        progressDialog1.dismiss();


    }


    private void createOrder2(int j) {
        Session session = new Session();
        session.getAll(PList.this);
        user_id=session.getId();
        pharmacy_id= 0;
        String result;

        //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
        database db = new database(PList.this);
        try {
            if(!checkBox.isChecked())
                result = db.execute("8", user_id.toString(), data.toString(), pharmacy_id.toString(), session.getSession_key(),u_comment,type.toString(),"0",total_meds_count.toString()).get();
            else {
                String pharmas="";
                for(int w =0; w< pharmacies.size();w++)
                    pharmas=pharmas+pharmacies.get(w).getId().toString()+",";
                result = db.execute("8", user_id.toString(), data.toString(), pharmas, session.getSession_key(), u_comment, type.toString(),"0").get();
            }
            Integer order_id = 0;
            try {
                order_id = Integer.parseInt(result);
            } catch (NumberFormatException e) {
                Toast.makeText(PList.this,getResources().getString(R.string.connection_error),Toast.LENGTH_LONG).show();
            }
            if (order_id != 0) {
                Intent intent = new Intent(getApplicationContext(), Waiting.class);
                intent.putExtra("order_id",order_id);
                intent.putExtra("pharmacies_distances",  pharmacies_distances.toString());
                startActivity(intent);
            } else{}
             // Toast.makeText(PList.this, result, Toast.LENGTH_LONG).show();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }



    private class pharmacyAdapter extends ArrayAdapter<pharmacy> {

        public pharmacyAdapter() {

            super(PList.this,R.layout.plistcontent,pharmacies);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View pview = convertView;
            if(pview==null){
                pview= getLayoutInflater().inflate(R.layout.plistcontent,parent,false);
            }

            pharmacy p = pharmacies.get(position);

            TextView name = (TextView)pview.findViewById(R.id.pname);
            TextView city = (TextView)pview.findViewById(R.id.city);
            TextView location = (TextView)pview.findViewById(R.id.location);

            name.setText(p.getName().toString());
            city.setText(p.getCity().toString()+",");
            location.setText(p.getLocation().toString());



            return pview;
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode==1&& resultCode == Activity.RESULT_OK) {
            u_comment = intent.getStringExtra("u_comment");
            int index = intent.getIntExtra("index",1);
            //Toast.makeText(PList.this,u_comment,Toast.LENGTH_LONG).show();
            listView.performItemClick(listView.getAdapter().getView(index, null, null),
                    index,
                    listView.getAdapter().getItemId(index));

        }else if(requestCode==7&& resultCode==RESULT_OK){
            m_latitude=intent.getDoubleExtra("u_lat",0.0);
            m_longitude=intent.getDoubleExtra("u_long",0.0);
            location_message.setText(getResources().getString(R.string.location_found));
            location_message.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.correct,0,0,0);
        }
        else if(requestCode==7 && resultCode!=RESULT_OK)
            location_type=0;
    }





    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            Toast.makeText(getApplicationContext(),"Please install Google Play services.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if(mLocation!=null && mLocation.hasAccuracy())
        {
            if(location_type==1){
                progressDialog.dismiss();
                location_message.setText(getResources().getString(R.string.location_found));
                location_message.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.correct,0,0,0);
            }
            u_latitude = mLocation.getLatitude();
            u_longitude = mLocation.getLongitude();
            //createList("");
        }

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if(location!=null && location_type==1) {
            progressDialog.dismiss();
            location_message.setText(getResources().getString(R.string.location_found));
            location_message.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.correct,0,0,0);
        }
        // Toast.makeText(getApplicationContext(),String.valueOf(mLocation.getLatitude()),Toast.LENGTH_LONG).show();
        u_latitude = location.getLatitude();
        u_longitude = location.getLongitude();
        //createList("");


    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else
                finish();

            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(2000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, PList.this);


    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PList.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    public void stopLocationUpdates()
    {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    public  void setAppLocale(String localeCode){

        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf= res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}


