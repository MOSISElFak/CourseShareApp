package com.example.stefanzivic.courseshare.model;
//import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Stefan Zivic on 7/3/2017.
 */

public class Coordinates {

    public double latitude;
    public double longitude;

    public Coordinates() {

    }

    public Coordinates(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

//    public LatLng toGoogleCoords() {
//        return new LatLng(latitude, longitude);
//    }
}
