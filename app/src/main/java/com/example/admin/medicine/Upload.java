package com.example.admin.medicine;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Upload extends AppCompatActivity {

    Boolean end=Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        savedInstanceState = getIntent().getExtras();

        final Session session = new Session();
        session.getAll(Upload.this);
        final String date = savedInstanceState.getString("date");
        final String session_key =session.getSession_key();
        for (Integer counter=0;counter<3;counter++)
            {
                if(end)
                    break;
                final Integer finalCounter = counter;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkUpload(date,session_key);
                        if (finalCounter ==12) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result", 0);
                            setResult(3, returnIntent);
                            finish();
                        }
                    }
                }, 5000*counter);
            }
        //Toast.makeText(Upload.this, "123123123123", Toast.LENGTH_SHORT).show();


    }


    public void checkUpload(String date,String session_key){
        database db = new database(Upload.this);
        try {
            String result = db.execute("5", date,session_key).get();
            if (Integer.parseInt(result)>0) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", Integer.parseInt(result));
                setResult(3, returnIntent);
                end=Boolean.TRUE;
                finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
