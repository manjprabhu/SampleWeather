package com.example.prabhum.sampleweather;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

import com.google.gson.Gson;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchWeatherService extends HandlerThread {

    private Handler fetchDataHandler = null;

    public FetchWeatherService(String name) {
        super(name, Process.THREAD_PRIORITY_BACKGROUND);
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

                fetchWeatherData(latitude,longitude);
            }
        };
    }

    private void  fetchWeatherData(Double latitude,Double longitude) {
        String forecastURL="https://api.darksky.net/forecast/" + "7e9e54f22038c6b245aceb3ab734c0ff" + "/" + latitude + "," + longitude;
        String coord = latitude+","+longitude;
        try {
            URL url = new URL(String.format((forecastURL), coord));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();
            Gson gson = new Gson();
            final DailyData  dailyData = gson.fromJson(json.toString(),DailyData.class);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postToQueue(String msg, Bundle data,DataFetchListner dataFetchListner) {
        Message message = fetchDataHandler.obtainMessage();
        message.setData(data);
        fetchDataHandler.sendMessage(message);
    }
}
