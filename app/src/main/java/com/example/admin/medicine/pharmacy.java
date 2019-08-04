package com.example.admin.medicine;

import android.app.Activity;

/**
 * Created by Admin on 9/22/2017.
 */

public class pharmacy extends Activity {

    String name;
    String city;
    String location;
    Integer id;
    Double latitude;
    Double longitude;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    Double distance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public pharmacy(String name, String city, String location, Integer id, Double latitude, Double longitude) {
        this.name = name;
        this.city = city;
        this.location = location;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public pharmacy(String name, String city, String location, Integer id, Double latitude, Double longitude, Double distance) {
        this.name = name;
        this.city = city;
        this.location = location;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



}
