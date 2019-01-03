package com.btk.mnj.forecast;

import com.google.gson.annotations.SerializedName;

public class HourlyWeatherData {

    @SerializedName("time")
    private long time;

    @SerializedName("summary")
    private String summary;

    @SerializedName("temperature")
    private float temp;

    @SerializedName("humidity")
    private float humidity;

    @SerializedName("icon")
    private String icon;

    public float getHumidity() {
        return humidity;
    }

    public float getTemp() {
        return temp;
    }

    public long getTime() {
        return time;
    }

    public String getIcon() {
        return icon;
    }

    public String getSummary() {
        return summary;
    }

    public void setHumidity(long humidity) {
        this.humidity = humidity;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTemp(long temp) {
        this.temp = temp;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
