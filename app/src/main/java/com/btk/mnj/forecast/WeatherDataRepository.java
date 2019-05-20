package com.btk.mnj.forecast;

public class WeatherDataRepository {

    private static WeatherDataRepository INSTANCE;

    private WeatherDataRepository() {
    }

    public static WeatherDataRepository getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new WeatherDataRepository();
        }
        return INSTANCE;
    }

}
