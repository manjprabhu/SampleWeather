package com.btk.mnj.forecast.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherData implements Serializable {

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("timezone")
    private String timeZone;

    @SerializedName("currently")
    private Currently currentWeatherData;

    @SerializedName("hourly")
    private Hourly hourlyData;

    @SerializedName("daily")
    private Daily dailyForecastData;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String city;

    public Hourly getHourlyData() {
        return hourlyData;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public Currently getCurrentWeatherData() {
        return currentWeatherData;
    }

    public Daily getDailyForecastData() {
        return dailyForecastData;
    }

    public void setCurrentWeatherData(Currently currentWeatherData) {
        this.currentWeatherData = currentWeatherData;
    }

    public void setHourlyData(Hourly hourlyData) {
        this.hourlyData = hourlyData;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }


}

