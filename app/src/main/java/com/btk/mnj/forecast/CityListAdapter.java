package com.btk.mnj.forecast;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btk.mnj.forecast.utils.Utils;

import java.util.HashMap;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.citylistHolder> {

//    private String[] mDataSet;
    private HashMap<String, Utils.cordinates> mDataSet;
    private ClickHandler mClickHandler;

    private Object[] mKeySet;
   /* public  CityListAdapter(String[] dataSet) {
        mDataSet = dataSet;
    }*/

    public CityListAdapter(HashMap<String,Utils.cordinates> dataSet) {
        mDataSet = dataSet;
        mKeySet = mDataSet.keySet().toArray();
    }

    public interface ClickHandler{
         void onItemClick(int position,String city);
    }

    public void setClickCustomClickListner(ClickHandler clickHandler) {
        this.mClickHandler =  clickHandler;
    }

    public static class citylistHolder extends RecyclerView.ViewHolder {
        TextView textView ;
        public  citylistHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.id_list_item);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull citylistHolder citylistHolder, final int i) {
//        citylistHolder.textView.setText(mDataSet[i]);
        citylistHolder.textView.setText(mKeySet[i].toString());
        citylistHolder.textView.setOnClickListener(v -> mClickHandler.onItemClick(i,mKeySet[i].toString()));
    }

    @NonNull
    @Override
    public citylistHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.city_list_item, viewGroup, false);
        citylistHolder citylistHolder  = new citylistHolder(view);
        return  citylistHolder;
    }

    @Override
    public int getItemCount() {
        return mKeySet.length;
    }
}
