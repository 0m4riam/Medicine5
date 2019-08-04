package com.example.admin.medicine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Register extends AppCompatActivity {
    Context ctx;

    EditText name,phone,username,password,verif;
    TextView captcha;
    Button register;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=getApplication();
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");


        setContentView(R.layout.activity_register);




        name = (EditText)findViewById(R.id.editText);
        phone =(EditText)findViewById(R.id.editText2);
        username = (EditText)findViewById(R.id.editText3);
        password = (EditText)findViewById(R.id.editText4);
        verif = (EditText)findViewById(R.id.editText5);
        name.setText(null);
        captcha = (TextView)findViewById(R.id.textView);

        register = (Button) findViewById(R.id.button);

        //name.setText("a");

        String rand = random_captcha();
        captcha.setText(rand.toString());
        captcha.setTextSize(28);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("")){
                    Toast.makeText(Register.this,getResources().getString(R.string.name_field),Toast.LENGTH_SHORT).show();
                }else if (phone.getText().toString().length()!=10){
                    Toast.makeText(Register.this,getResources().getString(R.string.phone_field),Toast.LENGTH_SHORT).show();
                }else if (!phone.getText().toString().matches("(09|01).*")){
                    Toast.makeText(Register.this,getResources().getString(R.string.phone_field),Toast.LENGTH_SHORT).show();
                } else if (username.getText().toString().equals("")){
                    Toast.makeText(Register.this,getResources().getString(R.string.username_field),Toast.LENGTH_SHORT).show();
                }else if (password.getText().toString().equals("")){
                    Toast.makeText(Register.this,getResources().getString(R.string.password_field),Toast.LENGTH_SHORT).show();
                }else if (!verif.getText().toString().equals(captcha.getText().toString()) ){
                    Toast.makeText(Register.this,getResources().getString(R.string.wrong_captcha),Toast.LENGTH_SHORT).show();
                    String rand = random_captcha();
                    captcha.setText(rand.toString());
                }else {
                    progressDialog = ProgressDialog.show(Register.this, getResources().getString(R.string.connecting), getResources().getString(R.string.please_wait), false, false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            createAccount();
                        }
                    }, 500);
                }

            }
        });



    }

    public void createAccount(){
        try {
            String result;
            database db = new database(Register.this);
            result=db.execute("1",name.getText().toString(),phone.getText().toString(),username.getText().toString(),password.getText().toString()).get();
            progressDialog.dismiss();
            if(result.equals("New record created successfully ")){
                AlertDialog.Builder alert = new AlertDialog.Builder(Register.this);
                alert.setMessage("account has been created.").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        });
                AlertDialog dialog = alert.create();
                dialog.setTitle("Congrats");
                dialog.show();

            }
            else if(result.contains("Duplicate") && result.contains("username_2")){
                Toast.makeText(Register.this,"username is used",Toast.LENGTH_LONG).show();
            }else if(result.contains("Duplicate")){
                Toast.makeText(Register.this,"phone is used",Toast.LENGTH_LONG).show();
            }
            else if(result.equals("error")){
                Toast.makeText(Register.this,getResources().getString(R.string.connection_error),Toast.LENGTH_LONG).show();
            }
            else {
                // Toast.makeText(Register.this,result,Toast.LENGTH_LONG).show();
            }

        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        //error=db.createUser();
        //Toast.makeText(Register.this,error,Toast.LENGTH_LONG).show();
    }





    public String random_captcha(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public Context getContext(){
        return ctx;
    }

    public  void setAppLocale(String localeCode){

        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf= res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}


