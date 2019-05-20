package com.btk.mnj.forecast;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btk.mnj.forecast.Model.WeatherData;
import com.btk.mnj.forecast.Network.WeatherService;
import com.btk.mnj.forecast.Util.API;
import com.btk.mnj.forecast.Util.PersistenceManager;
import com.btk.mnj.forecast.Util.Util;
import com.btk.mnj.forecast.ViewModel.MainViewModel;
import com.btk.mnj.forecast.ViewModel.WeatherDataAdapter;
import com.btk.mnj.forecast.databinding.MainViewLayoutBinding;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DataFetchListner{

/*    @BindView(R.id.id_temp)
    TextView temp;

    @BindView(R.id.id_summary)
    TextView summary;

    @BindView(R.id.id_city)
    TextView city;*/


  /*  @BindView(R.id.id_weather_icon)
    ImageView mWeatherIcon;
*/
   /* @BindView(R.id.id_current_temp)
    TextView mCurrentCityTemp;

    @BindView(R.id.id_temp_summary)
    TextView mTempSummary;*/

   /* @BindView(R.id.id_add_city)
    Button mAddCityButton;*/

   @BindView(R.id.iv_add_city)
   ImageView mAddCity;



    private final String TAG  = MainActivity.class.getSimpleName();
    private final String KEY = "7e9e54f22038c6b245aceb3ab734c0ff";

    private final int ADD_CITY_LIST = 100;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private DataFetchListner dataFetchListner;
    FetchWeatherService fetchWeatherService;
    private final int LOCATION_PERMISSION = 100;
    private List<WeatherData> cities;
    private WeatherDataAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MainViewLayoutBinding binding;
    private MainViewModel viewModel;
    fetchDataThread fetchThread;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PersistenceManager.init(this);
        setContentView(R.layout.main_view_layout);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        cities = new ArrayList<WeatherData>();

//        binding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.main_view_layout,null,false);

        /*fetchWeatherService = new FetchWeatherService("FetchWeatherService",this);
        fetchWeatherService.start();
        fetchWeatherService.prepapreHandler();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.main_view_layout);
        ButterKnife.bind(this);

        if(checkLocationPermission()) {
            getCurrentLocation();
        } else {
            requestLoctionPermission();
        }
        fetchWeather();*/
        fetchWeather();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        mTypeface = Util.getXLightTypeface(this);
        fetchThread = new fetchDataThread();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_city_list);
        mAdapter =  new WeatherDataAdapter(new ArrayList<WeatherData>(0));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

