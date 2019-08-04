package com.example.admin.medicine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.icu.text.LocaleDisplayNames;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Settings extends AppCompatActivity {

    Button arabic,English;
    SharedPreferences lang;
    SharedPreferences.Editor edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");
        else {
            setAppLocale("en");
        }

        setContentView(R.layout.activity_settings);

        arabic=(Button)findViewById(R.id.arabic);
        English=(Button)findViewById(R.id.english);

        if(lang.getInt("language",1)==0){
            English.setBackgroundColor(Color.parseColor("#659EC7"));
            arabic.setBackgroundColor(Color.parseColor("#ffffffff"));
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Button logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


        Button back= (Button)findViewById(R.id.settings_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        edit=lang.edit();

        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putInt("language",1);
                edit.commit();
                Intent intent= new Intent(getApplicationContext(),Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putInt("language",0);
                edit.commit();
                Intent intent= new Intent(getApplicationContext(),Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Button password= (Button)findViewById(R.id.account_details);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Account_details.class);
                startActivity(intent);
            }
        });
    }

    private void Logout() {
        SharedPreferences sharedpreferences = getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        String result;
        database db = new database(Settings.this);
        try {
            result = db.execute("9", sharedpreferences.getString("session_key","")).get();
            Intent returnIntent = new Intent(getApplicationContext(),Login.class);
            setResult(Activity.RESULT_OK,returnIntent);
            returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(returnIntent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext(),Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public  void setAppLocale(String localeCode){

        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf= res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}
