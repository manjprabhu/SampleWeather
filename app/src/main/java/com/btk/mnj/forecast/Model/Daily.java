package com.btk.mnj.forecast.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Daily implements Serializable {

    @SerializedName("summary")
    String dailySummary;

    @SerializedName("icon")
    String dailyIcon;

    @SerializedName("data")
    DailyWeatherData[] mDailyWeatherData;


    public String getDailySummary() {
        return dailySummary;
    }

    public String getDailyIcon() {
        return dailyIcon;
    }

    public DailyWeatherData[] getDailyWeatherData() {
        return mDailyWeatherData;
    }
}
