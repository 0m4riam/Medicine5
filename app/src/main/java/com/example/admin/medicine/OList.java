package com.example.admin.medicine;

import android.app.Activity;

/**
 * Created by Admin on 9/25/2017.
 */

public class OList extends Activity {
    String name;
    String amount;
    Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public OList(String name, String amount, Integer id) {
        this.name = name;
        this.id = id;
        this.amount = amount;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


}
