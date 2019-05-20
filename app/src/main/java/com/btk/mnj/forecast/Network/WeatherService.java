package com.btk.mnj.forecast.Network;

import com.btk.mnj.forecast.Model.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {

//    https://api.darksky.net/forecast/7e9e54f22038c6b245aceb3ab734c0ff/37.8267,-122.4233

    @GET("/forecast/{apiKey}/{lat},{long}")
    Call<WeatherData> getData(@Path("apiKey") String key ,
                              @Path("lat") double latitude,
                              @Path("long") double longitude);
}
