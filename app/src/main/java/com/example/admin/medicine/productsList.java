package com.example.admin.medicine;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class productsList extends AppCompatActivity {

    EditText amount;
    ArrayList<String> names = new ArrayList<String>();
    final ArrayList<Integer> ids = new ArrayList<Integer>();
    TextView product;
    CheckBox alt_med;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        savedInstanceState = getIntent().getExtras();
        final Integer type = savedInstanceState.getInt("type");
        product = (TextView) findViewById(R.id.searchText);
        Button search = (Button) findViewById(R.id.searchButton);
        alt_med=(CheckBox)findViewById(R.id.alt_med);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getText().toString().length() > 2) {
                    names.clear();
                    ids.clear();

                    progressDialog = ProgressDialog.show(productsList.this, getResources().getString(R.string.connecting),getResources().getString(R.string.please_wait), false, false);
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            search(product.getText().toString(), type);
                        }
                    }, 500);
                }else{
                    Toast.makeText(productsList.this,getResources().getString(R.string.char_limit),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void search(final String productname, Integer type) {
        ListView listView = (ListView) findViewById(R.id.listproducts);

        database db = new database(productsList.this);
        try {
            final String result;
            result = db.execute("4", productname, type.toString()).get();
            JSONArray jsonArray = new JSONArray(result);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject product = jsonArray.getJSONObject(i);
                names.add(product.getString("name"));
                ids.add(Integer.parseInt(product.getString("id")));
            }
            ArrayAdapter<String> arrayadapter = new MyArrayAdapter();
            listView.setAdapter(arrayadapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    LinearLayout linearLayout= new LinearLayout(productsList.this);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    amount = new EditText(productsList.this);
                    amount.setInputType(2);
                    amount.setGravity(1);
                    ArrayList<View> list = new ArrayList<View>();
                    Button minus = new Button(productsList.this);
                    minus.setText("-");
                    Button plus = new Button(productsList.this);
                    plus.setText("+");
                    list.add(minus);
                    //list.add(amount);
                    list.add(plus);
                    linearLayout.addChildrenForAccessibility(list);
                    AlertDialog.Builder x = new AlertDialog.Builder(productsList.this);
                    x.setMessage(getResources().getString(R.string.requested_amount)).setCancelable(true);
                    x.setView(linearLayout);
                    x.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int q) {
                            if(!amount.getText().toString().equals("")) {
                                //Toast.makeText(productsList.this,"111", Toast.LENGTH_LONG).show();
                                //returnValues(names.get(i).toString(), amount.getText().toString(), ids.get(i));
                            }else
                                Toast.makeText(productsList.this,getResources().getString(R.string.requested_amount),Toast.LENGTH_LONG).show();
                        }
                    });

                    AlertDialog alert = x.create();
                    alert.setTitle(getResources().getString(R.string.amount));
                    alert.show();
                }
            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.dismiss();
    }

    private void returnValues(String name, String amount, Integer id) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("product_name", name);
        returnIntent.putExtra("product_amount", amount);
        returnIntent.putExtra("product_id", id);
        if(alt_med.isChecked())
            returnIntent.putExtra("alt_med", 1);
        else
            returnIntent.putExtra("alt_med", 0);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private class MyArrayAdapter extends ArrayAdapter<String> {


        public MyArrayAdapter() {
            super(productsList.this, R.layout.productcontent, names);
        }


        public View getView(final int position, View convertView, ViewGroup parent) {
            View pview = convertView;
            if (pview == null) {
                pview = getLayoutInflater().inflate(R.layout.productcontent, parent, false);
            }
            TextView textt = (TextView) pview.findViewById(R.id.textView17);
            final Button amo =(Button)pview.findViewById(R.id.button8);

            textt.setText(names.get(position).toString());

            amo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    View view1 = getLayoutInflater().inflate(R.layout.quantity_content,null);


                    final Dialog x = new Dialog(productsList.this);

                    x.setContentView(R.layout.quantity_content);
                    amount= (EditText)x.findViewById(R.id.quantity);
                    Button minus =(Button)x.findViewById(R.id.minus);
                    Button plus  =(Button)x.findViewById(R.id.plus);
                    TextView yes  =(TextView) x.findViewById(R.id.quantity_yes);
                    TextView no  =(TextView)x.findViewById(R.id.quantity_no);
                      //      if(!amount.getText().toString().equals("")) {
                    //            returnValues(names.get(position).toString(), amount.getText().toString(), ids.get(position));
                    //        }else
                     //           Toast.makeText(productsList.this,getResources().getString(R.string.requested_amount),Toast.LENGTH_LONG).show();
                 //       }
                  //  });

                    x.setTitle(getResources().getString(R.string.amount));
                    x.show();
                    x.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                    minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int current;
                            current=Integer.parseInt(amount.getText().toString());
                            if(current>1) {
                                current-= 1;
                                amount.setText(String.valueOf(current));
                            }
                        }
                    });
                    plus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int current;
                            current=Integer.parseInt(amount.getText().toString());
                            current+= 1;
                            amount.setText(String.valueOf(current));
                        }
                    });

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!amount.getText().toString().equals("")) {
                                returnValues(names.get(position).toString(), amount.getText().toString(), ids.get(position));
                            }else
                                Toast.makeText(productsList.this,getResources().getString(R.string.requested_amount),Toast.LENGTH_LONG).show();
                        }
                    });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        x.dismiss();
                    }
                });
                }
            });

            return pview;
        }
    }
}
