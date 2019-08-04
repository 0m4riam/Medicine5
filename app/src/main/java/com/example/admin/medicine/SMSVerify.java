package com.example.admin.medicine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class SMSVerify extends AppCompatActivity {

    ProgressDialog progressDialog;
    Integer code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsverify);
        savedInstanceState=getIntent().getExtras();

        code=savedInstanceState.getInt("code");

        final EditText editText = (EditText)findViewById(R.id.smsverify_code);
        Button verify = (Button)findViewById(R.id.smsverify_button);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = ProgressDialog.show(SMSVerify.this, "Verifying", getResources().getString(R.string.please_wait), false, false);
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (editText.getText().length()==0){
                            Toast.makeText(SMSVerify.this, "Please Enter Verification Number", Toast.LENGTH_SHORT).show();
                        }else{
                            if(code == Integer.parseInt(editText.getText().toString())){
                                login();
                            }
                            else{
                                Toast.makeText(SMSVerify.this, "Wrong Verification Number", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }
                }, 1000);

            }
        });


    }

    private void login() {

        Session session = new Session();
        session.getAll(SMSVerify.this);
        database db = new database(SMSVerify.this);
        String result="";

        try {
            result = db.execute("18", session.getId().toString()).get();
            if(result.equals("1")) {
                Intent x = new Intent(getApplicationContext(), Home.class);
                startActivity(x);
            }else
                Toast.makeText(session, result.toString(), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

}
