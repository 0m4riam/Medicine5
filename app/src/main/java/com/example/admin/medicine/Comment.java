package com.example.admin.medicine;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.CompletionException;

public class Comment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        savedInstanceState=getIntent().getExtras();
        final int x = savedInstanceState.getInt("index");

        Toast.makeText(Comment.this,String.valueOf(x),Toast.LENGTH_LONG).show();

        final EditText u_comment =(EditText)findViewById(R.id.u_comment);
        Button add_comment=(Button)findViewById(R.id.add_comment);

        u_comment.setText(savedInstanceState.getString("u_comment"));

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("u_comment",u_comment.getText().toString());
                returnIntent.putExtra("index",x);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
