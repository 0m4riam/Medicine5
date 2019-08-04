package com.example.admin.medicine;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class List extends AppCompatActivity {


    ListView listView;
    CheckBox checkBox;
    java.util.List<com.example.admin.medicine.request> requests = new ArrayList<request>();
    ProgressDialog progressDialog;
    TextView no_current_request;
    Button running_requests,old_requests,refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");
        else
            setAppLocale("en");


        setContentView(R.layout.activity_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createList();
            }
        });

        progressDialog = ProgressDialog.show(List.this, getResources().getString(R.string.searching), getResources().getString(R.string.please_wait), false, false);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        no_current_request=(TextView)findViewById(R.id.no_current_request);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                createList();
            }
        }, 500);

        running_requests = (Button)findViewById(R.id.list_running_requests);
        old_requests=(Button)findViewById(R.id.list_old_requests);
        refresh=(Button)findViewById(R.id.refresh);

        running_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh.setVisibility(View.VISIBLE);
                running_requests.setBackgroundColor(Color.parseColor("#000088"));
                running_requests.setTextColor(Color.parseColor("#FFFFFF"));
                old_requests.setBackgroundColor(Color.parseColor("#dddddd"));
                old_requests.setTextColor(Color.parseColor("#555555"));
                progressDialog = ProgressDialog.show(List.this, getResources().getString(R.string.searching), getResources().getString(R.string.please_wait), false, false);
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                no_current_request=(TextView)findViewById(R.id.no_current_request);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createList();
                    }
                }, 500);
            }
        });


        old_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh.setVisibility(View.GONE);
                old_requests.setBackgroundColor(Color.parseColor("#000088"));
                old_requests.setTextColor(Color.parseColor("#FFFFFF"));
                running_requests.setBackgroundColor(Color.parseColor("#dddddd"));
                running_requests.setTextColor(Color.parseColor("#555555"));
                progressDialog = ProgressDialog.show(List.this, getResources().getString(R.string.searching), getResources().getString(R.string.please_wait), false, false);
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                no_current_request=(TextView)findViewById(R.id.no_current_request);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createList2();
                    }
                }, 500);
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running_requests.setBackgroundColor(Color.parseColor("#000088"));
                running_requests.setTextColor(Color.parseColor("#FFFFFF"));
                old_requests.setBackgroundColor(Color.parseColor("#dddddd"));
                old_requests.setTextColor(Color.parseColor("#555555"));
                progressDialog = ProgressDialog.show(List.this, getResources().getString(R.string.searching), getResources().getString(R.string.please_wait), false, false);
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                no_current_request=(TextView)findViewById(R.id.no_current_request);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createList();
                    }
                }, 500);
            }
        });

    }

    private void createList() {
        requests.clear();

        Session session = new Session();
        session.getAll(List.this);
        database db = new database(List.this);
        String result="";

        no_current_request.setText(getResources().getString(R.string.no_request_fount));
        no_current_request.setVisibility(View.VISIBLE);


        try {
            result = db.execute("16", session.getSession_key()).get();

            //Toast.makeText(PList.this,result,Toast.LENGTH_LONG).show();

            JSONArray jsonArray = new JSONArray(result);
            //Toast.makeText(List.this, result, Toast.LENGTH_LONG).show();
            int i;
            for (i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");
                //String p_name = jsonObject.getString("name");
                Integer prescription_id = Integer.valueOf(jsonObject.getString("prescription_id"));
                Integer status = Integer.valueOf(jsonObject.getString("status"));
                Integer order_id = Integer.valueOf(jsonObject.getString("id"));
                Double p_lat = Double.valueOf(jsonObject.getString("latitude"));
                Double p_long = Double.valueOf(jsonObject.getString("longitude"));
                Double price = Double.valueOf(jsonObject.getString("price"));
                Integer request_type=Integer.valueOf(jsonObject.getString("request_type"));
                if(isMyServiceRunning(Order_sending.class) && i ==0) {
                    requests.add(0,new request("",date,request_type,order_id,20,p_lat,p_long,price,prescription_id));
                    no_current_request.setVisibility(View.GONE);
                }
            }
        } catch (InterruptedException e) {
            //Toast.makeText(List.this, result, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            // Toast.makeText(List.this, result, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(List.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        progressDialog.dismiss();

        ArrayAdapter<request> adapter = new List.requestAdapter();
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(),Waiting.class);
                intent.putExtra("order_id",requests.get(position).getOrder_id());
                startActivity(intent);

            }
        });

    }



    private void createList2() {
        requests.clear();

        Session session = new Session();
        session.getAll(List.this);
        database db = new database(List.this);
        String result="";

        no_current_request.setText(getResources().getString(R.string.no_request_fount));
        no_current_request.setVisibility(View.VISIBLE);

        try {
            result = db.execute("16", session.getSession_key()).get();

            //Toast.makeText(PList.this,result,Toast.LENGTH_LONG).show();

            JSONArray jsonArray = new JSONArray(result);
            //Toast.makeText(List.this, result, Toast.LENGTH_LONG).show();
            int i;
            for (i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");
                //String p_name = jsonObject.getString("name");
                Integer prescription_id = Integer.valueOf(jsonObject.getString("prescription_id"));
                Integer status = Integer.valueOf(jsonObject.getString("status"));
                Integer order_id = Integer.valueOf(jsonObject.getString("id"));
                Double p_lat = Double.valueOf(jsonObject.getString("latitude"));
                Double p_long = Double.valueOf(jsonObject.getString("longitude"));
                Double price = Double.valueOf(jsonObject.getString("price"));
                Integer request_type=Integer.valueOf(jsonObject.getString("request_type"));
                if(!isMyServiceRunning(Order_sending.class)) {
                    requests.add(new request("",date,request_type,order_id,status,p_lat,p_long,price,prescription_id));
                    no_current_request.setVisibility(View.GONE);
                }else if(i!=0){
                    requests.add(new request("",date,request_type,order_id,status,p_lat,p_long,price,prescription_id));
                    no_current_request.setVisibility(View.GONE);
                }

            }
        } catch (InterruptedException e) {
            //Toast.makeText(List.this, result, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            // Toast.makeText(List.this, result, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(List.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        progressDialog.dismiss();

        ArrayAdapter<request> adapter = new List.requestAdapter();
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //if(requests.get(position).getStatus()==3){
                    Intent intent= new Intent(getApplicationContext(),Final1.class);
                    intent.putExtra("order_id",requests.get(position).getOrder_id());
                    intent.putExtra("prescription_id",requests.get(position).getPrescription_id());
                    intent.putExtra("status",requests.get(position).getStatus());
                    startActivity(intent);
                //}

            }
        });

    }


    private class requestAdapter extends ArrayAdapter<request> {

        public requestAdapter() {

            super(List.this, R.layout.listcontent, requests);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View pview = convertView;
            if (pview == null) {
                pview = getLayoutInflater().inflate(R.layout.listcontent, parent, false);
            }

            TextView status = (TextView) pview.findViewById(R.id.textView31);
            TextView date = (TextView) pview.findViewById(R.id.textView29);
            TextView request_type = (TextView) pview.findViewById(R.id.textView30);

            TextView o_id = (TextView) pview.findViewById(R.id.textView333);



            String id=  Integer.toHexString(requests.get(position).getOrder_id()*9999);
            o_id.setText(id);
            date.setText(requests.get(position).getDate());

            if(requests.get(position).getRequest_type()==1)
                request_type.setText(getResources().getString(R.string.prescription_order));
            else
                request_type.setText(getResources().getString(R.string.normal_order));


            switch (requests.get(position).getStatus()){

                case 0: status.setText(getResources().getString(R.string.cancelled));
                    break;

                case 1: status.setText(getResources().getString(R.string.incomplete));
                    break;

                case 2: status.setText(getResources().getString(R.string.incomplete));
                    break;

                case 3: status.setText(getResources().getString(R.string.complete));
                    break;


                case 20: status.setText(getResources().getString(R.string.searching));
            }



            return pview;
        }


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public  void setAppLocale(String localeCode){

        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf= res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }

}
