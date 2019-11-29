package com.btk.mnj.forecast.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.btk.mnj.forecast.Model.WeatherData;
import com.btk.mnj.forecast.Network.WeatherService;
import com.btk.mnj.forecast.Network.API;
import com.btk.mnj.forecast.Util.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private final String TAG = MainViewModel.class.getSimpleName();
    private MutableLiveData<WeatherData> liveData = new MutableLiveData<WeatherData>();
    private MutableLiveData<List<WeatherData>> listliveData = new MutableLiveData<List<WeatherData>>();
    private List<WeatherData> list = new ArrayList<>();
    private HashSet<WeatherData> dataSet = new HashSet<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<WeatherData> getWeatherData() {
//        liveData = PersistenceManager.getInstance().getweatherData();
        return liveData;
    }

    public MutableLiveData<List<WeatherData>> getWeatherDataList() {
//        liveData = PersistenceManager.getInstance().getweatherData();
//        listliveData = PersistenceManager.getInstance().getweatherData();
        listliveData.setValue(PersistenceManager.getInstance().get());
        return listliveData;
    }

    public void fetchCurrentCityData(Double latitude, Double longitude) {
        Log.v(TAG,"fetchCurrentCityData");

        String city =  getLocationName(latitude,longitude);

        Log.v("manju","city::"+city);

        WeatherService weatherService = API.getRetrofitClient().create(WeatherService.class);

        weatherService.getData(API.KEY, latitude,longitude).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                response.body().setCity(city);
//                PersistenceManager.getInstance().put(response.body());
                writePersistence(response.body());
//                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
            }
        });
    }

    public void fetchActualData(String city) {
        HashMap<String, Float> cordinates = getCordinateforCity(city);
        WeatherService weatherService = API.getRetrofitClient().create(WeatherService.class);

        if(cordinates ==null) {
            return;
        }

        float latitude =  cordinates.get("Latitude");
        float longitude = cordinates.get("Longitude");
        Log.v(TAG," viewmodel latitude-->"+latitude +"longitude-->"+longitude);

        weatherService.getData(API.KEY,latitude,longitude).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                Log.v(TAG," viewmodel onResponse-->"+response.body());

                response.body().setCity(city);
//                Log.v("manju","get:"+PersistenceManager.getInstance().get().getCity());
//                PersistenceManager.getInstance().put(response.body());
                writePersistence(response.body());
//                liveData.setValue(response.body());
//                dataMap.put(city,response.body());
//                saveData(city,response.body());
//
//                data.add(response.body());
//                listMutableLiveData.setValue(data);
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
            }
        });
    }

    private HashMap<String, Float> getCordinateforCity(String city) {
        if(Geocoder.isPresent()){
            try {
                Geocoder gc = new Geocoder(getApplication());
                List<Address> addresses= gc.getFromLocationName(city, 5);
                HashMap<String,Float> cordinatesMap =  new HashMap<>();

                Log.i(TAG,"getCordinatesforCity:"+city + ":address:"+addresses);

                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        cordinatesMap.put("Latitude",(float)a.getLatitude());
                        cordinatesMap.put("Longitude",(float)a.getLongitude());
                        return cordinatesMap;
                    }
                }
            } catch (IOException e) {
                Log.i(TAG,"Exception-->");
            }
        }
        return null;
    }

    private String getLocationName(Double latitude, Double longitude) {
        List<Address> address;
        if(Geocoder.isPresent()) {
            Geocoder gc = new Geocoder(getApplication());
            try {
                address = gc.getFromLocation(latitude,longitude,1);
                return address.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void writePersistence(WeatherData data) {
        Log.v(TAG,"writePersistence:"+ data.getCity());
        List<WeatherData> weatherData = PersistenceManager.getInstance().get();

        if(weatherData!=null) {
            for(int i =0;i<weatherData.size();i++) {
                Log.v("manju","writePersistence get:"+ weatherData.get(i).getCity());
                if(weatherData.get(i).getCity().equalsIgnoreCase(data.getCity()))
                    return;
            }
        }
        if(weatherData == null) {
            weatherData = new ArrayList<>();
        }

        weatherData.add(data);
        PersistenceManager.getInstance().putList(weatherData);
        Log.v("manju","writePersistence after add:"+list.size());
        listliveData.setValue(weatherData);
    }
}
