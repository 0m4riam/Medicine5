package com.example.admin.medicine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Home extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    String [] values= {
            "الأدوية",
            "الأم و الطفل",
            "الشعر",
            "الاستحمام",
            "صحة الفم",
            "جمال",
    };

    int [] images = {
            R.drawable.meds,
            R.drawable.mother,
            R.drawable.hair,
            R.drawable.shower,
            R.drawable.teeth,
            R.drawable.beauthy,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");
        else
            setAppLocale("en");
        savedInstanceState=getIntent().getExtras();

        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GridView grid = (GridView)findViewById(R.id.grid);

        TextView health= (TextView)findViewById(R.id.button10);
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Health.class);
                startActivity(intent);
            }
        });


        TextView old= (TextView)findViewById(R.id.button11);
        old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),List.class);
                startActivity(intent);
            }
        });

        TextView medicines= (TextView)findViewById(R.id.medicines);
        medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Others.class);
                intent.putExtra("type",3);
                startActivity(intent);
            }
        });


        GridAdapter gridAdapter = new GridAdapter(this,values,images);

        grid.setAdapter(gridAdapter);


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(),Others.class);
                    intent.putExtra("type",position+3);
                    startActivity(intent);
            }
        });


        TextView settings = (TextView)findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(),Settings.class);
                startActivityForResult(x,1);
            }
        });

        TextView exit=(TextView) findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doubleBackToExitPressedOnce=true;
                onBackPressed();
            }
        });

        if(savedInstanceState!=null)
            if(savedInstanceState.getInt("from_waiting",0)==1){
                AlertDialog.Builder builder= new AlertDialog.Builder(Home.this);
                builder.setMessage(getResources().getString(R.string.order_sent)).setCancelable(true).
                        setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(),List.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog a= builder.create();
                a.show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            finish();
        }
    }




    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public  void setAppLocale(String localeCode){

        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf= res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}