//        cities = new ArrayList<WeatherData>();
//        viewModel.init();
//        viewModel.getWeatherData().observe(this, res -> updateData(res));

      /*  viewModel.getDataList().observe(this, new Observer<List<WeatherData>>() {
            @Override
            public void onChanged(@Nullable List<WeatherData> weatherData) {
                mAdapter.updateData(weatherData);
                mAdapter.notifyDataSetChanged();
            }
        });*/

        viewModel.getWeatherData().observe(this, new Observer<WeatherData>() {
            @Override
            public void onChanged(@Nullable WeatherData data) {
                Log.i("manju","onChanged cities size-->"+cities.size());

                for(int i =0;i<cities.size();i++) {
                    Log.i("manju","item-->"+cities.get(i).getCurrentWeatherData().getSummary());
                    Log.i("manju","item-->"+cities.get(i).getCity());
                }
                if(data!=null) {
//                    Log.i("manju","onChanged"+data.getCity());
                    cities.add(data);
                    mAdapter.updateData(cities);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void updateData(WeatherData data) {
        Log.v("manju","updateData-->"+data.getCurrentWeatherData().getTemp());
        if(data.getCurrentWeatherData() != null) {
            cities.add(data);
            mAdapter.updateData(cities);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void fetchWeather() {



//    thread.start();
//        Log.v(TAG,"start fetchWeather");
//        fetchThread.start();
    }

    private void getCurrentLocation() {
        if(checkLocationPermission()) {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    android.util.Log.v(TAG,"location-->"+location.getLatitude()+ "Longitude-->"+location.getLongitude());

                    Bundle bundle = new Bundle();
                    bundle.putDouble("latitude",location.getLatitude());
                    bundle.putDouble("longtitude",location.getLongitude());

//                    fetchWeatherService.postToQueue("currentlocation",bundle,dataFetchListner);
//                    thread.start();
//                    fetchThread.start();
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

            WeatherService weatherService = API.getRetrofitClient().create(WeatherService.class);

            weatherService.getData(API.KEY,latitude,longitude).enqueue(new Callback<WeatherData>() {
                @Override
                public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                    Log.v("manju","onResponse-->"+response.body().getCurrentWeatherData().getSummary());

//                    viewModel.saveData(response.body());
//                    updateData(response.body());

//                    cities.add(response.body());
//                    mAdapter.updateData(cities);
//                    viewModel.saveData(response.body());
                }

                @Override
                public void onFailure(Call<WeatherData> call, Throwable t) {

                }
            });
            /*double latitude=37.8267;
            double longitude=-122.4233;
            String forecastURL="https://api.darksky.net/forecast/" + KEY + "/" + latitude + "," + longitude;

            Log.v("manju","forecastUrl-->"+forecastURL);

            HttpURLConnection httpURLConnection = null;
            URL url;
            try {
                url = new URL(forecastURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                if(httpURLConnection.getResponseCode() == RESULT_OK) {
                    String response = readInputStream(httpURLConnection.getInputStream());
                    Log.v(TAG,"response");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }
    });


//    Thread fetchThread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            fetchData("");
//        }
//    });

    private String readInputStream(InputStream stream) {
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder response = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException ", e);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        } finally {
            try {
                if(stream!=null)
                    stream.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            }
        }
        Log.v(TAG,"Response readInputStream-->"+response.toString());
        return response.toString();
    }

    private void fetchData(String city) {
        Log.v(TAG,"fetchData");
        double latitude=  Utils.getCityFromAddedCityList(city).getLatitude();//12.971599;
        double longitude=Utils.getCityFromAddedCityList(city).getLongitude();//77.594566;


        String forecastURL="https://api.darksky.net/forecast/" + "7e9e54f22038c6b245aceb3ab734c0ff" + "/" + latitude + "," + longitude;
        Log.v("manju","URL:"+forecastURL);
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

            Log.v(TAG,"WeatherData-->"+ weatherData.getLatitude());

            Log.v(TAG,"WeatherData-->"+ weatherData.getLongitude());

            Log.v(TAG,"Summary-->"+ weatherData.getCurrentWeatherData().getTemp());

            Log.v(TAG,"Hourly length-->"+ weatherData.getHourlyData().getHourlyWeatherData().length);

            Log.v(TAG,"daily"+ weatherData.getDailyForecastData().getmDailyWeatherData().length);
            Log.v(TAG,"daily"+ weatherData.getDailyForecastData().getDailySummary());


            for(int i = 0; i< weatherData.getDailyForecastData().getmDailyWeatherData().length; i++) {
                Log.v(TAG,"First-->"+ weatherData.getDailyForecastData().getmDailyWeatherData()[i].getDailyForecastSummay());
            }

            for(int i = 0; i< weatherData.getHourlyData().getHourlyWeatherData().length; i++) {
                Log.v(TAG,"Time-->"+ weatherData.getHourlyData().getHourlyWeatherData()[i].getTime());
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    city.setText("Bangalore");
//                    temp.setText("Temp:"+convertFahrenheitToCelcius(weatherData.getCurrentWeatherData().getTemp()));
//                    summary.setText("Summary"+ weatherData.getCurrentWeatherData().getSummary());

//                    mTempSummary.setTypeface(Utils.getTypeface(MainActivity.this));
//                    mCurrentCityTemp.setTypeface(Utils.getTypeface(MainActivity.this));
//                    mTempSummary.setText(weatherData.getCurrentWeatherData().getSummary());
//                    mCurrentCityTemp.setText(""+weatherData.getCurrentWeatherData().getTemp());
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

    private float convertFahrenheitToCelcius(float fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    private float convertCelciusToFahrenheit(float celsius) {
        return ((celsius * 9) / 5) + 32;
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

   /* @OnClick(R.id.id_add_city)
    public void Addcity() {
        Log.v("manju","Add city clicked");
        Intent intent = new Intent();
        intent.setClass(this,CityList.class);
        this.startActivityForResult(intent,ADD_CITY_LIST);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == ADD_CITY_LIST) {

            if(data !=null) {
                String position = data.getStringExtra("position");
                String city = data.getStringExtra("city");

                Log.v("manju","Position:-->"+position);
                Log.v("manju","City:-->"+city);
                viewModel.fetchActualData(city);
//                fetchData(city);
//                Message message = Message.obtain();
//                Bundle bundle = new Bundle();
//                bundle.putString("CITY",city);
//                message.setData(bundle);
//
//                fetchThread.handler.sendMessage(message);
//                fetchThread.start();
            }
        }
    }

    @Override
    public void onSucess(String response, WeatherData weatherData) {
    }

    @Override
    public void onFailure(String response) {
    }

    @OnClick(R.id.iv_add_city)
    public void addCity() {
        Intent intent =  new Intent(this, CityList.class);
        startActivityForResult(intent,ADD_CITY_LIST);
    }

    private HashMap<String, Float> getCordinateforCity(String city) {

        if(Geocoder.isPresent()){
            try {
                String location = city;
                Geocoder gc = new Geocoder(this);
                List<Address> addresses= gc.getFromLocationName(location, 5);
                HashMap<String,Float> cordinatesMap =  new HashMap<>();

                Log.i("manju","getCordinateforCity:"+city + ":address:"+addresses);

                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        cordinatesMap.put("latitude",(float)a.getLatitude());
                        cordinatesMap.put("longitude",(float)a.getLongitude());
                        return cordinatesMap;
                    }
                }
            } catch (IOException e) {

            }
        }
        return null;
    }



    public class fetchDataThread extends Thread {
        private String city;
        HashMap<String,Float> cordinates = new HashMap<>();
        protected  Handler handler;

        public fetchDataThread() {
             handler =  new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {

                    Bundle bundle = msg.getData();
                    city = bundle.getString("CITY");
                    cordinates = getCordinateforCity(city);
                    fetchActualData(cordinates,city);
                }
            };
        }


        @Override
        public void run() {
//            fetchActualData(cordinates,city);
        }
    }

    private void fetchActualData(HashMap<String, Float> cordinates,String city) {
        WeatherService weatherService = API.getRetrofitClient().create(WeatherService.class);

        float latitude =  cordinates.get("latitude");
        float longitude = cordinates.get("longitude");

        Log.v("manju","latitude-->"+latitude +"longitude-->"+longitude);

        weatherService.getData(API.KEY,latitude,longitude).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {

//                updateData(response.body());
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });
    }


}
