package com.btk.mnj.forecast;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.btk.mnj.forecast.Util.Utils;
import com.btk.mnj.forecast.databinding.ActivityCityListBinding;

public class CityList extends AppCompatActivity implements CityListAdapter.ClickHandler{

    private final String TAG = CityList.class.getSimpleName();
    private EditText mSearchBox;
    private String[] mCityList;
    private ActivityCityListBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_city_list);


        mBinding.cityListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        CityListAdapter cityListAdapter = new CityListAdapter(Utils.getCordinateMap());
        mBinding.cityListRecyclerview.setAdapter(cityListAdapter);

        mSearchBox = (EditText) findViewById(R.id.id_search);
//        mCityList = this.getResources().getStringArray(R.array.citylistarray);
//        CityListAdapter cityListAdapter = new CityListAdapter(mCityList);

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
        Log.v(TAG,"searchFilter-->"+str);
    }

    @Override
    public void onItemClick(int position, String city) {
        Log.v(TAG,"Position-->"+position +" City-->"+city);
//        Utils.addCitytoList(city);
//        Utils.getCityFromAddedCityList(city);
        Intent result   = new Intent();
        result.putExtra("position",position);
        result.putExtra("city",city);
        setResult(RESULT_OK,result);
        finish();
    }
}
