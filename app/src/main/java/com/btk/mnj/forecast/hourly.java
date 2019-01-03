package com.btk.mnj.forecast;

import com.google.gson.annotations.SerializedName;

public class hourly {

    @SerializedName("summary")
    private String hourlySummary;

    @SerializedName("icon")
    private String hourlyIcon;


    @SerializedName("data")
    private HourlyWeatherData[] hourlyWeatherData;

    public HourlyWeatherData[] getHourlyWeatherData() {
        return hourlyWeatherData;
    }

    public String getHourlyIcon() {
        return hourlyIcon;
    }

    public void setHourlyIcon(String hourlyIcon) {
        this.hourlyIcon = hourlyIcon;
    }

    public String getHourlySummary() {
        return hourlySummary;
    }

    public void setHourlySummary(String hourlySummary) {
        this.hourlySummary = hourlySummary;
    }

    public void setHourlyWeatherData(HourlyWeatherData[] hourlyWeatherData) {
        this.hourlyWeatherData = hourlyWeatherData;
    }
}
