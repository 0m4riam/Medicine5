package com.example.admin.medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AppCompatActivity {

    ProgressDialog progressDialog;
    String result;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor edit;
    String user,pass;
    String token;
    private static final String TAG = "Login";
    Integer code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");

        setContentView(R.layout.activity_login);


        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: " + token);

        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);
        Button login = (Button)findViewById(R.id.login);
        TextView register = (TextView) findViewById(R.id.register);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedpreferences = getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
                edit = sharedpreferences.edit();
                if(sharedpreferences.getInt("islogged",0)==1) {
                    login("","");
                }
            }
        }, 100);




         login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                //user=sharedpreferences.getString("user","");
                //pass=sharedpreferences.getString("pass","");
                user=username.getText().toString();
                pass=password.getText().toString();
                login(username.getText().toString(),password.getText().toString());
                password.setText("");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getApplicationContext(),Register.class);
                startActivity(x);
                /*try {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.cool.unblock"));
                    startActivity(viewIntent);
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(),"Unable to Connect Try Again...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }*/
            }
        });

    }







    public void login(String username,String password){
        sharedpreferences = getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        edit = sharedpreferences.edit();


        if(sharedpreferences.getInt("islogged",0)==1){

            try {
                String result;
                database db = new database(Login.this);
                result = db.execute("3", sharedpreferences.getString("session_key",""),token).get();
                //Toast.makeText(Login.this,result,Toast.LENGTH_LONG).show();
                Object json = new JSONTokener(result.toString()).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject userobj = (JSONObject) json;
                    edit.putInt("islogged", 1);
                    edit.putString("username", userobj.getString("username"));
                    edit.putString("name", userobj.getString("name"));
                    edit.putString("phone", userobj.getString("phone"));
                    edit.putInt("id",Integer.parseInt(userobj.getString("id")));
                    edit.commit();

                    if(Integer.parseInt(userobj.getString("sms"))==1){
                        Intent x = new Intent(getApplicationContext(), SMSVerify.class);
                        x.putExtra("code",Integer.parseInt(userobj.getString("sms_code")));
                        startActivity(x);
                    }else{
                        Intent x = new Intent(getApplicationContext(), Home.class);
                        startActivity(x);
                    }
                }else if(result.equals("deactivated")){
                    Toast.makeText(Login.this,getResources().getString(R.string.account_deactivated),Toast.LENGTH_LONG).show();
                    edit.putInt("islogged", 0);
                    edit.commit();
                }else if(result.equals("0")) {
                    Toast.makeText(Login.this, getResources().getString(R.string.account_deactivated), Toast.LENGTH_LONG).show();
                    edit.putInt("islogged", 0);
                    edit.commit();
                }else{
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }else {
                AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                AsyncTaskUploadClassOBJ.execute();
            }



    }

    private String loginrequest() {
        StringBuilder stringBuilder = new StringBuilder();

        try {

            URL url;
            HttpURLConnection httpURLConnectionObject ;
            OutputStream OutPutStream;
            BufferedWriter bufferedWriterObject ;
            BufferedReader bufferedReaderObject ;
            int RC ;

            url = new URL("http://"+getResources().getString(R.string.server_ip)+"/login.php");

            httpURLConnectionObject = (HttpURLConnection) url.openConnection();

            httpURLConnectionObject.setReadTimeout(19000);

            httpURLConnectionObject.setConnectTimeout(10000);

            httpURLConnectionObject.setRequestMethod("POST");

            httpURLConnectionObject.setDoInput(true);

            httpURLConnectionObject.setDoOutput(true);

            OutPutStream = httpURLConnectionObject.getOutputStream();

            bufferedWriterObject = new BufferedWriter(

                    new OutputStreamWriter(OutPutStream, "UTF-8"));

            bufferedWriterObject.write(URLEncoder.encode("auth","UTF-8")+"=1111&"+
                    URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(user,"UTF-8")+"&"+
                    URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(token,"UTF-8")+"&"+
                    URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8"));

            bufferedWriterObject.flush();

            bufferedWriterObject.close();

            OutPutStream.close();

            RC = httpURLConnectionObject.getResponseCode();

            if (RC == HttpsURLConnection.HTTP_OK) {

                bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                stringBuilder = new StringBuilder();

                String RC2;

                while ((RC2 = bufferedReaderObject.readLine()) != null){

                    stringBuilder.append(RC2);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }



    class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            // Showing progress dialog at image upload time.
            progressDialog = ProgressDialog.show(Login.this, "connecting", "Please Wait", false, false);
        }

        @Override
        protected void onPostExecute(String string1) {

            super.onPostExecute(string1);

            // Dismiss the progress dialog after done uploading.
            progressDialog.dismiss();

            // Printing uploading success message coming from server on android app.
            if(string1.equals("1")){
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
            }else if(string1.equals("5")){
                Intent x = new Intent(getApplicationContext(), SMSVerify.class);
                x.putExtra("code",code);
                startActivity(x);
            }else if (string1.equals("deactivated")) {
                Toast.makeText(Login.this, getResources().getString(R.string.account_deactivated), Toast.LENGTH_LONG).show();
                edit.putInt("islogged", 0);
                edit.commit();
            } else if (string1.equals("0")) {
                Toast.makeText(Login.this, getResources().getString(R.string.wrong_credintials), Toast.LENGTH_LONG).show();
                edit.putInt("islogged", 0);
                edit.commit();
            }else if (string1.equals("-1")) {
                Toast.makeText(Login.this, getResources().getString(R.string.update), Toast.LENGTH_LONG).show();
                edit.putInt("islogged", 0);
                edit.commit();
            }else{
                Toast.makeText(Login.this, getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                edit.putInt("islogged", 0);
                edit.commit();
            }


        }

        @Override
        protected String doInBackground(Void... params) {
            result = loginrequest();
            try{
                Object json = new JSONTokener(result.toString()).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject userobj = (JSONObject) json;
                    edit.putInt("islogged", 1);
                    edit.putString("username", userobj.getString("username"));
                    edit.putString("name", userobj.getString("name"));
                    edit.putString("phone", userobj.getString("phone"));
                    edit.putInt("id", Integer.parseInt(userobj.getString("id")));
                    edit.putString("session_key", userobj.getString("session_key"));
                    edit.commit();

                    if(Integer.parseInt(userobj.getString("sms"))==1){
                        code=Integer.parseInt(userobj.getString("sms_code"));
                        return "5";
                    }else{
                        return "1";
                    }


                } else{
                    return result;
                }
            }catch (JSONException e) {
                e.printStackTrace();
                return "-2";
            }
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
