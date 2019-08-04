package com.example.admin.medicine;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.*;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Admin on 1/19/2019.
 */

public class Order_sending extends Service {


    String pharmacies;
    Integer order_id;
    JSONArray pharmacies_distances;
    JSONObject pharmacy_distance;
    Integer[] ids,order;
    Double[] distances;
    Double distance1=10000000000.0,distance2=-1.0;
    Thread notify;
    Session session = new Session();
    Runnable sendnotificationrunnable;
    Future sendnotificationrunnablefuture;
    ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    public void onCreate() {


        SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pharmacies= intent.getExtras().getString("pharmacies");
        order_id=intent.getExtras().getInt("order_id");


        try {
            pharmacies_distances= new JSONArray(pharmacies);
            ids= new Integer[pharmacies_distances.length()];
            order= new Integer[pharmacies_distances.length()];
            distances= new Double[pharmacies_distances.length()];

            for (int i = 0; i < pharmacies_distances.length();i++){
                pharmacy_distance = pharmacies_distances.getJSONObject(i);
                ids[i]=Integer.valueOf(pharmacy_distance.getString("pharmacy_id"));
                distances[i]=Double.parseDouble(pharmacy_distance.getString("distance"));
                //Toast.makeText(getApplicationContext(),pharmacy_distance.getString("pharmacy_id"),Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //order by distances
        for (int i = 0; i < pharmacies_distances.length();i++){
            distance1=100000000.0;
            for (int j = 0; j < pharmacies_distances.length();j++){

                if (distances[j]< distance1 && distances[j]> distance2 ) {
                    order[i]= ids[j];
                    distance1=distances[j];
                }


            }
            //Toast.makeText(getApplicationContext(),String.valueOf(i)+distance1.toString(),Toast.LENGTH_LONG).show();
            distance2=distance1;
        }

        //Toast.makeText(getApplicationContext(),String.valueOf(order.length),Toast.LENGTH_LONG).show();

        session.getAll(getApplicationContext());
        notify = new Thread(new sendNotifications(startId));
        notify.start();



        //sendnotificationrunnable = new sendNotifications(startId);
        //sendnotificationrunnablefuture = executorService.submit(sendnotificationrunnable);


        //AsyncTaskUploadClass task = new AsyncTaskUploadClass();
        //task.execute();

        return START_STICKY;

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }






    class sendNotifications implements Runnable{

        int serviceId;
        Boolean isStopped=false;

        sendNotifications(int serviceId){
            this.serviceId=serviceId;
        }

        public void stop(){
            isStopped= true;
        }



        @Override
        public void run() {
            int z=0,k = 0;
            synchronized (this) {

                sendNotification(getResources().getString(R.string.order_under_process),1);
                Integer error=0;


                while(true){

                    if(notify.isInterrupted()){
                        break;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    HttpURLConnection httpURLConnectionObject;

                    try {

                        URL url;
                        OutputStream OutPutStream;
                        BufferedWriter bufferedWriterObject ;
                        BufferedReader bufferedReaderObject ;
                        int RC ;

                        url = new URL("http://"+getResources().getString(R.string.server_ip)+"/send_notification.php");
                        //url = new URL("http://192.168.0.110/send_notification.php");

                        httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                        httpURLConnectionObject.setReadTimeout(5000);

                        httpURLConnectionObject.setConnectTimeout(5000);

                        httpURLConnectionObject.setRequestMethod("POST");

                        httpURLConnectionObject.setDoInput(true);

                        httpURLConnectionObject.setDoOutput(true);

                        OutPutStream = httpURLConnectionObject.getOutputStream();

                        bufferedWriterObject = new BufferedWriter(

                                new OutputStreamWriter(OutPutStream, "UTF-8"));


                        if(z<order.length) {
                            bufferedWriterObject.write(URLEncoder.encode("auth", "UTF-8") + "=1111&" +
                                    URLEncoder.encode("session_key", "UTF-8") + "=" + URLEncoder.encode(session.getSession_key(), "UTF-8") + "&" +
                                    URLEncoder.encode("order_id", "UTF-8") + "=" + URLEncoder.encode(order_id.toString(), "UTF-8") + "&" +
                                    URLEncoder.encode("pharmacy_id", "UTF-8") + "=" + URLEncoder.encode(order[z].toString(), "UTF-8"));
                        }else{
                            bufferedWriterObject.write(URLEncoder.encode("auth", "UTF-8") + "=1111&" +
                                    URLEncoder.encode("session_key", "UTF-8") + "=" + URLEncoder.encode(session.getSession_key(), "UTF-8") + "&" +
                                    URLEncoder.encode("order_id", "UTF-8") + "=" + URLEncoder.encode(order_id.toString(), "UTF-8") + "&" +
                                    URLEncoder.encode("pharmacy_id", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8"));
                        }
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

                    }catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        sendNotification("الرجاء التأكد من تفعيل خدمة الانترنت حتى يكتمل الطلب.",1);
                        error=1;
                        try {
                            Thread.sleep(3000);
                            continue;
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                        e.printStackTrace();
                    }


                    if(stringBuilder.toString().equals("1")) {
                        if(error==1)
                            sendNotification("طلبك قيد التنفيذ",1);
                        z++;
                        error=0;
                    }else if(stringBuilder.toString().equals("3")) {
                        sendNotification("انتهى الطلب و يمكن عرض النتائج",2);
                        break;
                    } else if(stringBuilder.toString().equals("-1")){
                        sendNotification("تم الغاء الطلب",1);
                        break;
                    }else if(stringBuilder.toString().equals("0")){
                        if(error==1)
                            sendNotification("طلبك قيد التنفيذ",1);
                    }

                    error=0;


                    if(z>order.length){
                        stopSelf(serviceId);
                        sendNotification("انتهى الطلب و يمكن عرض النتائج",2);
                        break;
                    }

                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(isStopped)
                        break;

                }
                stopSelf();
            }
        }
    }



    private void sendNotification(String body,int i) {

        Intent intent = new Intent(this, welcome.class);
        if(i==1) {
            intent = new Intent(this,Waiting.class);
            intent.putExtra("order_id",order_id);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //Set sound of notification
        Uri notificationSound = Uri.parse("android.resource://com.example.admin.medicine/"+R.raw.whatsapp_whistle);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logoo)
                .setContentTitle("Saydaletk")
                .setContentText(body)
                .setAutoCancel(true)
                //.setSound(notificationSound)
                .setContentIntent(pendingIntent);
        //.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
        //.setVibrate(new long[] { 1000, 1000, 1000 , 1000, 1000 });

        Notification notification =notifiBuilder.build();
        //notification.sound=notificationSound;
        // notification.flags = Notification.FLAG_AUTO_CANCEL;
        // notification.flags = Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager)Order_sending.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(155 /*ID of notification*/, notification);




    }


    @Override
    public void onTaskRemoved(Intent rootIntent){
        sendNotification("the app is closed",1);
    }


    public  void setAppLocale(String localeCode){

        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf= res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }

}


