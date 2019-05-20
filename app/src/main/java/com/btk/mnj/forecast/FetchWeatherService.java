package com.btk.mnj.forecast;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.btk.mnj.forecast.Model.WeatherData;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchWeatherService extends HandlerThread {

    private Handler fetchDataHandler = null;
    private Handler mMainHandler = null;
    private Context mContext = null;
    private static final String TAG =  FetchWeatherService.class.getSimpleName();

    public FetchWeatherService(String name, Context context) {
        super(name, Process.THREAD_PRIORITY_BACKGROUND);
        mMainHandler = new Handler(context.getMainLooper());
    }


    public void postTask(Runnable task) {
        fetchDataHandler.post(task);
    }

    public void prepapreHandler() {
        fetchDataHandler = new Handler(getLooper());

    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        fetchDataHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Bundle bundle = msg.getData();
                Double latitude =  (Double) bundle.get("latitude");
                Double longitude =  (Double) bundle.get("longitude");

//                fetchWeatherData(latitude,longitude);
                fetchData();
            }
        };
    }

    private void fetchData() {
        Log.v(TAG,"fetchData");
        double latitude=12.971599;
        double longitude=-77.594566;
        String forecastURL="https://api.darksky.net/forecast/" + "7e9e54f22038c6b245aceb3ab734c0ff" + "/" + latitude + "," + longitude;
        String coord = latitude+","+longitude;
        try{
            URL url = new URL(String.format((forecastURL), coord));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp;
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            Log.v(TAG,"jsondata-->"+data);

            Gson gson = new Gson();

            final WeatherData weatherData = gson.fromJson(json.toString(),WeatherData.class);

            Log.v(TAG,"Latitude-->"+ weatherData.getLatitude());

            Log.v(TAG,"Longitude-->"+ weatherData.getLongitude());

            Log.v(TAG,"Temp-->"+ weatherData.getCurrentWeatherData().getTemp());

            Log.v(TAG,"Hourly length-->"+ weatherData.getHourlyData().getHourlyWeatherData().length);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



/*    private void  fetchWeatherData(Double latitude,Double longitude) {
        Log.v(TAG,"Got fetchWeatherData");

//        double latitude=12.971599;
//        double longitude=-77.594566;
        String forecastURL="https://api.darksky.net/forecast/" + "7e9e54f22038c6b245aceb3ab734c0ff" + "/" + latitude + "," + longitude;

//        String forecastURL="https://api.darksky.net/forecast/" + "7e9e54f22038c6b245aceb3ab734c0ff" + "/" + latitude + "," + longitude;
        String coord = latitude+","+longitude;
        try {
            URL url = new URL(String.format((forecastURL), coord));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(2048);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();
            Gson gson = new Gson();
            final WeatherData weatherData = gson.fromJson(json.toString(),WeatherData.class);

            Log.v(TAG, "WeatherData-->"+ weatherData.getCurrentWeatherData().getSummary());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void postToQueue(String msg, Bundle data,DataFetchListner dataFetchListner) {
        Message message = fetchDataHandler.obtainMessage();
        message.setData(data);
        fetchDataHandler.sendMessage(message);
    }
}
