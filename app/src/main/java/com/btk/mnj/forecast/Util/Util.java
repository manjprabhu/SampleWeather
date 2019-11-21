package com.btk.mnj.forecast.Util;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.btk.mnj.forecast.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class Util {

    public static final String[] LOCATION_PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    public static Typeface getXLightTypeface(Context context) {
        String mTtfFontName = "GothamNarrow-XLight.otf";
        Typeface typeface =Typeface.createFromAsset(context.getAssets(), mTtfFontName);
        return typeface;
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
        Log.v("manju","getForecastDay-->"+formatted);
        return formatted;
    }
}
