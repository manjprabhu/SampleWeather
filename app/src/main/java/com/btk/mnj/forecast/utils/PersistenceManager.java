package com.btk.mnj.forecast.utils;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.btk.mnj.forecast.model.WeatherData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PersistenceManager {

    private final String TAG = PersistenceManager.class.getSimpleName();

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
        mPref.edit().putString(DATA,weatherdata).apply();
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
        Log.v(TAG,"put:"+data.getCity());
        Gson gson = new Gson();
        String weatherdata =  gson.toJson(data);
        mPref.edit().putString(DATA,weatherdata).apply();
    }

    public void putList(List<WeatherData> data) {
        Log.v(TAG,"putList:"+data.get(0).getCity());
        Gson gson = new Gson();
        String weatherdata =  gson.toJson(data);
        mPref.edit().putString(DATA,weatherdata).apply();
    }

    public List<WeatherData> get() {
        String data =  mPref.getString(DATA, null);
        Log.v(TAG,"get data:"+data);

        Type listType = new TypeToken<List<WeatherData>>(){}.getType();
        List<WeatherData> list  = new Gson().fromJson(data,listType);

        if(list !=null) {
            for(int i =0;i<list.size();i++) {
                Log.v(TAG,"getdata:"+list.get(i).getCity());
            }
        }
        return list;
    }

    public MutableLiveData<List<WeatherData>> getweatherData() {
        String data =  mPref.getString(DATA, null);
        Log.v(TAG,"getweatherData data:"+data);
        MutableLiveData<List<WeatherData>> livedata = new MutableLiveData<>();
        Gson gson = new Gson();
//        livedata.setValue(gson.fromJson(data,WeatherData.class));


        java.lang.reflect.Type type = new TypeToken<List<WeatherData>>(){}.getType();
        List<WeatherData> list  = gson.fromJson(data,type);

        if(list !=null) {
            for(int i =0;i<list.size();i++) {
                Log.v(TAG,"getdata:"+list.get(i).getCity());
            }
        }

        livedata.setValue(list);
        return livedata;
    }

}
