package com.example.admin.medicine;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Saja Al-azhari on 10/19/2017.
 */

public class request extends Activity {

    String p_name,date;
    Integer method,order_id,status,prescription_id,request_type;
    Double p_lat,p_long,price;

    public request(String p_name, String date, Integer request_type, Integer order_id, Integer status, Double p_lat, Double p_long, Double price, Integer prescription_id) {
        this.p_name = p_name;
        this.date = date;
        this.request_type = request_type;
        this.order_id = order_id;
        this.status = status;
        this.p_lat = p_lat;
        this.p_long = p_long;
        this.price = price;
        this.prescription_id=prescription_id;
    }

    public request(String p_name, String date, Integer method, Integer order_id, Integer status, Double p_lat, Double p_long) {
        this.p_name = p_name;
        this.date = date;
        this.method = method;
        this.order_id = order_id;
        this.status = status;
        this.p_lat = p_lat;
        this.p_long = p_long;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getP_lat() {
        return p_lat;
    }

    public void setP_lat(Double p_lat) {
        this.p_lat = p_lat;
    }

    public Double getP_long() {
        return p_long;
    }

    public void setP_long(Double p_long) {
        this.p_long = p_long;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(Integer prescription_id) {
        this.prescription_id = prescription_id;
    }

    public Integer getRequest_type() {
        return request_type;
    }

    public void setRequest_type(Integer request_type) {
        this.request_type = request_type;
    }
}
