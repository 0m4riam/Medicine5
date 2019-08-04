package com.example.admin.medicine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Account_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Session session = new Session();
        session.getAll(Account_details.this);

        TextView username= (TextView)findViewById(R.id.account_details_username);
        TextView name= (TextView)findViewById(R.id.account_details_name);
        TextView phone= (TextView)findViewById(R.id.account_details_phone);

        username.setText(session.getUsername());
        name.setText(session.getName());
        phone.setText(session.getPhone());

        final Button change_password= (Button)findViewById(R.id.account_details_change_password);

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),change_password.class);
                startActivity(intent);
            }
        });

    }
}
