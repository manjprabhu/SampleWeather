package com.btk.mnj.forecast.Util;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.btk.mnj.forecast.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

public class  Utils {

    private static final String TAG  = Utils.class.getSimpleName();
    public static final int ADD_CITY_LIST = 100;
    public static final int LOCATION_PERMISSION = 100;
    public static final String KEY = "7e9e54f22038c6b245aceb3ab734c0ff";


    public static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private static LinkedHashMap<String,cordinates> citiListMap  = new LinkedHashMap<>();
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
//        return  addCityList.get(city);
        return citiListMap.get(city);
    }

    public static HashMap<String,cordinates> getCordinateMap() {
        return citiListMap;
    }

    public static void populateCityList() {
        citiListMap =  new LinkedHashMap<>();
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
        citiListMap.put("Benidorm",new cordinates(38.541058,-0.122494));
        citiListMap.put("Berlin",new cordinates(52.520008,13.404954));
        citiListMap.put("Birmingham",new cordinates(33.518589,-86.810356));
        citiListMap.put("Boston",new cordinates(42.360081,-71.058884));
        citiListMap.put("Bratislava",new cordinates(48.148598,17.107748));
        citiListMap.put("Bruges",new cordinates(51.209347,3.224699));
        citiListMap.put("Brussels",new cordinates(50.850346,4.351721));
        citiListMap.put("Bucharest",new cordinates(44.426765,26.102537));
        citiListMap.put("Budapest",new cordinates(47.497913,19.040236));
        citiListMap.put("Buenos Aires",new cordinates(-34.603683,-58.381557));

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

    public static int getForecastIcon(String forecastDescription) {
        int icon;
        switch (forecastDescription){
            case "Drizzle":
                icon = R.drawable.ic_drizzle;
                break;
            case "Clear":
                icon = R.drawable.ic_clear;
                break;
            case "Mostly Cloudy":
                icon = R.drawable.ic_cloudy;
                break;
            default:
                icon = R.mipmap.ic_clear;
        }
        return icon;
    }

    public static String convertFahrenheitToCelcius(float fahrenheit) {
        return Math.round((fahrenheit - 32) * 5 / 9) +"";
    }

    public static String getForecastDay(long time) {

        Date date = new Date(time * 1000L);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        Log.v(TAG,"getForecastDay-->"+formatted);
        return formatted;
    }

    public static Typeface getXLightTypeface(Context context) {
        String mTtfFontName = "GothamNarrow-XLight.otf";
        Typeface typeface =Typeface.createFromAsset(context.getAssets(), mTtfFontName);
        return typeface;
    }

}
