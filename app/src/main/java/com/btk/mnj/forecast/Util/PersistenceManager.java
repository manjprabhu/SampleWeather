package com.btk.mnj.forecast.Util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.btk.mnj.forecast.Model.WeatherData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PersistenceManager {

    private static PersistenceManager mInstance = null;
    private static final String PREF_KEY = "com.sample.pref";
    private static final String DATA = "com.weatherdata";
    private static SharedPreferences mPref;

    private  PersistenceManager(Context context) {
        mPref = context.getSharedPreferences(PREF_KEY,Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if(mInstance== null) {
            mInstance = new PersistenceManager(context);
        }
    }


    public static PersistenceManager getInstance() {
        return mInstance;
    }

    public void putweatherData(HashMap<String,WeatherData> data) {
        Gson gson = new Gson();
        String weatherdata =  gson.toJson(data);
        mPref.edit().putString(DATA,weatherdata).commit();
        String str  = gson.toJson(data);
        mPref.edit().putString(DATA,str).commit();
    }

    public MutableLiveData<List<WeatherData>> getData() {
        Gson gson = new Gson();
        String str =  mPref.getString(DATA,"");
        java.lang.reflect.Type type = new TypeToken<HashMap<String,WeatherData>>(){}.getType();
        HashMap<String,WeatherData> cityData  = gson.fromJson(str,type);
        MutableLiveData<List<WeatherData>> livedata = new MutableLiveData<>();

        Collection<WeatherData> data = cityData.values();

        ArrayList<WeatherData> weatherData = new ArrayList<>(data);

        livedata.setValue(weatherData);
        return livedata;
    }




    public void put(WeatherData data) {
        Gson gson = new Gson();
        String weatherdata =  gson.toJson(data);
        mPref.edit().putString(DATA,weatherdata).commit();
    }

    public MutableLiveData<WeatherData> getweatherData() {
        String data =  mPref.getString(DATA, null);
        MutableLiveData<WeatherData> livedata = new MutableLiveData<>();
        Gson gson = new Gson();
        livedata.setValue(gson.fromJson(data,WeatherData.class));
        return livedata;
    }

}
