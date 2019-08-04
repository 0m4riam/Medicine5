package com.example.admin.medicine;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class change_password extends AppCompatActivity {

    String session_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final EditText old_password=(EditText)findViewById(R.id.old_password);
        final EditText new_password=(EditText)findViewById(R.id.new_password);
        final EditText new_password_again=(EditText)findViewById(R.id.new_password_again);

        Button change_password=(Button)findViewById(R.id.button8);


        Session session= new Session();
        session.getAll(getApplicationContext());
        session_key= session.getSession_key();

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword(old_password.getText().toString(),new_password.getText().toString(),new_password_again.getText().toString());
            }
        });

    }

    private void changePassword(String old_password , String new_password, String new_password_again) {

        if(!new_password.equals(new_password_again)){
            Toast.makeText(this, "password doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }else{
            //Toast.makeText(this, session_key, Toast.LENGTH_SHORT).show();
            String result;
            database db = new database(change_password.this);
            try {
                result = db.execute("17",session_key, old_password,new_password).get();

                //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

                if(result.equals("1")){
                    AlertDialog.Builder x = new AlertDialog.Builder(change_password.this);
                    x.setMessage(getResources().getString(R.string.password_changed)).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setCancelable(false);
                    AlertDialog alertDialog= x.create();
                    alertDialog.show();
                }else if(result.equals("0")){
                    Toast.makeText(this,getResources().getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }
    }
}
