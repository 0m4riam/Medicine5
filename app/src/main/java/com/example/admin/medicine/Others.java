package com.example.admin.medicine;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Others extends AppCompatActivity {

    java.util.List<OList> olist= new ArrayList<OList>();
    java.util.List<Integer> alt_med= new ArrayList<Integer>();
    Integer index,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences lang= getApplicationContext().getSharedPreferences("m3d1c1ne4pp", Context.MODE_PRIVATE);
        if(lang.getInt("language",1)==1)
            setAppLocale("ar");

        setContentView(R.layout.activity_others);
        savedInstanceState = getIntent().getExtras();
        TextView typeTitle = (TextView)findViewById(R.id.TypeTitle);
        type=savedInstanceState.getInt("type",0);
                olist.add(new OList("","",0));
        alt_med.add(0);
        createList();

        Button request=(Button)findViewById(R.id.request);

        if(isMyServiceRunning(Order_sending.class)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Others.this);
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

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder();
            }
        });
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


    private void createList() {
        if(!olist.get(olist.size()-1).getName().toString().equals("") && olist.size()<=8) {
            olist.add(new OList("", "", 0));
            alt_med.add(0);
        }
        ArrayAdapter<OList> adapter = new othersAdapter();
        ListView listView= (ListView)findViewById(R.id.others_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i<8) {
                    index = i;
                    Intent x = new Intent(getApplicationContext(), productsList.class);
                    x.putExtra("type", type);
                    startActivityForResult(x, 1);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder =new AlertDialog.Builder(Others.this);
                builder.setMessage(getResources().getString(R.string.delete_med)).setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int ix) {
                                olist.remove(i);
                                alt_med.remove(i);
                                createList();
                            }
                        }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ix) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle("حذف");
                if(i!=olist.size()-1)
                    alert.show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            String product_name=data.getStringExtra("product_name");
            String product_amount=data.getStringExtra("product_amount");
            Integer product_id = data.getIntExtra("product_id",0);
            olist.set(index,new OList(product_name,product_amount,product_id));
            Integer alt= data.getIntExtra("alt_med",0);
            alt_med.set(index,alt);
            createList();
        }
    }


    private void createOrder() {
       /* JSONArray data = new JSONArray();
        try {
            for(Integer i =0;i<olist.size()-1;i++){
                JSONObject obj = new JSONObject();
                obj.put("id",olist.get(i).getId());
                obj.put("amount",olist.get(i).getAmount());
                data.put(obj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        String data="";
        for(Integer i =0;i<olist.size()-1;i++){
             data=data+olist.get(i).getId()+","+olist.get(i).getAmount()+","+alt_med.get(i)+";";
        }
        //Toast.makeText(Others.this,data.toString(), Toast.LENGTH_LONG).show();
        Intent x = new Intent(getApplicationContext(),PList.class);
        x.putExtra("type",type);
        x.putExtra("data",data.toString());
        x.putExtra("total_meds",olist.size()-1);
        startActivity(x);
    }



    private class othersAdapter extends ArrayAdapter<OList> {

        public othersAdapter() {

            super(Others.this,R.layout.otherscontent,olist);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View pview = convertView;
            if(pview==null){
                pview= getLayoutInflater().inflate(R.layout.otherscontent,parent,false);
            }

            OList p = olist.get(position);

            TextView name = (TextView)pview.findViewById(R.id.textView15);
            TextView amount = (TextView)pview.findViewById(R.id.textView4);

            name.setText(p.getName());
            if(p.getAmount().equals("")) {
                amount.setText(p.getAmount().toString());
            }else{
                amount.setText(getResources().getString(R.string.amount)+p.getAmount().toString());
            }

            if(position==olist.size()-1){
                TextView add = (TextView)pview.findViewById(R.id.addText);
                if(olist.size()<9) {
                    add.setVisibility(View.VISIBLE);
                }
            }else{
                TextView add = (TextView)pview.findViewById(R.id.addText);
                add.setVisibility(View.INVISIBLE);
            }






            return pview;
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
