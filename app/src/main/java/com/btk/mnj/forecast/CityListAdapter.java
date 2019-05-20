package com.btk.mnj.forecast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.citylistHolder> {

    private String[] mDataSet;
    private ClickHandler mClickHandler;

    public  CityListAdapter(String[] dataSet) {
        mDataSet = dataSet;
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
            textView =  (TextView) view.findViewById(R.id.id_list_item);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull citylistHolder citylistHolder, final int i) {
        citylistHolder.textView.setText(mDataSet[i]);

       citylistHolder.textView.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               mClickHandler.onItemClick(i,mDataSet[i]);
           }
       });
    }

    @NonNull
    @Override
    public citylistHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.city_list_item, viewGroup, false);

        TextView  textView = (TextView) view.findViewById(R.id.id_list_item);
        citylistHolder citylistHolder  = new citylistHolder(view);

        return  citylistHolder;

    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
