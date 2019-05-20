package com.btk.mnj.forecast;

import com.btk.mnj.forecast.Model.WeatherData;

public interface DataFetchListner {

    public void onSucess(String response, WeatherData weatherData);

    public void onFailure(String response);
}
