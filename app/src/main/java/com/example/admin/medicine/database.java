package com.example.admin.medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by Admin on 9/2/2017.
 */

public class database extends AsyncTask<String,Void,String> {
    Context context;
    String server_ip="http://saydaletk.sd/api/";

    public database(Context ctx) {
        context = ctx;
    }


    @Override
    protected String doInBackground(String... params) {
        String type = params[0];


        if(type.equals("1")) {
            //|----------------------------------------------------|
            //|this is registration section                        |
            //|----------------------------------------------------|
            String name , phone , username , password;

            name = params[1];
            phone = params[2];
            username = params[3];
            password = params[4];

            String url_string = server_ip+"createuser.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                                URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                                URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                                URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                                URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }
        }




        if(type.equals("2")) {
            //|----------------------------------------------------|
            //|this is LOGIN section                               |
            //|----------------------------------------------------|

            String name , phone , username , password;
            username = params[1];
            password = params[2];
            String url_string = server_ip+"login.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }
        }



        if(type.equals("3")) {
            //|----------------------------------------------------|
            //|this is LOGIN using session_key section                        |
            //|----------------------------------------------------|
            String session_key = params[1];
            String token = params[2];

            String url_string = server_ip+"loginget.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(token,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
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
                return e.toString();
            }
        }


