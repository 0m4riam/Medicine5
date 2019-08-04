package com.example.admin.medicine;

import android.app.Activity;

/**
 * Created by Admin on 10/15/2017.
 */

public class prices extends Activity {

    String name;
    String price;

    public prices(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
