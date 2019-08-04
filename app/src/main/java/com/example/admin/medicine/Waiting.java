package com.example.admin.medicine;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Waiting extends AppCompatActivity {

    Integer  order_id;
    Integer status=1;
    Handler handler;
    Runnable runnable;
    String pharmacies ;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");

        setContentView(R.layout.activity_waiting);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        savedInstanceState = getIntent().getExtras();
        order_id= savedInstanceState.getInt("order_id");
        pharmacies=savedInstanceState.getString("pharmacies_distances","0");

        intent = new Intent(getApplicationContext(),Order_sending.class);

        //Toast.makeText(getApplicationContext(),pharmacies,Toast.LENGTH_LONG).show();

        Button cancel = (Button)findViewById(R.id.cancel_request);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRequest();
            }
        });


        if(!isMyServiceRunning(Order_sending.class) && !pharmacies.equals("0")) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startsending();
                }
            },200);
        }else{
            AlertDialog.Builder builder= new AlertDialog.Builder(Waiting.this);
            builder.setMessage("لا يمكنك ارسال طلب جديد حتى انتهاء الطلب الحالي\n هل تريد انهاء الطلب الجاري؟").setCancelable(true).
                    setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stopService(intent);
                            startsending();
                        }
                    }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();                }
            });
            AlertDialog a= builder.create();
            a.setTitle("تحذير");
            if(!pharmacies.equals("0"))
                a.show();
        }




        if(pharmacies.equals("0")) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(intent);
                    finish();
                }
            }, 5000);
        } else{
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtra("from_waiting",1);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    private void startsending() {
        intent.putExtra("order_id",order_id);
        intent.putExtra("pharmacies",pharmacies);
        startService(intent);
    }

    private void cancelRequest() {
        AlertDialog.Builder x = new AlertDialog.Builder(Waiting.this);
        x.setMessage("سيتم الغاء الطلب في حال الموافقه.\n هل انت متاكد").setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Session session = new Session();
                session.getAll(Waiting.this);
                database db = new database(Waiting.this);
                try {
                    stopService(intent);
                    String result = db.execute("10",order_id.toString(),session.getSession_key(),"0","0","0.0","0.0","0").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Intent home = new Intent(getApplicationContext(),Home.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(home);
            }
        }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog a = x.create();
        a.setTitle("انهاء الطلب");
        a.show();
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        Intent intent= new Intent(Waiting.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
