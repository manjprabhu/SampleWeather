package com.btk.mnj.forecast.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.btk.mnj.forecast.model.WeatherData;
import com.btk.mnj.forecast.network.WeatherService;
import com.btk.mnj.forecast.network.RetrofitClient;
import com.btk.mnj.forecast.utils.PersistenceManager;
import com.btk.mnj.forecast.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private final String TAG = MainViewModel.class.getSimpleName();
    private MutableLiveData<List<WeatherData>> listliveData = new MutableLiveData<List<WeatherData>>();
    private List<WeatherData> list = new ArrayList<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<List<WeatherData>> getWeatherDataList() {
        listliveData.setValue(PersistenceManager.getInstance().get());
        return listliveData;
    }

    public void fetchCurrentCityData(Double latitude, Double longitude) {
        Log.v(TAG,"fetchCurrentCityData");
        String city =  getLocationName(latitude,longitude);
        Log.v(TAG,"city::"+city);

        WeatherService weatherService = RetrofitClient.getRetrofitClient().create(WeatherService.class);
        weatherService.getData(Utils.KEY, latitude,longitude).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.isSuccessful()) {
                    Log.v(TAG,"fetchcurrent city data is success");
                    response.body().setCity(city);
                    writePersistence(response.body());
                }
            }
            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.v(TAG,"fetchcurrent city data is  failed:"+t.toString());
            }
        });
    }

    public void fetchActualData(String city) {
//        HashMap<String, Float> cordinates = getCordinateforCity(city);
        Utils.cordinates cordinates = Utils.getCityFromAddedCityList(city);

        if(cordinates ==null) {
            return;
        }
        double latitude =  cordinates.getLatitude();
        double longitude = cordinates.getLongitude();
//        float latitude =  cordinates.get("Latitude");
//        float longitude = cordinates.get("Longitude");
        Log.v(TAG," viewmodel latitude-->"+latitude +"longitude-->"+longitude);

        WeatherService weatherService = RetrofitClient.getRetrofitClient().create(WeatherService.class);
        weatherService.getData(Utils.KEY,latitude,longitude).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.isSuccessful()) {
                    Log.v(TAG," viewmodel onResponse-->"+response.body());
                    response.body().setCity(city);
                    writePersistence(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.v(TAG,"Viewmodel onFailure:"+t.toString());
            }
        });
    }

    public void updateData(WeatherData data) {
        Log.v(TAG,"UpdateData:");

        WeatherService weatherService = RetrofitClient.getRetrofitClient().create(WeatherService.class);
        weatherService.getData(Utils.KEY,data.getLatitude(),data.getLongitude()).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.isSuccessful()) {
                    Log.v(TAG,"UpdateData success:"+response.body());
                    response.body().setCity(data.getCity());
                    writePersistence(response.body());
                }
            }
            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.v(TAG,"UpdateData failure");
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
                Log.v(TAG,"writePersistence get:"+ weatherData.get(i).getCity());
                if(weatherData.get(i).getCity().equalsIgnoreCase(data.getCity()))
                    return;
            }
        }
        if(weatherData == null) {
            weatherData = new ArrayList<>();
        }

        weatherData.add(data);
        PersistenceManager.getInstance().putList(weatherData);
        Log.v(TAG,"writePersistence after add:"+list.size());
        listliveData.setValue(weatherData);
    }
}
