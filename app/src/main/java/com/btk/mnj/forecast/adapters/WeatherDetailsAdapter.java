package com.btk.mnj.forecast.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btk.mnj.forecast.model.WeatherData;
import com.btk.mnj.forecast.R;
import com.btk.mnj.forecast.utils.Utils;

public class WeatherDetailsAdapter extends RecyclerView.Adapter<WeatherDetailsAdapter.ForecastViewHolder> {

    WeatherData weatherData;

    public WeatherDetailsAdapter(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_details_item,null,false);
        return new WeatherDetailsAdapter.ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDetailsAdapter.ForecastViewHolder forecastViewHolder, int i) {
        forecastViewHolder.summary.setText(weatherData.getDailyForecastData().getDailyWeatherData()[i].getDailyForecastSummay());
        forecastViewHolder.dailyMaxTemp.setText(convertFahrenheitToCelcius(weatherData.getDailyForecastData().getDailyWeatherData()[i].getDailyForecastMaxTemp()));
        forecastViewHolder.dailyMinTemp.setText(convertFahrenheitToCelcius(weatherData.getDailyForecastData().getDailyWeatherData()[i].getDailyForecastMinTemp()));
        forecastViewHolder.dailyDay.setText(Utils.getForecastDay(weatherData.getDailyForecastData().getDailyWeatherData()[i].getDailyForecastTime()));
    }

    @Override
    public int getItemCount() {
        return weatherData.getDailyForecastData().getDailyWeatherData().length;
    }


    class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView dailyMaxTemp,dailyMinTemp,dailyDay,summary;

        public  ForecastViewHolder(View view) {
            super(view);
            dailyDay = (TextView) view.findViewById(R.id.tv_daily_day);
            dailyMaxTemp = (TextView) view.findViewById(R.id.tv_daily_max_temp);
            dailyMinTemp = (TextView) view.findViewById(R.id.tv_daily_min_temp);
            summary = (TextView) view.findViewById(R.id.tv_daily_description);
        }
    }

    private String convertFahrenheitToCelcius(float fahrenheit) {
        String s = Math.round((fahrenheit - 32) * 5 / 9) +"";
        return s;
    }
}
