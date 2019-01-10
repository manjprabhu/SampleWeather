package com.btk.mnj.forecast;

import com.google.gson.annotations.SerializedName;

public class WeatherData {

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("timezone")
    private String timeZone;

    @SerializedName("currently")
    private currently currentWeatherData;

    @SerializedName("hourly")
    private hourly hourlyData;

    @SerializedName("daily")
    private Daily dailyForecastData;

    public hourly getHourlyData() {
        return hourlyData;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public currently getCurrentWeatherData() {
        return currentWeatherData;
    }

    public Daily getDailyForecastData() {
        return dailyForecastData;
    }

    public void setCurrentWeatherData(currently currentWeatherData) {
        this.currentWeatherData = currentWeatherData;
    }

    public void setHourlyData(hourly hourlyData) {
        this.hourlyData = hourlyData;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}

