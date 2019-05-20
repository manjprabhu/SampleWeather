package com.btk.mnj.forecast.Model;

import com.google.gson.annotations.SerializedName;

public class DailyWeatherData {

    @SerializedName("summary")
    String dailyForecastSummay;

    @SerializedName("icon")
    String dailyForecastIcon;

    @SerializedName("time")
    private long dailyForecastTime;

    @SerializedName("temperatureHigh")
    private float dailyForecastTemp;

    @SerializedName("temperatureMax")
    private float dailyForecastMaxTemp;

    @SerializedName("temperatureMin")
    private float dailyForecastMinTemp;

    public float getDailyForecastMaxTemp() {
        return dailyForecastMaxTemp;
    }

    public float getDailyForecastMinTemp() {
        return dailyForecastMinTemp;
    }

    public float getDailyForecastTemp() {
        return dailyForecastTemp;
    }

    public long getDailyForecastTime() {
        return dailyForecastTime;
    }

    public String getDailyForecastSummay() {
        return dailyForecastSummay;
    }

    public String getDailyForecastIcon() {
        return dailyForecastIcon;
    }
}