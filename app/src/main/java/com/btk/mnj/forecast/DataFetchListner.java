package com.btk.mnj.forecast;

public interface DataFetchListner {

    public void onSucess(String response,DailyData dailyData);

    public void onFailure(String response);
}