        if(type.equals("4")){

            //|----------------------------------------------------|
            //|this is  product name search section          |
            //|----------------------------------------------------|

            String medicine_name = params[1];
            String medicine_type = params[2];

            String url_string = server_ip+"products.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("product_name","UTF-8")+"="+URLEncoder.encode(medicine_name,"UTF-8")+"&"+
                        URLEncoder.encode("product_type","UTF-8")+"="+URLEncoder.encode(medicine_type,"UTF-8");

                bufferedWriter.write(post);
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
                return e.toString();
            }

        }


        if(type.equals("5")){

            //|----------------------------------------------------|
            //|check image upload section          |
            //|----------------------------------------------------|

            String date = params[1];
            String session_key = params[2];

            String url_string = server_ip+"imagecheck.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");

                bufferedWriter.write(post);
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
                return e.toString();
            }

        }


        if(type.equals("6")){

            //|----------------------------------------------------|
            //|request order section          |
            //|----------------------------------------------------|

            String user_id = params[1];
            String prescription_id = params[2];
            String pharmacy_id = params[3];
            String session_key = params[4];
            String u_comment = params[5];
            String request_type= params[6];
            String any_pharmacy=params[7];
            String meds_details=params[8];
            String total_meds_count=params[9];


            String url_string = server_ip+"createorder.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"+
                        URLEncoder.encode("prescription_id","UTF-8")+"="+URLEncoder.encode(prescription_id,"UTF-8")+"&"+
                        URLEncoder.encode("pharmacy_id","UTF-8")+"="+URLEncoder.encode(pharmacy_id,"UTF-8")+"&"+
                        URLEncoder.encode("u_comment","UTF-8")+"="+URLEncoder.encode(u_comment,"UTF-8")+"&"+
                        URLEncoder.encode("request_type","UTF-8")+"="+URLEncoder.encode(request_type,"UTF-8")+"&"+
                        URLEncoder.encode("any_pharmacy","UTF-8")+"="+URLEncoder.encode(any_pharmacy,"UTF-8")+"&"+
                        URLEncoder.encode("meds_details","UTF-8")+"="+URLEncoder.encode(meds_details,"UTF-8")+"&"+
                        URLEncoder.encode("total_meds_count","UTF-8")+"="+URLEncoder.encode(total_meds_count,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }

        }



        if(type.equals("7")){

            //|----------------------------------------------------|
            //|this is  pharmacy search section          |
            //|----------------------------------------------------|

            String session_key = params[1];
            String search = params[2];
            String url_string = server_ip+"pharmacies.php";

            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8")+"&"+
                        URLEncoder.encode("search","UTF-8")+"="+URLEncoder.encode(search,"UTF-8");

                bufferedWriter.write(post);
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
                return e.toString();
            }

        }


        if(type.equals("8")){

            //|----------------------------------------------------|
            //|request order2 section                              |
            //|----------------------------------------------------|

            String user_id = params[1];
            String data = params[2];
            String pharmacy_id = params[3];
            String session_key = params[4];
            String u_comment= params[5];
            String request_type= params[6];
            String any_pharmacy=params[7];
            String total_meds=params[8];

            String url_string = server_ip+"createorder2.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"+
                        URLEncoder.encode("data","UTF-8")+"="+URLEncoder.encode(data,"UTF-8")+"&"+
                        URLEncoder.encode("pharmacy_id","UTF-8")+"="+URLEncoder.encode(pharmacy_id,"UTF-8")+"&"+
                        URLEncoder.encode("u_comment","UTF-8")+"="+URLEncoder.encode(u_comment,"UTF-8")+"&"+
                        URLEncoder.encode("request_type","UTF-8")+"="+URLEncoder.encode(request_type,"UTF-8")+"&"+
                        URLEncoder.encode("any_pharmacy","UTF-8")+"="+URLEncoder.encode(any_pharmacy,"UTF-8")+"&"+
                        URLEncoder.encode("total_meds","UTF-8")+"="+URLEncoder.encode(total_meds,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }

        }



        if(type.equals("9")){

            //|----------------------------------------------------|
            //|logout section                              |
            //|----------------------------------------------------|

            String session_key = params[1];

            String url_string = server_ip+"logout.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }

        }




        if(type.equals("10")){

            //|----------------------------------------------------|
            //|change order status section                              |
            //|----------------------------------------------------|

            String order_id = params[1];
            String session_key = params[2];
            String status= params[3];
            String service_type= params[4];
            String latitude= params[5];
            String longitude= params[6];
            String d_price=params[7];


            String url_string = server_ip+"update_order.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("order_id","UTF-8")+"="+URLEncoder.encode(order_id,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8")+"&"+
                        URLEncoder.encode("service_type","UTF-8")+"="+URLEncoder.encode(service_type,"UTF-8")+"&"+
                        URLEncoder.encode("latitude","UTF-8")+"="+URLEncoder.encode(latitude,"UTF-8")+"&"+
                        URLEncoder.encode("longitude","UTF-8")+"="+URLEncoder.encode(longitude,"UTF-8")+"&"+
                        URLEncoder.encode("d_price","UTF-8")+"="+URLEncoder.encode(d_price,"UTF-8")+"&"+
                        URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }

        }




        if(type.equals("11")){

            //|----------------------------------------------------|
            //|check order status section                              |
            //|----------------------------------------------------|

            String order_id = params[1];
            String session_key = params[2];
            String query = params[3];

            String url_string = server_ip+"check_order.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("order_id","UTF-8")+"="+URLEncoder.encode(order_id,"UTF-8")+"&"+
                        URLEncoder.encode("query","UTF-8")+"="+URLEncoder.encode(query,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }

        }




        if(type.equals("12")){

            //|----------------------------------------------------|
            //|get pharmacy info section                              |
            //|----------------------------------------------------|

            String order_id = params[1];
            String session_key = params[2];


            String url_string = server_ip+"pharmacy_info.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("order_id","UTF-8")+"="+URLEncoder.encode(order_id,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }

        }


        if(type.equals("13")){

            //|----------------------------------------------------|
            //|upload image section                              |
            //|----------------------------------------------------|

            String session_key = params[1];
            String date = params[2];
            String image_data = params[3];


            String url_string = server_ip+"insert_image2.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"+
                        URLEncoder.encode("image_data","UTF-8")+"="+URLEncoder.encode(image_data,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }

        }



        if(type.equals("14")){

            //|----------------------------------------------------|
            //|get order details                              |
            //|----------------------------------------------------|

            String session_key = params[1];
            String order_id = params[2];


            String url_string = server_ip+"prices.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("order_id","UTF-8")+"="+URLEncoder.encode(order_id,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }

        }



        if(type.equals("15")){

            //|----------------------------------------------------|
            //|get order details                              |
            //|----------------------------------------------------|

            String session_key = params[1];
            String order_id = params[2];
            String status = params[3];


            String url_string = server_ip+"orderdetails.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("order_id","UTF-8")+"="+URLEncoder.encode(order_id,"UTF-8")+"&"+
                        URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8")+"&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
                bufferedWriter.write(post);
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
                return e.toString();
            }

        }


        if(type.equals("16")){

            //|----------------------------------------------------|
            //|get user orders                                     |
            //|----------------------------------------------------|

            String session_key = params[1];


            String url_string = server_ip+"userorders.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8");
                bufferedWriter.write(post);
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
                return "22";
            } catch (IOException e) {
                e.printStackTrace();
                return "11";
            }

        }


        if(type.equals("17")) {
            //|----------------------------------------------------|
            //|this is LOGIN section                               |
            //|----------------------------------------------------|

            String name , session_key , username , password , password_new;
            session_key = params[1];
            password = params[2];
            password_new = params[3];
            String url_string = server_ip+"changePassword.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("session_key","UTF-8")+"="+URLEncoder.encode(session_key,"UTF-8")+"&"+
                        URLEncoder.encode("password_new","UTF-8")+"="+URLEncoder.encode(password_new,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post);
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

        if(type.equals("18")) {
            //|----------------------------------------------------|
            //|this is LOGIN section                               |
            //|----------------------------------------------------|

            String id ;
            id= params[1];

            String url_string = server_ip+"SMSVerify.php";
            try {
                URL url = new URL(url_string);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream out = connection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                String post =   URLEncoder.encode("auth","UTF-8")+"=1111&"+
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                bufferedWriter.write(post);
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








        else
            return null;
    }

    @Override
    protected void onPostExecute(String s) {
        //Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }
}
