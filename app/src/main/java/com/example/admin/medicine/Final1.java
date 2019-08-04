package com.example.admin.medicine;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.squareup.picasso.Picasso;


public class Final1 extends AppCompatActivity {

    Integer order_id, prescription_id,status;
    TextView order_id_field,pharmacy_name_field,total_price_field,pharmacy_location_field;
    Button button;
    double latitude,longitude,total_price=0;
    String pharmacy_name;
    ProgressDialog progressDialog;
    ListView listView;

    java.util.List<com.example.admin.medicine.prices> prices = new ArrayList<prices>();
    java.util.List<String> amounts = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppLocale("ar");

        SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");
        else {
            setAppLocale("en");
        }

        setContentView(R.layout.activity_final1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        savedInstanceState = getIntent().getExtras();

        order_id=savedInstanceState.getInt("order_id");
        prescription_id=savedInstanceState.getInt("prescription_id");
        status=savedInstanceState.getInt("status");

        order_id_field= (TextView)findViewById(R.id.final_order_id);
        pharmacy_name_field=(TextView)findViewById(R.id.final_pharmacy_name);
        pharmacy_location_field=(TextView)findViewById(R.id.final_pharmacy_location);
        total_price_field=(TextView)findViewById(R.id.final_price);
        listView=(ListView)findViewById(R.id.final_list);

        progressDialog = ProgressDialog.show(Final1.this, getResources().getString(R.string.connecting),getResources().getString(R.string.please_wait), false, false);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 500);
    }




    private void getData() {

        Session session = new Session();
        session.getAll(Final1.this);
        database db = new database(Final1.this);
        String result="";


        try {
            result = db.execute("15", session.getSession_key(),order_id.toString(),status.toString()).get();

            //Toast.makeText(PList.this,result,Toast.LENGTH_LONG).show();

            JSONArray jsonArray = new JSONArray(result);
            //Toast.makeText(List.this, result, Toast.LENGTH_LONG).show();
            int i;
            prices.add(new prices(getResources().getString(R.string.med_name),getResources().getString(R.string.price)));
            for (i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(status==3) {
                    pharmacy_name = jsonObject.getString("name");
                    latitude = Double.valueOf(jsonObject.getString("latitude"));
                    longitude = Double.valueOf(jsonObject.getString("longitude"));
                }else{
                    pharmacy_name=getResources().getString(R.string.not_exist);
                    latitude=0.0;
                    longitude=0.0;
                    pharmacy_location_field.setVisibility(View.GONE);
                    TextView textView = (TextView)findViewById(R.id.textView14);
                    textView.setVisibility(View.INVISIBLE);
                    if(prescription_id!=1){
                        ImageView image=(ImageView)findViewById(R.id.final1_precription);
                        //image.setBackgroundColor(Color.parseColor("#000000"));
                        listView.setVisibility(View.INVISIBLE);
                        ProgressBar progressBar=(ProgressBar)findViewById(R.id.final1_progressbar);
                        progressBar.setVisibility(View.VISIBLE);
                        Picasso.with(Final1.this).load("http://"+getResources().getString(R.string.server_ip)+"/image.php?session_id="+session.getSession_key()+
                                "&prescription_id="+prescription_id.toString()).fit().into(image);

                    }
                }

                    //String p_name = jsonObject.getString("name");
                    Integer price = Integer.valueOf(jsonObject.getString("price"));
                    String med_name = jsonObject.getString("med_name");

                    prices.add(new prices(med_name, price.toString()));
                    total_price += price;

            }

            ArrayAdapter<prices> adapter= new Final1.pricesAdapter();
            listView.setAdapter(adapter);
        } catch (InterruptedException e) {
            //Toast.makeText(List.this, result, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            // Toast.makeText(List.this, result, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (JSONException e) {
            pharmacy_name=getResources().getString(R.string.not_exist);
            latitude=0.0;
            longitude=0.0;
            pharmacy_location_field.setVisibility(View.GONE);
            TextView textView = (TextView)findViewById(R.id.textView14);
            textView.setVisibility(View.INVISIBLE);
            ImageView image=(ImageView)findViewById(R.id.final1_precription);
            //image.setBackgroundColor(Color.parseColor("#000000"));
            listView.setVisibility(View.INVISIBLE);
            ProgressBar progressBar=(ProgressBar)findViewById(R.id.final1_progressbar);
            progressBar.setVisibility(View.VISIBLE);
            Picasso.with(Final1.this).load("http://"+getResources().getString(R.string.server_ip)+"/image.php?session_id="+session.getSession_key()+
                    "&prescription_id="+prescription_id.toString()).fit().into(image);
            e.printStackTrace();
        }

        progressDialog.dismiss();

        String id=  Integer.toHexString(order_id*9999);

        order_id_field.setText(id);
        pharmacy_name_field.setText(pharmacy_name);
        total_price_field.setText(String.valueOf(total_price));
        pharmacy_location_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map= new Intent(getApplicationContext(),Map.class);
                map.putExtra("method",1);
                map.putExtra("p_lat",latitude);
                map.putExtra("p_long",longitude);
                startActivity(map);
            }
        });



    }


    public class pricesAdapter extends ArrayAdapter<prices> {

        public pricesAdapter() {
            super(Final1.this, R.layout.pricescontent, prices);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View pview = convertView;
            if (pview == null) {
                pview = getLayoutInflater().inflate(R.layout.pricescontent, parent, false);
            }

            TextView product_name= (TextView)pview.findViewById(R.id.textView18);
            TextView product_price= (TextView)pview.findViewById(R.id.textView16);

            product_name.setText(prices.get(position).getName());
            if(position==0){
                product_name.setGravity(Gravity.CENTER);
                product_name.setTypeface(null, Typeface.BOLD);
                product_price.setTypeface(null, Typeface.BOLD);
            }
            product_price.setText(prices.get(position).getPrice());
            if(prices.get(position).getPrice().equals("0"))
                product_price.setText(getResources().getString(R.string.na));


            return pview;
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