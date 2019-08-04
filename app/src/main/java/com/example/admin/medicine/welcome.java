package com.example.admin.medicine;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class welcome extends AppCompatActivity {

    private ImageView wel;
    SharedPreferences.Editor edit;
    SharedPreferences sharedpreferences;
    String token;
    Boolean doubleBackToExitPressedOnce=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        wel = (ImageView) findViewById(R.id.wel);
        Animation myanim = AnimationUtils.loadAnimation(this , R.anim.mytransition);
        wel.startAnimation(myanim);
        final Intent i =new Intent(this,Login.class);
        token = FirebaseInstanceId.getInstance().getToken();

       /* Thread timer= new Thread(){
            public void run()
            {
                try
                {
                    sleep(4000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }finally
                {
                    if(sharedpreferences.getInt("islogged",0)==1) {
                        welcome.AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new welcome.AsyncTaskUploadClass();
                        AsyncTaskUploadClassOBJ.execute();
                    }else {
                        startActivity(i);
                        finish();
                    }
                }
            }
        };*/

        sharedpreferences = getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        edit = sharedpreferences.edit();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedpreferences.getInt("islogged",0)==1) {
                    welcome.AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new welcome.AsyncTaskUploadClass();
                    AsyncTaskUploadClassOBJ.execute();
                }else {
                    startActivity(i);
                    finish();
                }
            }
        }, 4000);

    }




    class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            // Showing progress dialog at image upload time.
        }

        @Override
        protected void onPostExecute(String string1) {

            super.onPostExecute(string1);

            // Dismiss the progress dialog after done uploading.

            // Printing uploading success message coming from server on android app.

            if(string1.equals("1")){
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                finish();
            }else if (string1.equals("deactivated")) {
                Toast.makeText(welcome.this, getResources().getString(R.string.account_deactivated), Toast.LENGTH_LONG).show();
                edit.putInt("islogged", 0);
                edit.commit();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }else if(string1.equals("0")){
                //Toast.makeText(welcome.this, string1, Toast.LENGTH_LONG).show();
                edit.putInt("islogged", 0);
                edit.commit();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }else if(string1.equals("connectivity issue")){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                finish();
            }


        }

        @Override
        protected String doInBackground(Void... params) {
            String result= "";
            result=login();
            Log.d("back","ground");
            try{
                Object json = new JSONTokener(result.toString()).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject userobj = (JSONObject) json;
                    edit.putInt("islogged", 1);
                    edit.putString("username", userobj.getString("username"));
                    edit.putString("name", userobj.getString("name"));
                    edit.putString("phone", userobj.getString("phone"));
                    edit.putInt("id", Integer.parseInt(userobj.getString("id")));
                    edit.commit();

                    return "1";

                } else{
                    return result;
                }
            }catch (JSONException e) {
                e.printStackTrace();
                return result;
            }
        }
    }


    public String login() {

        String url_string = "http://"+getResources().getString(R.string.server_ip)+"/loginget.php";
        try {
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(20000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream out = connection.getOutputStream();
            BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
            String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                    URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(token,"UTF-8")+"&"+
                    URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(sharedpreferences.getString("session_key",""),"UTF-8");
            bufferedWriter.write(post);
            long requestStratTime = new Date().getTime();
            long requestEndTime = new Date().getTime();
            long timeOfRequest = (requestEndTime - requestStratTime) / 1000;
            if ( timeOfRequest > 10) {

                return "aaa";
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            out.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "", result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            connection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "connectivity issue";
        }
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
