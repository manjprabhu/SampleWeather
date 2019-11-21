package com.btk.mnj.forecast.Util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static Retrofit retrofit = null;

    public static final String base_url = "https://api.darksky.net/";

    public static final String KEY = "7e9e54f22038c6b245aceb3ab734c0ff";

    public static Retrofit getRetrofitClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
