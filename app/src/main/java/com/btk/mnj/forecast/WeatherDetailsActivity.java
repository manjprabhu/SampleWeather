package com.btk.mnj.forecast;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.btk.mnj.forecast.Model.WeatherData;
import com.btk.mnj.forecast.adapters.WeatherDetailsAdapter;
import com.btk.mnj.forecast.databinding.WeatherDetailsActivityBinding;

public class WeatherDetailsActivity extends AppCompatActivity {

    private WeatherDetailsActivityBinding binding;
    private WeatherData weatherData;
    private RecyclerView mRecyclerView;
    private WeatherDetailsAdapter mDetailsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.weather_details_activity);
        Bundle bundle = getIntent().getExtras();
        if(! bundle.isEmpty()){
            weatherData = (WeatherData) bundle.getSerializable("weatherData");
        }
       init();
    }

    public void init() {
        binding.setWeatherdata(weatherData);
        binding.setCurrentdata(weatherData.getCurrentWeatherData());
        mDetailsAdapter = new WeatherDetailsAdapter(weatherData);
        binding.rvDetailsForecast.setLayoutManager(new LinearLayoutManager(this));
//        binding.rvDetailsForecast.setAdapter(mDetailsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        supportFinishAfterTransition();
    }
}
