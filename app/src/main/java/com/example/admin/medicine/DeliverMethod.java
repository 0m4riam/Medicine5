package com.example.admin.medicine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.client.*;

public class DeliverMethod extends AppCompatActivity {

    RelativeLayout layout1;
    Double p_lat,p_long,u_lat=0.0,u_long=0.0,distance;
    Integer order_id,method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_method);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        savedInstanceState= getIntent().getExtras();
        p_lat=savedInstanceState.getDouble("p_lat");
        p_long=savedInstanceState.getDouble("p_long");
        order_id=savedInstanceState.getInt("order_id");


        final Button btn1 = (Button)findViewById(R.id.button4);
        final Button btn2 = (Button)findViewById(R.id.button5);
        final Button btn3 = (Button)findViewById(R.id.button6);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),Map.class);
                intent.putExtra("p_lat",p_lat);
                intent.putExtra("p_long",p_long);
                intent.putExtra("method",method);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),Map.class);
                intent.putExtra("p_lat",p_lat);
                intent.putExtra("p_long",p_long);
                intent.putExtra("method",method);
                startActivityForResult(intent,1);            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(method==1){
                    updateOrder();
                    Intent intent = new Intent(getApplicationContext(),Final1.class);
                    intent.putExtra("order_id",order_id);
                    intent.putExtra("p_lat",p_lat);
                    intent.putExtra("p_long",p_long);
                    intent.putExtra("method",method);
                    startActivity(intent);
                }
                else if (method==2){
                    if(u_lat==0.0 || u_long==0.0){
                        Toast.makeText(DeliverMethod.this,"please choose location",Toast.LENGTH_LONG).show();
                    }else{
                        updateOrder2();
                        Intent intent = new Intent(getApplicationContext(),Final1.class);
                        intent.putExtra("order_id",order_id);
                        intent.putExtra("p_lat",p_lat);
                        intent.putExtra("p_long",p_long);
                        intent.putExtra("method",method);
                        startActivity(intent);
                    }
                }
            }
        });


        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.group1);
        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton= (RadioButton)findViewById(i);
                if(radioButton.getText().equals("التوصيل")) {
                    //btn1.setVisibility(View.INVISIBLE);
                    //btn2.setVisibility(View.VISIBLE);
                    //btn3.setVisibility(View.VISIBLE);
                    //method=2;
                    radioGroup.check(R.id.radioButton);
                    Toast.makeText(getApplicationContext(),"سيتم تفعيل هذا الخيار لاحقا",Toast.LENGTH_LONG).show();
                }
                else {
                    btn1.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.INVISIBLE);
                    btn3.setVisibility(View.VISIBLE);
                    method=1;
                }
            }
        });

        Session session = new Session();
        session.getAll(DeliverMethod.this);
        database db = new database(DeliverMethod.this);
        try {
            String result = db.execute("11",order_id.toString(),session.getSession_key(),"d_comment").get();
            TextView comment = (TextView)findViewById(R.id.textView25);
            if(!result.equals("0") && !result.equals(""))
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                comment.setText(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




    }

    private void updateOrder() {
        Session session = new Session();
        session.getAll(DeliverMethod.this);
        database db = new database(DeliverMethod.this);
        try {
            String result = db.execute("10",order_id.toString(),session.getSession_key(),"3",method.toString(),"0.0","0.0","0.0").get();
            //Toast.makeText(DeliverMethod.this,result,Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }



    private void updateOrder2() {
        Double d_price=10+distance*3;
        Session session = new Session();
        session.getAll(DeliverMethod.this);
        database db = new database(DeliverMethod.this);
        try {
            String result = db.execute("10",order_id.toString(),session.getSession_key(),"3",method.toString(),u_lat.toString(),u_long.toString(),d_price.toString()).get();
            //Toast.makeText(DeliverMethod.this,result,Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        u_lat=data.getDoubleExtra("u_lat",0.0);
        u_long=data.getDoubleExtra("u_long",0.0);
        //Toast.makeText(DeliverMethod.this,u_lat.toString(),Toast.LENGTH_LONG).show();
        DeliverMethod.AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new DeliverMethod.AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    ProgressDialog progressDialog;
    class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            // Showing progress dialog at image upload time.
            progressDialog = ProgressDialog.show(DeliverMethod.this, "connection", "Please Wait", false, false);
        }

        @Override
        protected void onPostExecute(String string1) {

            super.onPostExecute(string1);

            // Dismiss the progress dialog after done uploading.
            progressDialog.dismiss();

            // Printing uploading success message coming from server on android app.
            if(string1.split(" ")[1].equals("km")){
              distance = Double.parseDouble(string1.split(" ")[0]);
                Toast.makeText(DeliverMethod.this,String.valueOf(10+distance*3),Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(DeliverMethod.this, string1.split(" ")[0], Toast.LENGTH_LONG).show();



        }

        @Override
        protected String doInBackground(Void... params) {
            String result = getDistanceOnRoad();
            try{
                Object json = new JSONTokener(result.toString()).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject userobj = (JSONObject) json;
                    /*edit.putInt("islogged", 1);
                    edit.putString("username", userobj.getString("username"));
                    edit.putString("name", userobj.getString("name"));
                    edit.putString("phone", userobj.getString("phone"));
                    edit.putInt("id", Integer.parseInt(userobj.getString("id")));
                    edit.putString("session_key", userobj.getString("session_key"));
                    edit.commit();*/

                    return result;

                } else{
                    return result;
                }
            }catch (JSONException e) {
                e.printStackTrace();
                return "-2";
            }
        }
    }




    private String distancecalc() {
        StringBuilder stringBuilder = new StringBuilder();

        try {

            URL url;
            HttpURLConnection httpURLConnectionObject ;
            OutputStream OutPutStream;
            BufferedWriter bufferedWriterObject ;
            BufferedReader bufferedReaderObject ;
            int RC ;

            url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&sensor=false");

            httpURLConnectionObject = (HttpURLConnection) url.openConnection();

            httpURLConnectionObject.setReadTimeout(19000);

            httpURLConnectionObject.setConnectTimeout(10000);

            httpURLConnectionObject.setRequestMethod("POST");

            httpURLConnectionObject.setDoInput(true);

            httpURLConnectionObject.setDoOutput(true);

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

    private String getDistanceOnRoad() {
        String result_in_kms = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + p_lat + "," + p_long + "&destination=" + u_lat
                + "," + u_long + "&sensor=false&units=metric";
        String tag[] = { "text" };
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add(" - ");
                    }
                }
                result_in_kms = String.format("%s", args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_in_kms;
    }
}
