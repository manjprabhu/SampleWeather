package com.btk.mnj.forecast;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.btk.mnj.forecast.ViewModel.MainViewModel;

public class CityList extends AppCompatActivity implements CityListAdapter.ClickHandler{

    RecyclerView cityListView ;

    EditText mSearchBox;

    private String[] mCityList;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("manju","onCreate of CityList ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        viewModel.init();

        mCityList = this.getResources().getStringArray(R.array.citylistarray);

        mSearchBox = (EditText) findViewById(R.id.id_search);

        cityListView = (RecyclerView) findViewById(R.id.city_list_recyclerview);

        cityListView.setLayoutManager(new LinearLayoutManager(this));

        CityListAdapter cityListAdapter = new CityListAdapter(mCityList);

        cityListView.setAdapter(cityListAdapter);

        cityListAdapter.setClickCustomClickListner(this);

        init();

    }

    private void init() {
        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchFilter(s.toString());

            }
        });
    }

    private void searchFilter(String str) {

        Log.v("manju","searchFilter-->"+str);

    }


    @Override
    public void onItemClick(int position, String city) {
        Log.v("manju","Position-->"+position +" City-->"+city);
        Utils.addCitytoList(city);
        Intent result   = new Intent();
        result.putExtra("position",position);
        result.putExtra("city",city);
        setResult(RESULT_OK,result);
//        viewModel.setcity(city);
        finish();

//        viewModel.fetchActualData(city);
    }

}
