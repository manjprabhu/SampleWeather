package com.btk.mnj.forecast.viewModel;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btk.mnj.forecast.Model.WeatherData;
import com.btk.mnj.forecast.R;
import com.btk.mnj.forecast.Util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherDataAdapter extends RecyclerView.Adapter<WeatherDataAdapter.WeatherDataHolder> {

    private final String TAG = WeatherDataAdapter.class.getSimpleName();

    private List<WeatherData> cities;
    private OnItemClickListener onItemClickListener;
    private Activity activity;

    public WeatherDataAdapter(List<WeatherData> cities,OnItemClickListener OnItemClickListener, Activity activity) {
        this.cities = cities;
        this.onItemClickListener = OnItemClickListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public WeatherDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.citites_weather_card,null,false);
        return new WeatherDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDataHolder weatherDataHolder, int i) {
        Log.v(TAG,"onBindViewHolder-->"+ i);
        weatherDataHolder.cityName.setText(cities.get(i).getCity());
        weatherDataHolder.currentTemp.setText(Utils.convertFahrenheitToCelcius(cities.get(i).getCurrentWeatherData().getTemp())+(char) 0x00B0 + "c");
        weatherDataHolder.description.setText(cities.get(i).getCurrentWeatherData().getSummary());
        weatherDataHolder.maxTemp.setText(Utils.convertFahrenheitToCelcius(cities.get(i).getDailyForecastData().getDailyWeatherData()[i].getDailyForecastMaxTemp())+ "" +(char) 0x00B0 + "c");
        weatherDataHolder.minTemp.setText(Utils.convertFahrenheitToCelcius(cities.get(i).getDailyForecastData().getDailyWeatherData()[i].getDailyForecastMinTemp())+ "" +(char) 0x00B0 + "c");

        Picasso.with(activity).load(Utils.getForecastIcon(cities.get(i).getCurrentWeatherData().getSummary())).into(weatherDataHolder.forecastIcon);
        weatherDataHolder.cardView.setOnClickListener(v -> onItemClickListener.onItemClick(cities.get(i), weatherDataHolder.getAdapterPosition(),weatherDataHolder.cardView));
    }

    @Override
    public int getItemCount() {
        return this.cities.size();
    }

    class WeatherDataHolder extends RecyclerView.ViewHolder {
        TextView cityName,description,currentTemp,maxTemp,minTemp;
        ImageView forecastIcon;
        CardView cardView;

        public WeatherDataHolder(View view) {
            super(view);

            cardView = view.findViewById(R.id.id_WeatherCard);
            cityName = (TextView)view.findViewById(R.id.tv_card_city_name);
            description = (TextView) view.findViewById(R.id.tv_card_description);
            currentTemp = (TextView) view.findViewById(R.id.tv_card_current_temp);
            maxTemp = (TextView) view.findViewById(R.id.tv_card_max_temp);
            minTemp = (TextView) view.findViewById(R.id.tv_card_min_temp);
            forecastIcon = (ImageView) view.findViewById(R.id.iv_card_weather_icon);
        }
    }

    public void updateData(List<WeatherData> data) {
        this.cities = data;
        notifyDataSetChanged();
        Log.v("MainActivity","updateData-->"+cities.size()+ "  " +cities.get(0).getCurrentWeatherData().getSummary());
    }

    public void updateItem(WeatherData data){
        this.cities.add(cities.size(),data);
        notifyItemInserted(cities.size());
        Log.v(TAG,"updateItem-->"+cities.size()+ "  " +cities.get(0).getCurrentWeatherData().getSummary());
    }

    public interface OnItemClickListener {
        void onItemClick(WeatherData weatherData , int position,View view);
    }
}
