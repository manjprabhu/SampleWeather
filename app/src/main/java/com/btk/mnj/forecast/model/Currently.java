package com.btk.mnj.forecast.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Currently implements Serializable {

    @SerializedName("time")
    private long time;

    @SerializedName("icon")
    private String icon;

    @SerializedName("summary")
    private String summary;

    @SerializedName("temperature")
    private float temp;

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public long getTime() {
        return time;
    }

    public float getTemp() {
        return temp;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTemp(long temp) {
        this.temp = temp;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
