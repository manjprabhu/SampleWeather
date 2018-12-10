package com.example.prabhum.sampleweather;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DataFetchListner{

    @BindView(R.id.id_temp)
    TextView temp;

    @BindView(R.id.id_summary)
    TextView summary;

    @BindView(R.id.id_city)
    TextView city;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private DataFetchListner dataFetchListner;
    FetchWeatherService fetchWeatherService;
    private final int LOCATION_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchWeatherService = new FetchWeatherService("FetchWeatherService");
        fetchWeatherService.start();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(checkLocationPermission()) {
            getCurrentLocation();
        } else {
            requestLoctionPermission();
        }
//        fetchWeather();
    }


    private void fetchWeather() {
//    thread.start();
        Log.v("manju","start fetchWeather");
        fetchThread.start();
    }

    private void getCurrentLocation() {
        if(checkLocationPermission()) {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    android.util.Log.v("manju","location-->"+location.getLatitude()+ "Longitude-->"+location.getLongitude());

                    Bundle bundle = new Bundle();
                    bundle.putDouble("latitude",location.getLatitude());
                    bundle.putDouble("longtitude",location.getLongitude());

                    fetchWeatherService.postToQueue("currentlocation",bundle,dataFetchListner);
                }
            });
        }

    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLoctionPermission() {
        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_PERMISSION) {

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this,"No location permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    Thread thread  = new Thread(new Runnable() {
        @Override
        public void run() {
            double latitude=37.8267;
            double longitude=-122.4233;
            String forecastURL="https://api.darksky.net/forecast/" + "7e9e54f22038c6b245aceb3ab734c0ff" + "/" + latitude + "," + longitude;

            HttpURLConnection httpURLConnection = null;
            URL url = null;
            try {
                url = new URL(forecastURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                if(httpURLConnection.getResponseCode() == RESULT_OK) {
                    String response = readInputStream(httpURLConnection.getInputStream());
                    Log.v("manju","response");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    });


    Thread fetchThread = new Thread(new Runnable() {
        @Override
        public void run() {
            fetchData();
        }
    });

    private String readInputStream(InputStream stream) {
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder response = new StringBuilder();

        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            Log.e("manju", "IOException ", e);
        } catch (Exception e) {
            Log.e("manju", "Exception", e);
        } finally {
            try {
                if(stream!=null)
                    stream.close();
            } catch (IOException e) {
                Log.e("manju", "IOException", e);
            }
        }
        Log.v("manju","Response readInputStream-->"+response.toString());
        return response.toString();
    }


    private void fetchData() {
        Log.v("manju","fetchData");
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
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            Log.v("manju","jsondata-->"+data);

            Gson gson = new Gson();

            final DailyData  dailyData = gson.fromJson(json.toString(),DailyData.class);

            Log.v("manju","DailyData-->"+dailyData.getLatitude());

            Log.v("manju","DailyData-->"+dailyData.getLongitude());

            Log.v("manju","Summary-->"+dailyData.getCurrentWeatherData().getTemp());

            Log.v("manju","Hourly length-->"+dailyData.getHourlyData().getHourlyWeatherData().length);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    temp.setText(""+dailyData.getCurrentWeatherData().getTemp());
                    summary.setText(""+dailyData.getCurrentWeatherData().getSummary());
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean isNetworkAvailable() {
        boolean isAvailable = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo() !=null
            && connectivityManager.getActiveNetworkInfo().isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    public void onSucess(String response, DailyData dailyData) {

    }

    @Override
    public void onFailure(String response) {

    }
}
