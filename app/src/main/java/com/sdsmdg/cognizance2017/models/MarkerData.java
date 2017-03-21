package com.sdsmdg.cognizance2017.models;

/**
 * Created by Arihant Jain on 3/21/2017.
 */

public class MarkerData {
    private double longitude, latitude;
    private String title;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
