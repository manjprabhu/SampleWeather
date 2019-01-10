package com.btk.mnj.forecast;

public interface DataFetchListner {

    public void onSucess(String response,WeatherData weatherData);

    public void onFailure(String response);
}
