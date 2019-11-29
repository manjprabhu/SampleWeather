package com.btk.mnj.forecast.Network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static Retrofit retrofit = null;

    public static final String base_url = "https://api.darksky.net/";

    public static final String KEY = "7e9e54f22038c6b245aceb3ab734c0ff";

    private static OkHttpClient client;

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    public static Retrofit getRetrofitClient() {

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        if(client ==null) {
            client =  new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30,TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();
        }
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
