package com.example.admin.medicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.concurrent.ExecutionException;




public class Session extends AppCompatActivity {


    String username;
    String name;
    String phone;
    String session_key= "";
    Integer isLogged;
    Integer id;


    protected void getAll(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        username = sharedpreferences.getString("username","");
        name = sharedpreferences.getString("name","");
        phone = sharedpreferences.getString("phone","");
        session_key = sharedpreferences.getString("session_key","");
        isLogged = sharedpreferences.getInt("islogged",0);
        id=sharedpreferences.getInt("id",0);
    }



    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSession_key() {
        return session_key;
    }

    public Integer getIsLogged() {
        return isLogged;
    }

    public Integer getId() {
        return id;
    }





    public void set(String username,String name, String phone,String session_key){
        SharedPreferences sharedpreferences = getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putInt("islogged", 1);
        edit.putString("username", username);
        edit.putString("name", name);
        edit.putString("phone", phone);
        edit.putString("session_key", session_key);
        edit.commit();

    }







}
