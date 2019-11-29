package com.btk.mnj.forecast;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;

import com.google.android.gms.location.GeofenceStatusCodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class  Utils {

    private static HashMap<String,cordinates> citiListMap  = new HashMap<>();
    private static LinkedHashMap<String, cordinates> addCityList =  new LinkedHashMap<>();

    private static Context mContext;

    static {
        populateCityList();
    }

    public static  void init(Context context) {
        mContext = context;
    }

    public static void populateCities() {
    }

    public static class cordinates {

        double latitude,longitude;

        public cordinates(double latitude,double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public static Typeface getTypeface(Context context) {
        String mFontName = "GothamNarrow-Light.otf";
        return  Typeface.createFromAsset(context.getAssets(),mFontName);
    }


    public static void addCitytoList(String city) {
        cordinates addcitycordinate = citiListMap.get(city);
        addCityList.put(city, addcitycordinate);
    }

    public static cordinates getCityFromAddedCityList(String city) {
        return  addCityList.get(city);
    }

    public static void populateCityList() {
        citiListMap =  new HashMap<String, cordinates>();
        citiListMap.put("Abu Dhabi",new cordinates(24.453884,54.377342));
        citiListMap.put("Agra",new cordinates(27.176670,78.008072));
        citiListMap.put("Amsterdam",new cordinates(52.370216,4.895168));
        citiListMap.put("Antalya",new cordinates(36.896893,30.713324));
        citiListMap.put("Antwerp",new cordinates(51.219448,4.402464));
        citiListMap.put("Atlanta",new cordinates(33.748997,-84.387985));
        citiListMap.put("Austin",new cordinates(30.267153, -97.743057));
        citiListMap.put("Bangkok",new cordinates(13.756331,100.501762));
        citiListMap.put("Barcelona",new cordinates(41.385063,2.173404));
        citiListMap.put("Beijing",new cordinates(39.607120,-77.705230));
    }

    public static void getCordinatesfortheCity(String city,Context context) {

        if(Geocoder.isPresent()) {
            try{
                Geocoder geocoder = new Geocoder(context);

                List<Address> addressList =  geocoder.getFromLocationName(city,5);

                List<cordinates> cordinateList =  new ArrayList<cordinates>(addressList.size());

                for(Address address : addressList) {
                    if(address.hasLatitude() && address.hasLongitude()) {
                        cordinateList.add(new cordinates(address.getLatitude(),address.getLongitude()));
                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
