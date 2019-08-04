package com.example.admin.medicine;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;


public class Health extends AppCompatActivity {


    private static final String UPLOAD_URL = "http://saydaletk.sd/api/insert_image.php";
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;
    private SharedPreferences sharedpreferences;
    String product_name;
    Integer product_id;
    Integer type=0;
    boolean check = true;
    Integer total_meds_count,requested_meds_count;
    TextView message;
    Button show_image;
    int j;

    ProgressDialog progressDialog ;
    Session session = new Session();
    String session_key;
    String datee;
    Uri imageUri;
    CheckBox alt_meds[] = new CheckBox[8];
    CheckBox meds[] = new CheckBox[8];
    TextView requested_meds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Button choose_pic = (Button)findViewById(R.id.choose_pic);
        Button take_pic = (Button)findViewById(R.id.take_pic);
        final Button proceed = (Button)findViewById(R.id.proceed);
        imageView= (ImageView)findViewById(R.id.imageView);

        session.getAll(Health.this);
        session_key=session.getSession_key();


        requestStoragePermission();
        EnableRuntimePermissionToAccessCamera();


        if(isMyServiceRunning(Order_sending.class)){
            AlertDialog.Builder builder = new AlertDialog.Builder(Health.this);
            builder.setMessage(getResources().getString(R.string.another_order_is_under_processing))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle(getResources().getString(R.string.alert));
            alert.show();
        }


        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 7);
            }
        });

        choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePic();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 if(bitmap!=null && requested_meds_count!=0){
                    StringBuilder ordered_meds = new StringBuilder(100);
                     int x =0;
                     for (int j=0;j<total_meds_count;j++){
                         if (meds[j].isChecked()){
                             if(x!=0)
                                 ordered_meds.append(" و ");
                             ordered_meds.append(String.valueOf(j+1));
                             x=1;
                         }
                     }

                     AlertDialog.Builder builder = new AlertDialog.Builder(Health.this);
                     builder.setMessage(getResources().getString(R.string.please_confirm_to_continue))
                             .setCancelable(false)
                             .setPositiveButton(getResources().getString(R.string.Continue), new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     ImageUploadToServerFunction();
                                 }
                             })
                             .setNegativeButton(getResources().getString(R.string.Back), new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                 }
                             });
                     AlertDialog alert = builder.create();
                     alert.setTitle(getResources().getString(R.string.confirmation));
                     alert.show();
                 }
                 else if(bitmap ==null)
                     Toast.makeText(Health.this,getResources().getString(R.string.no_prescription_attached),Toast.LENGTH_LONG).show();
                 else if(requested_meds_count==0)
                     Toast.makeText(Health.this,getResources().getString(R.string.items_limit),Toast.LENGTH_LONG).show();
            }
        });

         message = (TextView)findViewById(R.id.message);
         show_image = (Button)findViewById(R.id.show_image);
        requested_meds=(TextView)findViewById(R.id.requested_meds);

        meds[0]= (CheckBox) findViewById(R.id.med1);
        meds[1]= (CheckBox) findViewById(R.id.med2);
        meds[2]= (CheckBox) findViewById(R.id.med3);
        meds[3]= (CheckBox) findViewById(R.id.med4);
        meds[4]= (CheckBox) findViewById(R.id.med5);
        meds[5]= (CheckBox) findViewById(R.id.med6);
        meds[6]= (CheckBox) findViewById(R.id.med7);
        meds[7]= (CheckBox) findViewById(R.id.med8);

        alt_meds[0]= (CheckBox) findViewById(R.id.alt_med1);
        alt_meds[1]= (CheckBox) findViewById(R.id.alt_med2);
        alt_meds[2]= (CheckBox) findViewById(R.id.alt_med3);
        alt_meds[3]= (CheckBox) findViewById(R.id.alt_med4);
        alt_meds[4]= (CheckBox) findViewById(R.id.alt_med5);
        alt_meds[5]= (CheckBox) findViewById(R.id.alt_med6);
        alt_meds[6]= (CheckBox) findViewById(R.id.alt_med7);
        alt_meds[7]= (CheckBox) findViewById(R.id.alt_med8);

        final EditText meds_in_pre= (EditText)findViewById(R.id.meds_in_pre);
        final LinearLayout pre_details=(LinearLayout)findViewById(R.id.pre_details);
        meds_in_pre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    //pre_details.setVisibility(View.VISIBLE);
                    proceed.setVisibility(View.VISIBLE);
                    if (Integer.valueOf(charSequence.toString())>8){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.items_limit),Toast.LENGTH_LONG).show();
                        total_meds_count=8;
                        meds_in_pre.setText("8");
                    }
                    else {
                        total_meds_count=Integer.valueOf(charSequence.toString());
                    }
                    requested_meds_count=total_meds_count;
                    requested_meds.setText(requested_meds_count.toString());

                } else{
                    pre_details.setVisibility(View.INVISIBLE);
                    proceed.setVisibility(View.INVISIBLE);
                }

                for (int j =0; j < total_meds_count;j++){
                    meds[j].setVisibility(View.VISIBLE);
                    alt_meds[j].setVisibility(View.VISIBLE);
                    meds[j].setChecked(Boolean.TRUE);
                    alt_meds[j].setEnabled(Boolean.TRUE);
                    alt_meds[j].setChecked(Boolean.TRUE);
                }
                for (int j =total_meds_count; j < 8;j++){
                    meds[j].setVisibility(View.INVISIBLE);
                    alt_meds[j].setVisibility(View.INVISIBLE);
                    meds[j].setChecked(Boolean.FALSE);
                    alt_meds[j].setChecked(Boolean.FALSE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


        for(j =0;j<8;j++){
            checkboxclick(j);

        }

    }

    private void checkboxclick(final int j) {
        meds[j].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!meds[j].isChecked()){
                    alt_meds[j].setChecked(Boolean.FALSE);
                    alt_meds[j].setEnabled(Boolean.FALSE);
                    requested_meds_count--;
                }else{
                    alt_meds[j].setEnabled(Boolean.TRUE);
                    alt_meds[j].setChecked(Boolean.TRUE);
                    requested_meds_count++;
                }
                requested_meds.setText(requested_meds_count.toString());

            }
        });
    }

    public void choosePic(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 7 && resultCode == RESULT_OK) {
            //Toast.makeText(Health.this,"asdasd",Toast.LENGTH_SHORT).show();
            try {
//                filePath = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                type =1;
                message.setText(getResources().getString(R.string.Prescription_is_chosen));
                message.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.correct,0,0,0);
                show_image.setVisibility(View.VISIBLE);
                show_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),image_veiw.class);
                        intent.putExtra("uri",imageUri.toString());
                        startActivity(intent);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

            //bitmap= (Bitmap) data.getExtras().get("data");
        }
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                message.setText(getResources().getString(R.string.prescription_attached));
                message.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.correct,0,0,0);
                show_image.setVisibility(View.VISIBLE);
                show_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),image_veiw.class);
                        intent.putExtra("uri",filePath.toString());
                        startActivity(intent);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode==4 && resultCode == 3){
                //

                Integer result = data.getIntExtra("result",0);

                if(result==0)
                    Toast.makeText(Health.this,getResources().getString(R.string.attaching_error),Toast.LENGTH_LONG).show();
                else{
                    JSONObject meds_details = new JSONObject();
                    try {
                    for(int z=1;z<=total_meds_count;z++) {
                        int y=0;
                        if(meds[z-1].isChecked())
                            y=1;
                        if(alt_meds[z-1].isChecked())
                            y=2;

                            meds_details.put(String.valueOf(z), String.valueOf(y));
                        }
                        }
                    catch (JSONException e) {
                    e.printStackTrace();
                    }
                    //Toast.makeText(getApplicationContext(),meds_details.toString(),Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();
                    Intent y = new Intent(getApplicationContext(),PList.class);
                    y.putExtra("total_meds_count",total_meds_count);
                    y.putExtra("product_id",product_id);
                    y.putExtra("prescription_id",result);
                    y.putExtra("meds_details",meds_details.toString());
                    y.putExtra("type",1);
                    startActivity(y);
                    //Toast.makeText(Health.this,"uploaded",Toast.LENGTH_LONG).show();
                }
        }else if(requestCode == 15 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    //Toast.makeText(Health.this,"asdasd",Toast.LENGTH_SHORT).show();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
    }

    public void uploadMultipart() {

        //getting the actual path of the image
        String path = getPath(filePath);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            sharedpreferences = getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
            String session_key = sharedpreferences.getString("session_key","");

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("auth", "1111")//Adding text parameter to the request
                    .addParameter("session_key", session_key)
                    .addParameter("date", currentDateTimeString)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        Intent x = new Intent(getApplicationContext(),Upload.class);
        x.putExtra("date",currentDateTimeString);
        //startActivityForResult(x,4);

    }

    public void uploadImage(){
        Session session = new Session();
        session.getAll(Health.this);
        String session_key= session.getSession_key();
        String date = DateFormat.getDateTimeInstance().format(new Date());
        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 1000000000, byteArrayOutputStreamObject);
       // byteArrayOutputStreamObject=bitmap.getNinePatchChunk();
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        final String image_data = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        database db = new database(Health.this);

        try {
            String result = db.execute("13",session_key,date,image_data).get();
            //Toast.makeText(Health.this,result,Toast.LENGTH_LONG).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
               // Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }else if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

             //   Toast.makeText(Health.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(Health.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(Health.this,
                Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
      //      Toast.makeText(Health.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Health.this,new String[]{Manifest.permission.CAMERA}, 1);

        }
    }

    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

       // final String ConvertImage = bitmap.toString();

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {



            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(Health.this,getResources().getString(R.string.uploading),getResources().getString(R.string.please_wait),false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.

                if(string1.equals("1")){
                    Intent x = new Intent(getApplicationContext(),Upload.class);
                    x.putExtra("date",datee);
                    startActivityForResult(x,4);
                }

            }

            @Override
            protected String doInBackground(Void... params) {
                datee=DateFormat.getDateTimeInstance().format(new Date());

                Health.ImageProcessClass imageProcessClass = new Health.ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put("image_name", session_key);

                HashMapParams.put("image_path", ConvertImage);

                HashMapParams.put("auth", "1111");

            //    HashMapParams.put("session_key", session.getSession_key());

                HashMapParams.put("date",datee);

                String FinalData = imageProcessClass.ImageHttpRequest("http://"+getResources().getString(R.string.server_ip)+"/insert_image.php", HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

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

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (java.util.Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public  void setAppLocale(String localeCode){

        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf= res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}
