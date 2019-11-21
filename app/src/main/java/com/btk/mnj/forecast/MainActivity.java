package com.btk.mnj.forecast;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.btk.mnj.forecast.Model.WeatherData;
import com.btk.mnj.forecast.Util.PersistenceManager;
import com.btk.mnj.forecast.viewModel.MainViewModel;
import com.btk.mnj.forecast.viewModel.WeatherDataAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements WeatherDataAdapter.OnItemClickListener{

   @BindView(R.id.iv_add_city)
   ImageView mAddCity;

    private final String TAG  = MainActivity.class.getSimpleName();

    private final int ADD_CITY_LIST = 100;
    private final int LOCATION_PERMISSION = 100;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private List<WeatherData> cities;
    private WeatherDataAdapter mAdapter;
    private MainViewModel viewModel;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view_layout);
        ButterKnife.bind(this);
        PersistenceManager.init(this);
        requestLoctionPermission();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        cities = new ArrayList<WeatherData>();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("manju","onResume:");

       /* viewModel.getWeatherData().observe(this, new Observer<WeatherData>() {
            @Override
            public void onChanged(@Nullable WeatherData data) {
                Log.i("manju","onChanged cities size-->"+cities.size()+ ":data:"+data);

                for(int i =0;i<cities.size();i++) {
                    Log.i(TAG,"item-->"+cities.get(i).getCurrentWeatherData().getSummary());
                    Log.i(TAG,"item-->"+cities.get(i).getCity());
                    if(cities.get(i).getCity().equalsIgnoreCase(data.getCity())) {
                        return;
                    }
                }
                if(data!=null) {
                    cities.add(data);
                    Log.i(TAG,"Data!=null cities size"+ cities.size());
                    mAdapter.updateData(cities);
                }
            }
        });*/

       viewModel.getWeatherDataList().observe(this, new Observer<List<WeatherData>>() {
           @Override
           public void onChanged(@Nullable List<WeatherData> weatherData) {
               if(weatherData!=null) {
                   Log.v("manju","Length:"+weatherData.size());

                   for(int i =0;i<weatherData.size();i++) {
                       Log.v("manju","Length:"+weatherData.get(i).getCity());
                   }
                   mAdapter.updateData(weatherData);
               }
           }
       });
    }

    private void init() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_city_list);
        mAdapter =  new WeatherDataAdapter(new ArrayList<WeatherData>(0),this,this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void getCurrentLocation() {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        android.util.Log.v(TAG,"manju location-->"+location.getLatitude()+ "Longitude-->"+location.getLongitude()+ " Name:"+location.toString());
                        viewModel.fetchCurrentCityData(location.getLatitude(), location.getLongitude());
                    }
                }
            });

            mFusedLocationProviderClient.getLastLocation().addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.v(TAG,"Failed to get the current location");
                }
            });
    }

    private void requestLoctionPermission() {
        if((ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION);
                return;
        }
        getCurrentLocation();
//        requestCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
//                requestCurrentLocation();
            } else {
                Toast.makeText(this,"No location permission",Toast.LENGTH_LONG).show();
            }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == ADD_CITY_LIST) {
            if(data !=null) {
                String position = data.getStringExtra("position");
                String city = data.getStringExtra("city");

                Log.v(TAG,"Position:-->"+position);
                Log.v(TAG,"City:-->"+city);
                viewModel.fetchActualData(city);
            }
        }
    }

    @OnClick(R.id.iv_add_city)
    public void addCity() {
        Intent intent =  new Intent(this, CityList.class);
        startActivityForResult(intent,ADD_CITY_LIST);
    }

    @Override
    public void onItemClick(WeatherData weatherData, int position, View view) {
        Log.v(TAG,"onItemClick-->"+position);
        Intent intent = new Intent(MainActivity.this,WeatherDetailsActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                MainActivity.this,view,
                "weatherCardTransition");

        intent.putExtra("weatherData",  weatherData);
        startActivity(intent,options.toBundle());
    }

    private void CreateProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
        mProgressDialog.setContentView(R.layout.progress);
        mProgressDialog.show();
    }
}
