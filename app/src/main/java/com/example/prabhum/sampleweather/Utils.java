package com.example.prabhum.sampleweather;

import android.app.Application;
import android.net.ConnectivityManager;

import java.util.HashMap;

public class Utils {

    private static HashMap<String,cordinates> citiListMap  = new HashMap<>();

    public static void populateCities() {
    }

    public static class cordinates {

        long latitude,longitude;

        public cordinates(long latitude,long longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public long getLatitude() {
            return latitude;
        }

        public long getLongitude() {
            return longitude;
        }
    }


}
